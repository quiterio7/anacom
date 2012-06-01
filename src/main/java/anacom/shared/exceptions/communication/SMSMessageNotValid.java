package anacom.shared.exceptions.communication;


public class SMSMessageNotValid extends PhoneCommunicationException {

	private static final long serialVersionUID = 1L;
	private String message;
	
	public SMSMessageNotValid() {}
	
	/**
	 * Constructor
	 * @param otherParty	the number of the otherParty in the Communication
	 * 						that caused the exception
	 * @param message		the sms message
	 */
	public SMSMessageNotValid(String otherParty, String message) {
		super(otherParty);
		this.message = message;
	}
	
	/**
	 * @return	the message
	 */
	public String getMessage() { return this.message; }
	
}
