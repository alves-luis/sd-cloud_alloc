
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author O Grupo
 */
public class Server {

  public static void main(String[] args) {
    CloudAlloc c = new CloudAlloc();
    try {
      ServerSocket ss = new ServerSocket(9999);
      while (true) {
        Socket s = ss.accept();
        new Thread(new ServerThread(s, c)).start();
        System.out.println("User connected with IP " + s.getRemoteSocketAddress());
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
