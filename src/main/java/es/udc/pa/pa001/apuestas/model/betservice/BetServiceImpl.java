package es.udc.pa.pa001.apuestas.model.betservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetBlock;
import es.udc.pa.pa001.apuestas.model.bet.BetDao;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betOption.BetOptionDao;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betType.BetTypeDao;
import es.udc.pa.pa001.apuestas.model.betservice.util.AlreadyPastedDateException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetOptionAnswerException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetTypeQuestionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateEventNameException;
import es.udc.pa.pa001.apuestas.model.betservice.util.MinimunBetOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.NotAllOptionsExistsException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OnlyOneWonOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.model.betservice.util.WrongQuantityException;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.category.CategoryDao;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Class BetServiceImpl.
 */
@Service("BetService")
@Transactional
public class BetServiceImpl implements BetService {

  /** The event dao. */
  @Autowired
  private EventDao eventDao;

  /** The category dao. */
  @Autowired
  private CategoryDao categoryDao;

  /** The bet dao. */
  @Autowired
  private BetDao betDao;

  /** The bet type dao. */
  @Autowired
  private BetTypeDao betTypeDao;

  /** The bet option dao. */
  @Autowired
  private BetOptionDao betOptionDao;

  /** The user profile dao. */
  @Autowired
  private UserProfileDao userProfileDao;

  @Override
  public final Event insertEvent(final Event event, final Long categoryId)
      throws AlreadyPastedDateException, InstanceNotFoundException,
      DuplicateEventNameException {

    if (event.getEventStart().getTime()
        .before(Calendar.getInstance().getTime())) {
      throw new AlreadyPastedDateException();
    }

    if (eventDao.findDuplicates(event.getName())) {
      throw new DuplicateEventNameException();
    }

    final Category category = categoryDao.find(categoryId);
    event.setCategory(category);
    eventDao.save(event);
    return event;
  }

  @Override
  @Transactional(
      readOnly = true)
  public final EventBlock findEvents(final String keyWords,
      final Long categoryId, final int startIndex, final int count,
      final boolean admin) {

    List<Event> events = new ArrayList<>();

    if (admin) {
      events = eventDao.findEvents(keyWords, categoryId, startIndex, count + 1,
          admin);
    } else {
      events = eventDao.findEvents(keyWords, categoryId, startIndex, count + 1,
          admin);
    }

    final boolean existMoreEvents = events.size() == (count + 1);

    if (existMoreEvents) {
      events.remove(events.size() - 1);
    }

    return new EventBlock(events, existMoreEvents);
  }

  @Override
  public final BetType insertBetType(final BetType betType)
      throws DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException {

    final List<BetOption> betOptions = betType.getBetOptions();
    if (betOptions == null || betOptions.size() < 2) {
      throw new MinimunBetOptionException();
    }
    final HashSet<String> answers = new HashSet<>();

    for (final BetOption b : betOptions) {
      if (answers.contains(b.getAnswer())) {
        throw new DuplicateBetOptionAnswerException();
      }
      answers.add(b.getAnswer());
    }
    if (betTypeDao.findDuplicates(betType.getEvent().getEventId(),
        betType.getQuestion())) {
      throw new DuplicateBetTypeQuestionException();
    }

    betTypeDao.save(betType);
    for (final BetOption betOption : betOptions) {
      betOption.setBetType(betType);
      betOption.setBetState(null);
      betOptionDao.save(betOption);
    }
    return betType;
  }

  @Override
  public final Bet makeBet(final Long userId, final Long betOptionId,
      final Float betedMoney)
      throws InstanceNotFoundException, OutdatedBetException,
      WrongQuantityException {

    if (betedMoney <= 0) {
      throw new WrongQuantityException();
    }
    final BetOption betOption = betOptionDao.find(betOptionId);

    if (betOption.getBetState() != null) {
      throw new OutdatedBetException();
    }

    final UserProfile userProfile = userProfileDao.find(userId);
    final Event event = betOption.getBetType().getEvent();
    final Bet bet = new Bet(betedMoney, userProfile, event, betOption);
    betDao.save(bet);
    return bet;
  }

  @Transactional(
      readOnly = true)
  @Override
  public final BetBlock findBets(final Long userId, final int startIndex,
      final int count) {

    final List<Bet> betList = betDao.findBetsByUserId(userId, startIndex,
        count + 1);

    final boolean existMoreBets = betList.size() == (count + 1);

    if (existMoreBets) {
      betList.remove(betList.size() - 1);
    }

    return new BetBlock(betList, existMoreBets);
  }

  @Transactional(
      readOnly = true)
  @Override
  public final BetType findBetType(final Long betTypeId)
      throws InstanceNotFoundException {

    return betTypeDao.find(betTypeId);
  }

  @Transactional(
      readOnly = true)
  @Override
  public final Event findEvent(final Long eventId)
      throws InstanceNotFoundException {

    return eventDao.find(eventId);
  }

  @Transactional(
      readOnly = true)
  @Override
  public final BetOption findBetOption(final Long betOptionId)
      throws InstanceNotFoundException {

    return betOptionDao.find(betOptionId);
  }

  @Override
  public final void checkOptions(final Long betTypeId, final Set<Long> winners)
      throws InstanceNotFoundException, OnlyOneWonOptionException,
      NotAllOptionsExistsException {

    final BetType betType = findBetType(betTypeId);

    if (!betType.getMultiple() && winners.size() > 1) {
      throw new OnlyOneWonOptionException();
    } else {

      final Set<Long> betOptions = new HashSet<Long>();

      for (final BetOption betOption : betType.getBetOptions()) {
        betOptions.add(betOption.getBetOptionId());
      }

      if (betOptions.containsAll(winners)) {

        for (final Long betOptionId : winners) {
          findBetOption(betOptionId).setBetState(true);
        }

        final Set<Long> notWinners = new HashSet<Long>();
        notWinners.addAll(betOptions);
        notWinners.removeAll(winners);

        for (final Long betOptionId : notWinners) {
          findBetOption(betOptionId).setBetState(false);
        }

        betTypeDao.save(betType);
      } else {
        throw new NotAllOptionsExistsException();
      }

    }

  }

  @Transactional(
      readOnly = true)
  @Override
  public final Category findCategory(final Long categoryId)
      throws InstanceNotFoundException {
    return categoryDao.find(categoryId);
  }

  @Transactional(
      readOnly = true)
  @Override
  public final List<Category> findCategories() {
    return categoryDao.findCategories();
  }

  @Transactional(
      readOnly = true)
  @Override
  public final int findEventsGetNumber(final String keyWords,
      final Long categoryId, final boolean admin) {
    return eventDao.getNumberOfEvents(keyWords, categoryId, admin);
  }

  @Override
  public final boolean findDuplicates(final Long eventId,
      final String fullName) {
    return betTypeDao.findDuplicates(eventId, fullName);
  }

  @Transactional(
      readOnly = true)
  @Override
  public final int findBetsByUserIdNumber(final Long userId) {
    return betDao.findBetsByUserIdNumber(userId);
  }
}
