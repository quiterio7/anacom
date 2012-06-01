package anacom.presentationserver.server.handlers;

import org.w3c.dom.NodeList;

import cacert.shared.stubs.CertificateType;
import anacom.presentationserver.server.ThreadContext;
import anacom.shared.handlers.SecurityHandler;

public class AppSecurityHandler extends SecurityHandler {
	
	@Override
	protected CertificateType recreateCertificate(NodeList nodeList) {
		CertificateType certificate = super.recreateCertificate(nodeList);
		ThreadContext.getInstance().setCertificate(certificate);
		return certificate;
	}

}
