package anacom.domain;

import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.misc.FieldVerifier;


public abstract class Communication extends Communication_Base {
	
    public Communication() {
        super();
    }
    
    /**
     * Initializes this communication instance.
     * @param cost
     * 		the communication's cost
     * @param otherParty
     * 		this communication's other party
     * @throws PhoneNumberNotValid
     * 		if the given other party number isn't a valid Phone number
     */
    protected void init(int cost, String otherParty)
    		throws PhoneNumberNotValid {
    	if (!FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
    		throw new PhoneNumberNotValid(otherParty);
    	}
    	this.setCost(cost);
    	this.setOtherParty(otherParty);
    }
    
    /**
     * Set communication's destination
	 * @param phone		the destination phone
     */
    @Override
    public final void setDestination(Phone phone) {
    	phone.addReceivedCalls(this);  	
    }
    
    /**
     * Set communication's origin
     * @param phone		the origin phone 
     */
    @Override
    public final void setOrigin(Phone phone) {
    	phone.addEstablishedCalls(this);    	
    }
    
    /**
     * Getter of the CommunicationLength
     * @return	the  length of communication
     */
    public abstract int getCommunicationLength();    
    
    
    /**
     * Getter of the Communication Type
     * @return the type communication in string format
     */
    public abstract String getCommunicationType();
}
