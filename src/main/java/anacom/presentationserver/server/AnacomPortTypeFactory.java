package anacom.presentationserver.server;

import java.util.List;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import anacom.presentationserver.server.handlers.AppSecurityHandler;
import anacom.presentationserver.server.handlers.PacketNumberHandler;
import anacom.shared.stubs.client.Anacom;
import anacom.shared.stubs.client.AnacomApplicationServerPortType;

public class AnacomPortTypeFactory {

	private final AnacomApplicationServerPortType  portType;
	
	public AnacomPortTypeFactory() {
		this.portType = new Anacom().getAnacomApplicationServicePort();
		
		Binding bindingAnacom = ((BindingProvider) this.portType).getBinding();
		List<Handler> handlersList = bindingAnacom.getHandlerChain();
	
		handlersList.add(new PacketNumberHandler());
		handlersList.add(new AppSecurityHandler());
		bindingAnacom.setHandlerChain(handlersList);
	}

    /**
     * Changes the end point address to the given url.
     * @param url the new end point url
     * @return the anacom application server port
     */
	public AnacomApplicationServerPortType setServer(String url) {
		BindingProvider bp = (BindingProvider)this.portType; 
		bp.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				url);
		return this.portType;
	}
}
