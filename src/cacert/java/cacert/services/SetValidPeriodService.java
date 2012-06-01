package cacert.services;

import pt.ist.fenixframework.FenixFramework;
import cacert.domain.CAManager;
import cacert.shared.dto.ValidPeriodDTO;
import cacert.shared.exceptions.CacertException;

public class SetValidPeriodService extends CacertService {

	private final ValidPeriodDTO periodDTO;
	
	public SetValidPeriodService(ValidPeriodDTO dto) {
		this.periodDTO = dto;
	}
	
	
	@Override
	public void dispatch() throws CacertException {
		CAManager manager = (CAManager) FenixFramework.getRoot();
		manager.setValidPeriod(this.periodDTO.getTimeStamp());
	}
	
	

}
