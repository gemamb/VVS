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
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.category.CategoryDao;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetServiceIntegrationTest {

	@Autowired
	private CategoryDao categoryDao;

	@Autowired
	private EventDao eventDao;

	@Autowired
	private BetTypeDao betTypeDao;

	@Autowired
	private BetOptionDao betOptionDao;

	@Autowired
	private UserProfileDao userProfileDao;

	@Autowired
	private BetDao betDao;

	@Autowired
	private BetService betService;

	Calendar eventCalendar;
	Category category1, category2;
	Event event1, event2, event3;
	BetType betType, duplicatedBetType;
	BetOption betOption1, betOption2;
	UserProfile user;

	private void initializeDate() {
		eventCalendar = Calendar.getInstance();
		eventCalendar.set(2017, Calendar.AUGUST, 31);
	}

	private void initializeCategory() {
		category1 = new Category("Baloncesto");
	}

	private void initializeCategories() {
		category1 = new Category("Baloncesto");
		categoryDao.save(category1);
		category2 = new Category("Futbol");
		categoryDao.save(category2);
	}

	private void initializeEvent() {
		event1 = new Event("Real Madrid - Barcelona", eventCalendar, category1);
	}

	private void initializeBetType() {
		betType = new BetType("¿Qué equipo ganará el encuentro?", false);
	}

	private void initializeDuplicatedBetType() {
		duplicatedBetType = new BetType("¿Qué equipo ganará el encuentro?",
				false);

		BetOption option1, option2;
		option1 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);
		option2 = new BetOption("Barcelona", (float) 1.75, null, betType);

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(option1);
		betOptions.add(option2);

		duplicatedBetType.setBetOptions(betOptions);
		event1.addBetType(duplicatedBetType);
	}

	private void initializeBetOptions() {
		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null,
				betType);
		betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);
	}

	private void initializeBetTypeWithBetOptions() {
		betType = new BetType("¿Qué equipo ganará el encuentro?", false);
		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null,
				betType);
		betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);
		List<BetOption> options = new ArrayList<BetOption>();
		options.add(betOption1);
		options.add(betOption2);
		betType.setBetOptions(options);
	}

	private void initializeBetTypeDuplicatedBetOptions() {
		betType = new BetType("¿Qué equipo ganará el encuentro?", false);
		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null,
				betType);
		betOption2 = new BetOption("Real Madrid CF", (float) 1.75, null,
				betType);
		List<BetOption> options = new ArrayList<BetOption>();
		options.add(betOption1);
		options.add(betOption2);
		betType.setBetOptions(options);
	}

	private void initializeBetTypeWithOneOption() {
		betType = new BetType("¿Qué equipo ganará el encuentro?", false);
		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null,
				betType);
		List<BetOption> options = new ArrayList<BetOption>();
		options.add(betOption1);
		betType.setBetOptions(options);
	}

	private void initializeUser() {
		user = new UserProfile("pepe6", "XxXyYyZzZ", "Pepe", "García",
				"pepe6@gmail.com");
		userProfileDao.save(user);
	}

	private void initializeCreatedEvent() {
		initializeDate();
		initializeCategory();
		categoryDao.save(category1);

		initializeEvent();
		eventDao.save(event1);

	}

	private void initializeCreatedEvents() {

		initializeDate();

		event1 = new Event("Madrid - Barcelona", eventCalendar, category1);
		eventDao.save(event1);

		event2 = new Event("Deportivo - Celta", eventCalendar, category2);
		eventDao.save(event2);

		event3 = new Event("Madrid - Barcelona", eventCalendar, category2);
		eventDao.save(event3);
	}

	private void initializeCreatedBetType() {

		initializeBetType();
		initializeBetOptions();

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);

		betType.setBetOptions(betOptions);
		event1.addBetType(betType);
		betTypeDao.save(betType);

		betOptionDao.save(betOption1);
		betOptionDao.save(betOption2);

		event1.addBetType(betType);
	}

	private void initializeCreatedMultipleBetType() {

		betType = new BetType("¿Qué equipo ganará el encuentro?", true);
		initializeBetOptions();

		List<BetOption> betOptions = new ArrayList<>();
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
	 * insertEvent findEvent insertBetType (no multi) insertBetOption
	 *
	 */

	@Test
	public void testCreationScenary() throws InstanceNotFoundException,
	AlreadyPastedDateException, DuplicateEventNameException,
	DuplicateBetTypeQuestionException,
	DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategory();
		categoryDao.save(category1);

		/* Crear un evento */

		initializeEvent();
		event1 = betService.insertEvent(event1, category1.getCategoryId());
		Event eventAssert = eventDao.find(event1.getEventId());

		assertEquals(event1, eventAssert);

		/* Buscar a un evento */

		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeBetType();
		initializeBetOptions();

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);

		betType.setBetOptions(betOptions);
		event1.addBetType(betType);

		betType = betService.insertBetType(betType);

		BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
		BetOption betOption1Assert = betOptionDao.find(betOption1
				.getBetOptionId());
		BetOption betOption2Assert = betOptionDao.find(betOption2
				.getBetOptionId());

		assertEquals(betType, betTypeAssert);
		assertEquals(betOption1, betOption1Assert);
		assertEquals(betOption2, betOption2Assert);
	}

	/**
	 * PR-IN-002
	 *
	 * insertEvent findEvent insertBetType (no multi) insertBetOption
	 * checkWinners
	 *
	 */

	@Test
	public void testCheckingWinnersScenary() throws InstanceNotFoundException,
	AlreadyPastedDateException, DuplicateEventNameException,
	DuplicateBetTypeQuestionException,
	DuplicateBetOptionAnswerException, MinimunBetOptionException,
	OnlyOneWonOptionException, NotAllOptionsExistsException {

		initializeDate();
		initializeCategory();
		categoryDao.save(category1);

		/* Crear un evento */

		initializeEvent();
		event1 = betService.insertEvent(event1, category1.getCategoryId());
		Event eventAssert = eventDao.find(event1.getEventId());

		assertEquals(event1, eventAssert);

		/* Buscar a un evento */

		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeBetType();
		initializeBetOptions();

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);

		betType.setBetOptions(betOptions);
		event1.addBetType(betType);

		betType = betService.insertBetType(betType);

		BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());

		assertEquals(betType, betTypeAssert);

		BetOption betOption1Assert = betOptionDao.find(betOption1
				.getBetOptionId());
		BetOption betOption2Assert = betOptionDao.find(betOption2
				.getBetOptionId());
		assertEquals(betOption1, betOption1Assert);
		assertEquals(betOption2, betOption2Assert);

		/* Marcar como ganadora una opción de apuesta */

		Set<Long> winners = new HashSet<Long>();
		winners.add(betOption1.getBetOptionId());

		betService.checkOptions(betType.getBetTypeId(), winners);

		assertTrue(betOption1.getBetState());
		assertFalse(betOption2.getBetState());

	}

	/**
	 * PR-IN-003
	 *
	 * findEvents makeBet findBets
	 *
	 */

	@Test
	public void testMakeBetScenary() throws InstanceNotFoundException,
	OutdatedBetException {

		/* SETUP */

		initializeUser();
		initializeCategories();
		initializeCreatedEvents();
		initializeCreatedBetType();

		/* Búsqueda de eventos */

		EventBlock foundEvents = betService.findEvents("madrid",
				category1.getCategoryId(), 0, 2, false);
		Event foundEvent = foundEvents.getEvents().get(0);

		assertEquals(event1, foundEvent);

		/* Realización de la apuesta */

		BetOption option = foundEvent.getBetTypes().get(0).getBetOptions()
				.get(0);
		float quantity = 10;
		Bet bet = betService.makeBet(user.getUserProfileId(),
				option.getBetOptionId(), quantity);

		/* Búsqueda de apuestas */

		BetBlock foundBets = betService.findBets(user.getUserProfileId(), 0, 1);
		Bet foundBet = foundBets.getBets().get(0);
		assertEquals(bet, foundBet);
	}

	/**
	 * PR-IN-004
	 *
	 * findEvents makeBet findBets
	 *
	 *
	 */

	@Test
	public void testCheckBetStatusScenary() throws InstanceNotFoundException,
	OutdatedBetException, OnlyOneWonOptionException,
	NotAllOptionsExistsException {

		initializeUser();
		initializeCategories();
		initializeCreatedEvents();
		initializeCreatedBetType();

		/* Realizar apuesta */

		float quantity = 10;
		BetOption option = betType.getBetOptions().get(0);
		betService.makeBet(user.getUserProfileId(), option.getBetOptionId(),
				quantity);

		/* Cambiamos fecha del evento */

		Calendar pastDate = Calendar.getInstance();
		pastDate.set(2016, Calendar.AUGUST, 31);
		event1.setEventStart(pastDate);

		/* Establecemos opciones ganadoras en el evento */

		Set<Long> winned = new HashSet<Long>();
		winned.add(option.getBetOptionId());
		betService.checkOptions(betType.getBetTypeId(), winned);

		/* Búsqueda de apuestas */

		BetBlock foundBets = betService.findBets(user.getUserProfileId(), 0, 1);
		Bet foundBet = foundBets.getBets().get(0);

		/* Comprobamos que la apuesta está ganada */

		assertTrue(foundBet.getBetOption().getBetState());

	}

	/**
	 * PR-IN-005
	 */

	@Test
	public void testMakeBet() throws InstanceNotFoundException,
	OutdatedBetException, DuplicateBetTypeQuestionException,
	DuplicateBetOptionAnswerException, MinimunBetOptionException,
	AlreadyPastedDateException, DuplicateEventNameException {

		/* Buscar evento ya existente */

		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeCreatedBetType();
		BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
		BetOption betOption1Assert = betOptionDao.find(betOption1
				.getBetOptionId());
		BetOption betOption2Assert = betOptionDao.find(betOption2
				.getBetOptionId());

		assertEquals(betType, betTypeAssert);
		assertEquals(betOption1, betOption1Assert);
		assertEquals(betOption2, betOption2Assert);

		/* Apostar sobre una opcion de apuesta */

		initializeUser();

		Bet bet = betService.makeBet(user.getUserProfileId(),
				betOption1.getBetOptionId(), (float) 2);
		Bet betFound = betDao.find(bet.getBetId());
		assertEquals(bet, betFound);

		List<Bet> betFounds = betDao.findBetsByUserId(user.getUserProfileId(),
				0, 10);
		assertTrue(betFounds.contains(bet));
	}

	/**
	 * PR-IN-006
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testMakeBetWrongUser() throws InstanceNotFoundException,
	OutdatedBetException, DuplicateBetTypeQuestionException,
	DuplicateBetOptionAnswerException, MinimunBetOptionException,
	AlreadyPastedDateException, DuplicateEventNameException {

		/* Buscar evento ya existente */

		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeCreatedBetType();
		BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
		BetOption betOption1Assert = betOptionDao.find(betOption1
				.getBetOptionId());
		BetOption betOption2Assert = betOptionDao.find(betOption2
				.getBetOptionId());

		assertEquals(betType, betTypeAssert);
		assertEquals(betOption1, betOption1Assert);
		assertEquals(betOption2, betOption2Assert);

		/* Apostar con usuario no existente */

		betService.makeBet(-1L, betOption1.getBetOptionId(), (float) 2);

	}

	/**
	 * PR-IN-007
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testMakeBetWrongBetOption() throws InstanceNotFoundException,
	OutdatedBetException {

		/* Buscar evento ya existente */

		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeCreatedBetType();
		BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
		BetOption betOption1Assert = betOptionDao.find(betOption1
				.getBetOptionId());
		BetOption betOption2Assert = betOptionDao.find(betOption2
				.getBetOptionId());

		assertEquals(betType, betTypeAssert);
		assertEquals(betOption1, betOption1Assert);
		assertEquals(betOption2, betOption2Assert);

		/* Apostar con opcion de apuesta no existente */

		initializeUser();
		betService.makeBet(user.getUserProfileId(), -1L, (float) 2);

	}

	/**
	 * PR-IN-008
	 */

	@Test(expected = OutdatedBetException.class)
	public void testOutDatedTrueMakeBet() throws InstanceNotFoundException,
	OutdatedBetException {

		/* Buscar evento ya existente */

		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeBetType();

		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, true,
				betType);
		betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);

		betType.setBetOptions(betOptions);
		event1.addBetType(betType);
		betTypeDao.save(betType);

		betOptionDao.save(betOption1);
		betOptionDao.save(betOption2);

		/* Apostar sobre opción de apuesta ya ganadora */

		initializeUser();

		betService.makeBet(user.getUserProfileId(),
				betOption1.getBetOptionId(), (float) 2);

	}

	/**
	 * PR-IN-009
	 */

	@Test(expected = OutdatedBetException.class)
	public void testOutDatedFalseMakeBet() throws InstanceNotFoundException,
	OutdatedBetException {

		/* Buscar evento ya existente */

		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeBetType();

		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, false,
				betType);
		betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);

		betType.setBetOptions(betOptions);
		event1.addBetType(betType);
		betTypeDao.save(betType);

		betOptionDao.save(betOption1);
		betOptionDao.save(betOption2);

		/* Apostar sobre opción de apuesta ya ganadora */

		initializeUser();

		betService.makeBet(user.getUserProfileId(),
				betOption1.getBetOptionId(), (float) 2);

	}

	/**
	 * PR-IN-010
	 */

	@Test
	public void testMakeBetWrongMoney() throws InstanceNotFoundException,
	OutdatedBetException {

		/* Buscar evento ya existente */

		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeCreatedBetType();
		BetType betTypeAssert = betTypeDao.find(betType.getBetTypeId());
		BetOption betOption1Assert = betOptionDao.find(betOption1
				.getBetOptionId());
		BetOption betOption2Assert = betOptionDao.find(betOption2
				.getBetOptionId());

		assertEquals(betType, betTypeAssert);
		assertEquals(betOption1, betOption1Assert);
		assertEquals(betOption2, betOption2Assert);

		/* Apostar sobre una opcion de apuesta */

		initializeUser();

		betService.makeBet(user.getUserProfileId(),
				betOption1.getBetOptionId(), (float) -2);
	}

	/**
	 * PR-IN-011
	 */

	@Test
	public void testInsertEvent() throws InstanceNotFoundException,
	AlreadyPastedDateException, DuplicateEventNameException {

		initializeDate();
		initializeCategory();
		categoryDao.save(category1);

		/* Crear un evento */

		initializeEvent();
		event1 = betService.insertEvent(event1, category1.getCategoryId());
		Event eventAssert = eventDao.find(event1.getEventId());

		assertEquals(event1, eventAssert);
	}

	/**
	 * PR-IN-012
	 */

	@Test(expected = AlreadyPastedDateException.class)
	public void testInsertPastedEvent() throws InstanceNotFoundException,
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
	 * PR-UN-013
	 */

	@Test(expected = DuplicateEventNameException.class)
	public void testInsertDuplicateEvent() throws InstanceNotFoundException,
	AlreadyPastedDateException, DuplicateEventNameException {

		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event1.getEventId());

		assertEquals(event1, eventFound);

		Event duplicatedEvent = event1 = new Event("Real Madrid - Barcelona",
				eventCalendar, category1);

		betService.insertEvent(duplicatedEvent, category1.getCategoryId());
	}

	/**
	 * PR-UN-014
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testInsertEventWrongCategory()
			throws InstanceNotFoundException, AlreadyPastedDateException,
			DuplicateEventNameException {

		initializeDate();
		category1 = new Category("Baloncesto");

		event1 = new Event("Real Madrid - Barcelona", eventCalendar, category1);

		betService.insertEvent(event1, 2L);
	}

	/**
	 * PR-UN-015
	 */

	@Test
	public void testInsertBetType() throws AlreadyPastedDateException,
	InstanceNotFoundException, DuplicateEventNameException,
	DuplicateBetTypeQuestionException,
	DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeCreatedEvent();
		initializeBetTypeWithBetOptions();
		event1.addBetType(betType);

		/* Insertamos tipo de apuesta */

		BetType createdBetType = betService.insertBetType(betType);

		assertEquals(betType, createdBetType);

	}

	/**
	 * PR-UN-016
	 */

	@Test(expected = MinimunBetOptionException.class)
	public void testInsertBetTypeWithoutOptions()
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
	 * PR-UN-017
	 */

	@Test(expected = MinimunBetOptionException.class)
	public void testInsertBetTypeWithOneOption()
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
	 * PR-UN-018
	 */

	@Test(expected = DuplicateBetOptionAnswerException.class)
	public void testInsertBetTypeDuplicateAnswer()
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
	 * PR-UN-019
	 */

	@Test(expected = DuplicateBetTypeQuestionException.class)
	public void testInsertBetTypeDuplicateQuestion()
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
	 * PR-UN-020
	 */

	@Test
	public void testCheckOptionsSimpleBetType()
			throws InstanceNotFoundException, OnlyOneWonOptionException,
			NotAllOptionsExistsException {

		initializeCreatedEvent();
		initializeCreatedBetType();

		Set<Long> winners = new HashSet<Long>();
		winners.add(betOption1.getBetOptionId());

		betService.checkOptions(betType.getBetTypeId(), winners);

	}

	/**
	 * PR-UN-021
	 */

	@Test
	public void testCheckOptionsMultipleBetType()
			throws InstanceNotFoundException, OnlyOneWonOptionException,
			NotAllOptionsExistsException {

		initializeCreatedEvent();
		initializeCreatedMultipleBetType();

		Set<Long> winners = new HashSet<Long>();
		winners.add(betOption1.getBetOptionId());
		winners.add(betOption2.getBetOptionId());

		betService.checkOptions(betType.getBetTypeId(), winners);

	}

	/**
	 * PR-UN-022
	 */

	@Test(expected = NotAllOptionsExistsException.class)
	public void testCheckInvalidOptions() throws NotAllOptionsExistsException,
	InstanceNotFoundException, OnlyOneWonOptionException {

		initializeCreatedEvent();
		initializeCreatedBetType();
		Long nonExistentBetOptionId = 0L;

		Set<Long> winners = new HashSet<Long>();
		winners.add(nonExistentBetOptionId);

		betService.checkOptions(betType.getBetTypeId(), winners);
	}

	/**
	 * PR-UN-023
	 */

	@Test(expected = OnlyOneWonOptionException.class)
	public void testCheckMultipleOptionsSimpleBetType()
			throws OnlyOneWonOptionException, InstanceNotFoundException,
			NotAllOptionsExistsException {

		initializeCreatedEvent();
		initializeCreatedBetType();

		Set<Long> winners = new HashSet<Long>();
		winners.add(betOption1.getBetOptionId());
		winners.add(betOption2.getBetOptionId());

		betService.checkOptions(betType.getBetTypeId(), winners);
	}

	/**
	 * PR-UN-024
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testCheckNonExistentBetTypeOption()
			throws InstanceNotFoundException, OnlyOneWonOptionException,
			NotAllOptionsExistsException {

		Long nonExistentBetTypeId = 0L;

		Set<Long> winners = new HashSet<Long>();

		betService.checkOptions(nonExistentBetTypeId, winners);
	}

}
