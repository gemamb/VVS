package es.udc.pa.pa001.apuestas.model.bet;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class BetDaoHibernate extends GenericDaoHibernate<Bet, Long> implements BetDao {

	@Override
	public List<Bet> findBetsByUserId(Long userId, int startIndex, int count) {

		return getSession()
		        .createQuery("SELECT b FROM Bet b WHERE b.userProfile.userProfileId = :usrId" + " ORDER BY b.date DESC")
		        .setParameter("usrId", userId).setFirstResult(startIndex).setMaxResults(count).list();
	}

	@Override
	public int findBetsByUserIdNumber(Long userId) {

		String hqlQuery = "SELECT COUNT (b) FROM Bet b WHERE b.userProfile.userProfileId = :usrId";

		Query queryHql = getSession().createQuery(hqlQuery);

		long numberOfOperations = (Long) queryHql.setParameter("usrId", userId).uniqueResult();
		return (int) numberOfOperations;
	}
}
