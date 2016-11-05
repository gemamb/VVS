package es.udc.pa.pa001.apuestas.model.betType;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository
public class BetTypeDaoHibernate extends GenericDaoHibernate<BetType, Long>
implements BetTypeDao {

	@Override
	public boolean findDuplicates(Long eventId, String fullName) {
		return !getSession()
				.createQuery(
						"Select e from BetType e where e.question = :fullName and"
								+ " e.event.eventId =:eventId")
								.setParameter("eventId", eventId)
								.setParameter("fullName", fullName).list().isEmpty();
	}

}