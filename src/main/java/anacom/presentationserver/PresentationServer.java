package anacom.presentationserver;

import anacom.applicationserver.DatabaseBootstrap;
import anacom.presentationserver.server.DistributedAnacomServerBridge;
import anacom.presentationserver.server.LocalAnacomServerBridge;
import anacom.presentationserver.server.replication.ReplicatedCommunication;
import anacom.presentationserver.server.replication.SingleServerCommunication;
import anacom.presentationserver.server.replication.protocols.ByzantineFailureTolerator;
import anacom.presentationserver.server.replication.protocols.SilentFailureTolerator;
import anacom.services.bridge.AnacomServerBridge;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.dto.ListPhonesDTO;
import anacom.shared.dto.OperatorPrefixDTO;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.PhoneReceivedSMSListDTO;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.dto.ReceivedSMSDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.UDDI.QueryServiceException;
import anacom.shared.exceptions.UDDI.UDDICommunicationError;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.BalanceLimitExceeded;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidState;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;


/**
 * This layer concerns user interaction and the form 
 * how information is presented to the user. In the future
 * it will be able to use GWT. All the public methods used in 
 * client side are explicit here.
 *
 */
public class PresentationServer {

	private static AnacomServerBridge serviceBridge = null;
	private static boolean initialized = false;
	
	/**
	 * Init 	initialise an application with remote or local behaviour. 
	 * @param server	the kind of server to initialise the application
	 * @throws UDDICommunicationError	occurs when communication with UDDI
	 */
	public void init(String server) throws UDDICommunicationError {
	    if (server.equals("ES+SD")) {
	    	serviceBridge = new DistributedAnacomServerBridge(new SingleServerCommunication());
	    }
	    else if(server.equals("ES+SD+Silent")) {
	    	serviceBridge = 
	    			new DistributedAnacomServerBridge(
	    					new ReplicatedCommunication(
	    							new SilentFailureTolerator()));
	    }
	    else if(server.equals("ES+SD+Byzantine")) {
	    	serviceBridge = 
	    			new DistributedAnacomServerBridge(
	    					new ReplicatedCommunication(
	    							new ByzantineFailureTolerator()));
	    }
	    else if (server.equals("ES-only")) {
	    	serviceBridge = new LocalAnacomServerBridge();
	    	if (!initialized) {
				DatabaseBootstrap.init();
				DatabaseBootstrap.setup();
				initialized = true;
			}
	    } 
	    else {
	    	throw new RuntimeException("Servlet parameter error: ES+SD nor ES-only");
	    }
	}
	
	static public AnacomServerBridge getBridge() {
		return serviceBridge;
	}

	
	/**
	 * Main Function
	 * @param args	never used
	 */
    public static void main(final String[] args) {
    	PresentationServer ps = new PresentationServer();
    	String server = System.getProperty("server.type");
    	try {
    		ps.init(server);
    		getPhoneBalance("961000000");
    		increasePhoneBalance("961000000", 100);
    		increasePhoneBalance("961009000", 100);
    	} catch(UDDICommunicationError e) {
    		System.out.println("Error while loggin in UDDI.");
    	}
    	 
    }
    
    /**
     * Sends a sms
     * @param source number
     * @param destination number
     * @param message 
     */
    @SuppressWarnings("unused")
	private static void sendSMS(String source, String destination, String message) {
      	try {
        	SendSMSDTO dto = new SendSMSDTO(message, source, destination);
    		serviceBridge.sendSMS(dto);
    	} catch(UnrecognisedPrefix e) {
    		System.out.println("The prefix " + e.getPrefix() + " does not exist.");
    	} catch(CommunicationError e) {
    		System.out.println("A communication error occured while executing service: " + e.getService());
    	} catch(PhoneNotExists e) {
    		System.out.println("This phone number does not exist: " + e.getNumber());
    	} catch(NotPositiveBalance e) {
    		System.out.println("The source phone ("+e.getNumber()+") does not have a positive balance ("+e.getBalance()+").");
    	} catch(InvalidStateSendSMS e) {
    		System.out.println("The destination phone is not in a valid state: " + e.getState());
    	} catch(InvalidAmount e) {
    		System.out.println("An error occured while calculating the sms cost ("+e.getAmount()+")");
    	} catch(PhoneNumberNotValid e) {
    		System.out.println("An error occured because the Phone Number introduced isn't a valid Number");
    	} catch(UDDICommunicationError e) {
    		System.out.println("There was a communication error while trying to contact the UDDI server...");
    	} catch(QueryServiceException e) {
    		System.out.println("The prefix "+e.getServiceName()+" is not recognized...");
    	}
    }
    
