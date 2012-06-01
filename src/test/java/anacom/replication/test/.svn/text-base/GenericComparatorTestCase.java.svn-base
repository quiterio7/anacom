package anacom.replication.test;

import java.math.BigInteger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import anacom.presentationserver.server.replication.GenericComparator;
import anacom.shared.stubs.client.BalanceLimitExceededException;
import anacom.shared.stubs.client.BalanceLimitExceededType;
import anacom.shared.stubs.client.BonusValueNotValidException;
import anacom.shared.stubs.client.BonusValueNotValidType;
import anacom.shared.stubs.client.CantChangeStateException;
import anacom.shared.stubs.client.CantChangeStateType;
import anacom.shared.stubs.client.CantMakeVideoCallException;
import anacom.shared.stubs.client.CantMakeVideoCallType;
import anacom.shared.stubs.client.CantMakeVoiceCallException;
import anacom.shared.stubs.client.CantMakeVoiceCallType;
import anacom.shared.stubs.client.CantReceiveVideoCallException;
import anacom.shared.stubs.client.CantReceiveVideoCallType;
import anacom.shared.stubs.client.CantReceiveVoiceCallException;
import anacom.shared.stubs.client.CantReceiveVoiceCallType;
import anacom.shared.stubs.client.DurationNotValidException;
import anacom.shared.stubs.client.DurationNotValidType;
import anacom.shared.stubs.client.FinishCallOnDestinationType;
import anacom.shared.stubs.client.IncompatiblePrefixException;
import anacom.shared.stubs.client.IncompatiblePrefixType;
import anacom.shared.stubs.client.InvalidAmountException;
import anacom.shared.stubs.client.InvalidAmountType;
import anacom.shared.stubs.client.InvalidCallTypeException;
import anacom.shared.stubs.client.InvalidCallTypeType;
import anacom.shared.stubs.client.InvalidPhoneTypeException;
import anacom.shared.stubs.client.InvalidPhoneTypeType;
import anacom.shared.stubs.client.InvalidStateException;
import anacom.shared.stubs.client.InvalidStateFinishMakingCallException;
import anacom.shared.stubs.client.InvalidStateFinishMakingCallType;
import anacom.shared.stubs.client.InvalidStateFinishReceivingCallException;
import anacom.shared.stubs.client.InvalidStateFinishReceivingCallType;
import anacom.shared.stubs.client.InvalidStateMakeVideoException;
import anacom.shared.stubs.client.InvalidStateMakeVideoType;
import anacom.shared.stubs.client.InvalidStateMakeVoiceException;
import anacom.shared.stubs.client.InvalidStateMakeVoiceType;
import anacom.shared.stubs.client.InvalidStateReceiveVideoException;
import anacom.shared.stubs.client.InvalidStateReceiveVideoType;
import anacom.shared.stubs.client.InvalidStateReceiveVoiceException;
import anacom.shared.stubs.client.InvalidStateReceiveVoiceType;
import anacom.shared.stubs.client.InvalidStateSendSMSException;
import anacom.shared.stubs.client.InvalidStateSendSMSType;
import anacom.shared.stubs.client.InvalidStateType;
import anacom.shared.stubs.client.LastMadeCommunicationType;
import anacom.shared.stubs.client.NoMadeCommunicationException;
import anacom.shared.stubs.client.NoMadeCommunicationType;
import anacom.shared.stubs.client.NotPositiveBalanceException;
import anacom.shared.stubs.client.NotPositiveBalanceType;
import anacom.shared.stubs.client.OperatorNameAlreadyExistsException;
import anacom.shared.stubs.client.OperatorNameAlreadyExistsType;
import anacom.shared.stubs.client.OperatorNameNotValidException;
import anacom.shared.stubs.client.OperatorNameNotValidType;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsException;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsType;
import anacom.shared.stubs.client.OperatorPrefixNotValidException;
import anacom.shared.stubs.client.OperatorPrefixNotValidType;
import anacom.shared.stubs.client.PhoneAlreadyExistsException;
import anacom.shared.stubs.client.PhoneAlreadyExistsType;
import anacom.shared.stubs.client.PhoneListType;
import anacom.shared.stubs.client.PhoneNotExistsException;
import anacom.shared.stubs.client.PhoneNotExistsType;
import anacom.shared.stubs.client.PhoneNumberNotValidException;
import anacom.shared.stubs.client.PhoneNumberNotValidType;
import anacom.shared.stubs.client.PhoneStateType;
import anacom.shared.stubs.client.PhoneType;
import anacom.shared.stubs.client.ReceiveSMSType;
import anacom.shared.stubs.client.ReceivedSMSListType;
import anacom.shared.stubs.client.ReceivedSMSType;
import anacom.shared.stubs.client.SMSMessageNotValidException;
import anacom.shared.stubs.client.SMSMessageNotValidType;
import anacom.shared.stubs.client.UnrecognisedPrefixException;
import anacom.shared.stubs.client.UnrecognisedPrefixType;
import anacom.shared.stubs.client.VoidResponseType;

public class GenericComparatorTestCase extends AnacomReplicationTestCase {

	/**
	 * The GenericComparator test instance.
	 */
	private final GenericComparator comparator = new GenericComparator();
	
	private final static int MILLISECOND1 = 1;
	private final static int MILLISECOND2 = 2;
	
	private final static XMLGregorianCalendar TIMESTAMP1;
	private final static XMLGregorianCalendar TIMESTAMP2;
	private final static String PHONE_NUMBER1 = "910000000";
	private final static String PHONE_NUMBER2 = "920000000";
	private final static String PHONE_NUMBER3 = "930000000";
	private final static int BALANCE1 = 10;
	private final static int BALANCE2 = 15;
	private final static String MESSAGE1 = "abc";
	private final static String MESSAGE2 = "abcd";
	private final static int COST1 = 5;
	private final static int COST2 = 6;
	private final static String STATE1 = "On";
	private final static String STATE2 = "Off";
	private final static String STATE3 = "Silence";
	private final static String COMMUNICATION_TYPE1 = "SMS";
	private final static String COMMUNICATION_TYPE2 = "Voice";
	private final static int COMMUNICATION_SIZE1 = 15;
	private final static int COMMUNICATION_SIZE2 = 16;
	private final static String INVALID_PHONETYPE1 = "4G";
	private final static String INVALID_PHONETYPE2 = "1G";
	private final static String INVALID_PHONETYPE_NULL = null;
	private final static String INVALID_NUMBER1 = "91000000";
	private final static String INVALID_NUMBER2 = "9100000000";
	private final static String INVALID_NUMBER_NULL = null;
	private final static String PHONE_PREFIX1 = "67";
	private final static String PHONE_PREFIX2 = "68";
	private final static int INVALID_AMOUNT1 = -1;
	private final static int INVALID_AMOUNT2 = -2;
	private final static int NEGATIVE_BALANCE1 = -1;
	private final static int NEGATIVE_BALANCE2 = -2;
	private final static String INVALID_MESSAGE1 = "";
	private final static String INVALID_MESSAGE2 = "a"; // just for comparison
	private final static String INVALID_MESSAGE_NULL = null;
	private final static String INVALID_STATE1 = "a";
	private final static String INVALID_STATE2 = "";
	private final static String INVALID_STATE_NULL = null;
	private final static String INVALID_CALL1 = "a";
	private final static String INVALID_CALL2 = "";
	private final static String INVALID_CALL_NULL = null;
	private final static int INVALID_DURATION1 = 0;
	private final static int INVALID_DURATION2 = -1;
	
	private final static String OPERATOR_NAME1 = "OP1";
	private final static String OPERATOR_NAME2 = "OP2";
	private final static String OPERATOR_PREFIX1 = "95";
	private final static String OPERATOR_PREFIX2 = "96";
	private final static int SMS_COST1 = 3;
	private final static int SMS_COST2 = 4;
	private final static int VOICE_COST1 = 5;
	private final static int VOICE_COST2 = 6;
	private final static int VIDEO_COST1 = 5;
	private final static int VIDEO_COST2 = 6;
	private final static int TAX1 = 20;
	private final static int TAX2 = 21;
	private final static int BONUS1 = 10;
	private final static int BONUS2 = 11;
	private final static String INVALID_PREFIX1 = "9";
	private final static String INVALID_PREFIX2 = "999";
	private final static String INVALID_PREFIX_NULL = null;
	private final static String INVALID_NAME1 = "";
	private final static String INVALID_NAME2 = "a"; // just for comparison
	private final static String INVALID_NAME_NULL = null;
	private final static int INVALID_BONUS1 = -1;
	private final static int INVALID_BONUS2 = -2;
	
	private static final void initializeTimestamp(
			XMLGregorianCalendar timestamp,
			int millisecond) {
		timestamp.setYear(YEAR);
		timestamp.setMonth(MONTH);
		timestamp.setDay(DAY);
		timestamp.setHour(HOUR);
		timestamp.setMinute(MINUTE);
		timestamp.setSecond(SECOND);
		timestamp.setMillisecond(millisecond);
	}
	
	/**
	 * All instances of this test will have the same timestamps.
	 */
	static {
		try {
			TIMESTAMP1 =
					DatatypeFactory.newInstance().newXMLGregorianCalendar();
			initializeTimestamp(TIMESTAMP1, MILLISECOND1);
			TIMESTAMP2 =
					DatatypeFactory.newInstance().newXMLGregorianCalendar();
			initializeTimestamp(TIMESTAMP2, MILLISECOND2);
		} catch (DatatypeConfigurationException dce) {
			throw new RuntimeException(dce); // KILL TEST
		}
	}
	
	/**
	 * Factory method used to create a Void response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @return
	 * 		the VoidResponseType object
	 */
	private VoidResponseType createVoidResponse(
			XMLGregorianCalendar timestamp) {
		VoidResponseType response = new VoidResponseType();
		response.setTimestamp(timestamp);
		return response;
	}
	
	/**
	 * Factory method used to create a PhoneType response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param number
	 * 		the response's Phone number
	 * @param balance
	 * 		the response's balance
	 * @return
	 * 		the PhoneType object
	 */
	private PhoneType createPhone(	XMLGregorianCalendar timestamp,
									String number,
									int balance) {
		PhoneType response = new PhoneType();
		response.setTimestamp(timestamp);
		response.setNumber(number);
		response.setBalance(new BigInteger("" + balance));
		return response;
	}
	
	/**
	 * Factory method used to create a PhoneListType response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @return
	 * 		the PhoneListType object
	 */
	private PhoneListType createPhoneList(XMLGregorianCalendar timestamp) {
		PhoneListType response = new PhoneListType();
		response.setTimestamp(timestamp);
		return response;
	}
	
	/**
	 * Factory method used to create a ReceiveSMSType response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param source
	 * 		the SMS' source
	 * @param destination
	 * 		the SMS' destination
	 * @param message
	 * 		the SMS' message
	 * @param cost
	 * 		the SMS' cost
	 * @return
	 * 		the ReceiveSMSType object
	 */
	private ReceiveSMSType createReceiveSMS(XMLGregorianCalendar timestamp,
											String source,
											String destination,
											String message,
											int cost) {
		ReceiveSMSType response = new ReceiveSMSType();
		response.setTimestamp(timestamp);
		response.setSource(source);
		response.setDestination(destination);
		response.setMessage(message);
		response.setCost(new BigInteger("" + cost));
		return response;
	}
	
	/**
	 * Factory method used to create a PhoneStateType response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param number
	 * 		the response's Phone number
	 * @param state
	 * 		the response's Phone state
	 * @return
	 * 		the PhoneStateType object
	 */
	private PhoneStateType createPhoneState(XMLGregorianCalendar timestamp,
											String number,
											String state) {
		PhoneStateType response = new PhoneStateType();
		response.setTimestamp(timestamp);
		response.setNumber(number);
		response.setState(state);
		return response;
	}
	
