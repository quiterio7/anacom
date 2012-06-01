package anacom.shared.misc;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;

/**
 * This class is a Singleton factory for creating SOAP header or body field
 * Name objects.
 */
public class SOAPFields {
	
	private static SOAPFields instance = null;

	public static final String NAMESPACE_PREFIX = "tns";
	public static final String NAMESPACE_URI = "http://anacom";
	
	public final static int INIT_PACKET_NUMBER = 0;
	
	private final static String PACKET_NUMBER_ENTRY = "packetnumber";
	
	/**
	 * Security Conventions
	 */
	
	public final static String SERIAL = "serial";
	public final static String SUBJECT = "subject";
	public final static String ISSUER = "issuer";
	public final static String KEY_USAGE = "keyUsage";
	public final static String PUB_KEY = "publicKey";
	public final static String CA_SIGNATURE = "CASignature";
	public final static String VALID_FROM = "validFrom";
	public final static String VALID_TO = "validTo";
	public final static String THUMB_P_ALGO = "thumbPrintAlgorithm";
	public final static String THUMB_PRINT = "thumbPrint";
	public final static String ALGO_SIGNATURE = "signatureAlgorithm";
	
	/**
	 * The name of the SOAP Action entry in the HTTP header of the SOAP message.
	 */
	public final static String SOAP_ACTION = "SOAPAction";
	
	/**
	 * Represents the soapAction content in a SOAP Message that may alter the
	 * server's database state.
	 */
	public final static String WRITE_SOAP_ACTION = "write";
	
	/**
	 * Represents the soapAction content in a SOAP Message that just reads data
	 * from the database without altering its state.
	 */
	public final static String READ_SOAP_ACTION = "read";
	
	private SOAPFields() {}
	
	public static SOAPFields getInstance() {
		if (instance == null) { 
			instance = new SOAPFields(); 
		}
		return instance;
	}
	
	/**
	 * Creates a name object for the Packet Number header entry.
	 * @param env the SOAPEnvelope object that will create the name
	 * @return the Name object
	 * @throws SOAPException if there is a SOAP error creating the name
	 */
	public Name getPacketNumber(SOAPEnvelope env) throws SOAPException {
		return env.createName(
				PACKET_NUMBER_ENTRY,
				NAMESPACE_PREFIX,
				NAMESPACE_URI);
	}
	
}
