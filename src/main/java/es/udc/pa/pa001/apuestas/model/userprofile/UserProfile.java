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
  public UserProfile(String loginName, String encryptedPassword,
      String firstName, String lastName, String email) {

    /**
     * NOTE: "userProfileId" *must* be left as "null" since its value is automatically generated.
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
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "UserProfileIdGenerator")
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
   * Gets the login name.
   *
   * @return the login name
   */
  public String getLoginName() {
    return loginName;
  }

  /**
   * Sets the login name.
   *
   * @param loginName
   *          the new login name
   */
  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  /**
   * Gets the encrypted password.
   *
   * @return the encrypted password
   */
  @Column(name = "enPassword")
  public String getEncryptedPassword() {
    return encryptedPassword;
  }

  /**
   * Sets the encrypted password.
   *
   * @param encryptedPassword
   *          the new encrypted password
   */
  public void setEncryptedPassword(String encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
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
   * Gets the last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name.
   *
   * @param lastName
   *          the new last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email.
   *
   * @param email
   *          the new email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "UserProfile [userProfileId=" + userProfileId + ", loginName="
        + loginName + ", encryptedPassword=" + encryptedPassword
        + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
        + email + "]";
  }

}
