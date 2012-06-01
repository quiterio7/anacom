package cacert.shared.dto;

import java.io.Serializable;

public class BlockCertificateDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private long oldSerial;
	private String entityName;
	private String oldPublicKey;
	private String newPublicKey;
	
	public BlockCertificateDTO(long oldSerial, String entityName,
			String oldPublicKey, String newPublicKey) {
		super();
		this.oldSerial = oldSerial;
		this.entityName = entityName;
		this.oldPublicKey = oldPublicKey;
		this.newPublicKey = newPublicKey;
	}
	
	public long getOldSerial() {
		return oldSerial;
	}
	
	public String getEntityName() {
		return entityName;
	}
	
	public String getOldPublicKey() {
		return oldPublicKey;
	}
	
	public String getNewPublicKey() {
		return newPublicKey;
	}
}
