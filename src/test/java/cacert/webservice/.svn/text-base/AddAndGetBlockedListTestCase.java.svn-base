package cacert.webservice;

import cacert.shared.stubs.BlockCertificateType;
import cacert.shared.stubs.BlockedSerialCertificateType;
import cacert.shared.stubs.CertificateRemoteException;
import cacert.shared.stubs.CertificateSerialsListType;
import cacert.shared.stubs.CertificateType;
import cacert.shared.stubs.SignCertificateType;

public class AddAndGetBlockedListTestCase extends CacertWebServiceTestCase{
	
	public void testingValidAddAndValidGetBlockedList() {

		long oldSerial = -1l;
		CertificateType certificate = null;
		BlockCertificateType parameters2 = new BlockCertificateType();		
		SignCertificateType parameters = new SignCertificateType();
		CertificateSerialsListType list = null;
		
		try{
			
			parameters.setEntityName("TEST2");
			parameters.setPublicKey(this.publicKey);
			
			certificate = cacert.signCertificate(parameters);
						
			SecurityInfo.getInstance().setCertificate(certificate);

			//There is no need to generate a new pair of keys for test purposes

			parameters2.setEntityName(certificate.getSubject());
			parameters2.setOldSerial(certificate.getSerial());
			parameters2.setOldPublicKey(certificate.getPublicKey());
			parameters2.setNewPublicKey(this.publicKey);

			certificate = cacert.blockCertificate(parameters2);
			
			SecurityInfo.getInstance().setCertificate(certificate);

			new GetBlackList(cacert);
			
			list = cacert.getBlockedList();
			
			for(BlockedSerialCertificateType serial : list.getSerialsDTOList()){
				if(serial.getSerial() == parameters2.getOldSerial()){
					oldSerial = serial.getSerial();
				}
			}
			
			assertEquals(parameters2.getOldSerial(), oldSerial);
		
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("error");
		}
	}
	
	public void testingRevogateCertificata() {
		SignCertificateType parameters = new SignCertificateType();
		BlockCertificateType blockCertificate = new BlockCertificateType();
		CertificateType certificate;
		CertificateSerialsListType list = null;
		CertificateSerialsListType list2 = null;

		
		SecurityInfo infoSec = SecurityInfo.getInstance();
		
		parameters.setEntityName("OP1");
		parameters.setPublicKey(this.publicKey);
		
		try {
			
			certificate = cacert.signCertificate(parameters);

			//set certificate in SecurityInfo
			infoSec.setCertificate(certificate);
			
			new GetBlackList(cacert);
			
			list = cacert.getBlockedList();
			
			//Create a block Request
			blockCertificate.setOldSerial(certificate.getSerial());
			blockCertificate.setEntityName(certificate.getSubject());
			blockCertificate.setNewPublicKey(this.publicKey);
			blockCertificate.setOldPublicKey(certificate.getPublicKey());
			
			//Revogate the old certificate
			cacert.blockCertificate(blockCertificate);
			
			list2 = cacert.getBlockedList();
			
			assertTrue(list2==null);
			
		} catch (CertificateRemoteException e) {
			fail("error");
		}
	}

	
		
	
}
