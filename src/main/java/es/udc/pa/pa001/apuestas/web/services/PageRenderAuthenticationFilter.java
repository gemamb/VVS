package es.udc.pa.pa001.apuestas.web.services;

import java.io.IOException;

import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

/**
 * The Class PageRenderAuthenticationFilter.
 */
public class PageRenderAuthenticationFilter implements PageRenderRequestFilter {

  /** The application state manager. */
  private ApplicationStateManager applicationStateManager;

  /** The component source. */
  private ComponentSource componentSource;

  /** The locator. */
  private MetaDataLocator locator;

  /**
   * Instantiates a new page render authentication filter.
   *
   * @param applicationStateManager
   *          the application state manager
   * @param componentSource
   *          the component source
   * @param locator
   *          the locator
   */
  public PageRenderAuthenticationFilter(
      final ApplicationStateManager applicationStateManager,
      final ComponentSource componentSource, final MetaDataLocator locator) {

    this.applicationStateManager = applicationStateManager;
    this.componentSource = componentSource;
    this.locator = locator;

  }


  @Override
  public final void handle(final PageRenderRequestParameters parameters,
      final PageRenderRequestHandler handler) throws IOException {

    PageRenderRequestParameters handlerParameters = parameters;
    String redirectPage = AuthenticationValidator.checkForPage(
        parameters.getLogicalPageName(), applicationStateManager,
        componentSource, locator);
    if (redirectPage != null) {
      handlerParameters = new PageRenderRequestParameters(redirectPage,
          new EmptyEventContext(), false);
    }

    handler.handle(handlerParameters);

  }

}
