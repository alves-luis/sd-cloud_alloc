

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author O Grupo
 */
public class Cloud {

  private String id;
  private String type;
  private double nominalPrice;
  private LocalDateTime requestDate;
  private boolean auctioned;

  public Cloud(String i, String t, double np, boolean flag) {
    this.id = i;
    this.type = t;
    this.nominalPrice = np;
    this.requestDate = LocalDateTime.now();
    this.auctioned = flag;
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public double getAmmountToPay() {
    Duration dur = Duration.between(requestDate, LocalDateTime.now());
    long minutes = dur.toMinutes();
    double ammount = (minutes * nominalPrice) / 60;
    return ammount;
  }

  public boolean isAuctioned() {
    return auctioned;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 37 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }
    Cloud other = (Cloud) obj;
    return this.id.equals(other.getId());
  }

}
