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
public class FreeCloudListener implements Runnable {

  private User user;
  private PrintWriter out;
  private String id;
  
  public FreeCloudListener(User u, PrintWriter o, String cloudId) {
    this.user = u;
    this.out = o;
    this.id = cloudId;
  }
  @Override
  public void run() {
    while (user.ownsCloud(id)) {}
    out.println("A tua Cloud de id " + id + " foi libertada!");
  }
  
}
