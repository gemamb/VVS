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

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.betservice.util.*;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.web.util.BetOptionEncoder;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class CheckWinners.
 */
public class CheckWinners {

  /** The event. */
  @Property
  private Event event;

  /** The bet option. */
  @Property
  private BetOption betOption;

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The locale. */
  @Inject
  private Locale locale;

  /** The messages. */
  @Inject
  private Messages messages;

  /** The select model factory. */
  @Inject
  private SelectModelFactory selectModelFactory;

  /** The options model. */
  @Property
  private SelectModel optionsModel;

  /** The bet type. */
  private BetType betType;

  /** The bet type id. */
  private Long betTypeId;

  /** The bet option id. */
  private Long betOptionId;

  /** The multiple. */
  @Property
  private boolean multiple;

  /** The finals. */
  @Property
  private List<BetOption> finals;

  /** The check list form. */
  @Component
  private Form checkListForm;

  /** The event details. */
  @InjectPage
  private EventDetails eventDetails;

  /** The selected id. */
  @Property
  private Long selectedId;

  /**
   * Gets the bet type.
   *
   * @return the bet type
   */
  public final BetType getBetType() {
    return this.betType;
  }

  /**
   * Gets the bet options.
   *
   * @return the bet options
   */
  public final List<BetOption> getBetOptions() {

    List<BetOption> options = new ArrayList<BetOption>();
    try {
      options = betService.findBetType(betTypeId).getBetOptions();

    } catch (InstanceNotFoundException e) {
    }
    return options;
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
  public final void setBetOptionId(final Long betOptionId) {
    this.betOptionId = betOptionId;
  }

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
   * Gets the format.
   *
   * @return the format
   */
  public final Format getFormat() {
    return NumberFormat.getInstance(locale);
  }

  /**
   * Setup render.
   */
  final void setupRender() {
    this.multiple = betType.getMultiple();

  }

  /**
   * On activate.
   *
   * @param betTypeId
   *          the bet type id
   */
  final void onActivate(final Long betTypeId) {

    this.betTypeId = betTypeId;
    this.betOption = new BetOption();

    try {
      this.betType = betService.findBetType(betTypeId);
    } catch (InstanceNotFoundException e) {
    }

    optionsModel = selectModelFactory.create(getBetOptions(), "answer");
  }

  /**
   * On passivate.
   *
   * @return the long
   */
  final Long onPassivate() {
    return betTypeId;
  }

  /**
   * Gets the option encoder.
   *
   * @return the option encoder
   */
  public final BetOptionEncoder getOptionEncoder() {
    return new BetOptionEncoder(betService);
  }

  /**
   * On validate from check list form.
   */
  @OnEvent(value = "validate", component = "checkListForm")
  final void onValidateFromCheckListForm() {

    List<Long> ids = new ArrayList<>();
    Set<Long> winners = new HashSet<Long>();

    if (betType.getMultiple()) {
      if (finals.isEmpty()) {
        checkListForm.recordError(messages.format("error-notSufficient"));
      }

      for (BetOption b : finals) {
        ids.add(b.getBetOptionId());
      }

      winners.addAll(ids);
    } else {
      if (selectedId == null) {
        checkListForm.recordError(messages.format("error-notSufficient"));
      }

      winners.add(selectedId);
    }

    try {
      betService.checkOptions(betTypeId, winners);
    } catch (InstanceNotFoundException e1) {
    } catch (OnlyOneWonOptionException e1) {
      checkListForm.recordError(messages.format("error-onlyOneWonOption"));
    } catch (NotAllOptionsExistsException e1) {
    }
  }

  /**
   * On success.
   *
   * @return the object
   */
  final Object onSuccess() {

    Long eventId = null;
    try {
      eventId = betService.findBetType(betTypeId).getEvent().getEventId();
    } catch (InstanceNotFoundException e) {
    }

    eventDetails.setEventId(eventId);
    return eventDetails;
  }

}
