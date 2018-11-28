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

    private final Map<String,Integer> maxCloudsPerType;
    private final Map<String,Double> nominalPricePerType;
    private final Map<String,Map<String,Cloud>> cloudMap;
    private Map<String,User> users; 
    private Map<String,Cloud> microClouds;
    private Map<String,Cloud> largeClouds;
    private Map<String,Cloud> massiveClouds;
    
    public CloudAlloc(){
        this.maxCloudsPerType = new HashMap<>();
        this.maxCloudsPerType.put("t3.micro",6);
        this.maxCloudsPerType.put("m5.large",8);
        this.maxCloudsPerType.put("r3.massive",4);
        this.nominalPricePerType = new HashMap<>();
        this.nominalPricePerType.put("t3.micro", 0.95);
        this.nominalPricePerType.put("m5.large", 2.95);
        this.nominalPricePerType.put("r3.massive",4.95);
        this.cloudMap = new HashMap<>();
        this.cloudMap.put("t3.micro", this.microClouds);
        this.cloudMap.put("m5.large", this.largeClouds);
        this.cloudMap.put("r3.massive",this.massiveClouds);
        
        this.users = new HashMap<>();
        this.microClouds = new HashMap<>();
        this.largeClouds = new HashMap<>();
        this.massiveClouds = new HashMap<>();
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

