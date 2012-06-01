package anacom.replication.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import anacom.presentationserver.server.replication.TimestampMaster;
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
import anacom.shared.stubs.client.CommunicationErrorException;
import anacom.shared.stubs.client.CommunicationErrorType;
import anacom.shared.stubs.client.DurationNotValidException;
import anacom.shared.stubs.client.DurationNotValidType;
import anacom.shared.stubs.client.FinishCallOnDestinationType;
import anacom.shared.stubs.client.FinishCallType;
import anacom.shared.stubs.client.IncompatiblePrefixException;
import anacom.shared.stubs.client.IncompatiblePrefixType;
import anacom.shared.stubs.client.IncreasePhoneBalanceType;
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
import anacom.shared.stubs.client.MakeCallType;
import anacom.shared.stubs.client.NoMadeCommunicationException;
import anacom.shared.stubs.client.NoMadeCommunicationType;
import anacom.shared.stubs.client.NotPositiveBalanceException;
import anacom.shared.stubs.client.NotPositiveBalanceType;
import anacom.shared.stubs.client.OperatorDetailedType;
import anacom.shared.stubs.client.OperatorNameAlreadyExistsException;
import anacom.shared.stubs.client.OperatorNameAlreadyExistsType;
import anacom.shared.stubs.client.OperatorNameNotValidException;
import anacom.shared.stubs.client.OperatorNameNotValidType;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsException;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsType;
import anacom.shared.stubs.client.OperatorPrefixNotValidException;
import anacom.shared.stubs.client.OperatorPrefixNotValidType;
import anacom.shared.stubs.client.OperatorPrefixType;
import anacom.shared.stubs.client.PhoneAlreadyExistsException;
import anacom.shared.stubs.client.PhoneAlreadyExistsType;
import anacom.shared.stubs.client.PhoneListType;
import anacom.shared.stubs.client.PhoneNotExistsException;
import anacom.shared.stubs.client.PhoneNotExistsType;
import anacom.shared.stubs.client.PhoneNumberNotValidException;
import anacom.shared.stubs.client.PhoneNumberNotValidType;
import anacom.shared.stubs.client.PhoneNumberType;
import anacom.shared.stubs.client.PhoneStateType;
import anacom.shared.stubs.client.PhoneType;
import anacom.shared.stubs.client.ReceiveSMSType;
import anacom.shared.stubs.client.ReceivedSMSListType;
import anacom.shared.stubs.client.ReceivedSMSType;
import anacom.shared.stubs.client.RegisterPhoneType;
import anacom.shared.stubs.client.SMSMessageNotValidException;
import anacom.shared.stubs.client.SMSMessageNotValidType;
import anacom.shared.stubs.client.SendSMSType;
import anacom.shared.stubs.client.UnrecognisedPrefixException;
import anacom.shared.stubs.client.UnrecognisedPrefixType;
import anacom.shared.stubs.client.VoidResponseType;

public class TimestampMasterTestCase extends AnacomReplicationTestCase {
	
	/**
	 * This object that will be tested.
	 */
	private final TimestampMaster timestampMaster;
	/**
	 * This is the timestamp that will be used during tests.
	 */
	private XMLGregorianCalendar timestamp;
	
	/**
	 * This list will be used to store the objects that will be tested.
	 */
	private Collection<Object> objectList; 

	/**
	 * This object will be used to fill objects that will be tested.
	 */
	private final Object NULL = null;
	
	/**
	 * This method creates a new timestamp that will be used for testing.
	 * @return XMLGregorianCaledar
	 */
	private XMLGregorianCalendar createTimestamp()  {
		XMLGregorianCalendar timestamp;
		try {
			timestamp = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			timestamp.setYear(YEAR);
			timestamp.setMonth(MONTH);
			timestamp.setDay(DAY);
			timestamp.setHour(HOUR);
			timestamp.setMinute(MINUTE);
			timestamp.setSecond(SECOND);
			return timestamp;
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException();
		}
	}
	
	public TimestampMasterTestCase() { 
		super(); 
			timestamp = createTimestamp();
			timestampMaster = new TimestampMaster();
			objectList = new ArrayList<Object>();
	}
	
	public TimestampMasterTestCase(String msg) {
		super(msg); 
		timestamp = createTimestamp();
		timestampMaster = new TimestampMaster();
		objectList = new ArrayList<Object>();
	}
	
