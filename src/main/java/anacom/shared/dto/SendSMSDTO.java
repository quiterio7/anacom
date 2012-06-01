package anacom.shared.dto;

public class SendSMSDTO extends AnacomDTO {

	private static final long serialVersionUID = 1L;
	
	private String message;
	private String source;
	private String destination;
	
	public SendSMSDTO() {
	}
	
	/**
	 * Constructor
	 * @param message 		The SMS Message
	 * @param source		The Source Number of the communication
	 * @param destination	The Destination Number of the communication
	 */
	public SendSMSDTO(String message, String source, String destination) {
		this.destination = destination;
		this.source = source;
		this.message = message;		
	}
	
	/**
	 * Get the message of communication
	 * @return Message
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Get the Destiny Number of communication SMS
	 * @return Destiny Number
	 */
	public String getDestination() {
		return this.destination;
	}
	
	/**
	 * Get the Source Number of communication SMS
	 * @return Source Number
	 */
	public String getSource() {
		return this.source;
	}
	
	/**
	 * 
	 * @return the prefix of Operator Source Phone
	 */
	public String getSourcePrefix() {
		return getPrefixFromNumber(this.getSource());
	}
	
	/**
	 * 
	 * @return the prefix of Operator Destination Phone
	 */
	public String getDestinationPrefix() {
		return getPrefixFromNumber(this.getDestination());
	}
	
	/**
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof SendSMSDTO) {
			SendSMSDTO dto = (SendSMSDTO) o;
			if(		dto.getDestination().equals(this.getDestination()) &&
					dto.getMessage().equals(this.getMessage()) &&
					dto.getSource().equals(this.getSource())) {
				return true;
			}
		}
		return false;
	}
	
}
