package anacom.shared.exceptions.phone;


public class PhoneNumberNotValid extends PhoneException {

	private static final long serialVersionUID = 1L;
	
	public PhoneNumberNotValid() {}
	
	/**
	 * Constructor
	 * @param number	the given invalid number
	 */
	public PhoneNumberNotValid(String number) { 
		super(number); 
	}

}
