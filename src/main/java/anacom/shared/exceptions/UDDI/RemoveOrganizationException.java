package anacom.shared.exceptions.UDDI;

public class RemoveOrganizationException extends UDDIException {

	private static final long serialVersionUID = 1L;
	private String organizationName;
	
	/**
	 * @param organizationName 	the name of the organization
	 */
	public RemoveOrganizationException(String organizationName) {
		this.organizationName = organizationName;
	}
	
	/**
	 * @return the name of the organization
	 */
	public String getOrganizationName() { 
		return this.organizationName;
	}

}
