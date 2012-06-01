package anacom.applicationserver;



import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import cacert.shared.stubs.BlockCertificateType;
import cacert.shared.stubs.Cacert;
import cacert.shared.stubs.CacertApplicationServerPortType;
import cacert.shared.stubs.CertificateRemoteException;
import cacert.shared.stubs.CertificateType;

import anacom.services.CancelPhoneRegistryService;
import anacom.services.CreateNewOperatorService;
import anacom.services.FinishCallOnDestinationService;
import anacom.services.FinishCallOnSourceService;
import anacom.services.GetLastMadeCommunicationService;
import anacom.services.GetPhoneBalanceService;
import anacom.services.GetPhoneSMSReceivedMessagesService;
import anacom.services.GetPhoneStateService;
import anacom.services.GetTimestampService;
import anacom.services.IncreasePhoneBalanceService;
import anacom.services.ListOperatorPhonesService;
import anacom.services.MakeCallService;
import anacom.services.ReceiveCallService;
import anacom.services.ReceiveSMSService;
import anacom.services.RegisterPhoneService;
import anacom.services.SendSMSService;
import anacom.services.SetPhoneStateService;
import anacom.services.SetTimestampService;
import anacom.shared.dto.CallDTO;
import anacom.shared.dto.FinishCallDTO;
import anacom.shared.dto.FinishCallOnDestinationDTO;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.dto.OperatorDetailedDTO;
import anacom.shared.dto.OperatorPrefixDTO;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.dto.ReceivedSMSDTO;
import anacom.shared.dto.RegisterPhoneDTO;
import anacom.shared.dto.SMSDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.dto.TimestampDTO;
import anacom.shared.exceptions.AnacomException;
import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.handlers.SecurityHandler;
import security.CryptoManager;
import anacom.shared.stubs.server.AnacomApplicationServerPortType;
import anacom.shared.stubs.server.BalanceLimitExceededException;
import anacom.shared.stubs.server.BalanceLimitExceededType;
import anacom.shared.stubs.server.CantChangeStateException;
import anacom.shared.stubs.server.CantChangeStateType;
import anacom.shared.stubs.server.CantMakeVideoCallException;
import anacom.shared.stubs.server.CantMakeVideoCallType;
import anacom.shared.stubs.server.CantMakeVoiceCallException;
import anacom.shared.stubs.server.CantMakeVoiceCallType;
import anacom.shared.stubs.server.CantReceiveVideoCallException;
import anacom.shared.stubs.server.CantReceiveVideoCallType;
import anacom.shared.stubs.server.CantReceiveVoiceCallException;
import anacom.shared.stubs.server.CantReceiveVoiceCallType;
import anacom.shared.stubs.server.CommunicationErrorException;
import anacom.shared.stubs.server.CommunicationErrorType;
import anacom.shared.stubs.server.DurationNotValidException;
import anacom.shared.stubs.server.DurationNotValidType;
import anacom.shared.stubs.server.FinishCallOnDestinationType;
import anacom.shared.stubs.server.FinishCallType;
import anacom.shared.stubs.server.IncompatiblePrefixException;
import anacom.shared.stubs.server.IncompatiblePrefixType;
import anacom.shared.stubs.server.IncreasePhoneBalanceType;
import anacom.shared.stubs.server.InvalidAmountException;
import anacom.shared.stubs.server.InvalidAmountType;
import anacom.shared.stubs.server.InvalidCallTypeException;
import anacom.shared.stubs.server.InvalidCallTypeType;
import anacom.shared.stubs.server.InvalidPhoneTypeException;
import anacom.shared.stubs.server.InvalidPhoneTypeType;
import anacom.shared.stubs.server.InvalidStateException;
import anacom.shared.stubs.server.InvalidStateFinishMakingCallException;
import anacom.shared.stubs.server.InvalidStateFinishMakingCallType;
import anacom.shared.stubs.server.InvalidStateFinishReceivingCallException;
import anacom.shared.stubs.server.InvalidStateFinishReceivingCallType;
import anacom.shared.stubs.server.InvalidStateMakeVideoException;
import anacom.shared.stubs.server.InvalidStateMakeVideoType;
import anacom.shared.stubs.server.InvalidStateMakeVoiceException;
import anacom.shared.stubs.server.InvalidStateMakeVoiceType;
import anacom.shared.stubs.server.InvalidStateReceiveVideoException;
import anacom.shared.stubs.server.InvalidStateReceiveVideoType;
import anacom.shared.stubs.server.InvalidStateReceiveVoiceException;
import anacom.shared.stubs.server.InvalidStateReceiveVoiceType;
import anacom.shared.stubs.server.InvalidStateSendSMSException;
import anacom.shared.stubs.server.InvalidStateSendSMSType;
import anacom.shared.stubs.server.InvalidStateType;
import anacom.shared.stubs.server.LastMadeCommunicationType;
import anacom.shared.stubs.server.MakeCallType;
import anacom.shared.stubs.server.NoMadeCommunicationException;
import anacom.shared.stubs.server.NoMadeCommunicationType;
import anacom.shared.stubs.server.NotPositiveBalanceException;
import anacom.shared.stubs.server.NotPositiveBalanceType;
import anacom.shared.stubs.server.OperatorDetailedType;
import anacom.shared.stubs.server.OperatorNameAlreadyExistsException;
import anacom.shared.stubs.server.OperatorNameAlreadyExistsType;
import anacom.shared.stubs.server.OperatorNameNotValidException;
import anacom.shared.stubs.server.OperatorNameNotValidType;
import anacom.shared.stubs.server.OperatorPrefixAlreadyExistsException;
import anacom.shared.stubs.server.OperatorPrefixAlreadyExistsType;
import anacom.shared.stubs.server.OperatorPrefixNotValidException;
import anacom.shared.stubs.server.OperatorPrefixNotValidType;
import anacom.shared.stubs.server.OperatorPrefixType;
import anacom.shared.stubs.server.PhoneAlreadyExistsException;
import anacom.shared.stubs.server.PhoneAlreadyExistsType;
import anacom.shared.stubs.server.PhoneListType;
import anacom.shared.stubs.server.PhoneNotExistsException;
import anacom.shared.stubs.server.PhoneNotExistsType;
import anacom.shared.stubs.server.PhoneNumberNotValidException;
import anacom.shared.stubs.server.PhoneNumberNotValidType;
import anacom.shared.stubs.server.PhoneNumberType;
import anacom.shared.stubs.server.PhoneStateType;
import anacom.shared.stubs.server.PhoneType;
import anacom.shared.stubs.server.ReceiveSMSType;
import anacom.shared.stubs.server.ReceivedSMSListType;
import anacom.shared.stubs.server.ReceivedSMSType;
import anacom.shared.stubs.server.RegisterPhoneType;
import anacom.shared.stubs.server.SendSMSType;
import anacom.shared.stubs.server.UnrecognisedPrefixException;
import anacom.shared.stubs.server.UnrecognisedPrefixType;
import anacom.shared.stubs.server.VoidResponseType;

