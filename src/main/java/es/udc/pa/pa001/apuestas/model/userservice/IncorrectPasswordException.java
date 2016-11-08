package es.udc.pa.pa001.apuestas.model.userservice;

/**
 * The Class IncorrectPasswordException.
 */
@SuppressWarnings("serial")
public class IncorrectPasswordException extends Exception {

  /** The login name. */
  private String loginName;

  /**
   * Instantiates a new incorrect password exception.
   *
   * @param loginName
   *          the login name
   */
  public IncorrectPasswordException(String loginName) {
    super("Incorrect password exception => loginName = " + loginName);
    this.loginName = loginName;
  }

  /**
   * Gets the login name.
   *
   * @return the login name
   */
  public String getLoginName() {
    return loginName;
  }

}
