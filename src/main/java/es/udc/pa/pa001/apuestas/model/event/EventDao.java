package es.udc.pa.pa001.apuestas.model.event;

import java.util.Calendar;
import java.util.List;

import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface EventDao extends GenericDao<Event, Long> {
	
	public int getNumberOfEvents(String keyWords, Long categoryId,boolean admin);
	
    public List<Event> findEvents(String keyWords, Long categoryId,
    		int startIndex, int count, boolean admin);
    
    public boolean findDuplicates(String fullName);
}
