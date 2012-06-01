package anacom.presentationserver.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.joda.time.DateTime;

import cacert.shared.stubs.Cacert;
import cacert.shared.stubs.CacertApplicationServerPortType;
import cacert.shared.stubs.CertificateType;
import cacert.shared.stubs.SignCertificateType;

import security.CryptoManager;
import anacom.applicationserver.SecurityInfo;
import anacom.presentationserver.server.handlers.PacketNumberHandler;
import anacom.shared.handlers.SecurityHandler;
import anacom.presentationserver.server.replication.WSCommunication;
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
import anacom.shared.dto.ReceivedSMSDTO;
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
import anacom.shared.misc.GetBlackList;
import anacom.shared.stubs.client.Anacom;
import anacom.shared.stubs.client.BalanceLimitExceededType;
import anacom.shared.stubs.client.BonusValueNotValidType;
import anacom.shared.stubs.client.CantChangeStateType;
import anacom.shared.stubs.client.CantMakeVideoCallType;
import anacom.shared.stubs.client.CantMakeVoiceCallType;
import anacom.shared.stubs.client.CantReceiveVideoCallType;
import anacom.shared.stubs.client.CantReceiveVoiceCallType;
import anacom.shared.stubs.client.CommunicationErrorType;
import anacom.shared.stubs.client.DurationNotValidType;
import anacom.shared.stubs.client.FinishCallOnDestinationType;
import anacom.shared.stubs.client.FinishCallType;
import anacom.shared.stubs.client.IncompatiblePrefixType;
import anacom.shared.stubs.client.IncreasePhoneBalanceType;
import anacom.shared.stubs.client.InvalidAmountType;
import anacom.shared.stubs.client.InvalidCallTypeType;
import anacom.shared.stubs.client.InvalidPhoneTypeType;
import anacom.shared.stubs.client.InvalidStateFinishMakingCallException;
import anacom.shared.stubs.client.InvalidStateFinishMakingCallType;
import anacom.shared.stubs.client.InvalidStateFinishReceivingCallException;
import anacom.shared.stubs.client.InvalidStateFinishReceivingCallType;
import anacom.shared.stubs.client.InvalidStateMakeVideoType;
import anacom.shared.stubs.client.InvalidStateMakeVoiceType;
import anacom.shared.stubs.client.InvalidStateReceiveVideoType;
import anacom.shared.stubs.client.InvalidStateReceiveVoiceType;
import anacom.shared.stubs.client.InvalidStateSendSMSType;
import anacom.shared.stubs.client.InvalidStateType;
import anacom.shared.stubs.client.LastMadeCommunicationType;
import anacom.shared.stubs.client.MakeCallType;
import anacom.shared.stubs.client.NoMadeCommunicationException;
import anacom.shared.stubs.client.NoMadeCommunicationType;
import anacom.shared.stubs.client.NotPositiveBalanceType;
import anacom.shared.stubs.client.OperatorDetailedType;
import anacom.shared.stubs.client.OperatorNameAlreadyExistsType;
import anacom.shared.stubs.client.OperatorNameNotValidType;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsType;
import anacom.shared.stubs.client.OperatorPrefixNotValidType;
import anacom.shared.stubs.client.OperatorPrefixType;
import anacom.shared.stubs.client.PhoneAlreadyExistsType;
import anacom.shared.stubs.client.PhoneListType;
import anacom.shared.stubs.client.PhoneNotExistsType;
import anacom.shared.stubs.client.PhoneNumberNotValidType;
import anacom.shared.stubs.client.PhoneNumberType;
import anacom.shared.stubs.client.PhoneStateType;
import anacom.shared.stubs.client.PhoneType;
import anacom.shared.stubs.client.ReceiveSMSType;
import anacom.shared.stubs.client.ReceivedSMSListType;
import anacom.shared.stubs.client.ReceivedSMSType;
import anacom.shared.stubs.client.RegisterPhoneType;
import anacom.shared.stubs.client.SMSMessageNotValidType;
import anacom.shared.stubs.client.SendSMSType;
import anacom.shared.stubs.client.UnrecognisedPrefixType;

/**
 * A Bridge Class from AnacomServerBridge.
 * Specialization of AnacomServer to Distributed Systems
 * All methods to execute a distributed service are here.
 */
// FIXME: parametros do CommunicationError tem de ser alterados - neves
public class DistributedAnacomServerBridge implements AnacomServerBridge {

