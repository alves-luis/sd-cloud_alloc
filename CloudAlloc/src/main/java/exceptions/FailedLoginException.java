package exceptions;

/**
 *
 * @author O Grupo
 */
public class FailedLoginException extends Exception {

  public FailedLoginException() {
  }

  public FailedLoginException(String message) {
    super(message);
  }

}
