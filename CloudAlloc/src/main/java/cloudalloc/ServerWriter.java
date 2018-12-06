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
public class ServerWriter implements Runnable {

    private Socket s;
    private CloudAlloc c;
    private PrintWriter out;
    private boolean loggedIn;

    public ServerWriter(Socket s, CloudAlloc c) {
      this.s = s;
      this.c = c;
      this.out = new PrintWriter(s.getOutputStream(),true);
      this.loggedIn = false;
    }

    public void run() {
      login();
    }

    private void login() {
      out.println(Menu.show(0));
    }
}
