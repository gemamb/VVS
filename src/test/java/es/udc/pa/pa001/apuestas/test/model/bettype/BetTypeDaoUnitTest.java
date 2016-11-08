package es.udc.pa.pa001.apuestas.test.model.bettype;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

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
import es.udc.pa.pa001.apuestas.model.betType.BetTypeDao;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;

/**
 * The Class BetTypeDaoUnitTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetTypeDaoUnitTest {

  /** The session factory. */
  @Autowired
  private SessionFactory sessionFactory;

  /** The bet type dao. */
  @Autowired
  private BetTypeDao betTypeDao;

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
   * Initialize event.
   */
  private void initializeEvent() {
    event1 = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
    sessionFactory.getCurrentSession().saveOrUpdate(event1);
  }

  /**
   * Initialize bet type.
   */
  private void initializeBetType() {
    betType = new BetType("¿Que jugador marcara el primer gol?", false);

  }

  /**
   * Initialize event with bet type.
   */
  private void initializeEventWithBetType() {
    initializeBetType();
    event1.addBetType(betType);
    betType.setEvent(event1);
    sessionFactory.getCurrentSession().saveOrUpdate(betType);
  }

  /**
   * PR-UN-031.
   */

  @Test
  public void testBetTypeFindDuplicates() {

    /* SETUP */
    initializeEvent();
    initializeEventWithBetType();

    /* INVOCACION */
    boolean duplicates = betTypeDao.findDuplicates(event1.getEventId(),
        "¿Que jugador marcara el primer gol?");

    /* ASERCION */
    assertTrue(duplicates);

  }

  /**
   * PR-UN-032.
   */

  @Test
  public void testBetTypeFindNoDuplicates() {

    /* SETUP */
    initializeEvent();
    initializeEventWithBetType();

    /* INVOCACION */
    boolean duplicates = betTypeDao.findDuplicates(event1.getEventId(),
        "Bla bla bla");

    /* ASERCION */
    assertFalse(duplicates);

  }

}
