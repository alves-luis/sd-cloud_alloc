package exceptions;

/**
 *
 * @author O Grupo
 */
public class EmailNotUniqueException extends Exception {

  public EmailNotUniqueException() {
    super();
  }

  public EmailNotUniqueException(String msg) {
    super(msg);
  }
}
