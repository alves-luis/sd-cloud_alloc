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
public class AuctionRequest implements Runnable {
  
  private CloudAlloc c;
  private PrintWriter out;
  private double value;
  private User user;
  private String type;


  public AuctionRequest(CloudAlloc c, PrintWriter out, String type, double d, User u) {
    this.c = c;
    this.out = out;
    this.value = d;
    this.user = u;
    this.type = type;
  }

  @Override
  public void run() {
    String id = c.auctionCloud(user, type, value);
    out.println("Reserva do tipo " + type + " concluída! Id: " + id);
  }
  
}
