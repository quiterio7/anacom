package anacom.presentationserver.server.replication;

import javax.xml.ws.WebServiceException;

import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.UDDI.QueryServiceException;
import anacom.shared.exceptions.UDDI.UDDICommunicationError;
import anacom.shared.stubs.client.BalanceLimitExceededException;
import anacom.shared.stubs.client.BonusValueNotValidException;
import anacom.shared.stubs.client.CantChangeStateException;
import anacom.shared.stubs.client.CantMakeVideoCallException;
import anacom.shared.stubs.client.CantMakeVoiceCallException;
import anacom.shared.stubs.client.CantReceiveVideoCallException;
import anacom.shared.stubs.client.CantReceiveVoiceCallException;
import anacom.shared.stubs.client.CommunicationErrorException;
import anacom.shared.stubs.client.DurationNotValidException;
import anacom.shared.stubs.client.IncompatiblePrefixException;
import anacom.shared.stubs.client.InvalidAmountException;
import anacom.shared.stubs.client.InvalidCallTypeException;
import anacom.shared.stubs.client.InvalidPhoneTypeException;
import anacom.shared.stubs.client.InvalidStateException;
import anacom.shared.stubs.client.InvalidStateFinishMakingCallException;
import anacom.shared.stubs.client.InvalidStateFinishReceivingCallException;
import anacom.shared.stubs.client.InvalidStateMakeVideoException;
import anacom.shared.stubs.client.InvalidStateMakeVoiceException;
import anacom.shared.stubs.client.InvalidStateReceiveVideoException;
import anacom.shared.stubs.client.InvalidStateReceiveVoiceException;
import anacom.shared.stubs.client.InvalidStateSendSMSException;
import anacom.shared.stubs.client.NoMadeCommunicationException;
import anacom.shared.stubs.client.NotPositiveBalanceException;
import anacom.shared.stubs.client.OperatorNameAlreadyExistsException;
import anacom.shared.stubs.client.OperatorNameNotValidException;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsException;
import anacom.shared.stubs.client.OperatorPrefixNotValidException;
import anacom.shared.stubs.client.PhoneAlreadyExistsException;
import anacom.shared.stubs.client.PhoneNotExistsException;
import anacom.shared.stubs.client.PhoneNumberNotValidException;
import anacom.shared.stubs.client.SMSMessageNotValidException;
import anacom.shared.stubs.client.UnrecognisedPrefixException;

public class ExceptionFactory {

