package anacom.domain;

import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;

public abstract class MakingCallState extends MakingCallState_Base {
    
    public  MakingCallState() {
        super();
    }
    
    /**
     * Constructor
     * @param otherParty	the number of the Phone receiving the call
     * @param startTime		the time at which the call was established in seconds
     * @param formerState	the state of the Phone before it made the call
     */
    public MakingCallState(String otherParty, long startTime, PhoneState formerState) {
    	this.init(otherParty, startTime, formerState);
    }
    
    /**
     * Initialise the instance of the MakingCallState.
     * @param otherParty	the number of the other Phone participating in the call
     * @param startTime		the time at which the call was established in seconds
     * @param formerState	the state of the Phone before it became Occupied
     */
    protected void init(String otherParty, long startTime, PhoneState formerState) {
    	this.init(otherParty, formerState);
    	this.setStartTime(startTime);
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
    Call finishCall(long endTime) 
    		throws DurationNotValid, InvalidStateFinishMakingCall {
    	int duration = (int)(endTime - this.getStartTime());
    	
    	// calcCost is implemented in the subclasses
    	int cost = calcCost(duration, this.getPhone().getPrefix(), 
    			Network.getOperatorPrefixfromNumber(this.getOtherParty()) );
    			
    	// createCommunication is implemented in the subclasses
    	Call establishedCall = createCommunication(
    			duration,
				cost,
				this.getOtherParty());
    	this.getPhone().addEstablishedCalls(establishedCall);
    	this.getPhone().setLastMadeCommunication(establishedCall);
    	
    	this.getPhone().decreaseBalance(cost);
    	this.getPhone().setState(this.getFormerState());
    	
    	return establishedCall;
    } 
    
    /**
	 * Calculates the cost of a call
	 * @param duration			the duration of the call
	 * @param originPrefix		the origin's operator prefix
	 * @param destinationPrefix	the destination's operator prefix
	 * @return	the cost of the call
	 */
    protected abstract int calcCost(
    		int duration,
    		String originPrefix,
    		String destinationPrefix);
    
}
