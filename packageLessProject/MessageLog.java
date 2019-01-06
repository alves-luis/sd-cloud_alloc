

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author O Grupo
 */
public class MessageLog {

  private List<String> log;
  private int read;
  private int written;
  private Lock lock;
  private Condition newMessage;

  public MessageLog() {
    this.log = new ArrayList<>();
    this.read = 0;
    this.written = 0;
    this.lock = new ReentrantLock();
    this.newMessage = lock.newCondition();
  }

  public void writeMessage(String msg) {
    try {
      lock.lock();
      log.add(msg);
      written++;
      newMessage.signal();
    } finally {
      lock.unlock();
    }
  }

  public String readMessage() {
    try {
      lock.lock();
      if (read < written) {
        return log.get(read++);
      } else {
        return null;
      }
    } finally {
      lock.unlock();
    }
  }

  public Condition getCondition() {
    return this.newMessage;
  }

  public Lock getLock() {
    return this.lock;
  }
}
