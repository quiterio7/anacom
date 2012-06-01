package cacert.domain;

public class BlockedCertificate extends BlockedCertificate_Base {
    
	/**
	 * Constructor. For identifying a blocked certificate we need only to know 
	 * 				its serial.
	 * @param blockedSerial		the serial number that identifies the 
	 * 							certificate.
	 */
    public  BlockedCertificate(long blockedSerial) {
        super();
        this.setSerial(blockedSerial);
    }
    
}
