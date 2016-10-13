package es.udc.pa.pa001.apuestas.test.model.betservice;

import static es.udc.pa.pa001.apuestas.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa001.apuestas.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.event.EventBlock;
import es.udc.pa.pa001.apuestas.model.event.EventDao;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.category.CategoryDao;
import es.udc.pa.pa001.apuestas.model.bet.Bet;
import es.udc.pa.pa001.apuestas.model.bet.BetBlock;
import es.udc.pa.pa001.apuestas.model.bet.BetDao;
import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.betOption.BetOptionDao;
import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.betType.BetTypeDao;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.betservice.util.AlreadyPastedDateException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetOptionAnswerException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateBetTypeQuestionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateEventNameException;
import es.udc.pa.pa001.apuestas.model.betservice.util.MinimunBetOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.NotAllOptionsExistsException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OnlyOneWonOptionException;
import es.udc.pa.pa001.apuestas.model.betservice.util.OutdatedBetException;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {SPRING_CONFIG_FILE,SPRING_CONFIG_TEST_FILE})
@Transactional
public class BetServiceTest {

    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private BetService betService;
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
    
    private static final String CATEGORY_NAME = "Baloncesto";
    private static final String CATEGORY_NAME2 = "Tenis";
    private static final String EVENT_NAME = "Barcelona - Real Madrid";
    private static final String EVENT_NAME2 = "Deportivo de la Coruña - Real Madrid";
    private static final String EVENT_NAME3 = "Roger Federer - Rafael Nadal";
    private static final String EVENT_NAME4 = "Roger Federer - Rafael Nadal dia 2";
    private static final String USERPROFILE_LOGINNAME = "pepe6";
    private static final String USERPROFILE_ENCRYPTEDPASSWORD = "XxXyYyZzZ";
    private static final String USERPROFILE_FIRSTNAME = "Pepe";
    private static final String USERPROFILE_LASTNAME = "Garcia Garcia";
    private static final String USERPROFILE_EMAIL = "pepe6@gmail.com";
    private static final String BETTYPE_QUESTION = "¿Qué equipo ganará el encuentro?";
    private static final String BETTYPE_QUESTION2 = "¿Quién marcará más goles?";
    private static final String BETOPTION_ANSWER1 = "FC Barcelona";
    private static final String BETOPTION_ANSWER2 = "Real Madrid CF";
    private static final String BETOPTION_ANSWER3 = "Racing de Ferrol";
    private static final Float BETOPTION_RATE = (float) 1.75;
    private static final Float BET_BETEDMONEY = (float) 10;
    
    @Test
    public void testInsertEvent() throws InstanceNotFoundException,  AlreadyPastedDateException, DuplicateEventNameException {

        /* Register event and find. */
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(2016, Calendar.AUGUST, 31);  
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	Event event1 = betService.insertEvent(new Event(EVENT_NAME,calendar,category),category.getCategoryId());
        Event event2 = eventDao.find(event1.getEventId());

        /* Check data. */
    	assertEquals(event1,event2);

    }
    
    @Test (expected = AlreadyPastedDateException.class)
    public void testInsertPastDateEvent() throws AlreadyPastedDateException, 
    InstanceNotFoundException, DuplicateEventNameException {

        /* Register event and find. */
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.HOUR,-1);
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	Event event1 = betService.insertEvent(new Event(EVENT_NAME,calendar,category),category.getCategoryId());
        Event event2 = eventDao.find(event1.getEventId());

