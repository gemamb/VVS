package es.udc.pa.pa001.apuestas.web.services;

import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;

import es.udc.pa.pa001.apuestas.web.util.UserSession;

/**
 * The Class AuthenticationValidator.
 */
public class AuthenticationValidator {

  /** The Constant LOGIN_PAGE. */
  private static final String LOGIN_PAGE = "user/Login";

  /** The Constant INIT_PAGE. */
  private static final String INIT_PAGE = "Index";

  /** The Constant PAGE_AUTHENTICATION_TYPE. */
  public static final String PAGE_AUTHENTICATION_TYPE =
      "page-authentication-type";

  /** The Constant EVENT_HANDLER_AUTHENTICATION_TYPE. */
  public static final String EVENT_HANDLER_AUTHENTICATION_TYPE =
      "event-handler-authentication-type";

  /**
   * Check for page.
   *
   * @param pageName
   *          the page name
   * @param applicationStateManager
   *          the application state manager
   * @param componentSource
   *          the component source
   * @param locator
   *          the locator
   * @return the string
   */
  public static String checkForPage(final String pageName,
      final ApplicationStateManager applicationStateManager,
      final ComponentSource componentSource, final MetaDataLocator locator) {

    String redirectPage = null;
    Component page = componentSource.getPage(pageName);
    try {
      String policyAsString = locator.findMeta(PAGE_AUTHENTICATION_TYPE,
          page.getComponentResources(), String.class);

      AuthenticationPolicyType policy = AuthenticationPolicyType
          .valueOf(policyAsString);
      redirectPage = check(policy, applicationStateManager);
    } catch (RuntimeException e) {
      System.out.println("Page: '" + pageName + "': " + e.getMessage());
    }
    return redirectPage;

  }

  /**
   * Check for component event.
   *
   * @param pageName
   *          the page name
   * @param componentId
   *          the component id
   * @param eventId
   *          the event id
   * @param eventType
   *          the event type
   * @param applicationStateManager
   *          the application state manager
   * @param componentSource
   *          the component source
   * @param locator
   *          the locator
   * @return the string
   */
  public static String checkForComponentEvent(final String pageName,
      final String componentId, final String eventId, final String eventType,
      final ApplicationStateManager applicationStateManager,
      final ComponentSource componentSource, final MetaDataLocator locator) {

    String redirectPage = null;
    String authenticationPolicyMeta = EVENT_HANDLER_AUTHENTICATION_TYPE + "-"
        + eventId + "-" + eventType;
    authenticationPolicyMeta = authenticationPolicyMeta.toLowerCase();

    Component component = null;
    if (componentId == null) {
      component = componentSource.getPage(pageName);
    } else {
      component = componentSource.getComponent(pageName + ":" + componentId);
    }
    try {
      String policyAsString = locator.findMeta(authenticationPolicyMeta,
          component.getComponentResources(), String.class);
      AuthenticationPolicyType policy = AuthenticationPolicyType
          .valueOf(policyAsString);
      redirectPage = AuthenticationValidator.check(policy,
          applicationStateManager);
    } catch (RuntimeException e) {
      System.out.println("Component: '" + pageName + ":" + componentId + "': "
          + e.getMessage());
    }
    return redirectPage;

  }

  /**
   * Check.
   *
   * @param policy
   *          the policy
   * @param applicationStateManager
   *          the application state manager
   * @return the string
   */
  public static String check(final AuthenticationPolicy policy,
      final ApplicationStateManager applicationStateManager) {

    if (policy != null) {
      return check(policy.value(), applicationStateManager);
    } else {
      return null;
    }

  }

  /**
   * Check.
   *
   * @param policyType
   *          the policy type
   * @param applicationStateManager
   *          the application state manager
   * @return the string
   */
  public static String check(final AuthenticationPolicyType policyType,
      final ApplicationStateManager applicationStateManager) {
    String redirectPage = null;

    boolean userAuthenticated = applicationStateManager
        .exists(UserSession.class);
    UserSession userSession = applicationStateManager
        .getIfExists(UserSession.class);
    switch (policyType) {

    case AUTHENTICATED:

      if (!userAuthenticated) {
        redirectPage = LOGIN_PAGE;
      }
      break;

    case NON_AUTHENTICATED_USERS:

      if (userAuthenticated) {
        redirectPage = INIT_PAGE;
      }
      break;

    case AUTHENTICATED_ADMIN:
      if (!userAuthenticated) {
        redirectPage = LOGIN_PAGE;
      } else {
        if (!userSession.isAdmin()) {
          redirectPage = LOGIN_PAGE;
        }
      }
      break;
    case AUTHENTICATED_NO_ADMIN:
      if (!userAuthenticated) {
        redirectPage = LOGIN_PAGE;
      } else {
        if (userSession.isAdmin()) {
          redirectPage = LOGIN_PAGE;
        }
      }
      break;
    default:
      break;

    }

    return redirectPage;
  }

}
