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
  private Long userProfileId;

  /** The login name. */
  private String loginName;

  /** The encrypted password. */
  private String encryptedPassword;

  /** The first name. */
  private String firstName;

  /** The last name. */
  private String lastName;

  /** The email. */
  private String email;

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

    this.loginName = loginName;
    this.encryptedPassword = encryptedPassword;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
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
    return userProfileId;
  }

  /**
   * Sets the user profile id.
   *
   * @param userProfileId
   *          the new user profile id
   */
  public final void setUserProfileId(final Long userProfileId) {
    this.userProfileId = userProfileId;
  }

  /**
   * Gets the login name.
   *
   * @return the login name
   */
  public final String getLoginName() {
    return loginName;
  }

  /**
   * Sets the login name.
   *
   * @param loginName
   *          the new login name
   */
  public final void setLoginName(final String loginName) {
    this.loginName = loginName;
  }

  /**
   * Gets the encrypted password.
   *
   * @return the encrypted password
   */
  @Column(name = "enPassword")
  public final String getEncryptedPassword() {
    return encryptedPassword;
  }

  /**
   * Sets the encrypted password.
   *
   * @param encryptedPassword
   *          the new encrypted password
   */
  public final void setEncryptedPassword(final String encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  /**
   * Gets the first name.
   *
   * @return the first name
   */
  public final String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name.
   *
   * @param firstName
   *          the new first name
   */
  public final void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name.
   *
   * @return the last name
   */
  public final String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name.
   *
   * @param lastName
   *          the new last name
   */
  public final void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the email.
   *
   * @return the email
   */
  public final String getEmail() {
    return email;
  }

  /**
   * Sets the email.
   *
   * @param email
   *          the new email
   */
  public final void setEmail(final String email) {
    this.email = email;
  }

  @Override
  public final String toString() {
    return "UserProfile [userProfileId=" + userProfileId + ", loginName="
        + loginName + ", encryptedPassword=" + encryptedPassword
        + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
        + email + "]";
  }

}
