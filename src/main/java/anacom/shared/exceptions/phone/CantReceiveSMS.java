package anacom.shared.exceptions.phone;


public class CantReceiveSMS extends PhoneException {

	private static final long serialVersionUID = 1L;
	
	public CantReceiveSMS() {}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone to which the SMS
	 * 					was sent
	 */
	public CantReceiveSMS(String number) { 
		super(number); 
	}
}
