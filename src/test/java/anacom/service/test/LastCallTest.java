package anacom.service.test;

import anacom.services.FinishCallOnDestinationService;
import anacom.services.FinishCallOnSourceService;
import anacom.services.GetLastMadeCommunicationService;
import anacom.services.MakeCallService;
import anacom.services.ReceiveCallService;
import anacom.services.ReceiveSMSService;
import anacom.services.SendSMSService;
import anacom.shared.dto.CallDTO;
import anacom.shared.dto.FinishCallDTO;
import anacom.shared.dto.FinishCallOnDestinationDTO;
import anacom.shared.dto.LastMadeCommunicationDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.SMSDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.NoMadeCommunication;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

public class LastCallTest extends AnacomServiceTestCase {

	/**
	 * Operator DATA 
	 */
	private final static String OPERATOR_NAME = "Vodafone";
	private final static String OPERATOR_PREFIX = "91";
	private final static int SMS_COST = 10;
	private final static int VOICE_COST = 11;
	private final static int VIDEO_COST = 12;
	private final static int TAX = 50;
	private final static int BONUS = 20;
	
	/**
	 * Phone DATA
	 */
	private final static String EXISTING_NUMBER = "911311111";
	private final static String EXISTING_NUMBER2 = "911322222";
	private final static String NON_EXISTING_OPERATOR = "961311111";
	private final static String NON_EXISTING_NUMBER = "911999999";
	private final static String INVALID_NUMBER = "91000000";
	private final static int CASH = 1000;
	private final static long START_TIME = 10;
	private final static long END_TIME = 50;
	private final static String MESSAGE = "Hello World";
	private final static int EXPECTED_SMS_COST =
			SMS_COST * ((MESSAGE.length() - 1)/100 + 1);
	private final static int EXPECTED_VOICE_COST =
			VOICE_COST * (int)(END_TIME - START_TIME);
	private final static int EXPECTED_VIDEO_COST =
			VIDEO_COST * (int)(END_TIME - START_TIME);

	private final static PhoneStateRepresentation stateRepresention
		= PhoneStateRepresentation.getInstance();
	private final static CommunicationRepresentation communicationRepresentation
		= CommunicationRepresentation.getInstance();

	/**
	 * Test Constructor 
	 * @param name the name of the Test Case. For dynamic invocation.
	 */
	public LastCallTest(String name) {
		super(name);
	}
	
	/**
	 * Test Constructor
	 */
	public LastCallTest() {
		super();
	}
	
