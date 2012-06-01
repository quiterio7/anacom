package anacom.domain;

import anacom.shared.exceptions.communication.DurationNotValid;

public class ReceivingVoiceCallState extends ReceivingVoiceCallState_Base {
    
    public  ReceivingVoiceCallState() {
        super();
    }
    
    /**
     * Constructor
     * @param otherParty	the number of the Phone making the call
     * @param startTime		the call's start time
     * @param formerState	the state of the Phone before it received the call
     */
    public ReceivingVoiceCallState(String otherParty, PhoneState formerState) {
    	this.init(otherParty, formerState);
    }
    
    /**
     * Creates the correct communication object.
     * @param duration		the call's duration
     * @param cost			the call's cost
     * @param otherParty	the other party participating in the call
     * @return	the Voice object with the given properties
     * @throws DurationNotValid		if the duration of the call is not a valid one
     */
    protected final Call createCommunication(
    		int duration,
    		int cost,
    		String otherParty) throws DurationNotValid {
    	return new Voice(duration, cost, otherParty);
    }
    
}
