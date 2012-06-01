package anacom.presentationserver.server.replication.protocols;

import java.util.Comparator;
import java.util.PriorityQueue;

import javax.xml.datatype.XMLGregorianCalendar;

import anacom.presentationserver.server.replication.TimestampMaster;

/**
 * Implements the replication protocol.
 * WARNING: this abstraction can be used because the presentation server
 * serializes requests.
 */
public abstract class FailureTolerator {
	
	/**
	 * Initial capacity for the response queue.
	 */
	private final static int INITIAL_CAPACITY = 11;
	
	/**
	 * The total number of replications of the application server. This is used
	 * by the protocol to determine how many responses it has to wait for.
	 */
	private int replicationDegree = 0;

	/**
	 * Queue containing the received messages ordered by timestamp (highest
	 * to smallest).
	 */
	private PriorityQueue<Object> responses =
			new PriorityQueue<Object>(
					INITIAL_CAPACITY,
					new ResponseComparator());
	
	/**
	 * Will contain the final response after successful execution of the
	 * replication protocol.
	 */
	private Object finalResponse = null;
	
	/**
	 * This class is used by the PriorityQueue to establish order in response
	 * objects. The order goes from the highest timestamp to the smallest.
	 */
	private class ResponseComparator implements Comparator<Object>{
		@Override
		public int compare(Object o1, Object o2) {
			TimestampMaster master = new TimestampMaster();
			XMLGregorianCalendar timestamp1 = master.getTimestamp(o1);
			XMLGregorianCalendar timestamp2 = master.getTimestamp(o2);
			return timestamp2.compare(timestamp1);
		}
	}
	
	/**
	 * @return the final response of the last execution of this replication
	 * protocol. If there was no execution of this protocol yet or if there's
	 * an undergoing execution, null is returned
	 */
	public Object getResponse() { return this.finalResponse; }
	
	/**
	 * Sets the final response of the protocol's execution.
	 * @param resp
	 * 		the desired response
	 */
	protected void setResponse(Object resp) { this.finalResponse = resp; }
	
	/**
	 * @return the queue with the application server responses received so far
	 */
	protected PriorityQueue<Object> getCurrentResponses() {
		return this.responses;
	}
	
	/**
	 * @return the number of replications of the application server
	 */
	public int getReplicationDegree() { return this.replicationDegree; }
	
	/**
	 * Sets the number of replications of the application server. This method
	 * must be invoked at least once before any execution of the protocol
	 * because it must know the degree of replication, or else it might not
	 * be able to determine a correct response.
	 * @param replics
	 * 		the degree of replication of the application server
	 */
	public void setReplicationDegree(int replics) {
		this.replicationDegree = replics;
	}
	
	/**
	 * @return true if a final response was determined, false otherwise
	 */
	public boolean hasResponse() { return this.finalResponse != null; }
	
	/**
	 * @return true if the replication protocol has terminated
	 */
	public abstract boolean hasTerminated();
	
	/**
	 * Handles the given response. If the replication protocol has terminated,
	 * the resulting response will be stored in the finalResponse attribute.
	 * @param resp
	 * 		a generic Object containing a response (may be an exception)
	 */
	public void handleResponse(Object resp) {
		this.responses.add(resp);
		determineResponse();
	}
	
	/**
	 * Tries to determine a correct response with the responses it currently
	 * has.
	 */
	public abstract void determineResponse();
	
	/**
	 * Clears the received and final responses. This MUST be invoked before
	 * a new execution of the replication protocol.
	 */
	public void clearResponses() {
		this.responses = new PriorityQueue<Object>(
				INITIAL_CAPACITY,
				new ResponseComparator());
		this.finalResponse = null;
	}
	
}
