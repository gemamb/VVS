package es.udc.pa.pa001.apuestas.model.bet;

import java.util.Calendar;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;

/**
 * The Class Bet.
 */
@Entity
@Immutable
public class Bet {

  /** The bet id. */
  private Long betId;

  /** The beted money. */
  private Float betedMoney;

  /** The date. */
  private Calendar date;

  /** The user profile. */
  private UserProfile userProfile;

  /** The event. */
  private Event event;

  /** The bet option. */
  private BetOption betOption;

  /**
   * Instantiates a new bet.
   */
  public Bet() {
  }

  /**
   * Instantiates a new bet.
   *
   * @param betId
   *          the bet id
   * @param betedMoney
   *          the beted money
   * @param userProfile
   *          the user profile
   * @param event
   *          the event
   * @param betOption
   *          the bet option
   */
  public Bet(Long betId, Float betedMoney, UserProfile userProfile, Event event,
      BetOption betOption) {
    super();
    this.betId = betId;
    this.betedMoney = betedMoney;
    this.date = Calendar.getInstance();
    this.date.set(Calendar.MILLISECOND, 0);
    this.userProfile = userProfile;
    this.event = event;
    this.betOption = betOption;
  }

  /**
   * Instantiates a new bet.
   *
   * @param betedMoney
   *          the beted money
   * @param userProfile
   *          the user profile
   * @param eventId
   *          the event id
   * @param betOption
   *          the bet option
   */
  public Bet(Float betedMoney, UserProfile userProfile, Event eventId,
      BetOption betOption) {
    super();
    this.betedMoney = betedMoney;
    this.date = Calendar.getInstance();
    this.date.set(Calendar.MILLISECOND, 0);
    this.userProfile = userProfile;
    this.event = eventId;
    this.betOption = betOption;
  }

  /**
   * Gets the bet id.
   *
   * @return the bet id
   */
  @Column(name = "BetId")
  @SequenceGenerator(// It only takes effect for
      name = "BetIdGenerator", // databases providing identifier
      sequenceName = "BetSeq")
  // generators.
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "BetIdGenerator")
  public Long getBetId() {
    return betId;
  }

  /**
   * Sets the bet id.
   *
   * @param betId
   *          the new bet id
   */
  public void setBetId(Long betId) {
    this.betId = betId;
  }

  /**
   * Gets the beted money.
   *
   * @return the beted money
   */
  @Column(name = "betedMoney")
  public Float getBetedMoney() {
    return betedMoney;
  }

  /**
   * Sets the beted money.
   *
   * @param betedMoney
   *          the new beted money
   */
  public void setBetedMoney(Float betedMoney) {
    this.betedMoney = betedMoney;
  }

  /**
   * Gets the date.
   *
   * @return the date
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date")
  public Calendar getDate() {
    return date;
  }

  /**
   * Sets the date.
   *
   * @param date
   *          the new date
   */
  public void setDate(Calendar date) {
    this.date = date;
  }

  /**
   * Gets the user profile.
   *
   * @return the user profile
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "usrId")
  public UserProfile getUserProfile() {
    return userProfile;
  }

  /**
   * Sets the user profile.
   *
   * @param userProfile
   *          the new user profile
   */
  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
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
   * Gets the bet option.
   *
   * @return the bet option
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "betOptionId")
  public BetOption getBetOption() {
    return betOption;
  }

  /**
   * Sets the bet option.
   *
   * @param betOption
   *          the new bet option
   */
  public void setBetOption(BetOption betOption) {
    this.betOption = betOption;
  }

}
