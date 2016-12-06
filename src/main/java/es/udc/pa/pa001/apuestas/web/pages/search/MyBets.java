/**
 *
 */
package es.udc.pa.pa001.apuestas.web.pages.search;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetBlock;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.MyBetsGridDataSource;
import es.udc.pa.pa001.apuestas.web.util.UserSession;

/**
 * The Class MyBets.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_NO_ADMIN)
public class MyBets {

  /** The user session. */
  @SessionState(create = false)
  private UserSession userSession;

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The locale. */
  @Inject
  private Locale locale;

  /** The Constant BETS_PER_PAGE. */
  private static final int BETS_PER_PAGE = 10;

  /** The start index. */
  private int startIndex = 0;

  /** The bet block. */
  private BetBlock betBlock;

  /** The bet. */
  private Bet bet;

  /** The My bets grid data source. */
  private MyBetsGridDataSource MyBetsGridDataSource;

  /**
   * Gets the my bets grid data source.
   *
   * @return the my bets grid data source
   */
  public final MyBetsGridDataSource getMyBetsGridDataSource() {
    return MyBetsGridDataSource;
  }

  /**
   * Gets the rows per page.
   *
   * @return the rows per page
   */
  public final int getRowsPerPage() {
    return BETS_PER_PAGE;
  }

  /**
   * Gets the pending.
   *
   * @return the pending
   */
  public final boolean getPending() {
    return bet.getBetOption().getBetState() == null;
  }

  /**
   * Gets the won.
   *
   * @return the won
   */
  public final boolean getWon() {
    if (bet.getBetOption().getBetState() != null) {
      return bet.getBetOption().getBetState();
    }
    return false;
  }

  /**
   * Gets the gain.
   *
   * @return the gain
   */
  public final Float getGain() {
    return bet.getBetedMoney() * bet.getBetOption().getRate();
  }

  /**
   * Gets the bet.
   *
   * @return the bet
   */
  public final Bet getBet() {
    return bet;
  }

  /**
   * Sets the bet.
   *
   * @param bet
   *          the new bet
   */
  public final void setBet(final Bet bet) {
    this.bet = bet;
  }

  /**
   * Gets the bets.
   *
   * @return the bets
   */
  public final List<Bet> getBets() {
    return betBlock.getBets();
  }

  /**
   * Gets the format.
   *
   * @return the format
   */
  public final DateFormat getFormat() {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
        locale);
  }

  /**
   * Gets the number format.
   *
   * @return the number format
   */
  public final Format getNumberFormat() {
    return NumberFormat.getInstance(locale);
  }

  /**
   * On activate.
   *
   * @param startIndex
   *          the start index
   */
  final void onActivate(final int startIndex) {
    this.startIndex = startIndex;
    betBlock = betService.findBets(userSession.getUserProfileId(), startIndex,
        BETS_PER_PAGE);

    MyBetsGridDataSource = new MyBetsGridDataSource(betService,
        userSession.getUserProfileId());
  }

  /**
   * On passivate.
   *
   * @return the object[]
   */
  final Object[] onPassivate() {
    return new Object[] {startIndex };
  }
}
