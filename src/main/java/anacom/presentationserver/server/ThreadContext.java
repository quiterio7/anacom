package anacom.presentationserver.server;

import javax.xml.datatype.XMLGregorianCalendar;

import cacert.shared.stubs.CertificateType;

import anacom.shared.misc.Constants;

/**
 * This class encapsulates the presentation server's thread local variables.
 */
public class ThreadContext {

	private static ThreadContext instance = null;
	
	public static ThreadContext getInstance() {
		if (instance == null) { instance = new ThreadContext(); }
		return instance;
	}

	/**
	 * This attribute stores the prefix of the Operator with which the
	 * presentation server is communicating at the moment.
	 */
	private ThreadLocal<String> currentPrefix = null;
	
	private ThreadLocal<XMLGregorianCalendar> currentRequestTimestamp = null;
	
	private ThreadLocal<Boolean> messageIsAWrite = null;
	
	private ThreadLocal<CertificateType> certificate = null;
	
	/**
	 * Creates thread local variables.
	 */
	private ThreadContext() {
		this.currentPrefix = new ThreadLocal<String>() {
			@Override
			protected String initialValue() {
				return Constants.NULL_OPERATOR_PREFIX;
			}
		};
		this.currentRequestTimestamp = new ThreadLocal<XMLGregorianCalendar>() {
			@Override
			protected XMLGregorianCalendar initialValue() {
				return null;
			}
		};
		this.messageIsAWrite = new ThreadLocal<Boolean>() {
			@Override
			protected Boolean initialValue() {
				return new Boolean(false);
			}
		};
		this.certificate = new ThreadLocal<CertificateType>() {
			@Override
			protected CertificateType initialValue() {
				return new CertificateType();
			}
		};
	}
	
	/**
	 * @return the prefix of the Operator with which the presentation server is
	 * communicating at the moment
	 */
	public String getCurrentOperatorPrefix() {
		return this.currentPrefix.get();
	}
	
	/**
	 * Sets the current Operator prefix.
	 * @param prefix
	 * 		the prefix of the Operator with which the presentation server is
	 * 		communicating at the moment
	 */
	public void setCurrentOperatorPrefix(String prefix) {
		this.currentPrefix.set(prefix);
	}
	
	/**
	 * @return the timestamp of the current request being sent
	 */
	public XMLGregorianCalendar getCurrentRequestTimestamp() {
		return this.currentRequestTimestamp.get();
	}
	
	/**
	 * Sets the current request timestamp.
	 * @param timestamp
	 * 		the timestamp of the current request being sent by the server
	 */
	public void setCurrentRequestTimestamp(XMLGregorianCalendar timestamp) {
		this.currentRequestTimestamp.set(timestamp);
	}

	/**
	 * @return true if the current message being sent is a write, false
	 * otherwise
	 */
	public boolean currentMessageIsWrite() {
		return this.messageIsAWrite.get().booleanValue();
	}
	
	public boolean currentMessageIsRead() {
		return !this.messageIsAWrite.get().booleanValue();
	}
	
	public void setCurrentMessageToWrite() {
		this.messageIsAWrite.set(new Boolean(true));
	}
	
	public void setCurrentMessageToRead() {
		this.messageIsAWrite.set(new Boolean(false));
	}
	
	public CertificateType getCertificate() {
		return this.certificate.get();
	}
	
	public void setCertificate(CertificateType certificate) {
		this.certificate.set(certificate);
	}
}
