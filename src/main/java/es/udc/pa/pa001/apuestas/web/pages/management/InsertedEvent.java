/**
 *
 */
package es.udc.pa.pa001.apuestas.web.pages.management;

/**
 * @author Gema
 *
 */
public class InsertedEvent {

	private Long eventId;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	Long onPassivate() {
		return eventId;
	}

	void onActivate(Long eventId) {
		this.eventId = eventId;
	}

}