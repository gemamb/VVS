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
	Category category;
	Event event;
	BetType betType;
	BetOption betOption1, betOption2;
	UserProfile userProfile;

	public void initializeDate() {
		eventCalendar = Calendar.getInstance();
		eventCalendar.set(2017, Calendar.AUGUST, 31);
	}

	public void initializeCategory() {
		category = new Category("Baloncesto");
	}

	public void initializeEvent() {
		event = new Event("Real Madrid - Barcelona", eventCalendar, category);
	}

	public void initializeBetType() {
		betType = new BetType("¿Qué equipo ganará el encuentro?", false);
	}

	public void initializeBetOptions() {
		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null,
				betType);
		betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);
	}

	public void initializeUser() {
		userProfile = new UserProfile("pepe6", "XxXyYyZzZ", "Pepe", "García", "pepe6@gmail.com");
	}
	
	public void initializeCreatedEvent() {
		initializeDate();
		initializeCategory();
		categoryDao.save(category);

		initializeEvent();
		eventDao.save(event);

	}
	
	public void initializeCreatedBetType() {

		initializeBetType();
		initializeBetOptions();

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);	
		
		betType.setBetOptions(betOptions);
		event.addBetType(betType);
		betTypeDao.save(betType);
		
		betOptionDao.save(betOption1);
		betOptionDao.save(betOption2);
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
		categoryDao.save(category);

		/* Crear un evento */

		initializeEvent();
		event = betService.insertEvent(event, category.getCategoryId());
		Event eventAssert = eventDao.find(event.getEventId());

		assertEquals(event, eventAssert);

		/* Buscar a un evento */

		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeBetType();
		initializeBetOptions();

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);

		betType.setBetOptions(betOptions);
		event.addBetType(betType);

		betType = betService.insertBetType(betType);

	}

	/**
	 * PR-IN-002
	 * 
	 * insertEvent 
	 * findEvent 
	 * insertBetType (no multi) 
	 * insertBetOption
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
		categoryDao.save(category);

		/* Crear un evento */

		initializeEvent();
		event = betService.insertEvent(event, category.getCategoryId());
		Event eventAssert = eventDao.find(event.getEventId());

		assertEquals(event, eventAssert);

		/* Buscar a un evento */

		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeBetType();
		initializeBetOptions();

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);

		betType.setBetOptions(betOptions);
		event.addBetType(betType);

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
	 * PR-IN-005 
	 */

	@Test
	public void testMakeBet() throws InstanceNotFoundException, OutdatedBetException, DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException, AlreadyPastedDateException, DuplicateEventNameException {

		/* Buscar evento ya existente */
		
		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);

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
		userProfileDao.save(userProfile);

		Bet bet = betService.makeBet(userProfile.getUserProfileId(), betOption1.getBetOptionId(), (float) 2);
		Bet betFound = betDao.find(bet.getBetId());
		assertEquals(bet, betFound);
		
		List<Bet> betFounds = betDao.findBetsByUserId(userProfile.getUserProfileId(), 0, 10);
		assertTrue(betFounds.contains(bet));
	}

	/**
	 * PR-IN-006
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testMakeBetWrongUser() throws InstanceNotFoundException, OutdatedBetException, DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException, AlreadyPastedDateException, DuplicateEventNameException {

		/* Buscar evento ya existente */
		
		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);

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
	 * PR-IN-006
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testMakeBetWrongBetOption() throws InstanceNotFoundException, OutdatedBetException {

		/* Buscar evento ya existente */
		
		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);

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
		userProfileDao.save(userProfile);
		betService.makeBet(userProfile.getUserProfileId(), -1L, (float) 2);

	}

	/**
	 * PR-IN-007
	 */

	@Test(expected = OutdatedBetException.class)
	public void testOutDatedTrueMakeBet() throws InstanceNotFoundException, OutdatedBetException {

		/* Buscar evento ya existente */
		
		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeBetType();

		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, true,
				betType);
		betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);
		
		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);	
		
		betType.setBetOptions(betOptions);
		event.addBetType(betType);
		betTypeDao.save(betType);
		
		betOptionDao.save(betOption1);
		betOptionDao.save(betOption2);

		/* Apostar sobre opción de apuesta ya ganadora */
		
		initializeUser();
		userProfileDao.save(userProfile);

		betService.makeBet(userProfile.getUserProfileId(), betOption1.getBetOptionId(), (float) 2);

	}

	/**
	 * PR-IN-008
	 */

	@Test(expected = OutdatedBetException.class)
	public void testOutDatedFalseMakeBet() throws InstanceNotFoundException, OutdatedBetException {

		/* Buscar evento ya existente */
		
		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);

		/* Crear un tipo de apuesta con dos opciones de apuesta */

		initializeBetType();

		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, false,
				betType);
		betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);
		
		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOption1);
		betOptions.add(betOption2);	
		
		betType.setBetOptions(betOptions);
		event.addBetType(betType);
		betTypeDao.save(betType);
		
		betOptionDao.save(betOption1);
		betOptionDao.save(betOption2);

		/* Apostar sobre opción de apuesta ya ganadora */
		
		initializeUser();
		userProfileDao.save(userProfile);

		betService.makeBet(userProfile.getUserProfileId(), betOption1.getBetOptionId(), (float) 2);

	}

	/**
	 * PR-IN-009
	 */

	@Test
	public void testMakeBetWrongMoney() throws InstanceNotFoundException, OutdatedBetException {

		/* Buscar evento ya existente */
		
		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);

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
		userProfileDao.save(userProfile);

		betService.makeBet(userProfile.getUserProfileId(), betOption1.getBetOptionId(), (float) -2);
	}

	/**
	 * PR-IN-010
	 */

	@Test
	public void testInsertEvent()
			throws InstanceNotFoundException, AlreadyPastedDateException, DuplicateEventNameException {

		initializeDate();
		initializeCategory();
		categoryDao.save(category);

		/* Crear un evento */

		initializeEvent();
		event = betService.insertEvent(event, category.getCategoryId());
		Event eventAssert = eventDao.find(event.getEventId());

		assertEquals(event, eventAssert);
	}

	/**
	 * PR-IN-011
	 */

	@Test(expected = AlreadyPastedDateException.class)
	public void testInsertPastedEvent()
			throws InstanceNotFoundException, AlreadyPastedDateException, DuplicateEventNameException {

		eventCalendar = Calendar.getInstance();
		eventCalendar.set(2014, Calendar.AUGUST, 31);

		initializeCategory();
		categoryDao.save(category);

		/* Crear un evento */

		event = new Event("Real Madrid - Barcelona", eventCalendar, category);
		betService.insertEvent(event, category.getCategoryId());
	}

	/**
	 * PR-UN-012
	 */

	@Test(expected = DuplicateEventNameException.class)
	public void testInsertDuplicateEvent()
			throws InstanceNotFoundException, AlreadyPastedDateException, DuplicateEventNameException {

		initializeCreatedEvent();
		Event eventFound = betService.findEvent(event.getEventId());

		assertEquals(event, eventFound);
		
		Event duplicatedEvent = event = new Event("Real Madrid - Barcelona", eventCalendar, category);

		betService.insertEvent(duplicatedEvent, category.getCategoryId());
	}

	/**
	 * PR-UN-013
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testInsertEventWrongCategory()
			throws InstanceNotFoundException, AlreadyPastedDateException, DuplicateEventNameException {

		initializeDate();
		category = new Category("Baloncesto");
		
		event = new Event("Real Madrid - Barcelona", eventCalendar, category);
		
		betService.insertEvent(event, 2L);
	}
	
}
