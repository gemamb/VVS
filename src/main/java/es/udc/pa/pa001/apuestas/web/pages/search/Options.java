/**
 * 
 */
package es.udc.pa.pa001.apuestas.web.pages.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * @author Gema
 *
 */
public class Options {
	
	@SessionState(create=false)
	private UserSession userSession;
	
	@Property
	private boolean checked;
	
	@Property
	private boolean eventStart;
	
	@Property
	private BetOption betOption;
	
	@Property
	private boolean admin;
	@Property
	private boolean autenticated;
	
	@Property
	private BetType betType;
	
	private Long betTypeId;

	@Inject
	private BetService betService;
	
	@InjectPage
	private CheckWinners checkWinners;
	
	private List<BetOption> betOptions= new ArrayList<>();
	
	public Long getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(Long betTypeId) {
		this.betTypeId = betTypeId;
	}
	
	public List<BetOption> getbetOptions(){
		return betOptions;
	}
	
	boolean checkOptions(Long betTypeId){
		
		boolean checked=false;
		for(BetOption b: betType.getBetOptions()){
			if (b.getBetState()!=null){
				checked=true;
			}
		}
		return checked;
	}

	void setupRender() {
		this.autenticated = userSession!=null;
		this.admin = userSession!=null && userSession.isAdmin();
		this.checked = checkOptions(betTypeId);
		Event e = betType.getEvent();
		this.eventStart =e.finishedEvent(e.getEventId());		
	}
	
	void onActivate(Long betTypeId) {

		this.betTypeId = betTypeId;

		try {
			this.betType=betService.findBetType(betTypeId);
			this.betOptions = betType.getBetOptions();
		} catch (InstanceNotFoundException e) {
		}
	}
	
	Long onPassivate() {
		return betTypeId;
	}

	Object onSuccess(){
		checkWinners.setBetTypeId(betTypeId);
		return checkWinners;		
	}
}