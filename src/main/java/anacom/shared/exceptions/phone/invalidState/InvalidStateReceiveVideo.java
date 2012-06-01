package anacom.shared.exceptions.phone.invalidState;

public class InvalidStateReceiveVideo extends InvalidState {

	private static final long serialVersionUID = 1L;
	
	public InvalidStateReceiveVideo() {}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone that received the video call
	 * @param state		a String representation of the state in which the Phone
	 * 					was in
	 */
	public InvalidStateReceiveVideo(String number, String state) {
		super(number, state);
	}

}
