package anacom.shared.dto;

public class SMSDTO extends AnacomDTO {

	private static final long serialVersionUID = 1L;
	
	private String message;
	private String source;
	private String destination;
	private int    cost;
	
	public SMSDTO() {}
	
	/**
	 * Constructor
	 * @param message	the sent message
	 * @param source	the source's Phone number
	 * @param destiny	the destination's Phone number
	 * @param cost		the SMS's cost
	 */
	public SMSDTO(String message, String source, String destiny, int cost) {
		this.destination = destiny;
		this.source = source;
		this.message = message;		
		this.cost = cost;
	}
	
	/**
	 * @return	the SMS message
	 */
	public String getMessage() { 
		return this.message; 
	}
	
	/**
	 * @return	the destination's Phone number
	 */
	public String getDestination() { 
		return this.destination; 
	}
	
	/**
	 * @return	the source's Phone number
	 */
	public String getSource() { 
		return this.source; 
	}
	
	/**
	 * @return	the SMS's cost
	 */
	public int getCost() { 
		return this.cost; 
	}
	
	/**
	 * @param obj	an Object to which to compare this SMSDTO
	 * @return		true if obj is a SMSDTO equal to this one, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SMSDTO) {
			SMSDTO dto = (SMSDTO)obj;
			if (	this.message.equals(dto.getMessage()) &&
					this.source.equals(dto.getSource()) &&
					this.destination.equals(dto.getDestination()) &&
					this.cost == dto.getCost()) {
				return true;
			}
		}
		return false;
	}
	
}