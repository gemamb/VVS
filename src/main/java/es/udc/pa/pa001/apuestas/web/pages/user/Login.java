package es.udc.pa.pa001.apuestas.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa001.apuestas.model.userservice.UserService;
import es.udc.pa.pa001.apuestas.web.pages.Index;
import es.udc.pa.pa001.apuestas.web.pages.search.MakeBet;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.CookiesManager;
import es.udc.pa.pa001.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class Login.
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

  /** The login name. */
  @Property
  private String loginName;

  /** The password. */
  @Property
  private String password;

  /** The remember my password. */
  @Property
  private boolean rememberMyPassword;

  /** The user session. */
  @SessionState(create = false)
  private UserSession userSession;

  /** The cookies. */
  @Inject
  private Cookies cookies;

  /** The login form. */
  @Component
  private Form loginForm;

  /** The messages. */
  @Inject
  private Messages messages;

  /** The user service. */
  @Inject
  private UserService userService;

  /** The make bet. */
  @InjectPage
  private MakeBet makeBet;

  /** The user profile. */
  private UserProfile userProfile = null;

  /** The bet option id. */
  private Long betOptionId;

  /**
   * On validate from login form.
   */
  void onValidateFromLoginForm() {

    if (!loginForm.isValid()) {
      return;
    }

    try {
      userProfile = userService.login(loginName, password, false);
    } catch (InstanceNotFoundException e) {
      loginForm.recordError(messages.get("error-authenticationFailed"));
    } catch (IncorrectPasswordException e) {
      loginForm.recordError(messages.get("error-authenticationFailed"));
    }

  }

  /**
   * On success.
   *
   * @return the object
   */
  Object onSuccess() {

    userSession = new UserSession();
    userSession.setAdmin(userProfile.getLoginName().equals("admin"));
    userSession.setUserProfileId(userProfile.getUserProfileId());
    userSession.setFirstName(userProfile.getFirstName());

    if (rememberMyPassword) {
      CookiesManager.leaveCookies(cookies, loginName,
          userProfile.getEncryptedPassword());
    }
    if (this.betOptionId == null)
      return Index.class;
    else {
      makeBet.setbetOptionId(betOptionId);
      return makeBet;
    }

  }

  /**
   * On activate.
   *
   * @param id
   *          the id
   */
  void onActivate(Long id) {
    this.betOptionId = id;
  }

  /**
   * On passivate.
   *
   * @return the long
   */
  Long onPassivate() {
    return this.betOptionId;
  }

}
