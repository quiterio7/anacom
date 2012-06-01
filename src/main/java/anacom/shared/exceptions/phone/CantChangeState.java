package anacom.shared.exceptions.phone;


public class CantChangeState extends PhoneException {
	
	private static final long serialVersionUID = 6379691376319959603L;
	private String currentState;
	private String newState;
	
	/**
	 * Empty cantChangeState
	 */
	public CantChangeState () {}
	
	/**
	 * Constructor of Exception
	 * @param number			the number which we want to change the state 
	 * @param currentState		the current State
	 * @param newState			The new Invalid State
	 */
	public CantChangeState(String number, String currentState, String newState) {
		super(number);
		this.currentState = currentState;
		this.newState = newState;
	}

	public String getCurrentState() {
		return this.currentState;
	}

	public String getInvalidState() {
		return this.newState;
	}
}
