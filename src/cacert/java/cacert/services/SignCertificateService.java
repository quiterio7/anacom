package cacert.services;

import pt.ist.fenixframework.FenixFramework;
import cacert.domain.CAManager;
import cacert.domain.Certificate;
import cacert.shared.dto.CertificateDTO;
import cacert.shared.dto.SignCertificateDTO;
import cacert.shared.exceptions.CantCreateCertificateException;

public class SignCertificateService extends CacertService{

	private SignCertificateDTO signDTO;
	private CertificateDTO certificateDTO;
	
	public SignCertificateService(SignCertificateDTO signDTO) {
		super();
		this.signDTO = signDTO;
	}

	@Override
	public void dispatch() 
			throws CantCreateCertificateException {
		CAManager caManager = (CAManager) FenixFramework.getRoot();
		
		Certificate newCertificate = 
				caManager.signCertificate(signDTO.getEntityName(), signDTO.getPublicKey());
		certificateDTO = new CertificateDTO(newCertificate.getSerial(),
										newCertificate.getSubject(), 
										newCertificate.getSignatureAlgorithm(),
										newCertificate.getSignature(),
										newCertificate.getIssuer(),
										newCertificate.getValidFrom(),
										newCertificate.getValidTo(),
										newCertificate.getKeyUsage(),
										newCertificate.getPublicKey(),
										newCertificate.getThumbPrintAlgorithm(),
										newCertificate.getThumbPrint());
	}

	public CertificateDTO getCertificateDTO() {
		return certificateDTO;
	}
}