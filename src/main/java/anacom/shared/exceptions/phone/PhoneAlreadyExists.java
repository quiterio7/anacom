package anacom.shared.exceptions.phone;

public class PhoneAlreadyExists extends PhoneException {

	private static final long serialVersionUID = 1L;
	
	public PhoneAlreadyExists() {}
	
	/**
	 * Constructor
	 * @param number	the number of the phone
	 */
	public PhoneAlreadyExists(String number) { 
		super(number); 
	}

}
