package cacert.shared.dto;

import java.io.Serializable;

public class SetKeysDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String publicKey;
	private String privateKey;
	
	public SetKeysDTO(String publicKey, String privateKey) {
		super();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
}
