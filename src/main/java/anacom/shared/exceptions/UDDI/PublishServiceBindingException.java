package anacom.shared.exceptions.UDDI;

public class PublishServiceBindingException extends PublishServiceException {

	private static final long serialVersionUID = 1L;
	private String bindingURL;
	
	/**
	 * @param organizationName 	the name of the organization
	 */
	public PublishServiceBindingException(String organizationName,
										String serviceName,
										String bindingURL) {
		super(organizationName, serviceName);
		this.bindingURL = bindingURL;
	}

	/**
	 * @return the service url
	 */
	public String getBindingURL() { 
		return this.bindingURL;
	}
}