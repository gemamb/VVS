/**
 *
 */
package es.udc.pa.pa001.apuestas.web.pages.management;

import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetOptionAnswerException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetTypeQuestionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.MinimunBetOptionException;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class InsertBetOption {

	@Property
	private BetOption betOption;

	@InjectComponent
	private Zone counterZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Property
	@Persist
	private List<BetOption> savedBetOptions;

	@Component
	private Form betOptionForm;

	@Component
	private Form betTypeForm;

	@Inject
	private Messages messages;

	@Inject
	private Locale locale;

	@Property
	private String answer;

	@Property
	private String rate;

	@Property
	private Float rateAsFloat;

	@InjectPage
	private InsertBetOption insertBetOption;

	@Component(id = "rate")
	private TextField balanceTextField;

	@Component(id = "answer")
	private TextField answerTextField;

	@InjectPage
	private InsertedBetType insertedBetType;

	@Inject
	private BetService betService;

	@Property
	private final ValueEncoder<BetOption> encoder = new ValueEncoder<BetOption>() {

		@Override
		public String toClient(BetOption value) {
			return value.getAnswer();
		}

		@Override
		public BetOption toValue(String clientValue) {

			int i = 0;
			for (BetOption b : savedBetOptions) {
				if (b.getAnswer().equals(clientValue)) {
					return savedBetOptions.get(i);
				}
				i++;
			}
			return null;
		}
	};

	private Long eventId;
	private boolean multiple;
	private String question;
	private Long betTypeId;

	public Long getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(Long betTypeId) {
		this.betTypeId = betTypeId;
	}

	void onActivate(Long betTypeId) {
		this.betTypeId = betTypeId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	Object[] onPassivate() {
		return new Object[] {eventId, multiple, question };
	}

	void onActivate(Long eventId, boolean multiple, String question) {
		this.eventId = eventId;
		this.multiple = multiple;
		this.question = question;
		this.savedBetOptions = this.savedBetOptions == null ? new ArrayList<BetOption>() : this.savedBetOptions;
	}

	@OnEvent(value = "validate", component = "betOptionForm")
	void onValidateFromBetOptionForm() {

		if (!betOptionForm.isValid()) {
			return;
		}

		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position = new ParsePosition(0);
		Number number = numberFormatter.parse(rate, position);
		if (position.getIndex() != rate.length()) {
			betOptionForm.recordError(balanceTextField, messages.format("error-incorrectNumberFormat", rate));
		} else {
			rateAsFloat = number.floatValue();
		}

		for (BetOption b : savedBetOptions) {
			if (b.getAnswer().equals(answer)) {
				betOptionForm.recordError(answerTextField, messages.format("error-duplicateAnswer", answer));
				return;
			}
		}
	}

	Object onSuccessFromBetOptionForm() {

		BetOption betOption = new BetOption();
		betOption.setAnswer(answer);
		betOption.setRate(rateAsFloat);
		this.savedBetOptions.add(betOption);

		insertBetOption.setEventId(eventId);
		insertBetOption.setMultiple(multiple);
		insertBetOption.setQuestion(question);

		return request.isXHR() ? counterZone.getBody() : null;

		// return insertBetOption;
	}

	@OnEvent(value = "validate", component = "betTypeForm")
	void onValidateFromBetTypeForm() {

		Event event;
		try {
			event = betService.findEvent(eventId);
			BetType betType = new BetType();
			event.addBetType(betType);
			for (BetOption newbetOption : savedBetOptions) {
				betType.addBetOption(newbetOption);
			}
			betType.setMultiple(multiple);
			betType.setQuestion(question);
			betService.insertBetType(betType);
			betTypeId = betType.getBetTypeId();
		} catch (InstanceNotFoundException e) {
			betTypeForm.recordError(messages.format("error-eventNotFound", eventId));
		} catch (DuplicateBetTypeQuestionException e) {
		} catch (DuplicateBetOptionAnswerException e) {
			betTypeForm.recordError(messages.format("error-duplicateAnswer"));
			this.savedBetOptions = null;
		} catch (MinimunBetOptionException e) {
			betTypeForm.recordError(messages.format("error-minimunBetOption"));
		}
	}

	Object onSuccessFromBetTypeForm() {
		this.savedBetOptions = null;
		insertedBetType.setBetTypeId(betTypeId);
		return insertedBetType;
	}

	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}

}