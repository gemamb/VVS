package es.udc.pa.pa001.apuestas.web.util;

import org.apache.tapestry5.services.Cookies;

/**
 * The Class CookiesManager.
 */
public class CookiesManager {

  /** The Constant LOGIN_NAME_COOKIE. */
  private static final String LOGIN_NAME_COOKIE = "loginName";

  /** The Constant ENCRYPTED_PASSWORD_COOKIE. */
  private static final String ENCRYPTED_PASSWORD_COOKIE = "encryptedPassword";

  /** The Constant REMEMBER_MY_PASSWORD_AGE. */
  private static final int REMEMBER_MY_PASSWORD_AGE = 30 * 24 * 3600; // 30
  // days
  // in
  // seconds

  /**
   * Leave cookies.
   *
   * @param cookies
   *          the cookies
   * @param loginName
   *          the login name
   * @param encryptedPassword
   *          the encrypted password
   */
  public static void leaveCookies(Cookies cookies, String loginName,
      String encryptedPassword) {

    cookies.getBuilder(LOGIN_NAME_COOKIE, loginName)
        .setMaxAge(REMEMBER_MY_PASSWORD_AGE).write();
    cookies.getBuilder(ENCRYPTED_PASSWORD_COOKIE, encryptedPassword)
        .setMaxAge(REMEMBER_MY_PASSWORD_AGE).write();

  }

  /**
   * Removes the cookies.
   *
   * @param cookies
   *          the cookies
   */
  public static void removeCookies(Cookies cookies) {
    cookies.removeCookieValue(LOGIN_NAME_COOKIE);
    cookies.removeCookieValue(ENCRYPTED_PASSWORD_COOKIE);
  }

  /**
   * Gets the login name.
   *
   * @param cookies
   *          the cookies
   * @return the login name
   */
  public static String getLoginName(Cookies cookies) {
    return cookies.readCookieValue(LOGIN_NAME_COOKIE);
  }

  /**
   * Gets the encrypted password.
   *
   * @param cookies
   *          the cookies
   * @return the encrypted password
   */
  public static String getEncryptedPassword(Cookies cookies) {
    return cookies.readCookieValue(ENCRYPTED_PASSWORD_COOKIE);
  }

}
