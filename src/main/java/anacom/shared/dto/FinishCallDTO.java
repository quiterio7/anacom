package anacom.shared.dto;

public class FinishCallDTO extends AnacomDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String source;
	private long endTime;

	public FinishCallDTO() {}
	
	/**
	 * Constructor
	 * This constructor exists because the endTime is set by the
	 * presentation server.
	 * @param source
	 */
	public FinishCallDTO(String source) { 
		this.source = source; 
	}
	
	/**
	 * Constructor
	 * @param source	the source's phone number
	 * @param endTime	the ending time of the call
	 */
	public FinishCallDTO(String source, long endTime) {
		this(source);
		this.endTime = endTime;
	}
	
	/**
	 * Gets the source's phone number
	 * @return	the source's phone number
	 */
	public String getSource() {
		return this.source;
	}
	
	/**
	 * 
	 * @return the prefix of Operator Phone
	 */
	public String getPrefix() {
		return getPrefixFromNumber(this.getSource());
	}
	
	/**
	 * Gets the ending time of the call
	 * @return	the ending time of the call
	 */
	public long getEndTime() {
		return this.endTime;
	}
	
	/**
	 * Sets the call's ending instant
	 * @param endTime	the desired ending time instant
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * @param obj	an Object to which to compare this SMSDTO
	 * @return		true if obj is a SMSDTO equal to this one, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FinishCallDTO) {
			FinishCallDTO dto = (FinishCallDTO)obj;
			if (	dto.getEndTime() == this.getEndTime() &&
					dto.getSource().equals(this.getSource())) {
				return true;
			}
		}
		return false;
	}
	
}
