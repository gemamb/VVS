package es.udc.pa.pa001.apuestas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;

public class MyBetsGridDataSource implements GridDataSource{
	
	private BetService betService;
	private Long userProfileId;
	private int startIndex;
	private List<Bet> bets;
	private boolean betNotFound;
	
	public MyBetsGridDataSource(BetService betService, Long userProfileId) {
		super();
		this.betService = betService;
		this.userProfileId = userProfileId;
	}

	@Override
	public int getAvailableRows() {

		return betService.findBetsByUserIdNumber(userProfileId);
	}

	@Override
	public void prepare(int startIndex, int endIndex,
			List<SortConstraint> sortConstraints) {

		bets = betService.findBets(userProfileId, startIndex, endIndex-startIndex+1).getBets();
        this.startIndex = startIndex;
	}

	@Override
	public Object getRowValue(int index) {
		return bets.get(index-this.startIndex);
	}

	@Override
	public Class<Bet> getRowType() {
		return Bet.class;
	}

	public boolean getBetNotFound() {
    	return betNotFound;
    }
}
