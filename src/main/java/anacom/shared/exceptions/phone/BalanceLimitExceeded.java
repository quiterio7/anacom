package anacom.shared.exceptions.phone;


public class BalanceLimitExceeded extends PhoneException {

	private static final long serialVersionUID = 1L;

	public BalanceLimitExceeded() {}
	
	/**
	 * Constructor
	 * @param number	the Phone number that caused the exception
	 */
	public BalanceLimitExceeded(String number) { 
		super(number); 
	}
	
}
