package anacom.domain;

import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.phone.CantChangeState;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

public abstract class OccupiedState extends OccupiedState_Base {
	
    public OccupiedState() {
        super();
    }
    
    /**
     * Constructor
     * A Phone is in the OccupiedState only when in middle of a call.
     * @param otherParty	the number of the other Phone participating in the call
     * @param formerState	the state of the Phone before it became Occupied
     */
    public OccupiedState(String otherParty, PhoneState formerState) {
    	this.init(otherParty, formerState);
    }
    
    /**
     * Initialise the instance of the OccupiedState.
     * @param otherParty	the number of the other Phone participating in the call
     * @param formerState	the state of the Phone before it became Occupied
     */
    protected void init(String otherParty, PhoneState formerState) {
    	this.setOtherParty(otherParty);
    	this.setFormerState(formerState);
    }
    
    /**
	 * Changes the Phone state to On.
	 * @throws CantChangeState if this state doesn't allow the Phone to be
	 * turned on
	 */
    @Override
    void turnOn() throws CantChangeState {
    	throw new CantChangeState(
    			this.getPhone().getNumber(), 
    			this.getStateType(), 
				PhoneStateRepresentation.getInstance().getOnState());
    }
    
    /**
	 * Changes the Phone state to Off.
	 * @throws CantChangeState if this state doesn't allow the Phone to be
	 * turned off
	 */
    @Override 
    void turnOff() throws CantChangeState {
    	throw new CantChangeState(
    			this.getPhone().getNumber(), 
    			this.getStateType(), 
				PhoneStateRepresentation.getInstance().getOffState());
    }
    
    /**
	 * Changes the Phone state to Silent.
	 * @throws CantChangeState if this state doesn't allow the Phone to be
	 * silenced
	 */
    @Override
    void silence() throws CantChangeState {
    	throw new CantChangeState(
    			this.getPhone().getNumber(), 
    			this.getStateType(), 
				PhoneStateRepresentation.getInstance().getSilentState());
    }

    /**
     * Sends a SMS from this state's Phone.
     * @param destination the destination's Phone number
     * @param message the SMS' content
     * @return SMS the communication object
     * @throws InvalidStateSendSMS if a Phone can't send a SMS in this state
     */
	@Override
	final SMS sendSMS(String destination, String message)
    		throws InvalidStateSendSMS {
		throw new InvalidStateSendSMS(
				this.getPhone().getNumber(),
				this.getStateType());
	}
	
	/**
     * Makes a Voice call from this state's Phone.
     * @param otherParty the number of the Phone receiving the Voice call
     * @param startTime	the time at which the Voice call was established in
     * seconds
     * @throws InvalidStateMakeVoice if a Phone can't make a Voice call in this
     * state
     */
	@Override
	final void makeVoiceCall(String otherParty, long startTime)
			throws InvalidStateMakeVoice {
		throw new InvalidStateMakeVoice(
				this.getPhone().getNumber(),
				this.getStateType());
	}
	
	/**
     * Makes a Video call from this state's Phone.
     * @param otherParty the number of the Phone receiving the Video call
     * @param startTime	the time at which the Video call was established in
     * seconds
     * @throws InvalidStateMakeVideo if a Phone can't make a Video call in this
     * state
     */
	@Override
	final void makeVideoCall(String otherParty, long startTime)
			throws InvalidStateMakeVideo {
		throw new InvalidStateMakeVideo(
				this.getPhone().getNumber(),
				this.getStateType());
	}
	
	/**
     * Receives a Voice call on this state's Phone.
     * @param otherParty the number of the Phone making the Voice call
     * @throws InvalidStateReceiveVoice	if a Phone can't receive a Voice call
     * in this state
     */
	@Override
	final void receiveVoiceCall(String otherParty)
			throws InvalidStateReceiveVoice {
		throw new InvalidStateReceiveVoice(
				this.getPhone().getNumber(),
				this.getStateType());
	}
	
	/**
     * Receives a Video call on this state's Phone.
     * @param otherParty the number of the Phone making the Video call
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVideo	if a Phone can't receive a Video call
     * in this state
     */
	@Override
	final void receiveVideoCall(String otherParty)
			throws InvalidStateReceiveVideo {
		throw new InvalidStateReceiveVideo(
				this.getPhone().getNumber(),
				this.getStateType());
	}
	
	/**
     * Creates the correct communication object.
     * @param duration the call's duration
     * @param cost the call's cost
     * @param otherParty the other party participating in the call
     * @throws DurationNotValid	if the call's duration isn't valid
     * @return the Call object with the given properties
     */
    protected abstract Call createCommunication(
    		int duration,
    		int cost,
    		String otherParty)
    	throws DurationNotValid;	
	
	/**
	 * @return	"Occupied" string
	 */
    @Override
    public String getStateType() { 
    	return PhoneStateRepresentation.getInstance().getOccupiedState();
    }
    
}
