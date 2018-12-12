/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís Alves
 */
public class ServerUpdater implements Runnable {

    private Socket s;
    private CloudAlloc c;
    private PrintWriter out;

    public ServerUpdater(Socket s, CloudAlloc c) {
      this.s = s;
      this.c = c;
        try {
            this.out = new PrintWriter(s.getOutputStream(),true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void run() {
    }

}
