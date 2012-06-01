package anacom.domain;

import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;

public class SMS extends SMS_Base {
	
	/**
	 * @param message
	 * 		the message used in communication
	 * @param cost
	 * 		the cost of the communication
	 * @param otherParty
	 * 		the other number involved on communication (in source side is
	 * 		destination, in destination side, is the source)
	 * @throws SMSMessageNotValid
	 * 		if the given message isn't a valid SMS
	 * @throws PhoneNumberNotValid
	 * 		if the given other party number isn't a valid Phone number
	 */
    public SMS(String message, int cost, String otherParty)
    		throws SMSMessageNotValid, PhoneNumberNotValid {
        super();
        init(cost, otherParty);
        this.setMessage(message);
    }
    
    /**
     * get Communication Type of Class SMS
     * @return the SMS string representation
     */
    @Override
    public String getCommunicationType() {
    	return CommunicationRepresentation.getInstance().getSMSCommunication(); 
    }
    
    /**
     * Getter of the Communication Length
     * @return the total length of communication
     */
    @Override
    public int getCommunicationLength() {
    	return this.getMessage().length();
    }
}