        /* Check data. */
        assertEquals(event1,event2);
    }
    
    @Test
    public void testFindEventsAdmin() throws AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException {

    	/* Register event and find. */
    	Calendar eventCalendar1 = Calendar.getInstance();
    	Calendar eventCalendar2 = Calendar.getInstance();
    	eventCalendar1.set(2016, Calendar.JANUARY, 31);
    	eventCalendar2.set(2016, Calendar.AUGUST, 31);  
    		
    	Category category1 = new Category(CATEGORY_NAME);
    	categoryDao.save(category1);
    	Category category2 = new Category(CATEGORY_NAME2);
    	categoryDao.save(category2);
    	
    	Event event1 = new Event(EVENT_NAME,eventCalendar1,category1);
    	Event event2 = new Event(EVENT_NAME2,eventCalendar1,category1);
    	Event event3 = new Event(EVENT_NAME3,eventCalendar1,category2);
    	eventDao.save(event1);
    	eventDao.save(event2);
    	eventDao.save(event3);
    	
    	Event event4 = betService.insertEvent(new Event(EVENT_NAME4,eventCalendar2,category2),category2.getCategoryId());
    	
    	/* Check data without parameters. */
    	EventBlock events = betService.findEvents(null, null, 0, 10,true);
    	List<Event> listEvents = new ArrayList<>();
    	listEvents.add(event1);
    	listEvents.add(event2);
    	listEvents.add(event3);
    	listEvents.add(event4);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with only category. */
    	events = betService.findEvents(null, category1.getCategoryId(), 0, 10,true);
    	listEvents.add(event1);
    	listEvents.add(event2);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with only keywords. */
    	events = betService.findEvents("Madrid", null, 0, 10,true);
    	listEvents.add(event1);
    	listEvents.add(event2);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with keywords and category. */
    	events = betService.findEvents("Barcelona", category1.getCategoryId(), 0, 10,true);
    	listEvents.add(event1);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with minus keywords and category. */
    	events = betService.findEvents("madrid", category1.getCategoryId(), 0, 10,true);
    	listEvents.add(event1);
    	listEvents.add(event2);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with mayus keywords and category. */
    	events = betService.findEvents("BARCELONA", category1.getCategoryId(), 0, 10,true);
    	listEvents.add(event1);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with wrong keywords and category. */
    	events = betService.findEvents("Racing madrid ferrol", category1.getCategoryId(), 0, 10,true);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	events = betService.findEvents("madrid Racing ferrol", category1.getCategoryId(), 0, 10,true);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	events = betService.findEvents("ferrol", category1.getCategoryId(), 0, 0,true);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	
    	/* Check data with the order of the keywords and category. */
    	events = betService.findEvents("Federer Nadal", category2.getCategoryId(), 0, 10,true);
    	listEvents.add(event3);
    	listEvents.add(event4);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with other number of output events. */
    	events = betService.findEvents("nadal federer", category2.getCategoryId(), 0, 1,true);
    	listEvents.add(event3);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),true);
    	listEvents.removeAll(listEvents);
    }
   
    
    @Test
    public void testInsertBetType() throws
    	AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException, DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException{
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.MINUTE,1);
    	Event event = new Event(EVENT_NAME,c,category);
    	betService.insertEvent(event,event.getCategory().getCategoryId());
    	BetType betType1 = new BetType(BETTYPE_QUESTION,false);
    	event.addBetType(betType1);
    	BetOption betOption = new BetOption(BETOPTION_ANSWER1,BETOPTION_RATE,null,betType1);
    	BetOption betOption2 = new BetOption(BETOPTION_ANSWER2,BETOPTION_RATE,null,betType1);
    	List<BetOption> betOptions = new LinkedList<BetOption>();
    	betOptions.add(betOption);
    	betOptions.add(betOption2);
    	betType1.setBetOptions(betOptions);
    	BetType betType2 = betService.insertBetType(betType1);
    	
    	assertEquals(betType1,betType2);
    }
    
    @Test (expected = DuplicateBetOptionAnswerException.class)
    public void testInsertBetTypeDuplicateAnswer() throws
    	AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException, DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException{
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.MINUTE,1);
    	Event event = new Event(EVENT_NAME,c,category);
    	betService.insertEvent(event,event.getCategory().getCategoryId());
    	BetType betType1 = new BetType(BETTYPE_QUESTION,false);
    	event.addBetType(betType1);
    	BetOption betOption = new BetOption(BETOPTION_ANSWER1,BETOPTION_RATE,null,betType1);
    	BetOption betOption2 = new BetOption(BETOPTION_ANSWER1,BETOPTION_RATE,null,betType1);
    	List<BetOption> betOptions = new LinkedList<BetOption>();
    	betOptions.add(betOption);
    	betOptions.add(betOption2);
    	betType1.setBetOptions(betOptions);
    	BetType betType2 = betService.insertBetType(betType1);
    	
    	assertEquals(betType1,betType2);
    }
    
    @Test (expected = MinimunBetOptionException.class)
    public void testInsertBetTypeMinimunAnswerSize() throws
    	AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException, DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException{
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.MINUTE,1);
    	Event event = new Event(EVENT_NAME,c,category);
    	betService.insertEvent(event,event.getCategory().getCategoryId());
    	BetType betType1 = new BetType(BETTYPE_QUESTION,false);
    	event.addBetType(betType1);
    	BetOption betOption = new BetOption(BETOPTION_ANSWER1,BETOPTION_RATE,null,betType1);
    	List<BetOption> betOptions = new LinkedList<BetOption>();
    	betOptions.add(betOption);
    	betType1.setBetOptions(betOptions);
    	BetType betType2 = betService.insertBetType(betType1);
    	
    	assertEquals(betType1,betType2);
    }
    
    
    @Test
    public void testOutDatedMakeBe() throws InstanceNotFoundException {
    	
    	UserProfile userProfile = new UserProfile(
        		USERPROFILE_LOGINNAME,
        		USERPROFILE_ENCRYPTEDPASSWORD,
        		USERPROFILE_FIRSTNAME,
        		USERPROFILE_LASTNAME,
        		USERPROFILE_EMAIL
        		);
        
    	userProfileDao.save(userProfile);

    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	BetType betType = new BetType(BETTYPE_QUESTION,false);
    	event.addBetType(betType);
    	betTypeDao.save(betType);
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
        betOptionDao.save(betOption);
    
        boolean exceptionCaptured = false;
	    try {   	
	    	
	    	betOption.setBetState(false);
	    	
	    	betService.makeBet(
	    		userProfile.getUserProfileId(), 
	    		betOption.getBetOptionId(), 
	    		BET_BETEDMONEY);
	    	
	    } catch (OutdatedBetException e){
	    	exceptionCaptured = true;
	    }
	    assertTrue(exceptionCaptured);
	    exceptionCaptured = false;
	    
	    try {
	    	
	    	betOption.setBetState(false);
	    	
	    	betService.makeBet(
	    		userProfile.getUserProfileId(), 
	    		betOption.getBetOptionId(), 
	    		BET_BETEDMONEY);
	    	
	    } catch (OutdatedBetException e){
	    	exceptionCaptured = true;
	    }
	    
	    assertTrue(exceptionCaptured);
	    exceptionCaptured = false;
	    
	    try {
	    	betOption.setBetState(true);
	    	
	    	betService.makeBet(
	    		userProfile.getUserProfileId(), 
	    		betOption.getBetOptionId(), 
	    		BET_BETEDMONEY);
	    	
	    } catch (OutdatedBetException e){
	    	exceptionCaptured = true;
	    }
	    
	    assertTrue(exceptionCaptured);
    
    }
    
    @Test
    public void testIlegalIdMakeBe() throws OutdatedBetException {
    	
    	UserProfile userProfile = new UserProfile(
        		USERPROFILE_LOGINNAME,
        		USERPROFILE_ENCRYPTEDPASSWORD,
        		USERPROFILE_FIRSTNAME,
        		USERPROFILE_LASTNAME,
        		USERPROFILE_EMAIL
        		);
        
    	userProfileDao.save(userProfile);

    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	BetType betType = new BetType(BETTYPE_QUESTION,false);
    	event.addBetType(betType);
    	betTypeDao.save(betType);
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
        betOptionDao.save(betOption);
        
        boolean exceptionCaptured = false;
        Long irrealId = (long) -1;
        
        try {
        	betService.makeBet(
        		irrealId, 
        		betOption.getBetOptionId(), 
        		BET_BETEDMONEY);
        } catch (InstanceNotFoundException e){
        	exceptionCaptured = true;
        }
        
        assertTrue(exceptionCaptured);
        exceptionCaptured = false;
        
        try {
        	betService.makeBet(
        		userProfile.getUserProfileId(), 
        		irrealId, 
        		BET_BETEDMONEY);
        } catch (InstanceNotFoundException e){
        	exceptionCaptured = true;
        }
        
        assertTrue(exceptionCaptured);
        exceptionCaptured = false; 
    	
    }

    @Test
    public void testMakeBet() throws InstanceNotFoundException, OutdatedBetException {
    	
    	UserProfile userProfile = new UserProfile(
        		USERPROFILE_LOGINNAME,
        		USERPROFILE_ENCRYPTEDPASSWORD,
        		USERPROFILE_FIRSTNAME,
        		USERPROFILE_LASTNAME,
        		USERPROFILE_EMAIL
        		);
        
    	userProfileDao.save(userProfile);

    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	BetType betType = new BetType(BETTYPE_QUESTION,false);
    	event.addBetType(betType);
    	betTypeDao.save(betType);
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
        betOptionDao.save(betOption);
        
    	Bet bet = betService.makeBet(
    			userProfile.getUserProfileId(), 
    			betOption.getBetOptionId(), 
    			BET_BETEDMONEY);
    	
    	Bet bet2 = betDao.find(bet.getBetId());
        
    	assertEquals(bet,bet2);
    }
    
    
    @Test
    public void testFindBets() {
    	
    	/* This user will make 3 bets */
    	UserProfile user1 = new UserProfile(
        		"user1",
        		USERPROFILE_ENCRYPTEDPASSWORD,
        		USERPROFILE_FIRSTNAME,
        		USERPROFILE_LASTNAME,
        		USERPROFILE_EMAIL
        		);
    	
    	/* This user will make 2 bets */
    	UserProfile user2 = new UserProfile(
        		"user2",
        		USERPROFILE_ENCRYPTEDPASSWORD,
        		USERPROFILE_FIRSTNAME,
        		USERPROFILE_LASTNAME,
        		USERPROFILE_EMAIL
        		);
    	
    	/* This user wont make bets */
    	UserProfile user3 = new UserProfile(
        		"user3",
        		USERPROFILE_ENCRYPTEDPASSWORD,
        		USERPROFILE_FIRSTNAME,
        		USERPROFILE_LASTNAME,
        		USERPROFILE_EMAIL
        		);
        
    	userProfileDao.save(user1);
    	userProfileDao.save(user2);
    	userProfileDao.save(user3);
    	
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	BetType betType1 = new BetType(BETTYPE_QUESTION,false);
    	event.addBetType(betType1);
    	betTypeDao.save(betType1);
    	
    	BetType betType2 = new BetType(
    			"¿Qué jugador marcará el primer gol?",false);
    	event.addBetType(betType2);
    	betTypeDao.save(betType2);
    	
    	BetOption betOption1 = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType1);
        betOptionDao.save(betOption1);
        	
    	BetOption betOption2 = new BetOption(
        		BETOPTION_ANSWER2,BETOPTION_RATE,null,betType1);
        betOptionDao.save(betOption2);
        	
    	BetOption betOption3 = new BetOption(
        		"X",BETOPTION_RATE,null,betType1);
        betOptionDao.save(betOption3);
        	
    	BetOption betOption4 = new BetOption(
        		"Messi",BETOPTION_RATE,null,betType2);
        betOptionDao.save(betOption4);
        	
    	BetOption betOption5 = new BetOption(
        		"Cr7",BETOPTION_RATE,null,betType2);
        betOptionDao.save(betOption5);
        
        List<Bet> betListUser1 = new ArrayList<>();
        List<Bet> betListUser2 = new ArrayList<>();
        List<Bet> betListUser3 = new ArrayList<>();
        
        /****** user1 bets ******/ 
    	Bet bet1 = new Bet(BET_BETEDMONEY,user1,event,betOption1);
    	Bet bet2 = new Bet(BET_BETEDMONEY,user1,event,betOption3);
    	Bet bet3 = new Bet(BET_BETEDMONEY,user1,event,betOption5);
    	
    	betDao.save(bet1);
    	betDao.save(bet2);
    	betDao.save(bet3);
    	
    	betListUser1.add(bet1);
    	betListUser1.add(bet2);
    	betListUser1.add(bet3);
    	
    	/****** user2 bets ******/ 
    	Bet bet4 = new Bet(BET_BETEDMONEY,user2,event,betOption1);
    	Bet bet5 = new Bet(BET_BETEDMONEY,user2,event,betOption4);
    	
    	betDao.save(bet4);
    	betDao.save(bet5);

    	betListUser2.add(bet4);
    	betListUser2.add(bet5);
    	
    	BetBlock betResultListUser1 = betService.findBets(user1.getUserProfileId(), 0, 10);
    	BetBlock betResultListUser2 = betService.findBets(user2.getUserProfileId(), 0, 10);
    	BetBlock betResultListUser3 = betService.findBets(user3.getUserProfileId(), 0, 10);
    	
        assertEquals(betListUser1,betResultListUser1.getBets());
        assertEquals(betListUser2,betResultListUser2.getBets());
        assertEquals(betListUser3.isEmpty(),betResultListUser3.getBets().isEmpty());
        assertEquals(false,betResultListUser1.getExistMoreBets());
    	
    }
    
    @Test
    public void testFindEventsUser() throws  AlreadyPastedDateException, InstanceNotFoundException, DuplicateEventNameException{

    	/* Register event and find. */
    	Calendar eventCalendar1 = Calendar.getInstance();
    	Calendar eventCalendar2 = Calendar.getInstance();
    	eventCalendar1.set(2016, Calendar.AUGUST, 31);
    	eventCalendar2.set(2016, Calendar.JANUARY, 31);  
    	
    	Category category1 = new Category(CATEGORY_NAME);
    	categoryDao.save(category1);
    	Category category2 = new Category(CATEGORY_NAME2);
    	categoryDao.save(category2);
    	
    	Event event1 = new Event(EVENT_NAME,eventCalendar1,category1);
    	Event event2 = new Event(EVENT_NAME2,eventCalendar1,category1);
    	Event event3 = new Event(EVENT_NAME3,eventCalendar1,category2);
    	Event event4 = new Event(EVENT_NAME4,eventCalendar2,category2);
    	
    	eventDao.save(event1);
    	eventDao.save(event2);
    	eventDao.save(event3);
    	eventDao.save(event4);
    	
    	/* Check data without parameters. */
    	EventBlock events = betService.findEvents(null, null, 0, 10,false);
    	List<Event> listEvents = new ArrayList<>();
    	listEvents.add(event1);
    	listEvents.add(event2);
    	listEvents.add(event3);
    	// assertEquals(listEvents,events.getEvents());
    	// assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with only category. */
    	events = betService.findEvents(null, category1.getCategoryId(), 0, 10,false);
    	listEvents.add(event1);
    	listEvents.add(event2);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);

    	/* Check data with only keywords. */
    	events = betService.findEvents("Madrid", null, 0, 10,false);
    	listEvents.add(event1);
    	listEvents.add(event2);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with keywords and category. */
    	events = betService.findEvents("Barcelona", category1.getCategoryId(), 0, 10,false);
    	listEvents.add(event1);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with other number of output events. */
    	events = betService.findEvents("madrid", category1.getCategoryId(), 0, 1,false);
    	listEvents.add(event1);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),true);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with mayus keywords and category. */
    	events = betService.findEvents("BARCELONA", category1.getCategoryId(), 0, 10,false);
    	listEvents.add(event1);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with wrong keywords and category. */
    	events = betService.findEvents("Racing madrid", category1.getCategoryId(), 0, 10,false);
    	assertEquals(listEvents,events.getEvents());
    	events = betService.findEvents("madrid Racing", category1.getCategoryId(), 0, 10,false);
    	assertEquals(listEvents,events.getEvents());
    	events = betService.findEvents("ferrol", category1.getCategoryId(), 0, 10,true);
    	assertEquals(listEvents,events.getEvents());
    	
    	/* Check data with the order of the keywords and category. */
    	events = betService.findEvents("Federer Nadal", category2.getCategoryId(), 0, 10,false);
    	listEvents.add(event3);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    	
    	/* Check data with different order of the keywords and category. */
    	events = betService.findEvents("nadal federer", category2.getCategoryId(), 0, 10,false);
    	listEvents.add(event3);
    	assertEquals(listEvents,events.getEvents());
    	assertEquals(events.getExistMoreEvents(),false);
    	listEvents.removeAll(listEvents);
    }
    
    @Test
    public void testCheckMultipleOptions() throws  InstanceNotFoundException, OnlyOneWonOptionException, DuplicateBetTypeQuestionException, NotAllOptionsExistsException, DuplicateBetOptionAnswerException, MinimunBetOptionException{
    	
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	BetType betType = new BetType(BETTYPE_QUESTION,true);
    	event.addBetType(betType);
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption);
        
        BetOption betOption2 = new BetOption(
        		BETOPTION_ANSWER2,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption2);
        
        BetOption betOption3 = new BetOption(
        		BETOPTION_ANSWER3,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption3);
        
        betService.insertBetType(betType);
        
        Set<Long> winners = new HashSet<Long>();
        winners.add(betOption.getBetOptionId());
        winners.add(betOption2.getBetOptionId());
        winners.add(betOption3.getBetOptionId());
        
    	betService.checkOptions(betType.getBetTypeId(), winners);
    	
    	assertTrue(betOption.getBetState());
    	assertTrue(betOption2.getBetState());
    	assertTrue(betOption3.getBetState());
    }
    
    @Test (expected = NotAllOptionsExistsException.class)
    public void testCheckInexistentsOptions() throws  InstanceNotFoundException, OnlyOneWonOptionException, NotAllOptionsExistsException, DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException{
    	
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	BetType betType = new BetType(BETTYPE_QUESTION,true);
    	event.addBetType(betType);
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption);
        
        BetOption betOption2 = new BetOption(
        		BETOPTION_ANSWER2,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption2);
        
        BetOption betOption3 = new BetOption(
        		BETOPTION_ANSWER3,BETOPTION_RATE,null,betType);
    	
        betService.insertBetType(betType);
        
        Set<Long> winners = new HashSet<Long>();
        winners.add(betOption.getBetOptionId());
        winners.add(betOption2.getBetOptionId());
        winners.add(betOption3.getBetOptionId());
        
    	betService.checkOptions(betType.getBetTypeId(), winners);
    }
    
    @Test (expected = OnlyOneWonOptionException.class)
    public void testCheckOnlyOneOption() throws  InstanceNotFoundException, OnlyOneWonOptionException, DuplicateBetTypeQuestionException, NotAllOptionsExistsException, DuplicateBetOptionAnswerException, MinimunBetOptionException{
    	
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
        BetType betType = new BetType(BETTYPE_QUESTION,false);
        event.addBetType(betType);
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption);
        
        BetOption betOption2 = new BetOption(
        		BETOPTION_ANSWER2,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption2);
        
        betService.insertBetType(betType);
        
        Set<Long> winners = new HashSet<Long>();
        winners.add(betOption.getBetOptionId());
        winners.add(betOption2.getBetOptionId());
        
    	betService.checkOptions(betType.getBetTypeId(), winners);
    }
    
    
    @Test (expected = DuplicateBetTypeQuestionException.class)
    public void DuplicateBetTypeQuestionTest() throws DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException {
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
        BetType betType = new BetType(BETTYPE_QUESTION,false);
        event.addBetType(betType);
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption);
        
        BetOption betOption2 = new BetOption(
        		BETOPTION_ANSWER2,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption2);
        
        
        betService.insertBetType(betType);
        
        BetType betType2 = new BetType(BETTYPE_QUESTION,false);
        event.addBetType(betType2);
        
        BetOption betOption3 = new BetOption(BETOPTION_ANSWER1,BETOPTION_RATE,null,betType2);
    	BetOption betOption4 = new BetOption(BETOPTION_ANSWER2,BETOPTION_RATE,null,betType2);
    	List<BetOption> betOptions = new LinkedList<BetOption>();
    	betOptions.add(betOption3);
    	betOptions.add(betOption4);
    	betType2.setBetOptions(betOptions);
        betService.insertBetType(betType2);
    }
    
    @Test
    public void DuplicateBetTypeQuestionDifferentEventTest() throws DuplicateBetTypeQuestionException, InstanceNotFoundException, DuplicateBetOptionAnswerException, MinimunBetOptionException {
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	Event event2 = new Event(EVENT_NAME2,Calendar.getInstance(),category);
    	eventDao.save(event2);
    	
        BetType betType = new BetType(BETTYPE_QUESTION,false);
        event.addBetType(betType);
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption);
        
        BetOption betOption2 = new BetOption(
        		BETOPTION_ANSWER2,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption2);
        
        
        betService.insertBetType(betType);
        
        BetType betType2 = new BetType(BETTYPE_QUESTION,false);
        event2.addBetType(betType2);
        
        
        BetOption betOption3 = new BetOption(BETOPTION_ANSWER1,BETOPTION_RATE,null,betType2);
    	BetOption betOption4 = new BetOption(BETOPTION_ANSWER2,BETOPTION_RATE,null,betType2);
    	List<BetOption> betOptions = new LinkedList<BetOption>();
    	betOptions.add(betOption3);
    	betOptions.add(betOption4);
    	betType2.setBetOptions(betOptions);
        
        betService.insertBetType(betType2);
        
        assertEquals(betType2,betService.findBetType(betType2.getBetTypeId()));
    }
    
    @Test
    public void navigability() throws DuplicateBetTypeQuestionException, DuplicateBetOptionAnswerException, MinimunBetOptionException, InstanceNotFoundException{
    	
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	
    	BetType betType = new BetType(BETTYPE_QUESTION,false);
    	event.addBetType(betType);
    	
    	
    	BetOption betOption = new BetOption(
        		BETOPTION_ANSWER1,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption);
        
        BetOption betOption2 = new BetOption(
        		BETOPTION_ANSWER2,BETOPTION_RATE,null,betType);
    	
        betType.getBetOptions().add(betOption2);
        
        
        betService.insertBetType(betType);
        
        BetType betType2 = new BetType(BETTYPE_QUESTION2,false);
        event.addBetType(betType2);
        BetOption betOption3 = new BetOption(BETOPTION_ANSWER1,BETOPTION_RATE,null,betType2);
    	BetOption betOption4 = new BetOption(BETOPTION_ANSWER2,BETOPTION_RATE,null,betType2);
    	List<BetOption> betOptions = new LinkedList<BetOption>();
    	betOptions.add(betOption3);
    	betOptions.add(betOption4);
    	betType2.setBetOptions(betOptions);
        
        betService.insertBetType(betType2);
        
        
        Event event2 = betService.findEvent(event.getEventId());
        
        List<BetType> testl = event2.getBetTypes();
        assertEquals(event2.getBetTypes().size(),2);
    }
    
    @Test
    public void lenght(){
    	Category category = new Category(CATEGORY_NAME);
    	categoryDao.save(category);
    	
    	
    	int size = betService.findEventsGetNumber(EVENT_NAME,null,true);
    	assertEquals(size,0);
    	
    	Event event = new Event(EVENT_NAME,Calendar.getInstance(),category);
    	eventDao.save(event);
    	
    	size = betService.findEventsGetNumber(EVENT_NAME,null,true);
    	assertEquals(size,1);
    	
    }
}
