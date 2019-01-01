package cloudalloc;

import exceptions.InexistentCloudException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author O Grupo
 */
public class User {

  private String email;
  private String password;
  private Map<String,Cloud> myClouds;
  private boolean loggedIn;
  private MessageLog log;
  private double debt;

  /**
   *
   * @param e
   * @param pass
   */
  public User(String e, String pass){
    this.email = e;
    this.password = pass;
    this.myClouds = new HashMap<>();
    this.loggedIn = true;
    this.log = new MessageLog();
    this.debt = 0;
  }
  
  public String getEmail() {
    return this.email;
  }

  /**
   * Returns whether login was successful or not
   * @param pass
   * @return
   */
  public synchronized boolean login (String pass) {
    boolean canLogin = !loggedIn && this.password.equals(pass);
    if (canLogin)
      loggedIn = true;
    return canLogin;
  }

  /**
   * Only one person can logout at a time
   * @return
   */
  public synchronized boolean logout() {
    boolean canLogout = this.loggedIn;
    if (canLogout) 
      this.loggedIn = false;
    return canLogout;
  }
  
  /**
   * Returns the status of the user
   * @return
   */
  public synchronized boolean isLoggedIn() {
    return loggedIn;
  }

  /**
   * Adds a Cloud to a User
   * @param c
   */
  public synchronized void addCloud(Cloud c) {
    this.myClouds.put(c.getId(),c);
  }

  /**
   * Given an Id, removes the Cloud from this User
   * @param id
   * @throws InexistentCloudException
   */
  public synchronized void removeCloud(String id) throws InexistentCloudException {
    Cloud c = this.myClouds.get(id);
    if (c == null)
      throw new InexistentCloudException(id);
    this.myClouds.remove(id);
    debt += c.getAmmountToPay();
  }
  
  /**
   * Given an id, returns if the cloud belongs to this user
   * @param id
   * @return
   */
  public synchronized boolean isMyCloud(String id) {
    return this.myClouds.containsKey(id);
  }

  /**
   * Returns the current debt of running clouds
   * @return
   */
  public synchronized double getTotalDebt() {
    return this.myClouds.values().stream().mapToDouble(c -> c.getAmmountToPay()).sum();
  }
  
  /**
   * Given an Id of a Cloud, returns the amount of money it cost until now
   * @param id
   * @return
   */
  public synchronized double getDebt(String id) {
    Cloud c = this.myClouds.get(id);
    if (c == null)
      return 0;
    else
      return c.getAmmountToPay();
  }
  
  /**
   * Returns cost of removed clouds
   * @return
   */
  public synchronized double getDebt() {
    return this.debt;
  }
  
  /**
   * Returns the Ids of Clouds this users owns
   * @return
   */
  public synchronized List<String> getCloudsId() {
    return this.myClouds.keySet().stream().collect(Collectors.toList());
  }
  
  /**
   * Returns this user's MessageLog
   * @return
   */
  public MessageLog getLog() {
    return this.log;
  }
  
  /**
   * Adds a message to this user Log
   * @param msg
   */
  public void addMsg(String msg) {
    this.log.writeMessage(msg);
  }
  
  /**
   * Returns a new message if available, or null if no new messages
   * @return
   */
  public String readMessage() {
    if (loggedIn)
      return log.readMessage();
    else
      return null;
  }

}
