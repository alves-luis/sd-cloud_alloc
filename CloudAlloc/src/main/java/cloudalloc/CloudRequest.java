package cloudalloc;

/**
 * This Class is called whenever someone makes a request of a cloud
 * It goes to ZZZzzzZZZ if no Clouds available, and when finally a Cloud is allocated,
 * adds a message to the userLog
 * 
 * @author O Grupo
 */
public class CloudRequest implements Runnable {

  private CloudAlloc c;
  private String type;
  private User u;

  public CloudRequest(CloudAlloc c, String type, User u) {
    this.c = c;
    this.type = type;
    this.u = u;
  }

  @Override
  public void run() {
    System.out.println("Started request for user " + u.getEmail() + " of type " + type);
    String id = c.requestCloud(u, type);
    u.addMsg("Reserva do tipo " + type + " concluída! Id: " + id);
    System.out.println("Reservation complete for user " + u.getEmail() + ". Id: " + id);
  }

}
