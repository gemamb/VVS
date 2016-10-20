package es.udc.pa.pa001.apuestas.test.VVS;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetDao;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class DaoUnitTest {

	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private BetDao betDao;

	Calendar eventCalendarPast, eventCalendarFuture;
	Category category1, category2; 
	Event event1, event2, event3;
	UserProfile userProfile;
	BetOption betOption1, betOption2;
	BetType betType;
	
	public void initializeUser(){
		userProfile = new UserProfile("pepe6", "XxXyYyZzZ",
				"Pepe", "García", "pepe6@gmail.com");
		
		sessionFactory.getCurrentSession().saveOrUpdate(userProfile);
	}
	
	public void initializeDates(){
		eventCalendarPast = Calendar.getInstance();
		eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);
	}
	
	public void initializeCategories(){
		category1 = new Category("Baloncesto");
		category2 = new Category("Futbol");
		
		sessionFactory.getCurrentSession().saveOrUpdate(category1);
		sessionFactory.getCurrentSession().saveOrUpdate(category2);
	}
	
	public void inicializeEvents(){
		event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		
		sessionFactory.getCurrentSession().saveOrUpdate(event1);
		sessionFactory.getCurrentSession().saveOrUpdate(event2);
		sessionFactory.getCurrentSession().saveOrUpdate(event3);
	}
	
	public void initializeBetType(){
		BetType betType = new BetType(
    			"¿Qué jugador marcará el primer gol?",false);
		
		
	}
	
	public void initializeBetOptions(){
		betOption1 = new BetOption("Real Madrid CF",
				(float) 1.75, null, betType);

		betOption2 = new BetOption("Barcelona", (float) 1.5,
				null, betType);
		
		sessionFactory.getCurrentSession().saveOrUpdate(betOption1);
		sessionFactory.getCurrentSession().saveOrUpdate(betOption2);
	}
	
	public void inicializeBets(){

		Bet bet1 = new Bet((float) 2, userProfile, event1, betOption1);
    	Bet bet2 = new Bet((float) 2, userProfile, event1, betOption2);
		
		sessionFactory.getCurrentSession().saveOrUpdate(bet1);
		sessionFactory.getCurrentSession().saveOrUpdate(bet2);
	}
	
	/**
	 * PR-UN-001
	 */

	@Test
	public void testfindAllEvents() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);
		listEvents.add(event3);

		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents(null, null, 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-002
	 */
	
	@Test
	public void testfindFutureEvents() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event2);
		listEvents.add(event3);

		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents(null, null, 0, 10,
				false);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-003
	 */
	
	@Test
	public void testfindEventsByCategory() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);

		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents(null, category1.getCategoryId(), 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-004
	 */
	
	public void testfindEventsByWrongCategory() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();

		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents(null, (long) 10, 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-005
	 */
	
	@Test
	public void testfindEventsByMinusKeyWords() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);
		listEvents.add(event3);
		
		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents("madrid", null, 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-006
	 */
	
	@Test
	public void testfindEventsByKeyWords() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);
		listEvents.add(event3);
		
		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents("adri", null, 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-007
	 */
	
	@Test
	public void testfindEventsByMayusKeyWords() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		
		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents("MADRID BARCELONA", null, 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-008
	 */
	
	@Test
	public void testfindEventsByOrderKeyWords() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		
		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents("BARCELONA MADRID", null, 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-009
	 */
	
	@Test
	public void testfindEventsByWrongKeyWords() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		
		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents("BarcMad", null, 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-010
	 */
	
	@Test
	public void testfindEventsByKeyWordsCategory() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		List<Event> listEvents = new ArrayList<>();
		listEvents.add(event1);
		listEvents.add(event2);
		
		/* INVOCACION */

		List<Event> listFindEvents = eventDao.findEvents("Madrid", category1.getCategoryId(), 0, 10,
				true);

		/* ASERCION */

		assertEquals(listFindEvents, listEvents);
	}
	
	/**
	 * PR-UN-011
	 */
	
	@Test
	public void testGetNumberOfAllEvents() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents(null, null, true);

		/* ASERCION */

		assertEquals(3, result);
	}
	
	/**
	 * PR-UN-012
	 */
	
	@Test
	public void testGetNumberOfFutureEvents() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents(null, null, false);

		/* ASERCION */

		assertEquals(2, result);
	}
	
	/**
	 * PR-UN-013
	 */
	
	@Test
	public void testGetNumberOfCategoryEvents() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents(null, category1.getCategoryId(), true);

		/* ASERCION */

		assertEquals(2, result);
	}
	
	/**
	 * PR-UN-014
	 */
	
	@Test
	public void testGetNumberOfWrongCategoryEvents() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents(null, (long) 10, true);

		/* ASERCION */

		assertEquals(0, result);
	}
	
	/**
	 * PR-UN-015
	 */
	
	@Test
	public void testGetNumberEventsByMinusKeyWords() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents("madrid",null, true);

		/* ASERCION */

		assertEquals(3, result);
	}
	
	/**
	 * PR-UN-016
	 */
	
	@Test
	public void testGetNumberEventsByKeyWords() {
		
		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents("adri",null, true);

		/* ASERCION */

		assertEquals(3, result);
	}
	
	/**
	 * PR-UN-017
	 */
	
	@Test
	public void testGetNumberEventsByMayusKeyWords() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents("MADRID BARCELONA",null, true);

		/* ASERCION */

		assertEquals(1, result);
	}
	
	/**
	 * PR-UN-018
	 */
	
	@Test
	public void testGetNumberEventsByOrderKeyWords() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents("BARCELONA MADRID",null, true);

		/* ASERCION */

		assertEquals(1, result);
	}
	
	/**
	 * PR-UN-019
	 */
	
	@Test
	public void testGetNumberEventsByWrongKeyWords() {
		
		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents("BarcMad",null, true);

		/* ASERCION */

		assertEquals(0, result);
	}
	
	/**
	 * PR-UN-01¡20
	 */
	
	@Test
	public void testGetNumberEventsByKeyWordsCategory() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		int result = eventDao.getNumberOfEvents("Madrid", category1.getCategoryId(), true);

		/* ASERCION */

		assertEquals(2, result);
	}
	
	/**
	 * PR-UN-021
	 */
	
	@Test
	public void testFindDuplicates() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		boolean duplicate = eventDao.findDuplicates("Real Madrid - Barcelona");

		/* ASERCION */

		assertEquals(true, duplicate);
	}
	
	/**
	 * PR-UN-022
	 */
	
	@Test
	public void testFindNotDuplicates() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		inicializeEvents();
		
		/* INVOCACION */

		boolean duplicate = eventDao.findDuplicates("Real Madrid - Barce");

		/* ASERCION */

		assertEquals(false, duplicate);
	}
	
	/**
	 * PR-UN-023
	 */

	@Test
	public void testSaveEvent() {

		/* SETUP */

		initializeCategories();
		initializeDates();
		Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);

		/* INVOCACION */

		eventDao.save(newEvent);

		/* ASERCION */

		/* void method */

	}

	/**
	 * PR-UN-024
	 */

	@Test
	public void testUpdateEvent() {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(newEvent);
		newEvent.setName("Deportivo - Celta");

		/* INVOCACION */

		eventDao.save(newEvent);

		/* ASERCION */

		/* void method */

	}

	/**
	 * PR-UN-025
	 */

	@Test
	public void testFindEvent() throws InstanceNotFoundException {

		/* SETUP */
		
		initializeCategories();
		initializeDates();
		Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(newEvent);
		newEvent.setName("Deportivo - Celta");

		/* INVOCACION */

		Event foundEvent = eventDao.find(newEvent.getEventId());

		/* ASERCION */

		assertEquals(newEvent, foundEvent);

	}

	/**
	 * PR-UN-026
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentEvent() throws InstanceNotFoundException {
		
		/* SETUP */

		initializeCategories();
		initializeDates();
		Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(newEvent);
		newEvent.setName("Deportivo - Celta");

		Long nonExistentId = 0L;

		/* INVOCACION */

		eventDao.find(nonExistentId);

		/* ASERCION */

		/* InstanceNotFoundException expected */

	}

	/**
	 * PR-UN-027
	 */

	@Test
	public void testRemoveEvent() throws InstanceNotFoundException {

		/* SETUP */

		initializeCategories();
		initializeDates();
		Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(newEvent);
		newEvent.setName("Deportivo - Celta");

		/* INVOCACION */

		eventDao.remove(newEvent.getEventId());

		/* ASERCION */

		/* void method */

	}

	/**
	 * PR-UN-028
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveNonExistentEvent() throws InstanceNotFoundException {

		/* SETUP */

		initializeCategories();
		initializeDates();
		Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(newEvent);
		newEvent.setName("Deportivo - Celta");

		Long nonExistentId = 0L;

		/* INVOCACION */

		eventDao.remove(nonExistentId);

		/* ASERCION */

		/* InstanceNotFoundException expected */
	}
	

	/**
	 * PR-UN-029
	 */
	
	@Test
	public void testFindNotBetsByUserIdNumber() {
		
		/* SETUP */
		
		initializeUser();
		
		/* INVOCACION */

		int result = betDao.findBetsByUserIdNumber(userProfile.getUserProfileId());

		/* ASERCION */
		
		assertEquals(result, 0);
	}
	
	/**
	 * PR-UN-030
	 */
	
	@Test
	public void testFindBetsByWrongUserIdNumber() {
		
		/* SETUP */
		
		Long nonExistentId = 0L;
		
		/* INVOCACION */

		int result = betDao.findBetsByUserIdNumber(nonExistentId);

		/* ASERCION */
		
		assertEquals(result, 0);
	}
	
	/**
	 * PR-UN-031
	 */
	
//	@Test
//	public void testFindSomeBetsByUserIdNumber() {
//		
//		/* SETUP */
//		
//		initializeUser();
//		initializeCategories();
//		initializeDates();
//		inicializeEvents();
//		initializeBetType();
//		initializeBetOptions();
//		betType.addBetOption(betOption1);
//		betType.addBetOption(betOption2);
//		sessionFactory.getCurrentSession().saveOrUpdate(betType);
//		event1.addBetType(betType);
//		inicializeBets();
//
//		
//		/* INVOCACION */
//
//		int result = betDao.findBetsByUserIdNumber(userProfile.getUserProfileId());
//
//		/* ASERCION */
//		
//		assertEquals(result, 2);
//	}
}
