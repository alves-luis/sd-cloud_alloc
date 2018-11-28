/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author grupo
 */
public class User {
    
    private int id;
    private String email;
    private String password;
    private List<Cloud> myClouds;
    
    public User(int id, String e, String pass){
        this.id = id;
        this.email = e;
        this.password = pass;
        this.myClouds = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean login (String pass){
        return this.password.equals(pass);
    }
    
    public void addCloud(Cloud c) {
        this.myClouds.add(c);
    }
    
    public void removeCloud(String id) throws InexistentCloudException {
        for(Cloud c : this.myClouds)
            if (c.getId().equals(id)) {
                myClouds.remove(c);
                break;
            }
    }
    
    public double getTotalDebt() {
        return this.myClouds.stream().mapToDouble(c -> c.getAmmountToPay()).sum();
    }
    
}
