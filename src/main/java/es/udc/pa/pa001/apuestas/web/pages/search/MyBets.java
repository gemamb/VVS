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

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_NO_ADMIN)
public class MyBets {

	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private BetService betService;

	@Inject
	private Locale locale;

	private final static int BETS_PER_PAGE = 10;
	private int startIndex = 0;
	private BetBlock betBlock;
	private Bet bet;

	private MyBetsGridDataSource MyBetsGridDataSource;
	
	public MyBetsGridDataSource getMyBetsGridDataSource() {
		return MyBetsGridDataSource;
	}
	
	public int getRowsPerPage() {
		return BETS_PER_PAGE;
	}

	public boolean getPending() {
		return bet.getBetOption().getBetState() == null;
	}

	public boolean getWon() {
		return bet.getBetOption().getBetState() == null ? false : bet.getBetOption().getBetState();
	}

	public Float getGain() {
		return bet.getBetedMoney() * bet.getBetOption().getRate();
	}

	public Bet getBet() {
		return bet;
	}

	public void setBet(Bet bet) {
		this.bet = bet;
	}

	public List<Bet> getBets() {
		return betBlock.getBets();
	}

	public DateFormat getFormat() {
		return DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT, locale);
	}
	
	public Format getNumberFormat() {
		return NumberFormat.getInstance(locale);
	}

	void onActivate(int startIndex) {
		this.startIndex = startIndex;
		betBlock = betService.findBets(userSession.getUserProfileId(),
				startIndex, BETS_PER_PAGE);

		MyBetsGridDataSource = new MyBetsGridDataSource(betService,
				userSession.getUserProfileId());
	}

	Object[] onPassivate() {
		return new Object[] { startIndex };
	}
}