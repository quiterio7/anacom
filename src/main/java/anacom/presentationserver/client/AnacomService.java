package anacom.presentationserver.client;

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
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.communication.InvalidCallType;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.BalanceLimitExceeded;
import anacom.shared.exceptions.phone.CantChangeState;
import anacom.shared.exceptions.phone.CantMakeVideoCall;
import anacom.shared.exceptions.phone.CantMakeVoiceCall;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.NoMadeCommunication;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidState;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface AnacomService extends RemoteService {

	/**
	 * Initialises service with the desired bridge
	 * @param serverType	string indicating the type of bridge (this can be
	 * 						"ES-only" or "ES-SD")
	 */
	public void initBridge(String serverType);
	
	/**
	 * Sends a SMS
	 * @param dto					sms text plus source, destination and cost
	 * 								information
     * @throws UnrecognisedPrefix	if the prefix is not recognised (source or
     * 								destination's operator)	
     * @throws PhoneNotExists		if the phone does not exist (source or destination)
     * @throws NotPositiveBalance	if the source phone does not have enough balance
     * @throws InvalidStateSendSMS	offstate and occupied state doen't allow phones to
     * 								send SMSs
     * @throws SMSMessageNotValid	if the given sms message isn't valid
     * @throws InvalidAmount		if there was an error calculating the SMS' cost that
     * 								resulted in 0 or a negative cost
     * @throws CommunicationError	only occurs when using remote services
	 */
	public void sendSMS(SendSMSDTO dto)
			throws UnrecognisedPrefix, PhoneNotExists, NotPositiveBalance,
				InvalidStateSendSMS, SMSMessageNotValid, InvalidAmount,
				CommunicationError;
	
	
	/**
	 * Increase the Phone Balance of a Phone with given number
	 * @param phone 				the number of the Phone and the amount you want
	 * 								increase.
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if the Phone number not exists in network
	 * @throws BalanceLimitExceeded	if the balance of the increase is higher than limit
	 * @throws InvalidAmount		if the amount is null or negative
	 * @throws CommunicationError	only occurs when using remote services
	 */
	public void increasePhoneBalance(IncreasePhoneBalanceDTO dto)
			throws UnrecognisedPrefix, PhoneNotExists, BalanceLimitExceeded,
				InvalidAmount, CommunicationError;
	
	/**
	 * Gets the Phone Balance of a Phone with given number
	 * @param number 				the number of the Phone
	 * @return PhoneDTO				contains Phone Number and Phone Balance.
	 * @throws UnrecognisedPrefix 	if the given number's prefix isn't correct 
	 * @throws PhoneNotExists		if the given Phone don't exist
	 * @throws CommunicationError	only occurs when using remote services
	 */
	public PhoneDTO getPhoneBalance(PhoneNumberDTO number)
			throws UnrecognisedPrefix, PhoneNotExists, CommunicationError;
	
	/**
	 * Change the state of the Phone with a given phone number to a given state
	 * @param newState	dto holding the desired phone number and the new state
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws InvalidState			if the state given in the dto isn't a valid state
	 * @throws CantChangeState 		if the new state in'st invalid
	 * @throws CommunicationError	only occurs when using remote services
	 */
	public void setPhoneState(PhoneStateDTO newState) 
			throws UnrecognisedPrefix, PhoneNotExists, 
					InvalidState, CantChangeState, CommunicationError;
				
	/**
	 * Get the state of the Phone with a given phone number
	 * @param number	dto holding the desired phone number 
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws CommunicationError	only occurs when using remote services
	 */
	public PhoneStateDTO getPhoneState(PhoneNumberDTO number)
			throws UnrecognisedPrefix, PhoneNotExists, CommunicationError;
			
	
	/** 
	 * Getter of a collection of SMS Communications from a specific Number.
	 * @param number	dto holding the desired phone number
	 * @return	dto holding a List of SMS Communications received by the given number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws CommunicationError	only occurs when using remote services
	 */
	public PhoneReceivedSMSListDTO getPhoneSMSReceivedMessages(PhoneNumberDTO number) 
			throws UnrecognisedPrefix, PhoneNotExists, CommunicationError;
	
	/**
	 * Get the Last communication of a specific Phone Number
	 * @param number	the number which we want to know it last communication
	 * @return the last communication performed by a specific Number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws NoMadeCommunication if the last made Communication is null
	 * @throws CommunicationError	only occurs when using remote services
	 */
	public LastMadeCommunicationDTO getLastMadeCommunication(PhoneNumberDTO number) 
			throws UnrecognisedPrefix, PhoneNotExists, NoMadeCommunication, CommunicationError;
	
	/**
	 * Makes a call
	 * @param call						the call to establish
	 * @throws UnrecognisedPrefix		if the prefix is not recognised (source)
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
	 * @throws CommunicationError				only occurs when using remote services
	 */
	public void makeCall(CallDTO call) 
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid, 
			NotPositiveBalance, InvalidStateMakeVoice, InvalidStateMakeVideo,
			CantMakeVoiceCall, CantMakeVideoCall, InvalidCallType, CommunicationError;
	
	/**
	 * Finishes a call
	 * @param finishDto							the source and end time info
	 * @throws UnrecognisedPrefix 				if the source prefix is not recognised
	 * @throws PhoneNotExists 					if the source Phone does not exist
	 * @throws DurationNotValid					if the duration of the call is not a valid one
	 * @throws InvalidStateFinishMakingCall		if the source Phone is not currently making a call
	 * @throws InvalidStateFinishReceivingCall	if the destination Phone is not currently making a call
	 * @throws CommunicationError				only occurs when using remote services
	 */
	public void finishCall(FinishCallDTO finishDto)
			throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
			InvalidStateFinishMakingCall, InvalidStateFinishReceivingCall, CommunicationError;
	
	
	/**
	 * This method is only used for testing revogate certificates - SD Mode
	 * It isn't part of Project's Logic Implementation.
	 * We use this to revogate a Operator certificate using GWT interface.
	 * In normal ways we use a console in Application Server side to revogate
	 * their own certificates.
	 * Not pretty!!!
	 *
	 * revogate Certificate from a specific Server
	 * @throws CommunicationError	only occurs when using remote services
	 */
	public void revogateCertificate(String prefix) throws CommunicationError;
}
