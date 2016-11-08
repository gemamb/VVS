package es.udc.pa.pa001.apuestas.test.model.betservice;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.category.CategoryDao;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;
import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;

/**
 * The Class BetServiceAleatoryIntegrationTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
public class BetServiceAleatoryIntegrationTest {

  /** The category dao. */
  @Autowired
  private CategoryDao categoryDao;

  /** The event dao. */
  @Autowired
  private EventDao eventDao;

  /** The bet type dao. */
  @Autowired
  private BetTypeDao betTypeDao;

  /** The bet option dao. */
  @Autowired
  private BetOptionDao betOptionDao;

  /** The user profile dao. */
  @Autowired
  private UserProfileDao userProfileDao;

  /** The bet dao. */
  @Autowired
  private BetDao betDao;

  /** The bet service. */
  @Autowired
  private BetService betService;

  /** The bet option 2. */
  BetOption betOption1, betOption2;

  /** The user. */
  UserProfile user;

  /**
   * Initialize user.
   */
  private void initializeUser() {
    user = new UserProfile("pepe6", "XxXyYyZzZ", "Pepe", "García",
        "pepe6@gmail.com");
    userProfileDao.save(user);
  }

  /**
   * Initialize bet options.
   */
  private void initializeBetOptions() {

    Category category1 = new Category("Baloncesto");
    categoryDao.save(category1);

    Calendar eventCalendar = Calendar.getInstance();
    eventCalendar.set(2017, Calendar.AUGUST, 31);

    Event event = new Event("Real Madrid - Barcelona", eventCalendar,
        category1);
    eventDao.save(event);

    BetType betType = new BetType("¿Qué equipo ganará el encuentro?", false);

    betOption1 = new BetOption("Real Madrid CF", (float) 1.75, null, betType);
    betOption2 = new BetOption("Barcelona", (float) 1.75, null, betType);

    List<BetOption> betOptions = new ArrayList<>();
    betOptions.add(betOption1);
    betOptions.add(betOption2);

    betType.setBetOptions(betOptions);
    event.addBetType(betType);
    betTypeDao.save(betType);
    betOptionDao.save(betOption1);
    betOptionDao.save(betOption2);

    event.addBetType(betType);
  }

  /**
   * Test make bet.
   *
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   */
  @Test
  public void testMakeBet() throws InstanceNotFoundException,
      OutdatedBetException, DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException,
      AlreadyPastedDateException, DuplicateEventNameException {

    initializeBetOptions();
    initializeUser();

    Generator<Double> aleatoryQuantity = PrimitiveGenerators.doubles(-100000,
        +100000);

    float quantity = aleatoryQuantity.next().floatValue();

    Bet bet = betService.makeBet(user.getUserProfileId(),
        betOption1.getBetOptionId(), quantity);
    Bet betFound = betDao.find(bet.getBetId());
    assertEquals(bet, betFound);

    List<Bet> betFounds = betDao.findBetsByUserId(user.getUserProfileId(), 0,
        10);
    assertTrue(betFounds.contains(bet));
  }
}
