package anacom.replication.test;

import java.math.BigInteger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import anacom.presentationserver.server.replication.protocols.ByzantineFailureTolerator;
import anacom.shared.stubs.client.PhoneType;

public class ByzantineFailureProtocolTestCase
extends ReplicationProtocolTestCase {

	/**
	 * The minimum replication degree required for that allows the byzantine
	 * failure replication protocol to work properly.
	 */
	private final static int MINIMUM_REPLICATION_DEGREE = 5;
	
	private final static int SERVER_DOWN_REPLICATION_DEGREE = 4;
	private final static int INCREASED_REPLICATION_DEGREE = 7;
	
	private final static XMLGregorianCalendar BYZANTINE_TIMESTAMP;
	private final static int BYZANTINE_MILLISECOND = 10;
	
	private static final void initializeByzantineTimestamp() {
		BYZANTINE_TIMESTAMP.setYear(YEAR);
		BYZANTINE_TIMESTAMP.setMonth(MONTH);
		BYZANTINE_TIMESTAMP.setDay(DAY);
		BYZANTINE_TIMESTAMP.setHour(HOUR);
		BYZANTINE_TIMESTAMP.setMinute(MINUTE);
		BYZANTINE_TIMESTAMP.setSecond(SECOND);
		BYZANTINE_TIMESTAMP.setMillisecond(BYZANTINE_MILLISECOND);
	}
	
	/**
	 * All instances of this test will have the same BYZANTINE_TIMESTAMP
	 */
	static {
		try {
			BYZANTINE_TIMESTAMP =
					DatatypeFactory.newInstance().newXMLGregorianCalendar();
			initializeByzantineTimestamp();
		} catch (DatatypeConfigurationException dce) {
			throw new RuntimeException(dce); // KILL TEST
		}
	}
	
	public ByzantineFailureProtocolTestCase() {
		super(new ByzantineFailureTolerator());
	}
	
	public ByzantineFailureProtocolTestCase(String msg) {
		super(new ByzantineFailureTolerator(), msg);
	}
	
	@Override
	protected void setUp() {
		super.setUp();
		this.protocol.setReplicationDegree(MINIMUM_REPLICATION_DEGREE);
	}
	
	/**
	 * Factory method used to create a byzantine replica response. A PhoneType
	 * is used here, but any other kind of response (including exceptions)
	 * could be used. it's assumed that the type of response won't affect the
	 * protocol's behaviour, as it should be.
	 * @return
	 * 		a byzantine response
	 */
	protected PhoneType createByzantineResponse() {
		PhoneType response = new PhoneType();
		response.setNumber(PHONE_NUMBER);
		response.setBalance(new BigInteger(WRONG_BALANCE));
		response.setTimestamp(BYZANTINE_TIMESTAMP);
		return response;
	}
	
	/**
	 * Test 1 - Test protocol execution with correct responses from all
	 * replicas.
	 */
	public void testCorrectResponseFromAllReplicas() {
		// Act (receive first response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive second response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive third response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive forth response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol should have a response)
		assertCorrectResponseDetermined();
		
		// Act (receive final response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (fifth response shouldn't affect the final response)
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 2 - Test protocol receiving one delayed response and the rest
	 * correct responses.
	 */
	public void testOneDelayedResponse() {
		// Act
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 3 - Test protocol receiving two equal delayed responses and the
	 * rest correct responses. Only some combinations are tested.
	 */
	public void testTwoDelayedResponses() {
		// Act
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 4 - Test receiving of a delayed response after the protocol
	 * determined a correct response.
	 */
	public void testFifthResponseDelayed() {
		// Act (receive correct responses)
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
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
	 * Test 5 - Test receiving of one byzantine response and the rest correct
	 * responses.
	 */
	public void testOneByzantineResponse() {
		// Act
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createByzantineResponse());
		
		// Assert
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 6 - Test protocol receiving one delayed response, one byzantine and
	 * the rest correct responses. Only some combinations are tested.
	 */
	public void testOneDelayedOneByzantineResponses() {
		// Act
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 7 - Test receiving of a byzantine response after the protocol
	 * determined a correct response.
	 */
	public void testFifthResponseByzantine() {
		// Act (receive correct responses)
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert (determined correct response)
		assertCorrectResponseDetermined();
		
		// Act (receive delayed response)
		this.protocol.handleResponse(createByzantineResponse());
		
		// Assert (determined response should be the same)
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 8 - Test protocol with a server down (replication degree is 4).
	 */
	public void testServerDown() {
		// Arrange
		this.protocol.setReplicationDegree(SERVER_DOWN_REPLICATION_DEGREE);
		
		// Act (receive first response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive second response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive third response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive forth response)
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol should have a response)
		assertCorrectResponseDetermined();
		
		// Arrange
		this.protocol.clearResponses();
		
		// Act (with byzantine response)
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert
		assertCorrectResponseDetermined();
	}
	
	/**
	 * Test 9 - Test protocol with two more servers. This is only meant to
	 * check if the protocol is correctly adapting to increases in number of
	 * servers.
	 */
	public void testIncreasedReplicationDegree() {
		// Arrange
		this.protocol.setReplicationDegree(INCREASED_REPLICATION_DEGREE);
		
		// Act (receive first and second response)
		this.protocol.handleResponse(createCorrectResponse());
		this.protocol.handleResponse(createDelayedResponse());
		this.protocol.handleResponse(createByzantineResponse());
		this.protocol.handleResponse(createCorrectResponse());
		
		// Assert (protocol shouldn't have terminated)
		assertProtocolNotTerminated();
		
		// Act (receive third response)
		this.protocol.handleResponse(createDelayedResponse());
		
		// Assert (protocol should have determined correct response)
		assertCorrectResponseDetermined();
	}
	
}
