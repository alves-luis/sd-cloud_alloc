
/**
 * This Class is called whenever someone makes a request of a cloud using an auction
 * It goes to ZZZzzzZZZ if no Clouds available, and when finally a Cloud is allocated,
 * adds a message to the userLog
 * 
 * @author O Grupo
 */
public class AuctionRequest implements Runnable {

  private final CloudAlloc c;
  private final double value;
  private final User user;
  private final String type;

  public AuctionRequest(CloudAlloc c, String type, double d, User u) {
    this.c = c;
    this.value = d;
    this.user = u;
    this.type = type;
  }

  @Override
  public void run() {
    System.out.println("Started auction request for user " + user.getEmail() + " of type " + type);
    
    String id = c.auctionCloud(user, type, value);
    user.addMsg(Menu.auctionConcluded(type, id, value));
    
    System.out.println("Auction complete for user " + user.getEmail() + ". Id: " + id);
  }

}
