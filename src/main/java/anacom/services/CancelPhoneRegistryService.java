package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;

public class CancelPhoneRegistryService extends AnacomService {
	
	private PhoneNumberDTO dto;
	
	public CancelPhoneRegistryService(PhoneNumberDTO dto) { this.dto = dto; }

	/**
	 * Cancels the registrations of a given Phone on the network
	 * @throws PhoneNotExists		if the phone to be removed doesn't exist
	 * @throws UnrecognisedPrefix	if operator with given prefix doesn't exist
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 */
	@Override
	public void dispatch() throws PhoneNotExists, UnrecognisedPrefix, PhoneNumberNotValid {
		Network network = FenixFramework.getRoot();
		network.unregisterPhone(this.dto.getNumber());
	}
}
