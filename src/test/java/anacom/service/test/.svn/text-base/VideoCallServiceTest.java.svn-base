package anacom.service.test;

import anacom.services.MakeCallService;
import anacom.services.ReceiveCallService;
import anacom.shared.dto.CallDTO;
import anacom.shared.exceptions.phone.CantMakeVideoCall;
import anacom.shared.exceptions.phone.CantReceiveVideoCall;

public class VideoCallServiceTest extends CallServiceTest {

	/**
	 * Test Constructor 
	 * @param name the name of the Test Case. For dynamic invocation.
	 */
	public VideoCallServiceTest(String name) {
		super(name, communicationRepresentation.getVideoCommunication());
	}
	
	/**
	 * Test Constructor 
	 */
	public VideoCallServiceTest(){
		super(communicationRepresentation.getVideoCommunication());
	}

	/**
	 * Test 9 - test call from an existing 2G phone to another phone in the
	 * same operator
	 */
	public void testSourcePhone2G() {
		// Arrange
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_912G_NUMBER);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_912G_NUMBER);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER2);
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER2);
		String exceptionNumber = null;
		CallDTO makeDto = new CallDTO(
				EXISTING_912G_NUMBER,
				EXISTING_91_NUMBER2,
				START_TIME,
				communicationRepresentation.getVideoCommunication());
		MakeCallService makeService = new MakeCallService(makeDto);
		
		// Act
		try {
			makeService.execute();
			fail("Should have thrown CantMakeVideoCall.");
		} catch(CantMakeVideoCall ex) {
			exceptionNumber = ex.getNumber();
		}

		// Assert
		assertEquals("The source phone's number in the exception should be "
				+ EXISTING_912G_NUMBER + " but it was " + exceptionNumber,
				EXISTING_912G_NUMBER, exceptionNumber);
		assertDataUnchanged(EXISTING_912G_NUMBER,
							EXISTING_91_NUMBER2,
							sourceEstablishedCommunications,
							sourceReceivedCommunications,
							destinationEstablishedCommunications,
							destinationReceivedCommunications,
							SOURCE_BALANCE,
							DESTINATION_BALANCE);
	}
	
	/**
	 * Test 10 - test call from an existing phone to a 2G phone in the same
	 * operator
	 */
	public void testDestinationPhone2G() {
		// Arrange
		int sourceEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_91_NUMBER);
		int sourceReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_91_NUMBER);
		int destinationEstablishedCommunications
			= countEstablishedCallsInPhone(EXISTING_912G_NUMBER2);
		int destinationReceivedCommunications
			= countReceivedCallsInPhone(EXISTING_912G_NUMBER2);
		String exceptionNumber = null;
		CallDTO makeDto = new CallDTO(
				EXISTING_91_NUMBER,
				EXISTING_912G_NUMBER2,
				START_TIME,
				communicationRepresentation.getVideoCommunication());
		MakeCallService makeService = new MakeCallService(makeDto);
		ReceiveCallService receiveService = new ReceiveCallService(makeDto);
		
		// Act
		try {
			makeService.execute();
			receiveService.execute();
			fail("Should have thrown CantReceiveVideoCall.");
		} catch(CantReceiveVideoCall ex) {
			exceptionNumber = ex.getNumber();
		}

		// Assert
		assertEquals("The destination phone's number in the exception "
				+ "should be " + EXISTING_912G_NUMBER2 + " but it was "
				+ exceptionNumber,
				EXISTING_912G_NUMBER2, exceptionNumber);
		assertDataUnchanged(EXISTING_91_NUMBER,
							EXISTING_912G_NUMBER2,
							sourceEstablishedCommunications,
							sourceReceivedCommunications,
							destinationEstablishedCommunications,
							destinationReceivedCommunications,
							SOURCE_BALANCE,
							DESTINATION_BALANCE);
	}

}