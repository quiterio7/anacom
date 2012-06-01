package anacom.service.test;

import pt.ist.fenixframework.pstm.Transaction;
import anacom.domain.Call;
import anacom.domain.Communication;
import anacom.domain.Video;
import anacom.domain.Voice;
import anacom.services.FinishCallOnDestinationService;
import anacom.services.FinishCallOnSourceService;
import anacom.services.MakeCallService;
import anacom.services.ReceiveCallService;
import anacom.shared.dto.CallDTO;
import anacom.shared.dto.FinishCallDTO;
import anacom.shared.dto.FinishCallOnDestinationDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;

public abstract class CallServiceTest extends CommunicationServiceTest {
	
	protected final static long START_TIME = 10;
	protected final static long END_TIME = 60;
	
	protected final static int MOCK_DURATION = 0;
	protected final static int MOCK_COST = 0;
	
	/**
	 * Test Constructor 
	 * @param name the name of the Test Case. For dynamic invocation.
	 * @param cummunicationType video or voice
	 */
	public CallServiceTest(String name, String communicationType) {
		super(name, communicationType);
	}
	
	/**
	 * Test Constructor 
	 * @param cummunicationType video or voice
	 */
	public CallServiceTest(String communicationType) {
		super(communicationType);
	}
	
	/**
	 * Asserts that the stored call data in the source and destination is
	 * correct.
	 * @param source
	 * 		the source's Phone number
	 * @param destination
	 * 		the destination's Phone number
	 */
	protected void assertCallDataIsCorrect(String source, String destination) {
		Communication lastMade = getLastMadeCommunication(source);
		Communication lastReceived = getLastReceivedCommunication(destination);
		Call made = null;
		Call received = null;
		if ((lastMade instanceof Voice
				|| lastMade instanceof Video) &&
				(lastReceived instanceof Voice
				|| lastReceived instanceof Video)) {
			made = (Call)lastMade;
			received = (Call)lastReceived;
		} else {
			fail("The last stored communications aren't of type Call "
					+ "(source's is " + lastMade.getClass() + " and "
					+ "destination's is " + lastReceived.getClass() + ")");
		}
		int expectedDuration = (int)(END_TIME - START_TIME);
		int expectedCost = calcCost(source, destination, expectedDuration);
		
		boolean commited = false;
		try {
			Transaction.begin();
			assertTrue("The established call should be of type "
					+ communicationType + " but it is of type "
					+ made.getClass(),
					((communicationType.equals(
						communicationRepresentation.getVoiceCommunication())
					  && made instanceof Voice) ||
					 (communicationType.equals(
						communicationRepresentation.getVideoCommunication()))
					  && made instanceof Video));
			assertTrue("The received call should be of type "
					+ communicationType + " but it is of type "
					+ received.getClass(),
					((communicationType.equals(
						communicationRepresentation.getVoiceCommunication())
						&& made instanceof Voice) ||
					 (communicationType.equals(
						communicationRepresentation.getVideoCommunication()))
						&& made instanceof Video));
			assertEquals("The other party in " + source + " should be "
					+ destination + " but it is " + made.getOtherParty(),
					destination, made.getOtherParty());
			assertEquals("The other party in " + destination + " should be "
					+ source + " but it is " + received.getOtherParty(),
					source, received.getOtherParty());
			assertEquals("The duration stored in " + source + " should be "
					+ expectedDuration + " but it is " + made.getDuration(),
					expectedDuration, made.getDuration());
			assertEquals("The duration stored in " + destination + " should "
					+ "be " + expectedDuration + " but it is "
					+ received.getDuration(),
					expectedDuration, received.getDuration());
			assertEquals("The cost stored in " + source + " should be "
					+ expectedCost + " but it is " + made.getCost(),
					expectedCost, made.getCost());
			Transaction.commit();
			commited = true;
		} finally { if (!commited) { Transaction.abort(); } }
	}

