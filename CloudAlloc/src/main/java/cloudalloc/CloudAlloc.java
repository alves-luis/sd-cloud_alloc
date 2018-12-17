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

/**
 *
 * @author o Grupo
 */
public class CloudAlloc {

  /* Key -> cloudType | Value -> Map of Id->Cloud */
  private final Map<String,Map<String,Cloud>> cloudMap;
  private ReentrantLock cloudLock;
  
  /* Conditions according to type of Cloud, when it's available */
  private final Map<String,Condition> cloudsAvailable;

  /* Counter for no repetition of ids */
  private int nextId;
  private ReentrantLock idLock;

  /* Auctions running */
  /* Key -> cloudType | Value -> Ordered Map of Auction Value -> User who made it */
  private final Map<String,Map<Double,User>> auctionsMap;

  /* Map of users by e-mail */
  private final Map<String,User> users;
  private ReentrantLock userLock;

  public CloudAlloc(){

    this.cloudMap = new HashMap<>();
    CloudTypes.getNames().forEach((name) -> {
      this.cloudMap.put(name, new HashMap<>());
    });
    this.cloudLock = new ReentrantLock();
    
    this.cloudsAvailable = new HashMap<>();
    CloudTypes.getNames().forEach((n) -> { 
      cloudsAvailable.put(n,cloudLock.newCondition());
    });

    this.auctionsMap = new HashMap<>();
    CloudTypes.getNames().forEach((name) -> {
      this.auctionsMap.put(name,new TreeMap<>(Comparator.reverseOrder()));
    });

    this.users = new HashMap<>();
    this.userLock = new ReentrantLock();
    
    this.nextId = 0;
    this.idLock = new ReentrantLock();
  }

  public void requestCloud(User u, String type) {
    Map<String,Cloud> usedClouds = this.cloudMap.get(type);
    int id = -1;
    try {
      cloudLock.lock();
      int currentSize = usedClouds.size();
      if (currentSize >= CloudTypes.maxSize(type)) {
        // wait up or try to get auctioned one
        // this is what needs to be done
      }
      id = this.nextId++;
    }
    finally {
      cloudLock.unlock();
    }
    String typeId = type + "_" + id;
    Cloud c = new Cloud(typeId,type,CloudTypes.getPrice(type),false);
    u.addCloud(c);
    usedClouds.put(typeId, c);
  }

  public void auctionCloud(User u, String type, double value) {
    Map<String,Cloud> usedClouds = this.cloudMap.get(type);
    int id = -1;
    try {
      cloudLock.lock();
      int currentSize = usedClouds.size();
      if (currentSize >= CloudTypes.maxSize(type)) {
        // add myself to queue, according to my value and ZZZzzzZZZ
        // this is what needs to be done
      }
      id = this.nextId++;
    }
    finally {
      cloudLock.unlock();
    }
    String typeId = type + "_" + id;
    Cloud c = new Cloud(typeId,type,value,true);
    u.addCloud(c);
    usedClouds.put(typeId,c);
  }

  /**
   * Need to add locks
   * @param u
   * @param id
   * @throws InexistentCloudException
  */
  public void freeCloud(User u, String id) throws InexistentCloudException {
    Map<String,Cloud> usedClouds = this.cloudMap.get(typeFromId(id));
    Cloud c = null;
    try {
      cloudLock.lock();
      c = usedClouds.remove(id);
    }
    finally{
      cloudLock.unlock();
    }
    if (c == null)
        throw new InexistentCloudException(id);
    u.removeCloud(id);
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
    return id.split("\\_")[0];
  }
}
