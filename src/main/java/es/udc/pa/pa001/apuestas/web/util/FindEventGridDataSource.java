package es.udc.pa.pa001.apuestas.web.util;

import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.event.Event;

public class FindEventGridDataSource implements GridDataSource {

	private BetService betService;
	private String keyWords;
	private Long categoryId;
	private boolean admin;
	private int startIndex;
	private List<Event> events;
	private boolean eventNotFound;
	
	public FindEventGridDataSource(BetService betService, String keyWords,
			Long categoryId, boolean admin) {
		super();
		this.betService = betService;
		this.keyWords = keyWords;
		this.categoryId = categoryId;
		this.admin = admin;
	}

	@Override
	public int getAvailableRows() {

		return betService.findEventsGetNumber(keyWords, categoryId, admin);
	}

	@Override
	public void prepare(int startIndex, int endIndex,
			List<SortConstraint> sortConstraints) {

		events = betService.findEvents(keyWords, categoryId, startIndex, endIndex-startIndex+1, admin).getEvents();
        this.startIndex = startIndex;
	}

	@Override
	public Object getRowValue(int index) {
		return events.get(index-this.startIndex);
	}

	@Override
	public Class<Event> getRowType() {
		return Event.class;
	}

	public boolean getEventNotFound() {
    	return eventNotFound;
    }
}
