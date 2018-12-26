package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author O Grupo
 */
public class ClientListener implements Runnable {

  private Socket s;
  private BufferedReader in;

  public ClientListener(Socket s) {
    this.s = s;
    try {
      this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    } catch (IOException e) {
      System.out.println("Oops " + e.getMessage());
    }
  }

  @Override
  public void run() {
    try {
      String input;
      while ((input = in.readLine()) != null) {
        System.out.println(input);
      }
      s.shutdownInput();
      s.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

}
