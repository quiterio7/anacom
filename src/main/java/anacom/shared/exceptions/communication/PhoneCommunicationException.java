package anacom.shared.exceptions.communication;

import anacom.shared.exceptions.AnacomException;


public class PhoneCommunicationException extends AnacomException {

	private static final long serialVersionUID = 1L;
	private String otherParty;
	
	public PhoneCommunicationException() {}
	
	/**
	 * Constructor
	 * @param otherParty	the number of the otherParty in the Communication
	 * 						that caused the exception
	 */
	public PhoneCommunicationException(String otherParty) {
		this.otherParty = otherParty;
	}
	
	/**
	 * @return	the number of the otherParty of the Communication
	 */
	public String getOtherParty() { return this.otherParty; }
	
}
