package cacert.shared.dto;

import java.io.Serializable;

public class ValidPeriodDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final long timePeriod;
	
	public ValidPeriodDTO(long timePeriod) {
		this.timePeriod = timePeriod;
	}
	
	public long getTimeStamp() {
		return this.timePeriod;
	}
	

}
