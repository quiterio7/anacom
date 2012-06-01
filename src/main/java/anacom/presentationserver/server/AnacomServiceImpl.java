package anacom.presentationserver.server;

import java.util.ArrayList;

import anacom.applicationserver.DatabaseBootstrap;
import anacom.presentationserver.client.AnacomService;
import anacom.presentationserver.server.replication.SingleServerCommunication;
import anacom.presentationserver.server.replication.WSCommunication;
import anacom.presentationserver.server.replication.ReplicatedCommunication;
import anacom.presentationserver.server.replication.protocols.ByzantineFailureTolerator;
import anacom.presentationserver.server.replication.protocols.SilentFailureTolerator;
import javax.xml.registry.JAXRException;
import anacom.services.bridge.AnacomServerBridge;
import anacom.shared.UDDI.UDDIQuery;
import anacom.shared.dto.CallDTO;
import anacom.shared.dto.FinishCallDTO;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.dto.LastMadeCommunicationDTO;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.PhoneReceivedSMSListDTO;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.UDDI.PublishOrganizationException;
import anacom.shared.exceptions.UDDI.PublishServiceBindingException;
import anacom.shared.exceptions.UDDI.PublishServiceException;
import anacom.shared.exceptions.UDDI.QueryServiceException;
import anacom.shared.exceptions.UDDI.UDDICommunicationError;
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.communication.InvalidCallType;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.BalanceLimitExceeded;
import anacom.shared.exceptions.phone.CantChangeState;
import anacom.shared.exceptions.phone.CantMakeVideoCall;
import anacom.shared.exceptions.phone.CantMakeVoiceCall;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidState;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;
import anacom.shared.stubs.client.CommunicationErrorException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
 
/**
 * The server side implementation of the RPC service.
 */

