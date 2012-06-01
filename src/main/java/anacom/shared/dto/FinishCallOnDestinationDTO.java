package anacom.shared.dto;

public class FinishCallOnDestinationDTO extends AnacomDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String destination;
	private int duration;
	private int cost;

	public FinishCallOnDestinationDTO() {
	}
	
	/**
	 * Constructor
	 * @param source	the source's phone number
	 * @param endTime	the ending time of the call
	 */
	public FinishCallOnDestinationDTO(String destination, int duration,
			int cost) {
		this.destination = destination;
		this.duration = duration;
		this.cost = cost;
	}
	
	/**
	 * Gets the destination's phone number
	 * @return	the destination's phone number
	 */
	public String getDestination() {
		return this.destination;
	}
	
	/**
	 * Gets the duration of the call
	 * @return	the duration of the call
	 */
	public int getDuration() {
		return this.duration;
	}
	
	/**
	 * Gets the cost of the call
	 * @return	the cost of the call
	 */
	public int getCost() {
		return this.cost;
	}
	
	/**
	 * Compares this FinishCallOnDestinationDTO with another object.
	 * @param obj	the Object to which compare this FinishCallOnDestinationDTO
	 * @return true if obj is a FinishCallOnDestinationDTO and both have the same destination,
	 * the same duration and the same cost, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FinishCallOnDestinationDTO) {
			FinishCallOnDestinationDTO dto = (FinishCallOnDestinationDTO)obj;
			if (	this.destination.equals(dto.getDestination()) &&
					this.duration == dto.getDuration() &&
					this.cost == dto.getCost()) {
				return true;
			}
		}
		return false;
	}
}
