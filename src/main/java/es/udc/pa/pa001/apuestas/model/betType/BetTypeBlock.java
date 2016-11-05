package es.udc.pa.pa001.apuestas.model.betType;

import java.util.List;

public class BetTypeBlock {

	private List<BetType> bets;
	private boolean existMoreBetTypes;

	public BetTypeBlock(List<BetType> bets, boolean existMoreBetTypes) {
		super();
		this.bets = bets;
		this.existMoreBetTypes = existMoreBetTypes;
	}

	public List<BetType> getBets() {
		return bets;
	}

	public void setBets(List<BetType> bets) {
		this.bets = bets;
	}

	public boolean getExistMoreBetTypes() {
		return existMoreBetTypes;
	}

	public void getExistMoreBetTypes(boolean existMoreBetTypes) {
		this.existMoreBetTypes = existMoreBetTypes;
	}

}
