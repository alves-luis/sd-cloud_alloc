/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

/**
 *
 * @author Luís Alves
 */
class UserDoesNotOwnCloudException extends Exception {

  public UserDoesNotOwnCloudException() {
  }

  public UserDoesNotOwnCloudException(String message) {
    super(message);
  }
  
}
