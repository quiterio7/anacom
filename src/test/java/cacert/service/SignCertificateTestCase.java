package cacert.service;

import java.security.NoSuchAlgorithmException;

import security.CryptoManager;
import cacert.services.SignCertificateService;
import cacert.shared.dto.CertificateDTO;
import cacert.shared.dto.SignCertificateDTO;
import cacert.shared.exceptions.CacertException;


//change in the future
public class SignCertificateTestCase extends CacertServiceTestCase {
	
	private final String ENTITY_NAME = "OP1";
	
	private final String SIGNATURE_ALGORITHM = "RSA/ECB/PKCS1Padding";
	
	private String pubKey;
	private String privKey;

	/**
	 * Constructor
	 * @param name	the name of the Test Case. For dynamic invocation.
	 */

	public SignCertificateTestCase(String msg) {
		super(msg);
		this.init();	
	}
	
	/**
	 * Empty Constructor
	 */
	public SignCertificateTestCase() {
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
	
	/**
	 * test a valid sign of certificate by CAManager
	 */
	public void testingValidSignCertificate() {
		
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
		
		assertEqualsCacert(cacert);
	}
	
	/**
	 * test an invalid sign of certificate by CAManager
	 */
	public void testingInvalidSignCertificate() {
		
		SignCertificateDTO signCacert = null;
		SignCertificateService service = null;
		CertificateDTO cacert = null;
		String [] keys = null;
		
		try {
			
			keys = CryptoManager.getInstance().generateKeys();
			signCacert = new SignCertificateDTO(ENTITY_NAME, keys[0]);
			service = new SignCertificateService(signCacert);
			
			service.execute();
			cacert = service.getCertificateDTO();
			
		} catch(CacertException e) {
			fail("this exception (CacertException) shouldn't been happen");
		} catch (NoSuchAlgorithmException e) {
			fail("this exception (NoSuchAlgorithmException) shouldn't been happen");
		}
		
		assertNotEqualsCacert(cacert);
	}

	private void assertEqualsCacert(CertificateDTO cacert) {
		CryptoManager manager = CryptoManager.getInstance();
		assertEquals("The keys should be the same", 
				this.pubKey, cacert.getPublicKey());
		assertEquals("The name of entity should be the same", 
				this.ENTITY_NAME, cacert.getSubject());
		try {
			assertEquals("The signature should be validated successfully", 
					true, manager.verifyDigitalSignature(cacert.getSignature(), 
							cacert.getThumbPrint(),
							getCacertPublicKey(),
							SIGNATURE_ALGORITHM));
		} catch (Exception e) {
			fail(e.getClass().getName());
		}
	}
	
	private void assertNotEqualsCacert(CertificateDTO cacert) {
		CryptoManager manager = CryptoManager.getInstance();
		assertNotSame("The keys should not be the same", 
				this.pubKey, cacert.getPublicKey());
		assertEquals("The name of entity should be the same", 
				this.ENTITY_NAME, cacert.getSubject());
		try {
			assertEquals("The signature should be validated successfully", 
					true, manager.verifyDigitalSignature(cacert.getSignature(), 
							cacert.getThumbPrint(),
							getCacertPublicKey(),
							SIGNATURE_ALGORITHM));
		} catch (Exception e) {
			fail(e.getClass().getName());
		}
	}

}
