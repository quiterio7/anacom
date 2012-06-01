package cacert.services;

import pt.ist.fenixframework.FenixFramework;
import cacert.domain.CAManager;
import cacert.domain.Certificate;
import cacert.shared.dto.BlockCertificateDTO;
import cacert.shared.dto.CertificateDTO;
import cacert.shared.exceptions.CantCreateCertificateException;

public class AddToBlockedListService extends CacertService{

	private BlockCertificateDTO blockDTO;
	private CertificateDTO certificateDTO;
	
	public AddToBlockedListService(BlockCertificateDTO blockDTO) {
		super();
		this.blockDTO = blockDTO;
	}

		@Override
		public void dispatch() throws CantCreateCertificateException {
			CAManager caManager = (CAManager) FenixFramework.getRoot();
			Certificate newCertificate = caManager.addToBlockedList(blockDTO.getOldSerial(),
																	blockDTO.getEntityName(),
																	blockDTO.getNewPublicKey());
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