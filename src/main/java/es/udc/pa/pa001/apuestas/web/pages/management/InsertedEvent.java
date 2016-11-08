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
  public Long getEventId() {
    return eventId;
  }

  /**
   * Sets the event id.
   *
   * @param eventId
   *          the new event id
   */
  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  /**
   * On passivate.
   *
   * @return the long
   */
  Long onPassivate() {
    return eventId;
  }

  /**
   * On activate.
   *
   * @param eventId
   *          the event id
   */
  void onActivate(Long eventId) {
    this.eventId = eventId;
  }

}
