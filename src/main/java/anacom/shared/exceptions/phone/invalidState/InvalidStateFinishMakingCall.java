package anacom.shared.exceptions.phone.invalidState;

public class InvalidStateFinishMakingCall extends InvalidState {

	private static final long serialVersionUID = 1L;
	
	public InvalidStateFinishMakingCall() {}

	/**
	 * Constructor
	 * @param number	the number of the Phone that tried to finish a call
	 * @param state		a String representation of the state the Phone was in
	 * 					when he tried to finish the call
	 */
	public InvalidStateFinishMakingCall(String number, String state) { 
		super(number, state);
	}
	
}
