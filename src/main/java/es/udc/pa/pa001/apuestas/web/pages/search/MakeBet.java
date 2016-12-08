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

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.model.betservice.util.WrongQuantityException;
import es.udc.pa.pa001.apuestas.web.pages.SuccessfulOperation;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pa.pa001.apuestas.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class MakeBet.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_NO_ADMIN)
public class MakeBet {

  /** The user session. */
  @SessionState(
      create = false)
  private UserSession userSession;

  /** The bet option id. */
  private Long betOptionId;

  /** The beted money as float. */
  private float betedMoneyAsFloat;

  /** The bet option. */
  @Property
  private BetOption betOption;

  /** The beted money. */
  @Property
  private String betedMoney;

  /** The make bet form. */
  @Component
  private Form makeBetForm;

  /** The beted money text field. */
  @Component(
      id = "betedMoney")
  private TextField betedMoneyTextField;

  /** The messages. */
  @Inject
  private Messages messages;

  /** The locale. */
  @Inject
  private Locale locale;

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The successful operation. */
  @InjectPage
  private SuccessfulOperation successfulOperation;

  /**
   * Gets the format.
   *
   * @return the format
   */
  public final DateFormat getFormat() {
    return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT,
        locale);
  }

  /**
   * Gets the bet option id.
   *
   * @return the bet option id
   */
  public final Long getBetOptionId() {
    return betOptionId;
  }

  /**
   * Sets the bet option id.
   *
   * @param betOptionId
   *          the new bet option id
   */
  public final void setbetOptionId(final Long betOptionId) {
    this.betOptionId = betOptionId;
  }

  /**
   * On validate from make bet form.
   */
  final void onValidateFromMakeBetForm() {

    if (!makeBetForm.isValid()) {
      return;
    }

    final NumberFormat numberFormatter = NumberFormat.getInstance(locale);
    final ParsePosition position = new ParsePosition(0);
    final Number number = numberFormatter.parse(betedMoney, position);

    if (position.getIndex() != betedMoney.length()) {
      makeBetForm.recordError(betedMoneyTextField,
          messages.format("error-incorrectNumberFormat", betedMoney));
    } else {
      betedMoneyAsFloat = number.floatValue();
    }

  }

  /**
   * On success.
   *
   * @return the object
   */
  final Object onSuccess() {
    try {
      betService.makeBet(userSession.getUserProfileId(), betOptionId,
          betedMoneyAsFloat);
    } catch (final InstanceNotFoundException e) {
      makeBetForm.recordError(messages.format("error-instanceNotFound"));
    } catch (OutdatedBetException | WrongQuantityException e) {
      makeBetForm.recordError(messages.format("error-outdatedBetException"));
    }
    return successfulOperation;
  }

  /**
   * On activate.
   *
   * @param betOptionId
   *          the bet option id
   */
  final void onActivate(final Long betOptionId) {

    this.betOptionId = betOptionId;

    try {

      this.betOption = betService.findBetOption(betOptionId);

    } catch (final InstanceNotFoundException e) {
    }

  }

  /**
   * On passivate.
   *
   * @return the long
   */
  final Long onPassivate() {
    return betOptionId;
  }

}
