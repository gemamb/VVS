package es.udc.pa.pa001.apuestas.test.dao;

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
public class UnitTest {

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
}
