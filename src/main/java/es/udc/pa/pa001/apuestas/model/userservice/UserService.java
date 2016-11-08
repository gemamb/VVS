package es.udc.pa.pa001.apuestas.model.userservice;

import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Interface UserService.
 */
public interface UserService {

  /**
   * Register user.
   *
   * @param loginName
   *          the login name
   * @param clearPassword
   *          the clear password
   * @param userProfileDetails
   *          the user profile details
   * @return the user profile
   * @throws DuplicateInstanceException
   *           the duplicate instance exception
   */
  public UserProfile registerUser(String loginName, String clearPassword,
      UserProfileDetails userProfileDetails) throws DuplicateInstanceException;

  /**
   * Login.
   *
   * @param loginName
   *          the login name
   * @param password
   *          the password
   * @param passwordIsEncrypted
   *          the password is encrypted
   * @return the user profile
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws IncorrectPasswordException
   *           the incorrect password exception
   */
  public UserProfile login(String loginName, String password,
      boolean passwordIsEncrypted)
      throws InstanceNotFoundException, IncorrectPasswordException;

  /**
   * Find user profile.
   *
   * @param userProfileId
   *          the user profile id
   * @return the user profile
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */
  public UserProfile findUserProfile(Long userProfileId)
      throws InstanceNotFoundException;

  /**
   * Update user profile details.
   *
   * @param userProfileId
   *          the user profile id
   * @param userProfileDetails
   *          the user profile details
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */
  public void updateUserProfileDetails(Long userProfileId,
      UserProfileDetails userProfileDetails) throws InstanceNotFoundException;

  /**
   * Change password.
   *
   * @param userProfileId
   *          the user profile id
   * @param oldClearPassword
   *          the old clear password
   * @param newClearPassword
   *          the new clear password
   * @throws IncorrectPasswordException
   *           the incorrect password exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */
  public void changePassword(Long userProfileId, String oldClearPassword,
      String newClearPassword)
      throws IncorrectPasswordException, InstanceNotFoundException;

}
