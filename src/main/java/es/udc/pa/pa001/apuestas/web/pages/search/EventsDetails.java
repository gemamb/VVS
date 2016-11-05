/**
 *
 */
package es.udc.pa.pa001.apuestas.web.pages.search;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.FindEventGridDataSource;
import es.udc.pa.pa001.apuestas.web.util.UserSession;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class EventsDetails {

	private final static int EVENTS_PER_PAGE = 10;

	private String keyWords;
	private int startIndex = 0;
	private EventBlock eventBlock;
	private Event event;
	private Long category;

	@SessionState(create = false)
	private UserSession userSession;

	private boolean admin;

	@Inject
	private Locale locale;

	@Inject
	private BetService betService;

	private FindEventGridDataSource findEventGridDataSource;

	public FindEventGridDataSource getFindEventGridDataSource() {
		return findEventGridDataSource;
	}

	public int getRowsPerPage() {
		return EVENTS_PER_PAGE;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Event> getEvents() {
		return (eventBlock == null) ? null : eventBlock.getEvents();
	}

	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public Long getCategory() {
		return category;
	}

	public void setCategory(Long category) {
		this.category = category;
	}

	// public Object[] getPreviousLinkContext(){
	// return (startIndex-EVENTS_PER_PAGE >=0) ? new
	// Object[]{keyWords,category,startIndex-EVENTS_PER_PAGE} : null;
	// }
	//
	// public Object[] getNextLinkContext(){
	// return eventBlock.getExistMoreEvents() ? new
	// Object[]{keyWords,category,startIndex+EVENTS_PER_PAGE} : null;
	// }

	public DateFormat getFormat() {
		return DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT, locale);
	}

	void onActivate(String keyWords, Long category, int startIndex) {
		this.keyWords = keyWords;
		this.category = category;
		this.startIndex = startIndex;
		boolean admin = userSession != null && userSession.isAdmin();

		findEventGridDataSource = new FindEventGridDataSource(betService,
				keyWords, category, admin);

		// eventBlock = betService.findEvents(keyWords, category,
		// startIndex,EVENTS_PER_PAGE,admin);
	}

	Object[] onPassivate() {
		return new Object[] { keyWords, category, startIndex };
	}

}