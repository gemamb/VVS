package es.udc.pa.pa001.apuestas.model.event;

import java.util.List;

/**
 * The Class EventBlock.
 */
public class EventBlock {

  /** The events. */
  private List<Event> events;

  /** The exist more events. */
  private boolean existMoreEvents;

  /**
   * Instantiates a new event block.
   *
   * @param events
   *          the events
   * @param existMoreEvents
   *          the exist more events
   */
  public EventBlock(List<Event> events, boolean existMoreEvents) {

    this.events = events;
    this.existMoreEvents = existMoreEvents;

  }

  /**
   * Gets the events.
   *
   * @return the events
   */
  public List<Event> getEvents() {
    return events;
  }

  /**
   * Gets the exist more events.
   *
   * @return the exist more events
   */
  public boolean getExistMoreEvents() {
    return existMoreEvents;
  }

}
