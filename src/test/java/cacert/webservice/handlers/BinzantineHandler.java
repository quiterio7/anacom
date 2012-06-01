package cacert.webservice.handlers;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;


public class BinzantineHandler extends SecurityHandler {
	
	public static boolean firstTime = true;
	
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("Passei aqui");
		return this.handleMessage(context);
	}
	
	/**
	 * Handles a normal message and change original message
	 * 
	 * @param context
	 *            a normal message
	 * @return true if handled correctly, false otherwise
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		System.out.println("[client] Binzantine Handler handling message");
		boolean answer;
		Boolean outboundProperty = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outboundProperty) {
			answer = this.handleOutboundMessage(context);
			
			if(!firstTime) {
				//duplicate message
				answer = answer && this.addBinzantineMessage(context);
			}
		} else {
			answer = this.handleInboundMessage(context);
		}
		return answer;
	}
	
	private boolean addBinzantineMessage(SOAPMessageContext context) {
		Name elementName;
		SOAPElement element;
		SOAPPart soapPart = context.getMessage().getSOAPPart();
		SOAPEnvelope envelope;
		try {
			envelope = soapPart.getEnvelope();
			SOAPBody body = envelope.getBody();
			elementName = envelope.createName("BINZATINE", "ca", "http://www.sd.com/");
			element = body.addChildElement(elementName);
			element.addTextNode("BINZANTINE");
			return true;
		} catch (SOAPException e) {
			return false;
		}
	}
}
