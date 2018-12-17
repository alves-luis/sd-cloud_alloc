/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.util.List;

/**
 *
 * @author Luís Alves
 */
public class Menu {

  public static String mainMenu() {
    return("**********************\n" +
           "1) Pedir Servidor\n" +
           "2) Leilão de Servidor\n" +
           "3) Consultar perfil\n" +
           "4) Libertar Servidor\n" +
           "0) Sair\n" +
           "**********************");
  }
    
  public static String loginMenu() {
    return("******************\n" +
           "1) Iniciar Sessão\n" +
           "2) Registar\n" +
           "0) Sair\n" +
           "******************");
  }

  public static String typesMenu() {
    StringBuilder sb = new StringBuilder();
    sb.append("**********************\n");
    List<String> names = CloudTypes.getNames();
    for(int i = 1; i <= names.size(); i++) {
      sb.append(i).append(") ").append(names.get(i-1)).append("\n");
    }
    sb.append("0) Sair\n").append("**********************");
    return sb.toString();
  }
  
  public static String freeMenu() {
    return null;
  }
  
  public static String profileMenu() {
    return null;
  }
}
