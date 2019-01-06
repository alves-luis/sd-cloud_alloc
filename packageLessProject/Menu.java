
import java.util.List;

/**
 *
 * @author O Grupo
 */
public class Menu {

  public static String mainMenu() {
    return ("**********************\n"
            + "1) Pedir Servidor\n"
            + "2) Leilão de Servidor\n"
            + "3) Consultar perfil\n"
            + "4) Libertar Servidor\n"
            + "0) Sair\n"
            + "**********************");
  }

  public static String loginMenu() {
    return ("******************\n"
            + "1) Iniciar Sessão\n"
            + "2) Registar\n"
            + "0) Sair\n"
            + "******************");
  }

  public static String typesMenu() {
    StringBuilder sb = new StringBuilder();
    sb.append("**********************\n");
    List<String> names = CloudTypes.getNames();
    for (int i = 1; i <= names.size(); i++)
      sb.append(i).append(") ").append(names.get(i - 1)).append("\n");
    sb.append("0) Sair\n").append("**********************");
    return sb.toString();
  }

  public static String freeMenu(User u) {
    StringBuilder sb = new StringBuilder();
    sb.append("**********************\n").append("0) Sair\n").append("Clouds disponíveis para libertar:\n");
    for (String id : u.getCloudsId())
      sb.append(id).append("\n");
    sb.append("Insira o ID da Cloud que deseja libertar:\n").append("**********************");
    return sb.toString();
  }
  
  public static String reservationConcluded(String type, String id) {
    StringBuilder sb = new StringBuilder();
    sb.append("Reserva do tipo ").append(type).append(" concluída! Id: ").append(id);
    return sb.toString();
  }
  
  public static String auctionConcluded(String type, String id, double val) {
    StringBuilder sb = new StringBuilder();
    sb.append("Reserva do tipo ").append(type).append(" concluída! Id: ").append(id).append(" . Valor: ").append(val);
    return sb.toString();
  }
  
  public static String cloudFreed(String id, double cost) {
    StringBuilder sb = new StringBuilder();
    sb.append("A tua Cloud de id ").append(id).append(" foi libertada! Custo da Cloud: ").append(cost);
    return sb.toString();
  }
}
