package anacom.applicationserver;

import java.util.ArrayList;
import java.util.List;

import security.CryptoManager;
import cacert.shared.stubs.CertificateType;

public class SecurityInfo {
	
	private static SecurityInfo instance = null;
	
	/**
	 * Security Data
	 */
	private CertificateType certificate;
	private String thisEntityPublicKey;
	private String thisEntityPrivateKey;
	private String certificateAPublicKey;
	private List<Long> blackList;
	
	/**
	 * Empty Constructor
	 */
	private SecurityInfo() {
		blackList = new ArrayList<Long>();
		try {
			this.certificateAPublicKey = 
		CryptoManager.getInstance().getPublicRSAKeyfromFile("RSAkeys/CAkeys");
		} catch(Exception e) {
			System.out.println("Error while reading the RSAKey");
		}
	}
	
	/**
	 * Singleton Rules :D
	 * @return	A lonely object
	 */
	public static SecurityInfo getInstance() {
		if(instance == null) {
			instance = new SecurityInfo();
		}
		return instance;
	}

	/**
	 * Get Certificate
	 * @return returns the current certificate in use
	 */
	public CertificateType getCertificate() {
		return certificate;
	}

	/**
	 * Set Certificate
	 * @param certificate	set the current certificate for the new one
	 */
	public void setCertificate(CertificateType certificate) {
		this.certificate = certificate;
	}
	
	/*
	 * Get the public key of a specific server
	 */
	public String getThisEntityPublicKey() {
		return thisEntityPublicKey;
	}
	
	/**
	 * setThisEntityPublicKey : set a public key of a entity server
	 * @param thisEntityPublicKey
	 */
	public void setThisEntityPublicKey(String thisEntityPublicKey) {
		this.thisEntityPublicKey = thisEntityPublicKey;
	}
	
	/**
	 * Get the black list of a CA
	 * @return return a blackList with all the denied certificate 
	 * serials 
	 * 
	 */
	public List<Long> getBlackList() {
		return blackList;
	}

	/**
	 * Set the black list of a CA
	 * @param blackList
	 */
	public void setBlackList(List<Long> blackList) {
		this.blackList = blackList;
	}

	/**
	 * Get the Entity Private Key
	 * @return	the private key of a specific entity 
	 */
	public String getThisEntityPrivateKey() {
		return thisEntityPrivateKey;
	}

	/**
	 * Set the Entity Private Key
	 * @param thisEntityPrivateKey
	 */
	public void setThisEntityPrivateKey(String thisEntityPrivateKey) {
		this.thisEntityPrivateKey = thisEntityPrivateKey;
	}

	/**
	 * getCertificateAPublicKey
	 * @return Public Key
	 */
	public String getCertificateAPublicKey() {
		return certificateAPublicKey;
	}

	/**
	 * setCertificateAPublicKey
	 * @param Public Key 
	 */
	public void setCertificateAPublicKey(String certificateAPublicKey) {
		this.certificateAPublicKey = certificateAPublicKey;
	}

}
