package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Call;
import anacom.domain.Network;
import anacom.shared.dto.FinishCallOnDestinationDTO;
import anacom.shared.dto.FinishCallDTO;
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;

public class FinishCallOnSourceService extends AnacomService {
	
	private FinishCallDTO finishSourceDTO;
	private FinishCallOnDestinationDTO finishDestinationDTO;
	
	public FinishCallOnSourceService(FinishCallDTO dto) {
		this.finishSourceDTO = dto;
	}
	
	/**
	* @throws UnrecognisedPrefix
	* 		if the prefix is not recognized
    * @throws PhoneNotExists
    * 		if the Phone does not exist
    * @throws DurationNotValid
    * 		if the duration of the call is not a valid one
    * @throws InvalidStateFinishMakingCall
    * 		if the Phone is not currently making a call
    * @throws PhoneNumberNotValid
    * 		if the number isn't a valid number
    */
	@Override
	public void dispatch()
			throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
				InvalidStateFinishMakingCall, PhoneNumberNotValid {
		Network network = FenixFramework.getRoot();
		Call call = network.finishCall(this.finishSourceDTO.getSource(),
									this.finishSourceDTO.getEndTime());
		this.finishDestinationDTO = new FinishCallOnDestinationDTO(
				call.getOtherParty(), 
				call.getDuration(),
				0);
	}
	
	public FinishCallOnDestinationDTO getFinishCallOnDestinationDTO() {
		return this.finishDestinationDTO;
	}
}