	/**
	 * Tests a successful call.
	 * Successful call tests are all alike apart from source and destination.
	 * @param source
	 * 		the call's source number
	 * @param destination
	 * 		the call's destination number
	 */
	protected void testSuccessfulCall(String source, String destination) {
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
		CallDTO makeDto = new CallDTO(
				source,
				destination,
				START_TIME,
				communicationType);
		FinishCallDTO finishDto = new FinishCallDTO(source, END_TIME);
		FinishCallOnDestinationDTO wantedResponse
			= new FinishCallOnDestinationDTO(
					destination,
					(int)(END_TIME - START_TIME),
					0);
		FinishCallOnDestinationDTO finishResponseDto = null;
		MakeCallService makeService = new MakeCallService(makeDto);
		ReceiveCallService receiveService = new ReceiveCallService(makeDto);
		FinishCallOnSourceService finishSourceService
			= new FinishCallOnSourceService(finishDto);
		FinishCallOnDestinationService finishDestinationService = null;
		
		// Act
		makeService.execute();
		receiveService.execute();
		finishSourceService.execute();
		finishResponseDto
			= finishSourceService.getFinishCallOnDestinationDTO();
		finishDestinationService
			= new FinishCallOnDestinationService(finishResponseDto);
		finishDestinationService.execute();
		
		// Assert
		assertEquals("The response to finish call on the source should be "
				+ "(Destination:" + wantedResponse.getDestination()
				+ ", Duration:" + wantedResponse.getDuration() + ", Cost:"
				+ wantedResponse.getCost() + ") but it was (Destination:"
				+ finishResponseDto.getDestination() + ", Duration:"
				+ finishResponseDto.getDuration() + ", Cost:"
				+ finishResponseDto.getCost() + ")",
				wantedResponse, finishResponseDto);
		assertBalanceUpdated(
				source,
				destination,
				calcCost(source, destination, (int)(END_TIME - START_TIME)));
		assertCommunicationsAdded(source,
								destination,
								sourceEstablishedCommunications,
								destinationReceivedCommunications);
		assertStateUnchanged(source,
							destination,
							sourceState,
							destinationState);
		assertCallDataIsCorrect(source, destination);
		assertReceivedCallsUnchanged(source, sourceReceivedCommunications);
		assertEstablishedCallsUnchanged(destination,
										destinationEstablishedCommunications);
	}

