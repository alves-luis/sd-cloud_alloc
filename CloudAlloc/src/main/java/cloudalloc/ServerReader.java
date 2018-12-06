/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

/**
 *
 * @author Luís Alves
 */
public class ServerReader implements Runnable {

  private Socket s;
  private CloudAlloc c;
  private User u;
  private BufferedReader in;

  public ServerReader(Socket s, CloudAlloc c) {
    this.s = s;
    this.c = c;
    this.u = null;
    this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
  }

  public void run() {
    login();
  }

  private int parseDecision() {
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

  private void login() {
    int decision = parseDecision();
    if (decision == 0) return; // activate condition to end session?
    String email;
    try {
      email = in.readLine();
    }
    catch (Exception e) {
      System.out.println("Error in reading e-mail!")
    }
    String pass;
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
    }
    catch (IncorrectPasswordException e) {
      System.out.println("Incorrect password! Try again!");
    }
  }

}
