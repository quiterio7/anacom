package anacom.shared.UDDI;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.FindQualifier;
import javax.xml.registry.JAXRException;
import javax.xml.registry.JAXRResponse;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.Service;
import javax.xml.registry.infomodel.ServiceBinding;

import anacom.shared.exceptions.UDDI.PublishOrganizationException;
import anacom.shared.exceptions.UDDI.PublishServiceBindingException;
import anacom.shared.exceptions.UDDI.PublishServiceException;
import anacom.shared.exceptions.UDDI.RemoveOrganizationException;
import anacom.shared.exceptions.UDDI.RemoveServiceBindingException;
import anacom.shared.exceptions.UDDI.RemoveServiceException;

/**
 * Abstract class that contains generic methods that will be used by other
 * classes. 
 * Several methods within this class return null. This is made to keep some
 * consistency with the JAXR API.
 * Classes that use this methods should be aware of the null returning and 
 * use exceptions to the other application classes.
 */
public abstract class UDDIConnection {

	private final String username;
	private final String password;
	private final String xmlUDDI;
	private final Connection connection;
	
	/**
	 * Constructor.
	 * @param username		the user name for logging in the UDDI server.
	 * @param password		the password for logging in the UDDI server.
	 * @param xmlUDDI		the path for the UDDI configuration file.
	 * @throws JAXRException	raised when some problem occurs while 
	 * 							communicating with UDDI server.
	 */
	protected UDDIConnection(String username, String password, String xmlUDDI) 
			throws JAXRException {
		this.username = username;
		this.password = password;
		this.xmlUDDI = xmlUDDI;
		this.connection = connect();
	}
	
	/**
	 * This constructor should be used only for testing!
	 */
	protected UDDIConnection() {
		this.username = null;
		this.password = null;
		this.xmlUDDI = null;
		this.connection = null;
	}
	
	/**
	 * Handles the UDDI server connection.
	 * @return						the connection object.
	 * @throws JAXRException		raised when some problem occurs while 
	 * 								communicating with UDDI server.
	 */
	private Connection connect() throws JAXRException {
		ConnectionFactory connFactory =
			org.apache.ws.scout.registry.ConnectionFactoryImpl.newInstance();

		Properties props = new Properties();
		props.setProperty("scout.juddi.client.config.file", 
						this.xmlUDDI);
		props.setProperty("javax.xml.registry.queryManagerURL", 
						"http://localhost:8081/juddiv3/services/inquiry");
		props.setProperty("javax.xml.registry.lifeCycleManagerURL", 
						"http://localhost:8081/juddiv3/services/publish");
		props.setProperty("javax.xml.registry.securityManagerURL", 
						"http://localhost:8081/juddiv3/services/security");
		props.setProperty("scout.proxy.uddiVersion", 
						"3.0");
		props.setProperty("scout.proxy.transportClass", 
						"org.apache.juddi.v3.client.transport.JAXWSTransport");
		connFactory.setProperties(props);

		Connection connection = connFactory.createConnection();

		PasswordAuthentication passwdAuth =
				new PasswordAuthentication(this.username,
										this.password.toCharArray());  
		Set<PasswordAuthentication> creds = 
				new HashSet<PasswordAuthentication>();  
		creds.add(passwdAuth);
		connection.setCredentials(creds);
		
		return connection;
	}
	
	/**
	 * Search organization by name.
	 * @param organizationName			the name of the organization.
	 * @return							the organization object.
	 * @throws JAXRException		raised when some problem occurs while 
	 * 								communicating with UDDI server.		
	 */
	protected Organization searchOrganization(String organizationName) 
			throws JAXRException {
		BusinessQueryManager businessQueryManager = 
				this.connection.getRegistryService().getBusinessQueryManager();

		Collection<String> findQualifiers = new ArrayList<String>();
		findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);

					
		Collection<String> namePatterns = new ArrayList<String>();
		namePatterns.add("%"+organizationName+"%");
		
		BulkResponse r = businessQueryManager.findOrganizations(
				findQualifiers, 
				namePatterns, 
				null, 
				null, 
				null, 
				null);
		@SuppressWarnings("unchecked")
		Collection<Organization> orgs = r.getCollection();
					                       
