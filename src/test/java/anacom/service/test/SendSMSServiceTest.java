package anacom.service.test;

import pt.ist.fenixframework.pstm.Transaction;
import anacom.domain.Communication;
import anacom.domain.SMS;
import anacom.services.MakeCallService;
import anacom.services.ReceiveCallService;
import anacom.services.ReceiveSMSService;
import anacom.services.SendSMSService;
import anacom.shared.dto.CallDTO;
import anacom.shared.dto.SMSDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;

public class SendSMSServiceTest extends CommunicationServiceTest {
	
	/**
	 * Message DATA 
	 */
	private final static String MESSAGE = "Hello World!";
	
	/**
	 * Invalid input
	 */
	private final static String INVALID_NULL_MESSAGE = null;
	private final static String INVALID_EMPTY_MESSAGE = "";
	
	private final static int MOCK_COST = 0;
	private final static long MOCK_START_TIME = 0;
	
	/**
	 * Test Constructor 
	 * @param name the name of the Test Case. For dynamic invocation.
	 * @param cummunicationType video or voice
	 */
	public SendSMSServiceTest(String name) {
		super(name, communicationRepresentation.getSMSCommunication());
	}
	
	/**
	 * Test Constructor 
	 * @param cummunicationType video or voice
	 */
	public SendSMSServiceTest() {
		super(communicationRepresentation.getSMSCommunication());
	}
	
	/**
	 * Asserts that the stored SMS data in the source and destination is
	 * correct
	 * @param source		the source's Phone number
	 * @param destination	the destination's Phone number
	 */
	protected void assertSMSDataIsCorrect(String source, String destination) {
		Communication lastMade = getLastMadeCommunication(source);
		Communication lastReceived = getLastReceivedCommunication(destination);
		SMS made = null;
		SMS received = null;
		if (lastMade instanceof SMS && lastReceived instanceof SMS) {
			made = (SMS)lastMade;
			received = (SMS)lastReceived;
		} else {
			fail("The last stored communications aren't of type SMS "
					+ "(source's is " + lastMade.getClass() + " and "
					+ "destination's is " + lastReceived.getClass() + ")");
		}
		int expectedCost = calcCost(source, destination, MESSAGE.length());
		
		boolean commited = false;
		try {
			Transaction.begin();
			assertEquals("The other party in " + source + " should be "
					+ destination + " but it is " + made.getOtherParty(),
					destination, made.getOtherParty());
			assertEquals("The other party in " + destination + " should be "
					+ source + " but it is " + received.getOtherParty(),
					source, received.getOtherParty());
			assertEquals("The message stored in " + source + " should be \""
					+ MESSAGE + "\" but it is " + made.getMessage(),
					MESSAGE, made.getMessage());
			assertEquals("The message stored in " + destination + " should "
					+ "be \"" + MESSAGE + "\" but it is "
					+ received.getMessage(),
					MESSAGE, received.getMessage());
			assertEquals("The cost stored in " + source + " should be "
					+ expectedCost + " but it is " + made.getCost(),
					expectedCost, made.getCost());
			Transaction.commit();
			commited = true;
		} finally { if (!commited) { Transaction.abort(); } }
	}

