package es.udc.pa.pa001.apuestas.model.betType;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface BetTypeDao extends GenericDao<BetType, Long> {

	public boolean findDuplicates(Long eventId, String fullName);

}