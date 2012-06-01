package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.FinishCallOnDestinationDTO;
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall;

public class FinishCallOnDestinationService extends AnacomService {
	
	private FinishCallOnDestinationDTO dto;
	
	public FinishCallOnDestinationService(FinishCallOnDestinationDTO dto) {
		this.dto = dto;
	}
	
	/**
	* @throws UnrecognisedPrefix 			if the prefix is not recognized
    * @throws PhoneNotExists 				if the Phone does not exist
    * @throws DurationNotValid				if the duration of the call is not a valid one
    * @throws InvalidStateFinishReceivingCall	if the Phone is not currently making a call
    * @throws PhoneNumberNotValid if the number isn't valid in Network Context
    */
	@Override
	public void dispatch()
			throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
				InvalidStateFinishReceivingCall, PhoneNumberNotValid {
		Network network = FenixFramework.getRoot();
		network.finishCall(
				this.dto.getDestination(),
				this.dto.getDuration(),
				this.dto.getCost());
	}
}
