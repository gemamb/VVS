package es.udc.pa.pa001.apuestas.web.pages.search;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.web.pages.management.InsertBetType;
import es.udc.pa.pa001.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class EventDetails {

	@SessionState(create=false)
	private UserSession userSession;
	
	private Long eventId;
	private Long betTypeId;
	private List<BetType> betTypes = new ArrayList<>();
	
	private boolean check;
	
	@Property
	private Event event;
	
	@Property
	private boolean eventStart;
	
	@Property
	private boolean admin;

	@Property
	private BetType betType;

	@Property
	private BetOption betOption;
	
	@Inject
	private BetService betService;

	@Inject
	private Locale locale;
	
	@InjectPage
	private InsertBetType insertBetType;
	
	@InjectPage
	private Options options;
	
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	
	public Long getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(Long betTypeId) {
		this.betTypeId = betTypeId;
	}
	
	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}
	
	public DateFormat getFormatDate(){
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, 
                DateFormat.SHORT,locale);
	}
	
	public List<BetType> getBetTypes(){
		return betTypes;
	}

	void setupRender() {
		this.admin = userSession!=null && userSession.isAdmin();
		this.eventStart = event.finishedEvent(eventId);		
	}

	Object onSuccess(){
		
		insertBetType.setEventId(this.eventId);
		return insertBetType;		
	}
	
	void onActivate(Long eventId) {

		this.eventId = eventId;

		try {
			this.event=betService.findEvent(eventId);
			this.betTypes = this.event.getBetTypes();
		} catch (InstanceNotFoundException e) {
		}
	}
	
	Long onPassivate() {
		return eventId;
	}
}