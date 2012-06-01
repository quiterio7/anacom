package anacom.shared.exceptions.operator;

public class OperatorPrefixNotValid extends OperatorException {

	private static final long serialVersionUID = 1L;
	
	public OperatorPrefixNotValid() {}
	
	/**
	 * Constructor
	 * @param prefix	the prefix of the operator
	 */
	public OperatorPrefixNotValid(String prefix) { 
		super(null, prefix); 
	}
	
}
