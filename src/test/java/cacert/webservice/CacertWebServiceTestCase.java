package cacert.webservice;

import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import junit.framework.TestCase;
import cacert.webservice.handlers.SecurityHandler;
import security.CryptoManager;
import cacert.shared.stubs.Cacert;
import cacert.shared.stubs.CacertApplicationServerPortType;

public class CacertWebServiceTestCase extends TestCase {
	
	protected final String THUMBPRINT_ALGORITHM = "MD5";
	
	protected CacertApplicationServerPortType cacert;
	
	protected CryptoManager manager = CryptoManager.getInstance();
	
	protected String publicKey; 
	
	protected String privateKey; 
	
	protected String CApublicKey;	
	
	public CacertWebServiceTestCase() {
		String relativePathOP1keys = "RSAkeys/OP1keys";
		String relativePathCAkeys = "RSAkeys/CAkeys";
		
		try {
			publicKey = manager.getPublicRSAKeyfromFile(relativePathOP1keys);
			privateKey = manager.getPrivateRSAKeyfromFile(relativePathOP1keys);
			CApublicKey = manager.getPublicRSAKeyfromFile(relativePathCAkeys);
			
			SecurityInfo securityInfo = SecurityInfo.getInstance();
			securityInfo.setThisEntityPublicKey(publicKey);
			securityInfo.setThisEntityPrivateKey(privateKey);
			securityInfo.setCertificateAPublicKey(CApublicKey);
			
		} catch (FileNotFoundException e) {
			//FIXME: later
			System.exit(-1);
		}
	}

	
	@Override
    protected void setUp() {
		Cacert service = new Cacert();
		cacert = service.getCacertApplicationServicePort();
		Binding binding = ((BindingProvider) cacert).getBinding();
		@SuppressWarnings("rawtypes")
		List<Handler> handlers = binding.getHandlerChain();
		handlers.add(new SecurityHandler());
		binding.setHandlerChain(handlers);
	}	

}
