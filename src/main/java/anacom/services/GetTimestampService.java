package anacom.services;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.TimestampDTO;
import anacom.shared.exceptions.AnacomException;

public class GetTimestampService extends AnacomService {
	
	private TimestampDTO timestampDTO = null;

	@Override
	public void dispatch() throws AnacomException {
		DateTime timestamp = ((Network)FenixFramework.getRoot()).getTimestamp();
		this.timestampDTO = new TimestampDTO(timestamp);
	}

	/**
	 * @return the TimestampDTO with the service execution's result
	 */
	public TimestampDTO getTimestampDTO() { return this.timestampDTO; }
	
}
