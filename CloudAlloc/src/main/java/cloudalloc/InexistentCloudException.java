/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

/**
 *
 * @author rafaelarodrigues
 */
class InexistentCloudException extends Exception {

    public InexistentCloudException() {
    }

    public InexistentCloudException(String message) {
        super(message);
    }
    
}