	// remote exceptions
	private BalanceLimitExceededException createBalanceLimitExceededException() {
		BalanceLimitExceededType local = new BalanceLimitExceededType();
		local.setTimestamp(timestamp);
		return new BalanceLimitExceededException((String)NULL, local);
	}
	
	private BonusValueNotValidException createBonusValueNotValidException() {
		BonusValueNotValidType local = new BonusValueNotValidType();
		local.setTimestamp(timestamp);
		return new BonusValueNotValidException((String)NULL, local);
	}
	
	private CantChangeStateException createCantChangeStateException() {
		CantChangeStateType local = new CantChangeStateType();
		local.setTimestamp(timestamp);
		return new CantChangeStateException((String)NULL, local);
	}
	
	private CantMakeVideoCallException createCantMakeVideoCallException() {
		CantMakeVideoCallType local = new CantMakeVideoCallType();
		local.setTimestamp(timestamp);
		return new CantMakeVideoCallException((String)NULL, local);
	}
	
	private CantMakeVoiceCallException createCantMakeVoiceCallException() {
		CantMakeVoiceCallType local = new CantMakeVoiceCallType();
		local.setTimestamp(timestamp);
		return new CantMakeVoiceCallException((String)NULL, local);
	}
	
	private CantReceiveVideoCallException createCantReceiveVideoCallException() {
		CantReceiveVideoCallType local = new CantReceiveVideoCallType();
		local.setTimestamp(timestamp);
		return new CantReceiveVideoCallException((String)NULL, local);
	}
	
	private CantReceiveVoiceCallException createCantReceiveVoiceCallException() {
		CantReceiveVoiceCallType local = new CantReceiveVoiceCallType();
		local.setTimestamp(timestamp);
		return new CantReceiveVoiceCallException((String)NULL, local);
	}
	
	private CommunicationErrorException createCommunicationErrorException() {
		CommunicationErrorType local = new CommunicationErrorType();
		local.setTimestamp(timestamp);
		return new CommunicationErrorException((String)NULL, local);
	}
	
	private DurationNotValidException createDurationNotValidException() {
		DurationNotValidType local = new DurationNotValidType();
		local.setTimestamp(timestamp);
		return new DurationNotValidException((String)NULL, local);
	}
	
	private IncompatiblePrefixException createIncompatiblePrefixException() {
		IncompatiblePrefixType local = new IncompatiblePrefixType();
		local.setTimestamp(timestamp);
		return new IncompatiblePrefixException((String)NULL, local);
	}
	
	private InvalidAmountException createInvalidAmountException() {
		InvalidAmountType local = new InvalidAmountType();
		local.setTimestamp(timestamp);
		return new InvalidAmountException((String)NULL, local);
	}
	
	private InvalidCallTypeException createInvalidCallTypeException() {
		InvalidCallTypeType local = new InvalidCallTypeType();
		local.setTimestamp(timestamp);
		return new InvalidCallTypeException((String)NULL, local);
	}
	
	private InvalidPhoneTypeException createInvalidPhoneTypeException() {
		InvalidPhoneTypeType local = new InvalidPhoneTypeType();
		local.setTimestamp(timestamp);
		return new InvalidPhoneTypeException((String)NULL, local);
	}
	
	private InvalidStateException createInvalidStateException() {
		InvalidStateType local = new InvalidStateType();
		local.setTimestamp(timestamp);
		return new InvalidStateException((String)NULL, local);
	}
	
	private InvalidStateFinishMakingCallException createInvalidStateFinishMakingCallException() {
		InvalidStateFinishMakingCallType local = new InvalidStateFinishMakingCallType();
		local.setTimestamp(timestamp);
		return new InvalidStateFinishMakingCallException((String)NULL, local);
	}

	private InvalidStateFinishReceivingCallException createInvalidStateFinishReceivingCallException() {
		InvalidStateFinishReceivingCallType local = new InvalidStateFinishReceivingCallType();
		local.setTimestamp(timestamp);
		return new InvalidStateFinishReceivingCallException((String)NULL, local);
	}
	
	private InvalidStateMakeVideoException createInvalidStateMakeVideoException() {
		InvalidStateMakeVideoType local = new InvalidStateMakeVideoType();
		local.setTimestamp(timestamp);
		return new InvalidStateMakeVideoException((String)NULL, local);
	}
	
	private InvalidStateMakeVoiceException createInvalidStateMakeVoiceException() {
		InvalidStateMakeVoiceType local = new InvalidStateMakeVoiceType();
		local.setTimestamp(timestamp);
		return new InvalidStateMakeVoiceException((String)NULL, local);
	}
	
