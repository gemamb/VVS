package es.udc.pa.pa001.apuestas.web.util;

import org.apache.tapestry5.ValueEncoder;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class BetOptionEncoder.
 */
public class BetOptionEncoder implements ValueEncoder<BetOption> {

  /** The bet service. */
  private BetService betService;

  /**
   * Instantiates a new bet option encoder.
   *
   * @param betService
   *          the bet service
   */
  public BetOptionEncoder(BetService betService) {
    this.betService = betService;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.ValueEncoder#toClient(java.lang.Object)
   */
  @Override
  public String toClient(BetOption option) {

    return option.getBetOptionId().toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.apache.tapestry5.ValueEncoder#toValue(java.lang.String)
   */
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