    /**
     * Sends a sms
     * @param source number
     * @param destination number
     * @param message 
     */
    private static void increasePhoneBalance(String number, int amount) {
      	try {
        	IncreasePhoneBalanceDTO dto = new IncreasePhoneBalanceDTO(number, amount);
    		serviceBridge.increasePhoneBalance(dto);
    	} catch(UnrecognisedPrefix e) {
    		System.out.println("The prefix " + e.getPrefix() + " does not exist.");
    	} catch(CommunicationError e) {
    		System.out.println("A communication error occured while executing service: " + e.getService());
    	} catch(PhoneNotExists e) {
    		System.out.println("This phone number does not exist: " + e.getNumber());
    	} catch(InvalidStateSendSMS e) {
    		System.out.println("The destination phone is not in a valid state: " + e.getState());
    	} catch(BalanceLimitExceeded e) {
    		System.out.println("The balance limit has been exceeded.");
    	} catch(InvalidAmount e) {
    		System.out.println("The amount is not valid ("+e.getAmount()+").");
    	} catch(PhoneNumberNotValid e) {
    		System.out.println("An error occured because the Phone Number introduced isn't a valid Number");
    	} catch(UDDICommunicationError e) {
    		System.out.println("There was a communication error while trying to contact the UDDI server...");
    	} catch(QueryServiceException e) {
    		System.out.println("The prefix "+e.getServiceName()+" is not recognized...");
    	}
    }
      
    /**
     * Lists all the Phones registered in the Operator with the given prefix
     * @param prefix	the prefix of the Operator whose Phones are to be listed
     */
    @SuppressWarnings("unused")
	private static void listOperatorPhones(String prefix) {
    	
    	OperatorPrefixDTO operatorPrefix = new OperatorPrefixDTO(prefix);
    	
    	try {
    		
    		ListPhonesDTO listOperatorPhones = serviceBridge.listOperatorPhones(operatorPrefix);
    		
    		for(PhoneDTO phone : listOperatorPhones.getPhoneList()) {
    			System.out.println("Number: " + phone.getNumber() + "     " + "Balance: " + phone.getBalance());
    		}	
    	}
    	catch(UnrecognisedPrefix e){
    		System.out.println("The prefix " + e.getPrefix() + " isn't recognized.");
    	}
    	catch(CommunicationError ce) {
    		System.out.println("A communication error occured while executing service: " + ce.getService());
    	} 
    	catch(UDDICommunicationError e) {
    		System.out.println("There was a communication error while trying to contact the UDDI server...");
    	} 
    	catch(QueryServiceException e) {
    		System.out.println("The prefix "+e.getServiceName()+" is not recognized...");
    	}
    }
    
    /**
     * Set the Phone State 
     * @param number  the phone Number 
     * @param state  the phone State 
     */
    @SuppressWarnings("unused")
    private static void setPhoneState(String number, String state) {
    	
    	PhoneStateDTO stateDto = new PhoneStateDTO(number, state);
    	
    	try {
    		serviceBridge.setPhoneState(stateDto);
    	} catch(UnrecognisedPrefix e){
    		System.err.println("The prefix " + e.getPrefix() + " isn't recognized.");
    	} catch(PhoneNotExists e) {
    		System.err.println("The Phone with number " + e.getNumber() + "isn't recognize by Network");
    	} catch (InvalidState e) {
    		System.err.println("The State " + e.getState() + " isn't recognized by Phone");
    	} catch(PhoneNumberNotValid e) {
    		System.out.println("An error occured because the Phone Number introduced isn't a valid Number");
    	} catch(CommunicationError ce) {
    		System.err.println("A communication error occured while executing service: " + ce.getService());
    	} catch(UDDICommunicationError e) {
    		System.out.println("There was a communication error while trying to contact the UDDI server...");
    	} catch(QueryServiceException e) {
    		System.out.println("The prefix "+e.getServiceName()+" is not recognized...");
    	}
    }
    
