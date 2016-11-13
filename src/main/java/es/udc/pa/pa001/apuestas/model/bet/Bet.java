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
  public Bet(final Long betId, final Float betedMoney,
      final UserProfile userProfile, final Event event,
      final BetOption betOption) {
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
  public Bet(final Float betedMoney, final UserProfile userProfile,
      final Event eventId,
      final BetOption betOption) {
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
  public final Long getBetId() {
    return betId;
  }

  /**
   * Sets the bet id.
   *
   * @param betId
   *          the new bet id
   */
  public final void setBetId(final Long betId) {
    this.betId = betId;
  }

  /**
   * Gets the beted money.
   *
   * @return the beted money
   */
  @Column(name = "betedMoney")
  public final Float getBetedMoney() {
    return betedMoney;
  }

  /**
   * Sets the beted money.
   *
   * @param betedMoney
   *          the new beted money
   */
  public final void setBetedMoney(final Float betedMoney) {
    this.betedMoney = betedMoney;
  }

  /**
   * Gets the date.
   *
   * @return the date
   */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date")
  public final Calendar getDate() {
    return date;
  }

  /**
   * Sets the date.
   *
   * @param date
   *          the new date
   */
  public final void setDate(final Calendar date) {
    this.date = date;
  }

  /**
   * Gets the user profile.
   *
   * @return the user profile
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "usrId")
  public final UserProfile getUserProfile() {
    return userProfile;
  }

  /**
   * Sets the user profile.
   *
   * @param userProfile
   *          the new user profile
   */
  public final void setUserProfile(final UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  /**
   * Gets the event.
   *
   * @return the event
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "eventId")
  public final Event getEvent() {
    return event;
  }

  /**
   * Sets the event.
   *
   * @param event
   *          the new event
   */
  public final void setEvent(final Event event) {
    this.event = event;
  }

  /**
   * Gets the bet option.
   *
   * @return the bet option
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "betOptionId")
  public final BetOption getBetOption() {
    return betOption;
  }

  /**
   * Sets the bet option.
   *
   * @param betOption
   *          the new bet option
   */
  public final void setBetOption(final BetOption betOption) {
    this.betOption = betOption;
  }

}
