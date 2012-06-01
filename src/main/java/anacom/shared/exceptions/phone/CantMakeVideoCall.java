package anacom.shared.exceptions.phone;


public class CantMakeVideoCall extends PhoneException {

	private static final long serialVersionUID = 1L;

	public CantMakeVideoCall() {}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone that tried to make the 
	 * 					Video call
	 */
	public CantMakeVideoCall(String number) { 
		super(number);
	}
	
}
