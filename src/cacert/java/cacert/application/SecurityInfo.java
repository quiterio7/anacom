package cacert.application;

import java.util.ArrayList;
import java.util.List;

import cacert.shared.stubs.CertificateType;

public class SecurityInfo {
	
	private static SecurityInfo instance = null;
	
	private CertificateType certificate;
	private List<Long> blackList;
	private String thisEntityPublicKey;
	private String thisEntityPrivateKey;
	private String certificateAPublicKey;
	
	private SecurityInfo() {
		blackList = new ArrayList<Long>();
	}
	
	public static SecurityInfo getInstance() {
		if(instance == null) {
			instance = new SecurityInfo();
		}
		return instance;
	}

	public CertificateType getCertificate() {
		return certificate;
	}

	public void setCertificate(CertificateType certificate) {
		this.certificate = certificate;
	}
	
	public List<Long> getBlackList() {
		return blackList;
	}

	public void setBlackList(List<Long> blackList) {
		this.blackList = blackList;
	}

	public String getThisEntityPublicKey() {
		return thisEntityPublicKey;
	}

	public void setThisEntityPublicKey(String thisEntityPublicKey) {
		this.thisEntityPublicKey = thisEntityPublicKey;
	}

	public String getThisEntityPrivateKey() {
		return thisEntityPrivateKey;
	}

	public void setThisEntityPrivateKey(String thisEntityPrivateKey) {
		this.thisEntityPrivateKey = thisEntityPrivateKey;
	}

	public String getCertificateAPublicKey() {
		return certificateAPublicKey;
	}

	public void setCertificateAPublicKey(String certificateAPublicKey) {
		this.certificateAPublicKey = certificateAPublicKey;
	}

}
