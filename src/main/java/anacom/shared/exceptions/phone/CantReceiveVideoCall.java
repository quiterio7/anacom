package anacom.shared.exceptions.phone;


public class CantReceiveVideoCall extends PhoneException {

	private static final long serialVersionUID = 1L;

	public CantReceiveVideoCall() {}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone to which the Video call
	 * 					was made
	 */
	public CantReceiveVideoCall(String number) { 
		super(number); 
	}
}
