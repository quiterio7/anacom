package anacom.shared.exceptions.UDDI;

//Attention - RemoveServiceBinding is not a kind of RemoveService
//I did the extension only by convenience of some common attributes and methods.
public class RemoveServiceBindingException extends RemoveServiceException {

	private static final long serialVersionUID = 1L;
	private String bindingURL;
	
	/**
	 * @param organizationName 	the name of the organization
	 */
	public RemoveServiceBindingException(String organizationName,
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