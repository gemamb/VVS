package es.udc.pa.pa001.apuestas.model.userprofile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * The Class UserProfile.
 */
@Entity
public class UserProfile {

  /** The user profile id. */
  private Long userId;

  /** The login name. */
  private String login;

  /** The encrypted password. */
  private String encryptedPass;

  /** The first name. */
  private String firstname;

  /** The last name. */
  private String lastname;

  /** The email. */
  private String userEmail;

  /**
   * Instantiates a new user profile.
   */
  public UserProfile() {
  }

  /**
   * Instantiates a new user profile.
   *
   * @param loginName
   *          the login name
   * @param encryptedPassword
   *          the encrypted password
   * @param firstName
   *          the first name
   * @param lastName
   *          the last name
   * @param email
   *          the email
   */
  public UserProfile(final String loginName, final String encryptedPassword,
      final String firstName, final String lastName, final String email) {

    /**
     * NOTE: "userProfileId" *must* be left as "null" since its value is
     * automatically generated.
     */

    this.login = loginName;
    this.encryptedPass = encryptedPassword;
    this.firstname = firstName;
    this.lastname = lastName;
    this.userEmail = email;
  }

  /**
   * Gets the user profile id.
   *
   * @return the user profile id
   */
  @Column(name = "usrId")
  @SequenceGenerator(// It only takes effect for
      name = "UserProfileIdGenerator", // databases providing identifier
      sequenceName = "UserProfileSeq")
  // generators.
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO,
  generator = "UserProfileIdGenerator")
  public final Long getUserProfileId() {
    return userId;
  }

  /**
   * Sets the user profile id.
   *
   * @param userProfileId
   *          the new user profile id
   */
  public final void setUserProfileId(final Long userProfileId) {
    this.userId = userProfileId;
  }

  /**
   * Gets the login name.
   *
   * @return the login name
   */
  public final String getLoginName() {
    return login;
  }

  /**
   * Sets the login name.
   *
   * @param loginName
   *          the new login name
   */
  public final void setLoginName(final String loginName) {
    this.login = loginName;
  }

  /**
   * Gets the encrypted password.
   *
   * @return the encrypted password
   */
  @Column(name = "enPassword")
  public final String getEncryptedPassword() {
    return encryptedPass;
  }

  /**
   * Sets the encrypted password.
   *
   * @param encryptedPassword
   *          the new encrypted password
   */
  public final void setEncryptedPassword(final String encryptedPassword) {
    this.encryptedPass = encryptedPassword;
  }

  /**
   * Gets the first name.
   *
   * @return the first name
   */
  public final String getFirstName() {
    return firstname;
  }

  /**
   * Sets the first name.
   *
   * @param firstName
   *          the new first name
   */
  public final void setFirstName(final String firstName) {
    this.firstname = firstName;
  }

  /**
   * Gets the last name.
   *
   * @return the last name
   */
  public final String getLastName() {
    return lastname;
  }

  /**
   * Sets the last name.
   *
   * @param lastName
   *          the new last name
   */
  public final void setLastName(final String lastName) {
    this.lastname = lastName;
  }

  /**
   * Gets the email.
   *
   * @return the email
   */
  public final String getEmail() {
    return userEmail;
  }

  /**
   * Sets the email.
   *
   * @param email
   *          the new email
   */
  public final void setEmail(final String email) {
    this.userEmail = email;
  }
}
