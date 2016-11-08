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
 * The Class Options.
 *
 */
public class Options {

  /** The user session. */
  @SessionState(create = false)
  private UserSession userSession;

  /** The checked. */
  @Property
  private boolean checked;

  /** The event start. */
  @Property
  private boolean eventStart;

  /** The bet option. */
  @Property
  private BetOption betOption;

  /** The admin. */
  @Property
  private boolean admin;

  /** The autenticated. */
  @Property
  private boolean autenticated;

  /** The bet type. */
  @Property
  private BetType betType;

  /** The bet type id. */
  private Long betTypeId;

  /** The bet service. */
  @Inject
  private BetService betService;

  /** The check winners. */
  @InjectPage
  private CheckWinners checkWinners;

  /** The bet options. */
  private List<BetOption> betOptions = new ArrayList<>();

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
   * Gets the bet options.
   *
   * @return the bet options
   */
  public List<BetOption> getbetOptions() {
    return betOptions;
  }

  /**
   * Check options.
   *
   * @param betTypeId
   *          the bet type id
   * @return true, if successful
   */
  boolean checkOptions(Long betTypeId) {

    boolean checked = false;
    for (BetOption b : betType.getBetOptions()) {
      if (b.getBetState() != null) {
        checked = true;
      }
    }
    return checked;
  }

  /**
   * Setup render.
   */
  void setupRender() {
    this.autenticated = userSession != null;
    this.admin = userSession != null && userSession.isAdmin();
    this.checked = checkOptions(betTypeId);
    Event e = betType.getEvent();
    this.eventStart = e.finishedEvent(e.getEventId());
  }

  /**
   * On activate.
   *
   * @param betTypeId
   *          the bet type id
   */
  void onActivate(Long betTypeId) {

    this.betTypeId = betTypeId;

    try {
      this.betType = betService.findBetType(betTypeId);
      this.betOptions = betType.getBetOptions();
    } catch (InstanceNotFoundException e) {
    }
  }

  /**
   * On passivate.
   *
   * @return the long
   */
  Long onPassivate() {
    return betTypeId;
  }

  /**
   * On success.
   *
   * @return the object
   */
  Object onSuccess() {
    checkWinners.setBetTypeId(betTypeId);
    return checkWinners;
  }
}
