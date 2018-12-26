package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author O Grupo
 */
public class Client {

  public static void main(String[] args) {
    try {
      Socket s = new Socket("localhost", 9999);
      PrintWriter out = new PrintWriter(s.getOutputStream(), true);
      BufferedReader inSys = new BufferedReader(new InputStreamReader(System.in));

      new Thread(new ClientListener(s)).start();

      String input;
      while ((input = inSys.readLine()) != null) {
        out.println(input);
      }
      out.close();
      inSys.close();
      s.close();
    } catch (IOException e) {
      System.out.println("Oops! " + e.getMessage());
    }
  }
}
