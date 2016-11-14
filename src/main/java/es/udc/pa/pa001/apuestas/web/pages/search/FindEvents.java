package es.udc.pa.pa001.apuestas.web.pages.search;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.UserSession;

/**
 * The Class FindEvents.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class FindEvents {

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The user session. */
  @SessionState(create = false)
  private UserSession userSession;

  /** The events details. */
  @InjectPage
  private EventsDetails eventsDetails;

  /** The key words. */
  @Property
  private String keyWords;

  /** The category. */
  @Property
  private String category;

  /**
   * On provide completions from key words.
   *
   * @param keyWords
   *          the key words
   * @return the list
   */
  final List<String> onProvideCompletionsFromKeyWords(final String keyWords) {

    if (keyWords.length() < 3) {
      return null;
    }

    final List<String> matches = new ArrayList<String>();
    boolean admin = false;
    if (userSession != null) {
      admin = userSession.isAdmin();
    }
    final EventBlock events = betService.findEvents(keyWords, null, 0, 10,
        admin);

    for (final Event e : events.getEvents()) {
      matches.add(e.getName());
    }

    return matches;
  }

  /**
   * Lastletter.
   *
   * @param str
   *          the str
   * @return the string
   */
  private static String lastletter(String str) {
    if (str != null && str.length() > 0
        && str.charAt(str.length() - 1) == 'x') {
      str = str.substring(0, str.length() - 1);
    }
    return str;
  }

  /**
   * Gets the categories.
   *
   * @return the categories
   */
  public final String getCategories() {
    final List<Category> categories = betService.findCategories();
    String model = "";
    for (final Category c : categories) {
      model = model + c.getCategoryId().toString() + "=" + c.getName() + ",";
    }
    return lastletter(model);
  }

  /**
   * On success.
   *
   * @return the object
   */
  final Object onSuccess() {
    eventsDetails.setKeyWords(keyWords);
    if (category != null) {
      eventsDetails.setCategory(Long.parseLong(category));
    }
    return eventsDetails;
  }
}
