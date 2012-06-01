package anacom.shared.exceptions.phone;

import anacom.shared.exceptions.AnacomException;

/**
 * General class for exceptions related to Phones
 */
public class PhoneException extends AnacomException {

	private static final long serialVersionUID = 1L;
	private String number;
	
	public PhoneException() {}
	
	/**
	 * Constructor
	 * @param number	the Phone number that caused the exception
	 */
	public PhoneException(String number) { 
		this.number = number; 
	}
	
	/**
	 * Get the Phone number that caused the exception
	 * @return	the Phone number
	 */
	public String getNumber() { 
		return this.number; 
	}
	
}
