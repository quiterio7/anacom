package anacom.shared.exceptions.phone.invalidState;

import anacom.shared.exceptions.phone.PhoneException;

public class InvalidState extends PhoneException {

	private static final long serialVersionUID = 1213712389129L;
	private String state;
	
	/**
	 * Constructor
	 * @param number	the number of the phone
	 * @param state		the state of the phone
	 */
	public InvalidState(String number, String state) {	
		super(number);
		this.state = state;
	}
	
	public InvalidState() {
		
	}

	/**
	 * Gets the State of the phone
	 * @return	the State of the phone
	 */
	public String getState() {
		return this.state;
	} 

}