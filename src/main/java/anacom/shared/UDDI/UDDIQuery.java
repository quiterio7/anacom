package anacom.shared.UDDI;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Organization;
import javax.xml.registry.infomodel.ServiceBinding;
import javax.xml.registry.infomodel.Service;

import anacom.shared.exceptions.UDDI.QueryOrganizationException;
import anacom.shared.exceptions.UDDI.QueryServiceException;

public class UDDIQuery extends UDDIConnection {

	/**
	 * Attribute that will be used to store the UDDI responses in memory.
	 * Interpretation: Organizations have Services that have multiple bindings.
	 */
	private final HashMap<String, HashMap<String, ArrayList<String>>> cache = 
			new HashMap<String, HashMap<String, ArrayList<String>>>();
	
	/**
	 * Constructor.
	 * This method causes the creation of an UDDI connection.
	 * @param username			the user name for the UDDI connection.
	 * @param password			the password for the UDDI connection.
	 * @param xmlUDDI			the UDDI configuration file path.
	 * @throws JAXRException	raised when some problem occurs while 
	 * 							communicating with UDDI server.
	 */
	public UDDIQuery(String username, String password, String xmlUDDI)
			throws JAXRException {
		super(username, password, xmlUDDI);
	}
	
	/**
	 * This constructor should be used only for testing!
	 */
	protected UDDIQuery() {
		super();
	}
	
	/**
	 * Search organization
	 * @param organizationName				the organization name.
	 * @return								the organization (cache object)
	 * @throws JAXRException	raised when some problem occurs while 
	 * 							communicating with UDDI server.
	 */
	public HashMap<String, ArrayList<String>> query(String organizationName) 
			throws JAXRException, QueryOrganizationException {
		if(this.cache.containsKey(organizationName))
			return this.cache.get(organizationName);
		
		HashMap<String, ArrayList<String>> hash = 
				new HashMap<String, ArrayList<String>>();

		Organization org = super.searchOrganization(organizationName);
		
		if(org == null) {
			throw new QueryOrganizationException(organizationName);
		}
		
		@SuppressWarnings("unchecked")
		Collection<Service> services = org.getServices();
		for(Service s : services) {
			String serviceName = s.getName().getValue();
			ArrayList<String> array = new ArrayList<String>();
			@SuppressWarnings("unchecked")
			Collection<ServiceBinding> bindings = s.getServiceBindings();
			for(ServiceBinding sb : bindings) {
				array.add(sb.getAccessURI());
			}
			hash.put(serviceName, array);
		}
		
		this.cache.put(organizationName, hash);
		
		return hash;
	}
	
	/**
	 * Search service
	 * @param organizationName			the organization name.
	 * @param serviceName				the service name.
	 * @return							the service (cache object).
	 * @throws JAXRException			raised when some problem occurs while 
	 * 									communicating with UDDI server.
	 * @throws QueryServiceException	raised if there is no such service.
	 */
	public ArrayList<String> query(String organizationName, String serviceName)
			throws JAXRException, QueryServiceException {
		if(this.cache.containsKey(organizationName))
			if(this.cache.get(organizationName).containsKey(serviceName)) 
				return this.cache.get(organizationName).get(serviceName);
		
		Service service = super.searchService(organizationName, serviceName);
		
		if(service == null)
			throw new QueryServiceException(organizationName, serviceName);
		
		@SuppressWarnings("unchecked")
		Collection<ServiceBinding> bindings = service.getServiceBindings();
		ArrayList<String> array = new ArrayList<String>();
		for(ServiceBinding sb : bindings) {
			array.add(sb.getAccessURI());
			System.out.println(sb.getAccessURI());
		}
		
		if(this.cache.containsKey(organizationName)) {
			this.cache.get(organizationName).put(serviceName, array);
		}
		else {
			HashMap<String, ArrayList<String>> hash = 
					new HashMap<String, ArrayList<String>>();
			hash.put(serviceName, array);
			this.cache.put(organizationName, hash);
		}
		
		return array;
	}

	/**
	 * Clears the cache content (organization object).
	 * @param organizationName			the organization name.
	 */
	public void clearCache(String organizationName) {
		this.cache.remove(organizationName);
	}
	
	/**
	 * Clears the cache content (service object). 
	 * @param organizationName			the organization name.
	 * @param serviceName				the service name.
	 */
	public void clearCache(String organizationName, String serviceName) {
		if(this.cache.containsKey(organizationName))
			this.cache.get(organizationName).remove(serviceName);
	}
	
}
