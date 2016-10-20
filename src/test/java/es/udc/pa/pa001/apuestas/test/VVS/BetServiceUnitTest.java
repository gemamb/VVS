package es.udc.pa.pa001.apuestas.test.VVS;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
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
	Category categoryDemo;
	Event eventDemo;
	BetType betTypeDemo;
	BetOption betOptionDemo;
	BetOption betOptionDemo1, betOptionDemo2;

	public void initializeUser(){
		userProfileDemo = new UserProfile("pepe6", "XxXyYyZzZ",
				"Pepe", "García", "pepe6@gmail.com");
	}
	
	public void initializeDate(){
		eventCalendar = Calendar.getInstance();
		eventCalendar.set(2017, Calendar.AUGUST, 31);
	}
	
	public void initializeCategory(){
		categoryDemo = new Category("Baloncesto");
	}
	
	public void initializeEvent(){
		eventDemo = new Event("Real Madrid - Barcelona",
				eventCalendar, categoryDemo);
	}
	
	public void initializeBetType(){
		betTypeDemo = new BetType("¿Qué equipo ganará el encuentro?",
				false);
	}
	
	public void initializeBetOptions(){
		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75,
				null, betTypeDemo);

		betOptionDemo1 = new BetOption("Real Madrid CF",
				(float) 1.75, null, betTypeDemo);

		betOptionDemo2 = new BetOption("Barcelona", (float) 1.5,
				null, betTypeDemo);
	}
	
	
	@Test
	public void testfindEventsMoreEvents() {

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);

		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);
		listEvents.add(event3);

		when(eventDaoMock.findEvents(null, null, 0, 10, true)).thenReturn(listEvents);
		EventBlock events = betService.findEvents(null, null, 0, 10,true);

		assertEquals(events.getExistMoreEvents(),false);
	}
	
	@Test
	public void testMakeBet() throws InstanceNotFoundException,
			OutdatedBetException {

		initializeDate();
		initializeCategory();
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

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testMakeBetWrongUser() throws InstanceNotFoundException,
			OutdatedBetException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		initializeBetType();
		initializeBetOptions();
		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(-1L)).thenThrow(
				InstanceNotFoundException.class);
		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		betService.makeBet(-1L, 1L, (float) 2);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testMakeBetWrongBetOption() throws InstanceNotFoundException,
			OutdatedBetException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		initializeUser();
		initializeBetType();
		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(2L)).thenReturn(userProfileDemo);
		when(betOptionDaoMock.find(1L)).thenThrow(
				InstanceNotFoundException.class);

		betService.makeBet(2L, 1L, (float) 2);
	}

	@Test(expected = OutdatedBetException.class)
	public void testOutDatedTrueMakeBet() throws InstanceNotFoundException,
			OutdatedBetException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		initializeUser();
		initializeBetType();
		
		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75,
				true, betTypeDemo);
		
		eventDemo.addBetType(betTypeDemo);

		when(userProfileDaoMock.find(2L)).thenReturn(userProfileDemo);
		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		Bet betDemo = betService.makeBet(2L, 1L, (float) 2);

		when(betDaoMock.find(1L)).thenReturn(betDemo);

		assertEquals(betDemo, betDaoMock.find(1L));
	}

	@Test(expected = OutdatedBetException.class)
	public void testOutDatedFalseMakeBet() throws InstanceNotFoundException,
			OutdatedBetException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		initializeBetType();
		
		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75,
				false, betTypeDemo);
		
		eventDemo.addBetType(betTypeDemo);
		
		when(userProfileDaoMock.find(2L)).thenReturn(userProfileDemo);
		when(betOptionDaoMock.find(1L)).thenReturn(betOptionDemo);

		Bet betDemo = betService.makeBet(2L, 1L, (float) 2);

		when(betDaoMock.find(1L)).thenReturn(betDemo);

		assertEquals(betDemo, betDaoMock.find(1L));
	}

	@Test
	public void testMakeBetWrongMoney() throws InstanceNotFoundException,
			OutdatedBetException {

		initializeDate();
		initializeCategory();
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

	@Test
	public void testInsertEvent() throws InstanceNotFoundException,
			AlreadyPastedDateException, DuplicateEventNameException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		
		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		when(eventDaoMock.find(1L)).thenReturn(eventDemo);

		assertEquals(eventDemo, eventDaoMock.find(1L));
	}

	@Test(expected = AlreadyPastedDateException.class)
	public void testInsertPastedEvent() throws InstanceNotFoundException,
			AlreadyPastedDateException, DuplicateEventNameException {

		initializeCategory();
		
		eventCalendar = Calendar.getInstance();
		eventCalendar.set(2014, Calendar.AUGUST, 31);
		
		eventDemo = new Event("Real Madrid - Barcelona",
				eventCalendar, categoryDemo);
		
		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);
	}

	@Test(expected = DuplicateEventNameException.class)
	public void testInsertDuplicateEvent() throws InstanceNotFoundException,
			AlreadyPastedDateException, DuplicateEventNameException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		
		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);
		when(eventDaoMock.findDuplicates(eventDemo.getName())).thenReturn(true);

		betService.insertEvent(eventDemo, 2L);
	}

	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testInsertEventWrongCategory()
			throws InstanceNotFoundException, AlreadyPastedDateException,
			DuplicateEventNameException {

		initializeDate();
		initializeEvent();
		
		when(categoryDaoMock.find(2L)).thenThrow(
				InstanceNotFoundException.class);

		betService.insertEvent(eventDemo, 2L);
	}

	@Test
	public void testInsertBetType() throws AlreadyPastedDateException,
			InstanceNotFoundException, DuplicateEventNameException,
			DuplicateBetTypeQuestionException,
			DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategory();
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

	@Test(expected = MinimunBetOptionException.class)
	public void testInsertBetTypeWithoutOptions()
			throws AlreadyPastedDateException, InstanceNotFoundException,
			DuplicateEventNameException, DuplicateBetTypeQuestionException,
			DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		initializeBetType();
		
		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		eventDemo.addBetType(betTypeDemo);

		betService.insertBetType(betTypeDemo);
	}

	@Test(expected = MinimunBetOptionException.class)
	public void testInsertBetTypeWithOneOption()
			throws AlreadyPastedDateException, InstanceNotFoundException,
			DuplicateEventNameException, DuplicateBetTypeQuestionException,
			DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		initializeBetType();
		
		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		eventDemo.addBetType(betTypeDemo);
		
		betOptionDemo = new BetOption("Real Madrid CF", (float) 1.75,
				null, betTypeDemo);
		
		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOptionDemo1);
		betTypeDemo.setBetOptions(betOptions);

		betService.insertBetType(betTypeDemo);
	}

	@Test(expected = DuplicateBetOptionAnswerException.class)
	public void testInsertBetTypeDuplicateAnswer()
			throws AlreadyPastedDateException, InstanceNotFoundException,
			DuplicateEventNameException, DuplicateBetTypeQuestionException,
			DuplicateBetOptionAnswerException, MinimunBetOptionException {

		initializeDate();
		initializeCategory();
		initializeEvent();
		initializeBetType();
		initializeBetOptions();
		
		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);

		betService.insertEvent(eventDemo, 2L);

		eventDemo.addBetType(betTypeDemo);

		betOptionDemo1 = new BetOption("Real Madrid CF",
				(float) 1.75, null, betTypeDemo);

		betOptionDemo2 = new BetOption("Real Madrid CF", (float) 1.5,
				null, betTypeDemo);
		
		List<BetOption> betOptions = new ArrayList<>();
		betOptions.add(betOptionDemo1);
		betOptions.add(betOptionDemo2);

		betTypeDemo.setBetOptions(betOptions);

		betService.insertBetType(betTypeDemo);
	}