	void createRegiterOperatorException(Object obj) 
			throws 	OperatorNameAlreadyExistsException, 
					CommunicationErrorException, 
					OperatorPrefixAlreadyExistsException, 
					OperatorPrefixNotValidException, 
					OperatorNameNotValidException, 
					BonusValueNotValidException {
		if(obj instanceof OperatorNameAlreadyExistsException)
			throw (OperatorNameAlreadyExistsException) obj;
		if(obj instanceof OperatorPrefixAlreadyExistsException)
			throw (OperatorPrefixAlreadyExistsException) obj;
		if(obj instanceof OperatorPrefixNotValidException)
			throw (OperatorPrefixNotValidException) obj;
		if(obj instanceof OperatorNameNotValidException)
			throw (OperatorNameNotValidException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof BonusValueNotValidException)
			throw (BonusValueNotValidException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createRegisterPhoneException(Object obj) 
			throws 	InvalidPhoneTypeException, 
					UnrecognisedPrefixException, 
					PhoneAlreadyExistsException, 
					PhoneNumberNotValidException, 
					IncompatiblePrefixException, 
					CommunicationErrorException{
		if(obj instanceof InvalidPhoneTypeException)
			throw (InvalidPhoneTypeException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneAlreadyExistsException)
			throw (PhoneAlreadyExistsException) obj;
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof IncompatiblePrefixException)
			throw (IncompatiblePrefixException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
	}
	
	void createGetPhoneBalanceException(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException{
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createIncreasePhoneBalanceException(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					InvalidAmountException, 
					BalanceLimitExceededException, 
					CommunicationErrorException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof InvalidAmountException)
			throw (InvalidAmountException) obj;
		if(obj instanceof BalanceLimitExceededException)
			throw (BalanceLimitExceededException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createListoperatorPhonesException(Object obj) 
			throws 	UnrecognisedPrefixException, 
					CommunicationErrorException{
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createCancelPhoneRegistryException(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createSendSMSException(Object obj) 
			throws 	PhoneNumberNotValidException, 
					PhoneNotExistsException, 	
					NotPositiveBalanceException, 
					UnrecognisedPrefixException, 
					InvalidStateSendSMSException, 
					SMSMessageNotValidException, 
					InvalidAmountException, 
					CommunicationErrorException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof NotPositiveBalanceException)
			throw (NotPositiveBalanceException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof InvalidStateSendSMSException)
			throw (InvalidStateSendSMSException) obj;
		if(obj instanceof SMSMessageNotValidException)
			throw (SMSMessageNotValidException) obj;
		if(obj instanceof InvalidAmountException)
			throw (InvalidAmountException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createSetPhoneStateException(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException, 
					InvalidStateException, 
					CantChangeStateException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof InvalidStateException)
			throw (InvalidStateException) obj;
		if(obj instanceof CantChangeStateException)
			throw (CantChangeStateException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createGetPhoneState(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createGetPhoneSMSReceivedMessage(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createGetLastMadeCommunication(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					CommunicationErrorException, 
					NoMadeCommunicationException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof NoMadeCommunicationException)
			throw (NoMadeCommunicationException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
	}
	
	void createMakeCall(Object obj) 
			throws 	UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					PhoneNumberNotValidException, 
					NotPositiveBalanceException, 
					InvalidStateMakeVoiceException, 
					InvalidStateMakeVideoException, 
					CantMakeVoiceCallException, 
					CantMakeVideoCallException, 
					InvalidCallTypeException, 
					CommunicationErrorException{
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof NotPositiveBalanceException)
			throw (NotPositiveBalanceException) obj;
		if(obj instanceof InvalidStateMakeVoiceException)
			throw (InvalidStateMakeVoiceException) obj;
		if(obj instanceof InvalidStateMakeVideoException)
			throw (InvalidStateMakeVideoException) obj;
		if(obj instanceof CantMakeVoiceCallException)
			throw (CantMakeVoiceCallException) obj;
		if(obj instanceof CantMakeVideoCallException)
			throw (CantMakeVideoCallException) obj;
		if(obj instanceof InvalidCallTypeException)
			throw (InvalidCallTypeException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
		if(obj instanceof CommunicationError)
			throw (CommunicationError) obj;
	}
	
	void createFinishCall(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					DurationNotValidException, 
					InvalidStateFinishMakingCallException, 
					CommunicationErrorException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof DurationNotValidException)
			throw (DurationNotValidException) obj;
		if(obj instanceof InvalidStateFinishMakingCallException)
			throw (InvalidStateFinishMakingCallException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
		if(obj instanceof CommunicationError)
			throw (CommunicationError) obj;
	}
	
	void createReceiveSMS(Object obj) 	
			throws 	PhoneNumberNotValidException, 
					PhoneNotExistsException, 
					UnrecognisedPrefixException, 
					CommunicationErrorException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
		if(obj instanceof CommunicationError)
			throw (CommunicationError) obj;
	}
	
	void createReceiveCall(Object obj) 
			throws 	UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					PhoneNumberNotValidException, 
					InvalidStateReceiveVoiceException, 
					InvalidStateReceiveVideoException, 
					CantReceiveVoiceCallException, 
					CantReceiveVideoCallException, 
					InvalidCallTypeException, 
					CommunicationErrorException{
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof InvalidStateReceiveVoiceException)
			throw (InvalidStateReceiveVoiceException) obj;
		if(obj instanceof InvalidStateReceiveVideoException)
			throw (InvalidStateReceiveVideoException) obj;
		if(obj instanceof CantReceiveVoiceCallException)
			throw (CantReceiveVoiceCallException) obj;
		if(obj instanceof CantReceiveVideoCallException)
			throw (CantReceiveVideoCallException) obj;
		if(obj instanceof InvalidCallTypeException)
			throw (InvalidCallTypeException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
		if(obj instanceof CommunicationError)
			throw (CommunicationError) obj;
	}
	
	void createFinishCallOnDestination(Object obj) 
			throws 	PhoneNumberNotValidException, 
					UnrecognisedPrefixException, 
					PhoneNotExistsException, 
					DurationNotValidException, 
					InvalidStateFinishReceivingCallException, 
					CommunicationErrorException{
		if(obj instanceof PhoneNumberNotValidException)
			throw (PhoneNumberNotValidException) obj;
		if(obj instanceof UnrecognisedPrefixException)
			throw (UnrecognisedPrefixException) obj;
		if(obj instanceof PhoneNotExistsException)
			throw (PhoneNotExistsException) obj;
		if(obj instanceof DurationNotValidException)
			throw (DurationNotValidException) obj;
		if(obj instanceof InvalidStateFinishReceivingCallException)
			throw (InvalidStateFinishReceivingCallException) obj;
		if(obj instanceof CommunicationErrorException)
			throw (CommunicationErrorException) obj;
		if(obj instanceof WebServiceException)
			throw (WebServiceException) obj;
		if(obj instanceof UDDICommunicationError)
			throw (UDDICommunicationError) obj;
		if(obj instanceof QueryServiceException)
			throw (QueryServiceException) obj;
		if(obj instanceof CommunicationError)
			throw (CommunicationError) obj;
	}
	
}
