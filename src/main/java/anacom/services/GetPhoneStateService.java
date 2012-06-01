package anacom.services;

import anacom.domain.Network;
import anacom.domain.Operator;
import anacom.domain.Phone;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;
import pt.ist.fenixframework.FenixFramework;

public class GetPhoneStateService extends AnacomService {
	
	private PhoneNumberDTO phoneNumberDTO;
	private PhoneStateDTO phoneStateDTO;
	
	/**
	 * Constructor
	 * @param number	the number of the Phone
	 */
	public GetPhoneStateService(PhoneNumberDTO dto) {
		this.phoneNumberDTO = dto;
		this.phoneStateDTO = null;
	}
	
	/**
	 * Gets a State from the number received in the constructor.
	 * Creates a new DTO holding the number and state of the Phone
	 * @throws UnrecognisedPrefix 	if the given prefix does not match any 
	 * 							  	operator in the network
	 * @throws PhoneNotExists		if the Phone with given number doesn't exist
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 */
	@Override
	public void dispatch() 
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid  {
		Network network = FenixFramework.getRoot();
		Operator operator = network.getOperatorFromNumber(this.phoneNumberDTO.getNumber());
		Phone phone = operator.getPhonebyNumber(this.phoneNumberDTO.getNumber());

		PhoneStateRepresentation representation = 
				PhoneStateRepresentation.getInstance();
		
		//this should not happen
		String stateType = representation.getUnknownState();
		
		if(representation.getOnState().
				equals(phone.getState().getStateType())) {
			stateType = representation.getOnState();
		} else if(representation.getOffState().
				equals(phone.getState().getStateType())) {
			stateType = representation.getOffState();
		} else if(representation.getSilentState().
				equals(phone.getState().getStateType())) {
			stateType = representation.getSilentState();
		} else if(representation.getOccupiedState().
				equals(phone.getState().getStateType())) {
			stateType = representation.getOccupiedState();
		}
		
		this.phoneStateDTO = new PhoneStateDTO(
				phone.getNumber(),
				stateType);
	}
	
	public PhoneStateDTO getPhoneStateDTO() {
		return this.phoneStateDTO;
	}
}