package anacom.applicationserver;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.registry.JAXRException;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.joda.time.DateTime;

import com.sleepycat.je.tree.BIN;

import security.CryptoManager;
import anacom.shared.misc.GetBlackList;
import cacert.shared.stubs.Cacert;
import cacert.shared.stubs.CacertApplicationServerPortType;
import cacert.shared.stubs.CertificateRemoteException;
import cacert.shared.stubs.CertificateType;
import cacert.shared.stubs.SignCertificateType;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.repository.Repository;
import anacom.shared.handlers.SecurityHandler;
import anacom.domain.Network;
import anacom.services.CreateNewOperatorService;
import anacom.services.RegisterPhoneService;
import anacom.services.SetTimestampService;
import anacom.shared.UDDI.UDDIPublish;
import anacom.shared.UDDI.UDDIRemove;
import anacom.shared.dto.OperatorDetailedDTO;
import anacom.shared.dto.RegisterPhoneDTO;
import anacom.shared.dto.TimestampDTO;
import anacom.shared.exceptions.UDDI.PublishOrganizationException;
import anacom.shared.exceptions.UDDI.PublishServiceBindingException;
import anacom.shared.exceptions.UDDI.PublishServiceException;
import anacom.shared.exceptions.operator.OperatorNameAlreadyExists;
import anacom.shared.exceptions.operator.OperatorNameNotValid;
import anacom.shared.exceptions.operator.OperatorPrefixAlreadyExists;
import anacom.shared.exceptions.operator.OperatorPrefixNotValid;
import anacom.shared.stubs.client.Anacom;
 
/**
 * ServletContext is a interface which helps us to communicate with 
 * the servlet container. There is only one ServletContext for the 
 * entire web application.  
 * The ServetContext is created by the container when the web application 
 * is deployed and after that only the context is available to each servlet 
 * in the web application.
 *
 */
public class ApplicationServerInitListener implements ServletContextListener {
	
	private String operator;
	private String prefix;
	private String replicaNumber;
	
	private SecurityInfo securityInfo;
	
	public ApplicationServerInitListener() {
		this.securityInfo = SecurityInfo.getInstance();
	}
	
	/**
	 * Event listener for the context initialization (deployment) of the
	 * Servlet. It creates a new or loads an existing database for the Operator
	 * given in the XML configuration file. The servlet stops if the parameters
	 * given in configuration file have invalid or inconsistent values as
	 * following:
	 *  - if the given name or prefix are invalid;
	 *  - if there's already an operator with the given prefix or name and the
	 *    values in the configuration are inconsistent with the properties of
	 *    the operator in the database.
	 * @param event the event object representing the context initialization
	 */
	@Override
	public void contextInitialized(final ServletContextEvent event) {
		System.out.println(".............STARTING ANA.COM SERVER.............");
		
		try {
			
			this.operator = this.getParameter(event, "operator");
			this.prefix = this.getParameter(event, "prefix");
			this.replicaNumber = this.getParameter(event, "replicaNumber");
			
			//Initialization of configuration terms (Sign own public key)
			this.getCertificateSignature(this.getParameter(event, "pathAsymKeys"),
									  	 this.getParameter(event, "CAPublicKey"));
			
		} catch(NullPointerException e) {
			System.err.println("[0] -- Error while reading paramater type: " + 
								e.getMessage());
			System.exit(-1);
		}
		
	    
		if(prepareDataBase(event) && prepareUDDI(event))
			System.out.println(	".............STARTED " +
				 this.operator + "'s SERVER...........");
	}
	
