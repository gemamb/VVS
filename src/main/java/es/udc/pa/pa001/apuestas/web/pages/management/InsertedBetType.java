/**
 * 
 */
package es.udc.pa.pa001.apuestas.web.pages.management;

/**
 * @author Gema
 *
 */
public class InsertedBetType {

	private Long betTypeId;
	
	public Long getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(Long betTypeId) {
		this.betTypeId = betTypeId;
	}

	Long onPassivate() {
		return betTypeId;
	}
	
	void onActivate(Long betTypeId) {
		this.betTypeId = betTypeId;
	}
}