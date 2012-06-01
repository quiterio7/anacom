package anacom.shared.exceptions.phone;

public class PhoneNotExists extends PhoneException {

	private static final long serialVersionUID = 1L;
	
	public PhoneNotExists() {}
	
	/**
	 * Constructor
	 * @param number	the number of the phone
	 */
	public PhoneNotExists(String number) { 
		super(number); 
	}
	
}
