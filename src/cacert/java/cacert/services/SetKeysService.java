package cacert.services;

import pt.ist.fenixframework.FenixFramework;
import cacert.domain.CAManager;
import cacert.shared.dto.SetKeysDTO;

public class SetKeysService extends CacertService{

	private SetKeysDTO setKeysDTO;
	
	public SetKeysService(SetKeysDTO setKeysDTO) {
		super();
		this.setKeysDTO = setKeysDTO;
	}

	@Override
	public void dispatch() {
		
		CAManager caManager = (CAManager) FenixFramework.getRoot();
		caManager.setKeys(setKeysDTO.getPublicKey(), setKeysDTO.getPrivateKey());
	}
}
