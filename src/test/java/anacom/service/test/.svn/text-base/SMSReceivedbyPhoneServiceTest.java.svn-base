package anacom.service.test;

import java.util.ArrayList;
import java.util.List;
import anacom.services.GetPhoneSMSReceivedMessagesService;
import anacom.services.IncreasePhoneBalanceService;
import anacom.services.ReceiveSMSService;
import anacom.services.SendSMSService;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.ReceivedSMSDTO;
import anacom.shared.dto.SMSDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

public class SMSReceivedbyPhoneServiceTest extends AnacomServiceTestCase {

	/**
	 * Operator DATA 
	 */
	private final static String OPERATOR_TMN = "TMN";
	private final static String OPERATOR_96 = "96";
	private final static int SMS_TMN = 1;
	private final static int VOICE_TMN = 10;
	private final static int VIDEO_TMN = 10;
	private final static int TAX_TMN = 2;
	private final static int BONUS_TMN = 1;
	
	private final static String OPERATOR_VDF = "Vodafone";
	private final static String OPERATOR_91 = "91";
	private final static int SMS_VDF = 2;
	private final static int VOICE_VDF = 11;
	private final static int VIDEO_VDF = 12;
	private final static int TAX_VDF = 1;
	private final static int BONUS_VDF = 2;
	
	/**
	 * Phone DATA
	 */
	private final static String NUMBER_TMN = "961311111";
	private final static String NUMBER_TMN2 = "961311112";
	private final static int CASH = 100;
	private final static String NUMBER_VDF = "911311111";
	private final static String NON_EXISTING_NUMBER = "941311111";
	private final static String INVALID_NUMBER = "9112311";
	// The number of messages used here 
	private final static String [] messages = { "Good Morning", 
												"Guten Morgen", 
												"Bom Dia", 
												"Buenos Dias"};
	//Phone State Representation
	private final static PhoneStateRepresentation stateRepresention
		= PhoneStateRepresentation.getInstance();
	
	/**
	 * Test Constructor 
	 * @param name the name of the Test Case. For dynamic invocation.
	 * @param cummunicationType video or voice
	 */
	public SMSReceivedbyPhoneServiceTest(String name) {
		super(name);
	}
	
	/**
	 * Test Constructor
	 * @param cummunicationType video or voice
	 */
	public SMSReceivedbyPhoneServiceTest() {
		super();
	}
	
	/**
	 * Adding two Operators and two numbers
	 */
	@Override
	public void setUp() {
		addOperator(
				OPERATOR_TMN,
				OPERATOR_96,
				SMS_TMN,
				VOICE_TMN,
				VIDEO_TMN,
				TAX_TMN,
				BONUS_TMN);
		addOperator(
				OPERATOR_VDF,
				OPERATOR_91,
				SMS_VDF,
				VOICE_VDF,
				VIDEO_VDF,
				TAX_VDF,
				BONUS_VDF);
		
		addPhone2G(NUMBER_TMN);
		addPhone2G(NUMBER_TMN2);
		addPhone2G(NUMBER_VDF);
		changePhoneState(NUMBER_TMN, stateRepresention.getOnState());
		changePhoneState(NUMBER_VDF, stateRepresention.getOnState());
	}
	
	
	/**
	 * Send 4 messages from number1 to number2
	 * @param number1	the number who is go to send the messages
	 * @param number2	the number who is go to receive the messages
	 */
	private void testingSendingN1N2(String number1, String number2) {
		//Arrange
		IncreasePhoneBalanceDTO 
		  dto = new IncreasePhoneBalanceDTO(number1, CASH);
		
		// Number of Messages to send
		SendSMSDTO [] dtos = { new SendSMSDTO(messages[0], number1, number2),
				new SendSMSDTO(messages[1], number1, number2),
				new SendSMSDTO(messages[2], number1, number2),
				new SendSMSDTO(messages[3], number1, number2)
		};
		
		IncreasePhoneBalanceService addService = new IncreasePhoneBalanceService(dto);
		SendSMSService generic = null;
		// To get the list of sms's received by NUMBER_VDF
		GetPhoneSMSReceivedMessagesService SMSListService = new GetPhoneSMSReceivedMessagesService(new PhoneNumberDTO(number2));
		ArrayList<ReceivedSMSDTO> SMSList = null;
		ArrayList<ReceivedSMSDTO> checkSMSList = new ArrayList<ReceivedSMSDTO>();
				
		addService.execute();
		for(int ix = 0; ix < dtos.length; ix++) {
			generic = new SendSMSService(dtos[ix]);
			generic.execute();
			receiveSMSServiceExecute(new SMSDTO(dtos[ix].getMessage(),
					dtos[ix].getSource(), dtos[ix].getDestination(), 
					generic.getSMSDTO().getCost()));
			checkSMSList.add(new ReceivedSMSDTO(dtos[ix].getMessage(),dtos[ix].getSource(), generic.getSMSDTO().getCost()));
		}
		//SMS ReceivedPhoneMessagesService 
		SMSListService.execute();
		SMSList = (ArrayList<ReceivedSMSDTO>) SMSListService.getDTOSMSList().getSMSList();
		
		assertingListSMS(SMSList, checkSMSList);
	}
	