	/**
	 * Tests a successful SMS.
	 * Successful SMS tests are all alike apart from source and destination.
	 * @param source		the SMS's source number
	 * @param destination	the SMS's destination number
	 */
	protected void testSuccessfulSMS(String source, String destination) {
		// Arrange
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(source);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(source);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(destination);
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(destination);
		String sourceState = getPhoneState(source);
		String destinationState = getPhoneState(destination);
		SendSMSDTO dto = new SendSMSDTO(MESSAGE, source, destination);
		SMSDTO response = null;
		SMSDTO wantedResponse = new SMSDTO(
				dto.getMessage(),
				dto.getSource(),
				dto.getDestination(),
				0);
		SendSMSService sendService = new SendSMSService(dto);
		ReceiveSMSService receiveService = null;
		
		// Act
		sendService.execute();
		response = sendService.getSMSDTO();
		receiveService = new ReceiveSMSService(response);
		receiveService.execute();
		
		// Assert
		assertEquals("The response to send SMS on the source should be "
				+ "(Source:" + wantedResponse.getSource() + ", Destination:"
				+ wantedResponse.getDestination() + ", Message:"
				+ wantedResponse.getMessage() + ", Cost:"
				+ wantedResponse.getCost() + ") but it was (Source:"
				+ response.getSource() + ", Destination:"
				+ response.getDestination() + ", Message:"
				+ response.getMessage() + ", Cost:" + response.getCost() + ")",
				wantedResponse, response);
		assertBalanceUpdated(
				source,
				destination,
				calcCost(source, destination, MESSAGE.length()));
		assertCommunicationsAdded(
				source,
				destination,
				sourceEstablishedCommunications,
				destinationReceivedCommunications);
		assertStateUnchanged(
				source,
				destination,
				sourceState,
				destinationState);
		assertSMSDataIsCorrect(source, destination);
		assertReceivedCallsUnchanged(source, sourceReceivedCommunications);
		assertEstablishedCallsUnchanged(destination,
										destinationEstablishedCommunications);
	}

	/**
	 * Tests a SMS from a phone in an invalid state.
	 * The source must already be in the desired state.
	 * @param source		the source's number
	 * @param destination	the destination's number
	 * @param state			the String representation of the invalid state
	 */
	protected void testSMSFromSourceInInvalidState(String source,
												String destination,
												String state) {
		// Arrange
		String exceptionNumber = null, exceptionState = null;
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(source);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(source);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(destination);
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(destination);
		SendSMSDTO dto = new SendSMSDTO(MESSAGE, source, destination);
		SendSMSService service = new SendSMSService(dto);
											
		// Act
		try {
			service.execute();
			fail("Should have thrown InvalidStateSendSMS");
		} catch (InvalidStateSendSMS isss) {
			exceptionNumber = isss.getNumber();
			exceptionState = isss.getState();
		}
											
		// Assert
		assertEquals("The source phone's number in the exception should be "
				+ source + " but it was " + exceptionNumber,
				source, exceptionNumber);
		assertEquals("The source phone's state in the exception should be "
				+ state + " but it was " + exceptionState,
				state, exceptionState);
		assertSourceStateUnchanged(source, state);
		assertDataUnchanged(source,
							destination,
							sourceEstablishedCommunications,
							sourceReceivedCommunications,
							destinationEstablishedCommunications,
							destinationReceivedCommunications,
							SOURCE_BALANCE,
							DESTINATION_BALANCE);
	}
	
	/**
	 * Test 1 - test phone from non existing operator making a SMS
	 */
	public void testInexistentSourceOperator() {
		// Arrange
		String exceptionPrefix = null;
		String operatorPrefix = NON_EXISTING_OPERATOR.substring(0, 2);
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int destinationBalance = getPhoneBalance(EXISTING_91_NUMBER);
		SendSMSDTO dto = new SendSMSDTO(MESSAGE,
										NON_EXISTING_OPERATOR,
										EXISTING_91_NUMBER);
		SendSMSService service = new SendSMSService(dto);
		
		// Act
		try {
			service.execute();
			fail("Should have thrown UnrecognisedPrefix");
		} catch (UnrecognisedPrefix up) {
			exceptionPrefix = up.getPrefix();
		}
		
		// Assert
		assertEquals("The prefix of the non existing operator in the "
				+ "exception should be " + operatorPrefix + " but it was "
				+ exceptionPrefix,
				operatorPrefix, exceptionPrefix);
		assertDestinationUnchanged(EXISTING_91_NUMBER,
								destinationReceivedCommunications,
								destinationBalance);
		assertEstablishedCallsUnchanged(EXISTING_91_NUMBER,
								destinationEstablishedCommunications);
	}

