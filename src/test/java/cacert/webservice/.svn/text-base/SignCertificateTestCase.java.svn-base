package cacert.webservice;

import cacert.shared.stubs.CertificateType;
import cacert.shared.stubs.SignCertificateType;

public class SignCertificateTestCase extends CacertWebServiceTestCase{

	public void testingValidSignCertificate() {

		CertificateType certificate = null;
		boolean verifyResult=false;
		SignCertificateType parameters = new SignCertificateType();
		parameters.setEntityName("TEST1");
		parameters.setPublicKey(this.publicKey);
		try{

			certificate = cacert.signCertificate(parameters);
			
			SecurityInfo.getInstance().setCertificate(certificate);

			String uniqueCertificateRepresentatin = certificate.getSerial() + certificate.getSubject() + certificate.getIssuer() +
					certificate.getKeyUsage() + certificate.getPublicKey() + 
	    			certificate.getValidFrom() + certificate.getValidTo() + 
	    			certificate.getThumbPrintAlgorithm() + certificate.getSignatureAlgorithm();;
			
			String thumbPrint = manager.makeThumbPrint(uniqueCertificateRepresentatin, THUMBPRINT_ALGORITHM);
			
			verifyResult = this.manager.verifyDigitalSignature(certificate.getSignature(), thumbPrint, 
					SecurityInfo.getInstance().getCertificateAPublicKey(), certificate.getSignatureAlgorithm());
			
		} catch(Exception e) {
			System.out.println("error");
		}
		
		assertTrue(verifyResult);

	}
	
	public void testingInvalidSignCertificate() {

		CertificateType certificate = null;
		boolean verifyResult=false;
		SignCertificateType parameters = new SignCertificateType();
		parameters.setEntityName("TEST1");
		parameters.setPublicKey(this.publicKey);
		try{

			certificate = cacert.signCertificate(parameters);
			certificate.setSubject("TESTERROR");
			SecurityInfo.getInstance().setCertificate(certificate);

			String uniqueCertificateRepresentatin = certificate.getSerial() + certificate.getSubject() + certificate.getIssuer() +
					certificate.getKeyUsage() + certificate.getPublicKey() + 
	    			certificate.getValidFrom() + certificate.getValidTo() + 
	    			certificate.getThumbPrintAlgorithm() + certificate.getSignatureAlgorithm();;
			
			String thumbPrint = manager.makeThumbPrint(uniqueCertificateRepresentatin, THUMBPRINT_ALGORITHM);
			
			verifyResult = this.manager.verifyDigitalSignature(certificate.getSignature(), thumbPrint, 
					SecurityInfo.getInstance().getCertificateAPublicKey(), certificate.getSignatureAlgorithm());
			
		} catch(Exception e) {
			System.out.println("error");
		}
		
		assertFalse(verifyResult);

	}	
	
	
	
	
	
}