/**
 * 
 * This layer separates the Presentation layer from the Business Logic layer.
 */
@javax.jws.WebService(
		endpointInterface="anacom.shared.stubs.server.AnacomApplicationServerPortType", 
		wsdlLocation="/anacom.wsdl",
		name="AnacomApplicationServerPortType",
		portName="AnacomApplicationServicePort",
		targetNamespace="http://anacom",
		serviceName="anacom"
)

// For initialization of Handler Chain
@javax.jws.HandlerChain(file="handler-chain.xml")
public class ApplicationServerWebService implements AnacomApplicationServerPortType {

	/**
	 * Converts DateTime to XMLGregorianCalendar
	 * @param datetime					the datetime to be converted.
	 * @return XMLGregorianCalendar		the dto's timestamp (ws rep)
	 */
	private XMLGregorianCalendar getXMLGregorianCalendar(DateTime datetime) {
		XMLGregorianCalendar timestamp = null;
		try {
			timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			timestamp.setYear(datetime.getYear());
			timestamp.setMonth(datetime.getMonthOfYear());
			timestamp.setDay(datetime.getDayOfMonth());
			timestamp.setHour(datetime.getHourOfDay());
			timestamp.setMinute(datetime.getMinuteOfHour());
			timestamp.setSecond(datetime.getSecondOfMinute());
			timestamp.setMillisecond(datetime.getMillisOfSecond());
		} catch (DatatypeConfigurationException e) {
			System.err.println("Error in XML Gregorian Calendar -> " + 
								e.getMessage());
		}
		return timestamp;
	}

