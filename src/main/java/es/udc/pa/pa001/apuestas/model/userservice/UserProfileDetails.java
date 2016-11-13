package es.udc.pa.pa001.apuestas.model.userservice;

/**
 * The Class UserProfileDetails.
 */
public class UserProfileDetails {

  /** The first name. */
  private String firstName;

  /** The last name. */
  private String lastName;

  /** The email. */
  private String email;

  /**
   * Instantiates a new user profile details.
   *
   * @param firstName
   *          the first name
   * @param lastName
   *          the last name
   * @param email
   *          the email
   */
  public UserProfileDetails(final String firstName, final String lastName,
      final String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
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
   * Gets the last name.
   *
   * @return the last name
   */
  public final String getLastName() {
    return lastName;
  }

  /**
   * Gets the email.
   *
   * @return the email
   */
  public final String getEmail() {
    return email;
  }

}
