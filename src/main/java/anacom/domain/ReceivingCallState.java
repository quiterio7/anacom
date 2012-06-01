package anacom.domain;

import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall;

public abstract class ReceivingCallState extends ReceivingCallState_Base {
    
    public  ReceivingCallState() {
        super();
    }
 
    /**
     * Constructor
     * @param otherParty	the number of the Phone receiving the call
     * @param formerState	the state of the Phone before it made the call
     */
    public ReceivingCallState(String otherParty, PhoneState formerState) {
    	this.init(otherParty, formerState);
    }
    
    /**
     * Finish an undergoing call. This is called on the source Phone.
     * @param endTime the call's ending time in seconds
     * @return the Call communication object
     * @throws DurationNotValid if the given call duration is not valid
     * @throws InvalidStateFinishMakingCall	if the Phone is not currently
     * making a call
     */
    @Override
    void finishCall(int duration, int cost) 
    		throws DurationNotValid, InvalidStateFinishReceivingCall {
    	this.getPhone().addReceivedCalls(this.createCommunication(
    			duration,
    			cost,
    			this.getOtherParty()));
    	this.getPhone().setState(this.getFormerState());
    }
    
}
