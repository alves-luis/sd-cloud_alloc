/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author grupo
 */
public class User {
    
  private String email;
  private String password;
  private Map<String,Cloud> myClouds;
  private boolean loggedIn;
    
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
  }

  /**
   *
   * @return
   */
  public String getEmail() {
    return email;
  }

  /**
   *
   * @param email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   *
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }
    
  // Only one person can login

  /**
   *
   * @param pass
   * @return
   */
  public synchronized boolean login (String pass){
    loggedIn = this.password.equals(pass) || this.loggedIn;
    return !loggedIn && this.password.equals(pass);
  }
    
  // Only one person can logout

  /**
   *
   * @return
   */
  public synchronized boolean logout() {
    boolean success = this.loggedIn;
    if (success) this.loggedIn = false;
    return success;
  }
    
  /**
   *
   * @param c
   */
  public void addCloud(Cloud c) {
    this.myClouds.put(c.getId(),c);
  }
    
  /**
   *
   * @param id
   * @throws InexistentCloudException
   */
  public void removeCloud(String id) throws InexistentCloudException {
    Cloud c = this.myClouds.get(id);
    if (c == null)
      throw new InexistentCloudException(id);
    this.myClouds.remove(id);
  }
    
  /**
   *
   * @return
   */
  public double getTotalDebt() {
    return this.myClouds.values().stream().mapToDouble(c -> c.getAmmountToPay()).sum();
  }
    
}
