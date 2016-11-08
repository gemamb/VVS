package es.udc.pa.pa001.apuestas.model.bet;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

/**
 * The Interface BetDao.
 */
public interface BetDao extends GenericDao<Bet, Long> {

  /**
   * Find bets by user id.
   *
   * @param userId
   *          the user id
   * @param startIndex
   *          the start index
   * @param count
   *          the count
   * @return the list
   */
  List<Bet> findBetsByUserId(Long userId, int startIndex, int count);

  /**
   * Find bets by user id number.
   *
   * @param userId
   *          the user id
   * @return the int
   */
  int findBetsByUserIdNumber(Long userId);
}
