/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class defines the constants of our CloudAlloc
 * 
 */
public class CloudTypes {
  
  private static final String[] NAMES = {"t3.micro","m5.large","r3.massive"};
  private static final double[] PRICES = {0.95,2.95,4.95};
  private static final int[] CAPACITIES = {6,8,4};
  
  private static final Map<String,Integer> MAX_CLOUDS_PER_TYPE = new HashMap<>();
  private static final Map<String,Double> NOMINAL_PRICE_PER_TYPE = new HashMap<>();
  
  static {
    for(int i = 0; i < NAMES.length; i++) {
      MAX_CLOUDS_PER_TYPE.put(NAMES[i],CAPACITIES[i]);
      NOMINAL_PRICE_PER_TYPE.put(NAMES[i],PRICES[i]);
    } 
  }
  
  public static List<String> getNames() {
    List<String> r = new ArrayList<>();
    r.addAll(Arrays.asList(NAMES));
    return r;
  }
  
  public static int maxSize(String type) {
    if (MAX_CLOUDS_PER_TYPE.containsKey(type))
      return MAX_CLOUDS_PER_TYPE.get(type);
    else
      return 0;
  }
  
  public static double getPrice(String type) {
    if (NOMINAL_PRICE_PER_TYPE.containsKey(type))
      return NOMINAL_PRICE_PER_TYPE.get(type);
    else
      return 0;
  }
  
  public static String getType(int id) throws InvalidTypeException {
    if (id <= NAMES.length)
      return NAMES[id-1];
    else
      throw new InvalidTypeException(Integer.toString(id));
  }
}