	/**
	 * Test 2 - test phone from non existing operator receiving a SMS
	 */
	public void testInexistentDestinationOperator() {
		// Arrange
		String exceptionPrefix = null;
		String operatorPrefix = NON_EXISTING_OPERATOR.substring(0, 2);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		SendSMSDTO dto = new SendSMSDTO(MESSAGE,
										EXISTING_91_NUMBER,
										NON_EXISTING_OPERATOR);
		SendSMSService sendService = new SendSMSService(dto);
		ReceiveSMSService receiveService = null;
		
		// Act
		try {
			sendService.execute();
			receiveService
				= new ReceiveSMSService(sendService.getSMSDTO());
			receiveService.execute();
			fail("Should have thrown UnrecognisedPrefix");
		} catch (UnrecognisedPrefix up) {
			exceptionPrefix = up.getPrefix();
		}
				
		// Assert
		assertEquals("The prefix of the non existing operator should be "
				+ operatorPrefix + " but it was " + exceptionPrefix,
				operatorPrefix, exceptionPrefix);
		assertReceivedCallsUnchanged(EXISTING_91_NUMBER,
									sourceReceivedCommunications);
	}

	/**
	 * Test 3 - test SMS from a non existing phone from an existing operator
	 */
	public void testInexistentSourcePhone() {
		// Arrange
		String exceptionNumber = null;
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int destinationBalance = getPhoneBalance(EXISTING_91_NUMBER);
		SendSMSDTO dto = new SendSMSDTO(MESSAGE,
										NON_EXISTING_NUMBER,
										EXISTING_91_NUMBER);
		SendSMSService service = new SendSMSService(dto);
				
		// Act
		try {
			service.execute();
			fail("Should have thrown PhoneNotExists");
		} catch (PhoneNotExists pne) {
			exceptionNumber = pne.getNumber();
		}
				
		// Assert
		assertEquals("The prefix of the non existing operator should be "
				+ NON_EXISTING_NUMBER + " but it was " + exceptionNumber,
				NON_EXISTING_NUMBER, exceptionNumber);
		assertDestinationUnchanged(EXISTING_91_NUMBER,
								destinationReceivedCommunications,
								destinationBalance);
		assertEstablishedCallsUnchanged(EXISTING_91_NUMBER,
										destinationEstablishedCommunications);
	}

	/**
	 * Test 4 - test SMS to a non existing phone in an existing operator
	 */
	public void testInexistentDestinationPhone() {
		// Arrange
		String exceptionNumber = null;
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		SendSMSDTO dto = new SendSMSDTO(MESSAGE,
										EXISTING_91_NUMBER,
										NON_EXISTING_NUMBER);
		SendSMSService sendService = new SendSMSService(dto);
		ReceiveSMSService receiveService = null;

		// Act
		try {
			sendService.execute();
			receiveService
				= new ReceiveSMSService(sendService.getSMSDTO());
			receiveService.execute();
			fail("Should have thrown PhoneNotExists");
		} catch (PhoneNotExists pne) {
			exceptionNumber = pne.getNumber();
		}
						
		// Assert
		assertEquals("The prefix of the non existing operator should be "
				+ NON_EXISTING_NUMBER + " but it was " + exceptionNumber,
				NON_EXISTING_NUMBER, exceptionNumber);
		assertReceivedCallsUnchanged(EXISTING_91_NUMBER,
									sourceReceivedCommunications);
	}
	
