/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author rafaelarodrigues
 */
public class Cloud {
    
    private String id;
    private String type;
    private double nominalPrice;
    private LocalDateTime requestDate;
    private boolean auctioned;
    
   public Cloud (String i, String t,double np, boolean flag){
       this.id = i;
       this.type = t;
       this.nominalPrice = np;
       this.requestDate = LocalDateTime.now();
       this.auctioned = flag;
   }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getNominalPrice() {
        return nominalPrice;
    }

    public void setNominalPrice(double nominalPrice) {
        this.nominalPrice = nominalPrice;
    }
    
    public double getAmmountToPay() {
        Duration dur = Duration.between(requestDate,LocalDateTime.now());
        long minutes = dur.toMinutes();
        double ammount = (minutes*nominalPrice) / 60;
        return ammount;
    }
    
    public boolean isAuctioned() {
        return auctioned;
    }
   
   
}
