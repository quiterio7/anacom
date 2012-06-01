package anacom.shared.exceptions.phone;


public class InvalidPhoneType extends PhoneException {

	private static final long serialVersionUID = 1L;
	private String type = null;
	
	public InvalidPhoneType() {}
	
	/**
	 * Constructor
	 * @param number	the given number for the Phone with the invalid type
	 * @param type		the given invalid Phone type
	 */
	public InvalidPhoneType(String number, String type) {
		super(number);
		this.type = type;
	}
	
	/**
	 * @return	the String representation of the Phone type
	 */
	public String getType() { 
		return this.type; 
	}

}