	/**
	 * Tests a call from a phone in an invalid state.
	 * The source must already be in the desired state.
	 * @param source		the source's number
	 * @param destination	the destination's number
	 * @param state			the String representation of the invalid state
	 */
	protected void testCallFromSourceInInvalidState(String source,
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
		CallDTO dto = new CallDTO(
				source,
				destination,
				START_TIME,
				communicationType);
		MakeCallService service = new MakeCallService(dto);
											
		// Act
		try {
			service.execute();
			if(communicationType.equals(
					communicationRepresentation.getVideoCommunication())) {
				fail("Should have thrown InvalidStateMakeVideo");
			} else {
				fail("Should have thrown InvalidStateMakeVoice");
			}
		} catch (InvalidStateMakeVoice ismv) {
			if(!communicationType.equals(
					communicationRepresentation.getVoiceCommunication())) {
				fail("Should have thrown InvalidStateMakeVideo");
			}
			exceptionNumber = ismv.getNumber();
			exceptionState = ismv.getState();
		} catch (InvalidStateMakeVideo ismv) {
			if(!communicationType.equals(
					communicationRepresentation.getVideoCommunication())) {
				fail("Should have thrown InvalidStateMakeVoice");
			}
			exceptionNumber = ismv.getNumber();
			exceptionState = ismv.getState();
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
	 * Tests a call to a phone in an invalid state.
	 * The destination must already be in the desired state.
	 * The source must  be in a valid state.
	 * @param source		the source's number
	 * @param destination	the destination's number
	 * @param state			the String representation of the invalid state
	 */
	protected void testCallToDestinationInInvalidState(String source,
													   String destination,
													   String state) {
		// Arrange
		String exceptionNumber = null;
		String exceptionState = null;
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(source);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(source);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(destination);
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(destination);
		CallDTO dto = new CallDTO(
				source,
				destination,
				START_TIME,
				communicationType);
		MakeCallService makeService = new MakeCallService(dto);
		ReceiveCallService receiveService = new ReceiveCallService(dto);
													
		// Act
		try {
			makeService.execute();
			receiveService.execute();
			if(communicationType.equals(
					communicationRepresentation.getVideoCommunication())) {
				fail("Should have thrown InvalidStateReceiveVideo");
			} else {
				fail("Should have thrown InvalidStateReceiveVoice");
			}
		} catch (InvalidStateReceiveVoice isrv) {
			if(!communicationType.equals(
					communicationRepresentation.getVoiceCommunication())) {
				fail("Should have thrown InvalidStateReceiveVideo");
			}
			exceptionNumber = isrv.getNumber();
			exceptionState = isrv.getState();
		} catch (InvalidStateReceiveVideo isrv) {
			if(!communicationType.equals(
					communicationRepresentation.getVideoCommunication())) {
				fail("Should have thrown InvalidStateReceiveVoice");
			}
			exceptionNumber = isrv.getNumber();
			exceptionState = isrv.getState();
		}
													
		// Assert
		assertEquals("The destination phone's number in the exception should "
				+ "be " + destination + " but it was " + exceptionNumber,
				destination, exceptionNumber);
		assertEquals("The destination phone's state in the exception should "
				+ "be " + state + " but it was " + exceptionState,
				state, exceptionState);
		assertDestinationStateUnchanged(destination, state);
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
	 * Test 1 - test phone from non existing operator making a call
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
		CallDTO dto = new CallDTO(
				NON_EXISTING_OPERATOR,
				EXISTING_91_NUMBER,
				START_TIME,
				communicationType);
		MakeCallService service = new MakeCallService(dto);
		
		// Act
		try {
			service.execute();
			fail("Should have thrown UnrecognisedPrefix");
		} catch (UnrecognisedPrefix up) {
			exceptionPrefix = up.getPrefix();
		}
		
		// Assert
		assertEquals("The prefix of the non existing operator in the "
				+ "exception " + "should be " + operatorPrefix + " but it was "
				+ exceptionPrefix, operatorPrefix, exceptionPrefix);
		assertDestinationUnchanged(EXISTING_91_NUMBER,
								destinationReceivedCommunications,
								destinationBalance);
		assertEstablishedCallsUnchanged(EXISTING_91_NUMBER,
								destinationEstablishedCommunications);
	}

	/**
	 * Test 2 - test phone from non existing operator receiving a call
	 */
	public void testInexistentDestinationOperator() {
		// Arrange
		String exceptionPrefix = null;
		String operatorPrefix = NON_EXISTING_OPERATOR.substring(0, 2);
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int sourceBalance = getPhoneBalance(EXISTING_91_NUMBER);
		CallDTO dto = new CallDTO(
				EXISTING_91_NUMBER,
				NON_EXISTING_OPERATOR,
				START_TIME,
				communicationType);
		MakeCallService makeService = new MakeCallService(dto);
		ReceiveCallService receiveService = new ReceiveCallService(dto);
		
		// Act
		try {
			makeService.execute();
			receiveService.execute();
			fail("Should have thrown UnrecognisedPrefix");
		} catch (UnrecognisedPrefix up) {
			exceptionPrefix = up.getPrefix();
		}
				
		// Assert
		assertEquals("The prefix of the non existing operator should be "
				+ operatorPrefix + " but it was " + exceptionPrefix,
				operatorPrefix, exceptionPrefix);
		assertSourceUnchanged(EXISTING_91_NUMBER,
							sourceEstablishedCommunications,
							sourceBalance);
		assertReceivedCallsUnchanged(EXISTING_91_NUMBER,
									sourceReceivedCommunications);
	}

	/**
	 * Test 3 - test call from a non existing phone from an existing operator
	 */
	public void testInexistentSourcePhone() {
		// Arrange
		String exceptionNumber = null;
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int destinationBalance = getPhoneBalance(EXISTING_91_NUMBER);
		CallDTO dto = new CallDTO(
				NON_EXISTING_NUMBER,
				EXISTING_91_NUMBER,
				START_TIME,
				communicationType);
		MakeCallService service = new MakeCallService(dto);
				
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
	 * Test 4 - test call to a non existing phone in an existing operator
	 */
	public void testInexistentDestinationPhone() {
		// Arrange
		String exceptionNumber = null;
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int sourceBalance = getPhoneBalance(EXISTING_91_NUMBER);
		CallDTO dto = new CallDTO(
				EXISTING_91_NUMBER,
				NON_EXISTING_NUMBER,
				START_TIME,
				communicationType);
		MakeCallService makeService = new MakeCallService(dto);
		ReceiveCallService receiveService = new ReceiveCallService(dto);
						
		// Act
		try {
			makeService.execute();
			receiveService.execute();
			fail("Should have thrown PhoneNotExists");
		} catch (PhoneNotExists pne) {
			exceptionNumber = pne.getNumber();
		}
						
		// Assert
		assertEquals("The prefix of the non existing operator should be "
				+ NON_EXISTING_NUMBER + " but it was " + exceptionNumber,
				NON_EXISTING_NUMBER, exceptionNumber);
		assertSourceUnchanged(EXISTING_91_NUMBER,
				sourceEstablishedCommunications,
				sourceBalance);
		assertReceivedCallsUnchanged(EXISTING_91_NUMBER,
				sourceReceivedCommunications);
	}
	
	/**
	 * Test 5 - test make call to an invalid phone number
	 */
	public void testInvalidDestinationNumber() {
		// Arrange
		int establishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int receivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int balance = getPhoneBalance(EXISTING_91_NUMBER);
		CallDTO nullNumberDTO = new CallDTO(
				EXISTING_91_NUMBER,
				INVALID_NULL_NUMBER,
				START_TIME,
				communicationType);
		CallDTO smallerNumberDTO = new CallDTO(
				EXISTING_91_NUMBER,
				INVALID_SMALLER_NUMBER,
				START_TIME,
				communicationType);
		CallDTO biggerNumberDTO = new CallDTO(
				EXISTING_91_NUMBER,
				INVALID_BIGGER_NUMBER,
				START_TIME,
				communicationType);
		CallDTO letterNumberDTO = new CallDTO(
				EXISTING_91_NUMBER,
				INVALID_LETTER_NUMBER,
				START_TIME,
				communicationType);
		MakeCallService nullNumberService
			= new MakeCallService(nullNumberDTO);
		MakeCallService smallerNumberService
			= new MakeCallService(smallerNumberDTO);
		MakeCallService biggerNumberService
			= new MakeCallService(biggerNumberDTO);
		MakeCallService letterNumberService
			= new MakeCallService(letterNumberDTO);
		
		// Act
		try {
			nullNumberService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			smallerNumberService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			biggerNumberService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			letterNumberService.execute();
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
	 * Test 6 - test receive call from an invalid phone number
	 */
	public void testInvalidSourceNumber() {
		// Arrange
		int establishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int receivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int balance = getPhoneBalance(EXISTING_91_NUMBER);
		CallDTO nullNumberDTO = new CallDTO(
				INVALID_NULL_NUMBER,
				EXISTING_91_NUMBER,
				START_TIME,
				communicationType);
		CallDTO smallerNumberDTO = new CallDTO(
				INVALID_SMALLER_NUMBER,
				EXISTING_91_NUMBER,
				START_TIME,
				communicationType);
		CallDTO biggerNumberDTO = new CallDTO(
				INVALID_BIGGER_NUMBER,
				EXISTING_91_NUMBER,
				START_TIME,
				communicationType);
		CallDTO letterNumberDTO = new CallDTO(
				INVALID_LETTER_NUMBER,
				EXISTING_91_NUMBER,
				START_TIME,
				communicationType);
		ReceiveCallService nullNumberService
			= new ReceiveCallService(nullNumberDTO);
		ReceiveCallService smallerNumberService
			= new ReceiveCallService(smallerNumberDTO);
		ReceiveCallService biggerNumberService
			= new ReceiveCallService(biggerNumberDTO);
		ReceiveCallService letterNumberService
			= new ReceiveCallService(letterNumberDTO);
		
		// Act
		try {
			nullNumberService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			smallerNumberService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			biggerNumberService.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch (PhoneNumberNotValid pnnv) { /* Nothing to do here */ }
		try {
			letterNumberService.execute();
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
	 * Test 7 - test call from an existing phone to another in the same
	 * operator
	 */
	public void testSuccessfulCallSameOperator() {
		testSuccessfulCall(EXISTING_91_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 8 - test call from an existing phone to another in a different
	 * operator
	 */
	public void testSuccessfulCallDifferentOperator() {
		testSuccessfulCall(EXISTING_91_NUMBER, EXISTING_96_NUMBER);
	}
	
	/**
	 * Test 9 - test call from an existing 2G phone to another phone in the
	 * same operator
	 */
	public abstract void testSourcePhone2G();
	
	/**
	 * Test 10 - test call from an existing phone to a 2G phone in the same
	 * operator
	 */
	public abstract void testDestinationPhone2G();
	
	/**
	 * Test 11 - test call from an existing phone without positive balance
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
		CallDTO dto = new CallDTO(
				EXISTING_91_NUMBER2,
				EXISTING_91_NUMBER,
				START_TIME,
				communicationType);
		MakeCallService service = new MakeCallService(dto);
								
		// Act
		try {
			service.execute();
			fail("Should have thrown NotPositiveBalance.");
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
	 * Test 12 - test call from an existing phone in the Off state
	 */
	public void testSourcePhoneOff() {
		changePhoneState(EXISTING_91_NUMBER, stateRepresention.getOffState());
		testCallFromSourceInInvalidState(EXISTING_91_NUMBER,
										EXISTING_91_NUMBER2,
										stateRepresention.getOffState());
	}
	
	/**
	 * Test 13 - test call from an existing phone in the Silent state
	 */
	public void testSourcePhoneSilent() {
		changePhoneState(EXISTING_91_NUMBER,
						stateRepresention.getSilentState());
		testSuccessfulCall(EXISTING_91_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 14 - test call from an existing phone in the Occupied state
	 */
	public void testSourcePhoneOccupied() {
		// Arrange
		CallDTO dto = new CallDTO(
				EXISTING_91_NUMBER,
				EXISTING_91_NUMBER2,
				START_TIME,
				communicationRepresentation.getVoiceCommunication());
		MakeCallService makeService = new MakeCallService(dto);
		ReceiveCallService receiveService = new ReceiveCallService(dto);
		makeService.execute();
		receiveService.execute();
		
		// Test
		testCallFromSourceInInvalidState(EXISTING_91_NUMBER,
										EXISTING_96_NUMBER,
										stateRepresention.getOccupiedState());
	}
	
	/**
	 * Test 15 - test call to an existing phone in the Off state
	 */
	public void testDestinationPhoneOff() {
		changePhoneState(EXISTING_91_NUMBER2, stateRepresention.getOffState());
		testCallToDestinationInInvalidState(EXISTING_91_NUMBER,
											EXISTING_91_NUMBER2,
											stateRepresention.getOffState());
	}
	
	/**
	 * Test 16 - test call to an existing phone in the Silent state
	 */
	public void testDestinationPhoneSilent() {
		changePhoneState(EXISTING_91_NUMBER2,
						stateRepresention.getSilentState());
		testCallToDestinationInInvalidState(EXISTING_91_NUMBER,
											EXISTING_91_NUMBER2,
											stateRepresention.getSilentState());
	}
	
	/**
	 * Test 17 - test call to an existing phone in the Occupied state
	 */
	public void testDestinationPhoneOccupied() {
		// Arrange
		CallDTO dto = new CallDTO(
				EXISTING_912G_NUMBER,
				EXISTING_91_NUMBER2,
				START_TIME,
				communicationRepresentation.getVoiceCommunication());
		MakeCallService makeService = new MakeCallService(dto);
		ReceiveCallService receiveService = new ReceiveCallService(dto);
		makeService.execute();
		receiveService.execute();
				
		// Test
		testCallToDestinationInInvalidState(EXISTING_91_NUMBER,
										EXISTING_91_NUMBER2,
										stateRepresention.getOccupiedState());
	}
	
	/**
	 * Test 18 - test finish call without invoking make call service first
	 */
	public void testFinishCallWithoutMake() {
		// Arrange
		String exceptionNumber = null;
		String state = getPhoneState(EXISTING_91_NUMBER);
		FinishCallDTO dto = new FinishCallDTO(EXISTING_91_NUMBER, END_TIME);
		FinishCallOnSourceService service = new FinishCallOnSourceService(dto);
		
		// Act
		try {
			service.execute();
			fail("Should have thrown InvalidStateFinishMakingCall");
		} catch(InvalidStateFinishMakingCall isfmc) {
			exceptionNumber = isfmc.getNumber();
		}
		
		// Assert
		assertEquals("The phone's number in the exception should be "
				+ EXISTING_91_NUMBER + " but it was " + exceptionNumber,
				EXISTING_91_NUMBER, exceptionNumber);
		assertEquals("The phone's state should be the same (" + state
				+ ") but it changed to " + getPhoneState(EXISTING_91_NUMBER),
				state, getPhoneState(EXISTING_91_NUMBER));
	}
	
	/**
	 * Test 19 - test finish call without invoking receive call service first
	 */
	public void testFinishCallWithoutReceive() {
		// Arrange
		String exceptionNumber = null;
		String state = getPhoneState(EXISTING_91_NUMBER);
		FinishCallOnDestinationDTO dto
			= new FinishCallOnDestinationDTO(
					EXISTING_91_NUMBER,
					MOCK_DURATION,
					MOCK_COST);
		FinishCallOnDestinationService service
			= new FinishCallOnDestinationService(dto);
		
		// Act
		try {
			service.execute();
			fail("Should have thrown InvalidStateFinishReceivingCall");
		} catch(InvalidStateFinishReceivingCall isfrc) {
			exceptionNumber = isfrc.getNumber();
		}
		
		// Assert
		assertEquals("The phone's number in the exception should be "
				+ EXISTING_91_NUMBER + " but it was " + exceptionNumber,
				EXISTING_91_NUMBER, exceptionNumber);
		assertEquals("The phone's state should be the same (" + state
				+ ") but it changed to " + getPhoneState(EXISTING_91_NUMBER),
				state, getPhoneState(EXISTING_91_NUMBER));
	}
	
}
