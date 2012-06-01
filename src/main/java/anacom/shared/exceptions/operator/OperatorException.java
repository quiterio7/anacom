package anacom.shared.exceptions.operator;

import anacom.shared.exceptions.AnacomException;

public class OperatorException extends AnacomException {

	private static final long serialVersionUID = 1L;
	private String name;
	private String prefix;
	
	public OperatorException() {}
	
	/**
	 * Constructor
	 * @param prefix	the prefix of the Operator that caused
	 * 					the exception
	 * @param name		the name of the Operator that caused the
	 * 					exception
	 */
	public OperatorException(String name, String prefix) {
		this.name = name;
		this.prefix = prefix;
	}
	
	/**
	 * @return	the Operator's name
	 */
	public String getName() { 
		return this.name; 
	}
	
	/**
	 * @return	the Operator's prefix
	 */
	public String getPrefix() { 
		return this.prefix; 
	}
	
}
