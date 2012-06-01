package cacert.shared.dto;

import java.io.Serializable;

public class SignCertificateDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private final String entityName;
	private final String publicKey;	

	public SignCertificateDTO(String entityName, String publicKey) {
		super();
		this.entityName = entityName;
		this.publicKey = publicKey;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	
	public String getEntityName() {
		return this.entityName;
	}
}
