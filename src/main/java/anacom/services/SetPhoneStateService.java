package anacom.services;

import anacom.domain.Network;
import pt.ist.fenixframework.FenixFramework;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.CantChangeState;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidState;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

public class SetPhoneStateService extends AnacomService {

	private PhoneStateDTO phoneStateDTO;
	
	/**
	 * Set the state of a specific phone
	 * @param dto the phone number and the phone state
	 */
	public SetPhoneStateService(PhoneStateDTO dto) {
		this.phoneStateDTO = dto;
	}

	/**
	 * Changes the state of the Phone with the given Number to the given State
	 * @throws UnrecognisedPrefix 	if the given prefix does not match any 
	 * 							  	operator in the network
	 * @throws PhoneNotExists		if the Phone with given number doesn't exist
	 * @throws InvalidState			if the State received in the constructor is not valid
	 * @throws CantChangeState 		if the new state in'st invalid
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 */
	@Override
	public void dispatch() 
			throws 	UnrecognisedPrefix, 
					PhoneNotExists, 
					InvalidState, 
					CantChangeState, 
					PhoneNumberNotValid {
		
		Network network = FenixFramework.getRoot();
		PhoneStateRepresentation _category = PhoneStateRepresentation.getInstance();

		if(this.phoneStateDTO.getState().equals(_category.getOnState())) {
			network.turnOnPhone(this.phoneStateDTO.getNumber());
		} else if(this.phoneStateDTO.getState().equals(_category.getOffState())) {
			network.turnOffPhone(this.phoneStateDTO.getNumber());
		} else if(this.phoneStateDTO.getState().equals(_category.getSilentState())) {
			network.silencePhone(this.phoneStateDTO.getNumber());
		} else {
			throw new InvalidState(this.phoneStateDTO.getNumber(), this.phoneStateDTO.getState());
		}
	}
}