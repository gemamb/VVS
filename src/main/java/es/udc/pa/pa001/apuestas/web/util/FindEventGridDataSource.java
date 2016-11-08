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
  public FindEventGridDataSource(BetService betService, String keyWords,
      Long categoryId, boolean admin) {
    super();
    this.betService = betService;
    this.keyWords = keyWords;
    this.categoryId = categoryId;
    this.admin = admin;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.grid.GridDataSource#getAvailableRows()
   */
  @Override
  public int getAvailableRows() {

    return betService.findEventsGetNumber(keyWords, categoryId, admin);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.grid.GridDataSource#prepare(int, int, java.util.List)
   */
  @Override
  public void prepare(int startIndex, int endIndex,
      List<SortConstraint> sortConstraints) {

    events = betService.findEvents(keyWords, categoryId, startIndex,
        endIndex - startIndex + 1, admin).getEvents();
    this.startIndex = startIndex;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.grid.GridDataSource#getRowValue(int)
   */
  @Override
  public Object getRowValue(int index) {
    return events.get(index - this.startIndex);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.grid.GridDataSource#getRowType()
   */
  @Override
  public Class<Event> getRowType() {
    return Event.class;
  }

  /**
   * Gets the event not found.
   *
   * @return the event not found
   */
  public boolean getEventNotFound() {
    return eventNotFound;
  }
}