	private InvalidStateReceiveVideoException createInvalidStateReceiveVideoException() {
		InvalidStateReceiveVideoType local = new InvalidStateReceiveVideoType();
		local.setTimestamp(timestamp);
		return new InvalidStateReceiveVideoException((String)NULL, local);
	}
	
	private InvalidStateReceiveVoiceException createInvalidStateReceiveVoiceException() {
		InvalidStateReceiveVoiceType local = new InvalidStateReceiveVoiceType();
		local.setTimestamp(timestamp);
		return new InvalidStateReceiveVoiceException((String)NULL, local);
	}
	
	private InvalidStateSendSMSException createInvalidStateSendSMSException() {
		InvalidStateSendSMSType local = new InvalidStateSendSMSType();
		local.setTimestamp(timestamp);
		return new InvalidStateSendSMSException((String)NULL, local);
	}
	
	private NoMadeCommunicationException createNoMadeCommunicationException() {
		NoMadeCommunicationType local = new NoMadeCommunicationType();
		local.setTimestamp(timestamp);
		return new NoMadeCommunicationException((String)NULL, local);
	}
	
	private NotPositiveBalanceException createNotPositiveBalanceException() {
		NotPositiveBalanceType local = new NotPositiveBalanceType();
		local.setTimestamp(timestamp);
		return new NotPositiveBalanceException((String)NULL, local);
	}
	
	private OperatorNameAlreadyExistsException createOperatorNameAlreadyExistsException() {
		OperatorNameAlreadyExistsType local = new OperatorNameAlreadyExistsType();
		local.setTimestamp(timestamp);
		return new OperatorNameAlreadyExistsException((String)NULL, local);
	}
	
	private OperatorNameNotValidException createOperatorNameNotValidException() {
		OperatorNameNotValidType local = new OperatorNameNotValidType();
		local.setTimestamp(timestamp);
		return new OperatorNameNotValidException((String)NULL, local);
	}
	
	private OperatorPrefixAlreadyExistsException createOperatorPrefixAlreadyExistsException() {
		OperatorPrefixAlreadyExistsType local = new OperatorPrefixAlreadyExistsType();
		local.setTimestamp(timestamp);
		return new OperatorPrefixAlreadyExistsException((String)NULL, local);
	}
	
	private OperatorPrefixNotValidException createOperatorPrefixNotValidException() {
		OperatorPrefixNotValidType local = new OperatorPrefixNotValidType();
		local.setTimestamp(timestamp);
		return new OperatorPrefixNotValidException((String)NULL, local);
	}
	
	private PhoneAlreadyExistsException createPhoneAlreadyExistsException() {
		PhoneAlreadyExistsType local = new PhoneAlreadyExistsType();
		local.setTimestamp(timestamp);
		return new PhoneAlreadyExistsException((String)NULL, local);
	}
	
	private PhoneNotExistsException createPhoneNotExistsException() {
		PhoneNotExistsType local = new PhoneNotExistsType();
		local.setTimestamp(timestamp);
		return new PhoneNotExistsException((String)NULL, local);
	}
	
	private PhoneNumberNotValidException createPhoneNumberNotValidException() {
		PhoneNumberNotValidType local = new PhoneNumberNotValidType();
		local.setTimestamp(timestamp);
		return new PhoneNumberNotValidException((String)NULL, local);
	}
	
	private SMSMessageNotValidException createSMSMessageNotValidException() {
		SMSMessageNotValidType local = new SMSMessageNotValidType();
		local.setTimestamp(timestamp);
		return new SMSMessageNotValidException((String)NULL, local);
	}
	
	private UnrecognisedPrefixException createUnrecognisedPrefixException() {
		UnrecognisedPrefixType local = new UnrecognisedPrefixType();
		local.setTimestamp(timestamp);
		return new UnrecognisedPrefixException((String)NULL, local);
	}
	