    /**
     * Print the Phone State to system out
     * @param number  the phone Number 
     */
    @SuppressWarnings("unused")
    private static void getPhoneState(String number) {
    	
    	PhoneNumberDTO numberDto = new PhoneNumberDTO(number);
    	
    	try {
    		PhoneStateDTO phoneState = serviceBridge.getPhoneState(numberDto);
    		System.out.println("Number: " + phoneState.getNumber() + " | State: " + phoneState.getState());
    	} catch(UnrecognisedPrefix e){
    		System.err.println("The prefix " + e.getPrefix() + " isn't recognized.");
    	} catch(PhoneNotExists e) {
    		System.err.println("The Phone with number " + e.getNumber() + "isn't recognize by Network");
    	} catch(PhoneNumberNotValid e) {
    		System.out.println("An error occured because the Phone Number introduced isn't a valid Number");
    	} catch(CommunicationError ce) {
    		System.err.println("A communication error occured while executing service: " + ce.getService());
    	} catch(UDDICommunicationError e) {
    		System.out.println("There was a communication error while trying to contact the UDDI server...");
    	} catch(QueryServiceException e) {
    		System.out.println("The prefix "+e.getServiceName()+" is not recognized...");
    	}
    }
    
    /**
     * Print the Phone balance to system out
     * @param number  the phone Number 
     */
    private static void getPhoneBalance(String number) {
    	
    	PhoneNumberDTO numberDto = new PhoneNumberDTO(number);
    	
    	try {
    		PhoneDTO phone = serviceBridge.getPhoneBalance(numberDto);
    		System.out.println("Number: " + phone.getNumber() + " | Balance: " + phone.getBalance());
    	} catch(UnrecognisedPrefix e){
    		System.err.println("The prefix " + e.getPrefix() + " isn't recognized.");
    	} catch(PhoneNotExists e) {
    		System.err.println("The Phone with number " + e.getNumber() + "isn't recognize by Network");
    	} catch(PhoneNumberNotValid e) {
    		System.out.println("An error occured because the Phone Number introduced isn't a valid Number");
    	} catch(CommunicationError ce) {
    		System.err.println("A communication error occured while executing service: " + ce.getService());
    	} catch(UDDICommunicationError e) {
    		System.out.println("There was a communication error while trying to contact the UDDI server...");
    	} catch(QueryServiceException e) {
    		System.out.println("The prefix "+e.getServiceName()+" is not recognized...");
    	}
    }
 
    /**
     * Cancels the phone registration
     * @param number  the phone Number 
     */
    @SuppressWarnings("unused")
    private static void cancelPhoneRegistry(String number) {
    	
    	PhoneNumberDTO numberDto = new PhoneNumberDTO(number);
    	
    	try {
    		serviceBridge.cancelPhoneRegistry(numberDto);
    	} catch(UnrecognisedPrefix e){
    		System.err.println("The prefix " + e.getPrefix() + " isn't recognized.");
    	} catch(PhoneNotExists e) {
    		System.err.println("The Phone with number " + e.getNumber() + "isn't recognize by Network");
    	} catch(PhoneNumberNotValid e) {
    		System.out.println("An error occured because the Phone Number introduced isn't a valid Number");
    	} catch(CommunicationError ce) {
    		System.err.println("A communication error occured while executing service: " + ce.getService());
    	} catch(UDDICommunicationError e) {
    		System.out.println("There was a communication error while trying to contact the UDDI server...");
    	} catch(QueryServiceException e) {
    		System.out.println("The prefix "+e.getServiceName()+" is not recognized...");
    	}
    }
    
    /**
     * Getter of all SMS received by a specific phone
     * @param number
     */
    @SuppressWarnings("unused")
    private static void getPhoneReceivedSMS(String number) {
    	PhoneNumberDTO numberDTO = new PhoneNumberDTO(number);
    	try {
    		PhoneReceivedSMSListDTO list = serviceBridge.getPhoneSMSReceivedMessages(numberDTO);
    		
    		if(list.getSMSList() == null ) {
    			System.out.println("This number " + list.getNumber() + "does not receive any SMS Messages");
    			return;
    		}
    		for (ReceivedSMSDTO element : list.getSMSList()) {
				System.out.print("Destination: " + element.getSource());
				System.out.print("| Message: " + element.getMessage());
				System.out.println("| Cost: " + element.getCost());
			}
    		
    	} catch(UnrecognisedPrefix e){
    		System.err.println("The prefix " + e.getPrefix() + " isn't recognized.");
    	} catch(PhoneNotExists e) {
    		System.err.println("The Phone with number " + e.getNumber() + "isn't recognize by Network");
    	} catch(PhoneNumberNotValid e) {
    		System.out.println("An error occured because the Phone Number introduced isn't a valid Number");
    	} catch(CommunicationError ce) {
    		System.err.println("A communication error occured while executing service: " + ce.getService());
    	} catch(UDDICommunicationError e) {
    		System.out.println("There was a communication error while trying to contact the UDDI server...");
    	} catch(QueryServiceException e) {
    		System.out.println("The prefix "+e.getServiceName()+" is not recognized...");
    	}
    }
}