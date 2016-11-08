package es.udc.pa.pa001.apuestas.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userservice.UserProfileDetails;
import es.udc.pa.pa001.apuestas.model.userservice.UserService;
import es.udc.pa.pa001.apuestas.web.pages.Index;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;

/**
 * The Class Register.
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Register {

  /** The login name. */
  @Property
  private String loginName;

  /** The password. */
  @Property
  private String password;

  /** The retype password. */
  @Property
  private String retypePassword;

  /** The first name. */
  @Property
  private String firstName;

  /** The last name. */
  @Property
  private String lastName;

  /** The email. */
  @Property
  private String email;

  /** The user session. */
  @SessionState(create = false)
  private UserSession userSession;

  /** The user service. */
  @Inject
  private UserService userService;

  /** The registration form. */
  @Component
  private Form registrationForm;

  /** The login name field. */
  @Component(id = "loginName")
  private TextField loginNameField;

  /** The password field. */
  @Component(id = "password")
  private PasswordField passwordField;

  /** The messages. */
  @Inject
  private Messages messages;

  /** The user profile id. */
  private Long userProfileId;

  /**
   * On validate from registration form.
   */
  void onValidateFromRegistrationForm() {

    if (!registrationForm.isValid()) {
      return;
    }

    if (!password.equals(retypePassword)) {
      registrationForm.recordError(passwordField,
          messages.get("error-passwordsDontMatch"));
    } else {

      try {
        UserProfile userProfile = userService.registerUser(loginName, password,
            new UserProfileDetails(firstName, lastName, email));
        userProfileId = userProfile.getUserProfileId();
      } catch (DuplicateInstanceException e) {
        registrationForm.recordError(loginNameField,
            messages.get("error-loginNameAlreadyExists"));
      }

    }

  }

  /**
   * On success.
   *
   * @return the object
   */
  Object onSuccess() {

    userSession = new UserSession();
    userSession.setUserProfileId(userProfileId);
    userSession.setFirstName(firstName);
    return Index.class;

  }

}