	private final WSCommunication server;
	private final Lock lock;
	private SecurityInfo securityInfo;
	
	/**
	 * Constructor
	 * The handlers are created here.
	 */
	public DistributedAnacomServerBridge(WSCommunication server) {
		this.server = server;
		this.lock = new ReentrantLock();
		this.securityInfo = SecurityInfo.getInstance();
		this.initializeBindingContext();
	}
	
	private void initializeBindingContext() {
		CryptoManager manager = CryptoManager.getInstance();
		
		try {
			String publicKey = manager.getPublicRSAKeyfromFile("RSAkeys/PSkeys");
			String privateKey = manager.getPrivateRSAKeyfromFile("RSAkeys/PSkeys");
			String CAPublickey = manager.getPublicRSAKeyfromFile("RSAkeys/CAkeys");
			
			//Construct an DTO for communication
			SignCertificateType signature = new SignCertificateType();
			
			//The name of presentation server
			signature.setEntityName("Presentation_Server");
			signature.setPublicKey(publicKey);
			
			//make a new PortType to future web communications
			CacertApplicationServerPortType cacert = 
					new Cacert().getCacertApplicationServicePort();
			
			//set new handlers to communication with CA
			this.setHandlersCACommunication(cacert);
			
			/*
			 * Make the first web service to sign Certificate of Presentation
			 * Server 
			 */
			CertificateType certificate = cacert.signCertificate(signature);
			this.initializeSecurityInfo(publicKey, privateKey, 
										certificate, CAPublickey);
			
			//initialize service to observe the blocked certificate list
			GetBlackList revogatedList = new GetBlackList(cacert);
			
		} catch(Exception e) {
			System.err.println("[presentation-server]  " + 
							"ERROR with bootstrap configurations");
		}
	}
	
	/**
	 * Set Hanlers to communication with CA Manager 
	 * @param cacert
	 */
	private void setHandlersCACommunication(CacertApplicationServerPortType cacert) {
		Binding bindingCA = ((BindingProvider) cacert).getBinding();
		@SuppressWarnings("rawtypes")
		List<Handler> handlers = bindingCA.getHandlerChain();
		handlers.add(new SecurityHandler());
		bindingCA.setHandlerChain(handlers);
	}
	
	/**
	 * Initialize the Security Info of this Server
	 * @param publicKey	the public key of the server
	 * @param privateKey	the private key of the server
	 * @param certificate	the certificate signed by CA
	 * @param CAPublicKey	the CA public key
	 */
	private void initializeSecurityInfo(String publicKey, 
										String privateKey, 
										CertificateType certificate,
										String CAPublicKey) {
		this.securityInfo.setThisEntityPublicKey(publicKey);
		this.securityInfo.setThisEntityPrivateKey(privateKey);
		this.securityInfo.setCertificate(certificate);
		this.securityInfo.setCertificateAPublicKey(CAPublicKey);
	}
	
