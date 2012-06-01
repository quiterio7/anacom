package anacom.shared.exceptions.operator;


public class UnrecognisedPrefix extends OperatorException {

	private static final long serialVersionUID = 1L;
	
	public UnrecognisedPrefix() {}
	
	/**
	 * Constructor
	 * @param prefix	the unrecognised prefix
	 */
	public UnrecognisedPrefix(String prefix) { 
		super(null, prefix); 
	}
	
}
