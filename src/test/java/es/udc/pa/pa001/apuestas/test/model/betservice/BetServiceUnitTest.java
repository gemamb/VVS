package es.udc.pa.pa001.apuestas.test.model.betservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetBlock;
import es.udc.pa.pa001.apuestas.model.bet.BetDao;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betOption.BetOptionDao;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betType.BetTypeDao;
import es.udc.pa.pa001.apuestas.model.betservice.BetServiceImpl;
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

@RunWith(MockitoJUnitRunner.class)
public class BetServiceUnitTest {

	@InjectMocks
	private BetServiceImpl betService = new BetServiceImpl();

	@Mock
	private EventDao eventDaoMock;

	@Mock
	private UserProfileDao userProfileDaoMock;

	@Mock
	private CategoryDao categoryDaoMock;

	@Mock
	private BetOptionDao betOptionDaoMock;

	@Mock
	private BetTypeDao betTypeDaoMock;

	@Mock
	private BetDao betDaoMock;

	Calendar eventCalendar;
	UserProfile userProfileDemo;
	Category categoryDemo, categoryDemo1;
	Event eventDemo;
	Event event1, event2, event3;
	BetType betTypeDemo;
	BetOption betOptionDemo;
	BetOption betOptionDemo1, betOptionDemo2;
	Bet betDemo1, betDemo2, betDemo3;

	private void initializeUser() {
		userProfileDemo = new UserProfile("pepe6", "XxXyYyZzZ", "Pepe", "García", "pepe6@gmail.com");
	}

	private void initializeDate() {
		eventCalendar = Calendar.getInstance();
		eventCalendar.set(2017, Calendar.AUGUST, 31);
	}

	private void initializeCategories() {
		categoryDemo = new Category("Baloncesto");
		categoryDemo1 = new Category("Tenis");
	}

	private void initializeEvent() {
		eventDemo = new Event("Real Madrid - Barcelona", eventCalendar, categoryDemo);
	}

	private void initializeBetType() {
		betTypeDemo = new BetType("¿Qué equipo ganará el encuentro?", false);
	}

	private void initializeSimpleBetType() {
		betTypeDemo = new BetType("¿Qué equipo ganará el encuentro?", false);
		betTypeDemo.setBetTypeId(1L);
	}

	private void initializeMultipleBetType() {
		betTypeDemo = new BetType("¿Qué equipo ganará el encuentro?", true);
		betTypeDemo.setBetTypeId(1L);
	}

	private void initializeBetOptions() {
		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75, null, betTypeDemo);

		betOptionDemo1 = new BetOption("Real Madrid CF", (float) 1.75, null, betTypeDemo);

