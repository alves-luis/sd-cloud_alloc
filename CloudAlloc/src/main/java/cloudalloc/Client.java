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
public class Client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("localhost",9999);
            PrintWriter out = new PrintWriter(s.getOutputStream(),true);
            BufferedReader inSys = new BufferedReader(new InputStreamReader(System.in));

            new Thread(new ClientListener(s)).start();
            
            String input;
            while((input = inSys.readLine()) != null)
                out.println(input);
            s.shutdownOutput();
            s.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