	/**
	 * Adding two Operators and two numbers
	 */
	@Override
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
		addPhone3G(EXISTING_NUMBER);
		addPhone3G(EXISTING_NUMBER2);
		increasePhoneBalance(EXISTING_NUMBER, CASH);
		changePhoneState(EXISTING_NUMBER, stateRepresention.getOnState());
		changePhoneState(EXISTING_NUMBER2, stateRepresention.getOnState());
	}
	
	/**
	 * Asserts that the data in a last made communication response is correct
	 * @param dto			the response DTO
	 * @param communication	the desired communication type
	 * @param destination	the desired destination
	 * @param cost			the desired cost
	 * @param size			the desired size
	 */
	private void assertLastCommunication(	LastMadeCommunicationDTO dto,
											String communication,
											String destination,
											int cost,
											int size) {
		assertEquals("The last made communication should be a " + communication
				+ " but it was " + dto.getCommunicationType(),
				communication, dto.getCommunicationType());
		assertEquals("The destination of the last made communication should "
				+ "be " + destination + " but it was " + dto.getDestination(),
				destination, dto.getDestination());
		assertEquals("The cost of the last made communication should be "
				+ cost + " but it was " + dto.getCost(),
				cost, dto.getCost());
		assertEquals("The size of the last made communication should be "
				+ size + " but it was " + dto.getSize(),
				size, dto.getSize());
	}
	
	/**
	 * Tests a successful last made call with the given call type.
	 * @param call
	 * 		the desired call type
	 * @param expectedCost
	 * 		the call's expected cost
	 */
	private void testSuccessfulLastMadeCall(String call, int expectedCost) {
		// Arrange
		PhoneNumberDTO sourceDTO = new PhoneNumberDTO(EXISTING_NUMBER);
		PhoneNumberDTO destinationDTO = new PhoneNumberDTO(EXISTING_NUMBER2);
		LastMadeCommunicationDTO lastCommunication = null;
		GetLastMadeCommunicationService sourceService
			= new GetLastMadeCommunicationService(sourceDTO);
		GetLastMadeCommunicationService destinationService
			= new GetLastMadeCommunicationService(destinationDTO);
		CallDTO makeCallDTO = new CallDTO(
				EXISTING_NUMBER,
				EXISTING_NUMBER2,
				START_TIME,
				call);
		FinishCallDTO finishDTO = new FinishCallDTO(EXISTING_NUMBER, END_TIME);
		FinishCallOnDestinationDTO finishDestinationDTO = null;
		MakeCallService makeCallService
			= new MakeCallService(makeCallDTO);
		ReceiveCallService receiveCallService
			= new ReceiveCallService(makeCallDTO);
		FinishCallOnSourceService finishCallService
			= new FinishCallOnSourceService(finishDTO);
		FinishCallOnDestinationService finishCallOnDestination = null;
		
		// Act
		makeCallService.execute();
		receiveCallService.execute();
		try {
			sourceService.execute();
			fail("Source phone shouldn't have a last made communication "
					+ "before it ended");
		} catch(NoMadeCommunication nmc) { /* Nothing to do here */ }
		finishCallService.execute();
		finishDestinationDTO
			= finishCallService.getFinishCallOnDestinationDTO();
		finishCallOnDestination
			= new FinishCallOnDestinationService(finishDestinationDTO);
		finishCallOnDestination.execute();
		sourceService.execute();
		lastCommunication = sourceService.getLastCommunication();
		try {
			destinationService.execute();
			fail("There shouldn't be a last made communication in the "
					+ "destination Phone");
		} catch(NoMadeCommunication nmc) { /* Nothing to do here */ }
		
		// Assert
		assertLastCommunication(lastCommunication,
								call,
								EXISTING_NUMBER2,
								expectedCost,
								finishDestinationDTO.getDuration());
	}
	
	/**
	 * Test 1 - test requiring the last made call of a Phone who hasn't made a
	 * call yet
	 */
	public void testNoMadeCommunication() {
		// Arrange
		String exceptionNumber = null;
		PhoneNumberDTO dto = new PhoneNumberDTO(EXISTING_NUMBER);
		GetLastMadeCommunicationService service
			= new GetLastMadeCommunicationService(dto);
		
		// Act
		try {
			service.execute();
			fail("Should have thrown NoMadeCommunication");
		} catch(NoMadeCommunication nmc) {
			exceptionNumber = nmc.getNumber();
		}
		
		// Assert
		assertEquals("The phone number in the exception should be "
				+ EXISTING_NUMBER + " but it was " + exceptionNumber,
				EXISTING_NUMBER, exceptionNumber);
	}
	
	/**
	 * Test 2 - test successful retrieval of last made communications after a
	 * SMS
	 */
	public void testSuccessfulLastCommunicationSMS() {
		// Arrange
		PhoneNumberDTO sourceDTO = new PhoneNumberDTO(EXISTING_NUMBER);
		PhoneNumberDTO destinationDTO = new PhoneNumberDTO(EXISTING_NUMBER2);
		LastMadeCommunicationDTO lastCommunication = null;
		GetLastMadeCommunicationService sourceService
			= new GetLastMadeCommunicationService(sourceDTO);
		GetLastMadeCommunicationService destinationService
			= new GetLastMadeCommunicationService(destinationDTO);
		SendSMSDTO sendSMSDTO = new SendSMSDTO(
				MESSAGE,
				EXISTING_NUMBER,
				EXISTING_NUMBER2);
		SMSDTO responseDTO = null;
		SendSMSService sendService = new SendSMSService(sendSMSDTO);
		ReceiveSMSService receiveService = null;
		
		// Act
		sendService.execute();
		responseDTO = sendService.getSMSDTO();
		receiveService = new ReceiveSMSService(responseDTO);
		receiveService.execute();
		sourceService.execute();
		lastCommunication = sourceService.getLastCommunication();
		try {
			destinationService.execute();
			fail("There shouldn't be a last made communication in the "
					+ "destination Phone");
		} catch(NoMadeCommunication nmc) { /* Nothing to do here */ }
		
		// Assert
		assertLastCommunication(
				lastCommunication,
				communicationRepresentation.getSMSCommunication(),
				EXISTING_NUMBER2,
				EXPECTED_SMS_COST,
				MESSAGE.length());
	}
	
	/**
	 * Test 3 - test successful retrieval of last made communications after a
	 * Voice call
	 */
	public void testSuccessfulLastCommunicationVoice() {
		testSuccessfulLastMadeCall(
				communicationRepresentation.getVoiceCommunication(),
				EXPECTED_VOICE_COST);
	}
	
	/**
	 * Test 4 - test successful retrieval of last made communications after a
	 * Video call
	 */
	public void testSuccessfulLastCommunicationVideo() {
		testSuccessfulLastMadeCall(
				communicationRepresentation.getVideoCommunication(),
				EXPECTED_VIDEO_COST);
	}

	/**
	 * Test 5 - test retrieval of a last made communication of a Phone with the
	 * prefix of a non existing operator
	 */
	public void testInexistentOperator() {
		// Arrange
		String exceptionPrefix = null;
		PhoneNumberDTO dto = new PhoneNumberDTO(NON_EXISTING_OPERATOR);
		GetLastMadeCommunicationService service
			= new GetLastMadeCommunicationService(dto);
		
		// Act
		try {
			service.execute();
			fail("Should have thrown UnrecognisedPrefix");
		} catch(UnrecognisedPrefix up) {
			exceptionPrefix = up.getPrefix();
		}
		
		// Assert
		assertEquals("The prefix in the exception should be "
				+ NON_EXISTING_OPERATOR.substring(0, 2) + " but it was "
				+ exceptionPrefix,
				NON_EXISTING_OPERATOR.substring(0, 2), exceptionPrefix);
	}
	
	/**
	 * Test 6 - test retrieval of a last made communication of a Phone that
	 * doesn't exist with the prefix of an existing Operator
	 */
	public void testInexistentNumber() {
		// Arrange
		String exceptionNumber = null;
		PhoneNumberDTO dto = new PhoneNumberDTO(NON_EXISTING_NUMBER);
		GetLastMadeCommunicationService service
			= new GetLastMadeCommunicationService(dto);
		
		// Act
		try {
			service.execute();
			fail("Should have thrown PhoneNotExists");
		} catch(PhoneNotExists pne) {
			exceptionNumber = pne.getNumber();
		}
		
		// Assert
		assertEquals("The number in the exception should be "
				+ NON_EXISTING_NUMBER + " but it was " + exceptionNumber,
				NON_EXISTING_NUMBER, exceptionNumber);
	}
	
	/**
	 * Test 7 - test retrieval of a last made communication of a Phone that
	 * have a invalid number.
	 */
	public void testInvalidNumber() {
		// Arrange
		String exceptionNumb = null;
		PhoneNumberDTO dto = new PhoneNumberDTO(INVALID_NUMBER);
		GetLastMadeCommunicationService service
			= new GetLastMadeCommunicationService(dto);
		
		// Act
		try {
			service.execute();
			fail("Should have thrown PhoneNumberNotValid");
		} catch(PhoneNumberNotValid pnnv) {
			exceptionNumb = pnnv.getNumber();
		}
		
		// Assert
		assertEquals("The number in the exception should be "
				+ INVALID_NUMBER + " but it was " + exceptionNumb,
				INVALID_NUMBER, exceptionNumb);
	}

}