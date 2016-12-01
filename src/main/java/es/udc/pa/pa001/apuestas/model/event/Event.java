package es.udc.pa.pa001.apuestas.model.event;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.category.Category;

/**
 * The Class Event.
 */
@Entity
@BatchSize(size = 10)
public class Event {

  /** The event id. */
  private Long eventId;

  /** The name. */
  private String eventName;

  /** The event start. */
  private Calendar eventStart;

  /** The category. */
  private Category category;

  /** The bet types. */
  private List<BetType> betTypes;

  /**
   * Instantiates a new event.
   */
  public Event() {
  }

 
  /**
   * Instantiates a new event.
   *
   * @param name
   *          the name
   * @param eventStartDate
   *          the event start
   * @param cat
   *          the category
   */
  public Event(final String name, final Calendar eventStartDate,
      final Category cat) {
    super();
    this.eventName = name;
    this.eventStart = eventStartDate;
    if (this.eventStart != null) {
      this.eventStart.set(Calendar.SECOND, 0);
      this.eventStart.set(Calendar.MILLISECOND, 0);
    }
    this.category = cat;
    this.betTypes = new LinkedList<BetType>();
  }

  /**
   * Gets the event id.
   *
   * @return the event id
   */
  @Column(name = "eventId")
  @SequenceGenerator(// It only takes effect for
      name = "EventIdGenerator", // databases providing identifier
      sequenceName = "EventSeq")
  // generators.
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "EventIdGenerator")
  public final Long getEventId() {
    return eventId;
  }

  /**
   * Sets the event id.
   *
   * @param eventID
   *          the new event id
   */
  public final void setEventId(final Long eventID) {
    this.eventId = eventID;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  @Column(name = "name")
  public final String getName() {
    return eventName;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public final void setName(final String name) {
    this.eventName = name;
  }

  /**
   * Gets the event start.
   *
   * @return the event start
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "eventStart")
  public final Calendar getEventStart() {
    return eventStart;
  }

  /**
   * Sets the event start.
   *
   * @param eventStartDate
   *          the new event start
   */
  public final void setEventStart(final Calendar eventStartDate) {
    this.eventStart = eventStartDate;
    if (this.eventStart != null) {
      this.eventStart.set(Calendar.SECOND, 0);
      this.eventStart.set(Calendar.MILLISECOND, 0);
    }
  }

  /**
   * Gets the category.
   *
   * @return the category
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categoryId")
  public final Category getCategory() {
    return category;
  }

  /**
   * Sets the category.
   *
   * @param cat
   *          the new category
   */
  public final void setCategory(final Category cat) {
    this.category = cat;
  }

  /**
   * Gets the bet types.
   *
   * @return the bet types
   */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
  public final List<BetType> getBetTypes() {
    return betTypes;
  }

  /**
   * Sets the bet types.
   *
   * @param types
   *          the new bet types
   */
  public final void setBetTypes(final List<BetType> types) {
    this.betTypes = types;
  }

  /**
   * Adds the bet type.
   *
   * @param betType
   *          the bet type
   */
  public final void addBetType(final BetType betType) {
    this.betTypes.add(betType);
    betType.setEvent(this);
  }

  /**
   * Finished event.
   *
   * @param eventID
   *          the event id
   * @return true, if successful
   */
  public final boolean finishedEvent(final Long eventID) {
    return eventStart.getTime().before(Calendar.getInstance().getTime());
  }
}
