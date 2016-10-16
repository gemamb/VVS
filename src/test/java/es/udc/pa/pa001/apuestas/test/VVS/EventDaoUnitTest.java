package es.udc.pa.pa001.apuestas.test.VVS;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.category.CategoryDao;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class EventDaoUnitTest {

	@Autowired
	private EventDao eventDao;

	@Autowired
	private CategoryDao categoryDao;

	/**
	 * PR-UN-001
	 */

	@Test
	public void testfindAllEvents() {

		/* SETUP */

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		Category category2 = new Category("Futbol");
		categoryDao.save(category1);
		categoryDao.save(category2);

		Event event1 = new Event("Real Madrid - Barcelona", eventCalendarPast,
				category1);
		Event event2 = new Event("Obradoiro - Real Madrid",
				eventCalendarFuture, category1);
		Event event3 = new Event("Real Madrid - Celta", eventCalendarFuture,
				category2);
		eventDao.save(event1);
		eventDao.save(event2);
		eventDao.save(event3);
		
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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		categoryDao.save(category1);

		Event event = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);

		/* INVOCACION */

		eventDao.save(event);

		/* ASERCION */

		/* void method */

	}

	/**
	 * PR-UN-024
	 */

	@Test
	public void testUpdateEvent() {

		/* SETUP */

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		categoryDao.save(category1);

		Event event = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(event);
		event.setName("Deportivo - Celta");

		/* INVOCACION */

		eventDao.save(event);

		/* ASERCION */

		/* void method */

	}

	/**
	 * PR-UN-025
	 */

	@Test
	public void testFindEvent() throws InstanceNotFoundException {

		/* SETUP */

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		categoryDao.save(category1);

		Event event = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(event);
		event.setName("Deportivo - Celta");

		/* INVOCACION */

		Event foundEvent = eventDao.find(event.getEventId());

		/* ASERCION */

		assertEquals(event, foundEvent);

	}

	/**
	 * PR-UN-026
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentEvent() throws InstanceNotFoundException {

		/* SETUP */

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		categoryDao.save(category1);

		Event event = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(event);
		event.setName("Deportivo - Celta");

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

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		categoryDao.save(category1);

		Event event = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(event);
		event.setName("Deportivo - Celta");

		/* INVOCACION */

		eventDao.remove(event.getEventId());

		/* ASERCION */

		/* void method */

	}

	/**
	 * PR-UN-028
	 */

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveNonExistentEvent() throws InstanceNotFoundException {

		/* SETUP */

		Calendar eventCalendarPast = Calendar.getInstance();
		Calendar eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);

		Category category1 = new Category("Baloncesto");
		categoryDao.save(category1);

		Event event = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		eventDao.save(event);
		event.setName("Deportivo - Celta");

		Long nonExistentId = 0L;

		/* INVOCACION */

		eventDao.remove(nonExistentId);

		/* ASERCION */

		/* InstanceNotFoundException expected */

	}
}