	/**
	 * Factory method used to create a ReceivedSMSType response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param source
	 * 		the SMS' source Phone number
	 * @param message
	 * 		the SMS message
	 * @param cost
	 * 		the SMS' cost
	 * @return
	 * 		the ReceivedSMSType object
	 */
	private ReceivedSMSType createReceivedSMS(	XMLGregorianCalendar timestamp,
												String source,
												String message,
												int cost) {
		ReceivedSMSType response = new ReceivedSMSType();
		response.setTimestamp(timestamp);
		response.setSource(source);
		response.setMessage(message);
		response.setCost(new BigInteger("" + cost));
		return response;
	}
	
	/**
	 * Factory method used to create a ReceivedSMSListType response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param number
	 * 		the response's Phone number
	 * @return
	 * 		the ReceivedSMSListType object
	 */
	private ReceivedSMSListType createReceivedSMSList(
			XMLGregorianCalendar timestamp,
			String number) {
		ReceivedSMSListType response = new ReceivedSMSListType();
		response.setTimestamp(timestamp);
		response.setNumber(number);
		return response;
	}
	
	/**
	 * Factory method used to create a LastMadeCommunicationType response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param communicationType
	 * 		the response's communication type
	 * @param destination
	 * 		the communication's destination
	 * @param size
	 * 		the communication's size
	 * @param cost
	 * 		the communication's cost
	 * @return
	 * 		the LastMadeCommunicationType object
	 */
	private LastMadeCommunicationType createLastMadeCommunication(
			XMLGregorianCalendar timestamp,
			String communicationType,
			String destination,
			int size,
			int cost) {
		LastMadeCommunicationType response = new LastMadeCommunicationType();
		response.setTimestamp(timestamp);
		response.setCommunicationType(communicationType);
		response.setDestination(destination);
		response.setTotal(new BigInteger("" + size));
		response.setCost(new BigInteger("" + cost));
		return response;
	}
	
	/**
	 * Factory method used to create a FinishCallOnDestinationType response.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param destination
	 * 		the call's destination
	 * @param duration
	 * 		the call's duration
	 * @param cost
	 * 		the call's cost
	 * @return
	 * 		the FinishCallOnDestinationType object
	 */
	private FinishCallOnDestinationType createFinishCallOnDestination(
			XMLGregorianCalendar timestamp,
			String destination,
			int duration,
			int cost) {
		FinishCallOnDestinationType response =
				new FinishCallOnDestinationType();
		response.setTimestamp(timestamp);
		response.setDestination(destination);
		response.setDuration(new BigInteger("" + duration));
		response.setCost(new BigInteger("" + cost));
		return response;
	}

	/**
	 * Factory method used to create an OperatorNameAlreadyExistsException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param name
	 * 		the operator's name
	 * @param prefix
	 * 		the operator's prefix
	 * @param smsCost
	 * 		the operator's SMS cost
	 * @param voiceCost
	 * 		the operator's voice call cost
	 * @param videoCost
	 * 		the operator's video call cost
	 * @param tax
	 * 		the operator's tax
	 * @param bonus
	 * 		the operator's balance bonus
	 * @return
	 * 		the OperatorNameAlreadyExistsException object
	 */
	private OperatorNameAlreadyExistsException
	createOperatorNameAlreadyExists(XMLGregorianCalendar timestamp,
									String name,
									String prefix,
									int smsCost,
									int voiceCost,
									int videoCost,
									int tax,
									int bonus) {
		OperatorNameAlreadyExistsType info =
				new OperatorNameAlreadyExistsType();
		info.setTimestamp(timestamp);
		info.setName(name);
		info.setPrefix(prefix);
		info.setSmsCost(new BigInteger("" + smsCost));
		info.setVoiceCost(new BigInteger("" + voiceCost));
		info.setVideoCost(new BigInteger("" + videoCost));
		info.setTax(new BigInteger("" + tax));
		info.setBonus(new BigInteger("" + bonus));
		return new OperatorNameAlreadyExistsException(
				"OperatorNameAlreadyExistsException",
				info);
	}
	
	/**
	 * Factory method used to create an OperatorPrefixAlreadyExistsExceptions.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param name
	 * 		the operator's name
	 * @param prefix
	 * 		the operator's prefix
	 * @param smsCost
	 * 		the operator's SMS cost
	 * @param voiceCost
	 * 		the operator's voice cost
	 * @param videoCost
	 * 		the operator's video cost
	 * @param tax
	 * 		the operator's tax
	 * @param bonus
	 * 		the operator's balance bonus
	 * @return
	 * 		the OperatorPrefixAlreadyExistsException object
	 */
	private OperatorPrefixAlreadyExistsException
	createOperatorPrefixAlreadyExists(	XMLGregorianCalendar timestamp,
										String name,
										String prefix,
										int smsCost,
										int voiceCost,
										int videoCost,
										int tax,
										int bonus) {
		OperatorPrefixAlreadyExistsType info =
				new OperatorPrefixAlreadyExistsType();
		info.setTimestamp(timestamp);
		info.setName(name);
		info.setPrefix(prefix);
		info.setSmsCost(new BigInteger("" + smsCost));
		info.setVoiceCost(new BigInteger("" + voiceCost));
		info.setVideoCost(new BigInteger("" + videoCost));
		info.setTax(new BigInteger("" + tax));
		info.setBonus(new BigInteger("" + bonus));
		return new OperatorPrefixAlreadyExistsException(
				"OperatorPrefixAlreadyExistsException",
				info);
	}
	
	/**
	 * Factory method used to create an OperatorPrefixNotValidException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param prefix
	 * 		the invalid prefix
	 * @return
	 * 		the OperatorPrefixNotValidException object
	 */
	private OperatorPrefixNotValidException createOperatorPrefixNotValid(
			XMLGregorianCalendar timestamp,
			String prefix) {
		OperatorPrefixNotValidType info = new OperatorPrefixNotValidType();
		info.setTimestamp(timestamp);
		info.setPrefix(prefix);
		return new OperatorPrefixNotValidException(
				"OperatorPrefixNotValidException",
				info);
	}
	
	/**
	 * Factory method used to create an OperatorNameNotValidException.
	 * @param timestamp
	 * 		the response's timestamp
	 * @param name
	 * 		the invalid operator name
	 * @return
	 * 		the OperatorNameNotValidException object
	 */
	private OperatorNameNotValidException createOperatorNameNotValid(
			XMLGregorianCalendar timestamp,
			String name) {
		OperatorNameNotValidType info = new OperatorNameNotValidType();
		info.setTimestamp(timestamp);
		info.setName(name);
		return new OperatorNameNotValidException(
				"OperatorNameNotValidException",
				info);
	}
	
	/**
	 * Factory method used to create a BonusValueNotValidException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param bonus
	 * 		the invalid bonus
	 * @return
	 * 		the BonusValueNotValidException object
	 */
	private BonusValueNotValidException createBonusValueNotValid(
			XMLGregorianCalendar timestamp,
			int bonus) {
		BonusValueNotValidType info = new BonusValueNotValidType();
		info.setTimestamp(timestamp);
		info.setBonus(new BigInteger("" + bonus));
		return new BonusValueNotValidException(	"BonusValueNotValidException",
												info);
	}
	
	/**
	 * Factory method used to create an InvalidPhoneTypeException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param type
	 * 		the invalid type
	 * @return
	 * 		the InvalidPhoneTypeException object
	 */
	private InvalidPhoneTypeException createInvalidPhoneType(
			XMLGregorianCalendar timestamp,
			String number,
			String type) {
		InvalidPhoneTypeType info = new InvalidPhoneTypeType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setType(type);
		return new InvalidPhoneTypeException("InvalidPhonTypeException", info);
	}
	
	/**
	 * Factory method used to create an UnrecognisedPrefixException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param prefix
	 * 		the unrecognized operator prefix
	 * @return
	 * 		the UnrecognisedPrefixException object
	 */
	private UnrecognisedPrefixException createUnrecognisedPrefix(
			XMLGregorianCalendar timestamp,
			String prefix) {
		UnrecognisedPrefixType info = new UnrecognisedPrefixType();
		info.setTimestamp(timestamp);
		info.setOperatorPrefix(prefix);
		return new UnrecognisedPrefixException(	"UnrecognisedPrefixException",
												info);
	}
	
	/**
	 * Factory method used to create a PhoneAlreadyExistsException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the existing Phone number
	 * @return
	 * 		the PhoneAlreadyExistsExceptions object
	 */
	private PhoneAlreadyExistsException createPhoneAlreadyExists(
			XMLGregorianCalendar timestamp,
			String number) {
		PhoneAlreadyExistsType info = new PhoneAlreadyExistsType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new PhoneAlreadyExistsException(	"PhoneAlreadyExistsException",
												info);
	}
	
	/**
	 * Factory method used to create a PhoneNumberNotValidException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the invalid number
	 * @return
	 * 		the PhoneNumberNotValidException object
	 */
	private PhoneNumberNotValidException createPhoneNumberNotValid(
			XMLGregorianCalendar timestamp,
			String number) {
		PhoneNumberNotValidType info = new PhoneNumberNotValidType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new PhoneNumberNotValidException("PhoneNumberNotValidException",
												info);
	}
	
	/**
	 * Factory method used to create an IncompatiblePrefixException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param operatorPrefix
	 * 		the operator's prefix
	 * @param phonePrefix
	 * 		the phone's prefix
	 * @return
	 * 		the IncompatiblePrefixException object
	 */
	private IncompatiblePrefixException createIncompatiblePrefix(
			XMLGregorianCalendar timestamp,
			String operatorPrefix,
			String phonePrefix) {
		IncompatiblePrefixType info = new IncompatiblePrefixType();
		info.setTimestamp(timestamp);
		info.setOperatorprefix(operatorPrefix);
		info.setPhoneprefix(phonePrefix);
		return new IncompatiblePrefixException(	"IncompatiblePrefixException",
												info);
	}
	
	/**
	 * Factory method used to create a PhoneNotExistsException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the non existing phone number
	 * @return
	 * 		the PhoneNotExistsException object
	 */
	private PhoneNotExistsException createPhoneNotExists(
			XMLGregorianCalendar timestamp,
			String number) {
		PhoneNotExistsType info = new PhoneNotExistsType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new PhoneNotExistsException(	"PhoneNotExistsExceptions",
											info);
	}
	
	/**
	 * Factory method used to create an InvalidAmountException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param amount
	 * 		the invalid amount
	 * @return
	 * 		the InvalidAmountException object
	 */
	private InvalidAmountException createInvalidAmount(
			XMLGregorianCalendar timestamp,
			String number,
			int amount) {
		InvalidAmountType info = new InvalidAmountType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setAmount(new BigInteger("" + amount));
		return new InvalidAmountException("InvalidAmountException", info);
	}

	/**
	 * Factory method used to create a BalanceLimitExceededException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @return
	 * 		the BalanceLimitExceededException object
	 */
	private BalanceLimitExceededException createBalanceLimitExceeded(
			XMLGregorianCalendar timestamp,
			String number) {
		BalanceLimitExceededType info = new BalanceLimitExceededType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new BalanceLimitExceededException(
				"BalanceLimitExceededException",
				info);
	}
	
	/**
	 * Factory method used to create a NotPositiveBalanceException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param balance
	 * 		the phone's balance
	 * @return
	 * 		the NotPositiveBalanceException object
	 */
	private NotPositiveBalanceException createNotPositiveBalance(
			XMLGregorianCalendar timestamp,
			String number,
			int balance) {
		NotPositiveBalanceType info = new NotPositiveBalanceType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setBalance(new BigInteger("" + balance));
		return new NotPositiveBalanceException(	"NotPositiveBalanceException",
												info);
	}
	
	/**
	 * Factory method used to create an InvalidStateSendSMSException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param state
	 * 		the invalid state
	 * @return
	 * 		the InvalidStateSendSMSException object
	 */
	private InvalidStateSendSMSException createInvalidStateSendSMS(
			XMLGregorianCalendar timestamp,
			String number,
			String state) {
		InvalidStateSendSMSType info = new InvalidStateSendSMSType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setState(state);
		return new InvalidStateSendSMSException("InvalidStateSendSMSException",
												info);
	}
	