	/**
	 * Returns the current timestamp in the ws format.
	 * @return XMLGregorianCalender 	the timestamp
	 */
	private XMLGregorianCalendar getTimestamp() {
		DateTime tmp = new DateTime(); 
		XMLGregorianCalendar timestamp = null;
		try {
			timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			timestamp.setYear(tmp.getYear());
			timestamp.setMonth(tmp.getMonthOfYear());
			timestamp.setDay(tmp.getDayOfMonth());
			timestamp.setHour(tmp.getHourOfDay());
			timestamp.setMinute(tmp.getMinuteOfHour());
			timestamp.setSecond(tmp.getSecondOfMinute());
			timestamp.setMillisecond(tmp.getMillisOfSecond());
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	
    /**
	 * Registers a new Operator in the Network
	 * @param dto 			the data of the Operator to be registered
	 * @throws OperatorPrefixAlreadyExists 	if an Operator with the given prefix
	 * 										already exists
	 * @throws OperatorNameAlreadyExists   	if an Operator with the given name
	 * 										already exists
	 * @throws OperatorPrefixNotValid		if given prefix isn't valid
	 * @throws OperatorNameNotValid			if given name isn't valid
	 * @throws CommunicationError			if there's an error in the
	 * 										communication with the service
	 * @throws BonusValueNotValid			if the given tax Bonus isn't valid
	 */
	@Override
	public void registerOperator(OperatorDetailedDTO dto)
			throws 	OperatorNameAlreadyExists, 
					OperatorPrefixAlreadyExists,
					OperatorPrefixNotValid,
					OperatorNameNotValid,
					CommunicationError,
					UDDICommunicationError,
					QueryServiceException {
		OperatorDetailedType remoteDTO = new OperatorDetailedType();
		remoteDTO.setName(dto.getName());
		remoteDTO.setPrefix(dto.getPrefix());
		remoteDTO.setSmsCost(new BigInteger("" + dto.getSmsCost()));
		remoteDTO.setTax(new BigInteger("" + dto.getTax()));
		remoteDTO.setVideoCost(new BigInteger("" + dto.getVideoCost()));
		remoteDTO.setVoiceCost(new BigInteger("" + dto.getVoiceCost()));
		remoteDTO.setBonus(new BigInteger("" + dto.getBonus()));
		remoteDTO.setTimestamp(getTimestamp());
		try {
			this.lock.lock();
			this.server.registerOperator(remoteDTO, dto.getPrefix());
		} 
		catch(anacom.shared.stubs.client.OperatorNameAlreadyExistsException e) {
			OperatorNameAlreadyExistsType info = e.getFaultInfo();
			throw new OperatorNameAlreadyExists(info.getName(),
												info.getPrefix(),
												info.getSmsCost().intValue(),
												info.getVoiceCost().intValue(),
												info.getVideoCost().intValue(),
												info.getTax().intValue(),
												info.getBonus().intValue());
		} 
		catch(anacom.shared.stubs.client.OperatorPrefixAlreadyExistsException e) {
			OperatorPrefixAlreadyExistsType info = e.getFaultInfo();
			throw new OperatorPrefixAlreadyExists(info.getName(),
												  info.getPrefix(),
												  info.getSmsCost().intValue(),
												  info.getVoiceCost().intValue(),
												  info.getVideoCost().intValue(),
												  info.getTax().intValue(),
												  info.getBonus().intValue());
		}
		catch(anacom.shared.stubs.client.OperatorPrefixNotValidException e) {
			OperatorPrefixNotValidType info = e.getFaultInfo();
			throw new OperatorPrefixNotValid(info.getPrefix());
		}
		catch(anacom.shared.stubs.client.OperatorNameNotValidException e) {
			OperatorNameNotValidType info = e.getFaultInfo();
			throw new OperatorNameNotValid(info.getName());
		}
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(anacom.shared.stubs.client.BonusValueNotValidException e){
			BonusValueNotValidType info = e.getFaultInfo();
			throw new BonusValueNotValid(info.getBonus().intValue());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("register operator in operator "
					+ dto.getPrefix());
		}
		finally {
			this.lock.unlock();
		}
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
	 * @throws CommunicationError	if there's an error in the communication with
	 * 								the service
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public void registerPhone(RegisterPhoneDTO dto)
			throws 	InvalidPhoneType,
					UnrecognisedPrefix, 
					PhoneAlreadyExists,
					PhoneNumberNotValid,
					IncompatiblePrefix,
					CommunicationError,
					UDDICommunicationError,
					QueryServiceException {
		RegisterPhoneType remoteDTO = new RegisterPhoneType();
		remoteDTO.setType(dto.getType());
		remoteDTO.setNumber(dto.getNumber());
		remoteDTO.setOpPrefix(dto.getOperatorPrefix());
		remoteDTO.setTimestamp(getTimestamp());
		
		try {
			this.lock.lock();
			this.server.registerPhone(remoteDTO, dto.getOperatorPrefix());
		}
		catch(anacom.shared.stubs.client.InvalidPhoneTypeException e) {
			InvalidPhoneTypeType info = e.getFaultInfo();
			throw new InvalidPhoneType(info.getNumber(), info.getType());
		}
		catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		} 
		catch(anacom.shared.stubs.client.PhoneAlreadyExistsException e) {
			PhoneAlreadyExistsType info = e.getFaultInfo();
			throw new PhoneAlreadyExists(info.getNumber());
		}
		catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
			PhoneNumberNotValidType info = e.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		}
		catch(anacom.shared.stubs.client.IncompatiblePrefixException e) {
			IncompatiblePrefixType info = e.getFaultInfo();
			throw new IncompatiblePrefix(info.getOperatorprefix(), info.getPhoneprefix());
		} 
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("register phone 2g in operator "
					+ dto.getOperatorPrefix());
		}
		finally {
			this.lock.unlock();
		}
	}

