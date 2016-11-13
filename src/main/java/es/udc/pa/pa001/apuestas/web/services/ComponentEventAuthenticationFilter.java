package es.udc.pa.pa001.apuestas.web.services;

import java.io.IOException;

import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

/**
 * The Class ComponentEventAuthenticationFilter.
 */
public class ComponentEventAuthenticationFilter
    implements ComponentEventRequestFilter {

  /** The application state manager. */
  private ApplicationStateManager applicationStateManager;

  /** The component source. */
  private ComponentSource componentSource;

  /** The locator. */
  private MetaDataLocator locator;

  /** The page render request handler. */
  private PageRenderRequestHandler pageRenderRequestHandler;

  /**
   * Instantiates a new component event authentication filter.
   *
   * @param applicationStateManager
   *          the application state manager
   * @param componentSource
   *          the component source
   * @param locator
   *          the locator
   * @param pageRenderRequestHandler
   *          the page render request handler
   */
  public ComponentEventAuthenticationFilter(
      final ApplicationStateManager applicationStateManager,
      final ComponentSource componentSource, final MetaDataLocator locator,
      final PageRenderRequestHandler pageRenderRequestHandler) {

    this.applicationStateManager = applicationStateManager;
    this.componentSource = componentSource;
    this.locator = locator;
    this.pageRenderRequestHandler = pageRenderRequestHandler;

  }


  @Override
  public final void handle(final ComponentEventRequestParameters parameters,
      final ComponentEventRequestHandler handler) throws IOException {

    ComponentEventRequestParameters handlerParameters = parameters;
    String redirectPage = AuthenticationValidator.checkForPage(
        parameters.getActivePageName(), applicationStateManager,
        componentSource, locator);
    if (redirectPage == null) {
      String componentId = parameters.getNestedComponentId();
      if (componentId != null) {
        String mainComponentId = null;
        String eventId = null;
        if (componentId.indexOf(".") != -1) {
          mainComponentId = componentId.substring(0,
              componentId.lastIndexOf("."));
          eventId = componentId.substring(componentId.lastIndexOf(".") + 1);
        } else {
          eventId = componentId;
        }

        redirectPage = AuthenticationValidator.checkForComponentEvent(
            parameters.getActivePageName(), mainComponentId, eventId,
            parameters.getEventType(), applicationStateManager, componentSource,
            locator);

      }
    }

    if (redirectPage != null) {
      pageRenderRequestHandler.handle(new PageRenderRequestParameters(
          redirectPage, new EmptyEventContext(), false));
    } else {
      handler.handle(handlerParameters);
    }
  }
}
