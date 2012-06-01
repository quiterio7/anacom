package anacom.presentationserver.server;

import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * This class is used to keep record across the presentation server of which
 * messages have been successfully received by the application server. This
 * class is needed because the packet number handler needs to know if the
 * current message being processed by it is a new message or a copy of an
 * already sent message. In the later case, the packet number handler can't
 * generate a new packet number, it must use the old one.
 * WARNING: this class may not work if the presentation server isn't
 * serialized.
 */
public class WaitingResponses {

	private static WaitingResponses instance = null;
	
	public static WaitingResponses getInstance() {
		if (instance == null) { instance = new WaitingResponses(); }
		return instance;
	}
	
	/**
	 * Maintains the data needed about a service waiting for response.
	 */
	private class PendingResponseInfo {
		
		private int replicationDegree = 0;
		private int responsesReceived = 0;
		private int packetNumber = 0;
		private boolean packetNumberIsSet = false;
		
		/**
		 * Creates a new PendingResponseInfo with the given replicationDegree.
		 * @param replicationDegree
		 * 		the number of responses to wait for
		 */
		PendingResponseInfo(int replicationDegree) {
			this.replicationDegree = replicationDegree;
		}
		
		/**
		 * @return the number of received responses for this request
		 */
		int getResponsesReceived() { return this.responsesReceived; }
		
		/**
		 * @return the total number of responses to wait for
		 */
		int getReplicationDegree() { return this.replicationDegree; }
		
		/**
		 * Sets the replicationDegree for this pending request. New servers may
		 * be added or removed in run time.
		 * @param replicationDegree
		 * 		the new replication degree
		 */
		void setReplicationDegree(int replicationDegree) {
			this.replicationDegree = replicationDegree;
		}
		
		/**
		 * @return the packet number for this pending request
		 */
		int getPacketNumber() { return this.packetNumber; }
		
		/**
		 * Sets the request's packet number.
		 * @param number
		 * 		the desired packet number
		 */
		void setPacketNumber(int number) {
			this.packetNumber = number;
			this.packetNumberIsSet = true;
		}
		
		/**
		 * @return true if this request's packet number is set, false otherwise
		 */
		boolean packetNumberIsSet() { return this.packetNumberIsSet; }
		
		/**
		 * Increments the number of received responses.
		 */
		void responseReceived() { this.responsesReceived++; }
		
	}
	
	/**
	 * This map associates the timestamp of a request with information on the
	 * number of pending responses for that request. This info includes the
	 * replication degree, the responses received so far for that request and
	 * its packet number.
	 */
	private Map<XMLGregorianCalendar, PendingResponseInfo> pendingResponses;
	
	/**
	 * Initializes the pending responses with an empty HashMap.
	 */
	private WaitingResponses() {
		pendingResponses =
				new HashMap<XMLGregorianCalendar, PendingResponseInfo>();
	}
	
	/**
	 * Adds a request to the WaitingResponses.
	 * @param timestamp
	 * 		the timestamp of the request
	 * @param replicationDegree
	 * 		the number of copies sent for that request
	 */
	public void addRequest(	XMLGregorianCalendar timestamp,
							int replicationDegree) {
		this.pendingResponses.put(
				timestamp,
				new PendingResponseInfo(replicationDegree));
	}
	
	/**
	 * Adds a request to the WaitingResponses.
	 * @param timestamp
	 * 		the timestamp of the request
	 */
	public void addRequest(XMLGregorianCalendar timestamp) {
		this.addRequest(timestamp, 1);
	}
	
	/**
	 * Checks if there's a request with given timestamp waiting for responses.
	 * @param timestamp
	 * 		the request's timestamp
	 * @return
	 * 		true if there's a request with given timestamp waiting for
	 * 		responses
	 */
	public boolean contains(XMLGregorianCalendar timestamp) {
		return this.pendingResponses.containsKey(timestamp);
	}
	
	/**
	 * Sets the replication degree for the request with the given timestamp.
	 * New servers (bindings) may be added or removed on run time. If the new
	 * replication degree is smaller than the responses received so far, the
	 * request is deleted.
	 * @param timestamp
	 * 		the request's timestamp
	 * @param replicationDegree
	 * 		the new replication degree
	 */
	public void setReplicationDegree(	XMLGregorianCalendar timestamp,
										int replicationDegree) {
		PendingResponseInfo info = this.pendingResponses.get(timestamp);
		if (info != null) {
			if (info.getResponsesReceived() >= replicationDegree) {
				this.pendingResponses.remove(timestamp);
			} else {
				info.setReplicationDegree(replicationDegree);
			}
		}
	}
	
	/**
	 * Decreases the replication degree for the request with the given
	 * timestamp. If the replication degree becomes equal too or smaller than
	 * the responses received, the request is removed.
	 * @param timestamp
	 * 		the request's timestamp
	 */
	public void decreaseReplicationDegree(XMLGregorianCalendar timestamp) {
		PendingResponseInfo info = this.pendingResponses.get(timestamp);
		if (info != null) {
			info.setReplicationDegree(info.getReplicationDegree() - 1);
			if (info.getReplicationDegree() <= info.getResponsesReceived()) {
				this.pendingResponses.remove(timestamp);
			}
		}
	}
	
	/**
	 * Gets the packet number of a request with the given timestamp.
	 * @param timestamp
	 * 		the request's timestamp
	 * @return
	 * 		an Integer object with the packet number or null if the there's no
	 * 		pending request with the given timestamp or if the packet number
	 * 		for that request isn't set
	 */
	public Integer getPacketNumber(XMLGregorianCalendar timestamp) {
		PendingResponseInfo info = this.pendingResponses.get(timestamp);
		if (info == null || !info.packetNumberIsSet()) {
			return null;
		}
		return new Integer(info.getPacketNumber());
	}
	
	/**
	 * Sets the packet number of a request with the given timestamp if it
	 * exists.
	 * @param timestamp
	 * 		the request's timestamp
	 * @param number
	 * 		the desired packet number
	 */
	public void setPacketNumber(XMLGregorianCalendar timestamp, int number) {
		PendingResponseInfo info = this.pendingResponses.get(timestamp);
		if (info != null) {
			info.setPacketNumber(number);
		}
	}
	
	/**
	 * Checks if the packet number is set for a request with the given
	 * timestamp.
	 * @param timestamp
	 * 		the request's timestamp
	 * @return
	 * 		true if there's a request with given timestamp waiting responses
	 * 		and its packet number is set, false otherwise
	 */
	public boolean packetNumberIsSet(XMLGregorianCalendar timestamp) {
		PendingResponseInfo info = this.pendingResponses.get(timestamp);
		if (info != null) {
			return info.packetNumberIsSet();
		}
		return false;
	}
	
	/**
	 * Notifies this class that a response was received for the request with
	 * the given timestamp.
	 * @param timestamp
	 * 		the request's timestamp
	 */
	public void responseReceived(XMLGregorianCalendar timestamp) {
		PendingResponseInfo info = this.pendingResponses.get(timestamp);
		if (info != null) {
			if (info.getResponsesReceived() ==
					info.getReplicationDegree() - 1) {
				this.pendingResponses.remove(timestamp);
			} else {
				info.responseReceived();
			}
		}
	}
	
}
