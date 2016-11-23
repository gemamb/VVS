package es.udc.pa.pa001.apuestas.model.betservice;

import java.util.List;
import java.util.Set;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetBlock;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betservice.util.AlreadyPastedDateException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetOptionAnswerException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetTypeQuestionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateEventNameException;
import es.udc.pa.pa001.apuestas.model.betservice.util.MinimunBetOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.NotAllOptionsExistsException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OnlyOneWonOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * The Interface BetService.
 */
public interface BetService {

  /**
   * Find events get number.
   *
   * @param keywords
   *          the keywords
   * @param categoryId
   *          the category id
   * @param admin
   *          the admin
   * @return the int
   */
  int findEventsGetNumber(String keywords, Long categoryId, boolean admin);

  /**
   * Find events.
   *
   * @param keywords
   *          the keywords
   * @param categoryId
   *          the category id
   * @param startIndex
   *          the start index
   * @param count
   *          the count
   * @param admin
   *          the admin
   * @return the event block
   */
  EventBlock findEvents(String keywords, Long categoryId, int startIndex,
      int count, boolean admin);

  /**
   * Make bet.
   *
   * @param userId
   *          the user id
   * @param betOptionId
   *          the bet option id
   * @param betedMoney
   *          the beted money
   * @return the bet
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OutdatedBetException
   *           the outdated bet exception
   */
  Bet makeBet(Long userId, Long betOptionId, Float betedMoney)
      throws InstanceNotFoundException, OutdatedBetException;

  /**
   * Find bets.
   *
   * @param userId
   *          the user id
   * @param startIndex
   *          the start index
   * @param count
   *          the count
   * @return the bet block
   */
  BetBlock findBets(Long userId, int startIndex, int count);

  /**
   * Insert event.
   *
   * @param event
   *          the event
   * @param categoryId
   *          the category id
   * @return the event
   * @throws AlreadyPastedDateException
   *           the already pasted date exception
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws DuplicateEventNameException
   *           the duplicate event name exception
   */
  Event insertEvent(Event event, Long categoryId)
      throws AlreadyPastedDateException, InstanceNotFoundException,
      DuplicateEventNameException;

  /**
   * Insert bet type.
   *
   * @param betType
   *          the bet type
   * @return the bet type
   * @throws DuplicateBetTypeQuestionException
   *           the duplicate bet type question exception
   * @throws DuplicateBetOptionAnswerException
   *           the duplicate bet option answer exception
   * @throws MinimunBetOptionException
   *           the minimun bet option exception
   */
  BetType insertBetType(BetType betType)
      throws DuplicateBetTypeQuestionException,
      DuplicateBetOptionAnswerException, MinimunBetOptionException;

  /**
   * Find event.
   *
   * @param event
   *          the event
   * @return the event
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */
  Event findEvent(Long event) throws InstanceNotFoundException;

  /**
   * Find bet type.
   *
   * @param betTypeId
   *          the bet type id
   * @return the bet type
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */
  BetType findBetType(Long betTypeId) throws InstanceNotFoundException;

  /**
   * Find bet option.
   *
   * @param betOptionId
   *          the bet option id
   * @return the bet option
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */
  BetOption findBetOption(Long betOptionId) throws InstanceNotFoundException;

  /**
   * Check options.
   *
   * @param betTypeId
   *          the bet type id
   * @param winned
   *          the winned
   * @throws InstanceNotFoundException
   *           the instance not found exception
   * @throws OnlyOneWonOptionException
   *           the only one won option exception
   * @throws NotAllOptionsExistsException
   *           the not all options exists exception
   */
  void checkOptions(Long betTypeId, Set<Long> winned)
      throws InstanceNotFoundException, OnlyOneWonOptionException,
      NotAllOptionsExistsException;

  /**
   * Find categories.
   *
   * @return the list
   */
  List<Category> findCategories();

  /**
   * Find category.
   *
   * @param categoryId
   *          the category id
   * @return the category
   * @throws InstanceNotFoundException
   *           the instance not found exception
   */
  Category findCategory(Long categoryId) throws InstanceNotFoundException;

  /**
   * Find duplicates.
   *
   * @param eventId
   *          the event id
   * @param fullName
   *          the full name
   * @return true, if successful
   */
  boolean findDuplicates(Long eventId, String fullName);

  /**
   * Find bets by user id number.
   *
   * @param userId
   *          the user id
   * @return the int
   */
  int findBetsByUserIdNumber(Long userId);
}
