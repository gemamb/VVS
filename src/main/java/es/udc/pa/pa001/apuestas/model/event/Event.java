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
  private String name;

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
   * @param eventId
   *          the event id
   * @param name
   *          the name
   * @param eventStart
   *          the event start
   * @param category
   *          the category
   * @param betTypes
   *          the bet types
   */
  public Event(Long eventId, String name, Calendar eventStart,
      Category category, List<BetType> betTypes) {
    super();
    this.eventId = eventId;
    this.name = name;
    this.eventStart = eventStart;
    if (this.eventStart != null) {
      this.eventStart.set(Calendar.SECOND, 0);
      this.eventStart.set(Calendar.MILLISECOND, 0);
    }
    this.category = category;
    this.betTypes = betTypes;
  }

  /**
   * Instantiates a new event.
   *
   * @param name
   *          the name
   * @param eventStart
   *          the event start
   * @param category
   *          the category
   */
  public Event(String name, Calendar eventStart, Category category) {
    super();
    this.name = name;
    this.eventStart = eventStart;
    if (this.eventStart != null) {
      this.eventStart.set(Calendar.SECOND, 0);
      this.eventStart.set(Calendar.MILLISECOND, 0);
    }
    this.category = category;
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
   * Gets the name.
   *
   * @return the name
   */
  @Column(name = "name")
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the event start.
   *
   * @return the event start
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "eventStart")
  public Calendar getEventStart() {
    return eventStart;
  }

  /**
   * Sets the event start.
   *
   * @param eventStart
   *          the new event start
   */
  public void setEventStart(Calendar eventStart) {
    this.eventStart = eventStart;
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
  public Category getCategory() {
    return category;
  }

  /**
   * Sets the category.
   *
   * @param category
   *          the new category
   */
  public void setCategory(Category category) {
    this.category = category;
  }

  /**
   * Gets the bet types.
   *
   * @return the bet types
   */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
  public List<BetType> getBetTypes() {
    return betTypes;
  }

  /**
   * Sets the bet types.
   *
   * @param betTypes
   *          the new bet types
   */
  public void setBetTypes(List<BetType> betTypes) {
    this.betTypes = betTypes;
  }

  /**
   * Adds the bet type.
   *
   * @param betType
   *          the bet type
   */
  public void addBetType(BetType betType) {
    this.betTypes.add(betType);
    betType.setEvent(this);
  }

  /**
   * Finished event.
   *
   * @param eventId
   *          the event id
   * @return true, if successful
   */
  public boolean finishedEvent(Long eventId) {
    return eventStart.getTime().before(Calendar.getInstance().getTime());
  }
}
