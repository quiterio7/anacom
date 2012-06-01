package anacom.shared.UDDI;

import javax.xml.registry.JAXRException;

import anacom.shared.exceptions.UDDI.RemoveOrganizationException;
import anacom.shared.exceptions.UDDI.RemoveServiceBindingException;
import anacom.shared.exceptions.UDDI.RemoveServiceException;

public class UDDIRemove extends UDDIConnection {

	/**
	 * Constructor.
	 * This method causes the creation of an UDDI connection.
	 * @param username			the user name for the UDDI connection.
	 * @param password			the password for the UDDI connection.
	 * @param xmlUDDI			the UDDI configuration file path.
	 * @throws JAXRException	raised when some problem occurs while 
	 * 							communicating with UDDI server.
	 */
	public UDDIRemove(String username, String password, String xmlUDDI) 
			throws JAXRException {
		super(username, password, xmlUDDI);
	}
	
	/**
	 * Remove organization with name.
	 * @param organizationName					the organization name.
	 * @throws JAXRException					raised when some problem occurs while 
	 * 											communicating with UDDI server.
	 * @throws RemoveOrganizationException		if the UDDI response is negative.
	 */
	public void Remove(String organizationName) 
			throws JAXRException, RemoveOrganizationException { 
		super.removeOrganization(organizationName); 
	}
	
	/**
	 * Remove service with name.
	 * @param organizationName					the organization name.
	 * @param serviceName						the service name.
	 * @param serviceDescription				the service description.
	 * @throws JAXRException					raised when some problem occurs while 
	 * 											communicating with UDDI server.
	 * @throws RemoveServiceException			if the UDDI response is negative.
	 */
	public void Remove(String organizationName, String serviceName) 
			throws JAXRException, RemoveServiceException {
		super.removeService(organizationName, serviceName);
	}
	
	/**
	 * Remove service binding with URL.
	 * @param organizationName					the organization name.
	 * @param serviceName						the service name.
	 * @param serviceDescription				the service description
	 * @param bindingURL						the URL.
	 * @throws JAXRException					raised when some problem occurs while 
	 * 											communicating with UDDI server.
	 * @throws RemoveServiceBindingException	if the UDDI response is negative.
	 */
	public void Remove(
			String organizationName, 
			String serviceName, 
			String bindingURL) 
					throws JAXRException, RemoveServiceBindingException {
		super.removeBinding(organizationName, serviceName, bindingURL);
	}
	
}
