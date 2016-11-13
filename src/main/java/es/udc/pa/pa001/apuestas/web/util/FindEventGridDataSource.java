package es.udc.pa.pa001.apuestas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.event.Event;

/**
 * The Class FindEventGridDataSource.
 */
public class FindEventGridDataSource implements GridDataSource {

  /** The bet service. */
  private BetService betService;

  /** The key words. */
  private String keyWords;

  /** The category id. */
  private Long categoryId;

  /** The admin. */
  private boolean admin;

  /** The start index. */
  private int startIndex;

  /** The events. */
  private List<Event> events;

  /** The event not found. */
  private boolean eventNotFound;

  /**
   * Instantiates a new find event grid data source.
   *
   * @param betService
   *          the bet service
   * @param keyWords
   *          the key words
   * @param categoryId
   *          the category id
   * @param admin
   *          the admin
   */
  public FindEventGridDataSource(final BetService betService,
      final String keyWords,
      final Long categoryId, final boolean admin) {
    super();
    this.betService = betService;
    this.keyWords = keyWords;
    this.categoryId = categoryId;
    this.admin = admin;
  }


  @Override
  public final int getAvailableRows() {

    return betService.findEventsGetNumber(keyWords, categoryId, admin);
  }


  @Override
  public final void prepare(final int startIndex, final int endIndex,
      final List<SortConstraint> sortConstraints) {

    events = betService.findEvents(keyWords, categoryId, startIndex,
        endIndex - startIndex + 1, admin).getEvents();
    this.startIndex = startIndex;
  }


  @Override
  public final Object getRowValue(final int index) {
    return events.get(index - this.startIndex);
  }


  @Override
  public final Class<Event> getRowType() {
    return Event.class;
  }

  /**
   * Gets the event not found.
   *
   * @return the event not found
   */
  public final boolean getEventNotFound() {
    return eventNotFound;
  }
}
