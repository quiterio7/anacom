package anacom.domain;

import anacom.shared.exceptions.communication.DurationNotValid;

public class ReceivingVideoCallState extends ReceivingVideoCallState_Base {
    
    public  ReceivingVideoCallState() {
        super();
    }
    
    /**
     * Constructor
     * @param otherParty	the number of the Phone making the call
     * @param formerState	the state of the Phone before it received the call
     */
    public ReceivingVideoCallState(String otherParty, PhoneState formerState) {
    	this.init(otherParty, formerState);
    }
    
    /**
     * Creates the correct communication object.
     * @param duration		the call's duration
     * @param cost			the call's cost
     * @param otherParty	the other party participating in the call
     * @return	the Video object with the given properties
     * @throws DurationNotValid		if the duration of the call is not a valid one
     */
    protected final Call createCommunication(
    		int duration,
    		int cost,
    		String otherParty) throws DurationNotValid {
    	return new Video(duration, cost, otherParty);
    }
    
}
