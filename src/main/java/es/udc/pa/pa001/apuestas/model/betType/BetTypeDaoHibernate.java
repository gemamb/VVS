package es.udc.pa.pa001.apuestas.model.betType;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository
public class BetTypeDaoHibernate extends GenericDaoHibernate<BetType, Long>
		implements BetTypeDao {


	public boolean findDuplicates(Long eventId, String fullName) {
		return !getSession()
				.createQuery(
						"Select e from BetType e where e.question = :fullName and"
								+ " e.event.eventId =:eventId")
				.setParameter("eventId", eventId)
				.setParameter("fullName", fullName).list().isEmpty();
	}

	
}