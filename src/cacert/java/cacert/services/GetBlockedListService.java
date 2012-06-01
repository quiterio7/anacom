package cacert.services;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.FenixFramework;
import cacert.domain.BlockedCertificate;
import cacert.domain.CAManager;
import cacert.shared.dto.BlockedSerialDTO;
import cacert.shared.dto.CertificatesSerialListDTO;

public class GetBlockedListService extends CacertService{

	private CertificatesSerialListDTO certificatesList;
	
	/**
	 * Gets all Blocked Certificates and stores them in certificatesList
	 */
	@Override
	public void dispatch() {
		CAManager caManager = (CAManager) FenixFramework.getRoot();
		
		List<BlockedSerialDTO> certificatesDTOs = new ArrayList<BlockedSerialDTO>();
		for(BlockedCertificate blockedSerial : caManager.getBlockedList()) {
			
			certificatesDTOs.add(new BlockedSerialDTO(blockedSerial.getSerial()));
		}
		
		certificatesList = new CertificatesSerialListDTO(certificatesDTOs);
	}

	/**
	 * Getter of Certificate Serial List
	 * @return
	 */
	public CertificatesSerialListDTO getCertificatesList() {
		return certificatesList;
	}
}
