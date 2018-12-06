/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

/**
 *
 * @author alves-luis
 */
public class EmailNotUniqueException extends Exception {
    public EmailNotUniqueException() {
        super();
    }
    
    public EmailNotUniqueException(String msg) {
        super(msg);
    }
}
