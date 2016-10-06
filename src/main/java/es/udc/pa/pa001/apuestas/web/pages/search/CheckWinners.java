/**
 * 
 */
package es.udc.pa.pa001.apuestas.web.pages.search;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;
import org.jboss.logging.annotations.Message;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.betservice.util.NotAllOptionsExistsException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OnlyOneWonOptionException;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.web.util.BetOptionEncoder;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class CheckWinners {
	
	@Property
	private Event event;
	
	@Property
	private BetOption betOption;
	
	@Inject
	private BetService betService;

	@Inject
	private Locale locale;
	
	@Inject
	private Messages messages;
	
	@Inject
	private SelectModelFactory selectModelFactory;
	
	@Property
	private SelectModel optionsModel;
	
	private BetType betType;
	
	private Long betTypeId;
	private Long betOptionId;
	
	@Property
	private boolean multiple;
    
    @Property
    private List<BetOption> finals;
    
    @Component
	private Form checkListForm;
    
	@InjectPage
	private EventDetails eventDetails;
	
	@Property
	private Long selectedId;
	
	public BetType getBetType(){
		return this.betType;
	}
	
	public List<BetOption> getBetOptions() {

		List<BetOption> options = new ArrayList<BetOption>();
		try {
			options = betService.findBetType(betTypeId).getBetOptions();

		} catch (InstanceNotFoundException e) {
		}
		return options;
	}
	
	public Long getBetOptionId() {
		return betOptionId;
	}

	public void setBetOptionId(Long betOptionId) {
		this.betOptionId = betOptionId;
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
	
	void setupRender() {
		this.multiple = betType.getMultiple();
		
	}
	
	void onActivate(Long betTypeId) {

		this.betTypeId = betTypeId;
		this.betOption = new BetOption();
		
		try {
			this.betType=betService.findBetType(betTypeId);
		} 
		catch (InstanceNotFoundException e) {
		}
		
		optionsModel = selectModelFactory.create(getBetOptions(),"answer");
	}
	
	Long onPassivate() {
		return betTypeId;
	}
	
	public BetOptionEncoder getOptionEncoder() {
        return new BetOptionEncoder(betService);
    }
	
	@OnEvent(value="validate", component="checkListForm")
	void onValidateFromCheckListForm(){
		
		List<Long> ids = new ArrayList<>();
		Set<Long> winners = new HashSet<Long>();
		
		if(betType.getMultiple()){
			if(finals.isEmpty())
				checkListForm.recordError(messages.format(
						"error-notSufficient"));
			
			for(BetOption b: finals){
				ids.add(b.getBetOptionId());
			}
			
			winners.addAll(ids);
		}else {
			if (selectedId==null)
				checkListForm.recordError(messages.format(
						"error-notSufficient"));
			
			winners.add(selectedId);
		}
		
		try {
			betService.checkOptions(betTypeId, winners);
		} catch (InstanceNotFoundException e1) {
		} catch (OnlyOneWonOptionException e1) {
			checkListForm.recordError(messages.format(
					"error-onlyOneWonOption"));
		} catch (NotAllOptionsExistsException e1) {
		}
	}

	Object onSuccess(){
		
		Long eventId = null;
		try {
			eventId = betService.findBetType(betTypeId).getEvent().getEventId();
		} catch (InstanceNotFoundException e) {
		}
		
		eventDetails.setEventId(eventId);
		return eventDetails;		
	}

}