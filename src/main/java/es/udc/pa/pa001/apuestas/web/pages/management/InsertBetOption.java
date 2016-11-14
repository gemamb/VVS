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
  private final ValueEncoder<BetOption> encoder = new ValueEncoder<BetOption>() {

    @Override
    public String toClient(final BetOption value) {
      return value.getAnswer();
    }

    @Override
    public BetOption toValue(final String clientValue) {

      int i = 0;
      for (final BetOption b : savedBetOptions) {
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
    return new Object[] { eventId, multiple, question };
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
    if (this.savedBetOptions == null) {
      this.savedBetOptions = new ArrayList<BetOption>();
    }
  }

  /**
   * On validate from bet option form.
   */
  @OnEvent(value = "validate", component = "betOptionForm")
  final void onValidateFromBetOptionForm() {

    if (!betOptionForm.isValid()) {
      return;
    }

    final NumberFormat numberFormatter = NumberFormat.getInstance(locale);
    final ParsePosition position = new ParsePosition(0);
    final Number number = numberFormatter.parse(rate, position);
    if (position.getIndex() != rate.length()) {
      betOptionForm.recordError(balanceTextField,
          messages.format("error-incorrectNumberFormat", rate));
    } else {
      rateAsFloat = number.floatValue();
    }

    for (final BetOption b : savedBetOptions) {
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

    final BetOption betOption = new BetOption();
    betOption.setAnswer(answer);
    betOption.setRate(rateAsFloat);
    this.savedBetOptions.add(betOption);

    insertBetOption.setEventId(eventId);
    insertBetOption.setMultiple(multiple);
    insertBetOption.setQuestion(question);
    if (request.isXHR()) {
      return counterZone.getBody();
    }
    return null;
  }

  /**
   * On validate from bet type form.
   */
  @OnEvent(value = "validate", component = "betTypeForm")
  final void onValidateFromBetTypeForm() {

    Event event;
    try {
      event = betService.findEvent(eventId);
      final BetType betType = new BetType();
      event.addBetType(betType);
      for (final BetOption newbetOption : savedBetOptions) {
        betType.addBetOption(newbetOption);
      }
      betType.setMultiple(multiple);
      betType.setQuestion(question);
      betService.insertBetType(betType);
      betTypeId = betType.getBetTypeId();
    } catch (final InstanceNotFoundException e) {
      betTypeForm.recordError(messages.format("error-eventNotFound", eventId));
    } catch (final DuplicateBetTypeQuestionException e) {
    } catch (final DuplicateBetOptionAnswerException e) {
      betTypeForm.recordError(messages.format("error-duplicateAnswer"));
      this.savedBetOptions = null;
    } catch (final MinimunBetOptionException e) {
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
