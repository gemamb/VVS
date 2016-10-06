package es.udc.pa.pa001.apuestas.model.betOption;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa001.apuestas.model.betType.BetType;

@Entity
@BatchSize(size = 10)
public class BetOption {
	
	private Long betOptionId;
	private String answer;
	private Float rate;
	private Boolean betState;
	private BetType betType;
	
	public BetOption(){}
	
	public BetOption(Long betOptionId, String answer, Float rate,
			Boolean betState, BetType betType) {
		super();
		this.betOptionId = betOptionId;
		this.answer = answer;
		this.rate = rate;
		this.betState = null;
		this.betType = betType;
	}
	
	public BetOption(String answer, Float rate, Boolean betState,
			BetType betType) {
		super();
		this.answer = answer;
		this.rate = rate;
		this.betState = betState;
		this.betType = betType;
	}

	@Column(name="BetOptionId")
    @SequenceGenerator(             // It only takes effect for
         name="BetOptionIdGenerator", // databases providing identifier
         sequenceName="BetOptionSeq") // generators.
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,
                    generator="BetOptionIdGenerator")
	public Long getBetOptionId() {
		return betOptionId;
	}

	public void setBetOptionId(Long betOptionId) {
		this.betOptionId = betOptionId;
	}

	@Column(name="answer")
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Column(name="rate")
	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	@Column(name="betState")
    public Boolean getBetState() {
		return betState;
	}

	public void setBetState(Boolean betState) {
		this.betState = betState;
	}

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="betTypeId")
	public BetType getBetType() {
		return betType;
	}

	public void setBetType(BetType betType) {
		this.betType = betType;
	}
	
}
