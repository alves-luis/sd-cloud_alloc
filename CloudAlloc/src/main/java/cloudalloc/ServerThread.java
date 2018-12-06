/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 *
 * @author Luís Alves
 */
public class ServerThread implements Runnable {

  private Socket s;
  private CloudAlloc c;
  private User u;
  private BufferedReader in;
  private PrintWriter out;

  public ServerThread(Socket s, CloudAlloc c) {
    this.s = s;
    this.c = c;
    this.u = null;
      try {
          this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
          this.out = new PrintWriter(s.getOutputStream(),true);
      } catch (IOException e) {
          System.out.println(e.getMessage());
      }
  }

  public void run() {
    startUp();
  }

  private int getDecision() {
    String input;
    int r = 0;
    try {
      input = in.readLine();
      r = Integer.parseInt(input);
    }
    catch (Exception e) {
      System.out.println("Error in parsing decision!");
    }
    return r;
  }

  /** When user connects, either logs in or registers in the system */
  private void startUp() {
    int decision;
    do {
        out.println(Menu.loginMenu());
        decision = getDecision();
        switch(decision) {
            case 1: login();
                break;
            case 2: register();
                break;
            default: break;
        }
    } while(decision != 0);
  }
  
  private void register() {
    out.println("Insere o teu e-mail:");
    String email = null;
    try {
        email = in.readLine();
    }
    catch (Exception e) {
        System.out.println("Error in read e-mail");
    }
    out.println("Insere a palavra-passe:");
    String pass = null;
    try {
       pass = in.readLine(); 
    }
    catch (Exception e) {
        System.out.println("Error in reading password");
    }
    
    try {
        c.registerUser(email, pass);
    }
    catch (EmailNotUniqueException e) {
        out.println("Email já existe! " + e.getMessage());
        System.out.println("Email already registered");
    }
  }
  
  private void login() {
    out.println("Insere o teu e-mail:");
    String email = null;
    try {
      email = in.readLine();
    }
    catch (Exception e) {
      System.out.println("Error in reading e-mail!");
    }
    String pass = null;
    try {
      pass = in.readLine();
    }
    catch (Exception e) {
      System.out.println("Error in reading the password!");
    }
    try {
      this.u = c.loginUser(email, pass);
    }
    catch (InexistentUserException e) {
      System.out.println("User does not exist!");
      out.println("Impossível autenticar!");
    }
    catch (IncorrectPasswordException e) {
      System.out.println("Incorrect password!");
      out.println("Impossível autenticar!");
    }
  }
}