	/**
	 * Event listener for the context destruction (undeployment) of the Servlet.
	 * It closes the database connection when the application is undeployed.
	 * @param event 	the event object representing the context destruction
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		pt.ist.fenixframework.pstm.TransactionChangeLogs.finalizeTransactionSystem();
		
		this.unregisterUDDI(event);
		
		Repository rep = Repository.getRepository();
		rep.closeRepository();
	}
	
	/**
	 * Method that prepares the operator data base
	 * @param event			this object has information about the operator environment
	 * @return
	 */
	boolean prepareDataBase(final ServletContextEvent event) {
		boolean result = false;
		// Get operator's configuration parameters
		int smsCost=-1, voiceCost=-1, videoCost=-1, tax=-1, bonus=-1;
		try {
			smsCost = new Integer(event.getServletContext().
					getInitParameter("smsCost")).intValue();
			voiceCost = new Integer(event.getServletContext().
					getInitParameter("voiceCost")).intValue();
			videoCost = new Integer(event.getServletContext().
					getInitParameter("videoCost")).intValue();
			tax = new Integer(event.getServletContext().
					getInitParameter("tax")).intValue();
			bonus = new Integer(event.getServletContext().
					getInitParameter("bonus")).intValue();

		// Initialize FenixFramework
	    FenixFramework.initialize(new Config() {
		    {
			dbAlias = "/tmp/db/" + event.getServletContext().getContextPath();
			domainModelPath = "/tmp/anacom.dml"; 
			repositoryType = RepositoryType.BERKELEYDB;
			rootClass = Network.class;
		    }
		});
	    
	    // Initializes operator
	    	OperatorDetailedDTO dto = new OperatorDetailedDTO(
	    			operator,
	    			prefix,
	    			smsCost,
	    			voiceCost,
	    			videoCost,
	    			tax,
	    			bonus);
	    	TimestampDTO timestampDTO = new TimestampDTO(new DateTime(1));
	    	
	    	new CreateNewOperatorService(dto).execute();
			new SetTimestampService(timestampDTO).execute();
			System.out.println("............." + operator + " operator " +
					"created...........");
			
			result = true;
	    } catch (NumberFormatException nfe) {
			System.out.println("Configuration ERROR: There was an error " +
					"while processing the config file (converting operator " +
					"parameters).");
			nfe.printStackTrace();
		} catch(OperatorNameAlreadyExists onae) {
	    	
	    	// Verify if existing operator is consistent with the given
	    	//configuration
	    	if (	onae.getPrefix().equals(prefix) && 
	    			onae.getSMSCost() == smsCost &&
	    			onae.getVideoCost() == videoCost &&
	    			onae.getVoiceCost() == voiceCost &&
	    			onae.getTax() == tax &&
	    			onae.getBonus() == bonus) {
	    		System.out.println(
	    				"............." +
	    				operator + 
	    				"'s database loaded.............");
	    		result = true;
	    	} else {
	    		System.out.println("Configuration ERROR: An operator with" +
	    				" given name already " +
	    				"exists with a different configuration.");
	    		onae.printStackTrace();
	    	}
	    	
	    } catch(OperatorPrefixAlreadyExists opae) {
	    	
	    	// Verify if existing operator is consistent with the given
	    	// configuration
	    	if (	opae.getName().equals(operator) && 
	    			opae.getSMSCost() == smsCost &&
	    			opae.getVideoCost() == videoCost &&
	    			opae.getVoiceCost() == voiceCost &&
	    			opae.getTax() == tax &&
	    			opae.getBonus() == bonus) {
	    		System.out.println(
	    				"............." + 
	    				operator + 
	    				"'s database loaded.............");
	    		result = true;
	    	} else {
	    		System.out.println("Configuration ERROR: An operator with" +
	    				" given prefix already " +
	    				"exists with a different configuration.");
	    		opae.printStackTrace();
	    	}
	    	
	    } catch(OperatorPrefixNotValid opnv) {
	    	System.out.println("Configuration ERROR: Given operator prefix" +
	    			" is not a valid prefix " +
	    			"(it must be a number with 2 digits).");
	    	opnv.printStackTrace();
	    } catch(OperatorNameNotValid onnv) {
	    	System.out.println("Configuration ERROR: Given operator name" +
	    			" is not a valid name " +
	    			"(it must have at least 1 character).");
	    	onnv.printStackTrace();
	    }
		return result;
	}

	/**
	 * Method that prepares the UDDI registry for the new operator (publish).
	 * @param event			this object has information about the operator environment
	 * @return
	 */	
	boolean prepareUDDI(ServletContextEvent event) {
		boolean result = false;
	    String url = 	"http://localhost:8080"+
	    				event.getServletContext().getContextPath()+
	    				"/AnacomApplicationServer";
	    
	    try {
			new UDDIPublish("username", "password", "uddi.xml").publish(
					"Anacom", prefix, operator, url);
			result = true;
		} catch (JAXRException e) {
			System.out.println("UDDI ERROR: an error was reported while trying" +
					" to communicate with the uddi server.");
			e.printStackTrace();
		} catch(PublishServiceBindingException e) {
			System.out.println("UDDI ERROR: the server couldn't publish the " +
					"service binding" + e.getBindingURL() + ".");			
		} catch(PublishServiceException e) {
			System.out.println("UDDI ERROR: the server couldn't publish the " +
					"service " + e.getServiceName() + ".");		
		} catch(PublishOrganizationException e) {
			System.out.println("UDDI ERROR: the server couldn't publish the " +
					"organization " + e.getOrganizationName() + ".");
		}
	    return result;
	}
	
