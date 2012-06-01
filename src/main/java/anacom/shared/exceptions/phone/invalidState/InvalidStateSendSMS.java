package anacom.shared.exceptions.phone.invalidState;

public class InvalidStateSendSMS extends InvalidState {

	private static final long serialVersionUID = 1L;
	
	public InvalidStateSendSMS() {}

	/**
	 * Constructor
	 * @param number	the number of the phone
	 * @param state		the state of the phone
	 */
	public InvalidStateSendSMS(String number, String state) { 
		super(number, state);
	}
	
}
