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
    terminate();
  }
  
  private void terminate() {
    try {
      out.println("Bye bye!");
      System.out.println("User disconnected with IP " + s.getRemoteSocketAddress());
      s.close();
    }
    catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  private int getDecision() {
    int r = 0; // default decision is 0
    try {
      String input = in.readLine();
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
  
  private double getDouble() {
    double r = 0; // default double is 0;
    try {
      String input = in.readLine();
      r = Double.parseDouble(input);
    }
    catch (IOException | NumberFormatException e) {
      System.out.println("Error in parsing decision! " + e.getMessage());
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
  
  private void requestCloud() {
    int decision;
    boolean valid = false;
    do {
      out.println(Menu.typesMenu());
      decision = getDecision();
      switch(decision) {
        case 0: valid = true;
                break;
        default: try {
                  String type = CloudTypes.getType(decision);
                  valid = true;
                  new Thread(new CloudRequest(c,out,type,u)).start();
                 }
                 catch(InvalidTypeException e) {
                   System.out.println("Not a valid type! " + e.getMessage());
                 }
                 break;
      }
    }
    while (decision != 0 && !valid);
  }
  
  private void auctionCloud() {
    int decision;
    boolean valid = false;
    do {
      out.println(Menu.typesMenu());
      decision = getDecision();
      switch(decision) {
        case 0: valid = true;
                break;
        default: try {
                  String type = CloudTypes.getType(decision);
                  valid = true;
                  out.println("Insere o valor nominal a pagar pela Cloud:");
                  double value = getDouble();
                  new Thread(new AuctionRequest(c,out,type,value,u)).start();
                 }
                 catch(InvalidTypeException e) {
                   System.out.println("Not a valid type! " + e.getMessage());
                 }
                 break;
      }
    }
    while (decision != 0 && !valid);
  }
  
  private void getProfile() {
    // TODO
  }
  
  private void freeCloud() {
    String decision;
    boolean valid = false;
    do {
      out.println(Menu.freeMenu(u));
      decision = getString();
      if (decision != null && !decision.equals("0")) {
        valid = true;
        try {
          c.freeCloud(u, decision);
        }
        catch (InexistentCloudException e) {
          out.println("A Cloud que pretendes libertar não existe!");
        }
        catch (UserDoesNotOwnCloudException e) {
          out.println("A Cloud que pretendes libertar não te pertence!");
        }
      }
    }
    while (decision != null && !decision.equals("0") && !valid);
    
  }
  
  // User has loggedIn, so do stuff, namely create a new Thread
  private void loggedIn() {
    out.println("*** Bem-vindo " + u.getEmail() + " ***");
    int decision;
    do {
      out.println(Menu.mainMenu());
      decision = getDecision();
      switch(decision) {
        case 0: out.println("Goodbye " + u.getEmail() + " !");
                u.logout();
                break;
        case 1: requestCloud();
                break;
        case 2: auctionCloud();
                break;
        case 3: getProfile();
                break;
        case 4: freeCloud();
                break;
        default: break;
      }
    } while(decision != 0);
  }
}
