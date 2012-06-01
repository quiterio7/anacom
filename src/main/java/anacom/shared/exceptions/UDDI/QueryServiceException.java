package anacom.shared.exceptions.UDDI;

public class QueryServiceException extends UDDIException {

	private static final long serialVersionUID = 1L;
	private String organizationName;
	private String serviceName;
	
	/**
	 * @param organizationName 	the name of the organization
	 */
	public QueryServiceException(String organizationName, String serviceName) {
		this.organizationName = organizationName;
		this.serviceName = serviceName;
	}
	
	/**
	 * @return the name of the organization
	 */
	public String getOrganizationName() { 
		return this.organizationName;
	}
	
	/**
	 * @return the name of the service
	 */
	public String getServiceName() { 
		return this.serviceName;
	}

}