public class AnacomServiceImpl extends RemoteServiceServlet implements
    AnacomService {

	private static final long serialVersionUID = 1L;
	
	private static AnacomServerBridge bridge = null;
	private static boolean databasedInitialized = false;

	/**
	 * Initializes service with the desired bridge
	 * @param serverType	string indicating the type of bridge (this can be
	 * 						"ES-only" or "ES-SD")
	 */
	public void initBridge(String serverType) throws RuntimeException{
		if (serverType.equals("ES+SD")) {
			bridge = new DistributedAnacomServerBridge(new SingleServerCommunication());
		} else if(serverType.equals("ES+SD+Silent")) {
			bridge = 
					new DistributedAnacomServerBridge(
							new ReplicatedCommunication(
									new SilentFailureTolerator()));
		}
		else if(serverType.equals("ES+SD+Byzantine")) {
			bridge = 
					new DistributedAnacomServerBridge(
							new ReplicatedCommunication(
									new ByzantineFailureTolerator()));
		}
		else if (serverType.equals("ES-only")) {
			bridge = new LocalAnacomServerBridge();
			if (!databasedInitialized) {
				DatabaseBootstrap.init();
				DatabaseBootstrap.setup();
				databasedInitialized = true;
			}
		} else {
			throw new RuntimeException("Servlet parameter error: ES+SD nor " +
					"ES-only");
		}
	}
	
	/**
	 * Sends a SMS
	 * @param dto					sms text plus source, destination and cost
	 * 								information
     * @throws UnrecognisedPrefix	if the prefix is not recognized (source or
     * 								destination's operator)	
     * @throws PhoneNotExists		if the phone does not exist (source or
     * 								destination)
     * @throws NotPositiveBalance	if the source phone does not have enough
     * 								balance
     * @throws InvalidStateSendSMS	offstate and occupied state doen't allow
     * 								phones to send SMSs
     * @throws SMSMessageNotValid	if the given sms message isn't valid
     * @throws InvalidAmount		if there was an error calculating the SMS'
     * 								cost that resulted in 0 or a negative cost
     * @throws CommunicationError	only occurs when using remote services
	 */
	@Override
	public void sendSMS(SendSMSDTO dto)
			throws UnrecognisedPrefix, PhoneNotExists, NotPositiveBalance,
				InvalidStateSendSMS, SMSMessageNotValid, InvalidAmount,
				CommunicationError {
		try {
			bridge.sendSMS(dto);
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().SENDSMS_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().SENDSMS_SERVICE);
		}
	}
	
	/**
	 * Increase the Phone Balance of a Phone with given number
	 * @param phone 				the number of the Phone and the amount you
	 * 								want to add
	 * @throws UnrecognisedPrefix	if the prefix isn't recognized
	 * @throws PhoneNotExists		if the Phone number not exists in network
	 * @throws BalanceLimitExceeded	if the balance of the increase is higher
	 * 								than limit
	 * @throws InvalidAmount		if the amount is null or negative
	 * @throws CommunicationError	only occurs when using remote services
	 */
	@Override
	public void increasePhoneBalance(IncreasePhoneBalanceDTO dto)
			throws UnrecognisedPrefix, PhoneNotExists, BalanceLimitExceeded,
				InvalidAmount, CommunicationError, UDDICommunicationError, 
				QueryServiceException {
		try {
			bridge.increasePhoneBalance(dto);
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().INCREASEPHONEBALANCE_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().INCREASEPHONEBALANCE_SERVICE);
		}
	}
	
	/**
	 * Gets the Phone Balance of a Phone with given number
	 * @param number 				the number of the Phone
	 * @return PhoneDTO				contains Phone Number and Phone Balance.
	 * @throws UnrecognisedPrefix 	if the given number's prefix isn't correct 
	 * @throws PhoneNotExists		if the given Phone don't exist
	 * @throws CommunicationError	only occurs when using remote services
	 */
	@Override
	public PhoneDTO getPhoneBalance(PhoneNumberDTO number)
			throws UnrecognisedPrefix, PhoneNotExists, CommunicationError,
			UDDICommunicationError, QueryServiceException {
		try {
			return bridge.getPhoneBalance(number);
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().GETPHONEBALANCE_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().GETPHONEBALANCE_SERVICE);
		}
	}
	
	/**
	 * Change the state of the Phone with a given phone number to a given state
	 * @param newState	dto holding the desired phone number and the new state
	 * @throws UnrecognisedPrefix	if the prefix isn't recognized
	 * @throws PhoneNotExists		if a Phone with the given number doesn't
	 * 								exist
	 * @throws InvalidState			if the state given in the dto isn't a valid
	 * 								state
	 * @throws CantChangeState 		if the new state in'st invalid
	 * @throws CommunicationError	only occurs when using remote services
	 */
	@Override
	public void setPhoneState(PhoneStateDTO state) 
			throws UnrecognisedPrefix, PhoneNotExists, InvalidState,
			CantChangeState, CommunicationError {
		try {
			bridge.setPhoneState(state);
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().SETPHONESTATE_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().SETPHONESTATE_SERVICE);
		}
	}
	
	/**
	 * Get the state of the Phone with a given phone number
	 * @param number	dto holding the desired phone number 
	 * @throws UnrecognisedPrefix	if the prefix isn't recognized
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws CommunicationError	only occurs when using remote services
	 */
	@Override
	public PhoneStateDTO getPhoneState(PhoneNumberDTO number) 
			throws UnrecognisedPrefix, PhoneNotExists, CommunicationError{
		try {
			return bridge.getPhoneState(number);
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().GETPHONESTATE_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().GETPHONESTATE_SERVICE);
		}
	}
	
	/** 
	 * Getter of a collection of SMS Communications from a specific Number.
	 * @param number	dto holding the desired phone number
	 * @return	dto holding a List of SMS Communications received by the given number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognized
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws CommunicationError	only occurs when using remote services
	 */
	@Override
	public PhoneReceivedSMSListDTO getPhoneSMSReceivedMessages(PhoneNumberDTO number) 
			throws UnrecognisedPrefix, PhoneNotExists, CommunicationError {
		try {
			return bridge.getPhoneSMSReceivedMessages(number);
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().GETPHONESMSRECEIVEDMESSAGES_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().GETPHONESMSRECEIVEDMESSAGES_SERVICE);
		}		
	}

	/**
	 * Get the Last communication of a specific Phone Number
	 * @param number	the number which we want to know it last communication
	 * @return the last communication performed by a specific Number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognized
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws CommunicationError	only occurs when using remote services
	 */
	@Override
	public LastMadeCommunicationDTO getLastMadeCommunication(
			PhoneNumberDTO number) throws UnrecognisedPrefix, PhoneNotExists,
			CommunicationError {
		try {
			return bridge.getLastMadeCommunication(number);
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().GETLASTMADECOMMUNICATION_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().GETLASTMADECOMMUNICATION_SERVICE);
		}	
	}
  	
	/**
	 * Makes a call
	 * @param call						the call to establish
	 * @throws UnrecognisedPrefix		if the prefix is not recognized (source)
	 * @throws PhoneNotExists			if the phone does not exist (source)
	 * @throws PhoneNumberNotValid		if the given destination number isn't a valid
	 * 									Phone number
	 * @throws NotPositiveBalance		if the source phone does not have enough balance
	 * @throws InvalidStateMakeVoice	only on and silent states allow the phone to
	 * 									make voice calls
	 * @throws InvalidStateMakeVideo	only on and silent states allow the phone to
	 * 									make Video calls
	 * @throws CantMakeVoiceCall		if the source is not a phone capable of making
	 * 									voice calls
	 * @throws CantMakeVideoCall		if the source is not a phone capable of making
	 * 									Video calls
	 * @throws InvalidCallType			if the given call type isn't Voice or Video
	 */
	@Override
	public void makeCall(CallDTO call) throws UnrecognisedPrefix,
			PhoneNotExists, PhoneNumberNotValid, NotPositiveBalance,
			InvalidStateMakeVoice, InvalidStateMakeVideo, CantMakeVoiceCall,
			CantMakeVideoCall, InvalidCallType, CommunicationError {
		try {
			bridge.makeCall(call);		
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().MAKECALL_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().MAKECALL_SERVICE);
		}
	}
	
	/**
	 * Finishes a call
	 * @param finishDto						the source and end time info
	 * @throws UnrecognisedPrefix 			if the prefix is not recognized
	 * @throws PhoneNotExists 				if the Phone does not exist
	 * @throws DurationNotValid				if the duration of the call is not a valid one
	 * @throws InvalidStateFinishMakingCall	if the Phone is not currently making a call
	 */
	@Override
	public void finishCall(FinishCallDTO finishDto)
			throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
			InvalidStateFinishMakingCall, CommunicationError {
		try {
			bridge.finishCall(finishDto);
		} catch(UDDICommunicationError uce) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().FINISHCALL_SERVICE);
		} catch (QueryServiceException qse) {
			throw new CommunicationError(
					ServiceRepresentation.getInstance().FINISHCALL_SERVICE);
		}
	}

	/**
	 * This method is only used for testing revogate certificates - SD Mode
	 * It isn't part of Project's Logic Implementation.
	 * We use this to revogate a Operator certificate using GWT interface.
	 * In normal ways we use a console in Application Server side to revogate
	 * their own certificates.
	 * Not pretty!!!
	 */
	@Override
	public void revogateCertificate(String prefix) throws CommunicationError {
		try {
			
			UDDIQuery uddiQuery = new UDDIQuery("user", "password", 
					System.getProperty("uddi.xml"));
			AnacomPortTypeFactory portTypeFactory = new AnacomPortTypeFactory();
			
			// receives a collection of servers with a given prefix
			ArrayList<String> list = uddiQuery.query("Anacom", prefix);
			
			/**
			 *  We only need to get one replica and send the web service
			 *  to it
			 */
			System.out.println("Executing web Service");
			portTypeFactory.setServer(list.get(0)).revogateCertificate();
		}
		catch (JAXRException e) {
			System.out.println("UDDI ERROR: an error was reported while trying" +
					" to communicate with the uddi server.");
			throw new CommunicationError();
		} catch(PublishServiceBindingException e) {
			System.out.println("UDDI ERROR: the server couldn't publish the " +
					"service binding" + e.getBindingURL() + ".");
			throw new CommunicationError();
		} catch(PublishServiceException e) {
			System.out.println("UDDI ERROR: the server couldn't publish the " +
					"service " + e.getServiceName() + ".");
			throw new CommunicationError();
		} catch(PublishOrganizationException e) {
			System.out.println("UDDI ERROR: the server couldn't publish the " +
					"organization " + e.getOrganizationName() + ".");
			throw new CommunicationError();
		} catch(CommunicationErrorException e) {
			throw new CommunicationError();
		} catch(Exception e ) {
			System.out.println(e.getClass().getName());
			throw new CommunicationError();
		}
	}
}
