/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.io.PrintWriter;

/**
 *
 * @author Luís Alves
 */
public class CloudRequest implements Runnable {
  
  private CloudAlloc c;
  private PrintWriter out;
  private String type;
  private User u;

  public CloudRequest(CloudAlloc c, PrintWriter out, String type, User u) {
    this.c = c;
    this.out = out;
    this.type = type;
    this.u = u;
  }

  @Override
  public void run(){
    String id = c.requestCloud(u, type);
    out.println("Querido utilizador "+u.getEmail()+" já tem a sua cloud! O seu ID é "+id);
  }
  
}
