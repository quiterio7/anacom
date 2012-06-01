package anacom.shared.dto;


public class ReceivedSMSDTO extends AnacomDTO {

	private static final long serialVersionUID = 1L;
	private String message;
	private String source;
	private int cost;
	
	/**
	 * Constructor of a DTO with a received SMS
	 * @param message		the message received in the SMS body
	 * @param source		the source of the SMS Communication
	 * @param cost			the cost of the SMS Communication 
	 */
	public ReceivedSMSDTO(String message, String source, int cost) {
		this.message = message;
		this.source = source;
		this.cost = cost;
	}
	
	/**
	 * Empty Constructor
	 */
	public ReceivedSMSDTO() {}
	
	/**
	 * Getter of the Message in ReceivedSMSDTO
	 * @return	the message of the SMS Communication
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Getter of the Source in ReceivedSMSDTO
	 * @return	the source of the SMS Communication
	 */
	public String getSource() {
		return this.source;
	}
	
	/**
	 * Getter of the Communication Cost
	 * @return	the cost of service SMS
	 */
	public int getCost() {
		return this.cost;
	}
	
	/**
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof ReceivedSMSDTO) {
			ReceivedSMSDTO dto = (ReceivedSMSDTO) o;
			if(		dto.getCost() == this.getCost() && 
					dto.getMessage().equals(this.getMessage()) &&
					dto.getSource().equals(this.getSource())) {
				return true;
			}
		}
		return false;
	}
}
