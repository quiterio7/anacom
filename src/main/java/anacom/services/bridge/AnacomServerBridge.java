package anacom.services.bridge;


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
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.BonusValueNotValid;
import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.IncompatiblePrefix;
import anacom.shared.exceptions.UDDI.QueryServiceException;
import anacom.shared.exceptions.UDDI.UDDICommunicationError;
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


public interface AnacomServerBridge  {
	
	/**
	 * Registers a new Operator in the Network
	 * @param dto 			the data of the Operator to be registered
	 * @throws OperatorPrefixAlreadyExists 	if an Operator with the given prefix
	 * 										already exists
	 * @throws OperatorNameAlreadyExists   	if an Operator with the given name
	 * 										already exists
	 * @throws OperatorPrefixNotValid		if given prefix isn't valid
	 * @throws OperatorNameNotValid			if given name isn't valid
	 * @throws BonusValueNotValid			if the given tax bonus is minor than 0
	 * @throws CommunicationError			only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public void registerOperator(OperatorDetailedDTO dto)
			throws OperatorNameAlreadyExists, OperatorPrefixAlreadyExists,
				OperatorPrefixNotValid, OperatorNameNotValid, 
				BonusValueNotValid, CommunicationError, 
				UDDICommunicationError, QueryServiceException;
	
	/**
	 * Registers a new Phone on the Network
	 * @param dto		stores the number of the Phone and the prefix
	 * 					of the operator on which it should be registered
	 * @throws InvalidPhoneType		if the given Phone type is invalid
	 * @throws UnrecognisedPrefix	if the prefix is unrecognised
	 * @throws PhoneAlreadyExists 	if the phone already exists
	 * @throws PhoneNumberNotValid	if the given phone number isn't valid
	 * @throws IncompatiblePrefix	if the given phone number and operator's prefix
	 * 								aren't equal
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public void registerPhone(RegisterPhoneDTO dto)
			throws InvalidPhoneType, UnrecognisedPrefix, PhoneAlreadyExists,
				PhoneNumberNotValid, IncompatiblePrefix, CommunicationError,
				UDDICommunicationError, QueryServiceException;
	
	/**
	 * Gets the Phone Balance of a Phone with given number
	 * @param number 				the number of the Phone
	 * @return PhoneDTO				contains Phone Number and Phone Balance.
	 * @throws UnrecognisedPrefix 	if the given number's prefix isn't correct 
	 * @throws PhoneNotExists		if the given Phone don't exist
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public PhoneDTO getPhoneBalance(PhoneNumberDTO number)
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid, CommunicationError,
			UDDICommunicationError, QueryServiceException;
	
	/**
	 * Increase the Phone Balance of a Phone with given number
	 * @param phone 				the number of the Phone and the amount you want
	 * 								increase.
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if the Phone number not exists in network
	 * @throws BalanceLimitExceeded	if the balance of the increase is higher than limit
	 * @throws InvalidAmount		if the amount is null or negative
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public void increasePhoneBalance(IncreasePhoneBalanceDTO phone)
			throws UnrecognisedPrefix, PhoneNotExists, BalanceLimitExceeded,
				InvalidAmount, PhoneNumberNotValid, CommunicationError,
				UDDICommunicationError, QueryServiceException;
	
	/**
	 * Gets a list of all the Phones from an Operator
	 * @param prefix 			the prefix of the Operator
	 * @return ListPhonesDTO	the list of Phones
	 * @throws UnrecognisedPrefix 	if the given prefix does not match any 
	 * 							  	operator in the network
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public ListPhonesDTO listOperatorPhones(OperatorPrefixDTO prefix)
			throws UnrecognisedPrefix, CommunicationError,
			UDDICommunicationError, QueryServiceException;
	
	/**
	 * Cancels phone registry in the network
	 * @param number				the number of the phone to be cancelled
	 * @throws PhoneNotExists		thrown if Phone with given number doesn't exist
	 * @throws UnrecognisedPrefix	if operator with given prefix doesn't exist
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public void cancelPhoneRegistry(PhoneNumberDTO number)
			throws PhoneNotExists, UnrecognisedPrefix, PhoneNumberNotValid, 
			CommunicationError, UDDICommunicationError, QueryServiceException;
	
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
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     * @throws CommunicationError	only occurs when using remote services
     * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public void sendSMS(SendSMSDTO dto) 
			throws UnrecognisedPrefix, PhoneNotExists, NotPositiveBalance,
				InvalidStateSendSMS, SMSMessageNotValid, InvalidAmount, PhoneNumberNotValid,
				CommunicationError, UDDICommunicationError, QueryServiceException;
	
	/**
	 * Change the state of the Phone with a given phone number to a given state
	 * @param newState	dto holding the desired phone number and the new state
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws InvalidState			if the state given in the dto isn't a valid state
	 * @throws CantChangeState 		if the new state in'st invalid
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public void setPhoneState(PhoneStateDTO dto)
		throws UnrecognisedPrefix, PhoneNotExists, 
		InvalidState, CantChangeState, PhoneNumberNotValid, 
		CommunicationError, UDDICommunicationError, QueryServiceException;
	
	/**
	 * Get the state of the Phone with a given phone number
	 * @param number	dto holding the desired phone number 
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public PhoneStateDTO getPhoneState(PhoneNumberDTO dto)
		throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid, 
		CommunicationError, UDDICommunicationError, QueryServiceException;
	
	/** 
	 * Getter of a collection of SMS Communications from a specific Number.
	 * @param number	dto holding the desired phone number
	 * @return	dto holding a List of SMS Communications received by the given number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public PhoneReceivedSMSListDTO getPhoneSMSReceivedMessages(PhoneNumberDTO number) 
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid,
			CommunicationError, UDDICommunicationError, QueryServiceException;
	
	/**
	 * Get the Last communication of a specific Phone Number
	 * @param number	the number which we want to know it last communication
	 * @return the last communication performed by a specific Number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws NoMadeCommunication if the last made communication is null
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public LastMadeCommunicationDTO getLastMadeCommunication(PhoneNumberDTO number) 
			throws UnrecognisedPrefix, PhoneNotExists, NoMadeCommunication, 
			PhoneNumberNotValid, CommunicationError, UDDICommunicationError, 
			QueryServiceException;

	
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
	 * @throws CommunicationError		only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public void makeCall(CallDTO call) 
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid, 
				NotPositiveBalance, InvalidStateMakeVoice, InvalidStateMakeVideo,
				InvalidStateReceiveVoice, InvalidStateReceiveVideo, CantMakeVoiceCall,
				CantMakeVideoCall, CantReceiveVoiceCall, CantReceiveVideoCall,
				InvalidCallType, CommunicationError, UDDICommunicationError, 
				QueryServiceException;;
	
	/**
	 * Finishes a call
	 * @param finishDto							the source and end time info
	 * @throws UnrecognisedPrefix 				if the source prefix is not recognised
	 * @throws PhoneNotExists 					if the source Phone does not exist
	 * @throws DurationNotValid					if the duration of the call is not a valid one
	 * @throws InvalidStateFinishMakingCall		if the source Phone is not currently making a call
	 * @throws InvalidStateFinishReceivingCall	if the destination Phone is not currently making a call
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 * @throws CommunicationError				only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	public void finishCall(FinishCallDTO finishDto)
			throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
			InvalidStateFinishMakingCall, InvalidStateFinishReceivingCall, 
			PhoneNumberNotValid, CommunicationError, UDDICommunicationError, 
			QueryServiceException;
	
}
