package cacert.shared.dto;

import java.io.Serializable;

public class BlockedSerialDTO implements Serializable {

	private static final long serialVersionUID = 58374247242L;
	
	private long serial;
	
	public BlockedSerialDTO(long serial) {
		this.serial = serial;
	}
	
	public long getSerial() {
		return this.serial;
	}

}
