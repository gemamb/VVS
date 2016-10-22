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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetTypeDaoUnitTest {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private BetTypeDao betTypeDao;

	Calendar eventCalendarPast, eventCalendarFuture;
	Category category1, category2;
	Event event1, event2, event3;
	UserProfile userProfile;
	BetOption betOption1, betOption2;
	BetType betType;
	Bet bet1, bet2;

	private void initializeEvent() {
		event1 = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		sessionFactory.getCurrentSession().saveOrUpdate(event1);
	}

	private void initializeBetType() {
		betType = new BetType("¿Que jugador marcara el primer gol?", false);

	}

	private void initializeEventWithBetType() {
		initializeBetType();
		event1.addBetType(betType);
		betType.setEvent(event1);
		sessionFactory.getCurrentSession().saveOrUpdate(betType);
	}

	/**
	 * PR-UN-031
	 */

	@Test
	public void testBetTypeFindDuplicates() {

		/* SETUP */
		initializeEvent();
		initializeEventWithBetType();

		/* INVOCACION */
		boolean duplicates = betTypeDao.findDuplicates(event1.getEventId(), "¿Que jugador marcara el primer gol?");

		/* ASERCION */
		assertTrue(duplicates);

	}

	/**
	 * PR-UN-032
	 */

	@Test
	public void testBetTypeFindNoDuplicates() {

		/* SETUP */
		initializeEvent();
		initializeEventWithBetType();

		/* INVOCACION */
		boolean duplicates = betTypeDao.findDuplicates(event1.getEventId(), "Bla bla bla");

		/* ASERCION */
		assertFalse(duplicates);

	}

}
