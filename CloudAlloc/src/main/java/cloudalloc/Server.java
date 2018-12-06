/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudalloc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Luís Alves
 */
public class Server {
    public static void main(String[] args) {
        CloudAlloc c = new CloudAlloc();
        try {
            ServerSocket ss = new ServerSocket(9999);
            while(true) {
                Socket s = ss.accept();
                new Thread(new ServerThread(s,c)).start();
                System.out.println("User with IP " + s.getRemoteSocketAddress());
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
