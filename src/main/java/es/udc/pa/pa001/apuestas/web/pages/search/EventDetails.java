package es.udc.pa.pa001.apuestas.web.pages.search;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.web.pages.management.InsertBetType;
import es.udc.pa.pa001.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class EventDetails.
 */
public class EventDetails {

  /** The user session. */
  @SessionState(create = false)
  private UserSession userSession;

  /** The event id. */
  private Long eventId;

  /** The bet type id. */
  private Long betTypeId;

  /** The bet types. */
  private List<BetType> betTypes = new ArrayList<>();

  /** The check. */
  private boolean check;

  /** The event. */
  @Property
  private Event event;

  /** The event start. */
  @Property
  private boolean eventStart;

  /** The admin. */
  @Property
  private boolean admin;

  /** The bet type. */
  @Property
  private BetType betType;

  /** The bet option. */
  @Property
  private BetOption betOption;

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The locale. */
  @Inject
  private Locale locale;

  /** The insert bet type. */
  @InjectPage
  private InsertBetType insertBetType;

  /** The options. */
  @InjectPage
  private Options options;

  /**
   * Gets the event id.
   *
   * @return the event id
   */
  public final Long getEventId() {
    return eventId;
  }

  /**
   * Sets the event id.
   *
   * @param eventId
   *          the new event id
   */
  public final void setEventId(final Long eventId) {
    this.eventId = eventId;
  }

  /**
   * Gets the bet type id.
   *
   * @return the bet type id
   */
  public final Long getBetTypeId() {
    return betTypeId;
  }

  /**
   * Sets the bet type id.
   *
   * @param betTypeId
   *          the new bet type id
   */
  public final void setBetTypeId(final Long betTypeId) {
    this.betTypeId = betTypeId;
  }

  /**
   * Gets the format.
   *
   * @return the format
   */
  public final Format getFormat() {
    return NumberFormat.getInstance(locale);
  }

  /**
   * Gets the format date.
   *
   * @return the format date
   */
  public final DateFormat getFormatDate() {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
        locale);
  }

  /**
   * Gets the bet types.
   *
   * @return the bet types
   */
  public final List<BetType> getBetTypes() {
    return betTypes;
  }

  /**
   * Setup render.
   */
  final void setupRender() {
    this.admin = userSession != null && userSession.isAdmin();
    this.eventStart = event.finishedEvent(eventId);
  }

  /**
   * On success.
   *
   * @return the object
   */
  final Object onSuccess() {

    insertBetType.setEventId(this.eventId);
    return insertBetType;
  }

  /**
   * On activate.
   *
   * @param eventId
   *          the event id
   */
  final void onActivate(final Long eventId) {

    this.eventId = eventId;

    try {
      this.event = betService.findEvent(eventId);
      this.betTypes = this.event.getBetTypes();
    } catch (final InstanceNotFoundException e) {
    }
  }

  /**
   * On passivate.
   *
   * @return the long
   */
  final Long onPassivate() {
    return eventId;
  }
}
