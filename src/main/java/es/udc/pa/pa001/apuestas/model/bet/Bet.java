package es.udc.pa.pa001.apuestas.model.bet;

import java.util.Calendar;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.udc.pa.pa001.apuestas.model.betOption.BetOption;
import es.udc.pa.pa001.apuestas.model.event.Event;
import es.udc.pa.pa001.apuestas.model.userprofile.UserProfile;

@Entity
@Immutable
public class Bet {

	private Long betId;
	private Float betedMoney;
	private Calendar date;
	private UserProfile userProfile;
	private Event event;
	private BetOption betOption;
	
	public Bet(){}

	public Bet(Long betId, Float betedMoney, UserProfile userProfile, 
			Event event, BetOption betOption) {
		super();
		this.betId = betId;
		this.betedMoney = betedMoney;
		this.date = Calendar.getInstance();
		this.date.set(Calendar.MILLISECOND, 0);
		this.userProfile = userProfile;
		this.event = event;
		this.betOption = betOption;
	}

	public Bet(Float betedMoney, UserProfile userProfile, Event eventId, 
			BetOption betOption) {
		super();
		this.betedMoney = betedMoney;
		this.date = Calendar.getInstance();
		this.date.set(Calendar.MILLISECOND, 0);
		this.userProfile = userProfile;
		this.event = eventId;
		this.betOption = betOption;
	}
	
	@Column(name="BetId")
    @SequenceGenerator(             // It only takes effect for
         name="BetIdGenerator", // databases providing identifier
         sequenceName="BetSeq") // generators.
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,
                    generator="BetIdGenerator")
	public Long getBetId() {
		return betId;
	}

	public void setBetId(Long betId) {
		this.betId = betId;
	}

	@Column(name="betedMoney")
	public Float getBetedMoney() {
		return betedMoney;
	}

	public void setBetedMoney(Float betedMoney) {
		this.betedMoney = betedMoney;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date")
	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="usrId")
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="eventId")
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="betOptionId")
	public BetOption getBetOption() {
		return betOption;
	}

	public void setBetOption(BetOption betOption) {
		this.betOption = betOption;
	}
	
}
