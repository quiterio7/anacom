package cacert.shared.exceptions;

public class CantCreateCertificateException extends CacertException {
	
	private static final long serialVersionUID = 9234832432848324L;
	private String _message;
	
	/**
	 * Empty Constructor
	 */
	public CantCreateCertificateException() {
		super();
	}
	
	/**
	 * Constructor
	 * @param message	the message of exception 
	 */
	public CantCreateCertificateException(String message) {
		this._message = message;
	}
	
	/**
	 * @return the message of exception
	 */
	public String getMessage() {
		return _message;
	}

}
