package anacom.shared.exceptions.phone.invalidState;

public class InvalidStateFinishReceivingCall extends InvalidState {

	private static final long serialVersionUID = 1L;
	
	public InvalidStateFinishReceivingCall() {}

	/**
	 * Constructor
	 * @param number	the number of the Phone that tried to finish a call
	 * @param state		a String representation of the state the Phone was in
	 * 					when he tried to finish
	 */
	public InvalidStateFinishReceivingCall(String number, String state) { 
		super(number, state);
	}
	
}
