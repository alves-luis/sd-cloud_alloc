package cloudalloc;

import exceptions.FailedLoginException;
import exceptions.UserDoesNotOwnCloudException;
import exceptions.InexistentCloudException;
import exceptions.EmailNotUniqueException;
import exceptions.InexistentUserException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import server.Menu;

/**
 *
 * @author o Grupo
 */
public class CloudAlloc {

  /* Key -> cloudType | Value -> Map of Id->Cloud */
  private final Map<String, Map<String, Cloud>> cloudMap;
  /* Key -> Id of Cloud | Value -> User that owns it */
  private final Map<String, User> userByCloudId;
  /* Lock to be used with CloudMaps, per type */
  private final Map<String,ReentrantLock> cloudLockByType;

  /* Conditions according to type of Cloud, when one becomes available */
  private final Map<String, Condition> cloudsAvailable;

  /* Counter for no repetition of ids */
  private final Counter nextId;
  
  /* Counter for num of request pending */
  private final Counter requestWaiting;

  /* Auctions running */
  /* Key -> cloudType | Value -> Ordered Map of Auction Value -> User who made it */
  private final Map<String, TreeMap<Double, User>> auctionsMap;

  /* Map of users by e-mail */
  private final Map<String, User> users;
  /* Lock to be used with UserMap */
  private ReentrantLock userLock;

  public CloudAlloc() {

    this.cloudMap = new HashMap<>();
    this.userByCloudId = new HashMap<>();
    this.cloudsAvailable = new HashMap<>();
    this.auctionsMap = new HashMap<>();
    this.users = new HashMap<>();
    this.cloudLockByType = new HashMap<>();
    this.userLock = new ReentrantLock();
    this.nextId = new Counter();
    this.requestWaiting = new Counter();

    // add a new condition, auction TreeMap and CloudMap per CloudType
    CloudTypes.getNames().forEach((n) -> {
      cloudLockByType.put(n,new ReentrantLock());
      cloudsAvailable.put(n, (cloudLockByType.get(n)).newCondition());
      auctionsMap.put(n, new TreeMap<>(Comparator.reverseOrder()));
      cloudMap.put(n, new HashMap<>());
    });
  }

  /**
   * Given a User and a type of Cloud, returns the ID of the Cloud who was given
   * to that User
   * @param u
   * @param type
   * @return ID of the Cloud
   */
  public String requestCloud(User u, String type) {
    boolean foundOne = false;
    Map<String, Cloud> clouds = this.cloudMap.get(type);
    String id = type + "_" + this.nextId.getId();
    Cloud c = new Cloud(id, type, CloudTypes.getPrice(type), false);

    try {
      this.cloudLockByType.get(type).lock();
      // if no clouds available, search for auctioned ones
      if (clouds.size() >= CloudTypes.maxSize(type)) {
        for (Cloud d : clouds.values()) {
          if (d.isAuctioned()) {
            this.freeCloud(null, d.getId()); // found one, so free it
            foundOne = true;
            break;
          }
        }
      }

      // if not found an auctioned one, go ZZZzzZZ while no Clouds available
      if (!foundOne) {
        this.requestWaiting.add(); // add to count of requests waiting
        while (clouds.size() >= CloudTypes.maxSize(type)) {
          try {
            this.cloudsAvailable.get(type).await();
          } catch (InterruptedException e) {}
        }
      }

      // Got a spot, so add to CloudMaps
      clouds.put(id, c);
      this.userByCloudId.put(id, u);
      this.requestWaiting.remove(); // no longer waiting so remove
      
    } catch (InexistentCloudException | UserDoesNotOwnCloudException e) {
      System.out.println(e.getMessage());
    } finally {
      this.cloudLockByType.get(type).unlock();
    }

    u.addCloud(c);
    
    return id;
  }

