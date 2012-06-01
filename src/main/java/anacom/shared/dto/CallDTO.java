package anacom.shared.dto;

public class CallDTO extends AnacomDTO {

	private static final long serialVersionUID = 1L;
	
	private String source, destination, type;
	private long startTime;
	
	public CallDTO() {}
	
	/**
	 * Constructor
	 * This constructor exists because the startTime is always set by the
	 * presentation server.
	 * @param source		the number of the call's source Phone
	 * @param destination	the number of the call's destination Phone
	 */
	public CallDTO(String source, String destination, String type) {
		this.source = source;
		this.destination = destination;
		this.type = type;
	}
	
	/**
	 * Constructor
	 * @param source		the number of the call's source Phone
	 * @param destination	the number of the call's destination Phone
	 * @param startTime		the starting instant of the call
	 * @param type			a String representation of the call's type
	 */
	public CallDTO(String source, String destination, long startTime, String type) {
		this(source, destination, type);
		this.startTime = startTime;
	}
	
	/**
	 * @return	the source's Phone number
	 */
	public String getSource() { 
		return source; 
	}
	
	/**
	 * @return	the destination's Phone number
	 */
	public String getDestination() { 
		return destination; 
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
	 * @return	the starting instant of the call
	 */
	public long getStartTime() { 
		return startTime; 
	}
	
	/**
	 * @return	a String representation of the call's type
	 */
	public String getType() { 
		return type; 
	}
	
	/**
	 * Sets the starting time of the call
	 * @param startTime		the starting time
	 */
	public void setStartTime(long startTime) { 
		this.startTime = startTime; 
	}
	
	/**
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof CallDTO) {
			CallDTO dto = (CallDTO) o;
			if(		dto.getDestination().equals(this.getDestination()) &&
					dto.getSource().equals(this.getSource()) &&
					dto.getStartTime() == this.getStartTime() &&
					dto.getType().equals(this.getType()))
				return true;
		}
		return false;
	}
	
}
