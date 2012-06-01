package anacom.shared.exceptions.UDDI;

// Attention - RemoveService is not a kind of RemoveOrganization
// I did the extension only by convenience of some common attributes and methods.
public class RemoveServiceException extends RemoveOrganizationException {

	private static final long serialVersionUID = 1L;
	private String serviceName;
	
	/**
	 * @param organizationName 	the name of the organization
	 */
	public RemoveServiceException(String organizationName, String serviceName) {
		super(organizationName);
		this.serviceName = serviceName;
	}
	
	/**
	 * @return the name of the service
	 */
	public String getServiceName() { 
		return this.serviceName;
	}

}
