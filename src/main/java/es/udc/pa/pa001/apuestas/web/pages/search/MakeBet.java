package es.udc.pa.pa001.apuestas.web.pages.search;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.web.pages.SuccessfulOperation;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_NO_ADMIN)
public class MakeBet {
	@SessionState(create = false)
	private UserSession userSession;

	private Long betOptionId;

	private float betedMoneyAsFloat;

	@Property
	private BetOption betOption;

	@Property
	private String betedMoney;

	@Component
	private Form makeBetForm;

	@Component(id = "betedMoney")
	private TextField betedMoneyTextField;

	@Inject
	private Messages messages;

	@Inject
	private Locale locale;

	@Inject
	private BetService betService;

	@InjectPage
	private SuccessfulOperation successfulOperation;

	public DateFormat getFormat() {
		return DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT, locale);
	}

	public Long getBetOptionId() {
		return betOptionId;
	}

	public void setbetOptionId(Long betOptionId) {
		this.betOptionId = betOptionId;
	}

	void onValidateFromMakeBetForm() {

		if (!makeBetForm.isValid()) {
			return;
		}

		NumberFormat numberFormatter = NumberFormat.getInstance(locale);
		ParsePosition position = new ParsePosition(0);
		Number number = numberFormatter.parse(betedMoney, position);

		if (position.getIndex() != betedMoney.length()) {
			makeBetForm.recordError(betedMoneyTextField,
					messages.format("error-incorrectNumberFormat", betedMoney));
		} else {
			betedMoneyAsFloat = number.floatValue();
		}

	}

	Object onSuccess() {
		try {
			Bet bet = betService.makeBet(userSession.getUserProfileId(),
					betOptionId, betedMoneyAsFloat);
		} catch (InstanceNotFoundException e) {
			makeBetForm.recordError(messages.format("error-instanceNotFound"));
		} catch (OutdatedBetException e) {
			makeBetForm.recordError(messages
					.format("error-outdatedBetException"));
		}
		return successfulOperation;
	}

	void onActivate(Long betOptionId) {

		this.betOptionId = betOptionId;

		try {

			this.betOption = betService.findBetOption(betOptionId);

		} catch (InstanceNotFoundException e) {
		}

	}

	Long onPassivate() {
		return betOptionId;
	}

}