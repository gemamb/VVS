package es.udc.pa.pa001.apuestas.model.bet;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

/**
 * The Class BetDaoHibernate.
 */
@Repository
public class BetDaoHibernate extends GenericDaoHibernate<Bet, Long>
    implements BetDao {

  /*
   * (non-Javadoc)
   * 
   * @see es.udc.pa.pa001.apuestas.model.bet.BetDao#findBetsByUserId(java.lang.Long, int, int)
   */
  @Override
  public List<Bet> findBetsByUserId(Long userId, int startIndex, int count) {

    return getSession()
        .createQuery(
            "SELECT b FROM Bet b WHERE b.userProfile.userProfileId = :usrId"
                + " ORDER BY b.date DESC")
        .setParameter("usrId", userId).setFirstResult(startIndex)
        .setMaxResults(count).list();
  }

  /*
   * (non-Javadoc)
   * 
   * @see es.udc.pa.pa001.apuestas.model.bet.BetDao#findBetsByUserIdNumber(java.lang.Long)
   */
  @Override
  public int findBetsByUserIdNumber(Long userId) {

    String hqlQuery = "SELECT COUNT (b) FROM Bet b WHERE b.userProfile.userProfileId = :usrId";

    Query queryHql = getSession().createQuery(hqlQuery);

    long numberOfOperations = (Long) queryHql.setParameter("usrId", userId)
        .uniqueResult();
    return (int) numberOfOperations;
  }
}
