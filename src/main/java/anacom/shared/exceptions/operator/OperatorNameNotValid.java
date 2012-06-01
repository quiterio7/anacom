package anacom.shared.exceptions.operator;

public class OperatorNameNotValid extends OperatorException {

	private static final long serialVersionUID = 1L;
	
	public OperatorNameNotValid() {}

	/**
	 * Constructor
	 * @param name		the invalid operator name
	 */
	public OperatorNameNotValid(String name) { 
		super(name, null); 
	}
}
