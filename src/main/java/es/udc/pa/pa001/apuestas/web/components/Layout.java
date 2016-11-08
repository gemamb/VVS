package es.udc.pa.pa001.apuestas.web.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;

import es.udc.pa.pa001.apuestas.web.pages.Index;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.CookiesManager;
import es.udc.pa.pa001.apuestas.web.util.UserSession;

/**
 * The Class Layout.
 */
@Import(library = { "tapestry5/bootstrap/js/collapse.js",
    "tapestry5/bootstrap/js/dropdown.js" }, stylesheet = "tapestry5/bootstrap/css/bootstrap-theme.css")
public class Layout {

  /** The title. */
  @Property
  @Parameter(required = true, defaultPrefix = "message")
  private String title;

  /** The show title in body. */
  @Parameter(defaultPrefix = "literal")
  private Boolean showTitleInBody;

  /** The user session. */
  @Property
  @SessionState(create = false)
  private UserSession userSession;

  /** The cookies. */
  @Inject
  private Cookies cookies;

  /** The resources. */
  @Inject
  private ComponentResources resources;

  /** The request. */
  @Inject
  private Request request;

  /**
   * Gets the show title in body.
   *
   * @return the show title in body
   */
  public boolean getShowTitleInBody() {

    if (showTitleInBody == null) {
      return true;
    } else {
      return showTitleInBody;
    }

  }

  /**
   * On action from logout.
   *
   * @return the object
   */
  @AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED)
  Object onActionFromLogout() {
    userSession = null;
    CookiesManager.removeCookies(cookies);
    return Index.class;
  }

}
