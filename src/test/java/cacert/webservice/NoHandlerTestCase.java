package cacert.webservice;

import cacert.shared.stubs.Cacert;
import cacert.shared.stubs.CertificateSerialsListType;
import cacert.shared.stubs.CertificateType;
import cacert.shared.stubs.SignCertificateType;

public class NoHandlerTestCase  extends CacertWebServiceTestCase {
	
	
	@Override
	protected void setUp() {
		Cacert service = new Cacert();
		cacert = service.getCacertApplicationServicePort();
	}
	
	public void testingValidSignCertificate() {
		assertTrue("The sign Certificate should return true", 
					signCertificate());
	}
	
	
	public void testInvokingInvalidService() {
		if(!signCertificate()) {
			fail("Error signing certificate");
		}
		
		CertificateSerialsListType list = this.cacert.getBlockedList();
		
		assertTrue("The list should be null", list == null);
	}
	
	private boolean signCertificate() {
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
	    			certificate.getThumbPrintAlgorithm() + certificate.getSignatureAlgorithm();
	    			
	    			
			//Set security informations
	    	SecurityInfo info = SecurityInfo.getInstance();
	    	info.setCertificate(certificate);
	    	info.setThisEntityPublicKey(this.publicKey);
	    	info.setThisEntityPrivateKey(this.privateKey);
	    	
			String thumbPrint = manager.makeThumbPrint(uniqueCertificateRepresentatin, THUMBPRINT_ALGORITHM);
			
			verifyResult = this.manager.verifyDigitalSignature(certificate.getSignature(), thumbPrint, 
					SecurityInfo.getInstance().getCertificateAPublicKey(), certificate.getSignatureAlgorithm());
			
			return verifyResult;
			
		} catch(Exception e) {
			System.out.println("error");
			return false;
		}

	}
}
