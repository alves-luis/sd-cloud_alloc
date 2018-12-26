package exceptions;

/**
 *
 * @author O Grupo
 */
public class InexistentUserException extends Exception {

  public InexistentUserException() {
  }

  public InexistentUserException(String message) {
    super(message);
  }

}
