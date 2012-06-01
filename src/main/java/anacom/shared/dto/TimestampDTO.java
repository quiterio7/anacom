package anacom.shared.dto;

import org.joda.time.DateTime;

public class TimestampDTO extends AnacomDTO {

	private static final long serialVersionUID = 1L;
	
	private DateTime timestamp;
	
	/**
	 * Creates a TimestampDTO with given timestamp.
	 * @param timestamp the desired timestamp
	 */
	public TimestampDTO(DateTime timestamp) { 
		this.timestamp = timestamp; 
	}
	
	/**
	 * @return this TimestampDTO's timestamp
	 */
	public DateTime getTimestamp() { 
		return this.timestamp; 
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TimestampDTO) {
			TimestampDTO dto = (TimestampDTO)o;
			if (this.timestamp.equals(dto.getTimestamp())) {
				return true;
			}
		}
		return false;
	}

}
