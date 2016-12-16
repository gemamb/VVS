package es.udc.pa.pa001.apuestas.test.model.event;

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
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class EventDaoUnitTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
public class EventDaoUnitTest {

  /** The session factory. */
  @Autowired
  private SessionFactory sessionFactory;

  /** The event dao. */
  @Autowired
  private EventDao eventDao;

  /** The event calendar future. */
  Calendar eventCalendarPast, eventCalendarFuture;

  /** The category 2. */
  Category category1, category2;

  /** The event 3. */
  Event event1, event2, event3;

  /** The user profile. */
  UserProfile userProfile;

  /** The bet option 2. */
  BetOption betOption1, betOption2;

  /** The bet type. */
  BetType betType;

  /** The bet 2. */
  Bet bet1, bet2;

  /**
   * Initialize dates.
   */
  private void initializeDates() {
    eventCalendarPast = Calendar.getInstance();
    eventCalendarFuture = Calendar.getInstance();
    eventCalendarPast.set(2016, Calendar.JANUARY, 31);
    eventCalendarFuture.set(2017, Calendar.AUGUST, 31);
  }

  /**
   * Initialize categories.
   */
  private void initializeCategories() {
    category1 = new Category("Baloncesto");
    category2 = new Category("Futbol");

    sessionFactory.getCurrentSession().saveOrUpdate(category1);
    sessionFactory.getCurrentSession().saveOrUpdate(category2);
  }

  /**
   * Initialize event.
   */
  private void initializeEvent() {
    event1 = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
    sessionFactory.getCurrentSession().saveOrUpdate(event1);
  }

  /**
   * Initialize events.
   */
  private void initializeEvents() {
    event1 = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
    event2 = new Event("Obradoiro - Real Madrid", eventCalendarFuture,
        category1);
    event3 = new Event("Real Madrid - Celta", eventCalendarFuture, category2);

    sessionFactory.getCurrentSession().saveOrUpdate(event1);
    sessionFactory.getCurrentSession().saveOrUpdate(event2);
    sessionFactory.getCurrentSession().saveOrUpdate(event3);
  }

  /**
   * PR-UN-001.
   */