	/**
	 * Unregisters the service binding from the UDDI Registry
	 * @param event		the local execution context
	 * @return			true if it was removed, false otherwise
	 */
	boolean unregisterUDDI(ServletContextEvent event) {
		boolean result = false;
	    String url = 	"http://localhost:8080"+
	    				event.getServletContext().getContextPath()+
	    				"/AnacomApplicationServer";
	    try {
		    new UDDIRemove("username", "password", "uddi.xml").
		    	Remove("Anacom", prefix, url);
		    result = true;
	    } catch (JAXRException e) {
			System.out.println("UDDI ERROR: an error was reported while trying" +
					" to remove an url from the uddi server.");
			e.printStackTrace();
		}
	    return result;
	}
	/**
	 * getParameter from xml server init listener
	 * @param event	{@link ServletContextEvent} event
	 * @param parameter	parameter used to extract information
	 * @return the string value inside of xml
	 */
	private String getParameter(ServletContextEvent event, String parameter) {
		String buffer = event.getServletContext().getInitParameter(parameter);
		if(buffer == null) {
			throw new NullPointerException(parameter);
		}
		return buffer;
	}
	
	/**
	 * Initialize all the needed configurations to
	 * boot strap this server entity
	 * If something goes wrong, it abort deployment process
	 * @param KeyPathContext receives the current directory that contains 
	 * the asymmetric pair of keys used to initialize one of the configurations
	 * 	requirements 
	 * @param CAPublicKeyPath receives also the directory with CA public key
	 * 		  for future use
	 */
	private void getCertificateSignature( String KeyPathContext, 
										  String CAPublicKeyPath) {

		CryptoManager manager = CryptoManager.getInstance();
		
		try {
				
			System.out.println("[1] -- Reading keys from files...");
			//Reading asymmetric keys from file
			String publicKey = manager.getPublicRSAKeyfromFile(KeyPathContext);
			String privateKey = manager.getPrivateRSAKeyfromFile(KeyPathContext);
			String CAPublickey = manager.getPublicRSAKeyfromFile(CAPublicKeyPath);
			
			//Construct an DTO for communication
			SignCertificateType signature = new SignCertificateType();
			
			signature.setEntityName(operator+replicaNumber);
			signature.setPublicKey(publicKey);
			
			CacertApplicationServerPortType cacert = 
					new Cacert().getCacertApplicationServicePort();
			
			this.setHandlersCACommunication(cacert);
			
			System.out.println("[2] -- Communicate with CA Manager...");
			CertificateType certificate = cacert.signCertificate(signature);
			
			this.initializeSecurityInfo(publicKey, privateKey, 
										certificate, CAPublickey);
			
			//initialize service to observe the blocked certificate list
			GetBlackList revogatedList = new GetBlackList(cacert);
			
			System.out.println("[3] -- Signature successfully made with CA");
			
			this.dumpCertificateInfo(certificate);
			
		} catch(CertificateRemoteException e) {
			System.err.println("[0] -- Error in communication with CA Entity");
			System.exit(-1);
		} catch(Exception e) {
			System.err.println("[0] -- Error reading public and private key from " + 
					KeyPathContext);
			System.exit(-1);
		}
	}
	
	/**
	 * Initialize the Security Info of this Server
	 * @param publicKey	the public key of the server
	 * @param privateKey	the private key of the server
	 * @param certificate	the certificate signed by CA
	 * @param CAPublicKey	the CA public key
	 */
	private void initializeSecurityInfo(String publicKey, 
										String privateKey, 
										CertificateType certificate,
										String CAPublicKey) {
		this.securityInfo.setThisEntityPublicKey(publicKey);
		this.securityInfo.setThisEntityPrivateKey(privateKey);
		this.securityInfo.setCertificate(certificate);
		this.securityInfo.setCertificateAPublicKey(CAPublicKey);
	}
	
	
	/**
	 * Set handlers for communication with Certificate Authority
	 * @param cacert	receives a Cacert port type to associate 
	 * a collection of handlers to web service requested
	 */
	private void setHandlersCACommunication(CacertApplicationServerPortType cacert) {
		Binding binding = ((BindingProvider) cacert).getBinding();
		@SuppressWarnings("rawtypes")
		List<Handler> handlers = binding.getHandlerChain();
		handlers.add(new SecurityHandler());
		binding.setHandlerChain(handlers);
	}
	
	//used only for debug
	/**
	 * dumpCertificateInfo
	 * @param certificate receives a certificate and prints
	 * all the fields X509 inside of it
	 */
	private void dumpCertificateInfo(CertificateType certificate) {
		System.out.println("_============= DUMP CERTIFICATE INFO =============");
		System.out.println("Serial: " + certificate.getSerial());
		System.out.println("Subject: " + certificate.getSubject());
		System.out.println("Issuer : " + certificate.getIssuer());
		System.out.println("valid From: " + certificate.getValidFrom());
		System.out.println("valid To: " + certificate.getValidTo());
		System.out.println("Public Key: " + certificate.getPublicKey());
		System.out.println("=======================END DUMP =================");
	}
}