	/**
	 * Factory method used to create a SMSMessageNotValidException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param otherParty
	 * 		the message's other party
	 * @param message
	 * 		the invalid message
	 * @return
	 * 		the SMSMessageNotValidException object
	 */
	private SMSMessageNotValidException createSMSMessageNotValid(
			XMLGregorianCalendar timestamp,
			String otherParty,
			String message) {
		SMSMessageNotValidType info = new SMSMessageNotValidType();
		info.setTimestamp(timestamp);
		info.setOtherParty(otherParty);
		info.setMessage(message);
		return new SMSMessageNotValidException(	"SMSMessageNotValidException",
												info);
	}
	
	/**
	 * Factory method used to create an InvalidStateException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param state
	 * 		the invalid state
	 * @return
	 * 		the InvalidStateException object
	 */
	private InvalidStateException createInvalidState(
			XMLGregorianCalendar timestamp,
			String number,
			String state) {
		InvalidStateType info = new InvalidStateType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setState(state);
		return new InvalidStateException("InvalidStateException", info);
	}
	
	/**
	 * Factory method used to create a CantChangeStateException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param currentState
	 * 		the phone's current state
	 * @param invalidState
	 * 		the phone's new state
	 * @return
	 * 		the CantChangeStateException object
	 */
	private CantChangeStateException createCantChangeState(
			XMLGregorianCalendar timestamp,
			String number,
			String currentState,
			String invalidState) {
		CantChangeStateType info = new CantChangeStateType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setCurrentState(currentState);
		info.setInvalidState(invalidState);
		return new CantChangeStateException("CantChangeStateException",
											info);
	}
	
	/**
	 * Factory method used to create a NoMadeCommunicationException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @return
	 * 		the NoMadeCommunicationException object
	 */
	private NoMadeCommunicationException createNoMadeCommunication(
			XMLGregorianCalendar timestamp,
			String number) {
		NoMadeCommunicationType info = new NoMadeCommunicationType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new NoMadeCommunicationException("NoMadeCommunicationException",
												info);
	}
	
	/**
	 * Factory method used to create an InvalidStateMakeVoiceException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param state
	 * 		the phone state
	 * @return
	 * 		the InvalidStatemakeVoiceException object
	 */
	private InvalidStateMakeVoiceException createInvalidStateMakeVoice(
			XMLGregorianCalendar timestamp,
			String number,
			String state) {
		InvalidStateMakeVoiceType info = new InvalidStateMakeVoiceType();
		info.setNumber(number);
		info.setState(state);
		info.setTimestamp(timestamp);
		return new InvalidStateMakeVoiceException(
				"InvalidStateMakeVoiceException",
				info);
	}

	/**
	 * Factory method used to create an InvalidStateMakeVideoException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param state
	 * 		the invalid state
	 * @return
	 * 		the InvalidStateMakeV
	 */
	private InvalidStateMakeVideoException createInvalidStateMakeVideo(
			XMLGregorianCalendar timestamp,
			String number,
			String state) {
		InvalidStateMakeVideoType info = new InvalidStateMakeVideoType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setState(state);
		return new InvalidStateMakeVideoException(
				"InvalidStateMakeVideoException",
				info);
	}
	
	/**
	 * Factory method used to create a CantMakeVoiceCallException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone's number
	 * @return
	 * 		the CantMakeVoiceCallException object
	 */
	private CantMakeVoiceCallException createCantMakeVoiceCall(
			XMLGregorianCalendar timestamp,
			String number) {
		CantMakeVoiceCallType info = new CantMakeVoiceCallType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new CantMakeVoiceCallException(	"CantMakeVoiceCallException",
												info);
	}
	
	/**
	 * Factory method used to create a CantMakeVideoCallException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @return
	 * 		the CantMakeVideoCallException object
	 */
	private CantMakeVideoCallException createCantMakeVideoCall(
			XMLGregorianCalendar timestamp,
			String number) {
		CantMakeVideoCallType info = new CantMakeVideoCallType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new CantMakeVideoCallException(	"CantMakeVideoCallException",
												info);
	}
	
	/**
	 * Factory method used to create an InvalidCallTypeException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param otherParty
	 * 		the call's other party
	 * @param call
	 * 		the invalid call type
	 * @return
	 * 		the InvalidCallTypeException object
	 */
	private InvalidCallTypeException createInvalidCallType(
			XMLGregorianCalendar timestamp,
			String otherParty,
			String call) {
		InvalidCallTypeType info = new InvalidCallTypeType();
		info.setTimestamp(timestamp);
		info.setOtherParty(otherParty);
		info.setType(call);
		return new InvalidCallTypeException("InvalidCallTypeException",
											info);
	}
	
	/**
	 * Factory method used to create an InvalidStateReceiveVoiceException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param state
	 * 		the invalid state
	 * @return
	 * 		the InvalidStateReceiveVoiceException object
	 */
	private InvalidStateReceiveVoiceException createInvalidStateReceiveVoice(
			XMLGregorianCalendar timestamp,
			String number,
			String state) {
		InvalidStateReceiveVoiceType info = new InvalidStateReceiveVoiceType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setState(state);
		return new InvalidStateReceiveVoiceException(
				"InvalidStateReceiveVoiceException",
				info);
	}
	
	/**
	 * Factory method used to create an InvalidStateReceiveVideoException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone number
	 * @param state
	 * 		the invalid state
	 * @return
	 * 		the InvalidStateReceiveVideoException object
	 */
	private InvalidStateReceiveVideoException createInvalidStateReceiveVideo(
			XMLGregorianCalendar timestamp,
			String number,
			String state) {
		InvalidStateReceiveVideoType info = new InvalidStateReceiveVideoType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setState(state);
		return new InvalidStateReceiveVideoException(
				"InvalidStateReceiveVideoException",
				info);
	}
	
	/**
	 * Factory method used to create a CantReceiveVoiceCallException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone's number
	 * @return
	 * 		the CantReceiveVoiceCallException object
	 */
	private CantReceiveVoiceCallException createCantReceiveVoiceCall(
			XMLGregorianCalendar timestamp,
			String number) {
		CantReceiveVoiceCallType info = new CantReceiveVoiceCallType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new CantReceiveVoiceCallException(
				"CantMakeVoiceCallException",
				info);
	}
	
	/**
	 * Factory method used to create a CantReceiveVideoCallException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone's number
	 * @return
	 * 		the CantReceiveVideoCallException object
	 */
	private CantReceiveVideoCallException createCantReceiveVideoCall(
			XMLGregorianCalendar timestamp,
			String number) {
		CantReceiveVideoCallType info = new CantReceiveVideoCallType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		return new CantReceiveVideoCallException(
				"CantMakeVoiceCallException",
				info);
	}
	
	/**
	 * Factory method used to create a DurationNotValidException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param otherParty
	 * 		the call's other party
	 * @param duration
	 * 		the call's invalid duration
	 * @return
	 * 		the DurationNotValidException object
	 */
	private DurationNotValidException createDurationNotValid(
			XMLGregorianCalendar timestamp,
			String otherParty,
			int duration) {
		DurationNotValidType info = new DurationNotValidType();
		info.setTimestamp(timestamp);
		info.setOtherParty(otherParty);
		info.setDuration(new BigInteger("" + duration));
		return new DurationNotValidException(	"DurationNotValidException",
												info);
	}
	
	/**
	 * Factory method used to create an InvalidStateFinishMakingCallException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone's number
	 * @param state
	 * 		the invalid state
	 * @return
	 * 		the InvalidStateFinishMakingCallException object
	 */
	private InvalidStateFinishMakingCallException
	createInvalidStateFinishMakingCall(
			XMLGregorianCalendar timestamp,
			String number,
			String state) {
		InvalidStateFinishMakingCallType info =
				new InvalidStateFinishMakingCallType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setState(state);
		return new InvalidStateFinishMakingCallException(
				"InvalidStateFinishMakingCallException",
				info);
	}
	
	/**
	 * Factory method used to create an
	 * InvalidStateFinishReceivingCallException.
	 * @param timestamp
	 * 		the exception's timestamp
	 * @param number
	 * 		the phone's number
	 * @param state
	 * 		the invalid state
	 * @return
	 * 		the InvalidStateFinishReceivingCallException object
	 */
	private InvalidStateFinishReceivingCallException
	createInvalidStateFinishReceivingCall(
			XMLGregorianCalendar timestamp,
			String number,
			String state) {
		InvalidStateFinishReceivingCallType info =
				new InvalidStateFinishReceivingCallType();
		info.setTimestamp(timestamp);
		info.setNumber(number);
		info.setState(state);
		return new InvalidStateFinishReceivingCallException(
				"InvalidStateFinishReceivingCallException",
				info);
	}
	
	/**
	 * Test 1 - Test comparing void responses.
	 */
	public void testVoidResponse() {
		// Arrange (equal responses)
		VoidResponseType response = createVoidResponse(TIMESTAMP1);
		VoidResponseType equalResponse = createVoidResponse(TIMESTAMP1);
		
		// Assert (equal responses)
		assertTrue("Equal VoidResponses returned 'false'",
				comparator.equals(response, equalResponse));
		
		// Arrange (different responses)
		VoidResponseType differentResponse = createVoidResponse(TIMESTAMP2);
		
		// Assert (different responses)
		assertFalse("Different VoidResponses returned 'true'",
				comparator.equals(response, differentResponse));
	}
	
