package anacom.shared.exceptions.phone.invalidState;

public class InvalidStateMakeVoice extends InvalidState {

	private static final long serialVersionUID = 1L;
	
	public InvalidStateMakeVoice() {}

	/**
	 * Constructor
	 * @param number	the number of the Phone that tried to make the Voice call
	 * @param state		a String representation of the state the Phone was in when
	 * 					he tried to make the Voice call
	 */
	public InvalidStateMakeVoice(String number, String state) { 
		super(number, state);
	}
	
}
