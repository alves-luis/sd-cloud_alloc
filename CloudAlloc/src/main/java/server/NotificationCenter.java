package server;

import cloudalloc.MessageLog;
import cloudalloc.User;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author O Grupo
 */
public class NotificationCenter implements Runnable {

  private User u;
  private PrintWriter out;
  private Socket socket;

  public NotificationCenter(User u, PrintWriter out, Socket s) {
    this.u = u;
    this.out = out;
    this.socket = s;
  }

  @Override
  public void run() {
    MessageLog log = u.getLog();
    Lock lock = log.getLock();
    Condition newMessage = log.getCondition();
    String msg = null;
    try {
      lock.lock();
      while (!socket.isClosed() && u.isLoggedIn()) {
        while (!socket.isClosed() && (msg = u.readMessage()) == null) {
          newMessage.await();
        }
        out.println(msg);
      }
    } catch (InterruptedException ex) {
      System.out.println(ex.getMessage());
    } finally {
      lock.unlock();
    }
  }
}
