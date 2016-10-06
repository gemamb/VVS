/**
 * 
 */
package es.udc.pa.pa001.apuestas.web.pages.management;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import es.udc.pa.pa001.apuestas.model.betservice.BetService;
import es.udc.pa.pa001.apuestas.model.betservice.util.AlreadyPastedDateException;
import es.udc.pa.pa001.apuestas.model.betservice.util.DuplicateEventNameException;
import es.udc.pa.pa001.apuestas.model.category.Category;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicy;
import es.udc.pa.pa001.apuestas.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_ADMIN)
public class InsertEvent {
	
	private Event event;
	
	private Long categoryId;
	
	@Property
    private String dateInFormatStr = "dd/MM/yyyy";
	
	@Property
    private String hoursInFormatStr = "HH:MM";
	
	@Property
	private String name;
	 
	@Property
	private String category;
	
	@Property
	private Date eventStart;
	
	@Property
	private String timeStart;
	
	@Component(id = "timeStart")
	private TextField timeStartDateField;

	@Component(id = "eventStart")
	private DateField eventStartDateField;
	
	@Component(id = "name")
	private TextField nameTextField;
	
	@Component
	private Form insertEventForm;
	
	@Inject
	private BetService betService;
	
	@InjectPage
	private InsertedEvent insertedEvent;
	
	@Inject
	private Messages messages;
	
	@Inject
	private Locale locale;
	
	public DateFormat getFormat(){
		return DateFormat.getDateInstance(DateFormat.SHORT,locale);
	}

	private static String lastletter(String str) {
	    if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='x') {
	      str = str.substring(0, str.length()-1);
	    }
	    return str;
	}
	
	public String getCategories(){
		List<Category> categories = betService.findCategories();
		String model = "";
		for (Category c : categories){
			model = model+c.getCategoryId().toString()+"="+c.getName()+",";
		}
		return lastletter(model);
	}
	
	public boolean validateTime(String timeStart){
		
		if (timeStart.contains(":")){
			String[] hours = timeStart.split(":");
			String h1 = hours[0];
			String h2 = hours[1];
			if ((Integer.valueOf(h1)>=00) && (Integer.valueOf(h1)<=23))
				if ((Integer.valueOf(h2)>=00) && (Integer.valueOf(h2)<=59)){
					return true;
				}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@OnEvent(value="validate", component="insertEventForm")
	void onValidateFromInsertEventForm() throws AlreadyPastedDateException{
		
		if (!insertEventForm.isValid()) {
			return;
		}
	
		if (!validateTime(timeStart)){
			insertEventForm.recordError(timeStartDateField, messages.format(
					"error-notValidateNumber"));
			return;
		}
		String[] hours = timeStart.split(":");
		String h1 = hours[0];
		String h2 = hours[1];
		
		Calendar start = Calendar.getInstance();
		eventStart.setHours(Integer.valueOf(h1));
		eventStart.setMinutes(Integer.valueOf(h2));
		DateFormat format = new SimpleDateFormat("yyyy/mm/dd hh:mm");
		format.format(eventStart);
		start = format.getCalendar();
	
		
		if (start.before(Calendar.getInstance())){
			insertEventForm.recordError(eventStartDateField, messages.format(
					"error-alreadyPastedDate"));
			insertEventForm.recordError(timeStartDateField, messages.format(
					"error-alreadyPastedDate"));
			return;
		}
		
		if (category!=null){
			categoryId = Long.parseLong(category);
		}
		
		try {
			event = new Event(name,start,betService.findCategory(categoryId));
			betService.insertEvent(event,categoryId);
			insertedEvent.setEventId(event.getEventId());
		} catch (InstanceNotFoundException e) {
			insertEventForm.recordError(nameTextField, messages.format(
					"error-instanceNotFound"));
		} catch (DuplicateEventNameException e) {
			insertEventForm.recordError(nameTextField, messages.format(
					"error-duplicateEventName",name));
		}
		
	}

	Object onSuccess() {
		return insertedEvent;
	}

}