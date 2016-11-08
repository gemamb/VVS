package es.udc.pa.pa001.apuestas.web.util;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.userservice.UserService;

/**
 * The Class UserSession.
 */
public class UserSession {

  /** The user profile id. */
  private Long userProfileId;

  /** The first name. */
  private String firstName;

  /** The admin. */
  private boolean admin = false;

  /** The user service. */
  @Inject
  private UserService userService;

  /**
   * Gets the user profile id.
   *
   * @return the user profile id
   */
  public Long getUserProfileId() {
    return userProfileId;
  }

  /**
   * Sets the user profile id.
   *
   * @param userProfileId
   *          the new user profile id
   */
  public void setUserProfileId(Long userProfileId) {
    this.userProfileId = userProfileId;
  }

  /**
   * Gets the first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name.
   *
   * @param firstName
   *          the new first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Checks if is admin.
   *
   * @return true, if is admin
   */
  public boolean isAdmin() {
    return admin;
  }

  /**
   * Sets the admin.
   *
   * @param admin
   *          the new admin
   */
  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

}
