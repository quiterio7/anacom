package anacom.service.test;

import pt.ist.fenixframework.pstm.Transaction;
import anacom.domain.Operator;
import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

public class CommunicationServiceTest extends AnacomServiceTestCase {
	
	/**
	 * Communication Type (Voice, Video)
	 */
	protected final String communicationType;

	/**
	 * Operator DATA 
	 */
	protected final static String OPERATOR_NAME = "TMN";
	protected final static String OPERATOR_PREFIX = "96";
	protected final static int SMS_COST = 1;
	protected final static int VOICE_COST = 2;
	protected final static int VIDEO_COST = 3;
	protected final static int TAX = 20;
	protected final static int BONUS = 0;
	
	protected final static String OPERATOR_NAME2 = "Vodafone";
	protected final static String OPERATOR_PREFIX2 = "91";
	protected final static int SMS_COST2 = 1;
	protected final static int VOICE_COST2 = 3;
	protected final static int VIDEO_COST2 = 3;
	protected final static int TAX2 = 50;
	
	/**
	 * Phone DATA
	 */
	protected final static String EXISTING_96_NUMBER = "961311111";
	protected final static String EXISTING_91_NUMBER = "911311111";
	protected final static String EXISTING_91_NUMBER2 = "911322222";
	protected final static String EXISTING_912G_NUMBER = "913333333";
	protected final static String EXISTING_912G_NUMBER2 = "913444444";
	protected final static String NON_EXISTING_NUMBER = "960001110";
	protected final static String NON_EXISTING_OPERATOR = "930001110";
	protected final static int SOURCE_BALANCE = 100;
	protected final static int DESTINATION_BALANCE = 0;
	
	protected final static PhoneStateRepresentation
		stateRepresention = PhoneStateRepresentation.getInstance();
	protected final static CommunicationRepresentation
		communicationRepresentation = CommunicationRepresentation.getInstance();
	
	/**
	 * Invalid inputs
	 */
	protected final static String INVALID_NULL_NUMBER = null;
	protected final static String INVALID_SMALLER_NUMBER = "91131111";
	protected final static String INVALID_BIGGER_NUMBER = "9113111111";
	protected final static String INVALID_LETTER_NUMBER = "91131111a";
		
	/**
	 * Test Constructor 
	 * @param name the name of the Test Case. For dynamic invocation.
	 * @param cummunicationType video or voice
	 */
	public CommunicationServiceTest(String name, String communicationType) {
		super(name);
		this.communicationType = communicationType;
	}
	
	/**
	 * Test Constructor 
	 * @param cummunicationType video or voice
	 */
	public CommunicationServiceTest(String communicationType) {
		super();
		this.communicationType = communicationType;
	}
	
	public void setUp() {
		super.setUp();
		addOperator(
				OPERATOR_NAME,
				OPERATOR_PREFIX,
				SMS_COST,
				VOICE_COST,
				VIDEO_COST,
				TAX,
				BONUS);
		addOperator(
				OPERATOR_NAME2,
				OPERATOR_PREFIX2,
				SMS_COST2,
				VOICE_COST2,
				VIDEO_COST2,
				TAX2,
				BONUS);
		
		addPhone3G(EXISTING_96_NUMBER);
		addPhone3G(EXISTING_91_NUMBER);
		addPhone3G(EXISTING_91_NUMBER2);
		addPhone2G(EXISTING_912G_NUMBER);
		addPhone2G(EXISTING_912G_NUMBER2);
		changePhoneState(EXISTING_96_NUMBER, stateRepresention.getOnState());
		changePhoneState(EXISTING_91_NUMBER, stateRepresention.getOnState());
		changePhoneState(EXISTING_91_NUMBER2, stateRepresention.getOnState());
		changePhoneState(EXISTING_912G_NUMBER, stateRepresention.getOnState());
		changePhoneState(EXISTING_912G_NUMBER2, stateRepresention.getOnState());
		increasePhoneBalance(EXISTING_91_NUMBER, SOURCE_BALANCE);
		increasePhoneBalance(EXISTING_912G_NUMBER, SOURCE_BALANCE);
	}
	
