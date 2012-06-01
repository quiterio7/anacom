package anacom.shared.exceptions.UDDI;

public class PublishServiceException extends PublishOrganizationException {

	private static final long serialVersionUID = 1L;
	private String serviceName;
	
	/**
	 * @param organizationName 	the name of the organization
	 */
	public PublishServiceException(String organizationName, String serviceName) {
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
