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
    private final Map<String,Map<String,Cloud>> cloudMap;
    private Map<String,User> users; 
    
    public CloudAlloc(){
        this.maxCloudsPerType = new HashMap<>();
        for(int i = 0; i < NAMES.length; i++)
            this.maxCloudsPerType.put(NAMES[i],CAPACITIES[i]);
     
        this.nominalPricePerType = new HashMap<>();
        for(int i = 0; i < NAMES.length; i++)
            this.nominalPricePerType.put(NAMES[i], PRICES[i]);
 
        this.cloudMap = new HashMap<>();
        for(int i = 0; i < NAMES.length; i++)
            this.cloudMap.put(NAMES[i], new HashMap<String,Cloud>());
       
        this.users = new HashMap<>();
    }
    
    public void requestCloud(User u, String type) {
        Map<String,Cloud> usedClouds = this.cloudMap.get(type);
        if (usedClouds.size() >= this.maxCloudsPerType.get(type)) { // probs while
            // put on hold or get auctionedOnes
        }
        else {
            int id = usedClouds.size();
            String typeId = type + "." + id;
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
            int id = usedClouds.size();
            String typeId = type + "." + id;
            Cloud c = new Cloud(typeId,type,value,true);
            u.addCloud(c);
            usedClouds.put(typeId,c);
        }
    }
    
    public void freeCloud(User u, int id, String type) {
        Map<String,Cloud> usedClouds = this.cloudMap.get(type);
        // to do :3
    }
}

