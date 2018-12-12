/*
 * This Thread is running everytime a new user connects to the main server
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

  @Override
  public void run() {
    startUp();
    try {
      out.println("Bye!");
      System.out.println("User disconnected with IP " + s.getRemoteSocketAddress());
      s.close();
    }
    catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
  
  private void terminate() {
    // TODO
  }

  private int getDecision() {
    String input = null;
    int r = 0;
    try {
      input = in.readLine();
      r = Integer.parseInt(input);
    }
    catch (IOException | NumberFormatException e) {
      System.out.println("Error in parsing decision! " + e.getMessage());
    }
    return r;
  }
  
  private String getString() {
    String input = null;
      try {
        input = in.readLine();
      }
      catch (IOException e) {
        System.out.println(e.getMessage());
      }
    return input;
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
  
  // user wants to register
  private void register() {
    out.println("Insere o teu e-mail:");
    String email = getString();
    out.println("Insere a palavra-passe:");
    String pass = getString(); 
    try {
      this.u = c.registerUser(email, pass);
      System.out.println("User with IP " + s.getRemoteSocketAddress() + " registered with e-mail " + email);
      loggedIn();
    }
    catch (EmailNotUniqueException e) {
      out.println("Email já existe! " + e.getMessage());
      System.out.println("Email already registered");
    }
  }
  
  // User wants to login
  private void login() {
    out.println("Insere o teu e-mail:");
    String email = getString();
    out.println("Insere a tua palavra-passe:");
    String pass = getString();
    try {
      this.u = c.loginUser(email, pass);
      System.out.println("User with IP " + s.getRemoteSocketAddress() + " logged in with e-mail " + email);
      loggedIn();
    }
    catch (InexistentUserException e) {
      System.out.println("User does not exist!");
      out.println("Utilizador não registado!");
    }
    catch (FailedLoginException e) {
      System.out.println("Failed login!");
      out.println("Erro de autenticação!");
    }
  }
  
  // User has loggedIn, so do stuff, namely create a new Thread
  private void loggedIn() {
    out.println("Bem-vindo " + u.getEmail());
    int decision;
    do {
      decision = getDecision();
      switch(decision) {
        case 0: break;
      }
    } while(decision != 0);
  }
}
