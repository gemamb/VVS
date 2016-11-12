/**
 *
 */
package es.udc.pa.pa001.apuestas.web.pages.search;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.FindEventGridDataSource;
import es.udc.pa.pa001.apuestas.web.util.UserSession;

/**
 * The Class EventsDetails.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class EventsDetails {

  /** The Constant EVENTS_PER_PAGE. */
  private static final int EVENTS_PER_PAGE = 10;

  /** The key words. */
  private String keyWords;

  /** The start index. */
  private int startIndex = 0;

  /** The event block. */
  private EventBlock eventBlock;

  /** The event. */
  private Event event;

  /** The category. */
  private Long category;

  /** The user session. */
  @SessionState(create = false)
  private UserSession userSession;

  /** The admin. */
  private boolean admin;

  /** The locale. */
  @Inject
  private Locale locale;

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The find event grid data source. */
  private FindEventGridDataSource findEventGridDataSource;

  /**
   * Gets the find event grid data source.
   *
   * @return the find event grid data source
   */
  public FindEventGridDataSource getFindEventGridDataSource() {
    return findEventGridDataSource;
  }

  /**
   * Gets the rows per page.
   *
   * @return the rows per page
   */
  public int getRowsPerPage() {
    return EVENTS_PER_PAGE;
  }

  /**
   * Gets the event.
   *
   * @return the event
   */
  public Event getEvent() {
    return event;
  }

  /**
   * Sets the event.
   *
   * @param event
   *          the new event
   */
  public void setEvent(Event event) {
    this.event = event;
  }

  /**
   * Gets the events.
   *
   * @return the events
   */
  public List<Event> getEvents() {
    return (eventBlock == null) ? null : eventBlock.getEvents();
  }

  /**
   * Gets the key words.
   *
   * @return the key words
   */
  public String getKeyWords() {
    return keyWords;
  }

  /**
   * Sets the key words.
   *
   * @param keyWords
   *          the new key words
   */
  public void setKeyWords(String keyWords) {
    this.keyWords = keyWords;
  }

  /**
   * Gets the category.
   *
   * @return the category
   */
  public Long getCategory() {
    return category;
  }

  /**
   * Sets the category.
   *
   * @param category
   *          the new category
   */
  public void setCategory(Long category) {
    this.category = category;
  }

  // public Object[] getPreviousLinkContext(){
  // return (startIndex-EVENTS_PER_PAGE >=0) ? new
  // Object[]{keyWords,category,startIndex-EVENTS_PER_PAGE} : null;
  // }
  //
  // public Object[] getNextLinkContext(){
  // return eventBlock.getExistMoreEvents() ? new
  // Object[]{keyWords,category,startIndex+EVENTS_PER_PAGE} : null;
  // }

  /**
   * Gets the format.
   *
   * @return the format
   */
  public DateFormat getFormat() {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
        locale);
  }

  /**
   * On activate.
   *
   * @param keyWords
   *          the key words
   * @param category
   *          the category
   * @param startIndex
   *          the start index
   */
  void onActivate(String keyWords, Long category, int startIndex) {
    this.keyWords = keyWords;
    this.category = category;
    this.startIndex = startIndex;
    boolean admin = userSession != null && userSession.isAdmin();

    findEventGridDataSource = new FindEventGridDataSource(betService, keyWords,
        category, admin);

    // eventBlock = betService.findEvents(keyWords, category,
    // startIndex,EVENTS_PER_PAGE,admin);
  }

  /**
   * On passivate.
   *
   * @return the object[]
   */
  Object[] onPassivate() {
    return new Object[] { keyWords, category, startIndex };
  }

}
