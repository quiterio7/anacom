package anacom.shared.dto;

public class LastMadeCommunicationDTO extends AnacomDTO {
	
	private static final long serialVersionUID = 1123123213154214L;
	
	private String destination;
	private String communicationType;
	private int	cost;
	private int size;
	
	public LastMadeCommunicationDTO () {}
	
	/**
	 * Constructor of LastCommunicationDTO
	 * @param destination		the destination of the last communication
	 * @param communicationType	the Type of Communication committed
	 * @param cost				the total cost of Communication 
	 * @param total				total time for (Video and Voice Calls and
	 * 							number of characters to SMS)
	 */
	public LastMadeCommunicationDTO(String destination,
									String communicationType,
									int cost,
									int total)	{
		this.destination = destination;
		this.communicationType = communicationType;
		this.cost = cost;
		this.size = total;
	}

	/**
	 * Getter of Destination
	 * @return	the destination Number of the last communication
	 */
	public String getDestination() {
		return this.destination;
	}

	/**
	 * Getter of the Communication Type (SMS, VOICE, VIDEO)
	 * @return	the type of last communication
	 */
	public String getCommunicationType() {
		return this.communicationType;
	}

	/**
	 * Getter of the cost of last communication
	 * @return	the total cost of communication
	 */
	public int getCost() {
		return this.cost;
	}

	/**
	 * Getter of total characters used in SMS or 
	 * total time dispensed in communication
	 * @return	total time or chars dispensed in communication
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * @param obj	an Object to which to compare this SMSDTO
	 * @return		true if obj is a SMSDTO equal to this one, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LastMadeCommunicationDTO) {
			LastMadeCommunicationDTO dto = (LastMadeCommunicationDTO)obj;
			if (	dto.getCommunicationType().equals(this.getCommunicationType()) &&
					dto.getCost() == this.getCost() &&
					dto.getDestination().equals(this.getDestination()) &&
					dto.getSize() == this.getSize()) {
				return true;
			}
		}
		return false;
	}
	
}