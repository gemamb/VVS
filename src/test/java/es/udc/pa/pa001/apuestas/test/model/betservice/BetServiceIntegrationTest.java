package es.udc.pa.pa001.apuestas.test.model.betservice;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetBlock;
import es.udc.pa.pa001.apuestas.model.bet.BetDao;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betOption.BetOptionDao;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betType.BetTypeDao;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.betservice.util.AlreadyPastedDateException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetOptionAnswerException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetTypeQuestionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateEventNameException;
import es.udc.pa.pa001.apuestas.model.betservice.util.MinimunBetOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.NotAllOptionsExistsException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OnlyOneWonOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.model.betservice.util.WrongQuantityException;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.category.CategoryDao;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class BetServiceIntegrationTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    locations = { SPRING_CONFIG_FILE,
        SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetServiceIntegrationTest {

  /** The category dao. */
  @Autowired
  private CategoryDao categoryDao;

  /** The event dao. */
  @Autowired
  private EventDao eventDao;

  /** The bet type dao. */
  @Autowired
  private BetTypeDao betTypeDao;

  /** The bet option dao. */
  @Autowired
  private BetOptionDao betOptionDao;

  /** The user profile dao. */
  @Autowired
  private UserProfileDao userProfileDao;

  /** The bet dao. */
  @Autowired
  private BetDao betDao;

  /** The bet service. */
  @Autowired
  private BetService betService;

  /** The event calendar. */
  Calendar eventCalendar;

  /** The category 2. */
  Category category1, category2;

  /** The event 3. */
  Event event1, event2, event3;

  /** The duplicated bet type. */
  BetType betType, duplicatedBetType;

  /** The bet option 2. */
  BetOption betOption1, betOption2;

  /** The user. */
  UserProfile user;

  /**
   * Initialize date.
   */
  private void initializeDate() {
    eventCalendar = Calendar.getInstance();
    eventCalendar.set(2017, Calendar.AUGUST, 31);
  }

  /**
   * Initialize category.
   */
  private void initializeCategory() {
    category1 = new Category("Baloncesto");
  }

  /**
   * Initialize categories.
   */
  private void initializeCategories() {
    category1 = new Category("Baloncesto");
    categoryDao.save(category1);
    category2 = new Category("Futbol");
    categoryDao.save(category2);
  }

  /**
   * Initialize event.
   */
  private void initializeEvent() {
    event1 = new Event("Real Madrid - Barcelona", eventCalendar, category1);
  }

  /**
   * Initialize bet type.
   */
  private void initializeBetType() {
    betType = new BetType("¿Qué equipo ganará el encuentro?", false);
  }

  /**
   * Initialize duplicated bet type.
   */
  private void initializeDuplicatedBetType() {
    duplicatedBetType = new BetType("¿Qué equipo ganará el encuentro?", false);

    BetOption option1, option2;
    option1 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);
    option2 = new BetOption("Barcelona", (float) 1.75, null, betType);

    final List<BetOption> betOptions = new ArrayList<>();
    betOptions.add(option1);
    betOptions.add(option2);

    duplicatedBetType.setBetOptions(betOptions);
    event1.addBetType(duplicatedBetType);
  }

  /**
   * Initialize bet options.
   */
  private void initializeBetOptions() {
    betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);
    betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);
  }

  /**
   * Initialize bet type with bet options.
   */
  private void initializeBetTypeWithBetOptions() {
    betType = new BetType("¿Qué equipo ganará el encuentro?", false);
    betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);
    betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);
    final List<BetOption> options = new ArrayList<BetOption>();
    options.add(betOption1);
    options.add(betOption2);
    betType.setBetOptions(options);
  }

  /**
   * Initialize bet type duplicated bet options.
   */
  private void initializeBetTypeDuplicatedBetOptions() {
    betType = new BetType("¿Qué equipo ganará el encuentro?", false);
    betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);
    betOption2 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);
    final List<BetOption> options = new ArrayList<BetOption>();
    options.add(betOption1);
    options.add(betOption2);
    betType.setBetOptions(options);
  }

  /**
   * Initialize bet type with one option.
   */
  private void initializeBetTypeWithOneOption() {
    betType = new BetType("¿Qué equipo ganará el encuentro?", false);
    betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);
    final List<BetOption> options = new ArrayList<BetOption>();
    options.add(betOption1);
    betType.setBetOptions(options);
  }

  /**
   * Initialize user.
   */
  private void initializeUser() {
    user = new UserProfile("pepe6", "XxXyYyZzZ", "Pepe", "García",
        "pepe6@gmail.com");
    userProfileDao.save(user);
  }

  /**
   * Initialize created event.
   */
  private void initializeCreatedEvent() {
    initializeDate();
    initializeCategory();
    categoryDao.save(category1);

    initializeEvent();
    eventDao.save(event1);

  }

  /**
   * Initialize created events.
   */
  private void initializeCreatedEvents() {

    initializeDate();

    event1 = new Event("Madrid - Barcelona", eventCalendar, category1);
    eventDao.save(event1);

    event2 = new Event("Deportivo - Celta", eventCalendar, category2);
    eventDao.save(event2);

    event3 = new Event("Madrid - Barcelona", eventCalendar, category2);
    eventDao.save(event3);
  }

  /**
   * Initialize created bet type.
   */
  private void initializeCreatedBetType() {

    initializeBetType();
    initializeBetOptions();

    final List<BetOption> betOptions = new ArrayList<>();
//    betOptions.add(betOption1);
//    betOptions.add(betOption2);

    betType.setBetOptions(betOptions);
    event1.addBetType(betType);
    betTypeDao.save(betType);

    betOptionDao.save(betOption1);
    betOptionDao.save(betOption2);
    
    betType.addBetOption(betOption1);
    betType.addBetOption(betOption2);
    betTypeDao.save(betType);

    event1.addBetType(betType);
  }

  /**
   * Initialize created multiple bet type.
   */
  private void initializeCreatedMultipleBetType() {

    betType = new BetType("¿Qué equipo ganará el encuentro?", true);
    initializeBetOptions();

    final List<BetOption> betOptions = new ArrayList<>();
    betOptions.add(betOption1);
    betOptions.add(betOption2);

    betType.setBetOptions(betOptions);
    event1.addBetType(betType);
    betTypeDao.save(betType);

    betOptionDao.save(betOption1);
    betOptionDao.save(betOption2);

    event1.addBetType(betType);
  }

  /**
   * PR-IN-001
   *
   * insertEvent findEvent insertBetType (no multi) insertBetOption.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   */

  @Test
  public final void testCreationScenary()
      throws InstanceNotFoundException, AlreadyPastedDateException,
      DuplicateEventNameException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException {

    initializeDate();
    initializeCategory();
    categoryDao.save(category1);

    /* Crear un evento */

    initializeEvent();
    event1 = betService.insertEvent(event1, category1.getCategoryId());
    final Event eventAssert = eventDao.find(event1.getEventId());

    assertEquals(event1, eventAssert);

    /* Buscar a un evento */

    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);

    /* Crear un tipo de apuesta con dos opciones de apuesta */

    initializeBetType();
    initializeBetOptions();

    final List<BetOption> betOptions = new ArrayList<>();
    betOptions.add(betOption1);
    betOptions.add(betOption2);

    betType.setBetOptions(betOptions);
    event1.addBetType(betType);

    betType = betService.insertBetType(betType);

    final BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
    final BetOption betOption1Assert = betOptionDao.find(betOption1
        .getBetOptionId());
    final BetOption betOption2Assert = betOptionDao.find(betOption2
        .getBetOptionId());

    assertEquals(betType, betTypeAssert);
    assertEquals(betOption1, betOption1Assert);
    assertEquals(betOption2, betOption2Assert);
  }

  /**
   * PR-IN-002
   *
   * insertEvent findEvent insertBetType (no multi) insertBetOption
   * checkWinners.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   * @throws OnlyOneWonOptionException
   *           the only one won option exception
   * @throws NotAllOptionsExistsException
   *           the not all options exists exception
   */

  @Test
  public final void testCheckingWinnersScenary()
      throws InstanceNotFoundException, AlreadyPastedDateException,
      DuplicateEventNameException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException,
      OnlyOneWonOptionException, NotAllOptionsExistsException {

    initializeDate();
    initializeCategory();
    categoryDao.save(category1);

    /* Crear un evento */

    initializeEvent();
    event1 = betService.insertEvent(event1, category1.getCategoryId());
    final Event eventAssert = eventDao.find(event1.getEventId());

    assertEquals(event1, eventAssert);

    /* Buscar a un evento */

    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);

    /* Crear un tipo de apuesta con dos opciones de apuesta */

    initializeBetType();
    initializeBetOptions();

    final List<BetOption> betOptions = new ArrayList<>();
    betOptions.add(betOption1);
    betOptions.add(betOption2);

    betType.setBetOptions(betOptions);
    event1.addBetType(betType);

    betType = betService.insertBetType(betType);

    final BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());

    assertEquals(betType, betTypeAssert);

    final BetOption betOption1Assert = betOptionDao.find(betOption1
        .getBetOptionId());
    final BetOption betOption2Assert = betOptionDao.find(betOption2
        .getBetOptionId());
    assertEquals(betOption1, betOption1Assert);
    assertEquals(betOption2, betOption2Assert);

    /* Marcar como ganadora una opción de apuesta */

    final Set<Long> winners = new HashSet<Long>();
    winners.add(betOption1.getBetOptionId());

    betService.checkOptions(betType.getBetTypeId(), winners);

    assertTrue(betOption1.getBetState());
    assertFalse(betOption2.getBetState());

  }

  /**
   * PR-IN-003
   *
   * findEvents makeBet findBets.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws WrongQuantityException
   */

  @Test
  public final void testMakeBetScenary()
      throws InstanceNotFoundException, OutdatedBetException,
      WrongQuantityException {

    /* SETUP */

    initializeUser();
    initializeCategories();
    initializeCreatedEvents();
    initializeCreatedBetType();

    /* Búsqueda de eventos */

    final EventBlock foundEvents = betService.findEvents("madrid",
        category1.getCategoryId(), 0, 2, false);
    final Event foundEvent = foundEvents.getEvents().get(0);

    assertEquals(event1, foundEvent);

    /* Realización de la apuesta */

    final BetOption option = foundEvent.getBetTypes().get(0).getBetOptions()
        .get(0);
    final float quantity = 10;
    final Bet bet = betService.makeBet(user.getUserProfileId(),
        option.getBetOptionId(), quantity);

    /* Búsqueda de apuestas */

    final BetBlock foundBets = betService.findBets(user.getUserProfileId(), 0,
        1);
    final Bet foundBet = foundBets.getBets().get(0);
    assertEquals(bet, foundBet);
  }

  /**
   * PR-IN-004
   *
   * findEvents makeBet findBets.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws OnlyOneWonOptionException
   *           the only one won option exception
   * @throws NotAllOptionsExistsException
   *           the not all options exists exception
   * @throws WrongQuantityException
   */

  @Test
  public final void testCheckBetStatusScenary()
      throws InstanceNotFoundException, OutdatedBetException,
      OnlyOneWonOptionException, NotAllOptionsExistsException,
      WrongQuantityException {

    initializeUser();
    initializeCategories();
    initializeCreatedEvents();
    initializeCreatedBetType();

    /* Realizar apuesta */

    final float quantity = 10;
    final BetOption option = betType.getBetOptions().get(0);
    betService.makeBet(user.getUserProfileId(), option.getBetOptionId(),
        quantity);

    /* Cambiamos fecha del evento */

    final Calendar pastDate = Calendar.getInstance();
    pastDate.set(2016, Calendar.AUGUST, 31);
    event1.setEventStart(pastDate);

    /* Establecemos opciones ganadoras en el evento */

    final Set<Long> winned = new HashSet<Long>();
    winned.add(option.getBetOptionId());
    betService.checkOptions(betType.getBetTypeId(), winned);

    /* Búsqueda de apuestas */

    final BetBlock foundBets = betService.findBets(user.getUserProfileId(), 0,
        1);
    final Bet foundBet = foundBets.getBets().get(0);

    /* Comprobamos que la apuesta está ganada */

    assertTrue(foundBet.getBetOption().getBetState());

  }

  /**
   * PR-IN-005.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws WrongQuantityException
   */

  @Test
  public final void testMakeBet() throws InstanceNotFoundException,
      OutdatedBetException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException,
      AlreadyPastedDateException, DuplicateEventNameException,
      WrongQuantityException {

    /* Buscar evento ya existente */

    initializeCreatedEvent();
    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);

    /* Crear un tipo de apuesta con dos opciones de apuesta */

    initializeCreatedBetType();
    final BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
    final BetOption betOption1Assert = betOptionDao.find(betOption1
        .getBetOptionId());
    final BetOption betOption2Assert = betOptionDao.find(betOption2
        .getBetOptionId());

    assertEquals(betType, betTypeAssert);
    assertEquals(betOption1, betOption1Assert);
    assertEquals(betOption2, betOption2Assert);
    assertEquals(betOption1.getBetType(), betType);
    assertEquals(betOption2.getBetType(), betType);
    
    /* Apostar sobre una opcion de apuesta */

    initializeUser();

    final Bet bet = betService.makeBet(user.getUserProfileId(),
        betOption1.getBetOptionId(), (float) 2);
    final Bet betFound = betDao.find(bet.getBetId());
    assertEquals(bet, betFound);

    final List<Bet> betFounds = betDao.findBetsByUserId(user.getUserProfileId(),
        0,
        10);
    assertTrue(betFounds.contains(bet));
  }

  /**
   * PR-IN-006.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws WrongQuantityException
   */

  @Test(
      expected = InstanceNotFoundException.class)
  public final void testMakeBetWrongUser() throws InstanceNotFoundException,
      OutdatedBetException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException,
      AlreadyPastedDateException, DuplicateEventNameException,
      WrongQuantityException {

    /* Buscar evento ya existente */

    initializeCreatedEvent();
    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);
    assertFalse(event1.finishedEvent(event1.getEventId()));

    /* Crear un tipo de apuesta con dos opciones de apuesta */

    initializeCreatedBetType();
    final BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
    final BetOption betOption1Assert = betOptionDao.find(betOption1
        .getBetOptionId());
    final BetOption betOption2Assert = betOptionDao.find(betOption2
        .getBetOptionId());

    assertEquals(betType, betTypeAssert);
    assertEquals(betOption1, betOption1Assert);
    assertEquals(betOption2, betOption2Assert);

    /* Apostar con usuario no existente */

    betService.makeBet(-1L, betOption1.getBetOptionId(), (float) 2);

  }

  /**
   * PR-IN-007.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws WrongQuantityException
   */

  @Test(
      expected = InstanceNotFoundException.class)
  public final void testMakeBetWrongBetOption()
      throws InstanceNotFoundException, OutdatedBetException,
      WrongQuantityException {

    /* Buscar evento ya existente */

    initializeCreatedEvent();
    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);

    /* Crear un tipo de apuesta con dos opciones de apuesta */

    initializeCreatedBetType();
    final BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
    final BetOption betOption1Assert = betOptionDao.find(betOption1
        .getBetOptionId());
    final BetOption betOption2Assert = betOptionDao.find(betOption2
        .getBetOptionId());

    assertEquals(betType, betTypeAssert);
    assertEquals(betOption1, betOption1Assert);
    assertEquals(betOption2, betOption2Assert);

    /* Apostar con opcion de apuesta no existente */

    initializeUser();
    betService.makeBet(user.getUserProfileId(), -1L, (float) 2);

  }

  /**
   * PR-IN-008.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws WrongQuantityException
   */

  @Test(
      expected = OutdatedBetException.class)
  public final void testOutDatedTrueMakeBet()
      throws InstanceNotFoundException, OutdatedBetException,
      WrongQuantityException {

    /* Buscar evento ya existente */

    initializeCreatedEvent();
    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);

    /* Crear un tipo de apuesta con dos opciones de apuesta */

    initializeBetType();

    betOption1 = new BetOption("Real Madrid CF", (float) 1.75, true, betType);
    betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);

    final List<BetOption> betOptions = new ArrayList<>();
    betOptions.add(betOption1);
    betOptions.add(betOption2);

    betType.setBetOptions(betOptions);
    event1.addBetType(betType);
    betTypeDao.save(betType);

    betOptionDao.save(betOption1);
    betOptionDao.save(betOption2);

    /* Apostar sobre opción de apuesta ya ganadora */

    initializeUser();

    betService.makeBet(user.getUserProfileId(), betOption1.getBetOptionId(),
        (float) 2);

  }

  /**
   * PR-IN-009.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws WrongQuantityException
   */

  @Test(
      expected = OutdatedBetException.class)
  public final void testOutDatedFalseMakeBet()
      throws InstanceNotFoundException, OutdatedBetException,
      WrongQuantityException {

    /* Buscar evento ya existente */

    initializeCreatedEvent();
    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);

    /* Crear un tipo de apuesta con dos opciones de apuesta */

    initializeBetType();

    betOption1 = new BetOption("Real Madrid CF", (float) 1.75, false, betType);
    betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);

    final List<BetOption> betOptions = new ArrayList<>();
    betOptions.add(betOption1);
    betOptions.add(betOption2);

    betType.setBetOptions(betOptions);
    event1.addBetType(betType);
    betTypeDao.save(betType);

    betOptionDao.save(betOption1);
    betOptionDao.save(betOption2);

    /* Apostar sobre opción de apuesta ya ganadora */

    initializeUser();

    betService.makeBet(user.getUserProfileId(), betOption1.getBetOptionId(),
        (float) 2);

  }

  /**
   * PR-IN-010.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws WrongQuantityException
   */

  @Test(
      expected = WrongQuantityException.class)
  public final void testMakeBetWrongMoney()
      throws InstanceNotFoundException, OutdatedBetException,
      WrongQuantityException {

    /* Buscar evento ya existente */

    initializeCreatedEvent();
    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);

    /* Crear un tipo de apuesta con dos opciones de apuesta */

    initializeCreatedBetType();
    final BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
    final BetOption betOption1Assert = betOptionDao.find(betOption1
        .getBetOptionId());
    final BetOption betOption2Assert = betOptionDao.find(betOption2
        .getBetOptionId());

    assertEquals(betType, betTypeAssert);
    assertEquals(betOption1, betOption1Assert);
    assertEquals(betOption2, betOption2Assert);

    /* Apostar sobre una opcion de apuesta */

    initializeUser();

    betService.makeBet(user.getUserProfileId(), betOption1.getBetOptionId(),
        (float) -2);
  }

  /**
   * PR-IN-011.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   */

  @Test
  public final void testInsertEvent() throws InstanceNotFoundException,
      AlreadyPastedDateException, DuplicateEventNameException {

    initializeDate();
    initializeCategory();
    categoryDao.save(category1);

    /* Crear un evento */

    initializeEvent();
    event1 = betService.insertEvent(event1, category1.getCategoryId());
    final Event eventAssert = eventDao.find(event1.getEventId());

    assertEquals(event1, eventAssert);
  }

  /**
   * PR-IN-012.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   */

  @Test(
      expected = AlreadyPastedDateException.class)
  public final void testInsertPastedEvent() throws InstanceNotFoundException,
      AlreadyPastedDateException, DuplicateEventNameException {

    eventCalendar = Calendar.getInstance();
    eventCalendar.set(2014, Calendar.AUGUST, 31);

    initializeCategory();
    categoryDao.save(category1);

    /* Crear un evento */

    event1 = new Event("Real Madrid - Barcelona", eventCalendar, category1);
    betService.insertEvent(event1, category1.getCategoryId());
  }

  /**
   * PR-UN-013.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   */

  @Test(
      expected = DuplicateEventNameException.class)
  public final void testInsertDuplicateEvent() throws InstanceNotFoundException,
      AlreadyPastedDateException, DuplicateEventNameException {

    initializeCreatedEvent();
    final Event eventFound = betService.findEvent(event1.getEventId());

    assertEquals(event1, eventFound);

    final Event duplicatedEvent = event1 = new Event("Real Madrid - Barcelona",
        eventCalendar, category1);

    betService.insertEvent(duplicatedEvent, category1.getCategoryId());
  }

  /**
   * PR-UN-014.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   */

  @Test(
      expected = InstanceNotFoundException.class)
  public final void testInsertEventWrongCategory()
      throws InstanceNotFoundException,
      AlreadyPastedDateException, DuplicateEventNameException {

    initializeDate();
    category1 = new Category("Baloncesto");

    event1 = new Event("Real Madrid - Barcelona", eventCalendar, category1);

    betService.insertEvent(event1, 2L);
  }

  /**
   * PR-UN-015.
   *
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   */

  @Test
  public final void testInsertBetType()
      throws AlreadyPastedDateException, InstanceNotFoundException,
      DuplicateEventNameException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException {

    initializeCreatedEvent();
    initializeBetTypeWithBetOptions();
    event1.addBetType(betType);

    /* Insertamos tipo de apuesta */

    final BetType createdBetType = betService.insertBetType(betType);

    assertEquals(betType, createdBetType);

  }

  /**
   * PR-UN-016.
   *
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   */

  @Test(
      expected = MinimunBetOptionException.class)
  public final void testInsertBetTypeWithoutOptions()
      throws AlreadyPastedDateException, InstanceNotFoundException,
      DuplicateEventNameException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException {

    initializeCreatedEvent();
    initializeBetType();
    event1.addBetType(betType);

    /* Insertamos tipo de apuesta */

    betService.insertBetType(betType);

  }

  /**
   * PR-UN-017.
   *
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   */

  @Test(
      expected = MinimunBetOptionException.class)
  public final void testInsertBetTypeWithOneOption()
      throws AlreadyPastedDateException, InstanceNotFoundException,
      DuplicateEventNameException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException {

    initializeCreatedEvent();
    initializeBetTypeWithOneOption();
    event1.addBetType(betType);

    /* Insertamos tipo de apuesta */

    betService.insertBetType(betType);

  }

  /**
   * PR-UN-018.
   *
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   */

  @Test(
      expected = DuplicateBetOptionAnswerException.class)
  public final void testInsertBetTypeDuplicateAnswer()
      throws AlreadyPastedDateException, InstanceNotFoundException,
      DuplicateEventNameException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException {

    initializeCreatedEvent();
    initializeBetTypeDuplicatedBetOptions();
    event1.addBetType(betType);

    /* Insertamos tipo de apuesta */

    betService.insertBetType(betType);

  }

  /**
   * PR-UN-019.
   *
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   */

  @Test(
      expected = DuplicateBetTypeQuestionException.class)
  public final void testInsertBetTypeDuplicateQuestion()
      throws AlreadyPastedDateException, InstanceNotFoundException,
      DuplicateEventNameException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException {

    initializeCreatedEvent();
    initializeCreatedBetType();
    initializeDuplicatedBetType();

    /* Insertamos tipo de apuesta */

    betService.insertBetType(duplicatedBetType);

  }

  /**
   * PR-UN-020.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OnlyOneWonOptionException
   *           the only one won option exception
   * @throws NotAllOptionsExistsException
   *           the not all options exists exception
   */

  @Test
  public final void testCheckOptionsSimpleBetType()
      throws InstanceNotFoundException,
      OnlyOneWonOptionException, NotAllOptionsExistsException {

    initializeCreatedEvent();
    initializeCreatedBetType();

    final Set<Long> winners = new HashSet<Long>();
    winners.add(betOption1.getBetOptionId());

    betService.checkOptions(betType.getBetTypeId(), winners);

  }

  /**
   * PR-UN-021.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OnlyOneWonOptionException
   *           the only one won option exception
   * @throws NotAllOptionsExistsException
   *           the not all options exists exception
   */

  @Test
  public final void testCheckOptionsMultipleBetType()
      throws InstanceNotFoundException, OnlyOneWonOptionException,
      NotAllOptionsExistsException {

    initializeCreatedEvent();
    initializeCreatedMultipleBetType();

    final Set<Long> winners = new HashSet<Long>();
    winners.add(betOption1.getBetOptionId());
    winners.add(betOption2.getBetOptionId());

    betService.checkOptions(betType.getBetTypeId(), winners);

  }

  /**
   * PR-UN-022.
   *
   * @throws NotAllOptionsExistsException
   *           the not all options exists exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OnlyOneWonOptionException
   *           the only one won option exception
   */

  @Test(
      expected = NotAllOptionsExistsException.class)
  public final void testCheckInvalidOptions()
      throws NotAllOptionsExistsException,
      InstanceNotFoundException, OnlyOneWonOptionException {

    initializeCreatedEvent();
    initializeCreatedBetType();
    final Long nonExistentBetOptionId = 0L;

    final Set<Long> winners = new HashSet<Long>();
    winners.add(nonExistentBetOptionId);

    betService.checkOptions(betType.getBetTypeId(), winners);
  }

  /**
   * PR-UN-023.
   *
   * @throws OnlyOneWonOptionException
   *           the only one won option exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws NotAllOptionsExistsException
   *           the not all options exists exception
   */

  @Test(
      expected = OnlyOneWonOptionException.class)
  public final void testCheckMultipleOptionsSimpleBetType()
      throws OnlyOneWonOptionException, InstanceNotFoundException,
      NotAllOptionsExistsException {

    initializeCreatedEvent();
    initializeCreatedBetType();

    final Set<Long> winners = new HashSet<Long>();
    winners.add(betOption1.getBetOptionId());
    winners.add(betOption2.getBetOptionId());

    betService.checkOptions(betType.getBetTypeId(), winners);
  }

  /**
   * PR-UN-024.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OnlyOneWonOptionException
   *           the only one won option exception
   * @throws NotAllOptionsExistsException
   *           the not all options exists exception
   */

  @Test(
      expected = InstanceNotFoundException.class)
  public final void testCheckNonExistentBetTypeOption()
      throws InstanceNotFoundException, OnlyOneWonOptionException,
      NotAllOptionsExistsException {

    final Long nonExistentBetTypeId = 0L;

    final Set<Long> winners = new HashSet<Long>();

    betService.checkOptions(nonExistentBetTypeId, winners);
  }

}
