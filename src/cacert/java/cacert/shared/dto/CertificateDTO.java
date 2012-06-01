package cacert.shared.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

public class CertificateDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private long serial;
	private String subject;
	private String signatureAlgorithm;
	private String signature;
	private String issuer;
	private long validFrom;
	private long validTo;
	private String keyUsage;
	private String publicKey;
	private String thumbPrintAlgorithm;
	private String thumbPrint;

	public CertificateDTO(long serial, String subject,
			String signatureAlgorithm, String signature, String issuer,
			long validFrom, long validTo, String keyUsage, String publicKey,
			String thumbPrintAlgorithm, String thumbPrint) {
		this.serial = serial;
		this.subject = subject;
		this.signatureAlgorithm = signatureAlgorithm;
		this.signature = signature;
		this.issuer = issuer;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.keyUsage = keyUsage;
		this.publicKey = publicKey;
		this.thumbPrintAlgorithm = thumbPrintAlgorithm;
		this.thumbPrint = thumbPrint;
	}
	
	public long getSerial() {
		return serial;
	}

	public String getSubject() {
		return subject;
	}

	public String getSignatureAlgorithm() {
		return signatureAlgorithm;
	}

	public String getSignature() {
		return signature;
	}

	public String getIssuer() {
		return issuer;
	}

	public long getValidFrom() {
		return validFrom;
	}

	public long getValidTo() {
		return validTo;
	}

	public String getKeyUsage() {
		return keyUsage;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getThumbPrintAlgorithm() {
		return thumbPrintAlgorithm;
	}

	public String getThumbPrint() {
		return thumbPrint;
	}
}