  @Test
  public final void testSaveEvent() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast,
        category1);

    /* INVOCACION */

    eventDao.save(newEvent);

    /* ASERCION */

    /* void method */

  }

  /**
   * PR-UN-002.
   */

  @Test
  public final void testUpdateEvent() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast,
        category1);
    eventDao.save(newEvent);
    newEvent.setName("Deportivo - Celta");

    /* INVOCACION */

    eventDao.save(newEvent);

    /* ASERCION */

    /* void method */

  }

  /**
   * PR-UN-003.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */

  @Test
  public final void testRemoveEvent() throws InstanceNotFoundException {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvent(); /* event1 */

    /* INVOCACION */

    eventDao.remove(event1.getEventId());

    /* ASERCION */

    /* void method */

  }

  /**
   * PR-UN-004.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */

  @Test(expected = InstanceNotFoundException.class)
  public final void testRemoveNonExistentEvent()
      throws InstanceNotFoundException {

    /* SETUP */

    initializeCategories();
    initializeDates();
    Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast,
        category1);
    eventDao.save(newEvent);
    newEvent.setName("Deportivo - Celta");

    Long nonExistentId = 0L;

    /* INVOCACION */

    eventDao.remove(nonExistentId);

    /* ASERCION */

    /* InstanceNotFoundException expected */
  }

  /**
   * PR-UN-005.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */

  @Test
  public final void testFindEvent() throws InstanceNotFoundException {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvent(); /* event1 */

    /* INVOCACION */

    Event foundEvent = eventDao.find(event1.getEventId());

    /* ASERCION */

    assertEquals(event1, foundEvent);

  }

  /**
   * PR-UN-006.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */

  @Test(expected = InstanceNotFoundException.class)
  public final void testFindNonExistentEvent()
      throws InstanceNotFoundException {

    /* SETUP */

    initializeCategories();
    initializeDates();
    Event newEvent = new Event("Real Madrid - Barcelona", eventCalendarPast,
        category1);
    eventDao.save(newEvent);
    newEvent.setName("Deportivo - Celta");

    Long nonExistentId = 0L;

    /* INVOCACION */

    eventDao.find(nonExistentId);

    /* ASERCION */

    /* InstanceNotFoundException expected */

  }

  /**
   * PR-UN-007.
   */

  @Test
  public final void testGetNumberOfAllEvents() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents(null, null, true);

    /* ASERCION */

    assertEquals(3, result);
  }

  /**
   * PR-UN-008.
   */

  @Test
  public final void testGetNumberOfFutureEvents() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents(null, null, false);

    /* ASERCION */

    assertEquals(2, result);
  }

  /**
   * PR-UN-009.
   */

  @Test
  public final void testGetNumberOfCategoryEvents() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents(null, category1.getCategoryId(),
        true);

    /* ASERCION */

    assertEquals(2, result);
  }

  /**
   * PR-UN-010.
   */

  @Test
  public final void testGetNumberOfWrongCategoryEvents() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents(null, 0L, true);

    /* ASERCION */

    assertEquals(0, result);
  }

  /**
   * PR-UN-011.
   */

  @Test
  public final void testGetNumberEventsByMinusKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents("madrid", null, true);

    /* ASERCION */

    assertEquals(3, result);
  }

  /**
   * PR-UN-012.
   */

  @Test
  public final void testGetNumberEventsByKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents("adri", null, true);

    /* ASERCION */

    assertEquals(3, result);
  }

  /**
   * PR-UN-013.
   */

  @Test
  public final void testGetNumberEventsByMayusKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents("MADRID BARCELONA", null, true);

    /* ASERCION */

    assertEquals(1, result);
  }

  /**
   * PR-UN-014.
   */

  @Test
  public final void testGetNumberEventsByOrderKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents("BARCELONA MADRID", null, true);

    /* ASERCION */

    assertEquals(1, result);
  }

  /**
   * PR-UN-015.
   */

  @Test
  public final void testGetNumberEventsByWrongKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents("BarcMad", null, true);

    /* ASERCION */

    assertEquals(0, result);
  }

  /**
   * PR-UN-016.
   */

  @Test
  public final void testGetNumberEventsByKeyWordsCategory() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents("Madrid", category1.getCategoryId(),
        true);

    /* ASERCION */

    assertEquals(2, result);
  }
  
  /**
   * PR-UN-0x.
   */

  @Test
  public final void testGetNumberEventsByKeyWordsCategoryUser() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    int result = eventDao.getNumberOfEvents("Madrid", category1.getCategoryId(),
        false);

    /* ASERCION */

    assertEquals(1, result);
  }

  /**
   * PR-UN-017.
   */

  @Test
  public final void testfindAllEvents() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();
    listEvents.add(event1);
    listEvents.add(event2);
    listEvents.add(event3);

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents(null, null, 0, 10, true);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-018.
   */

  @Test
  public final void testfindFutureEvents() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();
    listEvents.add(event2);
    listEvents.add(event3);

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents(null, null, 0, 10, false);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-019.
   */

  @Test
  public final void testfindEventsByCategory() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();
    listEvents.add(event1);
    listEvents.add(event2);

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents(null,
        category1.getCategoryId(), 0, 10, true);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-020.
   */

  @Test
  public final void testfindEventsByWrongCategory() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents(null, (long) 10, 0, 10,
        true);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-021.
   */

  @Test
  public final void testfindEventsByMinusKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
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
   * PR-UN-022.
   */

  @Test
  public final void testfindEventsByKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();
    listEvents.add(event1);
    listEvents.add(event2);
    listEvents.add(event3);

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents("adri", null, 0, 10, true);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-023.
   */

  @Test
  public final void testfindEventsByMayusKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();
    listEvents.add(event1);

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents("MADRID BARCELONA", null,
        0, 10, true);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-024.
   */

  @Test
  public final void testfindEventsByOrderKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();
    listEvents.add(event1);

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents("BARCELONA MADRID", null,
        0, 10, true);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-025.
   */

  @Test
  public final void testfindEventsByWrongKeyWords() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents("BarcMad", null, 0, 10,
        true);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-026.
   */

  @Test
  public final void testfindEventsByKeyWordsCategory() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();
    List<Event> listEvents = new ArrayList<>();
    listEvents.add(event1);
    listEvents.add(event2);

    /* INVOCACION */

    List<Event> listFindEvents = eventDao.findEvents("Madrid",
        category1.getCategoryId(), 0, 10, true);

    /* ASERCION */

    assertEquals(listFindEvents, listEvents);
  }

  /**
   * PR-UN-027.
   */

  @Test
  public final void testEventFindDuplicates() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    boolean duplicate = eventDao.findDuplicates("Real Madrid - Barcelona");

    /* ASERCION */

    assertEquals(true, duplicate);
  }

  /**
   * PR-UN-028.
   */

  @Test
  public final void testFindNoDuplicates() {

    /* SETUP */

    initializeCategories();
    initializeDates();
    initializeEvents();

    /* INVOCACION */

    boolean duplicate = eventDao.findDuplicates("Real Madrid - Barce");

    /* ASERCION */

    assertEquals(false, duplicate);
  }

}
