package cacert.shared.dto;

import java.io.Serializable;
import java.util.List;

public class CertificatesSerialListDTO implements Serializable{

	private static final long serialVersionUID = 912312832711237213L;
	
	private final List<BlockedSerialDTO> certificatesList;

	/**
	 * Constructor of CertificatesSerialListDTO
	 * @param certificatesList	the list with serials of certificates
	 */
	public CertificatesSerialListDTO(List<BlockedSerialDTO> certificatesList) {
		this.certificatesList = certificatesList;
	}

	/**
	 * Getter of Certificate List Serials
	 * @return the list with serial certificates
	 */
	public List<BlockedSerialDTO> getCertificatesList() {
		return certificatesList;
	}

}
