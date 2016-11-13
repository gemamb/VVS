/**
 *
 */
package es.udc.pa.pa001.apuestas.web.pages.management;

/**
 * The Class InsertedEvent.
 *
 */
public class InsertedEvent {

  /** The event id. */
  private Long eventId;

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
   * On passivate.
   *
   * @return the long
   */
  final Long onPassivate() {
    return eventId;
  }

  /**
   * On activate.
   *
   * @param eventId
   *          the event id
   */
  final void onActivate(final Long eventId) {
    this.eventId = eventId;
  }

}
