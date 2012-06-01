package anacom.shared.exceptions.phone;


public class CantReceiveVoiceCall extends PhoneException {

	private static final long serialVersionUID = 1L;
	
	public CantReceiveVoiceCall() {}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone to which the SMS
	 * 					was sent
	 */
	public CantReceiveVoiceCall(String number) { 
		super(number); 
	}
}