	// Web DTOs
	private FinishCallOnDestinationType createFinishCallOnDestinationType() {
		FinishCallOnDestinationType local = new FinishCallOnDestinationType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private FinishCallType createFinishCallType() {
		FinishCallType local = new FinishCallType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private IncreasePhoneBalanceType createIncreasePhoneBalanceType() {
		IncreasePhoneBalanceType local = new IncreasePhoneBalanceType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private LastMadeCommunicationType createLastMadeCommunicationType() {
		LastMadeCommunicationType local = new LastMadeCommunicationType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private MakeCallType createMakeCallType() {
		MakeCallType local = new MakeCallType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private OperatorDetailedType createOperatorDetailedType() {
		OperatorDetailedType local = new OperatorDetailedType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private OperatorPrefixType createOperatorPrefixType() {
		OperatorPrefixType local = new OperatorPrefixType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private PhoneListType createPhoneListType() {
		PhoneListType local = new PhoneListType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private PhoneNumberType createPhoneNumberType() {
		PhoneNumberType local = new PhoneNumberType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private PhoneStateType createPhoneStateType() {
		PhoneStateType local = new PhoneStateType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private PhoneType createPhoneType() {
		PhoneType local = new PhoneType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private ReceivedSMSListType createReceivedSMSListType() {
		ReceivedSMSListType local = new ReceivedSMSListType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private ReceivedSMSType createReceivedSMSType() {
		ReceivedSMSType local = new ReceivedSMSType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private ReceiveSMSType createReceiveSMSType() {
		ReceiveSMSType local = new ReceiveSMSType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private RegisterPhoneType createRegisterPhoneType() {
		RegisterPhoneType local = new RegisterPhoneType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private SendSMSType createSendSMSType() {
		SendSMSType local = new SendSMSType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	private VoidResponseType createVoidResponseType() {
		VoidResponseType local = new VoidResponseType();
		local.setTimestamp(timestamp);
		return local;
	}
	
	
	/**
	 * This method will fill the object list.
	 */
	public void setUp() {
		objectList.add(createBalanceLimitExceededException());
		objectList.add(createBonusValueNotValidException());
		objectList.add(createCantChangeStateException());
		objectList.add(createCantMakeVideoCallException());
		objectList.add(createCantMakeVoiceCallException());
		objectList.add(createCantReceiveVideoCallException());
		objectList.add(createCantReceiveVoiceCallException());
		objectList.add(createCommunicationErrorException());
		objectList.add(createDurationNotValidException());
		objectList.add(createIncompatiblePrefixException());
		objectList.add(createInvalidAmountException());
		objectList.add(createInvalidCallTypeException());
		objectList.add(createInvalidPhoneTypeException());
		objectList.add(createInvalidStateException());
		objectList.add(createInvalidStateFinishMakingCallException());
		objectList.add(createInvalidStateFinishReceivingCallException());
		objectList.add(createInvalidStateMakeVideoException());
		objectList.add(createInvalidStateMakeVoiceException());
		objectList.add(createInvalidStateReceiveVideoException());
		objectList.add(createInvalidStateReceiveVoiceException());
		objectList.add(createInvalidStateSendSMSException());
		objectList.add(createNoMadeCommunicationException());
		objectList.add(createNotPositiveBalanceException());
		objectList.add(createOperatorNameAlreadyExistsException());
		objectList.add(createOperatorNameNotValidException());
		objectList.add(createOperatorPrefixAlreadyExistsException());
		objectList.add(createOperatorPrefixNotValidException());
		objectList.add(createPhoneAlreadyExistsException());
		objectList.add(createPhoneNotExistsException());
		objectList.add(createPhoneNumberNotValidException());
		objectList.add(createSMSMessageNotValidException());
		objectList.add(createUnrecognisedPrefixException());
		objectList.add(createFinishCallOnDestinationType());
		objectList.add(createFinishCallType());
		objectList.add(createIncreasePhoneBalanceType());
		objectList.add(createLastMadeCommunicationType());
		objectList.add(createMakeCallType());
		objectList.add(createOperatorDetailedType());
		objectList.add(createOperatorPrefixType());
		objectList.add(createPhoneListType());
		objectList.add(createPhoneNumberType());
		objectList.add(createPhoneStateType());
		objectList.add(createPhoneType());
		objectList.add(createReceivedSMSListType());
		objectList.add(createReceivedSMSType());
		objectList.add(createReceiveSMSType());
		objectList.add(createRegisterPhoneType());
		objectList.add(createSendSMSType());
		objectList.add(createVoidResponseType());
	}
	
	public void testTimestamps() {
		Iterator<Object> it = objectList.iterator();
		while(it.hasNext()) {
			Object obj = it.next();
			assertTrue(
					"The timestamp comparison failed in class" + 
					obj.getClass().toString(), 
					timestampMaster.getTimestamp(obj).equals(timestamp));
		}
	}
	
	
}
