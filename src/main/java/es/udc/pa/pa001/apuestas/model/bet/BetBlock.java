package es.udc.pa.pa001.apuestas.model.bet;

import java.util.List;

/**
 * The Class BetBlock.
 */
public class BetBlock {

  /** The bets. */
  private List<Bet> bets;

  /** The exist more bets. */
  private boolean existMoreBets;

  /**
   * Instantiates a new bet block.
   *
   * @param bets
   *          the bets
   * @param existMoreBets
   *          the exist more bets
   */
  public BetBlock(List<Bet> bets, boolean existMoreBets) {
    this.bets = bets;
    this.existMoreBets = existMoreBets;
  }

  /**
   * Gets the bets.
   *
   * @return the bets
   */
  public List<Bet> getBets() {
    return bets;
  }

  /**
   * Gets the exist more bets.
   *
   * @return the exist more bets
   */
  public boolean getExistMoreBets() {
    return existMoreBets;
  }

}