//	@Test(expected = DuplicateBetTypeQuestionException.class)
//	public void testInsertBetTypeDuplicateOption()
//			throws AlreadyPastedDateException, InstanceNotFoundException,
//			DuplicateEventNameException, DuplicateBetTypeQuestionException,
//			DuplicateBetOptionAnswerException, MinimunBetOptionException {
//
//		Calendar eventCalendar = Calendar.getInstance();
//		eventCalendar.add(Calendar.MINUTE, 1);
//
//		Category categoryDemo = new Category("Baloncesto");
//
//		Event eventDemo = new Event("Real Madrid - Barcelona", eventCalendar,
//				categoryDemo);
//
//		when(categoryDaoMock.find(2L)).thenReturn(categoryDemo);
//
//		betService.insertEvent(eventDemo, 2L);
//
//		BetType betTypeDemo = new BetType("¿Qué equipo ganará el encuentro?",
//				false);
//
//		eventDemo.addBetType(betTypeDemo);
//
//		BetOption betOptionDemo1 = new BetOption("Real Madrid CF",
//				(float) 1.75, null, betTypeDemo);
//
//		BetOption betOptionDemo2 = new BetOption("Barcelona", (float) 1.5,
//				null, betTypeDemo);
//
//		List<BetOption> betOptions = new ArrayList<>();
//		betOptions.add(betOptionDemo1);
//		betOptions.add(betOptionDemo2);
//
//		betTypeDemo.setBetOptions(betOptions);
//
//		when(eventDaoMock.find(1L)).thenReturn(eventDemo);
//		when(betTypeDaoMock.findDuplicates(1L,"¿Qué equipo ganará el encuentro?")).thenReturn(true);
//
//		betService.insertBetType(betTypeDemo);
//	}
}
