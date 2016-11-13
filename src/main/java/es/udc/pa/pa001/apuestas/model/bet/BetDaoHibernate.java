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
    implements
    BetDao {

  @SuppressWarnings("unchecked")
  @Override
  public final List<Bet> findBetsByUserId(final Long userId,
      final int startIndex,
      final int count) {

    return getSession().createQuery(
        "SELECT b FROM Bet b WHERE b.userProfile.userProfileId = :usrId"
            + " ORDER BY b.date DESC")
        .setParameter("usrId", userId)
        .setFirstResult(startIndex).setMaxResults(count).list();
  }

  @Override
  public final int findBetsByUserIdNumber(final Long userId) {

    String hqlQuery = "SELECT COUNT (b) FROM Bet b WHERE "
        + "b.userProfile.userProfileId = :usrId";

    Query queryHql = getSession().createQuery(hqlQuery);

    long numberOfOperations = (Long) queryHql
        .setParameter("usrId", userId)
        .uniqueResult();
    return (int) numberOfOperations;
  }
}
