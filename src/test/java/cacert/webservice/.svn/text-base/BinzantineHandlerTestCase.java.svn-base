package cacert.webservice;

import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import cacert.shared.stubs.BlockCertificateType;
import cacert.shared.stubs.Cacert;
import cacert.shared.stubs.CertificateRemoteException;
import cacert.shared.stubs.CertificateSerialsListType;
import cacert.shared.stubs.CertificateType;
import cacert.shared.stubs.SignCertificateType;
import cacert.webservice.handlers.BinzantineHandler;

public class BinzantineHandlerTestCase extends CacertWebServiceTestCase {
	
	public BinzantineHandlerTestCase() {
		super();
	}
	
	@Override
	protected void setUp() {
		Cacert service = new Cacert();
		cacert = service.getCacertApplicationServicePort();
		Binding binding = ((BindingProvider) cacert).getBinding();
		@SuppressWarnings("rawtypes")
		List<Handler> handlers = binding.getHandlerChain();
		handlers.add(new BinzantineHandler());
		binding.setHandlerChain(handlers);
	}
	
	public void testNormalServiceWithNullAnswer() {
		CertificateType answer = null;
		try {
		CertificateType certificate = signCertificate();
		BlockCertificateType blocked = new BlockCertificateType();
		assertTrue(certificate != null);
		
		
		blocked.setEntityName(certificate.getSubject());
		blocked.setNewPublicKey(publicKey);
		blocked.setOldPublicKey(privateKey);
		blocked.setOldSerial(certificate.getSerial());
		
		
		// this request should return null
		BinzantineHandler.firstTime = false;
		
		 answer = this.cacert.blockCertificate(blocked);
		assertTrue(answer == null);
		} catch(Exception e) {
			assertTrue(answer  == null);
		}
	}
	
	
	private CertificateType signCertificate() {
		CertificateType certificate = null;
		SignCertificateType parameters = new SignCertificateType();
		parameters.setEntityName("TEST1");
		parameters.setPublicKey(this.publicKey);
		
		try{
			certificate = cacert.signCertificate(parameters);	
			SecurityInfo.getInstance().setCertificate(certificate);
	    			
			//Set security informations
	    	SecurityInfo info = SecurityInfo.getInstance();
	    	info.setCertificate(certificate);
	    	
	    	return certificate;
			
		} catch(Exception e) {
			return null;
		}

	}

}
