package anacom.shared.exceptions;


public class IncompatiblePrefix extends AnacomException {

	private static final long serialVersionUID = 1L;
	private String operatorPrefix;
	private String phonePrefix;
	
	public IncompatiblePrefix() {}
	
	/**
	 * Constructor
	 * @param operatorPrefix	the prefix of the operator
	 * @param phonePrefix		the prefix of the phone
	 */
	public IncompatiblePrefix(String operatorPrefix, String phonePrefix) {
		this.operatorPrefix = operatorPrefix;
		this.phonePrefix = phonePrefix;
	}	
	
	/**
	 * Gets the operator's prefix
	 * @return		the operator's prefix
	 */
	public String getOperatorPrefix() { 
		return this.operatorPrefix; 
	}
	
	/**
	 * Gets the phone's prefix
	 * @return		the phone's prefix
	 */
	public String getPhonePrefix() { 
		return this.phonePrefix; 
	}
	
}
