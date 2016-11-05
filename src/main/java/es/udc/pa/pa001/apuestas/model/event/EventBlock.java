package es.udc.pa.pa001.apuestas.model.event;

import java.util.List;

public class EventBlock {

	private List<Event> events;
	private boolean existMoreEvents;

	public EventBlock(List<Event> events, boolean existMoreEvents) {

		this.events = events;
		this.existMoreEvents = existMoreEvents;

	}

	public List<Event> getEvents() {
		return events;
	}

	public boolean getExistMoreEvents() {
		return existMoreEvents;
	}

}