	/**
	 * Test 5 - test send SMS to an invalid phone number
	 */
	public void testInvalidDestinationNumber() {
		// Arrange
		int establishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int receivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int balance = getPhoneBalance(EXISTING_91_NUMBER);
		SendSMSDTO nullNumberDTO = new SendSMSDTO(
				MESSAGE,
				EXISTING_91_NUMBER,
				INVALID_NULL_NUMBER);
		SendSMSDTO smallerNumberDTO = new SendSMSDTO(
				MESSAGE,
				EXISTING_91_NUMBER,
				INVALID_SMALLER_NUMBER);
		SendSMSDTO biggerNumberDTO = new SendSMSDTO(
				MESSAGE,
				EXISTING_91_NUMBER,
				INVALID_BIGGER_NUMBER);
		SendSMSDTO letterNumberDTO = new SendSMSDTO(
				MESSAGE,
				EXISTING_91_NUMBER,
				INVALID_LETTER_NUMBER);
		SMSDTO nullNumberResponse = new SMSDTO(
				MESSAGE,
				EXISTING_91_NUMBER,
				INVALID_NULL_NUMBER,
				MOCK_COST);
		SMSDTO smallerNumberResponse = new SMSDTO(
				MESSAGE,
				EXISTING_91_NUMBER,
				INVALID_SMALLER_NUMBER,
				MOCK_COST);
		SMSDTO biggerNumberResponse = new SMSDTO(
				MESSAGE,
				EXISTING_91_NUMBER,
				INVALID_BIGGER_NUMBER,
				MOCK_COST);
		SMSDTO letterNumberResponse = new SMSDTO(
				MESSAGE,
				EXISTING_91_NUMBER,
				INVALID_LETTER_NUMBER,
				MOCK_COST);
		SendSMSService nullNumberSendService
			= new SendSMSService(nullNumberDTO);
		SendSMSService smallerNumberSendService
			= new SendSMSService(smallerNumberDTO);
		SendSMSService biggerNumberSendService
			= new SendSMSService(biggerNumberDTO);
		SendSMSService letterNumberSendService
			= new SendSMSService(letterNumberDTO);
		ReceiveSMSService nullNumberReceiveService
			= new ReceiveSMSService(nullNumberResponse);
		ReceiveSMSService smallerNumberReceiveService
			= new ReceiveSMSService(smallerNumberResponse);
		ReceiveSMSService biggerNumberReceiveService
			= new ReceiveSMSService(biggerNumberResponse);
		ReceiveSMSService letterNumberReceiveService
			= new ReceiveSMSService(letterNumberResponse);
		
		// Act
		try {
			nullNumberSendService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			nullNumberReceiveService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			smallerNumberSendService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			smallerNumberReceiveService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			biggerNumberSendService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			biggerNumberReceiveService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			letterNumberSendService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			letterNumberReceiveService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		
		// Assert
		assertSourceUnchanged(EXISTING_91_NUMBER,
							establishedCommunications,
							balance);
		assertReceivedCallsUnchanged(EXISTING_91_NUMBER,
									receivedCommunications);
	}
	
	/**
	 * Test 6 - test receive SMS from an invalid phone number
	 */
	public void testInvalidSourceNumber() {
		// Arrange
		int establishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int receivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int balance = getPhoneBalance(EXISTING_91_NUMBER);
		SendSMSDTO nullNumberDTO = new SendSMSDTO(
				MESSAGE,
				INVALID_NULL_NUMBER,
				EXISTING_91_NUMBER);
		SendSMSDTO smallerNumberDTO = new SendSMSDTO(
				MESSAGE,
				INVALID_SMALLER_NUMBER,
				EXISTING_91_NUMBER);
		SendSMSDTO biggerNumberDTO = new SendSMSDTO(
				MESSAGE,
				INVALID_BIGGER_NUMBER,
				EXISTING_91_NUMBER);
		SendSMSDTO letterNumberDTO = new SendSMSDTO(
				MESSAGE,
				INVALID_LETTER_NUMBER,
				EXISTING_91_NUMBER);
		SMSDTO nullNumberResponse = new SMSDTO(
				MESSAGE,
				INVALID_NULL_NUMBER,
				EXISTING_91_NUMBER,
				MOCK_COST);
		SMSDTO smallerNumberResponse = new SMSDTO(
				MESSAGE,
				INVALID_SMALLER_NUMBER,
				EXISTING_91_NUMBER,
				MOCK_COST);
		SMSDTO biggerNumberResponse = new SMSDTO(
				MESSAGE,
				INVALID_BIGGER_NUMBER,
				EXISTING_91_NUMBER,
				MOCK_COST);
		SMSDTO letterNumberResponse = new SMSDTO(
				MESSAGE,
				INVALID_LETTER_NUMBER,
				EXISTING_91_NUMBER,
				MOCK_COST);
		SendSMSService nullNumberSendService
			= new SendSMSService(nullNumberDTO);
		SendSMSService smallerNumberSendService
			= new SendSMSService(smallerNumberDTO);
		SendSMSService biggerNumberSendService
			= new SendSMSService(biggerNumberDTO);
		SendSMSService letterNumberSendService
			= new SendSMSService(letterNumberDTO);
		ReceiveSMSService nullNumberReceiveService
			= new ReceiveSMSService(nullNumberResponse);
		ReceiveSMSService smallerNumberReceiveService
			= new ReceiveSMSService(smallerNumberResponse);
		ReceiveSMSService biggerNumberReceiveService
			= new ReceiveSMSService(biggerNumberResponse);
		ReceiveSMSService letterNumberReceiveService
			= new ReceiveSMSService(letterNumberResponse);
		
		// Act
		try {
			nullNumberSendService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			nullNumberReceiveService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			smallerNumberSendService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			smallerNumberReceiveService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			biggerNumberSendService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			biggerNumberReceiveService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			letterNumberSendService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			letterNumberReceiveService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		
		// Assert
		assertDestinationUnchanged(EXISTING_91_NUMBER,
								establishedCommunications,
								balance);
		assertEstablishedCallsUnchanged(EXISTING_91_NUMBER,
										receivedCommunications);
	}
	
	/**
	 * Test 7 - test SMS from an existing phone to another in the same
	 * operator
	 */
	public void testSuccessfulCallSameOperator() {
		testSuccessfulSMS(EXISTING_91_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 8 - test SMS from an existing phone to another in a different
	 * operator
	 */
	public void testSuccessfulCallDifferentOperator() {
		testSuccessfulSMS(EXISTING_91_NUMBER, EXISTING_96_NUMBER);
	}
	
	/**
	 * Test 9 - test SMS from an existing 2G phone to another phone in the
	 * same operator
	 */
	public void testSourcePhone2G() {
		testSuccessfulSMS(EXISTING_912G_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 10 - test SMS from an existing phone to a 2G phone in the same
	 * operator
	 */
	public void testDestinationPhone2G() {
		testSuccessfulSMS(EXISTING_91_NUMBER, EXISTING_912G_NUMBER2);
	}
	
	/**
	 * Test 11 - test SMS from an existing phone without positive balance
	 */
	public void testNotPositiveBalance() {
		// Arrange
		String exceptionNumber = null;
		int exceptionBalance = -1;
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER2);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER2);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		SendSMSDTO dto = new SendSMSDTO(MESSAGE,
										EXISTING_91_NUMBER2,
										EXISTING_91_NUMBER);
		SendSMSService service = new SendSMSService(dto);
								
		// Act
		try {
			service.execute();
			fail("Should have thrown NotPositiveBalance");
		} catch (NotPositiveBalance npb) {
			exceptionNumber = npb.getNumber();
			exceptionBalance = npb.getBalance();
		}
							
		// Assert
		assertEquals("The source phone's number in the exception should be "
				+ EXISTING_91_NUMBER2 + " but it was " + exceptionNumber,
				EXISTING_91_NUMBER2, exceptionNumber);
		assertEquals("The source phone's balance in the exception should be "
				+ DESTINATION_BALANCE + " but it was " + exceptionBalance,
				DESTINATION_BALANCE, exceptionBalance);
		assertDataUnchanged(EXISTING_91_NUMBER2,
							EXISTING_91_NUMBER,
							sourceEstablishedCommunications,
							sourceReceivedCommunications,
							destinationEstablishedCommunications,
							destinationReceivedCommunications,
							DESTINATION_BALANCE,
							SOURCE_BALANCE);
	}
	
	/**
	 * Test 12 - test SMS from an existing phone in the Off state
	 */
	public void testSourcePhoneOff() {
		changePhoneState(EXISTING_91_NUMBER, stateRepresention.getOffState());
		testSMSFromSourceInInvalidState(EXISTING_91_NUMBER,
										EXISTING_91_NUMBER2,
										stateRepresention.getOffState());
	}
	
	/**
	 * Test 13 - test SMS from an existing phone in the Silent state
	 */
	public void testSourcePhoneSilent() {
		changePhoneState(EXISTING_91_NUMBER,
						stateRepresention.getSilentState());
		testSuccessfulSMS(EXISTING_91_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 14 - test SMS from an existing phone in the Occupied state
	 */
	public void testSourcePhoneOccupied() {
		// Arrange
		CallDTO dto = new CallDTO(
				EXISTING_91_NUMBER,
				EXISTING_91_NUMBER2,
				MOCK_START_TIME,
				communicationRepresentation.getVoiceCommunication());
		MakeCallService makeService = new MakeCallService(dto);
		ReceiveCallService receiveService = new ReceiveCallService(dto);
		makeService.execute();
		receiveService.execute();
		
		// Test
		testSMSFromSourceInInvalidState(EXISTING_91_NUMBER,
										EXISTING_96_NUMBER,
										stateRepresention.getOccupiedState());
	}
	
	/**
	 * Test 15 - test SMS to an existing phone in the Off state
	 */
	public void testDestinationPhoneOff() {
		changePhoneState(EXISTING_91_NUMBER2, stateRepresention.getOffState());
		testSuccessfulSMS(EXISTING_91_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 16 - test SMS to an existing phone in the Silent state
	 */
	public void testDestinationPhoneSilent() {
		changePhoneState(EXISTING_91_NUMBER2,
						stateRepresention.getSilentState());
		testSuccessfulSMS(EXISTING_91_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 17 - test SMS to an existing phone in the Occupied state
	 */
	public void testDestinationPhoneOccupied() {
		// Arrange
		CallDTO dto = new CallDTO(
				EXISTING_912G_NUMBER,
				EXISTING_91_NUMBER2,
				MOCK_START_TIME,
				communicationRepresentation.getVoiceCommunication());
		MakeCallService makeService = new MakeCallService(dto);
		ReceiveCallService receiveService = new ReceiveCallService(dto);
		makeService.execute();
		receiveService.execute();
				
		// Test
		testSuccessfulSMS(EXISTING_91_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 18 - test SMS with an invalid message
	 */
	public void testInvalidMessage() {
		// Arrange
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER2);
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER2);
		int sourceBalance = getPhoneBalance(EXISTING_91_NUMBER);
		int destinationBalance = getPhoneBalance(EXISTING_91_NUMBER2);
		SendSMSDTO nullMessageDTO = new SendSMSDTO(
				INVALID_NULL_MESSAGE,
				EXISTING_91_NUMBER,
				EXISTING_91_NUMBER2);
		SendSMSDTO emptyMessageDTO = new SendSMSDTO(
				INVALID_EMPTY_MESSAGE,
				EXISTING_91_NUMBER,
				EXISTING_91_NUMBER2);
		SendSMSService nullMessageService
			= new SendSMSService(nullMessageDTO);
		SendSMSService emptyMessageService
			= new SendSMSService(emptyMessageDTO);
				
		// Act
		try {
			nullMessageService.execute();
			fail("Should have thrown SMSMessageNotValid");
		} catch (SMSMessageNotValid smsmnv) { /* Nothing to do here */ }
		try {
			emptyMessageService.execute();
			fail("Should have thrown SMSMessageNotValid");
		} catch (SMSMessageNotValid smsmnv) { /* Nothing to do here */ }
				
		// Assert
		assertDataUnchanged(EXISTING_91_NUMBER,
							EXISTING_91_NUMBER2,
							sourceEstablishedCommunications,
							sourceReceivedCommunications,
							destinationEstablishedCommunications,
							destinationReceivedCommunications,
							sourceBalance,
							destinationBalance);
	}
	
}