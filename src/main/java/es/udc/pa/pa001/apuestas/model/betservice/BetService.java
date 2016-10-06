package es.udc.pa.pa001.apuestas.model.betservice;

import java.util.List;
import java.util.Set;

import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betType.BetTypeBlock;
import es.udc.pa.pa001.apuestas.model.betservice.util.AlreadyPastedDateException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetOptionAnswerException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetTypeQuestionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateEventNameException;
import es.udc.pa.pa001.apuestas.model.betservice.util.MinimunBetOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.NotAllOptionsExistsException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OnlyOneWonOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetBlock;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface BetService {

	public int findEventsGetNumber(String keywords, Long categoryId, boolean admin);
	public EventBlock findEvents(String keywords, Long categoryId,
			int startIndex, int count, boolean admin);

	public Bet makeBet(Long userId, Long betOptionId, Float betedMoney)
		throws InstanceNotFoundException, OutdatedBetException;
	
	public BetBlock findBets(Long userId,int startIndex, int count);
	
	public Event insertEvent(Event event, Long CategoryId)
		throws AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException;
	
	public BetType insertBetType(BetType betType) throws DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException;
		
	public Event findEvent (Long Event) throws InstanceNotFoundException;
	public BetType findBetType (Long betTypeId) throws InstanceNotFoundException;
	
	public BetOption findBetOption (Long betOptionId) throws InstanceNotFoundException;
	
	public void checkOptions (Long betTypeId, Set<Long> winned) 
			throws InstanceNotFoundException, OnlyOneWonOptionException, NotAllOptionsExistsException;
	
	public List<Category> findCategories();

	Category findCategory(Long categoryId) throws InstanceNotFoundException;
	boolean findDuplicates(Long eventId, String fullName);
	int findBetsByUserIdNumber(Long userId);
}
