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
 * @author o Grupo
 */
public class CloudAlloc {

    private static final String[] NAMES = {"t3.micro","m5.large","r3.massive"};
    private static final double[] PRICES = {0.95,2.95,4.95};
    private static final int[] CAPACITIES = {6,8,4};
    
    private final Map<String,Integer> maxCloudsPerType;
    private final Map<String,Double> nominalPricePerType;
    
    /* Key -> cloudType | Value -> Map of Id->Cloud */
    private Map<String,Map<String,Cloud>> cloudMap;
    
    /* Map of users by e-mail */
    private Map<String,User> users; 
    
    /* Counter for no repetition of ids */
    private int nextId;
    
    public CloudAlloc(){
        this.maxCloudsPerType = new HashMap<>();
        for(int i = 0; i < NAMES.length; i++)
            this.maxCloudsPerType.put(NAMES[i],CAPACITIES[i]);
     
        this.nominalPricePerType = new HashMap<>();
        for(int i = 0; i < NAMES.length; i++)
            this.nominalPricePerType.put(NAMES[i], PRICES[i]);
 
        this.cloudMap = new HashMap<>();
        for (String NAMES1 : NAMES) {
            this.cloudMap.put(NAMES1, new HashMap<>());
        }
       
        this.users = new HashMap<>();
        
        this.nextId = 0;
    }
    
    public void requestCloud(User u, String type) {
        Map<String,Cloud> usedClouds = this.cloudMap.get(type);
        if (usedClouds.size() >= this.maxCloudsPerType.get(type)) { // probs while
            // put on hold or get auctionedOnes
        }
        else {
            int id = this.nextId;
            String typeId = type + "_" + id;
            Cloud c = new Cloud(typeId,type,this.nominalPricePerType.get(type),false);
            u.addCloud(c);
            usedClouds.put(typeId,c);
        }
    }
    
    public void auctionCloud(User u, String type, double value) {
        Map<String,Cloud> usedClouds = this.cloudMap.get(type);
        if (usedClouds.size() >= this.maxCloudsPerType.get(type)) { // probs while
            // put on hold and store auction value
        }
        else {
            int id = this.nextId++;
            String typeId = type + "_" + id;
            Cloud c = new Cloud(typeId,type,value,true);
            u.addCloud(c);
            usedClouds.put(typeId,c);
        }
    }
    
    
    public void freeCloud(User u, String id) {
        Map<String,Cloud> usedClouds = this.cloudMap.get(typeFromId(id));
        usedClouds.remove(id);
        try{
            u.removeCloud(id);
        }
        catch (InexistentCloudException e){
          System.out.println("Cloud " + id + " doesn't exist.");
        }
    }
    
    public User loginUser(String email, String pass) throws InexistentUserException, IncorrectPasswordException {
        if (!this.users.containsKey(email))
            throw new InexistentUserException(email);
        else if (!this.users.get(email).login(pass))
                throw new IncorrectPasswordException();
        
        return this.users.get(email);
    }


    private String typeFromId(String id){
       return id.split("\\_")[0]; 
    }
    

    
    
    
    
    
    
    public static void main(String[] args) {
        String id = "t3.micro_3";
        String id2 = "m5.large_7";
        System.out.println(idFromId(id) + " e " + idFromId(id2));
    }
         








}

