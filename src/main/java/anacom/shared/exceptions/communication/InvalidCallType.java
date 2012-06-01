package anacom.shared.exceptions.communication;


public class InvalidCallType extends PhoneCommunicationException {

	private static final long serialVersionUID = 1L;
	
	private String type;
	
	public InvalidCallType() {}
	
	/**
	 * Constructor
	 * @param otherParty	the other party's Phone number
	 * @param type			a String representation of the given call type
	 */
	public InvalidCallType(String otherParty, String type) {
		super(otherParty);
		this.type = type;
	}
	
	/**
	 * @return	the String representation of the call type
	 */
	public String getType() { return this.type; }
	
}
