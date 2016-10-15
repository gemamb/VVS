package es.udc.pa.pa001.apuestas.model.event;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa001.apuestas.model.betType.BetType;
import es.udc.pa.pa001.apuestas.model.category.Category;

@Entity
@BatchSize(size = 10)
public class Event {

	private Long eventId;
	private String name;
	private Calendar eventStart;
	private Category category;
	private List<BetType> betTypes;

	public Event() {
	}

	public Event(Long eventId, String name, Calendar eventStart,
			Category category, List<BetType> betTypes) {
		super();
		this.eventId = eventId;
		this.name = name;
		this.eventStart = eventStart;
		if (this.eventStart != null) {
			this.eventStart.set(Calendar.SECOND, 0);
			this.eventStart.set(Calendar.MILLISECOND, 0);
		}
		this.category = category;
		this.betTypes = betTypes;
	}

	public Event(String name, Calendar eventStart, Category category) {
		super();
		this.name = name;
		this.eventStart = eventStart;
		if (this.eventStart != null) {
			this.eventStart.set(Calendar.SECOND, 0);
			this.eventStart.set(Calendar.MILLISECOND, 0);
		}
		this.category = category;
		this.betTypes = new LinkedList<BetType>();
	}

	@Column(name = "eventId")
	@SequenceGenerator( // It only takes effect for
	name = "EventIdGenerator", // databases providing identifier
	sequenceName = "EventSeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "EventIdGenerator")
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "eventStart")
	public Calendar getEventStart() {
		return eventStart;
	}

	public void setEventStart(Calendar eventStart) {
		this.eventStart = eventStart;
		if (this.eventStart != null) {
			this.eventStart.set(Calendar.SECOND, 0);
			this.eventStart.set(Calendar.MILLISECOND, 0);
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
	public List<BetType> getBetTypes() {
		return betTypes;
	}

	public void setBetTypes(List<BetType> betTypes) {
		this.betTypes = betTypes;
	}
	
	public void addBetType(BetType betType){
		this.betTypes.add(betType);
		betType.setEvent(this);
	}
	
	public boolean finishedEvent(Long eventId){
		return eventStart.getTime().before(Calendar.getInstance().getTime());
	}
}
