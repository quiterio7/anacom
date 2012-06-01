package anacom.shared.exceptions.phone.invalidState;

public class InvalidStateMakeVideo extends InvalidState {

	private static final long serialVersionUID = 1L;
	
	public InvalidStateMakeVideo() {}

	/**
	 * Constructor
	 * @param number	the number of the Phone that made the video call
	 * @param state		a String representation of the state the Phone was in
	 * 					when he tried to make the call
	 */
	public InvalidStateMakeVideo(String number, String state) { 
		super(number, state);
	}
	
}
