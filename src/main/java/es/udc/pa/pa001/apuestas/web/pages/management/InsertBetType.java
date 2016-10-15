/**
 * 
 */
package es.udc.pa.pa001.apuestas.web.pages.management;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;


@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class InsertBetType {

	
	@Property
	private String question;

	@Property
	private boolean multiple;

	private Long eventId;
	private Long betTypeId;

	public Long getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(Long betTypeId) {
		this.betTypeId = betTypeId;
	}
	
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	@Inject
	private BetService betService;

	@InjectPage
	private InsertBetOption insertBetOption;

	@Inject
	private Locale locale;

	@Component(id = "question")
	private TextField questionTextField;

	@Inject
	private Messages messages;

	@Component
	private Form betTypeForm;

	private BetType betType;
	
	void onValidateFromBetTypeForm() {
		Event event;

		try {
			event = betService.findEvent(eventId);
			betType = new BetType();
			
			if (betService.findDuplicates(eventId, question)){
				betTypeForm.recordError(questionTextField, messages.format(
						"error-duplicatedQuestion", question));
				return;
			}
			
			betType.setMultiple(multiple);
			betType.setQuestion(question);
		} catch (InstanceNotFoundException e) {
			betTypeForm.recordError(messages.format("error-eventNotFound", eventId));
		}
		
	}

	Object onSuccess(){
		insertBetOption.setEventId(eventId);
		insertBetOption.setMultiple(multiple);
		insertBetOption.setQuestion(question);
		return insertBetOption;
	}

	
	void onActivate(Long eventId) {
		this.eventId = eventId;
	}

	Long onPassivate() {
		return this.eventId;
	}

	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}

}