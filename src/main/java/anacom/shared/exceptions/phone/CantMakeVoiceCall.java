package anacom.shared.exceptions.phone;


public class CantMakeVoiceCall extends PhoneException {

	private static final long serialVersionUID = 1L;

	public CantMakeVoiceCall() {}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone that tried to make the 
	 * 					voice call
	 */
	public CantMakeVoiceCall(String number) { 
		super(number); 
	}
}
