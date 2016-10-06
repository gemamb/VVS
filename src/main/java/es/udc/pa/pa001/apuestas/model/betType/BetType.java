package es.udc.pa.pa001.apuestas.model.betType;

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

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.event.Event;

@Entity
@BatchSize(size = 10)
public class BetType {
	
	private Long betTypeId;
	private String question;
	private Boolean multiple;
	private Event event=null;
	private List<BetOption> betOptions;

	public BetType(){
		this.betOptions = new LinkedList<BetOption>();
	}

	public BetType(Long betTypeId, String question, Boolean multiple) {
		super();
		this.betTypeId = betTypeId;
		this.question = question;
		this.multiple = multiple;
		this.betOptions = new LinkedList<BetOption>();
	}

	public BetType(String question, Boolean multiple) {
		super();
		this.question = question;
		this.multiple = multiple;
		this.betOptions = new LinkedList<BetOption>();
	}

	@Column(name="BetTypeId")
    @SequenceGenerator(             // It only takes effect for
         name="BetTypeIdGenerator", // databases providing identifier
         sequenceName="BetTypeSeq") // generators.
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,
                    generator="BetTypeIdGenerator")
	public Long getBetTypeId() {
		return betTypeId;
	}

	public void setBetTypeId(Long betTypeId) {
		this.betTypeId = betTypeId;
	}

	@Column(name="question")
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Column(name="multiple")
	public Boolean getMultiple() {
		return multiple;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="eventId")
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "betType")
	public List<BetOption> getBetOptions() {
		return betOptions;
	}

	public void setBetOptions(List<BetOption> betOptions) {
		this.betOptions = betOptions;
	}
	
	public void addBetOption(BetOption betOption){
		this.betOptions.add(betOption);
		betOption.setBetType(this);
	}
	
}
