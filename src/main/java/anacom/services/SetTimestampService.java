package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.TimestampDTO;
import anacom.shared.exceptions.AnacomException;

public class SetTimestampService extends AnacomService {

	private TimestampDTO timestampDTO = null;
	
	/**
	 * Creates an instance of SetTimestampService with the timestamp in the
	 * given dto.
	 * @param dto the dto with the desired timestamp
	 */
	public SetTimestampService(TimestampDTO dto) { 
		this.timestampDTO = dto; }
	
	@Override
	public void dispatch() throws AnacomException {
		((Network)FenixFramework.getRoot()).setTimestamp(
				this.timestampDTO.getTimestamp());
	}

}
