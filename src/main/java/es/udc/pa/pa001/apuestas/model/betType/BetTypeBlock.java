package es.udc.pa.pa001.apuestas.model.betType;

import java.util.List;

/**
 * The Class BetTypeBlock.
 */
public class BetTypeBlock {

  /** The bets. */
  private List<BetType> bets;

  /** The exist more bet types. */
  private boolean existMoreBetTypes;

  /**
   * Instantiates a new bet type block.
   *
   * @param bets
   *          the bets
   * @param existMoreBetTypes
   *          the exist more bet types
   */
  public BetTypeBlock(List<BetType> bets, boolean existMoreBetTypes) {
    super();
    this.bets = bets;
    this.existMoreBetTypes = existMoreBetTypes;
  }

  /**
   * Gets the bets.
   *
   * @return the bets
   */
  public List<BetType> getBets() {
    return bets;
  }

  /**
   * Sets the bets.
   *
   * @param bets
   *          the new bets
   */
  public void setBets(List<BetType> bets) {
    this.bets = bets;
  }

  /**
   * Gets the exist more bet types.
   *
   * @return the exist more bet types
   */
  public boolean getExistMoreBetTypes() {
    return existMoreBetTypes;
  }

  /**
   * Gets the exist more bet types.
   *
   * @param existMoreBetTypes
   *          the exist more bet types
   * @return the existmorebettypes
   */
  public void getExistMoreBetTypes(boolean existMoreBetTypes) {
    this.existMoreBetTypes = existMoreBetTypes;
  }

}
