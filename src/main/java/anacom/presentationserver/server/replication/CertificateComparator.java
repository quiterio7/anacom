package anacom.presentationserver.server.replication;

import cacert.shared.stubs.CertificateType;

public class CertificateComparator {

	public boolean equals(CertificateType certificate1,
			CertificateType certificate2) {
		return certificate1.getSerial() == certificate1.getSerial() &&
				certificate1.getSubject().equals(certificate2.getSubject()) &&
				certificate1.getIssuer().equals(certificate2.getIssuer()) &&
				certificate1.getKeyUsage().equals(certificate2.getKeyUsage()) &&
				certificate1.getPublicKey().equals(certificate2.getPublicKey()) &&
				certificate1.getSignature().equals(certificate2.getSignature()) &&
				certificate1.getValidFrom() == certificate2.getValidFrom() &&
				certificate1.getValidTo() == certificate2.getValidTo() &&
				certificate1.getThumbPrintAlgorithm().equals(
						certificate2.getThumbPrintAlgorithm()) &&
				certificate1.getThumbPrint().equals(
								certificate2.getThumbPrint()) &&
				certificate1.getSignatureAlgorithm().equals(
						certificate2.getSignatureAlgorithm());
	}
}
