package anacom.domain;

import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.misc.FieldVerifier;
import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;

public class Voice extends Voice_Base {
    
	/**
	 * @param duration
	 * 		the duration of the voice call
	 * @param cost
	 * 		the cost of the voice call
	 * @param otherParty
	 * 		the other party involved in the call
	 * @throws DurationNotValid
	 * 		if the duration of the call is not a valid one
	 * @throws PhoneNumberNotValid
	 * 		if the given other party number isn't a valid Phone number
	 */
    public Voice(int duration, int cost, String otherParty) 
    	throws DurationNotValid, PhoneNumberNotValid {
        super();
        if (!FieldVerifier.getInstance().validDuration(duration)) {
        	throw new DurationNotValid(otherParty, duration);
        }
        init(cost, otherParty);
        this.setDuration(duration);
    }
   
    /**
     * get Communication Type of Class Voice
     * @return the SMS string representation
     */
    @Override
    public String getCommunicationType() {
    	return CommunicationRepresentation.getInstance().getVoiceCommunication(); 
    }
    
    /**
     * Getter of the Communication Length
     * @return the total length of communication
     */
    @Override
    public int getCommunicationLength() {
    	return this.getDuration();
    }
    
}
