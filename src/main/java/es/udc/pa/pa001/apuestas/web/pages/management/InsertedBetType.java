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
  public final Long getBetTypeId() {
    return betTypeId;
  }

  /**
   * Sets the bet type id.
   *
   * @param betTypeId
   *          the new bet type id
   */
  public final void setBetTypeId(final Long betTypeId) {
    this.betTypeId = betTypeId;
  }

  /**
   * On passivate.
   *
   * @return the long
   */
  final Long onPassivate() {
    return betTypeId;
  }

  /**
   * On activate.
   *
   * @param betTypeId
   *          the bet type id
   */
  final void onActivate(final Long betTypeId) {
    this.betTypeId = betTypeId;
  }
}
