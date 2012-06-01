package anacom.shared.exceptions;

public class CommunicationError extends AnacomException {

	private static final long serialVersionUID = 1L;
	private String service;
	
	public CommunicationError() {}
	
	/**
	 * Constructor
	 * @param service	service where the error occurred
	 */	
	public CommunicationError(String service) {	
		this.service = service; 
	}
	
	/**
	 * @return	the name of the service where the error occurred
	 */
	public String getService() { 
		return this.service; 
	}
	
}
