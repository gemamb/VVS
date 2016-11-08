/**
 *
 */
package es.udc.pa.pa001.apuestas.web.pages.management;

/**
 * The Class InsertedBetType.
 *
 */
public class InsertedBetType {

  /** The bet type id. */
  private Long betTypeId;

  /**
   * Gets the bet type id.
   *
   * @return the bet type id
   */
  public Long getBetTypeId() {
    return betTypeId;
  }

  /**
   * Sets the bet type id.
   *
   * @param betTypeId
   *          the new bet type id
   */
  public void setBetTypeId(Long betTypeId) {
    this.betTypeId = betTypeId;
  }

  /**
   * On passivate.
   *
   * @return the long
   */
  Long onPassivate() {
    return betTypeId;
  }

  /**
   * On activate.
   *
   * @param betTypeId
   *          the bet type id
   */
  void onActivate(Long betTypeId) {
    this.betTypeId = betTypeId;
  }
}
