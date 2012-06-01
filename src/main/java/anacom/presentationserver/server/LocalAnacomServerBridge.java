package anacom.presentationserver.server;


import pt.ist.fenixframework.pstm.Transaction;
import anacom.services.CancelPhoneRegistryService;
import anacom.services.CreateNewOperatorService;
import anacom.services.FinishCallOnDestinationService;
import anacom.services.FinishCallOnSourceService;
import anacom.services.GetLastMadeCommunicationService;
import anacom.services.GetPhoneBalanceService;
import anacom.services.GetPhoneSMSReceivedMessagesService;
import anacom.services.GetPhoneStateService;
import anacom.services.IncreasePhoneBalanceService;
import anacom.services.ListOperatorPhonesService;
import anacom.services.MakeCallService;
import anacom.services.ReceiveCallService;
import anacom.services.ReceiveSMSService;
import anacom.services.RegisterPhoneService;
import anacom.services.SendSMSService;
import anacom.services.SetPhoneStateService;
import anacom.services.bridge.AnacomServerBridge;
import anacom.shared.dto.CallDTO;
import anacom.shared.dto.FinishCallDTO;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.dto.LastMadeCommunicationDTO;
import anacom.shared.dto.ListPhonesDTO;
import anacom.shared.dto.OperatorDetailedDTO;
import anacom.shared.dto.OperatorPrefixDTO;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.PhoneReceivedSMSListDTO;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.dto.RegisterPhoneDTO;
import anacom.shared.dto.SMSDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.IncompatiblePrefix;
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.communication.InvalidCallType;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.operator.OperatorNameAlreadyExists;
import anacom.shared.exceptions.operator.OperatorNameNotValid;
import anacom.shared.exceptions.operator.OperatorPrefixAlreadyExists;
import anacom.shared.exceptions.operator.OperatorPrefixNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.BalanceLimitExceeded;
import anacom.shared.exceptions.phone.CantChangeState;
import anacom.shared.exceptions.phone.CantMakeVideoCall;
import anacom.shared.exceptions.phone.CantMakeVoiceCall;
import anacom.shared.exceptions.phone.CantReceiveVideoCall;
import anacom.shared.exceptions.phone.CantReceiveVoiceCall;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.InvalidPhoneType;
import anacom.shared.exceptions.phone.NoMadeCommunication;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneAlreadyExists;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidState;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;


/**
 * 
 * All the methods to execute a local application are here
 *
 */

public class LocalAnacomServerBridge implements AnacomServerBridge {
	
	/**
	 * Registers a new Operator in the Network
	 * @param dto 			the data of the Operator to be registered
	 * @throws OperatorPrefixAlreadyExists 	if an Operator with the given prefix
	 * 										already exists
	 * @throws OperatorNameAlreadyExists   	if an Operator with the given name
	 * 										already exists
	 * @throws OperatorPrefixNotValid		if given prefix isn't valid
	 * @throws OperatorNameNotValid			if given name isn't valid
	 */
	@Override
	public void registerOperator(OperatorDetailedDTO dto) 
			throws OperatorNameAlreadyExists, OperatorPrefixAlreadyExists,
				OperatorPrefixNotValid, OperatorNameNotValid {
		CreateNewOperatorService service = new CreateNewOperatorService(dto);
		service.execute();
	}

	/**
	 * Registers a new 2G Phone on the Network
	 * @param dto		stores the number of the Phone and the prefix
	 * 					of the operator on which it should be registered
	 * @throws InvalidPhoneType		if the given Phone type is invalid	
	 * @throws UnrecognisedPrefix	if the prefix is unrecognised
	 * @throws PhoneAlreadyExists 	if the phone already exists
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 * @throws IncompatiblePrefix	if the given phone number and operator's prefix
	 * 								aren't equal
	 */
	@Override 
	public void registerPhone(RegisterPhoneDTO dto)
			throws InvalidPhoneType, UnrecognisedPrefix, PhoneAlreadyExists,
				PhoneNumberNotValid, IncompatiblePrefix {
		new RegisterPhoneService(dto).execute();
	}
	