		for (Organization o : orgs)
			if (o.getName().getValue().equals(organizationName))
				return o;
		return null;
	}
	
	/**
	 * Search service by name.
	 * @param organizationName		the organization name.
	 * @param serviceName			the service name.
	 * @return						the service object.
	 * @throws JAXRException		raised when some problem occurs while 
	 * 								communicating with UDDI server.
	 */
	protected Service searchService(String organizationName, String serviceName)
			throws JAXRException {
		Organization org = this.searchOrganization(organizationName);
		if(org == null) return null;		
		return searchService(org, serviceName);
	}
	
	private Service searchService(Organization org, String serviceName) 
			throws JAXRException	 {
		@SuppressWarnings("unchecked")
		Collection<Service> services = org.getServices();
		
		for(Service s : services)
			if(s.getName().getValue().equals(serviceName))
				return s;
		return null;
	}
	
	/**
	 * Publish organization with name.
	 * This method will have no effect if the organization already exists.
	 * @param organizationName		the organization name.
	 * @return						the published organization object.
	 * @throws JAXRException		raised when some problem occurs while 
	 * 								communicating with UDDI server.
	 * @throws PublishOrganizationException		if the UDDI response is negative.
	 */
	protected Organization publishOrganization(String organizationName) 
			throws JAXRException, PublishOrganizationException {
		Organization org = this.searchOrganization(organizationName);
		
		if(org == null) {
			RegistryService rs = this.connection.getRegistryService();
			BusinessLifeCycleManager businessLifeCycleManager = 
					rs.getBusinessLifeCycleManager();
			org = businessLifeCycleManager.createOrganization(organizationName);
			
		    Collection<Organization> orgs = new ArrayList<Organization>();
		    orgs.add(org);
			BulkResponse br = businessLifeCycleManager.saveOrganizations(orgs);
			if(br.getStatus() != JAXRResponse.STATUS_SUCCESS)
				throw new PublishOrganizationException(organizationName);
			System.out.println("ORGANIZATION-CREATED -> " + organizationName);
		}
		
		return org;
	}
	
	/**
	 * Publish service with name. This method may cause an organization publish.
	 * This method has no effect if the service already exists.
	 * @param organizationName				the organization name.
	 * @param serviceName					the service name (operator prefix).
	 * @param serviceDescription			the service description (operator name).
	 * @return								the published service object.
	 * @throws JAXRException				raised when some problem occurs while 
	 * 										communicating with UDDI server.
	 * @throws PublishOrganizationException		if the UDDI response is negative.
	 * @throws PublishServiceException			same as before.		
	 */
	protected Service publishService(
			String organizationName, 
			String serviceName,
			String serviceDescription)
					throws JAXRException,
					PublishServiceException,
					PublishOrganizationException {
		Organization org = this.publishOrganization(organizationName);
		Service service = this.searchService(org, serviceName);
		
		if(service == null) {
			System.out.println("THE SERVICE DID NOT EXIST -> " + serviceName);
			RegistryService rs = this.connection.getRegistryService();
			BusinessLifeCycleManager businessLifeCycleManager = 
					rs.getBusinessLifeCycleManager();
			service = businessLifeCycleManager.createService(serviceName);
			service.setDescription(
					businessLifeCycleManager.createInternationalString(
							serviceName));
			org.addService(service);
			
		    Collection<Organization> orgs = 
		    		new ArrayList<Organization>();
		    orgs.add(org);
			BulkResponse br = businessLifeCycleManager.saveOrganizations(orgs);
			
			if(br.getStatus() != JAXRResponse.STATUS_SUCCESS)
				throw new PublishServiceException(organizationName,
												serviceName);
			System.out.println("SERVICE-CREATED -> " + serviceName);
		}
		
		return service;
		
	}
	
	/**
	 * Publish service binding with URL.
	 * This method will have no effect if the binding already exists.
	 * This method may cause an organization and a service publish.
	 * @param organizationName				the name of the organization.
	 * @param serviceName					the name of the service.
	 * @param serviceDescription			the description for the service.
	 * @param bindingURL					the URL.
	 * @return								the binding service object.
	 * @throws JAXRException				raised when some problem occurs while 
	 * 										communicating with UDDI server.
	 * @throws PublishOrganizationException		if the UDDI response is negative.
	 * @throws PublishServiceBindingException	same as before.
	 * @throws PublishServiceException			same as before.
	 */
	protected ServiceBinding publishBinding(
			String organizationName, 
			String serviceName,
			String serviceDescription,
			String bindingURL)
					throws JAXRException, 
					PublishServiceBindingException,
					PublishServiceException, 
					PublishOrganizationException {
		Service service = publishService(
				organizationName, serviceName, serviceDescription);
		Organization organization = searchOrganization(organizationName);
	
		
		@SuppressWarnings("unchecked")
		Collection<ServiceBinding> bindings = service.getServiceBindings();
		Iterator<ServiceBinding> it = bindings.iterator();
		
		while(it.hasNext()) {
			ServiceBinding sb = it.next();
			if(sb.getAccessURI().equals(bindingURL))
				return sb;
		}
		
		RegistryService rs = connection.getRegistryService();
		BusinessLifeCycleManager businessLifeCycleManager = 
				rs.getBusinessLifeCycleManager();
		ServiceBinding serviceBinding = 
				businessLifeCycleManager.createServiceBinding();
	    serviceBinding.setDescription(
	    		businessLifeCycleManager.createInternationalString(
	    				serviceDescription));
	    serviceBinding.setValidateURI(false);
	    serviceBinding.setAccessURI(bindingURL);	

		organization.removeService(service);
	    service.addServiceBinding(serviceBinding);
	    organization.addService(service);
	    System.out.println("ADDED NEW BINDING!");
		Collection<Organization> orgs = new ArrayList<Organization>();
		orgs.add(organization);
		BulkResponse br = businessLifeCycleManager.saveOrganizations(orgs);
		
		if(br.getStatus() != JAXRResponse.STATUS_SUCCESS)
			throw new PublishServiceBindingException(
					organizationName, 
					serviceName, 
					bindingURL);
		System.out.println("BINDING-CREATED -> " + bindingURL);
		
		return serviceBinding;
	}

	/**
	 * Remove organization by name.
	 * @param organizationName				the name of the organization.
	 * @throws JAXRException				raised when some problem occurs while 
	 * 										communicating with UDDI server.			
	 * @throws RemoveOrganizationException	if the UDDI response is negative.
	 */
	protected void removeOrganization(String organizationName) 
			throws JAXRException, RemoveOrganizationException {
		RegistryService rs = connection.getRegistryService();
		BusinessLifeCycleManager businessLifeCycleManager = 
				rs.getBusinessLifeCycleManager();
		Organization org = this.searchOrganization(organizationName);
		
	    Collection<Organization> orgs = new ArrayList<Organization>();
	    orgs.add(org);
		BulkResponse br = businessLifeCycleManager.deleteOrganizations(orgs);
		
		if(br.getStatus() != JAXRResponse.STATUS_SUCCESS)
			throw new RemoveOrganizationException(organizationName);
	}
	
	/**
	 * Remove service by name.
	 * @param organizationName				the organization name.
	 * @param serviceName					the service name.
	 * @throws JAXRException				raised when some problem occurs while 
	 * 										communicating with UDDI server.
	 * @throws RemoveServiceException		if the UDDI response is negative.
	 */
	protected void removeService(String organizationName, String serviceName)
			throws JAXRException,RemoveServiceException {
		RegistryService rs = connection.getRegistryService();
		BusinessLifeCycleManager businessLifeCycleManager = 
				rs.getBusinessLifeCycleManager();
		Service service = this.searchService(organizationName, serviceName);
		
		Collection<Service> services = new ArrayList<Service>();
		services.add(service);
		BulkResponse br = businessLifeCycleManager.deleteServices(services);
		
		if(br.getStatus() != JAXRResponse.STATUS_SUCCESS)
			throw new RemoveServiceException(organizationName, serviceName);
	}
	
	/**
	 * Remove binding by URL.
	 * @param organizationName				the organization name.
	 * @param serviceName					the service name.
	 * @param bindingURL					the binding URL.
	 * @throws JAXRException					raised when some problem occurs while 
	 * 											communicating with UDDI server.
	 * @throws RemoveServiceBindingException	if the UDDI response is negative.
	 */
	protected void removeBinding(
			String organizationName, 
			String serviceName, 
			String bindingURL) 
					throws JAXRException, RemoveServiceBindingException {
		RegistryService rs = connection.getRegistryService();
		BusinessLifeCycleManager businessLifeCycleManager = 
				rs.getBusinessLifeCycleManager();
		Service service = this.searchService(organizationName, serviceName);
		Organization organization = this.searchOrganization(organizationName);
		
		@SuppressWarnings("unchecked")
		Collection<ServiceBinding> bindings = service.getServiceBindings();
		Iterator<ServiceBinding> it = bindings.iterator();
		ServiceBinding toRemove = null;
		organization.removeService(service);
		while(it.hasNext()) {
			ServiceBinding sb = it.next();
			System.out.println("<REMOVE> Actual bindings="+sb.getAccessURI());
			if(sb.getAccessURI().equals(bindingURL)) {
				toRemove = sb;
				System.out.println("REMOVED BINDING "+sb.getAccessURI()+"!");
				break;
			}
		}
		
		if(toRemove != null)
			service.removeServiceBinding(toRemove);
	    organization.addService(service);
	    Collection<Organization> orgs = new ArrayList<Organization>();
		orgs.add(organization);
	    BulkResponse br = businessLifeCycleManager.saveOrganizations(orgs);
		
		if(br.getStatus() != JAXRResponse.STATUS_SUCCESS)
			throw new RemoveServiceBindingException(
					organizationName, 
					serviceName, 
					bindingURL);
	}
}
