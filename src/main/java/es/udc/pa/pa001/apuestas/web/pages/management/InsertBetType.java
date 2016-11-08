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

/**
 * The Class InsertBetType.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class InsertBetType {

  /** The question. */
  @Property
  private String question;

  /** The multiple. */
  @Property
  private boolean multiple;

  /** The event id. */
  private Long eventId;

  /** The bet type id. */
  private Long betTypeId;

  /**
   * Gets the bet type id.
   *
   * @return the bet type id
   */
  public Long getBetTypeId() {
    return betTypeId;
  }

  /**
   * Sets the bet type id.
   *
   * @param betTypeId
   *          the new bet type id
   */
  public void setBetTypeId(Long betTypeId) {
    this.betTypeId = betTypeId;
  }

  /**
   * Gets the event id.
   *
   * @return the event id
   */
  public Long getEventId() {
    return eventId;
  }

  /**
   * Sets the event id.
   *
   * @param eventId
   *          the new event id
   */
  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The insert bet option. */
  @InjectPage
  private InsertBetOption insertBetOption;

  /** The locale. */
  @Inject
  private Locale locale;

  /** The question text field. */
  @Component(id = "question")
  private TextField questionTextField;

  /** The messages. */
  @Inject
  private Messages messages;

  /** The bet type form. */
  @Component
  private Form betTypeForm;

  /** The bet type. */
  private BetType betType;

  /**
   * On validate from bet type form.
   */
  void onValidateFromBetTypeForm() {
    Event event;

    try {
      event = betService.findEvent(eventId);
      betType = new BetType();

      if (betService.findDuplicates(eventId, question)) {
        betTypeForm.recordError(questionTextField,
            messages.format("error-duplicatedQuestion", question));
        return;
      }

      betType.setMultiple(multiple);
      betType.setQuestion(question);
    } catch (InstanceNotFoundException e) {
      betTypeForm.recordError(messages.format("error-eventNotFound", eventId));
    }

  }

  /**
   * On success.
   *
   * @return the object
   */
  Object onSuccess() {
    insertBetOption.setEventId(eventId);
    insertBetOption.setMultiple(multiple);
    insertBetOption.setQuestion(question);
    return insertBetOption;
  }

  /**
   * On activate.
   *
   * @param eventId
   *          the event id
   */
  void onActivate(Long eventId) {
    this.eventId = eventId;
  }

  /**
   * On passivate.
   *
   * @return the long
   */
  Long onPassivate() {
    return this.eventId;
  }

  /**
   * Gets the format.
   *
   * @return the format
   */
  public Format getFormat() {
    return NumberFormat.getInstance(locale);
  }

}
