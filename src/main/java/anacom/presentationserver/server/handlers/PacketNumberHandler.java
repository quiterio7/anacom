
package anacom.presentationserver.server.handlers;

import java.util.HashMap;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import anacom.presentationserver.server.ThreadContext;
import anacom.presentationserver.server.WaitingResponses;
import anacom.shared.misc.FieldVerifier;
import anacom.shared.misc.SOAPFields;

/**
 * This handler is responsible for adding a sequential number to the SOAP
 * message. This number will be used in the application server to guarantee
 * that requests are processed in order.
 */
public class PacketNumberHandler implements SOAPHandler<SOAPMessageContext> {
	
	/**
	 * This HashMap stores the next packet number per Operator prefix.
	 */
	private HashMap<String, Integer> counters = new HashMap<String, Integer>();
	
	/**
	 * Gets the packet number for the given Operator. If there's no entry for
	 * the Operator, a new one is added with its counter starting at 0. The
	 * given Operator's counter is incremented.
	 * @param prefix the desired Operator's prefix
	 * @return the current Operator's packet number
	 */
	private Integer getPacketNumber(String prefix) {
		Integer number = this.counters.get(prefix);
		if (number == null) {
			number = new Integer(SOAPFields.INIT_PACKET_NUMBER);
		}
		this.counters.put(prefix, new Integer(number.intValue() + 1));
		return number;
	}

	@Override
	public void close(MessageContext arg0) {}

	@Override
	public Set<QName> getHeaders() { return null; }

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		System.out.println("Entering PacketNumberHandler");
		
		boolean result = true;
		Boolean outboundProperty =
				(Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outboundProperty.booleanValue()) {
			result = this.setPacketNumber(context);
		}
		
		if (result) {
			System.out.println("PacketNumberHandler returned successfully");
		} else {
			System.out.println("PacketNumberHandler failed, dropping message");
		}
		
		return result;
	}
	
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return this.handleMessage(context);
	}
	
	/**
	 * Numbers the given SOAP packet. The message will be dropped if the
	 * Operator prefix in the ThreadContext isn't a valid prefix, if the 
	 * current request timestamp wasn't set in Thread's Context, if the request
	 * wasn't added to WaitingResponses, if there's no SOAPAction HTTP header
	 * field in the message or if the SOAPAction value isn't either "write" or
	 * "read".
	 * @param context
	 * 		the SOAP message's context object
	 * @return true
	 * 		if the packet number was set successfully, false if there was an
	 * 		error setting the packet's number
	 */
	private boolean setPacketNumber(SOAPMessageContext context) {
		boolean result = false;

		// Checks if the current message is a write
		ThreadContext threadContext = ThreadContext.getInstance();
		if (threadContext.currentMessageIsWrite()) {
			System.out.println("Numbering a packet");
			try {
				String prefix = threadContext.getCurrentOperatorPrefix();
			
				if (FieldVerifier.getInstance().validOperatorPrefix(prefix)) {
					SOAPEnvelope env =
							context.getMessage().getSOAPPart().getEnvelope();
					SOAPHeader header = env.getHeader();
					if (header == null) { header = env.addHeader(); }
				
					// Add packet number to the header
					Name name = SOAPFields.getInstance().getPacketNumber(env);
					SOAPElement el = header.addChildElement(name);
					
					// Check if this message is a copy of an already numbered
					// request
					XMLGregorianCalendar requestTimestamp =
							threadContext.getCurrentRequestTimestamp();
					if (requestTimestamp == null) {
						System.out.println("Problem encountered setting " +
								"Packet Number: current request timestamp " +
								"wasn't set in ThreadContext, dropping " +
								"message");
					} else {
						WaitingResponses responses =
								WaitingResponses.getInstance();
						if (!responses.contains(requestTimestamp)) {
							System.out.println("Problem encountered setting " +
									"Packet Number: current request wasn't " +
									"added to WaitingResponses, dropping" +
									"message");
						} else {
							Integer number =
									responses.getPacketNumber(requestTimestamp);
							if (number == null) {
								// Generate new number for this request
								int newNumber =
										getPacketNumber(prefix).intValue();
								responses.setPacketNumber(
										requestTimestamp,
										newNumber);
								el.addTextNode("" + newNumber);
							} else {
								// Use request number
								el.addTextNode(number.toString());
							}
							result = true;
						}
					}
				} else {
					System.out.println("Problem encountered setting Packet " +
							"Number: current Operator prefix is invalid, it " +
							"must be an integer 2 digits long, dropping" +
							"message");
				}
			} catch (SOAPException soape) {
				System.out.println("Caught SOAPException while setting " +
						"Packet Number: " + soape.getMessage());
			}
			
			if (result) {
				System.out.println("Packet numbered successfully");
			}
			
		} else {
			result = true;
		}
		
		return result;
	}

}
