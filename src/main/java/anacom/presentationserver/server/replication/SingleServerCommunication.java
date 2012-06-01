package anacom.presentationserver.server.replication;

import java.util.Collection;
import java.util.NoSuchElementException;

import javax.xml.ws.WebServiceException;

import anacom.presentationserver.server.ThreadContext;
import anacom.presentationserver.server.WaitingResponses;
import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.UDDI.QueryServiceException;
import anacom.shared.exceptions.UDDI.UDDICommunicationError;
import anacom.shared.stubs.client.AnacomApplicationServerPortType;
import anacom.shared.stubs.client.BalanceLimitExceededException;
import anacom.shared.stubs.client.BonusValueNotValidException;
import anacom.shared.stubs.client.CantChangeStateException;
import anacom.shared.stubs.client.CantMakeVideoCallException;
import anacom.shared.stubs.client.CantMakeVoiceCallException;
import anacom.shared.stubs.client.CantReceiveVideoCallException;
import anacom.shared.stubs.client.CantReceiveVoiceCallException;
import anacom.shared.stubs.client.CommunicationErrorException;
import anacom.shared.stubs.client.DurationNotValidException;
import anacom.shared.stubs.client.FinishCallOnDestinationType;
import anacom.shared.stubs.client.FinishCallType;
import anacom.shared.stubs.client.IncompatiblePrefixException;
import anacom.shared.stubs.client.IncreasePhoneBalanceType;
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
import anacom.shared.stubs.client.LastMadeCommunicationType;
import anacom.shared.stubs.client.MakeCallType;
import anacom.shared.stubs.client.NoMadeCommunicationException;
import anacom.shared.stubs.client.NotPositiveBalanceException;
import anacom.shared.stubs.client.OperatorDetailedType;
import anacom.shared.stubs.client.OperatorNameAlreadyExistsException;
import anacom.shared.stubs.client.OperatorNameNotValidException;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsException;
import anacom.shared.stubs.client.OperatorPrefixNotValidException;
import anacom.shared.stubs.client.OperatorPrefixType;
import anacom.shared.stubs.client.PhoneAlreadyExistsException;
import anacom.shared.stubs.client.PhoneListType;
import anacom.shared.stubs.client.PhoneNotExistsException;
import anacom.shared.stubs.client.PhoneNumberNotValidException;
import anacom.shared.stubs.client.PhoneNumberType;
import anacom.shared.stubs.client.PhoneStateType;
import anacom.shared.stubs.client.PhoneType;
import anacom.shared.stubs.client.ReceiveSMSType;
import anacom.shared.stubs.client.ReceivedSMSListType;
import anacom.shared.stubs.client.RegisterPhoneType;
import anacom.shared.stubs.client.SMSMessageNotValidException;
import anacom.shared.stubs.client.SendSMSType;
import anacom.shared.stubs.client.UnrecognisedPrefixException;

public class SingleServerCommunication extends WSCommunication {