	/**
	 * Calculates the cost of a communication from one Phone to another.
	 * @param source		the source's number
	 * @param destination	the destination's number
	 * @param size			the communication's dimension (duration or length)
	 * @return				the communication's cost
	 */
	protected int calcCost(String source, String destination, int size) {
		Operator opSource = getOperator(source.substring(0, 2));
		
		boolean commited = false;
		try {
			int cost = 0;
			Transaction.begin();
			if(communicationType.equals(
					communicationRepresentation.getVoiceCommunication())) {
				cost = opSource.getVoiceCost() * size;
			} else if (communicationType.equals(
					communicationRepresentation.getVideoCommunication())) {
				cost = opSource.getVideoCost() * size;
			} else {
				cost = opSource.getSmsCost() * ((size - 1)/100 + 1);
			}
			if (!opSource.getPrefix().equals(destination.substring(0, 2))) {
				cost += (cost * opSource.getTax()) / 100;
			}
			Transaction.commit();
			commited = true;
			return cost;
		} finally { if (!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Used to verify if the state of the database remains after a failed
	 * service.
	 * @param source				the source's phone number
	 * @param communicationCount	the number of established calls in the
	 * 								source phone before the service
	 * @param balance				the source's balance before the service
	 */
	protected void assertSourceUnchanged(String source,
									   int communicationCount,
									   int balance) {
		assertEquals("The source's number of established calls should be "
				+ "the same (" + communicationCount + ") but it is "
				+ countEstablishedCallsInPhone(source),
				communicationCount, countEstablishedCallsInPhone(source));
		assertEquals("The source phone's balance should be the same ("
				+ balance + ") but it is " + getPhoneBalance(source),
				balance, getPhoneBalance(source));
	}
	
	/**
	 * Used to verify if the state of the database remains after a failed
	 * service.
	 * @param destination			the destination's phone number
	 * @param communicationCount	the number of received calls in the
	 * 								destination phone before the service
	 * @param balance				the destination's balance before the
	 * 								service
	 */
	protected void assertDestinationUnchanged(String destination,
									 		int communicationCount,
									 		int balance) {
		assertEquals("The destination's number of received calls should be "
				+ "the same (" + communicationCount + ") but it is "
				+ countReceivedCallsInPhone(destination),
				communicationCount, countReceivedCallsInPhone(destination));
		assertEquals("The destination phone's balance should be the same ("
				+ balance + ") but it is " + getPhoneBalance(destination),
				balance, getPhoneBalance(destination));
	}
	
	/**
	 * Used to verify if the program didn't erroneously change the received
	 * calls on the source phone
	 * @param source				the source's phone number
	 * @param communicationCount	the number of established calls in the
	 * 								destination phone before the service
	 */
	protected void assertReceivedCallsUnchanged(String source,
											int communicationCount) {
		assertEquals("The source's number of received calls should be the "
				+ "same (" + communicationCount + ") but it is "
				+ countReceivedCallsInPhone(source),
				communicationCount, countReceivedCallsInPhone(source));
	}
	
	/**
	 * Used to verify if the program didn't erroneously change the established
	 * calls on the destination phone
	 * @param destination			the destination's phone number
	 * @param communicationCount	the number of established calls in the
	 * 								destination phone before the service
	 */
	protected void assertEstablishedCallsUnchanged(String destination,
												int communicationCount) {
		assertEquals("The destination's number of established calls should "
				+ "be the " + "same (" + communicationCount + ") but it is "
				+ countEstablishedCallsInPhone(destination),
				communicationCount, countEstablishedCallsInPhone(destination));
	}
	
	/**
	 * Used to verify if the state of the database remains after a failed
	 * service.
	 * @param source								the source's phone number
	 * @param destination							the destination's phone
	 * 												number
	 * @param sourceEstablishedCommunications		the number of established
	 * 												communications in the
	 * 												source before the service
	 * @param sourceReceivedCommunications			the number of received
	 * 												communications in the
	 * 												source before the service
	 * @param destinationEstablishedCommunications	the number of established
	 * 												communications in the
	 * 												destination before the
	 * 												service
	 * @param destinationReceivedCommunications		the number of received
	 * 												communications in the
	 * 												destination before the
	 * 												service
	 * @param sourceBalance							the source's balance before
	 * 												the service
	 * @param destinationBalance					the destination's balance
	 * 												before the service
	 */
	protected void assertDataUnchanged(String source,
									String destination,
									int sourceEstablishedCommunications,
									int sourceReceivedCommunications,
									int destinationEstablishedCommunications,
									int destinationReceivedCommunications,
									int sourceBalance,
									int destinationBalance) {
		assertSourceUnchanged(source,
							sourceEstablishedCommunications,
							sourceBalance);
		assertReceivedCallsUnchanged(source,
									sourceReceivedCommunications);
		assertDestinationUnchanged(destination,
								destinationReceivedCommunications,
								destinationBalance);
		assertEstablishedCallsUnchanged(destination,
										destinationEstablishedCommunications);
	}
	
	/**
	 * Asserts that the state of the source of a call is correct
	 * @param source	the source's Phone number
	 * @param state		the source's state
	 */
	protected void assertSourceStateUnchanged(String source, String state) {
		assertEquals("The source's state should be " + state + " but it is "
				+ getPhoneState(source),
				state, getPhoneState(source));
	}
	
	/**
	 * Asserts that the state of the destination of a call is correct
	 * @param source	the destination's Phone number
	 * @param state		the destination's state
	 */
	protected void assertDestinationStateUnchanged(String destination,
												String state) {
		assertEquals("The destination's state should be " + state
				+ " but it is " + getPhoneState(destination),
				state, getPhoneState(destination));
	}
	
	/**
	 * Asserts that the states in the source and destination are correct
	 * @param source			the source's Phone number
	 * @param destination		the destination's Phone number
	 * @param sourceState		the source's state
	 * @param destinationState	the destination's state
	 */
	protected void assertStateUnchanged(String source,
										String destination,
										String sourceState,
										String destinationState) {
		assertSourceStateUnchanged(source, sourceState);
		assertDestinationStateUnchanged(destination, destinationState);
	}
	
	/**
	 * Asserts that the balance of two Phone were updated correctly after a
	 * successful communication
	 * @param source		the source's Phone number
	 * @param destination	the destination's Phone number
	 * @param cost			the communication's cost
	 */
	protected void assertBalanceUpdated(String source,
										String destination,
										int cost) {
		assertEquals("The source phone's balance should have been decreased "
				+ "by " + cost + " (balance should be "
				+ (SOURCE_BALANCE - cost) + " but it is "
				+ getPhoneBalance(source) + ")",
				SOURCE_BALANCE - cost,
				getPhoneBalance(source));
		assertEquals("The destination phone's balance should be the same ("
				+ DESTINATION_BALANCE + ") but it is "
				+ getPhoneBalance(destination),
				DESTINATION_BALANCE, getPhoneBalance(destination));
	}
	
	/**
	 * Asserts that new communications were added in the source's established
	 * communications and in the destination's received communications
	 * @param source							the source's Phone number
	 * @param destination						the destination's Phone number
	 * @param sourceEstablishedCommunications	the number of established
	 * 											communications in the source
	 * 											Phone initially
	 * @param destinationReceivedCommunications	the number of received
	 * 											communications in the
	 * 											destination Phone initially
	 */
	protected void assertCommunicationsAdded(String source,
										String destination,
										int sourceEstablishedCommunications,
										int destinationReceivedCommunications) {
		assertEquals("The source's number of established calls should have "
				+ "increased by one",
				sourceEstablishedCommunications + 1,
				countEstablishedCallsInPhone(source));
		assertEquals("The destination's number of received calls should have "
				+ "increased by one",
				destinationReceivedCommunications + 1,
				countReceivedCallsInPhone(destination));
	}
	
}
