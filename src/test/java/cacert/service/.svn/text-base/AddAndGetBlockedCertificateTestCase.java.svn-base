package cacert.service;

import java.security.NoSuchAlgorithmException;

import security.CryptoManager;
import cacert.services.AddToBlockedListService;
import cacert.services.GetBlockedListService;
import cacert.services.SignCertificateService;
import cacert.shared.dto.BlockCertificateDTO;
import cacert.shared.dto.BlockedSerialDTO;
import cacert.shared.dto.CertificateDTO;
import cacert.shared.dto.CertificatesSerialListDTO;
import cacert.shared.dto.SignCertificateDTO;
import cacert.shared.exceptions.CacertException;

public class AddAndGetBlockedCertificateTestCase extends CacertServiceTestCase {
	
	private final String ENTITY_NAME = "OP1";
	
	private String pubKey;
	private String privKey;

	/**
	 * Constructor
	 * @param name	the name of the Test Case. For dynamic invocation.
	 */

	public AddAndGetBlockedCertificateTestCase(String msg) {
		super(msg);
		this.init();	
	}
	
	/**
	 * Empty Constructor
	 */
	public AddAndGetBlockedCertificateTestCase() {
		super();
		this.init();
	}
	
	
	
	public void init() {
		String [] keys = null;
		try {
			keys = CryptoManager.getInstance().generateKeys();
		} catch (NoSuchAlgorithmException e) {
			fail("error creating SignCertificateTestCase in generation of " +
					" asym keys");
		}
		this.pubKey = keys[0];
		this.privKey = keys[1];
	}
	
	public CertificateDTO signCertificate() {
		
		SignCertificateDTO signCacert = 
				new SignCertificateDTO(ENTITY_NAME, this.pubKey);
		SignCertificateService service = new SignCertificateService(signCacert);
		CertificateDTO cacert = null;
		
		try {
			service.execute();
			cacert = service.getCertificateDTO();
		} catch(CacertException e) {
			fail("this exception (CacertException) shouldn't been happen");
		}
		
		return cacert;
	}
	

	public void testValidAddAndGetBlockedList(){
		
		CertificateDTO oldcertificate = this.signCertificate();
		
		long oldSerial = oldcertificate.getSerial();
		
		this.init();
		
		BlockCertificateDTO dto = new BlockCertificateDTO(oldcertificate.getSerial(), 
				oldcertificate.getSubject(), 
				oldcertificate.getPublicKey(), this.pubKey);
		
		AddToBlockedListService service = new AddToBlockedListService(dto);
		service.execute();
		
		CertificateDTO newcertificate = service.getCertificateDTO();
		
		GetBlockedListService service2 = new GetBlockedListService();
		service2.execute();
		
		CertificatesSerialListDTO list = service2.getCertificatesList();
		
		assertEqualsList(list, oldSerial);
		
	}
	
	public void testEmptyBlockedList(){
		
		this.setUp();
		this.init();
		this.signCertificate();
		
		GetBlockedListService service = new GetBlockedListService();
		service.execute();
		
		CertificatesSerialListDTO list = service.getCertificatesList();
		
		assertEmptyList(list);
		
	}
	
	private void assertEqualsList(CertificatesSerialListDTO list, long oldSerial) {
		
		assertEquals("The size of blocked should be 1", 
				list.getCertificatesList().size(), 1);		
		
		BlockedSerialDTO blocked = list.getCertificatesList().get(0);

		assertEquals("The serials should be the same", 
				blocked.getSerial() , oldSerial);
		
	}
	
	private void assertEmptyList(CertificatesSerialListDTO list) {
		
		assertEquals("The size of blocked should be 0", 
				list.getCertificatesList().size(), 0);		
	
	}

}