	/**
	 * Test 2 - Test comparing phone responses.
	 */
	public void testPhone() {
		// Arrange (equal responses)
		PhoneType response = createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE1);
		PhoneType equalResponse =
				createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE1);
		
		// Assert (equal responses)
		assertTrue("Equal PhoneTypes returned 'false'",
				comparator.equals(response, equalResponse));
		
		// Arrange (different timestamps)
		PhoneType differentTimestamp =
				createPhone(TIMESTAMP2, PHONE_NUMBER1, BALANCE1);
		
		// Assert (different timestamps)
		assertFalse("PhoneTypes with different timestamps returned 'true'",
				comparator.equals(response, differentTimestamp));
		
		// Arrange (different phone numbers)
		PhoneType differentNumber =
				createPhone(TIMESTAMP1, PHONE_NUMBER2, BALANCE1);
		
		// Assert (different phone numbers)
		assertFalse("PhoneTypes with different phone numbers returned 'true'",
				comparator.equals(response, differentNumber));
		
		// Arrange (different balances)
		PhoneType differentBalance =
				createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE2);
		
		// Assert (different balances)
		assertFalse("PhoneTypes with different balances returned 'true'",
				comparator.equals(response, differentBalance));
	}
	
	/**
	 * Test 3 - Test comparing phone list responses.
	 */
	public void testPhoneList() {
		// Arrange (equal responses)
		PhoneListType response = createPhoneList(TIMESTAMP1);
		response.getPhoneDTOList().add(
				createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE1));
		PhoneListType equalResponse = createPhoneList(TIMESTAMP1);
		equalResponse.getPhoneDTOList().add(
				createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE1));
		
		// Assert (equal responses)
		assertTrue("Equal PhoneListTypes returned 'false'",
				comparator.equals(response, equalResponse));
		
		// Arrange (different timestamps)
		PhoneListType differentTimestamp = createPhoneList(TIMESTAMP2);
		differentTimestamp.getPhoneDTOList().add(
				createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE1));
		
		// Assert (different timestamps)
		assertFalse("PhoneListTypes with different timestamps returned 'true'",
				comparator.equals(response, differentTimestamp));
		
		// Arrange (different PhoneTypes)
		PhoneListType differentPhone = createPhoneList(TIMESTAMP1);
		differentPhone.getPhoneDTOList().add(
				createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE2));
		
		// Assert (different PhoneTypes)
		assertFalse("PhoneListTypes with different PhoneTypes returned 'true'",
				comparator.equals(response, differentPhone));
		
		// Arrange (different number of PhoneTypes)
		PhoneListType differentNumberOfPhones = createPhoneList(TIMESTAMP1);
		differentNumberOfPhones.getPhoneDTOList().add(
				createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE1));
		differentNumberOfPhones.getPhoneDTOList().add(
				createPhone(TIMESTAMP1, PHONE_NUMBER1, BALANCE1));
		
		// Assert (different number of PhoneTypes)
		assertFalse("PhoneListTypes with different number of PhoneTypes " +
				"returned 'true'",
				comparator.equals(response, differentNumberOfPhones));
	}
	
	/**
	 * Test 4 - Test comparing receive SMS responses.
	 */
	public void testReceiveSMS() {
		// Arrange (equal responses)
		ReceiveSMSType response = createReceiveSMS(
				TIMESTAMP1,
				PHONE_NUMBER1,
				PHONE_NUMBER2,
				MESSAGE1,
				COST1);
		ReceiveSMSType equalResponse = createReceiveSMS(
				TIMESTAMP1,
				PHONE_NUMBER1,
				PHONE_NUMBER2,
				MESSAGE1,
				COST1);
		
		// Assert (equal responses)
		assertTrue("Equal ReceiveSMSTypes returned 'false'",
				comparator.equals(response, equalResponse));
		
		// Arrange (different timestamps)
		ReceiveSMSType differentTimestamp = createReceiveSMS(
				TIMESTAMP2,
				PHONE_NUMBER1,
				PHONE_NUMBER2,
				MESSAGE1,
				COST1);
		
		// Assert (different timestamps)
		assertFalse("ReceiveSMSTypes with different timestamps returned 'true'",
				comparator.equals(response, differentTimestamp));
		
		// Arrange (different sources)
		ReceiveSMSType differentSource = createReceiveSMS(
				TIMESTAMP1,
				PHONE_NUMBER3,
				PHONE_NUMBER2,
				MESSAGE1,
				COST1);
		
		// Assert (different sources)
		assertFalse("ReceiveSMSTypes with different sources returned 'true'",
				comparator.equals(response, differentSource));
		
		// Arrange (different destinations)
		ReceiveSMSType differentDestination = createReceiveSMS(
				TIMESTAMP1,
				PHONE_NUMBER1,
				PHONE_NUMBER3,
				MESSAGE1,
				COST1);
		
		// Assert (different destinations)
		assertFalse("ReceiveSMSTypes with different destinations returned " +
				"'true'",
				comparator.equals(response, differentDestination));
		
		// Arrange (different messages)
		ReceiveSMSType differentMessage = createReceiveSMS(
				TIMESTAMP1,
				PHONE_NUMBER1,
				PHONE_NUMBER2,
				MESSAGE2,
				COST1);
		
		// Assert (different messages)
		assertFalse("ReceiveSMSTypes with different messages returned 'true'",
				comparator.equals(response, differentMessage));
		
		// Arrange (different costs)
		ReceiveSMSType differentCost = createReceiveSMS(
				TIMESTAMP1,
				PHONE_NUMBER1,
				PHONE_NUMBER2,
				MESSAGE1,
				COST2);
		
		// Assert (different costs)
		assertFalse("ReceiveSMSTypes with different costs returned 'true'",
				comparator.equals(response, differentCost));
	}
	
	/**
	 * Test 5 - Test comparing phone state responses.
	 */
	public void testPhoneState() {
		// Arrange (equal responses)
		PhoneStateType response =
				createPhoneState(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		PhoneStateType equalResponse =
				createPhoneState(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		
		// Assert (equal responses)
		assertTrue("Equal PhoneStateTypes returned 'false'",
				comparator.equals(response, equalResponse));
		
		// Arrange (different timestamps)
		PhoneStateType differentTimestamp =
				createPhoneState(TIMESTAMP2, PHONE_NUMBER1, STATE1);
		
		// Assert (different timestamps)
		assertFalse("PhoneStateTypes with different timestamps returned 'true'",
				comparator.equals(response, differentTimestamp));
		
		// Arrange (different phone numbers)
		PhoneStateType differentNumber =
				createPhoneState(TIMESTAMP1, PHONE_NUMBER2, STATE1);
		
		// Assert (different phone numbers)
		assertFalse("PhoneStateTypes with different numbers returned 'true'",
				comparator.equals(response, differentNumber));
		
		// Arrange (different states)
		PhoneStateType differentState =
				createPhoneState(TIMESTAMP1, PHONE_NUMBER1, STATE2);
		
		// Assert (different states)
		assertFalse("PhoneStateTypes with different states returned 'true'",
				comparator.equals(response, differentState));
	}
	
	/**
	 * Test 6 - Test comparing received SMS list responses.
	 */
	public void testReceivedSMSList() {
		// Arrange (equal responses)
		ReceivedSMSListType response =
				createReceivedSMSList(TIMESTAMP1, PHONE_NUMBER1);
		response.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER2, MESSAGE1, COST1));
		ReceivedSMSListType equalResponse =
				createReceivedSMSList(TIMESTAMP1, PHONE_NUMBER1);
		equalResponse.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER2, MESSAGE1, COST1));
		
		// Assert (equal responses)
		assertTrue("Equal ReceivedSMSTypes returned 'false'",
				comparator.equals(response, equalResponse));
		
		// Arrange (different timestamps)
		ReceivedSMSListType differentTimestamp =
				createReceivedSMSList(TIMESTAMP2, PHONE_NUMBER1);
		equalResponse.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER2, MESSAGE1, COST1));
		
		// Assert (different timestamps)
		assertFalse("ReceivedSMSListTypes with different timestamps " +
				"returned 'true'",
				comparator.equals(response, differentTimestamp));
		
		// Arrange (different numbers)
		ReceivedSMSListType differentNumber =
				createReceivedSMSList(TIMESTAMP1, PHONE_NUMBER3);
		equalResponse.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER2, MESSAGE1, COST1));
		
		// Assert (different numbers)
		assertFalse("ReceivedSMSListTypes with different numbers returned " +
				"'true'",
				comparator.equals(response, differentNumber));
		
		// Arrange (different ReceivedSMSTypes)
		ReceivedSMSListType differentSMSTimestamp =
				createReceivedSMSList(TIMESTAMP1, PHONE_NUMBER1);
		differentSMSTimestamp.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP2, PHONE_NUMBER2, MESSAGE1, COST1));
		ReceivedSMSListType differentSMSNumber =
				createReceivedSMSList(TIMESTAMP1, PHONE_NUMBER1);
		differentSMSNumber.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER3, MESSAGE1, COST1));
		ReceivedSMSListType differentSMSMessage =
				createReceivedSMSList(TIMESTAMP1, PHONE_NUMBER1);
		differentSMSMessage.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER2, MESSAGE2, COST1));
		ReceivedSMSListType differentSMSCost =
				createReceivedSMSList(TIMESTAMP1, PHONE_NUMBER1);
		differentSMSCost.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER2, MESSAGE1, COST2));
		
		// Assert (different ReceivedSMSTypes)
		assertFalse("ReceivedSMSListTypes with ReceivedSMSTypes with " +
				"different timestamps returned 'true'",
				comparator.equals(response, differentSMSTimestamp));
		assertFalse("ReceivedSMSListTypes with ReceivedSMSTypes with " +
				"different numbers returned 'true'",
				comparator.equals(response, differentSMSNumber));
		assertFalse("ReceivedSMSListTypes with ReceivedSMSTypes with " +
				"different messages returned 'true'",
				comparator.equals(response, differentSMSMessage));
		assertFalse("ReceivedSMSListTypes with ReceivedSMSTypes with " +
				"different costs returned 'true'",
				comparator.equals(response, differentSMSCost));
		
		// Arrange (different number of ReceivedSMSTypes)
		ReceivedSMSListType differentNumberOfSMS =
				createReceivedSMSList(TIMESTAMP1, PHONE_NUMBER1);
		differentNumberOfSMS.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER2, MESSAGE1, COST1));
		differentNumberOfSMS.getSMSDTOList().add(
				createReceivedSMS(TIMESTAMP1, PHONE_NUMBER2, MESSAGE1, COST1));
		
		// Assert (different number of PhoneTypes)
		assertFalse("ReceivedSMSListTypes with different number of " +
				"ReceivedSMSTypes returned 'true'",
				comparator.equals(response, differentNumberOfSMS));
	}
	
	/**
	 * Test 7 - Test comparing last made communication responses.
	 */
	public void testLastMadeCommunication() {
		// Arrange (equal responses)
		LastMadeCommunicationType response = createLastMadeCommunication(
				TIMESTAMP1,
				COMMUNICATION_TYPE1,
				PHONE_NUMBER1,
				COMMUNICATION_SIZE1,
				COST1);
		LastMadeCommunicationType equalResponse = createLastMadeCommunication(
				TIMESTAMP1,
				COMMUNICATION_TYPE1,
				PHONE_NUMBER1,
				COMMUNICATION_SIZE1,
				COST1);
		
		// Assert (equal responses)
		assertTrue("Equal LastMadeCommunicationTypes returned 'true'",
				comparator.equals(response, equalResponse));
		
		// Arrange (different timestamps)
		LastMadeCommunicationType differentTimestamp =
				createLastMadeCommunication(
						TIMESTAMP2,
						COMMUNICATION_TYPE1,
						PHONE_NUMBER1,
						COMMUNICATION_SIZE1,
						COST1);
		
		// Assert (different timestamps)
		assertFalse("LastMadeCommunicationTypes with different timestamps " +
				"returned 'true'",
				comparator.equals(response, differentTimestamp));
		
		// Arrange (different timestamps)
		LastMadeCommunicationType differentCommunicationType =
				createLastMadeCommunication(
						TIMESTAMP1,
						COMMUNICATION_TYPE2,
						PHONE_NUMBER1,
						COMMUNICATION_SIZE1,
						COST1);
		
		// Assert (different timestamps)
		assertFalse("LastMadeCommunicationTypes with different " +
				"communication types returned 'true'",
				comparator.equals(response, differentCommunicationType));
		
		// Arrange (different numbers)
		LastMadeCommunicationType differentNumber =
				createLastMadeCommunication(
						TIMESTAMP1,
						COMMUNICATION_TYPE1,
						PHONE_NUMBER2,
						COMMUNICATION_SIZE1,
						COST1);
		
		// Assert (different numbers)
		assertFalse("LastMadeCommunicationTypes with different numbers " +
				"returned 'true'",
				comparator.equals(response, differentNumber));
		
		// Arrange (different sizes)
		LastMadeCommunicationType differentSize = createLastMadeCommunication(
				TIMESTAMP1,
				COMMUNICATION_TYPE1,
				PHONE_NUMBER1,
				COMMUNICATION_SIZE2,
				COST1);
		
		// Assert (different sizes)
		assertFalse("LastMadeCommunicationTypes with different sizes " +
				"returned 'true'",
				comparator.equals(response, differentSize));
		
		// Arrange (different costs)
		LastMadeCommunicationType differentCost = createLastMadeCommunication(
				TIMESTAMP1,
				COMMUNICATION_TYPE1,
				PHONE_NUMBER1,
				COMMUNICATION_SIZE1,
				COST2);
		
		// Assert (different costs)
		assertFalse("LastMadeCommunicationTypes with different costs " +
				"returned 'true'",
				comparator.equals(response, differentCost));
	}
	
	/**
	 * Test 8 - Test comparing finish call on destination responses.
	 */
	public void testFinishCallOnDestination() {
		// Arrange (equal responses)
		FinishCallOnDestinationType response = createFinishCallOnDestination(
				TIMESTAMP1,
				PHONE_NUMBER1,
				COMMUNICATION_SIZE1,
				COST1);
		FinishCallOnDestinationType equalResponse =
				createFinishCallOnDestination(
						TIMESTAMP1,
						PHONE_NUMBER1,
						COMMUNICATION_SIZE1,
						COST1);
		
		// Assert (equal responses)
		assertTrue("Equal FinishCallOnDestinationTypes returned 'false'",
				comparator.equals(response, equalResponse));
		
		// Arrange (different timestamps)
		FinishCallOnDestinationType differentTimestamp =
				createFinishCallOnDestination(
						TIMESTAMP2,
						PHONE_NUMBER1,
						COMMUNICATION_SIZE1,
						COST1);
		
		// Assert (different timestamps)
		assertFalse("FinishCallOnDestinationTypes with different timestamps " +
				"returned 'true'",
				comparator.equals(response, differentTimestamp));
		
		// Arrange (different numbers)
		FinishCallOnDestinationType differentNumber =
				createFinishCallOnDestination(
						TIMESTAMP1,
						PHONE_NUMBER2,
						COMMUNICATION_SIZE1,
						COST1);
		
		// Assert (different numbers)
		assertFalse("FinishCallOnDestinationTypes with different numbers " +
				"returned 'true'",
				comparator.equals(response, differentNumber));
		
		// Arrange (different durations)
		FinishCallOnDestinationType differentDuration =
				createFinishCallOnDestination(
						TIMESTAMP1,
						PHONE_NUMBER1,
						COMMUNICATION_SIZE2,
						COST1);
		
		// Assert (different durations)
		assertFalse("FinishCallOnDestinationTypes with different durations " +
				"returned 'true'",
				comparator.equals(response, differentDuration));
		
		// Arrange (different costs)
		FinishCallOnDestinationType differentCost =
				createFinishCallOnDestination(
						TIMESTAMP1,
						PHONE_NUMBER1,
						COMMUNICATION_SIZE1,
						COST2);
		
		// Assert (different costs)
		assertFalse("FinishCallOnDestinationTypes with different costs " +
				"returned 'true'",
				comparator.equals(response, differentCost));
	}
	
	/**
	 * Test 9 - Test comparing OperatorNameAlreadyExistsException.
	 */
	public void testOperatorNameAlreadyExistsException() {
		// Arrange (equal exceptions)
		OperatorNameAlreadyExistsException exception =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		OperatorNameAlreadyExistsException equalException =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (equal exceptions)
		assertTrue("Equal OperatorNameAlreadyExistsException returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		OperatorNameAlreadyExistsException differentTimestamp =
				createOperatorNameAlreadyExists(
						TIMESTAMP2,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different timestamps)
		assertFalse("OperatorNameAlreadyExistsExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different names)
		OperatorNameAlreadyExistsException differentName =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME2,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different names)
		assertFalse("OperatorNameAlreadyExistsExceptions with different " +
				"names returned 'true'",
				comparator.equals(exception, differentName));

		// Arrange (different prefixes)
		OperatorNameAlreadyExistsException differentPrefix =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX2,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different prefixes)
		assertFalse("OperatorNameAlreadyExistsExceptions with different " +
				"prefixes returned 'true'",
				comparator.equals(exception, differentPrefix));
		
		// Arrange (different SMS costs)
		OperatorNameAlreadyExistsException differentSMSCost =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST2,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different SMS Costs)
		assertFalse("OperatorNameAlreadyExistsExceptions with different " +
				"SMS costs returned 'true'",
				comparator.equals(exception, differentSMSCost));
		
		// Arrange (different voice costs)
		OperatorNameAlreadyExistsException differentVoiceCost =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST2,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different voice costs)
		assertFalse("OperatorNameAlreadyExistsExceptions with different " +
				"voice costs returned 'true'",
				comparator.equals(exception, differentVoiceCost));
		
		// Arrange (different video costs)
		OperatorNameAlreadyExistsException differentVideoCost =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST2,
						TAX1,
						BONUS1);
		
		// Assert (different video costs)
		assertFalse("OperatorNameAlreadyExistsExceptions with different " +
				"video costs returned 'true'",
				comparator.equals(exception, differentVideoCost));
		
		// Arrange (different taxes)
		OperatorNameAlreadyExistsException differentTax =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX2,
						BONUS1);
		
		// Assert (different taxes)
		assertFalse("OperatorNameAlreadyExistsExceptions with different " +
				"taxes returned 'true'",
				comparator.equals(exception, differentTax));
		
		// Arrange (different bonuses)
		OperatorNameAlreadyExistsException differentBonus =
				createOperatorNameAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS2);
		
		// Assert (different bonuses)
		assertFalse("OperatorNameAlreadyExistsExceptions with different " +
				"bonuses returned 'true'",
				comparator.equals(exception, differentBonus));
	}
	
	/**
	 * Test 10 - Test comparing OperatorPrefixAlreadyExistsException.
	 */
	public void testOperatorPrefixAlreadyExistsException() {
		// Arrange (equal exceptions)
		OperatorPrefixAlreadyExistsException exception =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		OperatorPrefixAlreadyExistsException equalException =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);

		// Assert (equal exceptions)
		assertTrue("Equal OperatorPrefixAlreadyExistsException returned 'false'",
				comparator.equals(exception, equalException));
		// Arrange (different timestamps)
		OperatorPrefixAlreadyExistsException differentTimestamp =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP2,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different timestamps)
		assertFalse("OperatorPrefixAlreadyExistsExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different names)
		OperatorPrefixAlreadyExistsException differentName =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME2,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different names)
		assertFalse("OperatorPrefixAlreadyExistsExceptions with different " +
				"names returned 'true'",
				comparator.equals(exception, differentName));

		// Arrange (different prefixes)
		OperatorPrefixAlreadyExistsException differentPrefix =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX2,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different prefixes)
		assertFalse("OperatorPrefixAlreadyExistsExceptions with different " +
				"prefixes returned 'true'",
				comparator.equals(exception, differentPrefix));
		
		// Arrange (different SMS costs)
		OperatorPrefixAlreadyExistsException differentSMSCost =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST2,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different SMS Costs)
		assertFalse("OperatorPrefixAlreadyExistsExceptions with different " +
				"SMS costs returned 'true'",
				comparator.equals(exception, differentSMSCost));
		
		// Arrange (different voice costs)
		OperatorPrefixAlreadyExistsException differentVoiceCost =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST2,
						VIDEO_COST1,
						TAX1,
						BONUS1);
		
		// Assert (different voice costs)
		assertFalse("OperatorPrefixAlreadyExistsExceptions with different " +
				"voice costs returned 'true'",
				comparator.equals(exception, differentVoiceCost));
		
		// Arrange (different video costs)
		OperatorPrefixAlreadyExistsException differentVideoCost =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST2,
						TAX1,
						BONUS1);
		
		// Assert (different video costs)
		assertFalse("OperatorPrefixAlreadyExistsExceptions with different " +
				"video costs returned 'true'",
				comparator.equals(exception, differentVideoCost));
		
		// Arrange (different taxes)
		OperatorPrefixAlreadyExistsException differentTax =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX2,
						BONUS1);
		
		// Assert (different taxes)
		assertFalse("OperatorPrefixAlreadyExistsExceptions with different " +
				"taxes returned 'true'",
				comparator.equals(exception, differentTax));
		
		// Arrange (different bonuses)
		OperatorPrefixAlreadyExistsException differentBonus =
				createOperatorPrefixAlreadyExists(
						TIMESTAMP1,
						OPERATOR_NAME1,
						OPERATOR_PREFIX1,
						SMS_COST1,
						VOICE_COST1,
						VIDEO_COST1,
						TAX1,
						BONUS2);
		
		// Assert (different bonuses)
		assertFalse("OperatorPrefixAlreadyExistsExceptions with different " +
				"bonuses returned 'true'",
				comparator.equals(exception, differentBonus));
	}

	/**
	 * Test 11 - Test comparing OperatorPrefixNotValidException.
	 */
	public void testOperatorPrefixNotValidException() {
		// Arrange (equal responses)
		OperatorPrefixNotValidException exception =
				createOperatorPrefixNotValid(TIMESTAMP1, INVALID_PREFIX1);
		OperatorPrefixNotValidException equalException =
				createOperatorPrefixNotValid(TIMESTAMP1, INVALID_PREFIX1);
		OperatorPrefixNotValidException exceptionNullPrefix =
				createOperatorPrefixNotValid(TIMESTAMP1, INVALID_PREFIX_NULL);
		OperatorPrefixNotValidException equalExceptionNullPrefix =
				createOperatorPrefixNotValid(TIMESTAMP1, INVALID_PREFIX_NULL);
		
		// Assert (equal responses)
		assertTrue("Equal OperatorPrefixNotValidExceptions returned 'false'",
				comparator.equals(exception, equalException));
		assertTrue("Equal OperatorPrefixNotValidExceptions (with nulls) " +
				"returned 'false'",
				comparator.equals(
						exceptionNullPrefix,
						equalExceptionNullPrefix));
		
		// Arrange (different timestamps)
		OperatorPrefixNotValidException differentTimestamp =
				createOperatorPrefixNotValid(TIMESTAMP2, INVALID_PREFIX1);
		
		// Assert (different timestamps)
		assertFalse("OperatorPrefixNotValidExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different prefixes)
		OperatorPrefixNotValidException differentPrefix =
				createOperatorPrefixNotValid(TIMESTAMP1, INVALID_PREFIX2);
		OperatorPrefixNotValidException differentPrefixNull =
				createOperatorPrefixNotValid(TIMESTAMP1, INVALID_PREFIX_NULL);
		
		// Assert (different prefixes)
		assertFalse("OperatorPrefixNotValidExceptions with different " +
				"prefixes returned 'true'",
				comparator.equals(exception, differentPrefix));
		assertFalse("OperatorPrefixNotValidExceptions with different " +
				"prefixes (one null) returned 'true'",
				comparator.equals(exception, differentPrefixNull));
	}
	
	/**
	 * Test 12 - Test comparing OperatorNameNotValidException.
	 */
	public void testOperatorNameNotValidException() {
		// Arrange (equal responses)
		OperatorNameNotValidException exception =
				createOperatorNameNotValid(TIMESTAMP1, INVALID_NAME1);
		OperatorNameNotValidException equalException =
				createOperatorNameNotValid(TIMESTAMP1, INVALID_NAME1);
		OperatorNameNotValidException exceptionNameNull =
				createOperatorNameNotValid(TIMESTAMP1, INVALID_NAME_NULL);
		OperatorNameNotValidException equalExceptionNameNull =
				createOperatorNameNotValid(TIMESTAMP1, INVALID_NAME_NULL);
		
		// Assert (equal responses)
		assertTrue("Equal OperatorNameNotValidExceptions returned 'false'",
				comparator.equals(exception, equalException));
		assertTrue("Equal OperatorNameNotValidExceptions (with null names) " +
				"returned 'false'",
				comparator.equals(exceptionNameNull, equalExceptionNameNull));
		
		// Arrange (different timestamps)
		OperatorNameNotValidException differentTimestamp =
				createOperatorNameNotValid(TIMESTAMP2, INVALID_NAME1);
		
		// Assert (different timestamps)
		assertFalse("OperatorNameNotValidExceptions with different timestamps" +
				" returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different names)
		OperatorNameNotValidException differentName =
				createOperatorNameNotValid(TIMESTAMP1, INVALID_NAME2);
		OperatorNameNotValidException differentNameNull =
				createOperatorNameNotValid(TIMESTAMP1, INVALID_NAME_NULL);
		
		// Assert (different names)
		assertFalse("OperatorNameNotValidExceptions with different names " +
				"returned 'true'",
				comparator.equals(exception, differentName));
		assertFalse("OperatorNameNotValidExceptions with different names " +
				"(one null) returned 'true'",
				comparator.equals(exception, differentNameNull));
	}

	/**
	 * Test 13 - Test comparing BonusValueNotValidException.
	 */
	public void testBonusValueNotValidException() {
		// Arrange (equal responses)
		BonusValueNotValidException exception =
				createBonusValueNotValid(TIMESTAMP1, INVALID_BONUS1);
		BonusValueNotValidException equalException =
				createBonusValueNotValid(TIMESTAMP1, INVALID_BONUS1);
		
		// Assert (equal responses)
		assertTrue("Equal BonusValueNotValidExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		BonusValueNotValidException differentTimestamp =
				createBonusValueNotValid(TIMESTAMP2, INVALID_BONUS1);
		
		// Assert (different timestamps)
		assertFalse("BonusValueNotValidExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different bonuses)
		BonusValueNotValidException differentBonus =
				createBonusValueNotValid(TIMESTAMP1, INVALID_BONUS2);
		
		// Assert (different bonuses)
		assertFalse("BonusValueNotValidExceptions with different bonuses " +
				"returned 'true'",
				comparator.equals(exception, differentBonus));
	}
	
	/**
	 * Test 14 - Test comparing InvalidPhoneTypeException.
	 */
	public void testInvalidPhoneTypeException() {
		// Arrange (equal exceptions)
		InvalidPhoneTypeException exception = createInvalidPhoneType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_PHONETYPE1);
		InvalidPhoneTypeException equalException = createInvalidPhoneType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_PHONETYPE1);
		InvalidPhoneTypeException exceptionNullType = createInvalidPhoneType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_PHONETYPE_NULL);
		InvalidPhoneTypeException equalExceptionNullType =
				createInvalidPhoneType(
						TIMESTAMP1,
						PHONE_NUMBER1,
						INVALID_PHONETYPE_NULL);

		// Assert (equal exceptions)
		assertTrue("Equal InvalidPhoneTypeExceptions returned 'false'",
				comparator.equals(exception, equalException));
		assertTrue("Equal InvalidPhoneTypeExceptions (with null types) " +
				"returned 'false'",
				comparator.equals(exceptionNullType, equalExceptionNullType));
		
		// Arrange (different timestamps)
		InvalidPhoneTypeException differentTimestamp = createInvalidPhoneType(
				TIMESTAMP2,
				PHONE_NUMBER1,
				INVALID_PHONETYPE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidPhoneTypeExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different phone numbers)
		InvalidPhoneTypeException differentNumber = createInvalidPhoneType(
				TIMESTAMP1,
				PHONE_NUMBER2,
				INVALID_PHONETYPE1);
		
		// Assert (different phone numbers)
		assertFalse("InvalidPhoneTypeExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different types)
		InvalidPhoneTypeException differentType = createInvalidPhoneType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_PHONETYPE2);
		InvalidPhoneTypeException differentTypeNull = createInvalidPhoneType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_PHONETYPE_NULL);
		
		// Assert (different types)
		assertFalse("InvalidPhoneTypeExceptions with different types " +
				"returned 'true'",
				comparator.equals(exception, differentType));
		assertFalse("InvalidPhoneTypeExceptions with different types (one " +
				"null) returned 'true'",
				comparator.equals(exception, differentTypeNull));
	}
	
	/**
	 * Test 15 - Test comparing UnregonisedPrefixException.
	 */
	public void testUnrecognisedPrefixException() {
		// Arrange (equal exceptions)
		UnrecognisedPrefixException exception =
				createUnrecognisedPrefix(TIMESTAMP1, OPERATOR_PREFIX1);
		UnrecognisedPrefixException equalException =
				createUnrecognisedPrefix(TIMESTAMP1, OPERATOR_PREFIX1);
		
		// Assert (equal exceptions)
		assertTrue("Equal UnrecognisedPrefixExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		UnrecognisedPrefixException differentTimestamp =
				createUnrecognisedPrefix(TIMESTAMP2, OPERATOR_PREFIX1);
		
		// Assert (different timestamps)
		assertFalse("UnrecognisedPrefixExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different prefixes)
		UnrecognisedPrefixException differentPrefix =
				createUnrecognisedPrefix(TIMESTAMP1, OPERATOR_PREFIX2);
		
		// Assert (different prefixes)
		assertFalse("UnrecognisedPrefixExceptions with different prefixes " +
				"returned 'true'",
				comparator.equals(exception, differentPrefix));
	}
	
	/**
	 * Test 16 - Test comparing PhoneAlreadyExistsException.
	 */
	public void testPhoneAlreadyExistsException() {
		// Arrange (equal exceptions)
		PhoneAlreadyExistsException exception =
				createPhoneAlreadyExists(TIMESTAMP1, PHONE_NUMBER1);
		PhoneAlreadyExistsException equalException =
				createPhoneAlreadyExists(TIMESTAMP1, PHONE_NUMBER1);
		
		// Assert (equal exceptions)
		assertTrue("Equal PhoneAlreadyExistsExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		PhoneAlreadyExistsException differentTimestamp =
				createPhoneAlreadyExists(TIMESTAMP2, PHONE_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("PhoneAlreadyExistsExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different phone numbers)
		PhoneAlreadyExistsException differentNumber =
				createPhoneAlreadyExists(TIMESTAMP1, PHONE_NUMBER2);
		
		// Assert (different phone numbers)
		assertFalse("PhoneAlreadyExistsExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
	}
	
	/**
	 * Test 17 - Test comparing PhoneNumberNotValidException.
	 */
	public void testPhoneNumberNotValidException() {
		// Arrange (equal exceptions)
		PhoneNumberNotValidException exception =
				createPhoneNumberNotValid(TIMESTAMP1, INVALID_NUMBER1);
		PhoneNumberNotValidException equalException =
				createPhoneNumberNotValid(TIMESTAMP1, INVALID_NUMBER1);
		PhoneNumberNotValidException exceptionNullNumber =
				createPhoneNumberNotValid(TIMESTAMP1, INVALID_NUMBER_NULL);
		PhoneNumberNotValidException equalExceptionNullNumber =
				createPhoneNumberNotValid(TIMESTAMP1, INVALID_NUMBER_NULL);
		
		// Assert (equal exceptions)
		assertTrue("Equal PhoneNumberNotValidExceptions returned 'false'",
				comparator.equals(exception, equalException));
		assertTrue("Equal PhoneNumberNotValidExceptions (with nulls) " +
				"returned 'false'",
				comparator.equals(
						exceptionNullNumber,
						equalExceptionNullNumber));
		
		// Arrange (different timestamps)
		PhoneNumberNotValidException differentTimestamp =
				createPhoneNumberNotValid(TIMESTAMP2, INVALID_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("PhoneNumberNotValidExceptions with different timestamps" +
				" returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		PhoneNumberNotValidException differentNumber =
				createPhoneNumberNotValid(TIMESTAMP1, INVALID_NUMBER2);
		PhoneNumberNotValidException differentNumberNull =
				createPhoneNumberNotValid(TIMESTAMP1, INVALID_NUMBER_NULL);
		
		// Assert (different numbers)
		assertFalse("PhoneNumberNotValidExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		assertFalse("PhoneNumberNotValidExceptions with different numbers " +
				"(one null) returned 'true'",
				comparator.equals(exception, differentNumberNull));
	}
	
	/**
	 * Test 18 - Test comparing IncompatiblePrefixException.
	 */
	public void testIncompatiblePrefixException() {
		// Arrange (equal exceptions)
		IncompatiblePrefixException exception = createIncompatiblePrefix(
				TIMESTAMP1,
				OPERATOR_PREFIX1,
				PHONE_PREFIX1);
		IncompatiblePrefixException equalException = createIncompatiblePrefix(
				TIMESTAMP1,
				OPERATOR_PREFIX1,
				PHONE_PREFIX1);
		
		// Assert (equal exceptions)
		assertTrue("Equal IncompatiblePrefixExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		IncompatiblePrefixException differentTimestamp =
				createIncompatiblePrefix(
						TIMESTAMP2,
						OPERATOR_PREFIX1,
						PHONE_PREFIX1);
		
		// Assert (different timestamps)
		assertFalse("IncompatiblePrefixExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different operator prefixes)
		IncompatiblePrefixException differentOpPrefix =
				createIncompatiblePrefix(
						TIMESTAMP1,
						OPERATOR_PREFIX2,
						PHONE_PREFIX1);
		
		// Assert (different operator prefixes)
		assertFalse("IncompatiblePrefixExceptions with different operator " +
				"prefixes returned 'true'",
				comparator.equals(exception, differentOpPrefix));
		
		// Arrange (different phone prefixes)
		IncompatiblePrefixException differentPhonePrefix =
				createIncompatiblePrefix(
						TIMESTAMP1,
						OPERATOR_PREFIX1,
						PHONE_PREFIX2);
		
		// Assert (different phone prefixes)
		assertFalse("IncompatiblePrefixExceptions with different phone " +
				"prefixes returned 'true'",
				comparator.equals(exception, differentPhonePrefix));
	}
	
	/**
	 * Test 19 - Test comparing PhoneNotExistsException.
	 */
	public void testPhoneNotExistsException() {
		// Arrange (equal exceptions)
		PhoneNotExistsException exception =
				createPhoneNotExists(TIMESTAMP1, PHONE_NUMBER1);
		PhoneNotExistsException equalException =
				createPhoneNotExists(TIMESTAMP1, PHONE_NUMBER1);
		
		// Assert (equal exceptions)
		assertTrue("Equal PhoneNotExistsExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		PhoneNotExistsException differentTimestamp =
				createPhoneNotExists(TIMESTAMP2, PHONE_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("PhoneNotExistsExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		PhoneNotExistsException differentNumber =
				createPhoneNotExists(TIMESTAMP1, PHONE_NUMBER2);
		
		// Assert (different numbers)
		assertFalse("PhoneNotExistsExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
	}
	
	/**
	 * Test 20 - Test comparing InvalidAmountException.
	 */
	public void testInvalidAmountException() {
		// Arrange (equal exceptions)
		InvalidAmountException exception =
				createInvalidAmount(TIMESTAMP1, PHONE_NUMBER1, INVALID_AMOUNT1);
		InvalidAmountException equalException =
				createInvalidAmount(TIMESTAMP1, PHONE_NUMBER1, INVALID_AMOUNT1);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidAmountExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		InvalidAmountException differentTimestamp =
				createInvalidAmount(TIMESTAMP2, PHONE_NUMBER1, INVALID_AMOUNT1);
		
		// Assert (different timestamps)
		assertFalse("InvalidAmountExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidAmountException differentNumber =
				createInvalidAmount(TIMESTAMP1, PHONE_NUMBER2, INVALID_AMOUNT1);
		
		// Assert (different numbers)
		assertFalse("InvalidAmountExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different amounts)
		InvalidAmountException differentAmount =
				createInvalidAmount(TIMESTAMP1, PHONE_NUMBER1, INVALID_AMOUNT2);
		
		// Assert (different amounts)
		assertFalse("InvalidAmountExceptions with different amounts " +
				"returned 'true'",
				comparator.equals(exception, differentAmount));
	}
	
	/**
	 * Test 21 - Test comparing BalanceLimitExceededException.
	 */
	public void testBalanceLimitExceededException() {
		// Arrange (equal exceptions)
		BalanceLimitExceededException exception =
				createBalanceLimitExceeded(TIMESTAMP1, PHONE_NUMBER1);
		BalanceLimitExceededException equalException =
				createBalanceLimitExceeded(TIMESTAMP1, PHONE_NUMBER1);
		
		// Assert (equal exceptions)
		assertTrue("Equal BalanceLimitExceededExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		BalanceLimitExceededException differentTimestamp =
				createBalanceLimitExceeded(TIMESTAMP2, PHONE_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("BalanceLimitExceededExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		BalanceLimitExceededException differentNumber =
				createBalanceLimitExceeded(TIMESTAMP1, PHONE_NUMBER2);
		
		// Assert (different numbers)
		assertFalse("BalancedLimitExceededExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentNumber));
	}
	
	/**
	 * Test 22 - Test comparing NotPositiveBalanceException.
	 */
	public void testNotPositiveBalanceException() {
		// Arrange (equal exceptions)
		NotPositiveBalanceException exception = createNotPositiveBalance(
				TIMESTAMP1,
				PHONE_NUMBER1,
				NEGATIVE_BALANCE1);
		NotPositiveBalanceException equalException = createNotPositiveBalance(
				TIMESTAMP1,
				PHONE_NUMBER1,
				NEGATIVE_BALANCE1);
		
		// Assert (equal exceptions)
		assertTrue("Equal NotPositiveBalanceExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		NotPositiveBalanceException differentTimestamp =
				createNotPositiveBalance(
						TIMESTAMP2,
						PHONE_NUMBER1,
						NEGATIVE_BALANCE1);
		
		// Assert (different timestamps)
		assertFalse("NotPositiveBalanceExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		NotPositiveBalanceException differentNumber = createNotPositiveBalance(
				TIMESTAMP1,
				PHONE_NUMBER2,
				NEGATIVE_BALANCE1);
		
		// Assert (different numbers)
		assertFalse("NotPositiveBalanceExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different balances)
		NotPositiveBalanceException differentBalance = createNotPositiveBalance(
				TIMESTAMP1,
				PHONE_NUMBER1,
				NEGATIVE_BALANCE2);
		
		// Assert (different balances)
		assertFalse("NotPositiveBalanceExceptions with different balances " +
				"returned 'true'",
				comparator.equals(exception, differentBalance));
	}
	
	/**
	 * Test 23 - Test comparing InvalidStateSendSMSException.
	 */
	public void testInvalidStateSendSMS() {
		// Arrange (equal exceptions)
		InvalidStateSendSMSException exception =
				createInvalidStateSendSMS(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		InvalidStateSendSMSException equalException =
				createInvalidStateSendSMS(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidStateSendSMSExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		InvalidStateSendSMSException differentTimestamp =
				createInvalidStateSendSMS(TIMESTAMP2, PHONE_NUMBER1, STATE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidStateSendSMSExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidStateSendSMSException differentNumber =
				createInvalidStateSendSMS(TIMESTAMP1, PHONE_NUMBER2, STATE1);
		
		// Assert (different numbers)
		assertFalse("InvalidStateSendSMSExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different states)
		InvalidStateSendSMSException differentState =
				createInvalidStateSendSMS(TIMESTAMP1, PHONE_NUMBER1, STATE2);
		
		// Assert (different states)
		assertFalse("InvalidStateSendSMSExceptions with different states " +
				"returned 'true'",
				comparator.equals(exception, differentState));	
	}
	
	/**
	 * Test 24 - Test comparing SMSMessageNotValidException.
	 */
	public void testSMSMessageNotValid() {
		// Arrange (equal exceptions)
		SMSMessageNotValidException exception = createSMSMessageNotValid(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_MESSAGE1);
		SMSMessageNotValidException equalException = createSMSMessageNotValid(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_MESSAGE1);
		SMSMessageNotValidException exceptionMessageNull =
				createSMSMessageNotValid(
						TIMESTAMP1,
						PHONE_NUMBER1,
						INVALID_MESSAGE_NULL);
		SMSMessageNotValidException equalExceptionMessageNull =
				createSMSMessageNotValid(
						TIMESTAMP1,
						PHONE_NUMBER1,
						INVALID_MESSAGE_NULL);
		
		// Assert (equal exceptions)
		assertTrue("Equal SMSMessageNotValidExceptions returned 'false'",
				comparator.equals(exception, equalException));
		assertTrue("Equal SMSMessageNotValidExceptions (with null messages) " +
				"returned 'false'",
				comparator.equals(
						exceptionMessageNull,
						equalExceptionMessageNull));
		
		// Arrange (different timestamps)
		SMSMessageNotValidException differentTimestamp =
				createSMSMessageNotValid(
						TIMESTAMP2,
						PHONE_NUMBER1,
						INVALID_MESSAGE1);
		
		// Assert (different timestamps)
		assertFalse("SMSMessageNotValidExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		SMSMessageNotValidException differentNumber =
				createSMSMessageNotValid(
						TIMESTAMP1,
						PHONE_NUMBER2,
						INVALID_MESSAGE1);
		
		// Assert (different numbers)
		assertFalse("SMSMessageNotValidExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different messages)
		SMSMessageNotValidException differentMessage =
				createSMSMessageNotValid(
						TIMESTAMP1,
						PHONE_NUMBER1,
						INVALID_MESSAGE2);
		SMSMessageNotValidException differentMessageNull =
				createSMSMessageNotValid(
						TIMESTAMP1,
						PHONE_NUMBER1,
						INVALID_MESSAGE_NULL);
		
		// Assert (different messages)
		assertFalse("SMSMessageNotValidExceptions with different messages " +
				"returned 'true'",
				comparator.equals(exception, differentMessage));
		assertFalse("SMSMessageNotValidExceptions with different messages " +
				"(one null) returned 'true'",
				comparator.equals(exception, differentMessageNull));
	}
	
	/**
	 * Test 25 - Test comparing InvalidStateException.
	 */
	public void testInvalidState() {
		// Arrange (equal exceptions)
		InvalidStateException exception =
				createInvalidState(TIMESTAMP1, PHONE_NUMBER1, INVALID_STATE1);
		InvalidStateException equalException =
				createInvalidState(TIMESTAMP1, PHONE_NUMBER1, INVALID_STATE1);
		InvalidStateException exceptionNullState = createInvalidState(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_STATE_NULL);
		InvalidStateException equalExceptionNullState = createInvalidState(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_STATE_NULL);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidStateExceptions returned 'false'",
				comparator.equals(exception, equalException));
		assertTrue("Equal InvalidStateExceptions (with null states) " +
				"returned 'false'",
				comparator.equals(exceptionNullState, equalExceptionNullState));
		
		// Arrange (different timestamps)
		InvalidStateException differentTimestamp =
				createInvalidState(TIMESTAMP2, PHONE_NUMBER1, INVALID_STATE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidStateExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidStateException differentNumber =
				createInvalidState(TIMESTAMP1, PHONE_NUMBER2, INVALID_STATE1);
		
		// Assert (different numbers)
		assertFalse("InvalidStateExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different states)
		InvalidStateException differentState =
				createInvalidState(TIMESTAMP1, PHONE_NUMBER1, INVALID_STATE2);
		InvalidStateException differentStateNull = createInvalidState(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_STATE_NULL);
		
		// Assert (different states)
		assertFalse("InvalidStateExceptions with different states returned " +
				"'true'",
				comparator.equals(exception, differentState));
		assertFalse("InvalidStateExceptions with different states (one null) " +
				"returned 'true'",
				comparator.equals(exception, differentStateNull));
	}
	
	/**
	 * Test 26 - Test comparing CantChangeStateException.
	 */
	public void testCantChangeState() {
		// Arrange (equal exceptions)
		CantChangeStateException exception = createCantChangeState(
				TIMESTAMP1,
				PHONE_NUMBER1,
				STATE1,
				STATE2);
		CantChangeStateException equalException = createCantChangeState(
				TIMESTAMP1,
				PHONE_NUMBER1,
				STATE1,
				STATE2);
		
		// Assert (equal exceptions)
		assertTrue("Equal CantChangeStateExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		CantChangeStateException differentTimestamp = createCantChangeState(
				TIMESTAMP2,
				PHONE_NUMBER1,
				STATE1,
				STATE2);
		 
		// Assert (different timestamps)
		assertFalse("CantChangeStateExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		CantChangeStateException differentNumber = createCantChangeState(
				TIMESTAMP1,
				PHONE_NUMBER2,
				STATE1,
				STATE2);
		
		// Assert (different numbers)
		assertFalse("CantChangeStateExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different current states)
		CantChangeStateException differentCurrentState = createCantChangeState(
				TIMESTAMP1,
				PHONE_NUMBER1,
				STATE3,
				STATE2);
		
		// Assert (different current states)
		assertFalse("CantChangeStateExceptions with different current " +
				"states returned 'true'",
				comparator.equals(exception, differentCurrentState));
		
		// Arrange (different invalid states)
		CantChangeStateException differentInvalidState = createCantChangeState(
				TIMESTAMP1,
				PHONE_NUMBER1,
				STATE1,
				STATE3);
		
		// Assert (different invalid states)
		assertFalse("CantChangeStateExceptions with different invalid " +
				"states returned 'true'",
				comparator.equals(exception, differentInvalidState));
	}
	
	/**
	 * Test 27 - Test comparing NoMadeCommunicationException.
	 */
	public void testNoMadeCommunication() {
		// Arrange (equal exceptions)
		NoMadeCommunicationException exception =
				createNoMadeCommunication(TIMESTAMP1, PHONE_NUMBER1);
		NoMadeCommunicationException equalException =
				createNoMadeCommunication(TIMESTAMP1, PHONE_NUMBER1);
		
		// Assert (equal exceptions)
		assertTrue("Equal NoMadeCommunicationException returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		NoMadeCommunicationException differentTimestamp =
				createNoMadeCommunication(TIMESTAMP2, PHONE_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("NoMadeCommunicationExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		NoMadeCommunicationException differentNumber =
				createNoMadeCommunication(TIMESTAMP1, PHONE_NUMBER2);
		
		// Assert (different numbers)
		assertFalse("NoMadeCommunicationExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
	}

	/**
	 * Test 28 - Test comparing InvalidStateMakeVoiceException.
	 */
	public void testInvalidStateMakeVoice() {
		// Arrange (equal exceptions)
		InvalidStateMakeVoiceException exception = 
				createInvalidStateMakeVoice(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		InvalidStateMakeVoiceException equalException = 
				createInvalidStateMakeVoice(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidStateMakeVoiceException returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		InvalidStateMakeVoiceException differentTimestamp =
				createInvalidStateMakeVoice(TIMESTAMP2, PHONE_NUMBER1, STATE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidStateMakeVoiceExceptions with different " +
				"timetamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidStateMakeVoiceException differentNumber =
				createInvalidStateMakeVoice(TIMESTAMP1, PHONE_NUMBER2, STATE1);
		
		// Assert (different numbers)
		assertFalse("InvalidStateMakeVoiceExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different states)
		InvalidStateMakeVoiceException differentState =
				createInvalidStateMakeVoice(TIMESTAMP1, PHONE_NUMBER1, STATE2);
		
		// Assert (different states)
		assertFalse("InvalidStateMakeVoiceExceptions with different states " +
				"returned 'true'",
				comparator.equals(exception, differentState));
	}
	
	/**
	 * Test 29 - Test comparing InvalidStateMakeVideoException.
	 */
	public void testInvalidStateMakeVideo() {
		// Arrange (equal exceptions)
		InvalidStateMakeVideoException exception =
				createInvalidStateMakeVideo(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		InvalidStateMakeVideoException equalException =
				createInvalidStateMakeVideo(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidStateMakeVideoExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		InvalidStateMakeVideoException differentTimestamp =
				createInvalidStateMakeVideo(TIMESTAMP2, PHONE_NUMBER1, STATE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidStateMakeVideoExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidStateMakeVideoException differentNumber =
				createInvalidStateMakeVideo(TIMESTAMP1, PHONE_NUMBER2, STATE1);
		
		// Assert (different numbers)
		assertFalse("InvalidStateMakeVideoExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different states)
		InvalidStateMakeVideoException differentState =
				createInvalidStateMakeVideo(TIMESTAMP1, PHONE_NUMBER1, STATE2);
		
		// Assert (different states)
		assertFalse("InvalidStateMakeVideoExceptions with different states " +
				"returned 'true'",
				comparator.equals(exception, differentState));
	}
	
	/**
	 * Test 30 - Test comparing CantMakeVoiceCallException.
	 */
	public void testCantMakeVoiceCall() {
		// Arrange (equal exceptions)
		CantMakeVoiceCallException exception =
				createCantMakeVoiceCall(TIMESTAMP1, PHONE_NUMBER1);
		CantMakeVoiceCallException equalException =
				createCantMakeVoiceCall(TIMESTAMP1, PHONE_NUMBER1);
		
		// Assert (equal exceptions)
		assertTrue("Equal CantMakeVoiceCallExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		CantMakeVoiceCallException differentTimestamp =
				createCantMakeVoiceCall(TIMESTAMP2, PHONE_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("CantMakeVoiceCallExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		CantMakeVoiceCallException differentNumber =
				createCantMakeVoiceCall(TIMESTAMP1, PHONE_NUMBER2);
		
		// Assert (different numbers)
		assertFalse("CantMakeVoiceCallExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
	}
	
	/**
	 * Test 31 - Test comparing CantMakeVideoCallException.
	 */
	public void testCantMakeVideoCall() {
		// Arrange (equal exceptions)
		CantMakeVideoCallException exception =
				createCantMakeVideoCall(TIMESTAMP1, PHONE_NUMBER1);
		CantMakeVideoCallException equalException =
				createCantMakeVideoCall(TIMESTAMP1, PHONE_NUMBER1);
		
		// Assert (equal exceptions)
		assertTrue("Equal CantMakeVideoCallExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		CantMakeVideoCallException differentTimestamp =
				createCantMakeVideoCall(TIMESTAMP2, PHONE_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("CantMakeVideoCallExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		CantMakeVideoCallException differentNumber =
				createCantMakeVideoCall(TIMESTAMP1, PHONE_NUMBER2);
		
		// Assert (different numbers)
		assertFalse("CantMakeVideoCallExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
	}
	
	/**
	 * Test 32 - Test comparing InvalidCallTypeException.
	 */
	public void testInvalidCallType() {
		// Arrange (equal exceptions)
		InvalidCallTypeException exception = createInvalidCallType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_CALL1);
		InvalidCallTypeException equalException = createInvalidCallType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_CALL1);
		InvalidCallTypeException exceptionNullType = createInvalidCallType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_CALL_NULL);
		InvalidCallTypeException equalExceptionNullType = createInvalidCallType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_CALL_NULL);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidCallTypeExceptions returned 'false'",
				comparator.equals(exception, equalException));
		assertTrue("Equal InvalidCallTypeExceptions (with null types) " +
				"returned 'false'",
				comparator.equals(exceptionNullType, equalExceptionNullType));
		
		// Arrange (different timestamps)
		InvalidCallTypeException differentTimestamp = createInvalidCallType(
				TIMESTAMP2,
				PHONE_NUMBER1,
				INVALID_CALL1);
		
		// Assert (different timestamps)
		assertFalse("InvalidCallTypeExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidCallTypeException differentNumber = createInvalidCallType(
				TIMESTAMP1,
				PHONE_NUMBER2,
				INVALID_CALL1);
		
		// Assert (different numbers)
		assertFalse("InvalidCallTypeExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different call types)
		InvalidCallTypeException differentType = createInvalidCallType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_CALL2);
		InvalidCallTypeException differentTypeNull = createInvalidCallType(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_CALL_NULL);
		
		// Assert (different call types)
		assertFalse("InvalidCallTypeExceptions with different types " +
				"returned 'true'",
				comparator.equals(exception, differentType));
		assertFalse("InvalidCallTypeExceptions with different types (one " +
				"null) returned 'true'",
				comparator.equals(exception, differentTypeNull));
	}
	
	/**
	 * Test 33 - Test comparing InvalidStateReceiveVoiceException.
	 */
	public void testInvalidStateReceiveVoice() {
		// Arrange (equal exceptions)
		InvalidStateReceiveVoiceException exception = 
				createInvalidStateReceiveVoice(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		InvalidStateReceiveVoiceException equalException = 
				createInvalidStateReceiveVoice(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidStateReceiveVoiceException returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		InvalidStateReceiveVoiceException differentTimestamp =
				createInvalidStateReceiveVoice(
						TIMESTAMP2,
						PHONE_NUMBER1,
						STATE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidStateReceiveVoiceExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidStateReceiveVoiceException differentNumber =
				createInvalidStateReceiveVoice(
						TIMESTAMP1,
						PHONE_NUMBER2,
						STATE1);
		
		// Assert (different numbers)
		assertFalse("InvalidStateReceiveVoiceExceptions with different" +
				"numbers returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different states)
		InvalidStateReceiveVoiceException differentState =
				createInvalidStateReceiveVoice(
						TIMESTAMP1,
						PHONE_NUMBER1,
						STATE2);
		
		// Assert (different states)
		assertFalse("InvalidStateReceiveVoiceExceptions with different" +
				"states returned 'true'",
				comparator.equals(exception, differentState));
	}
	
	/**
	 * Test 34 - Test comparing InvalidStateReceiveVideoException.
	 */
	public void testInvalidStateReceiveVideo() {
		// Arrange (equal exceptions)
		InvalidStateReceiveVideoException exception = 
				createInvalidStateReceiveVideo(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		InvalidStateReceiveVideoException equalException = 
				createInvalidStateReceiveVideo(TIMESTAMP1, PHONE_NUMBER1, STATE1);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidStateReceiveVideoException returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		InvalidStateReceiveVideoException differentTimestamp =
				createInvalidStateReceiveVideo(
						TIMESTAMP2,
						PHONE_NUMBER1,
						STATE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidStateReceiveVideoExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidStateReceiveVideoException differentNumber =
				createInvalidStateReceiveVideo(
						TIMESTAMP1,
						PHONE_NUMBER2,
						STATE1);
		
		// Assert (different numbers)
		assertFalse("InvalidStateReceiveVideoExceptions with different " +
				"numbers returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different states)
		InvalidStateReceiveVideoException differentState =
				createInvalidStateReceiveVideo(
						TIMESTAMP1,
						PHONE_NUMBER1,
						STATE2);
		
		// Assert (different states)
		assertFalse("InvalidStateReceiveVideoExceptions with different " +
				"states returned 'true'",
				comparator.equals(exception, differentState));
	}
	
	/**
	 * Test 35 - Test comparing CantReceiveVoiceCallException.
	 */
	public void testCantReceiveVoiceCall() {
		// Arrange (equal exceptions)
		CantReceiveVoiceCallException exception =
				createCantReceiveVoiceCall(TIMESTAMP1, PHONE_NUMBER1);
		CantReceiveVoiceCallException equalException =
				createCantReceiveVoiceCall(TIMESTAMP1, PHONE_NUMBER1);
		
		// Assert (equal exceptions)
		assertTrue("Equal CantReceiveVoiceCallExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		CantReceiveVoiceCallException differentTimestamp =
				createCantReceiveVoiceCall(TIMESTAMP2, PHONE_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("CantReceiveVoiceCallExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		CantReceiveVoiceCallException differentNumber =
				createCantReceiveVoiceCall(TIMESTAMP1, PHONE_NUMBER2);
		
		// Assert (different numbers)
		assertFalse("CantReceiveVoiceCallExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
	}
	
	/**
	 * Test 36 - Test comparing CantReceiveVideoCallException.
	 */
	public void testCantReceiveVideoCall() {
		// Arrange (equal exceptions)
		CantReceiveVideoCallException exception =
				createCantReceiveVideoCall(TIMESTAMP1, PHONE_NUMBER1);
		CantReceiveVideoCallException equalException =
				createCantReceiveVideoCall(TIMESTAMP1, PHONE_NUMBER1);
		
		// Assert (equal exceptions)
		assertTrue("Equal CantReceiveVideoCallExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		CantReceiveVideoCallException differentTimestamp =
				createCantReceiveVideoCall(TIMESTAMP2, PHONE_NUMBER1);
		
		// Assert (different timestamps)
		assertFalse("CantReceiveVideoCallExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		CantReceiveVideoCallException differentNumber =
				createCantReceiveVideoCall(TIMESTAMP1, PHONE_NUMBER2);
		
		// Assert (different numbers)
		assertFalse("CantReceiveVideoCallExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
	}
	
	/**
	 * Test 37 - Test comparing DurationNotValidException.
	 */
	public void testDurationNotValid() {
		// Arrange (equal exceptions)
		DurationNotValidException exception = createDurationNotValid(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_DURATION1);
		DurationNotValidException equalException = createDurationNotValid(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_DURATION1);
		
		// Assert (equal exceptions)
		assertTrue("Equal DurationNotValidExceptions returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		DurationNotValidException differentTimestamp = createDurationNotValid(
				TIMESTAMP2,
				PHONE_NUMBER1,
				INVALID_DURATION1);
		
		// Assert (different timestamps)
		assertFalse("DurationNotValidExceptions with different timestamps " +
				"returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		DurationNotValidException differentNumber = createDurationNotValid(
				TIMESTAMP1,
				PHONE_NUMBER2,
				INVALID_DURATION1);
		
		// Assert (different numbers)
		assertFalse("DurationNotValidExceptions with different numbers " +
				"returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different durations)
		DurationNotValidException differentDuration = createDurationNotValid(
				TIMESTAMP1,
				PHONE_NUMBER1,
				INVALID_DURATION2);
		
		// Assert (different durations)
		assertFalse("DurationNotValidExceptions with different durations " +
				"returned 'true'",
				comparator.equals(exception, differentDuration));
	}
	
	/**
	 * Test 38 - Test comparing InvalidStateFinishMakingCallException.
	 */
	public void testInvalidStateFinishMakingCall() {
		// Arrange (equal exceptions)
		InvalidStateFinishMakingCallException exception =
				createInvalidStateFinishMakingCall(
						TIMESTAMP1,
						PHONE_NUMBER1,
						STATE1);
		InvalidStateFinishMakingCallException equalException =
				createInvalidStateFinishMakingCall(
						TIMESTAMP1,
						PHONE_NUMBER1,
						STATE1);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidStateFinishMakingCallExceptions returned " +
				"'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		InvalidStateFinishMakingCallException differentTimestamp =
				createInvalidStateFinishMakingCall(
						TIMESTAMP2,
						PHONE_NUMBER1,
						STATE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidStateFinishMakingCallExceptions with different " +
				"timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidStateFinishMakingCallException differentNumber =
				createInvalidStateFinishMakingCall(
						TIMESTAMP1,
						PHONE_NUMBER2,
						STATE1);
		
		// Assert (different numbers)
		assertFalse("InvalidStateFinishMakingCallExceptions with different " +
				"numbers returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different states)
		InvalidStateFinishMakingCallException differentState =
				createInvalidStateFinishMakingCall(
						TIMESTAMP1,
						PHONE_NUMBER1,
						STATE2);
		
		// Assert (different states)
		assertFalse("InvalidStateFinishMakingCallExceptions with different " +
				"states returned 'true'",
				comparator.equals(exception, differentState));
	}
	
	/**
	 * Test 39 - Test comparing InvalidStateFinishReceivingCallException.
	 */
	public void testInvalidStateFinishReceivingCall() {
		// Arrange (equal exceptions)
		InvalidStateFinishReceivingCallException exception =
				createInvalidStateFinishReceivingCall(
						TIMESTAMP1,
						PHONE_NUMBER1,
						STATE1);
		InvalidStateFinishReceivingCallException equalException =
				createInvalidStateFinishReceivingCall(
						TIMESTAMP1,
						PHONE_NUMBER1,
						STATE1);
		
		// Assert (equal exceptions)
		assertTrue("Equal InvalidStateFinishReceivingCallExceptions " +
				"returned 'false'",
				comparator.equals(exception, equalException));
		
		// Arrange (different timestamps)
		InvalidStateFinishReceivingCallException differentTimestamp =
				createInvalidStateFinishReceivingCall(
						TIMESTAMP2,
						PHONE_NUMBER1,
						STATE1);
		
		// Assert (different timestamps)
		assertFalse("InvalidStateFinishReceivingCallExceptions with " +
				"different timestamps returned 'true'",
				comparator.equals(exception, differentTimestamp));
		
		// Arrange (different numbers)
		InvalidStateFinishReceivingCallException differentNumber =
				createInvalidStateFinishReceivingCall(
						TIMESTAMP1,
						PHONE_NUMBER2,
						STATE1);
		
		// Assert (different numbers)
		assertFalse("InvalidStateFinishReceivingCallExceptions with " +
				"different numbers returned 'true'",
				comparator.equals(exception, differentNumber));
		
		// Arrange (different states)
		InvalidStateFinishReceivingCallException differentState =
				createInvalidStateFinishReceivingCall(
						TIMESTAMP1,
						PHONE_NUMBER1,
						STATE2);
		
		// Assert (different states)
		assertFalse("InvalidStateFinishReceivingCallExceptions with " +
				"different states returned 'true'",
				comparator.equals(exception, differentState));
	}
	
}