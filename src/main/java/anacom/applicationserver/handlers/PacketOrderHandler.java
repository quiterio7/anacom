package anacom.applicationserver.handlers;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.DOMException;

import anacom.shared.misc.SOAPFields;

/**
 * This handler is responsible for guaranteeing that packets are received in
 * order by the web service.
 */
public class PacketOrderHandler implements SOAPHandler<SOAPMessageContext> {
	
	private int nextPacket = SOAPFields.INIT_PACKET_NUMBER;
	
	public PacketOrderHandler() {
		System.out.println("Packet Order Handler created successfully.");
	}
	
	/**
	 * This lock is used to guarantee synchronization while verifying if a
	 * packet number corresponds to the expected packet.
	 */
	Lock lock = new ReentrantLock();
	
	/**
	 * If a packet is received out of order, it awaits on this condition until
	 * the next expected packet is received.
	 */
	Condition inOrder = lock.newCondition();
	
	@Override
	public void close(MessageContext arg0) {}

	@Override
	public Set<QName> getHeaders() { return null; }

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		System.out.println("Entering PacketOrderHandler");
		
		boolean result = true;
		Boolean outboundProperty =
				(Boolean)context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (!outboundProperty.booleanValue()) {
			result = this.processPacket(context);
		}
		
		if (result) {
			System.out.println("PacketOrderHandler returned successfully");
		} else {
			System.out.println("PacketOrderHandler failed, dropping message");
		}
		
		return result;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return this.handleMessage(context);
	}
	
	/**
	 * Checks if given packet was received in order, if so, handler processing
	 * continues, otherwise, if this message is a "write" SOAPAction, it is
	 * intercepted. The message will be dropped if there's no SOAPAction HTTP
	 * header field in the message, if the SOAPAction value isn't "write" or
	 * "read", if the message is a "write" and its packet number wasn't set or
	 * if the packet number in the message isn't a valid number.
	 * @param context
	 * 		the SOAP message's context object
	 * @return true
	 * 		if the packet was received in order, false otherwise or if there
	 * 		was an error processing the message
	 */
	private boolean processPacket(SOAPMessageContext context) {
		boolean result = false;
		System.out.println("Processing a packet");
			
		try {
			SOAPEnvelope env =
					context.getMessage().getSOAPPart().getEnvelope();
			SOAPHeader header = env.getHeader();
			if (header == null) {
				System.out.println("Received SOAP message without Packet" +
						" Number, returning packet without ordering");
				result = true;
			} else {
			
				// Retrieve packet number
				Name name = SOAPFields.getInstance().getPacketNumber(env);
				SOAPElement numberEl = 
						(SOAPElement)header.getChildElements(name).next();
				int number = Integer.parseInt(numberEl.getTextContent());
				
				// Check if packet is in order
				this.lock.lock();
				while (number != this.nextPacket) {
					if (number == 0) {
						// reset counter
						this.nextPacket = 0;
					} else {
						// wait until packet in order is received
						this.inOrder.awaitUninterruptibly();
					}
				}
				this.nextPacket++;
				this.inOrder.signalAll(); // process packets received out of order
				result = true;
				this.lock.unlock();
			}
		} catch (SOAPException soape) {
			System.out.println("Caught SOAPException while retrieving " +
					"Packet Number: " + soape.getMessage());
		} catch (DOMException dome) {
			System.out.println("Caught DOMException while retrieving " +
					"Packet Number: " + dome.getMessage());
		} catch (NoSuchElementException nsee) {
			System.out.println("Received SOAP message without Packet " +
					"Number, returning packet without ordering");
			result = true;
		} catch (NumberFormatException nfe) {
			System.out.println("Received SOAP message with an invalid " +
					"Packet Number, dropping message");
		} catch (IllegalMonitorStateException imse) {
			System.out.println("Caught IllegalMonitorStateException " +
					"while checking if packet is in order: " +
					imse.getMessage());
		}
		
		if (result) {
			System.out.println("Packet ordered successfully");
		}
		
		return result;
	}

}