	/**
	 * Gets the Phone Balance of a Phone with given number
	 * @param number 				the number of the Phone
	 * @return PhoneDTO				contains Phone Number and Phone Balance.
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if the Phone number not exists in network
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid			
	 */
	@Override
	public PhoneDTO getPhoneBalance(PhoneNumberDTO number)
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
		GetPhoneBalanceService service = new GetPhoneBalanceService(number);
		service.execute();
		return service.getPhoneDTO();
	}

	/**
	 * Increase the Phone Balance of a Phone with given number
	 * @param phone 					the number of the Phone and the amount you want
	 * 									increase.
	 * @throws UnrecognisedPrefix 		if the given prefix does not match any 
	 * 							  		operator in the network
	 * @throws PhoneNotExists			if the Phone with given number doesn't exist
	 * @throws BalanceLimitExceeded		Existing balance more amount to increase > 100
	 * @throws InvalidAmount			if the amount is null or negative
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid  
	 */
	@Override
	public void increasePhoneBalance(IncreasePhoneBalanceDTO phone)
			throws UnrecognisedPrefix, PhoneNotExists, 
			BalanceLimitExceeded, InvalidAmount, PhoneNumberNotValid {
		IncreasePhoneBalanceService service = new IncreasePhoneBalanceService(phone);
		service.execute();
	}
	
	/**
	 * Gets a list of all the Phones from an Operator
	 * @param prefix 				the prefix of the Operator
	 * @return ListPhonesDTO		the list of Phones
	 * @throws UnrecognisedPrefix 	if the given prefix does not match any 
	 * 							  	operator in the network
	 */
	@Override
	public ListPhonesDTO listOperatorPhones(OperatorPrefixDTO prefix) 
			throws UnrecognisedPrefix {
		ListOperatorPhonesService service = new ListOperatorPhonesService(prefix);
		service.execute();
		return service.getOperatorPhones();
	}
	
	/**
	 * Removes given phone from the network
	 * @param number				the number of the phone to be removed
	 * @throws PhoneNotExists		if the Phone with given number doesn't exist
	 * @throws UnrecognisedPrefix	if given prefix doesn't exist
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 */
	@Override
	public void cancelPhoneRegistry(PhoneNumberDTO number)
			throws PhoneNotExists, UnrecognisedPrefix, PhoneNumberNotValid {
		(new CancelPhoneRegistryService(number)).execute();
	}

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
     * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 */
	@Override
	public void sendSMS(SendSMSDTO dto) 
			throws UnrecognisedPrefix, PhoneNotExists, NotPositiveBalance,
				InvalidStateSendSMS, SMSMessageNotValid, InvalidAmount, PhoneNumberNotValid {
		Transaction.begin();
		boolean txCommited = false;
		try {
			SendSMSService send = new SendSMSService(dto);
			send.dispatch();
			ReceiveSMSService receive = new ReceiveSMSService(send.getSMSDTO());
			receive.dispatch();
			Transaction.commit();
			txCommited = true;
		} finally {
			if(!txCommited)
				Transaction.abort();
		}		
	}
	
	/**
	 * Change the state of the Phone with a given phone number to a given state
	 * @param newState	dto holding the desired phone number and the new state
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws InvalidState			if the state given in the dto isn't a valid state
	 * @throws CantChangeState 		if the new state in'st invalid
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 */
	@Override
	public void setPhoneState(PhoneStateDTO dto) 
			throws UnrecognisedPrefix, PhoneNotExists, 
			InvalidState, CantChangeState, PhoneNumberNotValid {
		SetPhoneStateService service = new SetPhoneStateService(dto);
		service.execute();
	}
	
	/**
	 * Get the state of the Phone with a given phone number
	 * @param number	dto holding the desired phone number 
	 * @throws UnrecognisedPrefix	if the prefix isn't recognized
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 */
	@Override
	public PhoneStateDTO getPhoneState(PhoneNumberDTO dto) 
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
		GetPhoneStateService service = new GetPhoneStateService(dto);
		service.execute();
		return service.getPhoneStateDTO();
	}
	
	/** 
	 * Getter of a collection of SMS Communications from a specific Number.
	 * @param number	dto holding the desired phone number
	 * @return	dto holding a List of SMS Communications received by the given number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognized
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 */
	@Override
	public PhoneReceivedSMSListDTO getPhoneSMSReceivedMessages(PhoneNumberDTO number)
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
		GetPhoneSMSReceivedMessagesService service = new GetPhoneSMSReceivedMessagesService(number);
		service.execute();
		return service.getDTOSMSList();
	}

	/**
	 *  * Get the Last communication of a specific Phone Number
	 * @param number	the number which we want to know it last communication
	 * @return the last communication performed by a specific Number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognized
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws NoMadeCommunication if the last made communication is null
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 */
	@Override
	public LastMadeCommunicationDTO getLastMadeCommunication(PhoneNumberDTO number) 
			throws UnrecognisedPrefix, PhoneNotExists, 
			NoMadeCommunication, PhoneNumberNotValid {
		GetLastMadeCommunicationService service = new GetLastMadeCommunicationService(number);
		service.execute();
		return service.getLastCommunication();
	}

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
	 * @throws InvalidStateReceiveVoice	only on and silent states allow the phone to
	 * 									receive voice calls
	 * @throws InvalidStateReceiveVideo	only on and silent states allow the phone to
	 * 									receive Video calls
	 * @throws CantMakeVoiceCall		if the source is not a phone capable of making
	 * 									voice calls
	 * @throws CantMakeVideoCall		if the source is not a phone capable of making
	 * 									Video calls
	 * @throws CantReceiveVoiceCall		if the source is not a phone capable of
	 * 									receiving voice calls
	 * @throws CantReceiveVideoCall		if the source is not a phone capable of
	 * 									receiving Video calls
	 * @throws InvalidCallType			if the given call type isn't Voice or Video
	 */
	@Override
	public void makeCall(CallDTO call) 
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid, 
				NotPositiveBalance, InvalidStateMakeVoice, InvalidStateMakeVideo,
				InvalidStateReceiveVoice, InvalidStateReceiveVideo, CantMakeVoiceCall,
				CantMakeVideoCall, CantReceiveVoiceCall, CantReceiveVideoCall,
				InvalidCallType {
		Transaction.begin();
		boolean txCommited = false;
		try {
			call.setStartTime(System.currentTimeMillis()/1000);
			MakeCallService makeService = new MakeCallService(call);
			makeService.dispatch();
			ReceiveCallService receiveService = new ReceiveCallService(call);
			receiveService.dispatch();
			Transaction.commit();
			txCommited = true;
		} finally {
			if (!txCommited) {
				Transaction.abort();
			}
		}
	}
	
	/**
	 * Finishes a call
	 * @param finishDto							the source and end time info
	 * @throws UnrecognisedPrefix 				if the source prefix is not recognised
	 * @throws PhoneNotExists 					if the source Phone does not exist
	 * @throws DurationNotValid					if the duration of the call is not a valid one
	 * @throws InvalidStateFinishMakingCall		if the source Phone is not currently making a call
	 * @throws InvalidStateFinishReceivingCall	if the destination Phone is not currently making a call
	 * @throws CommunicationError				only occurs when using remote services
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 */
	@Override
	public void finishCall(FinishCallDTO finishDto)
			throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
				InvalidStateFinishMakingCall, InvalidStateFinishReceivingCall, 
				CommunicationError, PhoneNumberNotValid {
		Transaction.begin();
		boolean txCommited = false;
		try {
			finishDto.setEndTime(System.currentTimeMillis()/1000);
			FinishCallOnSourceService finishSource
				= new FinishCallOnSourceService(finishDto);
			finishSource.dispatch();
			FinishCallOnDestinationService finishDestination
				= new FinishCallOnDestinationService(finishSource.getFinishCallOnDestinationDTO());
			finishDestination.dispatch();
			Transaction.commit();
			txCommited = true;
		} finally {
			if(!txCommited)
				Transaction.abort();
		}
	}
}
