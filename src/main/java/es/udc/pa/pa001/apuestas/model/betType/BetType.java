package es.udc.pa.pa001.apuestas.model.betType;

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

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.event.Event;

/**
 * The Class BetType.
 */
@Entity
@BatchSize(size = 10)
public class BetType {

  /** The bet type id. */
  private Long betTypeId;

  /** The question. */
  private String question;

  /** The multiple. */
  private Boolean multiple;

  /** The event. */
  private Event event = null;

  /** The bet options. */
  private List<BetOption> betOptions;

  /**
   * Instantiates a new bet type.
   */
  public BetType() {
    this.betOptions = new LinkedList<BetOption>();
  }

  /**
   * Instantiates a new bet type.
   *
   * @param betTypeId
   *          the bet type id
   * @param question
   *          the question
   * @param multiple
   *          the multiple
   */
  public BetType(Long betTypeId, String question, Boolean multiple) {
    super();
    this.betTypeId = betTypeId;
    this.question = question;
    this.multiple = multiple;
    this.betOptions = new LinkedList<BetOption>();
  }

  /**
   * Instantiates a new bet type.
   *
   * @param question
   *          the question
   * @param multiple
   *          the multiple
   */
  public BetType(String question, Boolean multiple) {
    super();
    this.question = question;
    this.multiple = multiple;
    this.betOptions = new LinkedList<BetOption>();
  }

  /**
   * Gets the bet type id.
   *
   * @return the bet type id
   */
  @Column(name = "BetTypeId")
  @SequenceGenerator(// It only takes effect for
      name = "BetTypeIdGenerator", // databases providing identifier
      sequenceName = "BetTypeSeq")
  // generators.
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "BetTypeIdGenerator")
  public Long getBetTypeId() {
    return betTypeId;
  }

  /**
   * Sets the bet type id.
   *
   * @param betTypeId
   *          the new bet type id
   */
  public void setBetTypeId(Long betTypeId) {
    this.betTypeId = betTypeId;
  }

  /**
   * Gets the question.
   *
   * @return the question
   */
  @Column(name = "question")
  public String getQuestion() {
    return question;
  }

  /**
   * Sets the question.
   *
   * @param question
   *          the new question
   */
  public void setQuestion(String question) {
    this.question = question;
  }

  /**
   * Gets the multiple.
   *
   * @return the multiple
   */
  @Column(name = "multiple")
  public Boolean getMultiple() {
    return multiple;
  }

  /**
   * Sets the multiple.
   *
   * @param multiple
   *          the new multiple
   */
  public void setMultiple(Boolean multiple) {
    this.multiple = multiple;
  }

  /**
   * Gets the event.
   *
   * @return the event
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "eventId")
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
   * Gets the bet options.
   *
   * @return the bet options
   */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "betType")
  public List<BetOption> getBetOptions() {
    return betOptions;
  }

  /**
   * Sets the bet options.
   *
   * @param betOptions
   *          the new bet options
   */
  public void setBetOptions(List<BetOption> betOptions) {
    this.betOptions = betOptions;
  }

  /**
   * Adds the bet option.
   *
   * @param betOption
   *          the bet option
   */
  public void addBetOption(BetOption betOption) {
    this.betOptions.add(betOption);
    betOption.setBetType(this);
  }

}
