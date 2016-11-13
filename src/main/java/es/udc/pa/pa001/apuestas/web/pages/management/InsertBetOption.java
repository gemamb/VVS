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
import es.udc.pa.pa001.apuestas.model.betservice.util.*;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class InsertBetOption.
 */
public class InsertBetOption {

  /** The bet option. */
  @Property
  private BetOption betOption;

  /** The counter zone. */
  @InjectComponent
  private Zone counterZone;

  /** The request. */
  @Inject
  private Request request;

  /** The ajax response renderer. */
  @Inject
  private AjaxResponseRenderer ajaxResponseRenderer;

  /** The saved bet options. */
  @Property
  @Persist
  private List<BetOption> savedBetOptions;

  /** The bet option form. */
  @Component
  private Form betOptionForm;

  /** The bet type form. */
  @Component
  private Form betTypeForm;

  /** The messages. */
  @Inject
  private Messages messages;

  /** The locale. */
  @Inject
  private Locale locale;

  /** The answer. */
  @Property
  private String answer;

  /** The rate. */
  @Property
  private String rate;

  /** The rate as float. */
  @Property
  private Float rateAsFloat;

  /** The insert bet option. */
  @InjectPage
  private InsertBetOption insertBetOption;

  /** The balance text field. */
  @Component(id = "rate")
  private TextField balanceTextField;

  /** The answer text field. */
  @Component(id = "answer")
  private TextField answerTextField;

  /** The inserted bet type. */
  @InjectPage
  private InsertedBetType insertedBetType;

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The encoder. */
  @Property
  private final ValueEncoder<BetOption> encoder =
    new ValueEncoder<BetOption>() {

    @Override
    public String toClient(final BetOption value) {
      return value.getAnswer();
    }

    @Override
    public BetOption toValue(final String clientValue) {

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

  /** The event id. */
  private Long eventId;

  /** The multiple. */
  private boolean multiple;

  /** The question. */
  private String question;

  /** The bet type id. */
  private Long betTypeId;

  /**
   * Gets the bet type id.
   *
   * @return the bet type id
   */
  public final Long getBetTypeId() {
    return betTypeId;
  }

  /**
   * Sets the bet type id.
   *
   * @param betTypeId
   *          the new bet type id
   */
  public final void setBetTypeId(final Long betTypeId) {
    this.betTypeId = betTypeId;
  }

  /**
   * On activate.
   *
   * @param betTypeId
   *          the bet type id
   */
  final void onActivate(final Long betTypeId) {
    this.betTypeId = betTypeId;
  }

  /**
   * Gets the event id.
   *
   * @return the event id
   */
  public final Long getEventId() {
    return eventId;
  }

  /**
   * Sets the event id.
   *
   * @param eventId
   *          the new event id
   */
  public final void setEventId(final Long eventId) {
    this.eventId = eventId;
  }

  /**
   * Checks if is multiple.
   *
   * @return true, if is multiple
   */
  public final boolean isMultiple() {
    return multiple;
  }

  /**
   * Sets the multiple.
   *
   * @param multiple
   *          the new multiple
   */
  public final void setMultiple(final boolean multiple) {
    this.multiple = multiple;
  }

  /**
   * Gets the question.
   *
   * @return the question
   */
  public final String getQuestion() {
    return question;
  }

  /**
   * Sets the question.
   *
   * @param question
   *          the new question
   */
  public final void setQuestion(final String question) {
    this.question = question;
  }

  /**
   * On passivate.
   *
   * @return the object[]
   */
  final Object[] onPassivate() {
    return new Object[] {eventId, multiple, question};
  }

  /**
   * On activate.
   *
   * @param eventId
   *          the event id
   * @param multiple
   *          the multiple
   * @param question
   *          the question
   */
  final void onActivate(final Long eventId, final boolean multiple,
      final String question) {
    this.eventId = eventId;
    this.multiple = multiple;
    this.question = question;
    this.savedBetOptions = this.savedBetOptions == null
        ? new ArrayList<BetOption>() : this.savedBetOptions;
  }

  /**
   * On validate from bet option form.
   */
  @OnEvent(value = "validate", component = "betOptionForm")
  final void onValidateFromBetOptionForm() {

    if (!betOptionForm.isValid()) {
      return;
    }

    NumberFormat numberFormatter = NumberFormat.getInstance(locale);
    ParsePosition position = new ParsePosition(0);
    Number number = numberFormatter.parse(rate, position);
    if (position.getIndex() != rate.length()) {
      betOptionForm.recordError(balanceTextField,
          messages.format("error-incorrectNumberFormat", rate));
    } else {
      rateAsFloat = number.floatValue();
    }

    for (BetOption b : savedBetOptions) {
      if (b.getAnswer().equals(answer)) {
        betOptionForm.recordError(answerTextField,
            messages.format("error-duplicateAnswer", answer));
        return;
      }
    }
  }

  /**
   * On success from bet option form.
   *
   * @return the object
   */
  final Object onSuccessFromBetOptionForm() {

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

  /**
   * On validate from bet type form.
   */
  @OnEvent(value = "validate", component = "betTypeForm")
  final void onValidateFromBetTypeForm() {

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

  /**
   * On success from bet type form.
   *
   * @return the object
   */
  final Object onSuccessFromBetTypeForm() {
    this.savedBetOptions = null;
    insertedBetType.setBetTypeId(betTypeId);
    return insertedBetType;
  }

  /**
   * Gets the format.
   *
   * @return the format
   */
  public final Format getFormat() {
    return NumberFormat.getInstance(locale);
  }

}
