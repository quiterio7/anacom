package anacom.shared.exceptions.phone.invalidState;

public class InvalidStateReceiveVoice extends InvalidState {

	private static final long serialVersionUID = 1L;
	
	public InvalidStateReceiveVoice() {}

	/**
	 * Constructor
	 * @param number	the number of the Phone that received the Voice call
	 * @param state		a String representation of the state the Phone was in when
	 * 					he received the Voice call
	 */
	public InvalidStateReceiveVoice(String number, String state) { 
		super(number, state);
	}
	
}
