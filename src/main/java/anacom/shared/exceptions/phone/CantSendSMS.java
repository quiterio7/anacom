package anacom.shared.exceptions.phone;


public class CantSendSMS extends PhoneException {

	private static final long serialVersionUID = 1L;

	public CantSendSMS() {}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone that tried to send
	 * 					the SMS
	 */
	public CantSendSMS(String number) { 
		super(number); 
	}
	
}
