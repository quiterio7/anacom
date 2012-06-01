package cacert.domain;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import security.CryptoManager;

/**
 * A certificate has the following parameters, according to the X509 standard
 * serial 					uniquely identifies the certificate 
 * subject 					the person or entity identified
 * issuer					the entity that verified the information 
 * 							and issued the certificate
 * keyUsage					purpose of the public key (e.g signature)
 * publicKey				the public key of Subject
 * CAsignature				the actual signature to verify that it came 
 * 							from the issuer
 * validFrom				the date the certificate is first valid from
 * validTo				 	the expiration date
 * thumbPrintAlgorithm 	 	the algorithm used to hash the public key
 * thumbPrint 			 	the hash itself, used as an abbreviated form of 
 * 						 	the public key
 * CASignatureAlgorithm		the algorithm used to create the signature
 */
public class Certificate extends Certificate_Base {

	/**
	 * Constructor.
	 * @param serial						uniquely identifies the certificate
	 * @param subject						the person or entity identified
	 * @param issuer						the entity that verified the 
	 * 										information and issued the 
	 * 										certificate
	 * @param keyUsage						purpose of the public key (e.g 
	 * 										signature)
	 * @param publicKey						the public key of subject
	 * @param issuerPrivateKey				the private key of the entity of
	 * 										certification
	 * @param validPeriod					the period of time when the 
	 * 										certificate should be valid, 
	 * 										starting from the current time (in
	 * 										milliseconds)
	 * @param thumbPrintAlgorithm			the algorithm for the creation of 
	 * 										the thumbPrint
	 * @param signatureAlgorithm			the algorithm for the creation of
	 * 										the signature
	 * @throws NoSuchAlgorithmException		if one of the algorithms is not
	 * 										correctly specified
	 * @throws IOException					if one of the keys is not 
	 * 										correctly specified
	 * @throws InvalidKeyException			if one of the keys is not
	 * 										correctly specified
	 * @throws NoSuchPaddingException		if one of the algorithms is not
	 * 										correctly specified
	 * @throws InvalidKeySpecException		if one of the keys is not 
	 * 										correctly specified
	 * @throws BadPaddingException			if one of the keys is not 
	 * 										correctly specified
	 * @throws IllegalBlockSizeException	if one of the keys is not 
	 * 										correctly specified
	 */
    public Certificate(long serial, String subject, String issuer,
    		String keyUsage, String publicKey, String issuerPrivateKey,
    		long validPeriod, String thumbPrintAlgorithm,
    		String signatureAlgorithm) 
    				throws NoSuchAlgorithmException, 
    				IOException, InvalidKeyException, NoSuchPaddingException, 
    				InvalidKeySpecException, BadPaddingException, 
    				IllegalBlockSizeException {
    	super();
    	
    	CryptoManager manager = CryptoManager.getInstance();
    	
    	this.setSerial(serial);
    	this.setSubject(subject);
    	this.setIssuer(issuer);
    	this.setKeyUsage(keyUsage);
    	this.setPublicKey(publicKey);
    	this.setValidFrom(new Date().getTime());
    	this.setValidTo(new Date().getTime() + validPeriod);
    	this.setThumbPrintAlgorithm(thumbPrintAlgorithm);
    	this.setSignatureAlgorithm(signatureAlgorithm);
    	
		String thumbPrint = manager.makeThumbPrint(
				this.getUniqueStringRepresentation(), 
				thumbPrintAlgorithm);
		this.setThumbPrint(thumbPrint);
		
		String signature = manager.makeDigitalSignature(
				issuerPrivateKey,
				this.getThumbPrint(),
				signatureAlgorithm);
		this.setSignature(signature);    	
    }
    
    /**
     * Gets a unique string representation of this certificate to be used in
     * the thumbPrint
     * @return	a string representing this certificate
     */
    private String getUniqueStringRepresentation() {
    	return this.getSerial() + this.getSubject() + this.getIssuer() +
    			this.getKeyUsage() + this.getPublicKey() + 
    			this.getValidFrom() + this.getValidTo() + 
    			this.getThumbPrintAlgorithm() + this.getSignatureAlgorithm();
    }
}