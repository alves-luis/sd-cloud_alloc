package cloudalloc;

/**
 *
 * This class has a Counter with synchronized access
 */
public class Counter {

  private int id;

  public Counter() {
    id = 0;
  }

  public synchronized int getId() {
    int r = id++;
    return r;
  }
}
