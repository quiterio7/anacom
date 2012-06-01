package anacom.service.test;

public class VoiceCallServiceTest extends CallServiceTest {

	/**
	 * Test Constructor 
	 * @param name the name of the Test Case. For dynamic invocation.
	 */
	public VoiceCallServiceTest(String name) {
		super(name, communicationRepresentation.getVoiceCommunication());
	}
	
	/**
	 * Test Constructor 
	 */
	public VoiceCallServiceTest(){
		super(communicationRepresentation.getVoiceCommunication());
	}

	/**
	 * Test 9 - test call from an existing 2G phone to another phone in the 
	 * same operator
	 */
	public void testSourcePhone2G() {
		super.testSuccessfulCall(EXISTING_912G_NUMBER, EXISTING_91_NUMBER2);
	}
	
	/**
	 * Test 10 - test call from an existing phone to a 2G phone in the same 
	 * operator
	 */
	public void testDestinationPhone2G() {
		super.testSuccessfulCall(EXISTING_91_NUMBER, EXISTING_912G_NUMBER2);
	}

}