		betOptionDemo2 = new BetOption("Barcelona", (float) 1.5, null, betTypeDemo);
	}

	private void initializeCheckBetOptions() {
		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75, null, betTypeDemo);
		betOptionDemo.setBetOptionId(1L);

		betOptionDemo1 = new BetOption("Real Madrid CF", (float) 1.75, null, betTypeDemo);
		betOptionDemo1.setBetOptionId(2L);

		betOptionDemo2 = new BetOption("Barcelona", (float) 1.5, null, betTypeDemo);
		betOptionDemo2.setBetOptionId(3L);

		betTypeDemo.addBetOption(betOptionDemo);
		betTypeDemo.addBetOption(betOptionDemo1);
		betTypeDemo.addBetOption(betOptionDemo2);
	}

	private void initializeEvents() {
		event1 = new Event("Real Madrid - Barcelona", eventCalendar, categoryDemo);
		event2 = new Event("Obradoiro - Real Madrid", eventCalendar, categoryDemo);
		event3 = new Event("Real Madrid - Celta", eventCalendar, categoryDemo);
	}

	private void initializeBets() {
		betDemo1 = new Bet(1F, userProfileDemo, event1, betOptionDemo);
		betDemo2 = new Bet(1F, userProfileDemo, event1, betOptionDemo1);
		betDemo3 = new Bet(1F, userProfileDemo, event1, betOptionDemo2);
	}

	/**
	 * PR-UN-039
	 */

	@Test
	public void testFindNoEventsGetNumber() {

		when(eventDaoMock.getNumberOfEvents(null, null, true)).thenReturn(0);

		int events = betService.findEventsGetNumber(null, null, true);

		assertEquals(events, 0);
	}

	/**
	 * PR-UN-040
	 */

	@Test
	public void testFindEventsGetNumber() {

		when(eventDaoMock.getNumberOfEvents("deportivo", null, true)).thenReturn(3);

		int events = betService.findEventsGetNumber("deportivo", null, true);

		assertEquals(events, 3);
	}

	/**
	 * PR-UN-041
	 */

	@Test
	public void testFindEmptyFalseEvents() {

		initializeDate();
		initializeCategories();
		initializeEvents();

		List<Event> listEvents = new ArrayList<>();

		when(eventDaoMock.findEvents(null, null, 0, 2, true)).thenReturn(listEvents);

		EventBlock eventBlock = betService.findEvents(null, null, 0, 1, true);

		assertEquals(eventBlock.getEvents(), listEvents);
		assertEquals(eventBlock.getExistMoreEvents(), false);
	}

	/**
	 * PR-UN-042
	 */

	@Test
	public void testFindNotEmptyFalseEvents() {

		initializeDate();
		initializeCategories();
		initializeEvent();

		List<Event> listEvents = new ArrayList<>();
		listEvents.add(eventDemo);

		when(eventDaoMock.findEvents(null, null, 0, 2, true)).thenReturn(listEvents);

		EventBlock eventBlock = betService.findEvents(null, null, 0, 1, true);

		assertEquals(eventBlock.getEvents(), listEvents);
		assertEquals(eventBlock.getExistMoreEvents(), false);
	}

	/**
	 * PR-UN-043
	 */

	@Test
	public void testFindNotEmptyTrueEvents() {

		initializeDate();
		initializeCategories();
		initializeEvents();

		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);

		when(eventDaoMock.findEvents(null, null, 0, 2, true)).thenReturn(listEvents);

		EventBlock eventBlock = betService.findEvents(null, null, 0, 1, true);

		assertEquals(eventBlock.getEvents(), listEvents);
		assertEquals(eventBlock.getExistMoreEvents(), true);
	}

	/**
	 * PR-UN-044
	 */

	@Test
	public void testFindFalseEvents() {

		initializeDate();
		initializeCategories();
		initializeEvents();

		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);

		when(eventDaoMock.findEvents(null, null, 1, 3, true)).thenReturn(listEvents);

		EventBlock eventBlock = betService.findEvents(null, null, 1, 2, true);

		assertEquals(eventBlock.getEvents(), listEvents);
		assertEquals(eventBlock.getExistMoreEvents(), false);
	}

	/**
	 * PR-UN-045
	 */

	@Test
	public void testFindTrueEvents() {

		initializeDate();
		initializeCategories();
		initializeEvents();

		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);
		listEvents.add(event3);

		when(eventDaoMock.findEvents(null, null, 1, 3, true)).thenReturn(listEvents);

		EventBlock eventBlock = betService.findEvents(null, null, 1, 2, true);

		assertEquals(eventBlock.getEvents(), listEvents);
		assertEquals(eventBlock.getExistMoreEvents(), true);
	}

	/**
	 * PR-UN-046
	 */

	@Test
	public void testFindEmptyFalseBets() {

		initializeUser();
		List<Bet> bets = new ArrayList<Bet>();

		when(betDaoMock.findBetsByUserId(userProfileDemo.getUserProfileId(), 0, 2)).thenReturn(bets);

		BetBlock betBlock = betService.findBets(userProfileDemo.getUserProfileId(), 0, 1);

		assertTrue(betBlock.getBets().isEmpty());
		assertFalse(betBlock.getExistMoreBets());
	}

	/**
	 * PR-UN-047
	 */

	@Test
	public void testFindNotEmptyFalseBets() {

		initializeUser();
		initializeBets();

		List<Bet> bets = new ArrayList<Bet>();
		bets.add(betDemo1);

		when(betDaoMock.findBetsByUserId(userProfileDemo.getUserProfileId(), 0, 2)).thenReturn(bets);

		BetBlock betBlock = betService.findBets(userProfileDemo.getUserProfileId(), 0, 1);

		assertEquals(betBlock.getBets(), bets);
		assertFalse(betBlock.getExistMoreBets());
	}

	/**
	 * PR-UN-048
	 */

	@Test
	public void testFindNotEmptyTrueBets() {

		initializeUser();
		initializeBets();

		List<Bet> bets = new ArrayList<Bet>();
		bets.add(betDemo1);
		bets.add(betDemo2);

		when(betDaoMock.findBetsByUserId(userProfileDemo.getUserProfileId(), 0, 2)).thenReturn(bets);

		BetBlock betBlock = betService.findBets(userProfileDemo.getUserProfileId(), 0, 1);

		assertEquals(betBlock.getBets(), bets);
		assertTrue(betBlock.getExistMoreBets());
	}

	/**
	 * PR-UN-049
	 */

	@Test
	public void testFindIndexFalseBets() {

		initializeUser();
		initializeBets();

		List<Bet> bets = new ArrayList<Bet>();
		bets.add(betDemo1);
		bets.add(betDemo2);

		when(betDaoMock.findBetsByUserId(userProfileDemo.getUserProfileId(), 1, 3)).thenReturn(bets);

		BetBlock betBlock = betService.findBets(userProfileDemo.getUserProfileId(), 1, 2);

		assertEquals(betBlock.getBets(), bets);
		assertFalse(betBlock.getExistMoreBets());
	}

	/**
	 * PR-UN-050
	 */

	@Test
	public void testFindIndexTrueEvents() {

		initializeUser();
		initializeBets();

		List<Bet> bets = new ArrayList<Bet>();
		bets.add(betDemo1);
		bets.add(betDemo2);
		bets.add(betDemo3);

		when(betDaoMock.findBetsByUserId(userProfileDemo.getUserProfileId(), 1, 3)).thenReturn(bets);

		BetBlock betBlock = betService.findBets(userProfileDemo.getUserProfileId(), 1, 2);

		assertEquals(betBlock.getBets(), bets);
		assertTrue(betBlock.getExistMoreBets());
	}

	/**
	 * PR-UN-051
	 */

	@Test
	public void testMakeBet() throws InstanceNotFoundException, OutdatedBetException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeUser();
		initializeBetType();
		initializeBetOptions();
		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(2L)).thenReturn(userProfileDemo);
		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		Bet betDemo = betService.makeBet(2L, 1L, (float) 2);

		when(betDaoMock.find(1L)).thenReturn(betDemo);

		assertEquals(betDemo, betDaoMock.find(1L));
	}

	/**
	 * PR-UN-052
	 */

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testMakeBetWrongUser() throws InstanceNotFoundException, OutdatedBetException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();
		initializeBetOptions();
		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(-1L)).thenThrow(InstanceNotFoundException.class);
		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		betService.makeBet(-1L, 1L, (float) 2);
	}

	/**
	 * PR-UN-053
	 */

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testMakeBetWrongBetOption() throws InstanceNotFoundException, OutdatedBetException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeUser();
		initializeBetType();
		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(2L)).thenReturn(userProfileDemo);
		when(betOptionDaoMock.find(1L)).thenThrow(InstanceNotFoundException.class);

		betService.makeBet(2L, 1L, (float) 2);
	}

	/**
	 * PR-UN-054
	 */

	@Test(expected = OutdatedBetException.class)
	public void testOutDatedTrueMakeBet() throws InstanceNotFoundException, OutdatedBetException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeUser();
		initializeBetType();

		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75, true, betTypeDemo);

		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(2L)).thenReturn(userProfileDemo);
		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		Bet betDemo = betService.makeBet(2L, 1L, (float) 2);

		when(betDaoMock.find(1L)).thenReturn(betDemo);

		assertEquals(betDemo, betDaoMock.find(1L));
	}

	/**
	 * PR-UN-055
	 */

	@Test(expected = OutdatedBetException.class)
	public void testOutDatedFalseMakeBet() throws InstanceNotFoundException, OutdatedBetException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();

		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75, false, betTypeDemo);

		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(2L)).thenReturn(userProfileDemo);
		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		Bet betDemo = betService.makeBet(2L, 1L, (float) 2);

		when(betDaoMock.find(1L)).thenReturn(betDemo);

		assertEquals(betDemo, betDaoMock.find(1L));
	}

	/**
	 * PR-UN-056
	 */

	@Test
	public void testMakeBetWrongMoney() throws InstanceNotFoundException, OutdatedBetException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeUser();
		initializeBetType();
		initializeBetOptions();
		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(2L)).thenReturn(userProfileDemo);
		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		Bet betDemo = betService.makeBet(2L, 1L, (float) -2);

		when(betDaoMock.find(1L)).thenReturn(betDemo);

		assertEquals(betDemo, betDaoMock.find(1L));
	}

	/**
	 * PR-UN-057
	 */

	@Test
	public void testInsertEvent()
			throws InstanceNotFoundException, AlreadyPastedDateException, DuplicateEventNameException {

		initializeDate();
		initializeCategories();
		initializeEvent();

		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		when(eventDaoMock.find(1L)).thenReturn(eventDemo);

		assertEquals(eventDemo, eventDaoMock.find(1L));
	}

	/**
	 * PR-UN-058
	 */

	@Test(expected = AlreadyPastedDateException.class)
	public void testInsertPastedEvent()
			throws InstanceNotFoundException, AlreadyPastedDateException, DuplicateEventNameException {

		initializeCategories();

		eventCalendar = Calendar.getInstance();
		eventCalendar.set(2014, Calendar.AUGUST, 31);

		eventDemo = new Event("Real Madrid - Barcelona", eventCalendar, categoryDemo);

		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);
	}

	/**
	 * PR-UN-059
	 */

	@Test(expected = DuplicateEventNameException.class)
	public void testInsertDuplicateEvent()
			throws InstanceNotFoundException, AlreadyPastedDateException, DuplicateEventNameException {

		initializeDate();
		initializeCategories();
		initializeEvent();

		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);
		when(eventDaoMock.findDuplicates(eventDemo.getName())).thenReturn(true);

		betService.insertEvent(eventDemo, 2L);
	}

	/**
	 * PR-UN-060
	 */

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testInsertEventWrongCategory()
			throws InstanceNotFoundException, AlreadyPastedDateException, DuplicateEventNameException {

		initializeDate();
		initializeEvent();

		when(categoryDaoMock.find(2L)).thenThrow(InstanceNotFoundException.class);

		betService.insertEvent(eventDemo, 2L);
	}

	/**
	 * PR-UN-061
	 */

	@Test
	public void testInsertBetType()
			throws AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException,
			DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();
		initializeBetOptions();

		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		eventDemo.addBetType(betTypeDemo);

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOptionDemo1);
		betOptions.add(betOptionDemo2);

		betTypeDemo.setBetOptions(betOptions);

		betService.insertBetType(betTypeDemo);

		when(betTypeDaoMock.find(1L)).thenReturn(betTypeDemo);

		assertEquals(betTypeDemo, betTypeDaoMock.find(1L));
	}

	/**
	 * PR-UN-062
	 */

	@Test(expected = MinimunBetOptionException.class)
	public void testInsertBetTypeWithoutOptions()
			throws AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException,
			DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();

		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		eventDemo.addBetType(betTypeDemo);

		betService.insertBetType(betTypeDemo);
	}

	/**
	 * PR-UN-063
	 */

	@Test(expected = MinimunBetOptionException.class)
	public void testInsertBetTypeWithOneOption()
			throws AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException,
			DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();

		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		eventDemo.addBetType(betTypeDemo);

		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75, null, betTypeDemo);

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOptionDemo1);
		betTypeDemo.setBetOptions(betOptions);

		betService.insertBetType(betTypeDemo);
	}

	/**
	 * PR-UN-064
	 */

	@Test(expected = DuplicateBetOptionAnswerException.class)
	public void testInsertBetTypeDuplicateAnswer()
			throws AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException,
			DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();
		initializeBetOptions();

		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		eventDemo.addBetType(betTypeDemo);

		betOptionDemo1 = new BetOption("Real Madrid CF", (float) 1.75, null, betTypeDemo);

		betOptionDemo2 = new BetOption("Real Madrid CF", (float) 1.5, null, betTypeDemo);

		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOptionDemo1);
		betOptions.add(betOptionDemo2);

		betTypeDemo.setBetOptions(betOptions);

		betService.insertBetType(betTypeDemo);
	}

	/**
	 * PR-UN-065
	 */

	@Test
	public void testFindExistentEvent() throws InstanceNotFoundException {

		initializeEvent();

		when(eventDaoMock.find(eventDemo.getEventId())).thenReturn(eventDemo);

		Event foundEvent = betService.findEvent(eventDemo.getEventId());

		assertEquals(eventDemo, foundEvent);
	}

	/**
	 * PR-UN-066
	 */

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNotExistentEvent() throws InstanceNotFoundException {

		Long nonExistentEventId = 0L;

		when(eventDaoMock.find(nonExistentEventId)).thenThrow(InstanceNotFoundException.class);

		betService.findEvent(nonExistentEventId);
	}

	/**
	 * PR-UN-067
	 */

	@Test
	public void testFindExistentBetType() throws InstanceNotFoundException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();

		when(betTypeDaoMock.find(-1L)).thenReturn(betTypeDemo);

		BetType betTypeFound = betService.findBetType(1L);

		assertEquals(betTypeFound, betService.findBetType(1L));
	}

	/**
	 * PR-UN-068
	 */

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNotExistentBetType() throws InstanceNotFoundException {

		when(betTypeDaoMock.find(-1L)).thenThrow(InstanceNotFoundException.class);

		betService.findBetType(-1L);
	}

	/**
	 * PR-UN-069
	 */

	@Test
	public void testFindExistentBetOption() throws InstanceNotFoundException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();
		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75, null, betTypeDemo);

		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		BetOption betOptionFound = betService.findBetOption(1L);

		assertEquals(betOptionFound, betService.findBetOption(1L));
	}

	/**
	 * PR-UN-070
	 */

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNotExistentBetOption() throws InstanceNotFoundException {

		when(betOptionDaoMock.find(-1L)).thenThrow(InstanceNotFoundException.class);

		betService.findBetOption(-1L);
	}

	/**
	 * PR-UN-071
	 */

	@Test
	public void testCheckOptionsSimpleBetType()
			throws InstanceNotFoundException, OnlyOneWonOptionException, NotAllOptionsExistsException {

		initializeSimpleBetType();
		initializeCheckBetOptions();

		Set<Long> winners = new HashSet<Long>();
		winners.add(betOptionDemo.getBetOptionId());

		when(betTypeDaoMock.find(betTypeDemo.getBetTypeId())).thenReturn(betTypeDemo);
		when(betOptionDaoMock.find(betOptionDemo.getBetOptionId())).thenReturn(betOptionDemo);
		when(betOptionDaoMock.find(betOptionDemo1.getBetOptionId())).thenReturn(betOptionDemo1);
		when(betOptionDaoMock.find(betOptionDemo2.getBetOptionId())).thenReturn(betOptionDemo2);

		betService.checkOptions(betTypeDemo.getBetTypeId(), winners);

	}

	/**
	 * PR-UN-072
	 */

	@Test
	public void testCheckOptionsMultipleBetType()
			throws InstanceNotFoundException, OnlyOneWonOptionException, NotAllOptionsExistsException {

		initializeMultipleBetType();
		initializeCheckBetOptions();

		Set<Long> winners = new HashSet<Long>();
		winners.add(betOptionDemo.getBetOptionId());
		winners.add(betOptionDemo1.getBetOptionId());

		when(betTypeDaoMock.find(betTypeDemo.getBetTypeId())).thenReturn(betTypeDemo);
		when(betOptionDaoMock.find(betOptionDemo.getBetOptionId())).thenReturn(betOptionDemo);
		when(betOptionDaoMock.find(betOptionDemo1.getBetOptionId())).thenReturn(betOptionDemo1);
		when(betOptionDaoMock.find(betOptionDemo2.getBetOptionId())).thenReturn(betOptionDemo2);

		betService.checkOptions(betTypeDemo.getBetTypeId(), winners);

	}

	/**
	 * PR-UN-073
	 */

	@Test(expected = NotAllOptionsExistsException.class)
	public void testCheckInvalidOptions()
			throws NotAllOptionsExistsException, InstanceNotFoundException, OnlyOneWonOptionException {
		initializeMultipleBetType();
		initializeCheckBetOptions();

		Long nonExistentOptionId = 0L;
		Set<Long> winners = new HashSet<Long>();
		winners.add(betOptionDemo.getBetOptionId());
		winners.add(nonExistentOptionId);

		when(betTypeDaoMock.find(betTypeDemo.getBetTypeId())).thenReturn(betTypeDemo);
		when(betOptionDaoMock.find(betOptionDemo.getBetOptionId())).thenReturn(betOptionDemo);
		when(betOptionDaoMock.find(betOptionDemo1.getBetOptionId())).thenReturn(betOptionDemo1);
		when(betOptionDaoMock.find(betOptionDemo2.getBetOptionId())).thenReturn(betOptionDemo2);

		betService.checkOptions(betTypeDemo.getBetTypeId(), winners);

	}

	/**
	 * PR-UN-074
	 */

	@Test(expected = OnlyOneWonOptionException.class)
	public void testCheckMultipleOptionsSimpleBetType()
			throws OnlyOneWonOptionException, InstanceNotFoundException, NotAllOptionsExistsException {

		initializeSimpleBetType();
		initializeCheckBetOptions();

		Set<Long> winners = new HashSet<Long>();
		winners.add(betOptionDemo.getBetOptionId());
		winners.add(betOptionDemo1.getBetOptionId());

		when(betTypeDaoMock.find(betTypeDemo.getBetTypeId())).thenReturn(betTypeDemo);
		when(betOptionDaoMock.find(betOptionDemo.getBetOptionId())).thenReturn(betOptionDemo);
		when(betOptionDaoMock.find(betOptionDemo1.getBetOptionId())).thenReturn(betOptionDemo1);
		when(betOptionDaoMock.find(betOptionDemo2.getBetOptionId())).thenReturn(betOptionDemo2);

		betService.checkOptions(betTypeDemo.getBetTypeId(), winners);

	}

	/**
	 * PR-UN-075
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testCheckNonExistentBetTypeOption()
			throws InstanceNotFoundException, OnlyOneWonOptionException, NotAllOptionsExistsException {

		Long nonExistentBetTypeId = 0L;

		Set<Long> winners = new HashSet<Long>();

		when(betTypeDaoMock.find(nonExistentBetTypeId)).thenThrow(InstanceNotFoundException.class);

		betService.checkOptions(nonExistentBetTypeId, winners);

	}

	// @Test(expected = DuplicateBetTypeQuestionException.class)
	// public void testInsertBetTypeDuplicateOption()
	// throws AlreadyPastedDateException, InstanceNotFoundException,
	// DuplicateEventNameException, DuplicateBetTypeQuestionException,
	// DuplicateBetOptionAnswerException, MinimunBetOptionException {
	//
	// Calendar eventCalendar = Calendar.getInstance();
	// eventCalendar.add(Calendar.MINUTE, 1);
	//
	// Category categoryDemo = new Category("Baloncesto");
	//
	// Event eventDemo = new Event("Real Madrid - Barcelona", eventCalendar,
	// categoryDemo);
	//
	// when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);
	//
	// betService.insertEvent(eventDemo, 2L);
	//
	// BetType betTypeDemo = new BetType("¿Qué equipo ganará el encuentro?",
	// false);
	//
	// eventDemo.addBetType(betTypeDemo);
	//
	// BetOption betOptionDemo1 = new BetOption("Real Madrid CF",
	// (float) 1.75, null, betTypeDemo);
	//
	// BetOption betOptionDemo2 = new BetOption("Barcelona", (float) 1.5,
	// null, betTypeDemo);
	//
	// List<BetOption> betOptions = new ArrayList<>();
	// betOptions.add(betOptionDemo1);
	// betOptions.add(betOptionDemo2);
	//
	// betTypeDemo.setBetOptions(betOptions);
	//
	// when(eventDaoMock.find(1L)).thenReturn(eventDemo);
	// when(betTypeDaoMock.findDuplicates(1L,"¿Qué equipo ganará el
	// encuentro?")).thenReturn(true);
	//
	// betService.insertBetType(betTypeDemo);
	// }

	/**
	 * PR-UN-076
	 */

	@Test
	public void testFindCategories() {

		initializeCategories();
		List<Category> listcategories = new ArrayList<>();
		listcategories.add(categoryDemo);
		listcategories.add(categoryDemo1);
		when(categoryDaoMock.findCategories()).thenReturn(listcategories);

		List<Category> listFindCategories = betService.findCategories();

		assertEquals(listFindCategories, listcategories);
	}

	/**
	 * PR-UN-077
	 */

	@Test
	public void testNotFindCategories() {

		initializeCategories();
		List<Category> listcategories = new ArrayList<>();
		when(categoryDaoMock.findCategories()).thenReturn(listcategories);

		List<Category> listFindCategories = betService.findCategories();

		assertEquals(listFindCategories, listcategories);
	}

	/**
	 * PR-UN-078
	 */

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNotExistentCategory() throws InstanceNotFoundException {

		Long nonExistentCategoryId = 0L;

		when(categoryDaoMock.find(nonExistentCategoryId)).thenThrow(InstanceNotFoundException.class);

		betService.findCategory(nonExistentCategoryId);
	}

	/**
	 * PR-UN-079
	 */

	@Test
	public void testFindExistentCategory() throws InstanceNotFoundException {

		initializeCategories();

		when(categoryDaoMock.find(categoryDemo.getCategoryId())).thenReturn(categoryDemo);

		Category foundCategory = betService.findCategory(categoryDemo.getCategoryId());

		assertEquals(categoryDemo, foundCategory);
	}

	/**
	 * PR-UN-080
	 */

	@Test
	public void testFindDuplicates() throws InstanceNotFoundException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();
		eventDemo.addBetType(betTypeDemo);
		when(eventDaoMock.find(1L)).thenReturn(eventDemo);
		when(betTypeDaoMock.findDuplicates(1L, "¿Qué equipo ganará el encuentro?")).thenReturn(true);

		boolean duplicate = betService.findDuplicates(1L, "¿Qué equipo ganará el encuentro?");

		assertEquals(true, duplicate);
	}

	/**
	 * PR-UN-081
	 */

	@Test
	public void testFindNotDuplicates() throws InstanceNotFoundException {

		initializeDate();
		initializeCategories();
		initializeEvent();
		initializeBetType();
		eventDemo.addBetType(betTypeDemo);
		when(eventDaoMock.find(1L)).thenReturn(eventDemo);
		when(betTypeDaoMock.findDuplicates(1L, "¿Qué equipo ganará?")).thenReturn(false);

		boolean duplicate = betService.findDuplicates(1L, "¿Qué equipo ganará?");

		assertEquals(false, duplicate);
	}

	/**
	 * PR-UN-082
	 */

	@Test
	public void findNoBetsByUserIdNumber() {

		initializeUser();

		when(betDaoMock.findBetsByUserIdNumber(userProfileDemo.getUserProfileId())).thenReturn(0);

		int bets = betService.findBetsByUserIdNumber(userProfileDemo.getUserProfileId());

		assertEquals(0, bets);

	}

	/**
	 * PR-UN-083
	 */

	@Test
	public void findBetsByUserIdNumber() {

		initializeUser();

		when(betDaoMock.findBetsByUserIdNumber(userProfileDemo.getUserProfileId())).thenReturn(1);

		int bets = betService.findBetsByUserIdNumber(userProfileDemo.getUserProfileId());

		assertEquals(1, bets);

	}

}
