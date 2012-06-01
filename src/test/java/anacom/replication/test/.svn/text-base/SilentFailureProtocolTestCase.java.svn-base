package anacom.replication.test;

import anacom.presentationserver.server.replication.protocols.SilentFailureTolerator;

public class SilentFailureProtocolTestCase
extends ReplicationProtocolTestCase {
	
	/**
	 * The minimum replication degree required for that allows the silent
	 * failure replication protocol to work properly.
	 */
	private final static int MINIMUM_REPLICATION_DEGREE = 3;
	
	private final static int SERVER_DOWN_REPLICATION_DEGREE = 2;
	private final static int INCREASED_REPLICATION_DEGREE = 5;
	
	public SilentFailureProtocolTestCase() {
		super(new SilentFailureTolerator());
	}
	
	public SilentFailureProtocolTestCase(String msg) {
		super(new SilentFailureTolerator(), msg);
	}
	
	@Override
	protected void setUp() {
		super.setUp();
		this.protocol.setReplicationDegree(MINIMUM_REPLICATION_DEGREE);
	}
	
	/**
	 * Test 1 - Test protocol execution with correct responses from all
	 * replicas.
	 */
	public void testCorrectResponsesFromAllReplicas() {
		// Act (receive first response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive second response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol should have a response)
		assertCorrectResponseDetermined();
		
		// Act (receive final response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (third response shouldn't affect the final response)
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 2 - Test protocol receiving first a correct response and then a
	 * delayed response.
	 */
	public void testFirstResponseCorrectSecondDelayed() {
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 3 - Test protocol receiving first a delayed response and then a
	 * correct response.
	 */
	public void testFirstResponseDelayedSecondCorrect() {
		// Act (receive delayed response)
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (received correct response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol should have determined correct response)
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 4 - Test receiving of a delayed response after the protocol
	 * determined a correct response.
	 */
	public void testThirdResponseDelayed() {
		// Act (receive correct responses)
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (determined correct response)
		assertCorrectResponseDetermined();
		
		// Act (receive delayed response)
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert (determined response should be the same)
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 5 - Test protocol with a server down (replication degree is 2).
	 */
	public void testServerDown() {
		// Arrange
		this.protocol.setReplicationDegree(SERVER_DOWN_REPLICATION_DEGREE);
		
		// Act (receive first response)
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive second response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol should have determined correct response)
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 6 - Test protocol with two more servers. This is only meant to
	 * check if the protocol is correctly adapting to increases in number of
	 * servers.
	 */
	public void testIncreasedReplicationDegree() {
		// Arrange
		this.protocol.setReplicationDegree(INCREASED_REPLICATION_DEGREE);
		
		// Act (receive first and second response)
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive third response)
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert (protocol should have determined correct response)
		assertCorrectResponseDetermined();
	}

}