  /**
   * This method, given a type of Cloud, User who requests and a value, requests
   * a Cloud If no Clouds available, place in queue based on value (higher the
   * value, sooner the Cloud given)
   *
   * @param u
   * @param type
   * @param value
   * @return Id of the Cloud who was auctioned
   */
  public String auctionCloud(User u, String type, double value) {
    Map<String, Cloud> clouds = this.cloudMap.get(type);
    TreeMap<Double, User> auctionClouds = this.auctionsMap.get(type);
    Condition available = this.cloudsAvailable.get(type);
    int id = this.nextId.getId();
    String typeId = type + "_" + id;
    Cloud c = new Cloud(typeId, type, value, true);

    try {
      this.cloudLockByType.get(type).lock();
      // if no clouds available, put in the auction map
      if (clouds.size() >= CloudTypes.maxSize(type)) {
        auctionClouds.put(value, u);
        // while no clouds available, not the first in queue and requestsWaiting, go ZZZzzzZZZ
        while (clouds.size() >= CloudTypes.maxSize(type) ||
                !(auctionClouds.firstEntry().getValue().equals(u) && auctionClouds.firstEntry().getKey().equals(value))
                || this.requestWaiting.get() > 0) {
          try {
            available.await();
          } catch (InterruptedException e) {}
        }
        auctionClouds.remove(value, u); // no longer in queue, so leave it
      }
      // a cloud is available, so add to CloudMaps
      clouds.put(typeId, c);
      this.userByCloudId.put(typeId, u);
    } finally {
      this.cloudLockByType.get(type).unlock();
    }
    
    u.addCloud(c);
    
    return typeId;
  }

  /**
   * This method clears a Cloud. If User is null, that means that is the system
   * freeing a Cloud
   *
   * @param u User who wants to free the cloud
   * @param id id of the cloud that will be freed
   * @throws InexistentCloudException if tried to free a Cloud that does not
   * exist in the system
   * @throws UserDoesNotOwnCloudException if user who tried to free the Cloud
   * does not own it
   */
  public void freeCloud(User u, String id) throws InexistentCloudException, UserDoesNotOwnCloudException {
    String type = typeFromId(id);
    Map<String, Cloud> clouds = this.cloudMap.get(type);

    if (clouds == null) {
      throw new InexistentCloudException(typeFromId(id));
    }

    Cloud c = null;
    User owner = null;
    

    try {
      this.cloudLockByType.get(type).lock();
      c = clouds.get(id);
    } finally {
      this.cloudLockByType.get(type).unlock();
    }

    if (c == null) {
      throw new InexistentCloudException(id);
    }

    Condition available = cloudsAvailable.get(type);
    double cost = c.getAmmountToPay();

    try {
      this.cloudLockByType.get(type).lock();
      // if not system and does not own cloud, throw exception
      if (u != null && !u.isMyCloud(id)) 
        throw new UserDoesNotOwnCloudException(id);

      // remove from CloudMap
      clouds.remove(id);
      owner = this.userByCloudId.remove(id);
      // a new slot is available, so wake up all who are ZZZzzzZZ on this type
      available.signalAll();
    } finally {
      this.cloudLockByType.get(type).unlock();
    }
    // now we can remove the Cloud from its owner, and add a log message
    if (owner != null) {
      owner.removeCloud(id);
      owner.addMsg(Menu.cloudFreed(id, cost));
    }
  }

  /**
   * Method that logs a user into the system. If email does not exist, or
   * password is not a match, throws Exception. If everything went fine, returns
   * the User instance.
   *
   * @param email email of the user that wants to login
   * @param pass password that unlocks the account
   * @throws InexistentUserException if user does not exist in the database
   * @throws FailedLoginException if user is already logged in or wrong password
   * @return User logged in User
   */
  public User loginUser(String email, String pass) throws InexistentUserException, FailedLoginException {
    User u = null;
    try {
      this.userLock.lock();
      u = this.users.get(email);
    } finally {
      this.userLock.unlock();
    }
    if (u == null) {
      throw new InexistentUserException(email);
    }
    if (!u.login(pass)) {
      throw new FailedLoginException();
    }

    return u;
  }

  /**
   * This method registers a new user into the system. If email already exists,
   * throws an exception. If not, creates a new User, logs it into the system
   * and returns its instance
   *
   * @param email Email to be registered into the system
   * @param pass Password to be associated with the user
   * @throws EmailNotUniqueException if email already registered in the system
   * @return User instance of registered user
   */
  public User registerUser(String email, String pass) throws EmailNotUniqueException {
    User u = new User(email, pass);
    try {
      this.userLock.lock();
      if (this.users.containsKey(email)) {
        throw new EmailNotUniqueException(email);
      }
      this.users.put(email, u);
    } finally {
      this.userLock.unlock();
    }
    return u;
  }

  private static String typeFromId(String id) {
    if (id != null) {
      return id.split("\\_")[0];
    } else {
      return null;
    }
  }
}