	/**
	 * Local ReceiveSMSServiceExecute
	 * @param dto
	 * @throws UnrecognisedPrefix
	 * @throws PhoneNotExists
	 */
	private void receiveSMSServiceExecute(SMSDTO dto) 
			throws UnrecognisedPrefix, PhoneNotExists {
		ReceiveSMSService service = new ReceiveSMSService(dto);
		service.execute();
	}

	/**
	 * Asserts all the values from DTOs to a local data used to construct messages
	 * @param dtolist	List of DTOs executed by a specific 
	 * service to list all the SMS received by a number
	 * @param smslist	the Check List to make the assert Tests
	 */
	private void assertingListSMS(List<ReceivedSMSDTO> dtolist, List<ReceivedSMSDTO> smslist) {
		
		assertEquals("SMS List should have the size of " + smslist.size(), 
				dtolist.size(), smslist.size());		
		for(int ix = 0; ix < smslist.size(); ix++) {
			assertEquals("SMS List should have the same Destinations like: " + smslist.get(ix).getSource(),
					dtolist.get(ix).getSource(), smslist.get(ix).getSource());
			assertEquals("SMS List should have the same Messages like: " + smslist.get(ix).getMessage(),
					dtolist.get(ix).getMessage(), smslist.get(ix).getMessage());
			assertEquals("SMS List should have the same costs like: " + smslist.get(ix).getCost(),
					dtolist.get(ix).getCost(), smslist.get(ix).getCost());
		}
	}
	
	
	/**
	 * Test 1: Send 4 SMS Messages from Number TMN to Number VDF
	 */
	public void testReceiveSMSDifferentOperators() {
			testingSendingN1N2(NUMBER_TMN, NUMBER_VDF);	
	}
	
	/**
	 * Test 2: Send 4 SMS Messages from Number TMN to Number TMN
	 */
	public void testReceiveSMSSameOperators() {
			testingSendingN1N2(NUMBER_TMN, NUMBER_TMN2);			
	}
	
	/**
	 * Test 3: Send SMS for a Non Existing Number
	 */
	public void testReceivingSMSWithInexistentNumber() {
		//Arrange
		String invalidPrefix = new String();
		
		PhoneNumberDTO dto = new PhoneNumberDTO(NON_EXISTING_NUMBER);
		GetPhoneSMSReceivedMessagesService service = new GetPhoneSMSReceivedMessagesService(dto);
		
		//Act
		try {
			service.execute();
			fail("Should have thrown an exception UnrecognisedPrefix Exception.");
		} catch(UnrecognisedPrefix up) {
			invalidPrefix = up.getPrefix();
		} 
		//assert
		assertEquals("Service Invalid Prefix should be " + NON_EXISTING_NUMBER.substring(0, 2),
				invalidPrefix, NON_EXISTING_NUMBER.substring(0, 2));
		
	}
	
	/**
	 * Test 4: Send SMS for a Invalid Phone Number
	 */
	public void testSendingInvalidPhoneNumber() {
		//Arrange
		String invalidNumber = new String();
		
		PhoneNumberDTO dto = new PhoneNumberDTO(INVALID_NUMBER);
		GetPhoneSMSReceivedMessagesService service = new GetPhoneSMSReceivedMessagesService(dto);
		
		//Act
		try {
			service.execute();
			fail("Should have thrown an exception PhoneNumberNotValid Exception.");
		} catch(PhoneNumberNotValid pnnv) {
			invalidNumber = pnnv.getNumber();
		}
		//assert
		assertEquals("Service Invalid Phone should be " + INVALID_NUMBER,
				invalidNumber, INVALID_NUMBER);
	}
}