package es.udc.pa.pa001.apuestas.model.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfileDao;
import es.udc.pa.pa001.apuestas.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class UserServiceImpl.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

  /** The user profile dao. */
  @Autowired
  private UserProfileDao userProfileDao;

  @Override
  public final UserProfile registerUser(final String loginName,
      final String clearPassword,
      final UserProfileDetails userProfileDetails)
      throws DuplicateInstanceException {

    try {
      userProfileDao.findByLoginName(loginName);
      throw new DuplicateInstanceException(loginName,
          UserProfile.class.getName());
    } catch (InstanceNotFoundException e) {
      String encryptedPassword = PasswordEncrypter.crypt(clearPassword);

      UserProfile userProfile = new UserProfile(loginName, encryptedPassword,
          userProfileDetails.getFirstName(), userProfileDetails.getLastName(),
          userProfileDetails.getEmail());

      userProfileDao.save(userProfile);
      return userProfile;
    }

  }

  @Override
  @Transactional(readOnly = true)
  public final UserProfile login(final String loginName, final String password,
      final boolean passwordIsEncrypted)
      throws InstanceNotFoundException, IncorrectPasswordException {

    UserProfile userProfile = userProfileDao.findByLoginName(loginName);
    String storedPassword = userProfile.getEncryptedPassword();

    if (passwordIsEncrypted) {
      if (!password.equals(storedPassword)) {
        throw new IncorrectPasswordException(loginName);
      }
    } else {
      if (!PasswordEncrypter.isClearPasswordCorrect(password, storedPassword)) {
        throw new IncorrectPasswordException(loginName);
      }
    }
    return userProfile;

  }

  @Override
  @Transactional(readOnly = true)
  public final UserProfile findUserProfile(final Long userProfileId)
      throws InstanceNotFoundException {

    return userProfileDao.find(userProfileId);
  }

  @Override
  public final void updateUserProfileDetails(final Long userProfileId,
      final UserProfileDetails userProfileDetails)
      throws InstanceNotFoundException {

    UserProfile userProfile = userProfileDao.find(userProfileId);
    userProfile.setFirstName(userProfileDetails.getFirstName());
    userProfile.setLastName(userProfileDetails.getLastName());
    userProfile.setEmail(userProfileDetails.getEmail());

  }

  @Override
  public final void changePassword(final Long userProfileId,
      final String oldClearPassword,
      final String newClearPassword)
      throws IncorrectPasswordException, InstanceNotFoundException {

    UserProfile userProfile;
    userProfile = userProfileDao.find(userProfileId);

    String storedPassword = userProfile.getEncryptedPassword();

    if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
        storedPassword)) {
      throw new IncorrectPasswordException(userProfile.getLoginName());
    }

    userProfile.setEncryptedPassword(PasswordEncrypter.crypt(newClearPassword));

  }

}
