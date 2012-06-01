package security;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CryptoManager {
	
	private final String RSA_ALGORITHM = "RSA";
	
	private static CryptoManager manager = null;
	private CryptoManager() {}
	
	/**
	 * Singleton getInstance
	 * @return	an instance of CryptoManager
	 */
	public static CryptoManager getInstance() {
		if(manager == null) {
			manager = new CryptoManager();
		}
		return manager;
	}
	
	/**
	 * Gets current date in milliseconds
	 * @return current date in milliseconds
	 */
	public long getDateInMillis() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		return calendar.getTimeInMillis();
	}
	
	/**
	 * Gets the public key from a 64bit string.
	 * @param 	pubKey	64bit string
	 * @return	public 	key
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PublicKey getPubKeyFromString(String pubKey, String algorithm) 
			throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		KeyFactory keyFact = KeyFactory.getInstance(algorithm);
		byte[] pubKey_bytes = this.decode64(pubKey);
		return keyFact.generatePublic(new X509EncodedKeySpec(pubKey_bytes));		
	}
	
	/**
	 * Gets the private key from a 64bit string.
	 * @param 	privKey	64bit string
	 * @return	public 	key
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey getPrivKeyFromString(String privKey_str, String algorithm) 
			throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		byte[] privKey_bytes = this.decode64(privKey_str);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privKey_bytes));
	}
	
	/**
	 * getPublicRSAKeyfromFile
	 * @param path
	 * @return	String with the public key
	 * @throws FileNotFoundException
	 */
	public String getPublicRSAKeyfromFile(String path) 
			throws FileNotFoundException {
		try {
			String newPath = System.getProperty("user.dir") + "/" + path + "/" +
						"pub.key";
			FileInputStream fin = 
					new FileInputStream(newPath);
	        byte[] pubEncoded = new byte[ fin.available() ];
	        fin.read(pubEncoded);
	        fin.close();

	        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubEncoded);
	        KeyFactory keyFacPub = KeyFactory.getInstance(RSA_ALGORITHM);

	        PublicKey pub = keyFacPub.generatePublic(pubSpec);
	        
	        byte[] publicEncoded = pub.getEncoded();
	        return this.encode64(publicEncoded);
	        
		} catch(Exception e) {
			//FIXME: change the throw exception
			System.out.println(e.getClass().getName());
			throw new FileNotFoundException(path);
		}
	}
	
	/**
	 * getPrivateRSAKeyfromFile
	 * @param path
	 * @return	String with the private key
	 * @throws FileNotFoundException
	 */
	public String getPrivateRSAKeyfromFile(String path) 
			throws FileNotFoundException {
		
		try {
			String newPath = System.getProperty("user.dir") + "/" + path + 
							 "/priv.key";
			FileInputStream fin = 
					new FileInputStream(newPath);
	        byte[] privEncoded = new byte[ fin.available() ];
	        fin.read(privEncoded);
	        fin.close();

	        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privEncoded);
	        KeyFactory keyFacPriv = KeyFactory.getInstance(RSA_ALGORITHM);
	        PrivateKey priv = keyFacPriv.generatePrivate(privSpec);
	        
	        byte[] privateEncoded = priv.getEncoded();
	        return this.encode64(privateEncoded);
	        
		} catch(Exception e) {
			//FIXME: change the throw exception
			System.out.println(e.getClass().getName());
			throw new FileNotFoundException(path);
		}
	}
	
	/**
	 * Converts a byte array to 64bit String
	 * @param	byte array to be converted
	 * @return 			the string encoded with BASE64
	 * @throws 			IOException 
	 */
	public String encode64(byte[] array) {
        return new sun.misc.BASE64Encoder().encode(array);
	}
	
	/**
	 * Converts a 64bit string to byte array
	 * @param	param	string to be converted
	 * @return 			byte array
	 * @throws 			IOException 
	 */
	public byte[] decode64(String param) throws IOException {
        return new sun.misc.BASE64Decoder().decodeBuffer(param);
	}
	
	/**
	 * Make a thumb Print
	 * @param text
	 * @param thumbPrintAlgorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException - Invalid Convert of a byte array to 64bit String
	 */
	public String makeThumbPrint(String text, String thumbPrintAlgorithm) 
		throws NoSuchAlgorithmException, IOException{
		
		MessageDigest messageDigest = MessageDigest.getInstance(thumbPrintAlgorithm);

        byte[] textConverted = this.decode64(text);
        // calculate the digest and print it out
        messageDigest.update(textConverted);
        byte[] digest = messageDigest.digest();
        return this.encode64(digest);
	}

    /**
     * Makes a Digital Signature
     * @param privateKeyString - private Key in string format
     * @param thumbPrint	- the digest message
     * @return	the digital signature
     * @throws NoSuchAlgorithmException - 
     * 		if transformation is null, empty, in an invalid format, or if no 
     * 		Provider supports a CipherSpi implementation for the specified 
     * 		algorithm.
	 * @throws NoSuchPaddingException - 
	 * 		if transformation contains a padding scheme that is not available.
     * @throws IOException - Invalid Convert of a byte array to 64bit String
     * @throws InvalidKeyException - 
     * 		if the given key is inappropriate for initializing this cipher, or 
     * 		if this cipher is being initialized for decryption and requires 
     * 		algorithm parameters that cannot be determined from the given key, 
     * 		or if the given key has a keysize that exceeds the maximum allowable 
     * 		keysize (as determined from the configured jurisdiction policy files).
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public String makeDigitalSignature(String privateKeyString, 
    		String thumbPrint, String cipherAlgorithm) 
    	throws NoSuchAlgorithmException, NoSuchPaddingException, IOException,
    			InvalidKeySpecException, InvalidKeyException, BadPaddingException,
    			IllegalBlockSizeException{
    	

    	PrivateKey privateKey;
    	privateKey = this.getPrivKeyFromString(privateKeyString, RSA_ALGORITHM);
    	
        // get an RSA cipher object
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return this.encode64(cipher.doFinal(this.decode64(thumbPrint)));
    }
    
    /**
     * Verify a Signature and returns true if it is right 
     * @param privateKeyString - private Key in string format
     * @param thumbPrint	- the digest message
     * @return	the digital signature
     * @throws NoSuchAlgorithmException - 
     * 		if transformation is null, empty, in an invalid format, or if no 
     * 		Provider supports a CipherSpi implementation for the specified 
     * 		algorithm.
	 * @throws NoSuchPaddingException - 
	 * 		if transformation contains a padding scheme that is not available.
     * @throws IOException - Invalid Convert of a byte array to 64bit String
     * @throws InvalidKeyException - 
     * 		if the given key is inappropriate for initializing this cipher, or 
     * 		if this cipher is being initialized for decryption and requires 
     * 		algorithm parameters that cannot be determined from the given key, 
     * 		or if the given key has a keysize that exceeds the maximum allowable 
     * 		keysize (as determined from the configured jurisdiction policy files).
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public boolean verifyDigitalSignature(	String signature, 
    										String thumbPrint, 
    										String publicKey,
    										String cipherAlgorithm) 
    									throws NoSuchAlgorithmException, 
    									IOException, 
    									NoSuchPaddingException, 
    									InvalidKeyException, 
    									InvalidKeySpecException, 
    									IllegalBlockSizeException, 
    									BadPaddingException {

    	Cipher cipher = Cipher.getInstance(cipherAlgorithm);

    	cipher.init(Cipher.DECRYPT_MODE, this.getPubKeyFromString(publicKey, RSA_ALGORITHM));

    	String decodedText = 
    			this.encode64(cipher.doFinal(this.decode64(signature)));

    	if(decodedText.equals(thumbPrint)) {
    		return true;
    	}
    	return false;
    }
    
    
    /**
     * Generate a Pair of Keys
     * @return	a String array which contains in first position s[0] a public key
     * 	and in second position a private key s[1]
     * @throws NoSuchAlgorithmException - if no Provider supports a 
     * KeyPairGeneratorSpi implementation for the specified algorithm.
     */
    public String[] generateKeys() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		keyGen.initialize(1024);

		KeyPair key = keyGen.generateKeyPair();
		
		byte[] pubEncoded = key.getPublic().getEncoded();
		byte[] privEncoded = key.getPrivate().getEncoded();
		
		String publicKeyString64 = this.encode64(pubEncoded);
		String privatekeyString64 = this.encode64(privEncoded);
		
		String[] keys = new String[2];
		keys[0] = publicKeyString64;
		keys[1] = privatekeyString64;
		
		return keys;
	}
}
