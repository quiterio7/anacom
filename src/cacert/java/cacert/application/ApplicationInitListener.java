package cacert.application;

import java.io.FileNotFoundException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.repository.Repository;
import cacert.application.handlers.SecurityHandler;
import cacert.domain.CAManager;
import security.CryptoManager;
import cacert.services.SetKeysService;
import cacert.services.SetValidPeriodService;
import cacert.services.SignCertificateService;
import cacert.shared.dto.CertificateDTO;
import cacert.shared.dto.SetKeysDTO;
import cacert.shared.dto.SignCertificateDTO;
import cacert.shared.dto.ValidPeriodDTO;
import cacert.shared.stubs.CertificateType;
 
public class ApplicationInitListener implements ServletContextListener {

	private SecurityInfo securityInfo;
	
	public ApplicationInitListener() {
		securityInfo = SecurityInfo.getInstance();
	}
	
	/**
	 * Event listener for the context initialization (deployment) of the
	 * Servlet. It creates a new or loads an existing database for the CA 
	 * Manager given in the XML configuration file. The servlet stops if the 
	 * parameters given in configuration file have invalid or inconsistent 
	 * values as following:
	 *  - if the given validPeriod or public or private keys are invalid;
	 * @param event the event object representing the context initialization
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
		System.out.println("============> Starting CA-Manager Server...");
		
	    this.initializeFenixFramework();
	    
	    try {
	    	CryptoManager manager = CryptoManager.getInstance();
	    	
	    	int validPeriod = 
	    			Integer.parseInt(this.getParameter(event, "validPeriod"));
	    	String pathKeys = this.getParameter(event, "pathAsymKeys");
	    	String CAName = this.getParameter(event, "CAName");
	    	
		    String publicKey = 
		    		manager.getPublicRSAKeyfromFile(pathKeys);
		    String privateKey = 
		    		manager.getPrivateRSAKeyfromFile(pathKeys);
		    
		    //for debug
		    System.out.println("--- CA Manager Info ---");
		    System.out.println("1. CAName : " + CAName);
		    System.out.println("2. Valid Period of Certificate Signature: " +
		    					validPeriod);
		    System.out.println("3. Relative path of asymmetric keys: " + 
		    					pathKeys);
		    
		    this.initializeCA(publicKey, privateKey, validPeriod);
		    this.getCertificateForCA(CAName, publicKey);
		    
		    this.securityInfo.setThisEntityPrivateKey(privateKey);
		    this.securityInfo.setThisEntityPublicKey(publicKey);
		    this.securityInfo.setCertificateAPublicKey(publicKey);
		    
		    System.out.println("============> CA-Manager Server Started " +
		    		"successfully");
		    
	    } catch (NullPointerException e) {
	    	System.out.println("Error: Parameter " + e.getMessage() + 
	    			" is null");
	    	System.exit(-1);
	    } catch (FileNotFoundException e) {
			System.out.println("File " + e.getMessage() + " not found ");
			System.exit(-1);
		}
	}

	/**
	 * Initializes the Fenix Framework
	 */
	private void initializeFenixFramework() {
		FenixFramework.initialize(new Config() {
		    {
			dbAlias = "/tmp/db/" + "cacert";
			domainModelPath = "/tmp/cacert.dml"; 
			repositoryType = RepositoryType.BERKELEYDB;
			rootClass = CAManager.class;
		    }
		});
	}
	
	/**
	 * Initializes the Certificate Authority.
	 * @param publicKey		the public key of the CA
	 * @param privateKey	the private key of the CA
	 * @param validPeriod	the valid period of the certificates signed by
	 * 						the CA (in milliseconds)
	 */
	private void initializeCA(String publicKey, String privateKey,
			int validPeriod) {
		SetKeysDTO setKeysDTO = new SetKeysDTO(publicKey, privateKey);
		ValidPeriodDTO timeStampDTO = new ValidPeriodDTO(validPeriod);
			
		SetKeysService setKeyservice = new SetKeysService(setKeysDTO);
		SetValidPeriodService setTimeservice = 
									new SetValidPeriodService(timeStampDTO);
			
		setKeyservice.execute();
		setTimeservice.execute();
	}
	
	/**
	 * Gets a certificate for the CA, to be used by the handler to identify 
	 * the sender of the messages as the CA
	 * @param entityName	the name of the CA
	 * @param publicKey		the public key of the CA
	 */
	private void getCertificateForCA(String entityName, String publicKey) {
		SignCertificateDTO signCertificateDTO = 
				new SignCertificateDTO(entityName, publicKey);
		SignCertificateService signCertificateService = 
				new SignCertificateService(signCertificateDTO);
		
		signCertificateService.execute();
		CertificateDTO certificate = 
								signCertificateService.getCertificateDTO();
		
		CertificateType handlerCertificate = new CertificateType();
		handlerCertificate.setIssuer(certificate.getIssuer());
		handlerCertificate.setKeyUsage(certificate.getKeyUsage());
		handlerCertificate.setPublicKey(certificate.getPublicKey());
		handlerCertificate.setSerial(certificate.getSerial());
		handlerCertificate.setSignature(certificate.getSignature());
		handlerCertificate.setSignatureAlgorithm(
				certificate.getSignatureAlgorithm());
		handlerCertificate.setSubject(certificate.getSubject());
		handlerCertificate.setThumbPrint(certificate.getThumbPrint());
		handlerCertificate.setThumbPrintAlgorithm(
				certificate.getThumbPrintAlgorithm());
		handlerCertificate.setValidFrom(certificate.getValidFrom());
		handlerCertificate.setValidTo(certificate.getValidTo());
		
		this.securityInfo.setCertificate(handlerCertificate);
	}
	
	/**
	 * Event listener for the context destruction (undeployment) of the Servlet.
	 * It closes the database connection when the application is undeployed.
	 * @param event 	the event object representing the context destruction
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		pt.ist.fenixframework.pstm.TransactionChangeLogs.finalizeTransactionSystem();
		
		Repository rep = Repository.getRepository();
		rep.closeRepository();
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

}
