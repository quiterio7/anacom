package anacom.shared.UDDI;

import javax.xml.registry.JAXRException;

import anacom.shared.exceptions.UDDI.PublishOrganizationException;
import anacom.shared.exceptions.UDDI.PublishServiceBindingException;
import anacom.shared.exceptions.UDDI.PublishServiceException;

public class UDDIPublish extends UDDIConnection {

	/**
	 * Constructor.
	 * This method causes the creation of an UDDI connection.
	 * @param username			the user name for the UDDI connection.
	 * @param password			the password for the UDDI connection.
	 * @param xmlUDDI			the UDDI configuration file path.
	 * @throws JAXRException	raised when some problem occurs while 
	 * 							communicating with UDDI server.
	 */
	public UDDIPublish(String username, String password, String xmlUDDI)
			throws JAXRException {
		super(username, password, xmlUDDI);
	}
	
	/**
	 * Publish organization with name.
	 * @param organizationName					the organization name.
	 * @throws JAXRException					raised when some problem occurs while 
	 * 											communicating with UDDI server.
	 * @throws PublishOrganizationException		if the UDDI response is negative.
	 */
	public void publish(String organizationName) 
			throws JAXRException, PublishOrganizationException { 
		super.publishOrganization(organizationName); 
	}
	
	/**
	 * Publish service with name.
	 * This method may cause the organization publish.
	 * @param organizationName					the organization name.
	 * @param serviceName						the service name.
	 * @param serviceDescription				the service description.
	 * @throws JAXRException					raised when some problem occurs while 
	 * 											communicating with UDDI server.
	 * @throws PublishServiceException			if the UDDI response is negative.
	 */
	public void publish(
			String organizationName, 
			String serviceName, 
			String serviceDescription) 
					throws JAXRException, PublishServiceException {
		super.publishService(organizationName, serviceName, serviceDescription);
	}
	
	/**
	 * Publish service binding with URL.
	 * This method may cause the organization and/or service publish.
	 * @param organizationName					the organization name.
	 * @param serviceName						the service name.
	 * @param serviceDescription				the service description
	 * @param bindingURL						the URL.
	 * @throws JAXRException					raised when some problem occurs while 
	 * 											communicating with UDDI server.
	 * @throws PublishServiceBindingException	if the UDDI response is negative.
	 */
	public void publish(
			String organizationName, 
			String serviceName,
			String serviceDescription,
			String bindingURL) 
					throws JAXRException, PublishServiceBindingException {
		super.publishBinding(
				organizationName, serviceName, serviceDescription, bindingURL);
	}
	
}
