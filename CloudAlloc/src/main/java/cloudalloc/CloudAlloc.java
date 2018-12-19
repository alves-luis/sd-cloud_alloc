/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author o Grupo
 */
public class CloudAlloc {

  /* Key -> clouds | Value -> Map of Id->Cloud */
  private final Map<String,Map<String,Cloud>> cloudMap;
  private final Map<String,User> userByCloudId;
  private ReentrantLock cloudLock;
  
  /* Conditions according to type of Cloud, when it's available */
  private final Map<String,Condition> cloudsAvailable;

  /* Counter for no repetition of ids */
  private final Counter nextId;

  /* Auctions running */
  /* Key -> clouds | Value -> Ordered Map of Auction Value -> User who made it */
  private final Map<String,TreeMap<Double,User>> auctionsMap;
  private ReentrantLock auctionLock;

  /* Map of users by e-mail */
  private final Map<String,User> users;
  private ReentrantLock userLock;

  public CloudAlloc(){

    this.cloudMap = new HashMap<>();
    this.userByCloudId = new HashMap<>();
    this.cloudsAvailable = new HashMap<>();
    this.auctionsMap = new HashMap<>();
    this.users = new HashMap<>();
    this.cloudLock = new ReentrantLock();
    this.auctionLock = new ReentrantLock();
    this.userLock = new ReentrantLock();
    this.nextId = new Counter();
    
    CloudTypes.getNames().forEach((n) -> { 
      cloudsAvailable.put(n,cloudLock.newCondition());
      auctionsMap.put(n,new TreeMap<>(Comparator.reverseOrder()));
      cloudMap.put(n, new HashMap<>());
    });
  }

  public String requestCloud(User u, String type){
    boolean foundOne = false;
    Map<String,Cloud> clouds = this.cloudMap.get(type);
    String id = type + "_" + nextId.getId();
    Cloud c = new Cloud(id,type,CloudTypes.getPrice(type),false);
    
    try {
      cloudLock.lock();
      // if no clouds available, search for auctioned ones
      if(clouds.size()>= CloudTypes.maxSize(type))
        for (Cloud d: clouds.values())
          if (d.isAuctioned()){
            this.freeCloud(null,d.getId());
            foundOne=true;
            break;
          }
      
      // if not found an auctioned one, go ZZZzzZZ
      if(!foundOne)
        while(clouds.size()>= CloudTypes.maxSize(type))
          try {
            this.cloudsAvailable.get(type).await();
          } catch (InterruptedException e) {}
      
      clouds.put(id,c);
      this.userByCloudId.put(id, u);
    }
    catch (InexistentCloudException | UserDoesNotOwnCloudException e){
      System.out.println(e.getMessage());
    }
    finally{
      cloudLock.unlock();
    }
    
    u.addCloud(c);
    return id;
  }

  /**
   *
   * @param u
   * @param type
   * @param value
   * @return Id of the Cloud who was auctioned
   */
  public String auctionCloud(User u, String type, double value) {
    Map<String,Cloud> usedClouds = this.cloudMap.get(type);
    TreeMap<Double,User> auctionClouds = this.auctionsMap.get(type);
    Condition available = this.cloudsAvailable.get(type);
    int id = nextId.getId();
    String typeId = type + "_" + id;
    Cloud c = new Cloud(typeId,type,value,true);
    
    try {
      cloudLock.lock();
      
      if (usedClouds.size() >= CloudTypes.maxSize(type)) {
        auctionClouds.put(value, u);
        // while no clouds available and not the first in queue, go ZZZzzzZZZ
        while (usedClouds.size() >= CloudTypes.maxSize(type) || !(auctionClouds.firstEntry().getValue().equals(u) && auctionClouds.firstEntry().getKey().equals(value))) {
          try {
            available.await();
          }
          catch (InterruptedException e) {}
        }
      }
      usedClouds.put(typeId,c);
      this.userByCloudId.put(typeId,u);
      auctionClouds.remove(value,u);
    }
    finally {
      cloudLock.unlock();
    }
    u.addCloud(c);
    return typeId;
  }

  /**
   * This method clears a Cloud. If User is null, that means that is the system freeing a Cloud
   * @param u User who wants to free the cloud
   * @param id id of the cloud that will be freed
   * @throws InexistentCloudException if tried to free a Cloud that does not exist in the system
   * @throws UserDoesNotOwnCloudException if user who tried to free the Cloud does not own it
  */
  public void freeCloud(User u, String id) throws InexistentCloudException, UserDoesNotOwnCloudException{
    Map<String,Cloud> usedClouds = this.cloudMap.get(typeFromId(id));
    if (usedClouds == null)
      throw new InexistentCloudException(typeFromId(id));
    Cloud c = null;
    User owner = null;
    try {
      cloudLock.lock();
      c = usedClouds.get(id);
    }
    finally{
      cloudLock.unlock();
    }
    if (c == null)
        throw new InexistentCloudException(id);
    
    String type = c.getType();
    Condition available = cloudsAvailable.get(type);
    try {
      cloudLock.lock();
      if (u != null && !u.isMyCloud(id)) // if not system and does not own cloud, throw exception
        throw new UserDoesNotOwnCloudException(id);
      usedClouds.remove(id);
      owner = this.userByCloudId.remove(id);
      available.signalAll();
    }
    finally {
      cloudLock.unlock();
    }
    if (u != null) // if not system freeing, remove from user
      u.removeCloud(id);
    else if (owner != null) // if system freeing, owner removes cloud
          owner.removeCloud(id);
  }

  /**
   * Method that logs a user into the system. If email does not exist, or password
   * is not a match, throws Exception.
   * If everything went fine, returns the User instance.
   * @param email email of the user that wants to login
   * @param pass password that unlocks the account
   * @throws InexistentUserException if user does not exist in the database
   * @throws FailedLoginException if user is already logged in or wrong password
   * @return User logged in User
   */
  public User loginUser(String email, String pass) throws InexistentUserException, FailedLoginException {
    User u = null;
    try {
      userLock.lock();
      u = this.users.get(email);
    }
    finally {
      userLock.unlock();
    }
    if (u == null)
      throw new InexistentUserException(email);
    if (!u.login(pass))
      throw new FailedLoginException();

    return u;
  }

  /**
   * This method registers a new user into the system. If email already exists,
   * throws an exception.
   * If not, creates a new User, logs it into the system and returns its instance
   * @param email Email to be registered into the system
   * @param pass Password to be associated with the user
   * @throws EmailNotUniqueException if email already registered in the system
   * @return User instance of registered user
   */
  public User registerUser(String email, String pass) throws EmailNotUniqueException {
    User u = new User(email,pass);
    try {
      userLock.lock();
      if (this.users.containsKey(email))
        throw new EmailNotUniqueException(email);
      this.users.put(email,u);
    }
    finally {
      userLock.unlock();
    }
    return u;
  }

  private static String typeFromId(String id){
    if (id != null)
      return id.split("\\_")[0];
    else 
      return null;
  }
}