	/**
	 * Gets the Phone Balance of a Phone with given number
	 * @param number 				the number of the Phone
	 * @return PhoneDTO				contains Phone Number and Phone Balance.
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if the Phone number not exists in network
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public PhoneDTO getPhoneBalance(PhoneNumberDTO dto)
			throws 	UnrecognisedPrefix, 
					PhoneNotExists,
					CommunicationError,
					UDDICommunicationError,
					QueryServiceException,
					PhoneNumberNotValid {
		PhoneNumberType remoteDTO = new PhoneNumberType();
		remoteDTO.setNumber(dto.getNumber());
		remoteDTO.setTimestamp(getTimestamp());
		PhoneDTO output = null;
		try {
			this.lock.lock();
			PhoneType phone = this.server.getPhoneBalance(remoteDTO, dto.getPrefix());
			output = new PhoneDTO(phone.getNumber(), phone.getBalance().intValue());
		}
		catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
			PhoneNumberNotValidType info = e.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		}
		catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		}
		catch(anacom.shared.stubs.client.PhoneNotExistsException e) {
			PhoneNotExistsType info = e.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		} 
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("get phone balance in operator "
					+ dto.getPrefix());
		}
		finally {
			this.lock.unlock();
		}
		return output;
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
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI 
	 */
	@Override
	public void increasePhoneBalance(IncreasePhoneBalanceDTO dto)
			throws 	UnrecognisedPrefix, 
					PhoneNotExists, 
					BalanceLimitExceeded,
					InvalidAmount,
					CommunicationError,
					UDDICommunicationError,
					QueryServiceException,
					PhoneNumberNotValid {
		IncreasePhoneBalanceType remoteDTO = new IncreasePhoneBalanceType();
		remoteDTO.setAmount(new BigInteger("" + dto.getAmount()));
		remoteDTO.setNumber(dto.getNumber());
		remoteDTO.setTimestamp(getTimestamp());
		
		try {
			this.lock.lock();
			this.server.increasePhoneBalance(remoteDTO, dto.getPrefix());
		}
		catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
			PhoneNumberNotValidType info = e.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		}
		catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		}
		catch(anacom.shared.stubs.client.PhoneNotExistsException e) {
			PhoneNotExistsType info = e.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		}
		catch(anacom.shared.stubs.client.InvalidAmountException e) {
			InvalidAmountType info = e.getFaultInfo();
			throw new InvalidAmount(info.getNumber(), info.getAmount().intValue());
		} 
		catch(anacom.shared.stubs.client.BalanceLimitExceededException e) {
			BalanceLimitExceededType info = e.getFaultInfo();
			throw new BalanceLimitExceeded(info.getNumber());
		}
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("increase phone balance in operator "
					+ dto.getPrefix());
		}
		finally {
			this.lock.unlock();
		}
		
	}
	
	
	/**
	 * Gets a list of all the Phones from an Operator
	 * @param prefix 				the prefix of the Operator
	 * @return ListPhonesDTO		the list of Phones
	 * @throws UnrecognisedPrefix 	if the given prefix does not match any 
	 * 							  	operator in the network
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public ListPhonesDTO listOperatorPhones(OperatorPrefixDTO dto)
			throws 	UnrecognisedPrefix,
					CommunicationError,
					UDDICommunicationError,
					QueryServiceException {
		OperatorPrefixType remoteDTO = new OperatorPrefixType();
		remoteDTO.setPrefix(dto.getPrefix());
		remoteDTO.setTimestamp(getTimestamp());
		ListPhonesDTO output;
		try {
			this.lock.lock();
			PhoneListType phoneList = this.server.listOperatorPhones(remoteDTO, dto.getPrefix());
			List<PhoneDTO> returnList = new ArrayList<PhoneDTO>();

			for(PhoneType phone : phoneList.getPhoneDTOList()) {
				PhoneDTO tmp = new PhoneDTO(phone.getNumber(), phone.getBalance().intValue());
				returnList.add(tmp);
			}
			output = new ListPhonesDTO(returnList);
		} 
		catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		} 
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("list operator's phones in operator "
					+ dto.getPrefix());
		}
		finally {
			this.lock.unlock();
		}
		return output;
	}

	/**
	 * Cancels phone registry in the network
	 * @param number				the number of the phone to be cancelled
	 * @throws PhoneNotExists		thrown if Phone with given number doesn't exist
	 * @throws UnrecognisedPrefix	if operator with given prefix doesn't exist
	 * @throws CommunicationError			only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public void cancelPhoneRegistry(PhoneNumberDTO dto)
			throws 	PhoneNotExists, 
					UnrecognisedPrefix,
					CommunicationError, 
					UDDICommunicationError,
					QueryServiceException,
					PhoneNumberNotValid {
		PhoneNumberType remoteDTO = new PhoneNumberType();
		remoteDTO.setNumber(dto.getNumber());
		remoteDTO.setTimestamp(getTimestamp());
		
		try {
			this.lock.lock();
			this.server.cancelPhoneRegistry(remoteDTO, dto.getPrefix());
		} 
		catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
			PhoneNumberNotValidType info = e.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		}
		catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		} 
		catch(anacom.shared.stubs.client.PhoneNotExistsException e) {
			PhoneNotExistsType info = e.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		}
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("cancel phone registry in operator "
					+ dto.getPrefix());
		}
		finally {
			this.lock.unlock();
		}
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
     * @throws CommunicationError	only occurs when using remote services
     * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public void sendSMS(SendSMSDTO dto) 
			throws 	UnrecognisedPrefix,
					PhoneNotExists, 
					NotPositiveBalance, 
					InvalidStateSendSMS,
					SMSMessageNotValid,
					InvalidAmount,
					CommunicationError,
					UDDICommunicationError,
					QueryServiceException,
					PhoneNumberNotValid {
		SendSMSType sendRemoteDTO = new SendSMSType();
		sendRemoteDTO.setDestination(dto.getDestination());
		sendRemoteDTO.setMessage(dto.getMessage());
		sendRemoteDTO.setSource(dto.getSource());
		sendRemoteDTO.setTimestamp(getTimestamp());
		
		try {
			this.lock.lock();
			ReceiveSMSType receiveRemoteDTO = this.server.sendSMS(sendRemoteDTO, dto.getSourcePrefix());
			this.server.receiveSMS(receiveRemoteDTO, dto.getDestinationPrefix());
		}
		catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
				PhoneNumberNotValidType info = e.getFaultInfo();
				throw new PhoneNumberNotValid(info.getNumber());
		}
		catch(anacom.shared.stubs.client.PhoneNotExistsException e) {
			PhoneNotExistsType info = e.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		}
		catch(anacom.shared.stubs.client.NotPositiveBalanceException e) {
			NotPositiveBalanceType info = e.getFaultInfo();
			throw new NotPositiveBalance(info.getNumber(), info.getBalance().intValue());
		}
		catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		}
		catch(anacom.shared.stubs.client.InvalidStateSendSMSException e) {
			InvalidStateSendSMSType info = e.getFaultInfo();
			throw new InvalidStateSendSMS(info.getNumber(), info.getState());
		}
		catch(anacom.shared.stubs.client.SMSMessageNotValidException e) {
			SMSMessageNotValidType info = e.getFaultInfo();
			throw new SMSMessageNotValid(info.getOtherParty(), info.getMessage());
		}
		catch(anacom.shared.stubs.client.InvalidAmountException e) {
			InvalidAmountType info = e.getFaultInfo();
			throw new InvalidAmount(info.getNumber(), info.getAmount().intValue());
		}
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("send sms in operator "
					+ dto.getSourcePrefix());
		}
		finally {
			this.lock.unlock();
		}
	}
	
	/**
	 * Change the state of the Phone with a given phone number to a given state
	 * @param newState	dto holding the desired phone number and the new state
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws InvalidState			if the state given in the dto isn't a valid state
	 * @throws CantChangeState 		if the new state in'st invalid
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public void setPhoneState(PhoneStateDTO dto)
			throws 	UnrecognisedPrefix, 
					PhoneNotExists, 
					InvalidState, 
					CantChangeState,
					CommunicationError,
					UDDICommunicationError,
					QueryServiceException,
					PhoneNumberNotValid {
		PhoneStateType remoteDTO = new PhoneStateType();
		remoteDTO.setNumber(dto.getNumber());
		remoteDTO.setState(dto.getState());
		remoteDTO.setTimestamp(getTimestamp());
		try {
			this.lock.lock();
			this.server.setPhoneState(remoteDTO, dto.getPrefix());
		} catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
			PhoneNumberNotValidType info = e.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		} catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		} catch(anacom.shared.stubs.client.PhoneNotExistsException e) {
			PhoneNotExistsType info = e.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		} catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		} catch(anacom.shared.stubs.client.InvalidStateException e) {
			InvalidStateType info = e.getFaultInfo();
			throw new InvalidState(info.getNumber(), info.getState());
		} catch(anacom.shared.stubs.client.CantChangeStateException e) {
			CantChangeStateType info = e.getFaultInfo();
			throw new CantChangeState(	info.getNumber(), 
										info.getCurrentState(), 
										info.getInvalidState());
		} catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("get phone State in Number "
					+ dto.getNumber());
		}
		this.lock.unlock();
	}
	
	/**
	 * Get the state of the Phone with a given phone number
	 * @param number	dto holding the desired phone number 
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override 
	public PhoneStateDTO getPhoneState(PhoneNumberDTO dto) 
			throws 	UnrecognisedPrefix, 
					PhoneNotExists, 
					CommunicationError, 
					UDDICommunicationError,
					QueryServiceException,
					PhoneNumberNotValid {
		
		PhoneNumberType remoteDTO = new PhoneNumberType();
		remoteDTO.setNumber(dto.getNumber());
		remoteDTO.setTimestamp(getTimestamp());
		PhoneStateDTO output = null;
		try {
			this.lock.lock();
			 PhoneStateType phone = 
					 this.server.getPhoneState(remoteDTO, dto.getPrefix());
			output = new PhoneStateDTO(phone.getNumber(), phone.getState());
		}
		catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
			PhoneNumberNotValidType info = e.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		}
		catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		}
		catch(anacom.shared.stubs.client.PhoneNotExistsException e) {
			PhoneNotExistsType info = e.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		} 
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("get phone State in Number "
					+ dto.getNumber());
		}
		finally {
			this.lock.unlock();
		}
		return output; 
	}

	/** 
	 * Getter of a collection of SMS Communications from a specific Number.
	 * @param number	dto holding the desired phone number
	 * @return	dto holding a List of SMS Communications received by the given number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public PhoneReceivedSMSListDTO getPhoneSMSReceivedMessages(PhoneNumberDTO number)
			throws 	UnrecognisedPrefix, 
					PhoneNotExists, 
					CommunicationError,
					UDDICommunicationError,
					QueryServiceException,
					PhoneNumberNotValid {
		PhoneNumberType remoteDTO = new PhoneNumberType();
		remoteDTO.setNumber(number.getNumber());
		remoteDTO.setTimestamp(getTimestamp());
		PhoneReceivedSMSListDTO output = null;
		try {
			this.lock.lock();
			ReceivedSMSListType reply = 
					this.server.getPhoneSMSReceivedMessages(remoteDTO, number.getPrefix());
			ArrayList<ReceivedSMSDTO> smsList = new ArrayList<ReceivedSMSDTO>();
			
			for (ReceivedSMSType sms : reply.getSMSDTOList()) {
				ReceivedSMSDTO tmp = new ReceivedSMSDTO(	sms.getMessage(),
			   												sms.getSource(),
			   												sms.getCost().intValue());
				smsList.add(tmp);
			}
			output = new PhoneReceivedSMSListDTO(reply.getNumber(), smsList);
		}
		catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
				PhoneNumberNotValidType info = e.getFaultInfo();
				throw new PhoneNumberNotValid(info.getNumber());
		}
		catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		}
		catch(anacom.shared.stubs.client.PhoneNotExistsException e) {
			PhoneNotExistsType info = e.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		} 
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("Get the Messages Received by number "
					+ number.getNumber());
		}
		finally {
			this.lock.unlock();
		}
		return output;
	}

	/**
	 * Get the Last communication of a specific Phone Number
	 * @param number	the number which we want to know it last communication
	 * @return the last communication performed by a specific Number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws NoMadeCommunication if the last made communication is null
	 * @throws CommunicationError	only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public LastMadeCommunicationDTO getLastMadeCommunication(
			PhoneNumberDTO number) 
					throws 	UnrecognisedPrefix, 
							PhoneNotExists, 
							NoMadeCommunication, 
							CommunicationError,
							UDDICommunicationError,
							QueryServiceException,
							PhoneNumberNotValid {
		PhoneNumberType remoteDTO = new PhoneNumberType();
		remoteDTO.setNumber(number.getNumber());
		remoteDTO.setTimestamp(getTimestamp());
		LastMadeCommunicationDTO output = null;
		try {
			this.lock.lock();
			LastMadeCommunicationType lastCommunication = 
					this.server.getLastMadeCommunication(remoteDTO, number.getPrefix());
			output = new LastMadeCommunicationDTO(
					lastCommunication.getDestination(),
					lastCommunication.getCommunicationType(), 
					lastCommunication.getCost().intValue(), 
					lastCommunication.getTotal().intValue());
			
		} catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
			PhoneNumberNotValidType info = e.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		} catch(anacom.shared.stubs.client.UnrecognisedPrefixException e) {
			UnrecognisedPrefixType info = e.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		}
		catch(anacom.shared.stubs.client.PhoneNotExistsException e) {
			PhoneNotExistsType info = e.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		} 
		catch(anacom.shared.stubs.client.CommunicationErrorException e) {
			CommunicationErrorType info = e.getFaultInfo();
			throw new CommunicationError(info.getService());
		}
		catch(javax.xml.ws.WebServiceException e) {
			throw new CommunicationError("Get the Last Communication performed by number "
					+ number.getNumber());
		} catch (NoMadeCommunicationException e) {
			NoMadeCommunicationType info = e.getFaultInfo();
			throw new NoMadeCommunication(info.getNumber());
		}
		finally {
			this.lock.unlock();
		}
		return output;
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
	 * @throws CommunicationError		only occurs when using remote services
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public void makeCall(CallDTO call) 
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid, 
				NotPositiveBalance, InvalidStateMakeVoice, InvalidStateMakeVideo,
				InvalidStateReceiveVoice, InvalidStateReceiveVideo, CantMakeVoiceCall,
				CantMakeVideoCall, CantReceiveVoiceCall, CantReceiveVideoCall,
				InvalidCallType, CommunicationError, UDDICommunicationError,
				QueryServiceException {
		MakeCallType remoteCallDTO = new MakeCallType();
		remoteCallDTO.setSource(call.getSource());
		remoteCallDTO.setDestination(call.getDestination());
		remoteCallDTO.setStartTime(
				new BigInteger("" + System.currentTimeMillis()/1000));
		remoteCallDTO.setType(call.getType());
		remoteCallDTO.setTimestamp(getTimestamp());
		
		try {
			this.lock.lock();
			this.server.makeCall(remoteCallDTO, call.getSourcePrefix());
			this.server.receiveCall(remoteCallDTO, call.getDestinationPrefix());
		} catch(anacom.shared.stubs.client.UnrecognisedPrefixException upe) {
			UnrecognisedPrefixType info = upe.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		} catch(anacom.shared.stubs.client.PhoneNotExistsException pnee) {
			PhoneNotExistsType info = pnee.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		} catch(anacom.shared.stubs.client.PhoneNumberNotValidException pnnve) {
			PhoneNumberNotValidType info = pnnve.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		} catch(anacom.shared.stubs.client.NotPositiveBalanceException npbe) {
			NotPositiveBalanceType info = npbe.getFaultInfo();
			throw new NotPositiveBalance(info.getNumber(), info.getBalance().intValue());
		} catch(anacom.shared.stubs.client.InvalidStateMakeVoiceException ismve) {
			InvalidStateMakeVoiceType info = ismve.getFaultInfo();
			throw new InvalidStateMakeVoice(info.getNumber(), info.getState());
		} catch(anacom.shared.stubs.client.InvalidStateMakeVideoException ismve) {
			InvalidStateMakeVideoType info = ismve.getFaultInfo();
			throw new InvalidStateMakeVideo(info.getNumber(), info.getState());
		} catch(anacom.shared.stubs.client.InvalidStateReceiveVoiceException isrve) {
			InvalidStateReceiveVoiceType info = isrve.getFaultInfo();
			throw new InvalidStateReceiveVoice(info.getNumber(), info.getState());
		} catch(anacom.shared.stubs.client.InvalidStateReceiveVideoException isrve) {
			InvalidStateReceiveVideoType info = isrve.getFaultInfo();
			throw new InvalidStateReceiveVideo(info.getNumber(), info.getState());
		} catch(anacom.shared.stubs.client.CantMakeVoiceCallException cmvce) {
			CantMakeVoiceCallType info = cmvce.getFaultInfo();
			throw new CantMakeVoiceCall(info.getNumber());
		} catch(anacom.shared.stubs.client.CantMakeVideoCallException cmvce) {
			CantMakeVideoCallType info = cmvce.getFaultInfo();
			throw new CantMakeVideoCall(info.getNumber());
		} catch(anacom.shared.stubs.client.CantReceiveVoiceCallException crvce) {
			CantReceiveVoiceCallType info = crvce.getFaultInfo();
			throw new CantReceiveVoiceCall(info.getNumber());
		} catch(anacom.shared.stubs.client.CantReceiveVideoCallException crvce) {
			CantReceiveVideoCallType info = crvce.getFaultInfo();
			throw new CantReceiveVideoCall(info.getNumber());
		} catch(anacom.shared.stubs.client.InvalidCallTypeException icte) {
			InvalidCallTypeType info = icte.getFaultInfo();
			throw new InvalidCallType(info.getOtherParty(), info.getType());
		} catch(anacom.shared.stubs.client.CommunicationErrorException cee) {
			CommunicationErrorType info = cee.getFaultInfo();
			throw new CommunicationError(info.getService());
		} catch(javax.xml.ws.WebServiceException wse) {
			throw new CommunicationError("Make call from operator "
					+ call.getSourcePrefix());
		} finally {
			this.lock.unlock();
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
	 * @throws UDDICommunicationError	occurs when communication with UDDI
     * @throws QueryServiceException	occurs when the operator is not registered in UDDI
	 */
	@Override
	public void finishCall(FinishCallDTO finishDto)
			throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
				InvalidStateFinishMakingCall, InvalidStateFinishReceivingCall,
				CommunicationError, UDDICommunicationError, QueryServiceException,
				PhoneNumberNotValid {
		FinishCallType remoteDTO = new FinishCallType();
		remoteDTO.setSource(finishDto.getSource());
		remoteDTO.setEndTime(
				new BigInteger("" + System.currentTimeMillis()/1000));
		remoteDTO.setTimestamp(getTimestamp());
		
		try {
			this.lock.lock();
			FinishCallOnDestinationType finishDestinationRemoteDTO = 
					this.server.finishCall(remoteDTO, finishDto.getPrefix());
			this.server.finishCallOnDestination(
					finishDestinationRemoteDTO, 
					finishDestinationRemoteDTO.getDestination().substring(0, 2));
			
		} catch(anacom.shared.stubs.client.PhoneNumberNotValidException e) {
			PhoneNumberNotValidType info = e.getFaultInfo();
			throw new PhoneNumberNotValid(info.getNumber());
		} catch(anacom.shared.stubs.client.UnrecognisedPrefixException upe) {
			UnrecognisedPrefixType info = upe.getFaultInfo();
			throw new UnrecognisedPrefix(info.getOperatorPrefix());
		} catch(anacom.shared.stubs.client.PhoneNotExistsException pnee) {
			PhoneNotExistsType info = pnee.getFaultInfo();
			throw new PhoneNotExists(info.getNumber());
		} catch(anacom.shared.stubs.client.DurationNotValidException dnve) {
			DurationNotValidType info = dnve.getFaultInfo();
			throw new DurationNotValid(info.getOtherParty(),info.getDuration().intValue());
		} catch (InvalidStateFinishMakingCallException isfmce) {
			InvalidStateFinishMakingCallType info = isfmce.getFaultInfo();
			throw new InvalidStateFinishMakingCall(info.getNumber(), info.getState());
		} catch (InvalidStateFinishReceivingCallException isfrce) {
			InvalidStateFinishReceivingCallType info = isfrce.getFaultInfo();
			throw new InvalidStateFinishReceivingCall(info.getNumber(), info.getState());
		} catch(anacom.shared.stubs.client.CommunicationErrorException cee) {
			CommunicationErrorType info = cee.getFaultInfo();
			throw new CommunicationError(info.getService());
		} catch(javax.xml.ws.WebServiceException wse) {
			throw new CommunicationError("Finish call from operator "
					+ finishDto.getPrefix());
		} finally {
			this.lock.unlock();
		}
	}
}
