package anacom.presentationserver.server.replication;

import java.util.List;

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

public class GenericComparator {

	/**
	 * Generic method for object comparison.
	 * @param o1		the object t be compared
	 * @param o2		the other object
	 * @return			returns false if objects are not equal AND if the object 
	 * 					type does not match in any case. Fix this if you want.
	 * 					I don't want more exceptions...
	 */
	public boolean equals(Object o1, Object o2) {
		// Web Exceptions
		if((o1 instanceof BalanceLimitExceededException)
				&& (o2 instanceof BalanceLimitExceededException)) {
			BalanceLimitExceededException e1 = (BalanceLimitExceededException) o1;
			BalanceLimitExceededException e2 = (BalanceLimitExceededException) o2;
			BalanceLimitExceededType t1 = e1.getFaultInfo();
			BalanceLimitExceededType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof BonusValueNotValidException)
				&& (o2 instanceof BonusValueNotValidException)){
			BonusValueNotValidException e1 = (BonusValueNotValidException) o1;
			BonusValueNotValidException e2 = (BonusValueNotValidException) o2;
			BonusValueNotValidType t1 = e1.getFaultInfo();
			BonusValueNotValidType t2 = e2.getFaultInfo();
			if(t1.getBonus().equals(t2.getBonus()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof CantChangeStateException)
				&& (o2 instanceof CantChangeStateException)){
			CantChangeStateException e1 = (CantChangeStateException) o1;
			CantChangeStateException e2 = (CantChangeStateException) o2;
			CantChangeStateType t1 = e1.getFaultInfo();
			CantChangeStateType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))
					&& (t1.getCurrentState().equals(t2.getCurrentState()))
					&& (t1.getInvalidState().equals(t2.getInvalidState()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof CantMakeVideoCallException)
				&& (o2 instanceof CantMakeVideoCallException)){
			CantMakeVideoCallException e1 = (CantMakeVideoCallException) o1;
			CantMakeVideoCallException e2 = (CantMakeVideoCallException) o2;
			CantMakeVideoCallType t1 = e1.getFaultInfo();
			CantMakeVideoCallType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof CantMakeVoiceCallException)
				&& (o2 instanceof CantMakeVoiceCallException)){
			CantMakeVoiceCallException e1 = (CantMakeVoiceCallException) o1;
			CantMakeVoiceCallException e2 = (CantMakeVoiceCallException) o2;
			CantMakeVoiceCallType t1 = e1.getFaultInfo();
			CantMakeVoiceCallType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof CantReceiveVideoCallException)
				&& (o2 instanceof CantReceiveVideoCallException)){
			CantReceiveVideoCallException e1 = (CantReceiveVideoCallException) o1;
			CantReceiveVideoCallException e2 = (CantReceiveVideoCallException) o2;
			CantReceiveVideoCallType t1 = e1.getFaultInfo();
			CantReceiveVideoCallType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
		}
			return false;
		}
		
		if((o1 instanceof CantReceiveVoiceCallException)
				&& (o2 instanceof CantReceiveVoiceCallException)){
			CantReceiveVoiceCallException e1 = (CantReceiveVoiceCallException) o1;
			CantReceiveVoiceCallException e2 = (CantReceiveVoiceCallException) o2;
			CantReceiveVoiceCallType t1 = e1.getFaultInfo();
			CantReceiveVoiceCallType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))){
				return true;
			}
			return false;
		}
		
		if((o1 instanceof CommunicationErrorException)
				&& (o2 instanceof CommunicationErrorException)){
			CommunicationErrorException e1 = (CommunicationErrorException) o1;
			CommunicationErrorException e2 = (CommunicationErrorException) o2;
			CommunicationErrorType t1 = e1.getFaultInfo();
			CommunicationErrorType t2 = e2.getFaultInfo();
			if(t1.getService().equals(t2.getService()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))){
				return true;
			}
			return false;
		}
		
		if((o1 instanceof DurationNotValidException)
				&& (o2 instanceof DurationNotValidException)){
			DurationNotValidException e1 = (DurationNotValidException) o1;
			DurationNotValidException e2 = (DurationNotValidException) o2;
			DurationNotValidType t1 = e1.getFaultInfo();
			DurationNotValidType t2 = e2.getFaultInfo();
			if(t1.getOtherParty().equals(t2.getOtherParty())
					&& (t1.getDuration().equals(t2.getDuration()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))){
				return true;
			}
			return false;
		}
		
		if((o1 instanceof IncompatiblePrefixException)
				&& (o2 instanceof IncompatiblePrefixException)){
			IncompatiblePrefixException e1 = (IncompatiblePrefixException) o1;
			IncompatiblePrefixException e2 = (IncompatiblePrefixException) o2;
			IncompatiblePrefixType t1 = e1.getFaultInfo();
			IncompatiblePrefixType t2 = e2.getFaultInfo();
			if(t1.getOperatorprefix().equals(t2.getOperatorprefix())
					&& (t1.getPhoneprefix().equals(t2.getPhoneprefix()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))){
				return true; 
			}
			return false;
		}
		
		if((o1 instanceof InvalidAmountException)
				&& (o2 instanceof InvalidAmountException)){
			InvalidAmountException e1 = (InvalidAmountException) o1;
			InvalidAmountException e2 = (InvalidAmountException) o2;
			InvalidAmountType t1 = e1.getFaultInfo();
			InvalidAmountType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getAmount().equals(t2.getAmount()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))){
				return true;
			}
			return false;
		}

		if((o1 instanceof InvalidCallTypeException)
				&& (o2 instanceof InvalidCallTypeException)){
			InvalidCallTypeException e1 = (InvalidCallTypeException) o1;
			InvalidCallTypeException e2 = (InvalidCallTypeException) o2;
			InvalidCallTypeType t1 = e1.getFaultInfo();
			InvalidCallTypeType t2 = e2.getFaultInfo();

			if(t1.getOtherParty().equals(t2.getOtherParty())
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				if (t1.getType() == null) {
					if (t2.getType() == null) {
						return true;
					}
				} else if (t2.getType() != null
						&& t1.getType().equals(t2.getType())) {
					return true;
				}
			}
			return false;
		}
		
		if((o1 instanceof InvalidPhoneTypeException)
				&& (o2 instanceof InvalidPhoneTypeException)){
			InvalidPhoneTypeException e1 = (InvalidPhoneTypeException) o1;
			InvalidPhoneTypeException e2 = (InvalidPhoneTypeException) o2;
			InvalidPhoneTypeType t1 = e1.getFaultInfo();
			InvalidPhoneTypeType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber())
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				if (t1.getType() == null) {
					if (t2.getType() == null) {
						return true;
					}
				} else if (t2.getType() != null
						&& t1.getType().equals(t2.getType())) {
					return true;
				}
			}
			return false;
		}
		
		if((o1 instanceof InvalidStateException)
				&& (o2 instanceof InvalidStateException)){
			InvalidStateException e1 = (InvalidStateException) o1;
			InvalidStateException e2 = (InvalidStateException) o2;
			InvalidStateType t1 = e1.getFaultInfo();
			InvalidStateType t2 = e2.getFaultInfo();
			if (t1.getNumber().equals(t2.getNumber())
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				if (t1.getState() == null) {
					if (t2.getState() == null) {
						return true;
					}
				} else if (t2.getState() != null
						&& t1.getState().equals(t2.getState())) {
					return true;
				}
			}
			return false;
		}
		
		if((o1 instanceof InvalidStateFinishMakingCallException)
				&& (o2 instanceof InvalidStateFinishMakingCallException)){
			InvalidStateFinishMakingCallException e1 = (InvalidStateFinishMakingCallException) o1;
			InvalidStateFinishMakingCallException e2 = (InvalidStateFinishMakingCallException) o2;
			InvalidStateFinishMakingCallType t1 = e1.getFaultInfo();
			InvalidStateFinishMakingCallType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getState().equals(t2.getState()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof InvalidStateFinishReceivingCallException)
				&& (o2 instanceof InvalidStateFinishReceivingCallException)){
			InvalidStateFinishReceivingCallException e1 = (InvalidStateFinishReceivingCallException) o1;
			InvalidStateFinishReceivingCallException e2 = (InvalidStateFinishReceivingCallException) o2;
			InvalidStateFinishReceivingCallType t1 = e1.getFaultInfo();
			InvalidStateFinishReceivingCallType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber())
					&& (t1.getState().equals(t2.getState()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
	
		if((o1 instanceof InvalidStateMakeVideoException)
				&& (o2 instanceof InvalidStateMakeVideoException)){
			InvalidStateMakeVideoException e1 = (InvalidStateMakeVideoException) o1;
			InvalidStateMakeVideoException e2 = (InvalidStateMakeVideoException) o2;
			InvalidStateMakeVideoType t1 = e1.getFaultInfo();
			InvalidStateMakeVideoType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber())
					&& (t1.getState().equals(t2.getState()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof InvalidStateMakeVoiceException)
				&& (o2 instanceof InvalidStateMakeVoiceException)){
			InvalidStateMakeVoiceException e1 = (InvalidStateMakeVoiceException) o1;
			InvalidStateMakeVoiceException e2 = (InvalidStateMakeVoiceException) o2;
			InvalidStateMakeVoiceType t1 = e1.getFaultInfo();
			InvalidStateMakeVoiceType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber())
					&& (t1.getState().equals(t2.getState()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof InvalidStateReceiveVideoException)
				&& (o2 instanceof InvalidStateReceiveVideoException)){
			InvalidStateReceiveVideoException e1 = (InvalidStateReceiveVideoException) o1;
			InvalidStateReceiveVideoException e2 = (InvalidStateReceiveVideoException) o2;
			InvalidStateReceiveVideoType t1 = e1.getFaultInfo();
			InvalidStateReceiveVideoType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getState().equals(t2.getState()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof InvalidStateReceiveVoiceException)
				&& (o2 instanceof InvalidStateReceiveVoiceException)){
			InvalidStateReceiveVoiceException e1 = (InvalidStateReceiveVoiceException) o1;
			InvalidStateReceiveVoiceException e2 = (InvalidStateReceiveVoiceException) o2;
			InvalidStateReceiveVoiceType t1 = e1.getFaultInfo();
			InvalidStateReceiveVoiceType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber())
					&& (t1.getState().equals(t2.getState()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof InvalidStateSendSMSException)
				&& (o2 instanceof InvalidStateSendSMSException)){
			InvalidStateSendSMSException e1 = (InvalidStateSendSMSException) o1;
			InvalidStateSendSMSException e2 = (InvalidStateSendSMSException) o2;
			InvalidStateSendSMSType t1 = e1.getFaultInfo();
			InvalidStateSendSMSType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber())
					&& (t1.getState().equals(t2.getState()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof NoMadeCommunicationException)
				&& (o2 instanceof NoMadeCommunicationException)){
			NoMadeCommunicationException e1 = (NoMadeCommunicationException) o1;
			NoMadeCommunicationException e2 = (NoMadeCommunicationException) o2;
			NoMadeCommunicationType t1 = e1.getFaultInfo();
			NoMadeCommunicationType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof NotPositiveBalanceException)
				&& (o2 instanceof NotPositiveBalanceException)){
			NotPositiveBalanceException e1 = (NotPositiveBalanceException) o1;
			NotPositiveBalanceException e2 = (NotPositiveBalanceException) o2;
			NotPositiveBalanceType t1 = e1.getFaultInfo();
			NotPositiveBalanceType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getBalance().equals(t2.getBalance()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof OperatorNameAlreadyExistsException)
				&& (o2 instanceof OperatorNameAlreadyExistsException)){
			OperatorNameAlreadyExistsException e1 = (OperatorNameAlreadyExistsException) o1;
			OperatorNameAlreadyExistsException e2 = (OperatorNameAlreadyExistsException) o2;
			OperatorNameAlreadyExistsType t1 = e1.getFaultInfo();
			OperatorNameAlreadyExistsType t2 = e2.getFaultInfo();
			if(t1.getName().equals(t2.getName()) 
					&& (t1.getPrefix().equals(t2.getPrefix()))
					&& (t1.getSmsCost().equals(t2.getSmsCost()))
					&& (t1.getVoiceCost().equals(t2.getVoiceCost()))
					&& (t1.getVideoCost().equals(t2.getVideoCost()))
					&& (t1.getTax().equals(t2.getTax()))
					&& (t1.getBonus().equals(t2.getBonus()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof OperatorNameNotValidException)
				&& (o2 instanceof OperatorNameNotValidException)){
			OperatorNameNotValidException e1 = (OperatorNameNotValidException) o1;
			OperatorNameNotValidException e2 = (OperatorNameNotValidException) o2;
			OperatorNameNotValidType t1 = e1.getFaultInfo();
			OperatorNameNotValidType t2 = e2.getFaultInfo();
			if(t1.getTimestamp().equals(t2.getTimestamp())) {
				if (t1.getName() == null) {
					if (t2.getName() == null) {
						return true;
					}
				} else if (t2.getName() != null
						&& t1.getName().equals(t2.getName())) {
					return true;
				}
			}
			return false;
		}
		
		if((o1 instanceof OperatorPrefixAlreadyExistsException)
				&& (o2 instanceof OperatorPrefixAlreadyExistsException)){
			OperatorPrefixAlreadyExistsException e1 = (OperatorPrefixAlreadyExistsException) o1;
			OperatorPrefixAlreadyExistsException e2 = (OperatorPrefixAlreadyExistsException) o2;
			OperatorPrefixAlreadyExistsType t1 = e1.getFaultInfo();
			OperatorPrefixAlreadyExistsType t2 = e2.getFaultInfo();
			if(t1.getName().equals(t2.getName()) 
					&& (t1.getPrefix().equals(t2.getPrefix()))
					&& (t1.getSmsCost().equals(t2.getSmsCost()))
					&& (t1.getVoiceCost().equals(t2.getVoiceCost()))
					&& (t1.getVideoCost().equals(t2.getVideoCost()))
					&& (t1.getTax().equals(t2.getTax()))
					&& (t1.getBonus().equals(t2.getBonus()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof OperatorPrefixNotValidException)
				&& (o2 instanceof OperatorPrefixNotValidException)){
			OperatorPrefixNotValidException e1 = (OperatorPrefixNotValidException) o1;
			OperatorPrefixNotValidException e2 = (OperatorPrefixNotValidException) o2;
			OperatorPrefixNotValidType t1 = e1.getFaultInfo();
			OperatorPrefixNotValidType t2 = e2.getFaultInfo();
			if(t1.getTimestamp().equals(t2.getTimestamp())) {
				if (t1.getPrefix() == null) {
					if (t2.getPrefix() == null) {
						return true;
					}
				} else if (t2.getPrefix() != null
						&& t1.getPrefix().equals(t2.getPrefix())) {
					return true;
				}
			}
			return false;
		}
		
		if((o1 instanceof PhoneAlreadyExistsException)
				&& (o2 instanceof PhoneAlreadyExistsException)){
			PhoneAlreadyExistsException e1 = (PhoneAlreadyExistsException) o1;
			PhoneAlreadyExistsException e2 = (PhoneAlreadyExistsException) o2;
			PhoneAlreadyExistsType t1 = e1.getFaultInfo();
			PhoneAlreadyExistsType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))){
				return true;
			}
			return false;
		}
		
		if((o1 instanceof PhoneNotExistsException)
				&& (o2 instanceof PhoneNotExistsException)){
			PhoneNotExistsException e1 = (PhoneNotExistsException) o1;
			PhoneNotExistsException e2 = (PhoneNotExistsException) o2;
			PhoneNotExistsType t1 = e1.getFaultInfo();
			PhoneNotExistsType t2 = e2.getFaultInfo();
			if(t1.getNumber().equals(t2.getNumber()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))){
				return true;
			}
			return false;
		}
		
		if((o1 instanceof PhoneNumberNotValidException)
				&& (o2 instanceof PhoneNumberNotValidException)){
			PhoneNumberNotValidException e1 = (PhoneNumberNotValidException) o1;
			PhoneNumberNotValidException e2 = (PhoneNumberNotValidException) o2;
			PhoneNumberNotValidType t1 = e1.getFaultInfo();
			PhoneNumberNotValidType t2 = e2.getFaultInfo();
			if(t1.getTimestamp().equals(t2.getTimestamp())) {
				if (t1.getNumber() == null) {
					if (t2.getNumber() == null) {
						return true;
					}
				} else if (t2.getNumber() != null
						&& t1.getNumber().equals(t2.getNumber())) {
					return true;
				}
			}
			return false;
		}
		
		if((o1 instanceof SMSMessageNotValidException)
				&& (o2 instanceof SMSMessageNotValidException)){
			SMSMessageNotValidException e1 = (SMSMessageNotValidException) o1;
			SMSMessageNotValidException e2 = (SMSMessageNotValidException) o2;
			SMSMessageNotValidType t1 = e1.getFaultInfo();
			SMSMessageNotValidType t2 = e2.getFaultInfo();
			if(t1.getOtherParty().equals(t2.getOtherParty())
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				if (t1.getMessage() == null) {
					if (t2.getMessage() == null) {
						return true;
					}
				} else if (t2.getMessage() != null
						&& t1.getMessage().equals(t2.getMessage())) {
					return true;
				}
			}
			return false;
		}
		
		if((o1 instanceof UnrecognisedPrefixException)
				&& (o2 instanceof UnrecognisedPrefixException)){
			UnrecognisedPrefixException e1 = (UnrecognisedPrefixException) o1;
			UnrecognisedPrefixException e2 = (UnrecognisedPrefixException) o2;
			UnrecognisedPrefixType t1 = e1.getFaultInfo();
			UnrecognisedPrefixType t2 = e2.getFaultInfo();
			if(t1.getOperatorPrefix().equals(t2.getOperatorPrefix()) 
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		// Web DTOs
		if((o1 instanceof FinishCallOnDestinationType)
				&& (o2 instanceof FinishCallOnDestinationType)) {
			FinishCallOnDestinationType t1 = (FinishCallOnDestinationType) o1;
			FinishCallOnDestinationType t2 = (FinishCallOnDestinationType) o2;
			if((t1.getCost().equals(t2.getCost()))
					&& (t1.getDuration().equals(t2.getDuration()))
					&& (t1.getDestination().equals(t2.getDestination()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof FinishCallType)
				&& (o2 instanceof FinishCallType)){
			FinishCallType t1 = (FinishCallType) o1;
			FinishCallType t2 = (FinishCallType) o2;
			if((t1.getSource().equals(t2.getSource()))
					&& (t1.getEndTime().equals(t2.getEndTime()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof IncreasePhoneBalanceType)
				&& (o2 instanceof IncreasePhoneBalanceType)){
			IncreasePhoneBalanceType t1 = (IncreasePhoneBalanceType) o1;
			IncreasePhoneBalanceType t2 = (IncreasePhoneBalanceType) o2;
			if((t1.getNumber().equals(t2.getNumber()))
					&& (t1.getAmount().equals(t2.getAmount()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof LastMadeCommunicationType)
				&& (o2 instanceof LastMadeCommunicationType)){
			LastMadeCommunicationType t1 = (LastMadeCommunicationType) o1;
			LastMadeCommunicationType t2 = (LastMadeCommunicationType) o2;
			if((t1.getDestination().equals(t2.getDestination()))
					&& (t1.getCommunicationType().equals(t2.getCommunicationType()))
					&& (t1.getCost().equals(t2.getCost()))
					&& (t1.getTotal().equals(t2.getTotal()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof MakeCallType)
				&& (o2 instanceof MakeCallType)){
			MakeCallType t1 = (MakeCallType) o1;
			MakeCallType t2 = (MakeCallType) o2;
			if((t1.getSource().equals(t2.getSource()))
					&& (t1.getDestination().equals(t2.getDestination()))
					&& (t1.getType().equals(t2.getType()))
					&& (t1.getStartTime().equals(t2.getStartTime()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof OperatorDetailedType)
				&& (o2 instanceof OperatorDetailedType)){
			OperatorDetailedType t1 = (OperatorDetailedType) o1;
			OperatorDetailedType t2 = (OperatorDetailedType) o2;
			if(t1.getName().equals(t2.getName()) 
					&& (t1.getPrefix().equals(t2.getPrefix()))
					&& (t1.getSmsCost().equals(t2.getSmsCost()))
					&& (t1.getVoiceCost().equals(t2.getVoiceCost()))
					&& (t1.getVideoCost().equals(t2.getVideoCost()))
					&& (t1.getTax().equals(t2.getTax()))
					&& (t1.getBonus().equals(t2.getBonus()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof OperatorPrefixType)
				&& (o2 instanceof OperatorPrefixType)){
			OperatorPrefixType t1 = (OperatorPrefixType) o1;
			OperatorPrefixType t2 = (OperatorPrefixType) o2;
			if((t1.getPrefix().equals(t2.getPrefix()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof PhoneListType)
				&& (o2 instanceof PhoneListType)){
			PhoneListType t1 = (PhoneListType) o1;
			PhoneListType t2 = (PhoneListType) o2;
			List<PhoneType> t1PhoneList = t1.getPhoneDTOList();
			List<PhoneType> t2PhoneList = t2.getPhoneDTOList();
			
			if(!t1.getTimestamp().equals(t2.getTimestamp())
					|| !(t1PhoneList.size() == t2PhoneList.size())) {
				return false;
			}
			for(int i = 0; i < t1PhoneList.size(); i++){
					if(!t1PhoneList.get(i).getNumber().equals(t2PhoneList.get(i).getNumber())
							|| !t1PhoneList.get(i).getBalance().equals(t2PhoneList.get(i).getBalance())
							|| !t1PhoneList.get(i).getTimestamp().equals(t2PhoneList.get(i).getTimestamp())) {
							return false;
					}
				}
				return true;
		}
		
		if((o1 instanceof PhoneNumberType)
				&& (o2 instanceof PhoneNumberType)){
			PhoneNumberType t1 = (PhoneNumberType) o1;
			PhoneNumberType t2 = (PhoneNumberType) o2;
			if((t1.getNumber().equals(t2.getNumber()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		//CORRIGIDO
		if((o1 instanceof PhoneStateType)
				&& (o2 instanceof PhoneStateType)){
			PhoneStateType t1 = (PhoneStateType) o1;
			PhoneStateType t2 = (PhoneStateType) o2;
			if((t1.getNumber().equals(t2.getNumber()))
					&& (t1.getState().equals(t2.getState()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		// CORRIGIDO
		if((o1 instanceof PhoneType)
				&& (o2 instanceof PhoneType)){
			PhoneType t1 = (PhoneType) o1;
			PhoneType t2 = (PhoneType) o2;
			
			if((t1.getNumber().equals(t2.getNumber()))
					&& (t1.getBalance().equals(t2.getBalance()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
				return true;
			}
			return false;
		}
		
		if((o1 instanceof ReceivedSMSListType)
				&& (o2 instanceof ReceivedSMSListType))		{
			ReceivedSMSListType t1 = (ReceivedSMSListType) o1;
			ReceivedSMSListType t2 = (ReceivedSMSListType) o2;
			List<ReceivedSMSType> t1ReceivedSMSList = t1.getSMSDTOList();
			List<ReceivedSMSType> t2ReceivedSMSList = t2.getSMSDTOList();
			
			if(!t1.getNumber().equals(t2.getNumber())
					|| !t1.getTimestamp().equals(t2.getTimestamp())
					|| !(t1ReceivedSMSList.size() == t2ReceivedSMSList.size()))
				return false;
			for(int i = 0; i < t1ReceivedSMSList.size(); i++){
				if(!t1ReceivedSMSList.get(i).getMessage().equals(t2ReceivedSMSList.get(i).getMessage())
						|| !t1ReceivedSMSList.get(i).getSource().equals(t2ReceivedSMSList.get(i).getSource())
						|| !t1ReceivedSMSList.get(i).getCost().equals(t2ReceivedSMSList.get(i).getCost())
						|| !t1ReceivedSMSList.get(i).getTimestamp().equals(t2ReceivedSMSList.get(i).getTimestamp())) {
					return false;
				}
			}			
					return true;
		}
		
		if((o1 instanceof ReceivedSMSType)
				&& (o2 instanceof ReceivedSMSType)){
			ReceivedSMSType t1 = (ReceivedSMSType) o1;
			ReceivedSMSType t2 = (ReceivedSMSType) o2;
			if((t1.getMessage().equals(t2.getMessage()))
					&& (t1.getSource().equals(t2.getSource()))
					&& (t1.getCost().equals(t2.getCost()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof ReceiveSMSType)
				&& (o2 instanceof ReceiveSMSType)){
			ReceiveSMSType t1 = (ReceiveSMSType) o1;
			ReceiveSMSType t2 = (ReceiveSMSType) o2;
			if((t1.getMessage().equals(t2.getMessage()))
					&& (t1.getSource().equals(t2.getSource()))
					&& (t1.getDestination().equals(t2.getDestination()))
					&& (t1.getCost().equals(t2.getCost()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof RegisterPhoneType)
				&& (o2 instanceof RegisterPhoneType)){
			RegisterPhoneType t1 = (RegisterPhoneType) o1;
			RegisterPhoneType t2 = (RegisterPhoneType) o2;
			if((t1.getType().equals(t2.getType()))
					&& (t1.getNumber().equals(t2.getNumber()))
					&& (t1.getOpPrefix().equals(t2.getOpPrefix()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof SendSMSType)
				&& (o2 instanceof SendSMSType)){
			SendSMSType t1 = (SendSMSType) o1;
			SendSMSType t2 = (SendSMSType) o2;
			if((t1.getMessage().equals(t2.getMessage()))
					&& (t1.getSource().equals(t2.getSource()))
					&& (t1.getDestination().equals(t2.getDestination()))
					&& (t1.getTimestamp().equals(t2.getTimestamp()))) {
					return true;
			}
			return false;
		}
		
		if((o1 instanceof VoidResponseType)
				&& (o2 instanceof VoidResponseType)){
			VoidResponseType t1 = (VoidResponseType) o1;
			VoidResponseType t2 = (VoidResponseType) o2;
			if((t1.getTimestamp().equals(t2.getTimestamp()))){
					return true;
			}
			return false;
		}
		
		return false;
	}
}