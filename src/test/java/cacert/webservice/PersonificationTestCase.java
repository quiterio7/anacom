package cacert.webservice;

import java.io.FileNotFoundException;

import cacert.shared.stubs.CertificateRemoteException;
import cacert.shared.stubs.CertificateType;
import cacert.shared.stubs.SignCertificateType;

public class PersonificationTestCase extends CacertWebServiceTestCase {
	
	String publicKey2;
	String privateKey2;
	
	public PersonificationTestCase() {
		String relativePathOP1keys = "RSAkeys/OP2keys";
		String relativePathCAkeys = "RSAkeys/CAkeys";
		
		try {
			
			publicKey2 = manager.getPublicRSAKeyfromFile(relativePathOP1keys);
			privateKey2 = manager.getPrivateRSAKeyfromFile(relativePathOP1keys);
			
			SecurityInfo securityInfo = SecurityInfo.getInstance();
			securityInfo.setThisEntityPublicKey(publicKey);
			securityInfo.setThisEntityPrivateKey(privateKey);
			securityInfo.setCertificateAPublicKey(CApublicKey);
			
		} catch (FileNotFoundException e) {
			//FIXME: later
			System.exit(-1);
		}
	}
	
	public void testPersonificationClient() {
		
		SignCertificateType signCertificate1 = new SignCertificateType();
		SignCertificateType signCertificate2 = new SignCertificateType();
		CertificateType certificate1 = new CertificateType();
		CertificateType certificate2 = new CertificateType();
		SecurityInfo security = SecurityInfo.getInstance();
		
		// set Operator 1
		signCertificate1.setEntityName("OP1");
		signCertificate1.setPublicKey(this.publicKey);
		// set Operator 2
		signCertificate2.setEntityName("OP2");
		signCertificate2.setPublicKey(this.publicKey2);
		
		try {
			//Sign Certificates
			certificate1 = cacert.signCertificate(signCertificate1);
			certificate2 = cacert.signCertificate(signCertificate2);
		
			//setConfiguration 1 in Security Info
			security.setCertificate(certificate1);
			security.setThisEntityPrivateKey(this.privateKey2);
			security.setThisEntityPublicKey(this.publicKey2);
			
			assertTrue("This answer should be null", 
					cacert.getBlockedList() == null);
			
		} catch (CertificateRemoteException e) {
			fail("error sign certificate");
		}
	}	
}
