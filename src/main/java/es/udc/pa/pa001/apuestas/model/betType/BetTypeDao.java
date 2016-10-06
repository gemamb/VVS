package es.udc.pa.pa001.apuestas.model.betType;

import java.util.List;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface BetTypeDao extends GenericDao<BetType, Long> {

	public boolean findDuplicates(Long eventId, String fullName);
	
}