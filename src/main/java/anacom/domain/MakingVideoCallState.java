package anacom.domain;

import anacom.shared.exceptions.communication.DurationNotValid;

public class MakingVideoCallState extends MakingVideoCallState_Base {
    
    public  MakingVideoCallState() {
        super();
    }
    
    /**
     * Constructor
     * @param otherParty	the number of the Phone receiving the call
     * @param startTime		the call's start time
     * @param formerState	the state of the Phone before it made the call
     */
    public MakingVideoCallState(
    		String otherParty,
    		long startTime,
    		PhoneState formerState) {
    	this.init(otherParty, startTime, formerState);
    }
    
    /**
     * Creates the correct communication object.
     * @param duration		the call's duration
     * @param cost			the call's cost
     * @param otherParty	the other party participating in the call
     * @return	the Video object with the given properties
     * @throws DurationNotValid		if the duration of the call is not a valid one
     */
    @Override
    protected final Call createCommunication(
    		int duration,
    		int cost,
    		String otherParty) throws DurationNotValid { 
    	return new Video(duration, cost, otherParty);
    }

    /**
	 * Calculates the cost of a video call
	 * @param duration			the duration of the video call
	 * @param originPrefix		the origin's operator prefix
	 * @param destinationPrefix	the destination's operator prefix
	 * @return	the cost of the video call
	 */
	@Override
	protected int calcCost(int duration, String originPrefix, 
			String destinationPrefix) {
		return this.getPhone().getOperator().calcVideoCost(duration,
				   originPrefix, 
				   destinationPrefix);
	}
}
