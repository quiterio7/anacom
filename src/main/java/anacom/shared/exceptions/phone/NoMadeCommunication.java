package anacom.shared.exceptions.phone;


public class NoMadeCommunication extends PhoneException {

	private static final long serialVersionUID = 4787732662636390207L;
	
	/**
	 * Empty constructor
	 */
	public NoMadeCommunication() {}
	
	/**
	 * Constructor of NullMadeCommunication 
	 * @param number	the number of the Phone that caused the exception
	 */
	public NoMadeCommunication(String number) { 
		super(number); 
	}
	
}
