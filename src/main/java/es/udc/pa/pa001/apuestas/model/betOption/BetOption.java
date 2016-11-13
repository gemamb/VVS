package es.udc.pa.pa001.apuestas.model.betOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa001.apuestas.model.betType.BetType;

/**
 * The Class BetOption.
 */
@Entity
@BatchSize(size = 10)
public class BetOption {

  /** The bet option id. */
  private Long betOptionId;

  /** The answer. */
  private String answer;

  /** The rate. */
  private Float rate;

  /** The bet state. */
  private Boolean betState;

  /** The bet type. */
  private BetType betType;

  /**
   * Instantiates a new bet option.
   */
  public BetOption() {
  }

  /**
   * Instantiates a new bet option.
   *
   * @param betOptionId
   *          the bet option id
   * @param answer
   *          the answer
   * @param rate
   *          the rate
   * @param betState
   *          the bet state
   * @param betType
   *          the bet type
   */
  public BetOption(final Long betOptionId, final String answer,
      final Float rate,
      final Boolean betState, final BetType betType) {
    super();
    this.betOptionId = betOptionId;
    this.answer = answer;
    this.rate = rate;
    this.betState = null;
    this.betType = betType;
  }

  /**
   * Instantiates a new bet option.
   *
   * @param answer
   *          the answer
   * @param rate
   *          the rate
   * @param betState
   *          the bet state
   * @param betType
   *          the bet type
   */
  public BetOption(final String answer, final Float rate,
      final Boolean betState,
      final BetType betType) {
    super();
    this.answer = answer;
    this.rate = rate;
    this.betState = betState;
    this.betType = betType;
  }

  /**
   * Gets the bet option id.
   *
   * @return the bet option id
   */
  @Column(name = "BetOptionId")
  @SequenceGenerator(// It only takes effect for
      name = "BetOptionIdGenerator", // databases providing identifier
      sequenceName = "BetOptionSeq")
  // generators.
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO,
  generator = "BetOptionIdGenerator")
  public final Long getBetOptionId() {
    return betOptionId;
  }

  /**
   * Sets the bet option id.
   *
   * @param betOptionId
   *          the new bet option id
   */
  public final void setBetOptionId(final Long betOptionId) {
    this.betOptionId = betOptionId;
  }

  /**
   * Gets the answer.
   *
   * @return the answer
   */
  @Column(name = "answer")
  public final String getAnswer() {
    return answer;
  }

  /**
   * Sets the answer.
   *
   * @param answer
   *          the new answer
   */
  public final void setAnswer(final String answer) {
    this.answer = answer;
  }

  /**
   * Gets the rate.
   *
   * @return the rate
   */
  @Column(name = "rate")
  public final Float getRate() {
    return rate;
  }

  /**
   * Sets the rate.
   *
   * @param rate
   *          the new rate
   */
  public final void setRate(final Float rate) {
    this.rate = rate;
  }

  /**
   * Gets the bet state.
   *
   * @return the bet state
   */
  @Column(name = "betState")
  public final Boolean getBetState() {
    return betState;
  }

  /**
   * Sets the bet state.
   *
   * @param betState
   *          the new bet state
   */
  public final void setBetState(final Boolean betState) {
    this.betState = betState;
  }

  /**
   * Gets the bet type.
   *
   * @return the bet type
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "betTypeId")
  public final BetType getBetType() {
    return betType;
  }

  /**
   * Sets the bet type.
   *
   * @param betType
   *          the new bet type
   */
  public final void setBetType(final BetType betType) {
    this.betType = betType;
  }

}