	/**
	 * Converts XMLGregorianCalendar to DateTime
	 * @param timestamp					the datetime to be converted.
	 * @return DateTime					the dto's timestamp (ws rep)
	 */	
	private DateTime getDateTime(XMLGregorianCalendar timestamp) {
		return new DateTime(
				timestamp.getYear(),
				timestamp.getMonth(),
				timestamp.getDay(),
				timestamp.getHour(),
				timestamp.getMinute(),
				timestamp.getSecond(),
				timestamp.getMillisecond());
	}
	
	/**
	 * Returns the current timestamp on the data base
	 * @return						the timestamp
	 * @throws AnacomException		generic error exception
	 */
	private DateTime getCurrentDBTimestamp() throws AnacomException{
		GetTimestampService service = new GetTimestampService();
		service.execute();
		return service.getTimestampDTO().getTimestamp();
	}

	/**
	 * Sets the current timestamp on the data base
	 * @param						the timestamp
	 * @throws AnacomException		generic error exception
	 */	
	private void setCurrentDBTimestamp(DateTime datetime) throws AnacomException{
		SetTimestampService service = 
				new SetTimestampService(new TimestampDTO(datetime));
		service.execute();
	}
	
	@Override
	public VoidResponseType createNewOperator(OperatorDetailedType parameters)
			throws 	OperatorNameAlreadyExistsException, 
					OperatorPrefixAlreadyExistsException, 
					OperatorPrefixNotValidException,
					OperatorNameNotValidException,
					CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("createNewOperator", info);
		}
		
		OperatorDetailedDTO dto = new OperatorDetailedDTO(
				parameters.getName(), 
				parameters.getPrefix(), 
				parameters.getSmsCost().intValue(),
				parameters.getVoiceCost().intValue(),
				parameters.getVideoCost().intValue(),
				parameters.getTax().intValue(),
				parameters.getBonus().intValue());
		try {
			CreateNewOperatorService service = new CreateNewOperatorService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.operator.OperatorNameAlreadyExists e) {
			OperatorNameAlreadyExistsType info = new OperatorNameAlreadyExistsType();
			info.setName(e.getName());
			info.setPrefix(e.getPrefix());
			info.setSmsCost(new BigInteger("" + e.getSMSCost()));
			info.setVoiceCost(new BigInteger("" + e.getVoiceCost()));
			info.setVideoCost(new BigInteger("" + e.getVideoCost()));
			info.setTax(new BigInteger("" + e.getTax()));
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new OperatorNameAlreadyExistsException("OperatorNameAlreadyExistsException", info);
		} catch(anacom.shared.exceptions.operator.OperatorPrefixAlreadyExists e) {
			OperatorPrefixAlreadyExistsType info = new OperatorPrefixAlreadyExistsType();
			info.setName(e.getName());
			info.setPrefix(e.getPrefix());
			info.setSmsCost(new BigInteger("" + e.getSMSCost()));
			info.setVoiceCost(new BigInteger("" + e.getVoiceCost()));
			info.setVideoCost(new BigInteger("" + e.getVideoCost()));
			info.setTax(new BigInteger("" + e.getTax()));
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new OperatorPrefixAlreadyExistsException("OperatorPrefixAlreadyExistsException", info);
		} catch (anacom.shared.exceptions.operator.OperatorPrefixNotValid e) {
			OperatorPrefixNotValidType info = new OperatorPrefixNotValidType();
			info.setPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new OperatorPrefixNotValidException("OperatorPrefixNotValidException", info);
		} catch (anacom.shared.exceptions.operator.OperatorNameNotValid e) {
			OperatorNameNotValidType info = new OperatorNameNotValidType();
			info.setName(e.getName());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new OperatorNameNotValidException("OperatorNameNotValidException", info);
		}
	}

	@Override
	public VoidResponseType receiveSMS(ReceiveSMSType parameters)
			throws 	UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("receive sms", info);
		}
		
		SMSDTO dto = new SMSDTO(
				parameters.getMessage(), 
				parameters.getSource(),
				parameters.getDestination(),
				parameters.getCost().intValue());
		
		try {
			ReceiveSMSService service = new ReceiveSMSService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(this.getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("OperatorPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		}
	}

	@Override
	public PhoneType getPhoneBalance(PhoneNumberType parameters)
			throws 	UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("get phone balance", info);
		}
		 
		PhoneNumberDTO dto = new PhoneNumberDTO(parameters.getNumber());
		
		try {
			GetPhoneBalanceService service = new GetPhoneBalanceService(dto);
			service.execute();
			PhoneDTO localDTO = service.getPhoneDTO();
			PhoneType remoteDTO = new PhoneType();
			remoteDTO.setBalance(new BigInteger("" + localDTO.getBalance()));
			remoteDTO.setNumber(localDTO.getNumber());
			remoteDTO.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			return remoteDTO;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		}
	}

	@Override
	public VoidResponseType registerPhone(RegisterPhoneType parameters)
			throws 	InvalidPhoneTypeException,
					UnrecognisedPrefixException,
					PhoneAlreadyExistsException,
					PhoneNumberNotValidException,
					IncompatiblePrefixException, 
					CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("register phone", info);
		}
		
		RegisterPhoneDTO dto = new RegisterPhoneDTO(parameters.getType(),
													parameters.getNumber(),
													parameters.getOpPrefix());
		try {
			RegisterPhoneService service = new RegisterPhoneService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(this.getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.phone.InvalidPhoneType e) {
			InvalidPhoneTypeType info = new InvalidPhoneTypeType();
			info.setNumber(e.getNumber());
			info.setType(e.getType());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidPhoneTypeException("InvalidPhoneTypeException", info);
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneAlreadyExists e) {
			PhoneAlreadyExistsType info = new PhoneAlreadyExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneAlreadyExistsException("PhoneAlreadyExistsException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNumberNotValid e) {
			PhoneNumberNotValidType info = new PhoneNumberNotValidType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNumberNotValidException("PhoneNumberNotValidException", info);
		} catch(anacom.shared.exceptions.IncompatiblePrefix e) {
			IncompatiblePrefixType info = new IncompatiblePrefixType();
			info.setOperatorprefix(e.getOperatorPrefix());
			info.setPhoneprefix(e.getPhonePrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new IncompatiblePrefixException("IncompatiblePrefixException", info);
		}
		
	}
	
	@Override
	public VoidResponseType cancelPhoneRegistry(PhoneNumberType parameters)
			throws 	UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("cancel phone regitry", info);
		}
		
		PhoneNumberDTO dto = new PhoneNumberDTO(parameters.getNumber());
		
		try {
			CancelPhoneRegistryService service = new CancelPhoneRegistryService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		}
	}

	@Override
	public ReceiveSMSType sendSMS(SendSMSType parameters)
			throws 	NotPositiveBalanceException, 
					UnrecognisedPrefixException,
					PhoneNotExistsException, 
					InvalidStateSendSMSException, 
					InvalidAmountException, 
					CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("send sms", info);
		}
		
		SendSMSDTO dto = new SendSMSDTO(
				parameters.getMessage(), 
				parameters.getSource(),
				parameters.getDestination());
		
		try {
			SendSMSService service = new SendSMSService(dto);
			service.execute();
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			SMSDTO result = service.getSMSDTO();
			ReceiveSMSType info = new ReceiveSMSType();
			info.setCost(new BigInteger("" + result.getCost()));
			info.setDestination(result.getDestination());
			info.setMessage(result.getMessage());
			info.setSource(result.getSource());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			return info;
		} catch(anacom.shared.exceptions.phone.NotPositiveBalance e) {
			NotPositiveBalanceType info = new NotPositiveBalanceType();
			info.setBalance(new BigInteger("" + e.getBalance()));
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new NotPositiveBalanceException("NotPositiveBalanceException", info);
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		} catch(anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS e) {
			InvalidStateSendSMSType info = new InvalidStateSendSMSType();
			info.setState(e.getState());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidStateSendSMSException("InvalidStateSendSMSException", info);
		} catch(anacom.shared.exceptions.phone.InvalidAmount e) {
			InvalidAmountType info = new InvalidAmountType();
			info.setNumber(e.getNumber());
			info.setAmount(new BigInteger("" + e.getAmount()));
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidAmountException("InvalidAmountException", info);
		}
	}

	@Override
	public VoidResponseType increasePhoneBalance(IncreasePhoneBalanceType parameters)
			throws 	UnrecognisedPrefixException, 
					PhoneNotExistsException,
					BalanceLimitExceededException, 
					InvalidAmountException, 
					CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("createNewOperator", info);
		}
		
		IncreasePhoneBalanceDTO dto = new IncreasePhoneBalanceDTO(parameters.getNumber(), parameters.getAmount().intValue());
		
		try {
			IncreasePhoneBalanceService service = new IncreasePhoneBalanceService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		} catch(anacom.shared.exceptions.phone.BalanceLimitExceeded e) {
			BalanceLimitExceededType info = new BalanceLimitExceededType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new BalanceLimitExceededException("BalanceLimitExceededException", info);
		} catch(anacom.shared.exceptions.phone.InvalidAmount e) {
			InvalidAmountType info = new InvalidAmountType();
			info.setNumber(e.getNumber());
			info.setAmount(new BigInteger("" + e.getAmount()));
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidAmountException("InvalidAmountException" , info);
		}
	}

	@Override
	public PhoneListType listOperatorPhones(OperatorPrefixType parameters)
			throws 	UnrecognisedPrefixException, 
					CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("createNewOperator", info);
		}
		
		OperatorPrefixDTO dto = new OperatorPrefixDTO(parameters.getPrefix());
		
		try {
			ListOperatorPhonesService service = new ListOperatorPhonesService(dto);
			service.execute();
			List<PhoneDTO> localList = service.getOperatorPhones().getPhoneList();
			PhoneListType remoteList = new PhoneListType();
			DateTime currentTimestamp = this.getCurrentDBTimestamp();
			remoteList.setTimestamp(this.getXMLGregorianCalendar(currentTimestamp));
			for(PhoneDTO phone : localList) {
				PhoneType tmp = new PhoneType();
				tmp.setBalance(new BigInteger("" + phone.getBalance()));
				tmp.setNumber(phone.getNumber());
				tmp.setTimestamp(this.getXMLGregorianCalendar(currentTimestamp));
				remoteList.getPhoneDTOList().add(tmp);
			}
			return remoteList;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		}
	}
	

	@Override
	public VoidResponseType setPhoneState(PhoneStateType parameters)
			throws UnrecognisedPrefixException, PhoneNotExistsException,
			InvalidStateException, CantChangeStateException, CommunicationErrorException {

		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("Set Phone State", info);
		}
		 
		PhoneStateDTO dto = new PhoneStateDTO(parameters.getNumber(), parameters.getState());
		
		try {
			SetPhoneStateService service = new SetPhoneStateService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		} catch (anacom.shared.exceptions.phone.invalidState.InvalidState e) {
			InvalidStateType info = new InvalidStateType();
			info.setNumber(e.getNumber());
			info.setState(e.getState());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidStateException("InvalidPhoneStateException", info);
		} catch (anacom.shared.exceptions.phone.CantChangeState e) {
			CantChangeStateType info = new CantChangeStateType();
			info.setNumber(e.getNumber());
			info.setCurrentState(e.getCurrentState());
			info.setInvalidState(e.getInvalidState());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CantChangeStateException("CantChangeStateException", info);
		}
		
	}

	/**
	 * Get the phone state
	 * @param paremeters			the phone number
	 * @return PhoneStateType		the state of the phone
	 * @throws UnrecognisedPrefixException 	if the prefix is not recognized
	 * @throws PhoneNotExistsException		if the phone does not exist
	 * @throws CommunicationErrorException	if the parameters are null.
	 */
	@Override
	public PhoneStateType getPhoneState(PhoneNumberType parameters)
			throws UnrecognisedPrefixException, PhoneNotExistsException,
			CommunicationErrorException {

		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("get phone State", info);
		}
		
		PhoneNumberDTO dto = new PhoneNumberDTO(parameters.getNumber());
		try {
			GetPhoneStateService service = new GetPhoneStateService(dto);
			service.execute();
			PhoneStateDTO state = service.getPhoneStateDTO();
			PhoneStateType answer = new PhoneStateType();
			answer.setNumber(state.getNumber());
			answer.setState(state.getState());
			answer.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			return answer;
			
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		}
	}
	
	/**
	 * Get phone SMS received messages
	 * @param parameters					the phone number we are asking for received sms
	 * @return ReceivedSMSListType			the list of received SMSs
	 * @throws UnrecognisedPrefixException	if the prefix is not recognised
	 * @throws PhoneNotExistsException		if the phone does not exist
	 * @throws CommunicationErrorException	if the parameters are null
	 */
	@Override
	public  ReceivedSMSListType getPhoneSMSReceivedMessages(PhoneNumberType parameters) 
			throws UnrecognisedPrefixException,
			PhoneNotExistsException, CommunicationErrorException {

		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("Get Received Messages", info);
		}
		PhoneNumberDTO dto = new PhoneNumberDTO(parameters.getNumber());
		try {
			GetPhoneSMSReceivedMessagesService service = new GetPhoneSMSReceivedMessagesService(dto);
			service.execute();
			DateTime currentTimestamp = this.getCurrentDBTimestamp();
			ReceivedSMSListType answer = new ReceivedSMSListType();
			answer.setNumber(service.getDTOSMSList().getNumber());
			answer.setTimestamp(this.getXMLGregorianCalendar(currentTimestamp));
			List<ReceivedSMSDTO> localList = service.getDTOSMSList().getSMSList();
			
			for(ReceivedSMSDTO sms : localList) {
				ReceivedSMSType tmp = new ReceivedSMSType();
				tmp.setSource(sms.getSource());
				tmp.setMessage(sms.getMessage());
				tmp.setCost(new BigInteger("" + sms.getCost()));
				tmp.setTimestamp(this.getXMLGregorianCalendar(currentTimestamp));
				answer.getSMSDTOList().add(tmp);
			}
			return answer;
			
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		}
	}

	/**
	 * Get the Last communication of a specific Phone Number
	 * @param number	the number which we want to know it last communication
	 * @return the last communication performed by a specific Number
	 * @throws UnrecognisedPrefix	if the prefix isn't recognised
	 * @throws PhoneNotExists		if a Phone with the given number doesn't exist
	 * @throws NullMadeCommunicationException if the last made communication is null 
	 * @throws CommunicationError	only occurs when using remote services
	 */
	@Override
	public LastMadeCommunicationType getLastMadeCommunication(PhoneNumberType parameters) 
			throws 	UnrecognisedPrefixException,	
					PhoneNotExistsException, 
					CommunicationErrorException, 
					NoMadeCommunicationException {

		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("Get Last Made Communication ", info);
		}
		PhoneNumberDTO localDTO = new PhoneNumberDTO(parameters.getNumber());
		try {
			GetLastMadeCommunicationService service = new GetLastMadeCommunicationService(localDTO);
			service.execute();
			LastMadeCommunicationType response = new LastMadeCommunicationType();
			response.setDestination(service.getLastCommunication().getDestination());
			response.setCommunicationType(service.getLastCommunication().getCommunicationType());
			response.setCost(new BigInteger("" + service.getLastCommunication().getCost()));
			response.setTotal(new BigInteger("" + service.getLastCommunication().getSize()));
			response.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			return response;
			
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		} catch(anacom.shared.exceptions.phone.NoMadeCommunication e) {
			NoMadeCommunicationType info = new NoMadeCommunicationType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new NoMadeCommunicationException("NoMadeCommunicationException", info);
		}
	}	

	@Override
	public VoidResponseType makeCall(MakeCallType parameters)
			throws 	UnrecognisedPrefixException,
					PhoneNotExistsException,
					PhoneNumberNotValidException,
					NotPositiveBalanceException,
					InvalidStateMakeVoiceException,
					InvalidStateMakeVideoException,
					CantMakeVoiceCallException,
					CantMakeVideoCallException,
					InvalidCallTypeException,
					CommunicationErrorException {

		if (parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("Make Call", info);
		}
		
		CallDTO dto = new CallDTO(
				parameters.getSource(),
				parameters.getDestination(),
				parameters.getStartTime().longValue(),
				parameters.getType());
		
		try {
			MakeCallService service = new MakeCallService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix up) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(up.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists pne) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(pne.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNumberNotValid pnnv) {
			PhoneNumberNotValidType info = new PhoneNumberNotValidType();
			info.setNumber(pnnv.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNumberNotValidException("PhoneNumberNotValidException", info);
		} catch(anacom.shared.exceptions.phone.NotPositiveBalance npb) {
			NotPositiveBalanceType info = new NotPositiveBalanceType();
			info.setNumber(npb.getNumber());
			info.setBalance(new BigInteger("" + npb.getBalance()));
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new NotPositiveBalanceException("NotPositiveBalanceException", info);
		} catch(anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice ismv) {
			InvalidStateMakeVoiceType info = new InvalidStateMakeVoiceType();
			info.setNumber(ismv.getNumber());
			info.setState(ismv.getState());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidStateMakeVoiceException("InvalidStateMakeVoiceException", info);
		} catch(anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo ismv) {
			InvalidStateMakeVideoType info = new InvalidStateMakeVideoType();
			info.setNumber(ismv.getNumber());
			info.setState(ismv.getState());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidStateMakeVideoException("InvalidStateMakeVideoException", info);
		} catch(anacom.shared.exceptions.phone.CantMakeVoiceCall cmvc) {
			CantMakeVoiceCallType info = new CantMakeVoiceCallType();
			info.setNumber(cmvc.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CantMakeVoiceCallException("CantMakeVoiceCallException", info);
		} catch(anacom.shared.exceptions.phone.CantMakeVideoCall cmvc) {
			CantMakeVideoCallType info = new CantMakeVideoCallType();
			info.setNumber(cmvc.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CantMakeVideoCallException("CantMakeVideoCallException", info);
		} catch(anacom.shared.exceptions.communication.InvalidCallType ict) {
			InvalidCallTypeType info = new InvalidCallTypeType();
			info.setOtherParty(ict.getOtherParty());
			info.setType(ict.getType());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidCallTypeException("InvalidCallTypeException", info);
		}
	}
	
	@Override
	public VoidResponseType receiveCall(MakeCallType parameters)
			throws 	UnrecognisedPrefixException,
					PhoneNotExistsException,
					PhoneNumberNotValidException,
					InvalidStateReceiveVoiceException,
					InvalidStateReceiveVideoException,
					CantReceiveVoiceCallException,
					CantReceiveVideoCallException,
					InvalidCallTypeException,
					CommunicationErrorException {

		if (parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("Receive Call", info);
		}
		
		CallDTO dto = new CallDTO(
				parameters.getSource(),
				parameters.getDestination(),
				parameters.getStartTime().longValue(),
				parameters.getType());
		
		try {
			ReceiveCallService service = new ReceiveCallService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix up) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(up.getPrefix());
			info.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists pne) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(pne.getNumber());
			info.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNumberNotValid pnnv) {
			PhoneNumberNotValidType info = new PhoneNumberNotValidType();
			info.setNumber(pnnv.getNumber());
			info.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			throw new PhoneNumberNotValidException("PhoneNumberNotValidException", info);
		} catch(anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice isrv) {
			InvalidStateReceiveVoiceType info = new InvalidStateReceiveVoiceType();
			info.setNumber(isrv.getNumber());
			info.setState(isrv.getNumber());
			info.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			throw new InvalidStateReceiveVoiceException("InvalidStateReceiveVoiceException", info);
		} catch(anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo isrv) {
			InvalidStateReceiveVideoType info = new InvalidStateReceiveVideoType();
			info.setNumber(isrv.getNumber());
			info.setState(isrv.getState());
			info.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			throw new InvalidStateReceiveVideoException("InvalidStateReceiveVideoException", info);
		} catch(anacom.shared.exceptions.phone.CantReceiveVoiceCall crvc) {
			CantReceiveVoiceCallType info = new CantReceiveVoiceCallType();
			info.setNumber(crvc.getNumber());
			info.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			throw new CantReceiveVoiceCallException("CantReceiveVoiceCallException", info);
		} catch(anacom.shared.exceptions.phone.CantReceiveVideoCall crvc) {
			CantReceiveVideoCallType info = new CantReceiveVideoCallType();
			info.setNumber(crvc.getNumber());
			info.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			throw new CantReceiveVideoCallException("CantReceiveVideoCallException", info);
		} catch(anacom.shared.exceptions.communication.InvalidCallType ict) {
			InvalidCallTypeType info = new InvalidCallTypeType();
			info.setOtherParty(ict.getOtherParty());
			info.setType(ict.getType());
			info.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			throw new InvalidCallTypeException("InvalidCallTypeException", info);
		}
	}
	
	@Override
	public FinishCallOnDestinationType finishCall(FinishCallType parameters)
			throws UnrecognisedPrefixException,
			InvalidStateFinishMakingCallException, PhoneNotExistsException,
			DurationNotValidException, CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("Finish Call", info);
		}
		
		FinishCallDTO dto = new FinishCallDTO( 
				parameters.getSource(),
				parameters.getEndTime().longValue());
		
		try {
			FinishCallOnSourceService service = new FinishCallOnSourceService(dto);
			service.execute();
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			
			FinishCallOnDestinationDTO destinationDTO = service.getFinishCallOnDestinationDTO();
			FinishCallOnDestinationType destinationType = new FinishCallOnDestinationType();
			destinationType.setDestination(destinationDTO.getDestination());
			destinationType.setDuration(new BigInteger("" + destinationDTO.getDuration()));
			destinationType.setCost(new BigInteger("" + destinationDTO.getCost()));
			destinationType.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			
			return destinationType;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		} catch(anacom.shared.exceptions.communication.DurationNotValid e) {
			DurationNotValidType info = new DurationNotValidType();
			info.setOtherParty(e.getOtherParty());
			info.setDuration(new BigInteger("" + e.getDuration()));
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new DurationNotValidException("DurationNotValidException", info);
		} catch(anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall e) {
			InvalidStateFinishMakingCallType info = new InvalidStateFinishMakingCallType();
			info.setNumber(e.getNumber());
			info.setState(e.getState());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidStateFinishMakingCallException("InvalidStateFinishMakingCallException", info);
		}
	}

	@Override
	public VoidResponseType finishCallOnDestination(FinishCallOnDestinationType parameters)
			throws UnrecognisedPrefixException,
			InvalidStateFinishReceivingCallException, PhoneNotExistsException,
			DurationNotValidException, CommunicationErrorException {
		
		if(parameters == null) {
			CommunicationErrorType info = new CommunicationErrorType();
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new CommunicationErrorException("Finish Call On Destination", info);
		}
		
		FinishCallOnDestinationDTO dto = new FinishCallOnDestinationDTO( 
				parameters.getDestination(),
				parameters.getDuration().intValue(),
				parameters.getCost().intValue());
		
		try {
			FinishCallOnDestinationService service = new FinishCallOnDestinationService(dto);
			service.execute();
			VoidResponseType output = new VoidResponseType();
			output.setTimestamp(getXMLGregorianCalendar(getCurrentDBTimestamp()));
			setCurrentDBTimestamp(getDateTime(parameters.getTimestamp()));
			return output;
		} catch(anacom.shared.exceptions.operator.UnrecognisedPrefix e) {
			UnrecognisedPrefixType info = new UnrecognisedPrefixType();
			info.setOperatorPrefix(e.getPrefix());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new UnrecognisedPrefixException("UnrecognisedPrefixException", info);
		} catch(anacom.shared.exceptions.phone.PhoneNotExists e) {
			PhoneNotExistsType info = new PhoneNotExistsType();
			info.setNumber(e.getNumber());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new PhoneNotExistsException("PhoneNotExistsException", info);
		} catch(anacom.shared.exceptions.communication.DurationNotValid e) {
			DurationNotValidType info = new DurationNotValidType();
			info.setOtherParty(e.getOtherParty());
			info.setDuration(new BigInteger("" + e.getDuration()));
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new DurationNotValidException("DurationNotValidException", info);
		} catch(anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall e) {
			InvalidStateFinishReceivingCallType info = new InvalidStateFinishReceivingCallType();
			info.setNumber(e.getNumber());
			info.setState(e.getState());
			info.setTimestamp(this.getXMLGregorianCalendar(this.getCurrentDBTimestamp()));
			throw new InvalidStateFinishReceivingCallException("InvalidStateFinishReceivingCallException", info);
		}
	}
	
	/**
	 * 
	 * @throws RuntimeException
	 */
	public void revogateCertificate() 
			throws CommunicationErrorException {
		SecurityInfo security = SecurityInfo.getInstance();
		
		try {
			
			System.out.println("Generating new pair of keys");
			String[] keys = CryptoManager.getInstance().generateKeys();
			
			
			System.out.println("Creating a new service");
			CacertApplicationServerPortType cacertService = 
				new Cacert().getCacertApplicationServicePort();
			
			Binding binding = ((BindingProvider) cacertService).getBinding();
			@SuppressWarnings("rawtypes")
			List<Handler> handlers = binding.getHandlerChain();
				
			handlers.add(new SecurityHandler());
			binding.setHandlerChain(handlers);
			
		
			BlockCertificateType blockCertificate = new BlockCertificateType();
			
			blockCertificate.setEntityName(
										security.getCertificate().getSubject());
			blockCertificate.setOldSerial(
										 security.getCertificate().getSerial());
			
			blockCertificate.setOldPublicKey(security.getCertificateAPublicKey());
		
			blockCertificate.setNewPublicKey(keys[0]);
			
			
			CertificateType newCertificate = 
					cacertService.blockCertificate(blockCertificate);
			
			security.setCertificate(newCertificate);
			security.setThisEntityPrivateKey(keys[1]);
			security.setThisEntityPublicKey(keys[0]);
			
			System.out.println("ok!");

		} catch (CertificateRemoteException e) {
			System.out.println("ERROR");
			throw new CommunicationErrorException();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("ERROR");
			throw new CommunicationErrorException();
		}		
	}
}
