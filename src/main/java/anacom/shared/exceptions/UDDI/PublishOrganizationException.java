package anacom.shared.exceptions.UDDI;

public class PublishOrganizationException extends UDDIException {

	private static final long serialVersionUID = 1L;
	private String organizationName;
	
	/**
	 * @param organizationName 	the name of the organization
	 */
	public PublishOrganizationException(String organizationName) {
		this.organizationName = organizationName;
	}
	
	/**
	 * @return the name of the organization
	 */
	public String getOrganizationName() { 
		return this.organizationName;
	}

}
