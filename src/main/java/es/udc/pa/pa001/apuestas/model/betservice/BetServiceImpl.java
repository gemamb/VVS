package es.udc.pa.pa001.apuestas.model.betservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetBlock;
import es.udc.pa.pa001.apuestas.model.bet.BetDao;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betOption.BetOptionDao;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betType.BetTypeDao;
import es.udc.pa.pa001.apuestas.model.betservice.util.AlreadyPastedDateException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetOptionAnswerException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetTypeQuestionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateEventNameException;
import es.udc.pa.pa001.apuestas.model.betservice.util.MinimunBetOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.NotAllOptionsExistsException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OnlyOneWonOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.category.CategoryDao;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("BetService")
@Transactional
public class BetServiceImpl implements BetService {

	@Autowired
	private EventDao eventDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private BetDao betDao;
	@Autowired
	private BetTypeDao betTypeDao;
	@Autowired
	private BetOptionDao betOptionDao;
	@Autowired
	private UserProfileDao userProfileDao;

	@Override
	public Event insertEvent(Event event,Long categoryId) 
			throws AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException {
		if (event.getEventStart().getTime().before(Calendar.getInstance().getTime ())) 
			throw new AlreadyPastedDateException();
		
		if (eventDao.findDuplicates(event.getName())) 
			throw new DuplicateEventNameException();
		
		Category category = categoryDao.find(categoryId);
		event.setCategory(category);
		eventDao.save(event);
		return event;
	}

	@Override
	@Transactional(readOnly = true)
	public EventBlock findEvents(String keyWords, Long categoryId,
			int startIndex, int count, boolean admin) {

		List<Event> events = new ArrayList<>();
		
		if (admin){
			events = eventDao.findEvents(keyWords, categoryId,
					startIndex, count + 1, admin);
		}else {
			Calendar now = Calendar.getInstance();
			events = eventDao.findEvents(keyWords, categoryId,
					startIndex, count + 1, admin);
		}
		
		boolean existMoreEvents = events.size() == (count + 1);

		if (existMoreEvents) {
			events.remove(events.size() - 1);
		}

		return new EventBlock(events, existMoreEvents);
	}
	
	@Override
	public BetType insertBetType(BetType betType) throws DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException {
		
		List<BetOption> betOptions = betType.getBetOptions();
		if (betOptions==null || betOptions.size()<2)
			throw new MinimunBetOptionException();
		HashSet answers = new HashSet();
		
		for(BetOption b : betOptions){
			if(answers.contains(b.getAnswer()))
				throw new DuplicateBetOptionAnswerException();
			answers.add(b.getAnswer());
		}
		if (betTypeDao.findDuplicates(betType.getEvent().getEventId(),betType.getQuestion()))
			throw new DuplicateBetTypeQuestionException();
				
		betTypeDao.save(betType);
		for (BetOption betOption : betOptions){
			betOption.setBetType(betType);
			betOption.setBetState(null);
			betOptionDao.save(betOption);
		}
		return betType;
	}	

    @Override
    public Bet makeBet(Long userId, Long betOptionId, Float betedMoney) 
            throws InstanceNotFoundException, OutdatedBetException {
    	
    	BetOption betOption = betOptionDao.find(betOptionId);
    	
    	if (betOption.getBetState() != null)
    		throw new OutdatedBetException();
    	
//    	if (betedMoney <=0)
//    		throw new InputValidationException();
    	
        UserProfile userProfile = userProfileDao.find(userId);
        Event event = betOption.getBetType().getEvent();
        Bet bet = new Bet(betedMoney,userProfile,event,betOption);
        betDao.save(bet);
        return bet;
    }

    @Transactional(readOnly = true)
	@Override
	public BetBlock findBets(Long userId, int startIndex, int count) {
		
		List<Bet> betList = betDao.findBetsByUserId(userId, startIndex, 
			count + 1);
		
		boolean existMoreBets = betList.size() == (count + 1);
		
		if (existMoreBets) {
			betList.remove(betList.size() - 1);
		}

		return new BetBlock(betList, existMoreBets);
	}

	@Transactional(readOnly = true)
	@Override
	public BetType findBetType (Long betTypeId) throws InstanceNotFoundException{
		
		return betTypeDao.find(betTypeId);
	}

	@Transactional(readOnly = true)
	@Override
	public Event findEvent (Long EventId) throws InstanceNotFoundException{
		
		return eventDao.find(EventId);
	}

	@Transactional(readOnly = true)
	@Override
	public BetOption findBetOption (Long betOptionId) throws InstanceNotFoundException{
		
		return betOptionDao.find(betOptionId);
	}
	
	@Override
	public void checkOptions (Long betTypeId, Set<Long> winners) throws InstanceNotFoundException, OnlyOneWonOptionException, NotAllOptionsExistsException{
		
		BetType betType = findBetType(betTypeId);
	
		if (!betType.getMultiple() && winners.size()>1)
			throw new OnlyOneWonOptionException();
		
		else {
			
			Set<Long> betOptions = new HashSet<Long>();
			
			for (BetOption betOption : betType.getBetOptions()){
				betOptions.add(betOption.getBetOptionId());
			}
			
			if (betOptions.containsAll(winners)){
				
				for(Long betOptionId : winners){
					findBetOption(betOptionId).setBetState(true);
				}
				
				Set <Long> notWinners =  new HashSet<Long>();
				notWinners.addAll(betOptions);
				notWinners.removeAll(winners);
				
				for(Long betOptionId : notWinners){
					findBetOption(betOptionId).setBetState(false);
				}
				
				betTypeDao.save(betType);
			}
			else throw new NotAllOptionsExistsException();
			
		}
		
	}

	@Transactional(readOnly = true)
	@Override
	public Category findCategory(Long categoryId) throws InstanceNotFoundException {
		return categoryDao.find(categoryId);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Category> findCategories() {
		return categoryDao.findCategories();
	}

	@Transactional(readOnly = true)
	@Override
	public int findEventsGetNumber(String keyWords, Long categoryId, boolean admin) {
		return eventDao.getNumberOfEvents(keyWords, categoryId, admin);
	}

	@Override
	public boolean findDuplicates(Long eventId, String fullName){
		return betTypeDao.findDuplicates(eventId, fullName);
	}
	
	@Transactional(readOnly = true)
	@Override
	public int findBetsByUserIdNumber(Long userId){
		return betDao.findBetsByUserIdNumber(userId);
	}
}
