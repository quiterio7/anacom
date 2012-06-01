package anacom.replication.test;

import java.math.BigInteger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import anacom.presentationserver.server.replication.GenericComparator;
import anacom.presentationserver.server.replication.protocols.FailureTolerator;
import anacom.shared.stubs.client.PhoneType;

public abstract class ReplicationProtocolTestCase
extends AnacomReplicationTestCase {
	
	/**
	 * The replication protocol being tested.
	 */
	protected FailureTolerator protocol = null;
	
	private final static XMLGregorianCalendar CORRECT_TIMESTAMP;
	private final static XMLGregorianCalendar DELAYED_TIMESTAMP;
	private final static int CORRECT_MILLISECOND = 2;
	private final static int DELAYED_MILLISECOND = 1;
	
	protected final static String PHONE_NUMBER = "910000000";
	protected final static String CORRECT_BALANCE = "10";
	protected final static String WRONG_BALANCE = "5";
	
	private static final void initializeCorrectTimestamp() {
		CORRECT_TIMESTAMP.setYear(YEAR);
		CORRECT_TIMESTAMP.setMonth(MONTH);
		CORRECT_TIMESTAMP.setDay(DAY);
		CORRECT_TIMESTAMP.setHour(HOUR);
		CORRECT_TIMESTAMP.setMinute(MINUTE);
		CORRECT_TIMESTAMP.setSecond(SECOND);
		CORRECT_TIMESTAMP.setMillisecond(CORRECT_MILLISECOND);		
	}
	
	private static final void initializeDelayedTimestamp() {
		DELAYED_TIMESTAMP.setYear(YEAR);
		DELAYED_TIMESTAMP.setMonth(MONTH);
		DELAYED_TIMESTAMP.setDay(DAY);
		DELAYED_TIMESTAMP.setHour(HOUR);
		DELAYED_TIMESTAMP.setMinute(MINUTE);
		DELAYED_TIMESTAMP.setSecond(SECOND);
		DELAYED_TIMESTAMP.setMillisecond(DELAYED_MILLISECOND);
	}
	
	/**
	 * All instances of this test will have the same CORRECT_TIMESTAMP and
	 * DELAYED_TIMESTAMP.
	 */
	static {
		try {
			CORRECT_TIMESTAMP =
					DatatypeFactory.newInstance().newXMLGregorianCalendar();
			initializeCorrectTimestamp();
			DELAYED_TIMESTAMP =
					DatatypeFactory.newInstance().newXMLGregorianCalendar();
			initializeDelayedTimestamp();
		} catch (DatatypeConfigurationException dce) {
			throw new RuntimeException(dce); // KILL TEST
		}
	}
	
	/**
	 * Factory method used to create a correct replica response. A PhoneType is
	 * used here, but any other kind of response (including exceptions) could
	 * be used. it's assumed that the type of response won't affect the
	 * protocol's behaviour, as it should be.
	 * @return
	 * 		a correct response
	 */
	protected PhoneType createCorrectResponse() {
		PhoneType response = new PhoneType();
		response.setNumber(PHONE_NUMBER);
		response.setBalance(new BigInteger(CORRECT_BALANCE));
		response.setTimestamp(CORRECT_TIMESTAMP);
		return response;
	}
	
	/**
	 * Factory method used to create a delayed replica response. A PhoneType is
	 * used here, but any other kind of response (including exceptions) could
	 * be used. it's assumed that the type of response won't affect the
	 * protocol's behaviour, as it should be.
	 * @return
	 * 		a delayed response
	 */
	protected PhoneType createDelayedResponse() {
		PhoneType response = new PhoneType();
		response.setNumber(PHONE_NUMBER);
		response.setBalance(new BigInteger(WRONG_BALANCE));
		response.setTimestamp(DELAYED_TIMESTAMP);
		return response;
	}
	
	/**
	 * Asserts that the protocol has terminated and the response was determined
	 * correctly.
	 */
	protected void assertCorrectResponseDetermined() {
		assertTrue("Silent failure protocol didn't terminate after second " +
				"response but it should have",
				this.protocol.hasTerminated());
		assertTrue("Silent failure protocol didn't determine a response " +
				"after the protocol terminated but it should have",
				this.protocol.hasResponse());
		assertTrue("The response determined by the protocol isn't the " +
				"correct response",
				(new GenericComparator()).equals(
						this.protocol.getResponse(),
						createCorrectResponse()));		
	}
	
	/**
	 * Asserts that the protocol hasn't terminated yet and no response was
	 * determined.
	 */
	protected void assertProtocolNotTerminated() {
		assertFalse("Silent failure protocol terminated after first " +
				"response, this shouldn't have happened",
				this.protocol.hasTerminated());
		assertFalse("Silent failure protocol determined a response before" +
				"it terminated, this shouldn't have happened",
				this.protocol.hasResponse());
		assertEquals("Silent failure protocol returned a response instead " +
				"of null before it terminated",
				this.protocol.getResponse(),
				null);		
	}

	/**
	 * Creates a replication protocol test case.
	 * @param protocol
	 * 		the replication protocol (it's assumed that the protocol already
	 * 		contains the desired replication degree or that it'll be set by
	 * 		the subclass)
	 */
	public ReplicationProtocolTestCase(FailureTolerator protocol) {
		super();
		this.protocol = protocol;
	}
	
	/**
	 * Creates a replication protocol test case.
	 * @param protocol
	 * 		the replication protocol (it's assumed that the protocol already
	 * 		contains the desired replication degree or that it'll be set by
	 * 		the subclass)
	 * @param msg
	 * 		a TestCase message
	 */
	public ReplicationProtocolTestCase(FailureTolerator protocol, String msg) {
		super(msg);
		this.protocol = protocol;
	}
	
	@Override
	protected void setUp() {
		protocol.clearResponses();
	}
	
	@Override
	protected void tearDown() {
		protocol.clearResponses();
	}
	
}
