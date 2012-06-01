package anacom.shared.exceptions.communication;


public class DurationNotValid extends PhoneCommunicationException {

	private static final long serialVersionUID = 1L;
	private int duration;
	
	public DurationNotValid() {}
	
	/**
	 * Constructor
	 * @param otherParty	the number of the otherParty in the Communication
	 * @param duration		the invalid duration
	 */
	public DurationNotValid(String otherParty, int duration) {
		super(otherParty);
		this.duration = duration;
	}
	
	/**
	 * Gets the the invalid duration that originated this exception
	 * @return	the duration
	 */
	public int getDuration() { 
		return this.duration; 
	}
}