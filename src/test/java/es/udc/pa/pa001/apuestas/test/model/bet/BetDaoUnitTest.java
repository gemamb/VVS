package es.udc.pa.pa001.apuestas.test.model.bet;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetDaoUnitTest {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private BetDao betDao;

	Calendar eventCalendarPast, eventCalendarFuture;
	Category category1, category2;
	Event event1, event2, event3;
	UserProfile userProfile;
	BetOption betOption1, betOption2;
	BetType betType;
	Bet bet1, bet2;

	private void initializeUser() {
		userProfile = new UserProfile("pepe6", "XxXyYyZzZ", "Pepe", "García", "pepe6@gmail.com");

		sessionFactory.getCurrentSession().saveOrUpdate(userProfile);
	}

	private void initializeDates() {
		eventCalendarPast = Calendar.getInstance();
		eventCalendarFuture = Calendar.getInstance();
		eventCalendarPast.set(2016, Calendar.JANUARY, 31);
		eventCalendarFuture.set(2017, Calendar.AUGUST, 31);
	}

	private void initializeCategories() {
		category1 = new Category("Baloncesto");
		category2 = new Category("Futbol");

		sessionFactory.getCurrentSession().saveOrUpdate(category1);
		sessionFactory.getCurrentSession().saveOrUpdate(category2);
	}

	private void initializeEvents() {
		event1 = new Event("Real Madrid - Barcelona", eventCalendarPast, category1);
		event2 = new Event("Obradoiro - Real Madrid", eventCalendarFuture, category1);
		event3 = new Event("Real Madrid - Celta", eventCalendarFuture, category2);

		sessionFactory.getCurrentSession().saveOrUpdate(event1);
		sessionFactory.getCurrentSession().saveOrUpdate(event2);
		sessionFactory.getCurrentSession().saveOrUpdate(event3);
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

	private void initializeBetOptions() {
		betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);

		betOption2 = new BetOption("Barcelona", (float) 1.5, null, betType);

		sessionFactory.getCurrentSession().saveOrUpdate(betOption1);
		sessionFactory.getCurrentSession().saveOrUpdate(betOption2);
	}

	private void inicializeBets() {

		bet1 = new Bet((float) 2, userProfile, event1, betOption1);
		bet2 = new Bet((float) 2, userProfile, event1, betOption2);

		sessionFactory.getCurrentSession().saveOrUpdate(bet1);
		sessionFactory.getCurrentSession().saveOrUpdate(bet2);
	}

	/**
	 * PR-UN-033
	 */

	@Test
	public void testFindNoBetsByUserId() {

		/* SETUP */

		initializeUser();
		List<Bet> foundBets = new ArrayList<Bet>();

		/* INVOCACION */

		foundBets = betDao.findBetsByUserId(userProfile.getUserProfileId(), 0, 1);

		/* ASERCION */

		assertTrue(foundBets.isEmpty());
	}

	/**
	 * PR-UN-034
	 */

	@Test
	public void testFindBetsByNonExistentUserId() {

		/* SETUP */

		Long nonExistentId = 0L;
		List<Bet> foundBets = new ArrayList<Bet>();

		/* INVOCACION */

		foundBets = betDao.findBetsByUserId(nonExistentId, 0, 1);

		/* ASERCION */

		assertTrue(foundBets.isEmpty());
	}

	/**
	 * PR-UN-035
	 */

	@Test
	public void testFindSomeBetsByUserId() {

		/* SETUP */

		initializeUser();
		initializeCategories();
		initializeDates();
		initializeEvents();
		initializeEventWithBetType();
		initializeBetOptions();
		inicializeBets();

		List<Bet> bets = new ArrayList<Bet>();
		List<Bet> foundBets = new ArrayList<Bet>();

		bets.add(bet1);
		bets.add(bet2);

		/* INVOCACION */

		foundBets = betDao.findBetsByUserId(userProfile.getUserProfileId(), 0, 2);

		/* ASERCION */

		assertEquals(bets, foundBets);
	}

	/**
	 * PR-UN-036
	 */

	@Test
	public void testFindNoBetsByUserIdNumber() {

		/* SETUP */

		initializeUser();

		/* INVOCACION */

		int result = betDao.findBetsByUserIdNumber(userProfile.getUserProfileId());

		/* ASERCION */

		assertEquals(result, 0);
	}

	/**
	 * PR-UN-037
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
	 * PR-UN-038
	 */

	@Test
	public void testFindSomeBetsByUserIdNumber() {

		/* SETUP */

		initializeUser();
		initializeCategories();
		initializeDates();
		initializeEvents();
		initializeEventWithBetType();
		initializeBetOptions();
		inicializeBets();

		/* INVOCACION */

		int result = betDao.findBetsByUserIdNumber(userProfile.getUserProfileId());

		/* ASERCION */

		assertEquals(result, 2);
	}

}
