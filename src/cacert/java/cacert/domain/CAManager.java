package cacert.domain;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import cacert.shared.exceptions.CantCreateCertificateException;

public class CAManager extends CAManager_Base {
	
	/**
	 * Constructor. Initializes a serial number counter for identifying each
	 * certificate. It does not set the public and private key, that must be 
	 * done by calling the setKeys method.
	 */
    public  CAManager() {
        super();
        this.setCurrentSerial(1);
    }
    
    /**
     * Sets the public and private key of this Certificate Authority.
     * @param publicKey		the public key to be used by this CA
     * @param privateKey	the private key to be used by this CA
     */
    public void setKeys(String publicKey, String privateKey) {
    	this.setPublicKey(publicKey);
        this.setPrivateKey(privateKey);
    }
    
    /**
     * Creates a new certificate signed by this Certificate Authority
     * @param entityName				the entity requesting the certificate
     * @param publicKey					the public key of the entity requesting 
     * 									the certificate
     * @return							a new certificate signed by this 
     * 									Certificate Authority
     * @throws CantCreateCertificateException   	if there were problems on 
     * 												the generation of 
     * 												the certificate
     */
    public Certificate signCertificate(String entityName, String publicKey) 
    		throws CantCreateCertificateException {
    	return this.generateNewCertificate(entityName, publicKey);
    }
    
    
    /**
     * Adds the serial identified by oldSerial to the list of blocked serials 
     * (blocked certificates). Removes the old public key from the list of 
     * known server public keys and adds the new public key to it. Returns a 
     * new certificate for newPublicKey
     * @param oldSerial			the serial number of the certificate to block
     * @param newPublicKey		the public key of the new certificate
     * @return					a new certificate for newPublicKey
     * @throws CantCreateCertificateException	if there were problems on the 
     * 											generation of the certificate
     */
    public Certificate addToBlockedList(long oldSerial, String entityName, 
    		String newPublicKey) 
    		throws CantCreateCertificateException {
    	this.getBlockedCertificates().add(new BlockedCertificate(oldSerial));
		return this.generateNewCertificate(entityName, newPublicKey);
    }
    
    /**
     * Gets the list of blocked certificates in this CA
     * @return	the list with all the blocked certificates
     */
    public List<BlockedCertificate> getBlockedList() {
    	return this.getBlockedCertificates();
    }
    
    /**
     * Increments the serial number of the CAManager in one unit
     */
    private void incrementSerial() {
    	this.setCurrentSerial(this.getCurrentSerial()+1);
    }
  
    /**
     * Generates a new Certificate with CA signature 
     * @param publicKey		the publicKey of a specific entity
     * @return				the certificate signed with the private key of 
     * 						CA Manager
     * @throws CantCreateCertificateException	if there were problems on the 
     * 											generation of the certificate	
     */
    private Certificate generateNewCertificate(String entityName, String publicKey) 
    		throws CantCreateCertificateException {
    	
    	final String ISSUER_NAME = "Certificate-Authority";
    	final String KEY_USAGE = "SIGNATURE";
    	final String THUMBPRINT_ALGORITHM = "MD5";
    	final String SIGNATURE_ALGORITHM = "RSA/ECB/PKCS1Padding";
    	
    	Certificate certificate = null;
    	
    	try {
			certificate = new Certificate(this.getCurrentSerial(),
									entityName,
									ISSUER_NAME,
									KEY_USAGE,
									publicKey,
									this.getPrivateKey(),
									this.getValidPeriod(),
									THUMBPRINT_ALGORITHM,
									SIGNATURE_ALGORITHM);
			this.incrementSerial();
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Wrong algorithm: " + e.getMessage() + "(this " +
					"should never happen)");
		} catch (NoSuchPaddingException e) {
			System.err.println("Wrong algorithm: " + e.getMessage() + "(this " +
					"should never happen)");
		} catch (InvalidKeySpecException e) {
			System.err.println("Wrong algorithm: " + e.getMessage() + "(this " +
					"should never happen)");
		} catch (InvalidKeyException e) {
			throw new CantCreateCertificateException(e.getMessage());   
		} catch (BadPaddingException e) {
			throw new CantCreateCertificateException(e.getMessage());
		} catch (IllegalBlockSizeException e) {
			throw new CantCreateCertificateException(e.getMessage());
		} catch (IOException e) {
			throw new CantCreateCertificateException(e.getMessage());
		}
		return certificate;
    }
}
