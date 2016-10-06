package es.udc.pa.pa001.apuestas.web.util;

import org.apache.tapestry5.ValueEncoder;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class BetOptionEncoder implements ValueEncoder<BetOption> {
	
	private BetService betService;
	
	public BetOptionEncoder(BetService betService) {
		this.betService = betService;
	}

	@Override
	public String toClient(BetOption option) {
		
		return option.getBetOptionId().toString();
	}

	@Override
	public BetOption toValue(String betOptionIdAsString) {
		Long bet = Long.valueOf(betOptionIdAsString).longValue();
		try {
			return betService.findBetOption(bet);
		} catch (InstanceNotFoundException e) {

		}
		return null;
	}

}