	@Override
	public void registerOperator(OperatorDetailedType dto,
								String operatorPrefix)
			throws OperatorNameAlreadyExistsException,
				OperatorPrefixAlreadyExistsException,
				OperatorPrefixNotValidException, OperatorNameNotValidException,
				CommunicationErrorException, WebServiceException,
				BonusValueNotValidException, UDDICommunicationError,
				QueryServiceException, CommunicationError {
		
		try {
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			
			
			super.setServer(bindings.iterator().next()).createNewOperator(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.iterator().next()).createNewOperator(dto);				
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

	@Override
	public void registerPhone(RegisterPhoneType dto, String operatorPrefix)
			throws InvalidPhoneTypeException, UnrecognisedPrefixException,
				PhoneAlreadyExistsException, PhoneNumberNotValidException,
				IncompatiblePrefixException, CommunicationErrorException,
				UDDICommunicationError, QueryServiceException,
				WebServiceException, CommunicationError {

		try {		
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			super.setServer(bindings.iterator().next()).registerPhone(dto); 
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.iterator().next()).registerPhone(dto);				
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

	@Override
	public PhoneType getPhoneBalance(PhoneNumberType dto,
									String operatorPrefix)
			throws WebServiceException, PhoneNumberNotValidException,
				UnrecognisedPrefixException, PhoneNotExistsException,
				CommunicationErrorException, UDDICommunicationError,
				QueryServiceException, CommunicationError {
		PhoneType response = null;
		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToRead();
			response = setServer(
					bindings.iterator().next()).getPhoneBalance(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				response = setServer(bindings.iterator().next()).getPhoneBalance(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			} 
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
		return response;
	}

	@Override
	public void increasePhoneBalance(IncreasePhoneBalanceType dto,
									String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
				PhoneNotExistsException, InvalidAmountException,
				BalanceLimitExceededException, CommunicationErrorException,
				WebServiceException, UDDICommunicationError,
				QueryServiceException, CommunicationError {

		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			super.setServer(bindings.iterator().next()).increasePhoneBalance(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.iterator().next()).increasePhoneBalance(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

	@Override
	public PhoneListType listOperatorPhones(OperatorPrefixType dto,
											String operatorPrefix)
			throws UnrecognisedPrefixException, CommunicationErrorException,
				WebServiceException, UDDICommunicationError,
				QueryServiceException, CommunicationError {
		PhoneListType response = null;
		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToRead();
			response = setServer(bindings.iterator().next()).listOperatorPhones(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				response = setServer(bindings.iterator().next()).listOperatorPhones(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
		return response;
	}

	@Override
	public void cancelPhoneRegistry(PhoneNumberType dto, String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
				PhoneNotExistsException, CommunicationErrorException,
				WebServiceException, UDDICommunicationError,
				QueryServiceException, CommunicationError {

		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			setServer(bindings.iterator().next()).cancelPhoneRegistry(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.iterator().next()).cancelPhoneRegistry(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

	@Override
	public ReceiveSMSType sendSMS(SendSMSType dto, String operatorPrefix)
			throws PhoneNumberNotValidException, PhoneNotExistsException,
				NotPositiveBalanceException, UnrecognisedPrefixException,
				InvalidStateSendSMSException, SMSMessageNotValidException,
				InvalidAmountException, CommunicationErrorException,
				WebServiceException, UDDICommunicationError,
				QueryServiceException, CommunicationError {
		ReceiveSMSType response = null;
		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			response = setServer(bindings.iterator().next()).sendSMS(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				response = setServer(bindings.iterator().next()).sendSMS(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
		return response;
	}

	@Override
	public void setPhoneState(PhoneStateType dto, String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
				PhoneNotExistsException, CommunicationErrorException,
				InvalidStateException, CantChangeStateException,
				WebServiceException, UDDICommunicationError,
				QueryServiceException, CommunicationError {

		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			setServer(bindings.iterator().next()).setPhoneState(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.iterator().next()).setPhoneState(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

	@Override
	public PhoneStateType getPhoneState(PhoneNumberType dto,
										String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
				PhoneNotExistsException, CommunicationErrorException,
				WebServiceException, UDDICommunicationError,
				QueryServiceException, CommunicationError {
		PhoneStateType response = null;
		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToRead();
			response = setServer(bindings.iterator().next()).getPhoneState(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				response = setServer(bindings.iterator().next()).getPhoneState(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
		return response;
	}

	@Override
	public ReceivedSMSListType getPhoneSMSReceivedMessages(PhoneNumberType dto,
														String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
				PhoneNotExistsException, CommunicationErrorException,
				WebServiceException, UDDICommunicationError,
				QueryServiceException, CommunicationError {
		ReceivedSMSListType response = null;
		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToRead();
			AnacomApplicationServerPortType port =
				setServer(bindings.iterator().next());
			response = port.getPhoneSMSReceivedMessages(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				response = setServer(bindings.
						iterator().next()).getPhoneSMSReceivedMessages(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}				
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
		return response;
	}

	@Override
	public LastMadeCommunicationType getLastMadeCommunication(
				PhoneNumberType dto,
				String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
				PhoneNotExistsException, CommunicationErrorException,
				WebServiceException, NoMadeCommunicationException,
				UDDICommunicationError, QueryServiceException,
				CommunicationError {
		LastMadeCommunicationType response = null;
		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToRead();
			AnacomApplicationServerPortType port =
				setServer(bindings.iterator().next());
			response = port.getLastMadeCommunication(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				response = setServer(bindings.
						iterator().next()).getLastMadeCommunication(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}		
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
		return response;
	}

	@Override
	public void makeCall(MakeCallType dto, String operatorPrefix)
			throws	UnrecognisedPrefixException,
					PhoneNotExistsException,
					PhoneNumberNotValidException,
					NotPositiveBalanceException,
					InvalidStateMakeVoiceException,
					InvalidStateMakeVideoException,
					CantMakeVoiceCallException,
					CantMakeVideoCallException,
					InvalidCallTypeException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError {

		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			setServer(bindings.iterator().next()).makeCall(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.iterator().next()).makeCall(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

	@Override
	public FinishCallOnDestinationType finishCall(FinishCallType dto,
												String operatorPrefix)
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					DurationNotValidException,
					InvalidStateFinishMakingCallException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError {
		FinishCallOnDestinationType response = null;
		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			response = setServer(bindings.iterator().next()).finishCall(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				response = setServer(bindings.iterator().next()).finishCall(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
		return response;
	}

	@Override
	public void receiveSMS(ReceiveSMSType dto, String operatorPrefix)
			throws	PhoneNumberNotValidException,
					PhoneNotExistsException,
					UnrecognisedPrefixException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError {

		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			setServer(bindings.iterator().next()).receiveSMS(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.iterator().next()).receiveSMS(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

	@Override
	public void receiveCall(MakeCallType dto, String operatorPrefix)
			throws 	UnrecognisedPrefixException,
					PhoneNotExistsException,
					PhoneNumberNotValidException,
					InvalidStateReceiveVoiceException,
					InvalidStateReceiveVideoException,
					CantReceiveVoiceCallException,
					CantReceiveVideoCallException,
					InvalidCallTypeException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError {

		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			setServer(bindings.iterator().next()).receiveCall(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.iterator().next()).receiveCall(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

	@Override
	public void finishCallOnDestination(FinishCallOnDestinationType dto,
										String operatorPrefix)
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					DurationNotValidException,
					InvalidStateFinishReceivingCallException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError {

		try{
			Collection<String> bindings = super.query(operatorPrefix);
			WaitingResponses.getInstance().addRequest(dto.getTimestamp());
			super.setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
			AnacomApplicationServerPortType port =
				setServer(bindings.iterator().next());
		port.finishCallOnDestination(dto);
		} catch (WebServiceException wse) {
			Collection<String> bindings = super.refresh(operatorPrefix);
			try {
				super.setServer(bindings.
						iterator().next()).finishCallOnDestination(dto);
			} catch (NoSuchElementException nsee) {
				throw new CommunicationError(operatorPrefix);
			}					
		} catch (NoSuchElementException nsee) {
			throw new CommunicationError(operatorPrefix);
		}
	}

}
