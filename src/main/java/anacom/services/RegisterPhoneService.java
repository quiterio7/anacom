package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.RegisterPhoneDTO;
import anacom.shared.exceptions.IncompatiblePrefix;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.InvalidPhoneType;
import anacom.shared.exceptions.phone.PhoneAlreadyExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.misc.externRepresentation.phoneType.PhoneTypeRepresentation;

public class RegisterPhoneService extends AnacomService {
	
	private RegisterPhoneDTO registerPhoneDTO;
	
	public RegisterPhoneService(RegisterPhoneDTO dto) {
		this.registerPhoneDTO = dto; 
	}
	
    /**
     * @throws InvalidPhoneType		if the given Phone type is invalid
     * @throws UnrecognisedPrefix	if the phone's prefix does not exist in the network
     * @throws PhoneAlreadyExists	if the phone's number already exists in the network
     * @throws PhoneNumberNotValid	if the phone's number isn't valid
     * @throws IncompatiblePrefix	operator's prefix doesn't match phone's prefix
     */
	@Override
	public void dispatch() 
			throws InvalidPhoneType, UnrecognisedPrefix, PhoneAlreadyExists,
				PhoneNumberNotValid, IncompatiblePrefix {
		Network network = FenixFramework.getRoot();
		PhoneTypeRepresentation types = PhoneTypeRepresentation.getInstance();
		if (this.registerPhoneDTO.getType().equals(types.get2GType())) {
			network.registerPhone2G(this.registerPhoneDTO.getOperatorPrefix(), this.registerPhoneDTO.getNumber());
		} else if (this.registerPhoneDTO.getType().equals(types.get3GType())) {
			network.registerPhone3G(this.registerPhoneDTO.getOperatorPrefix(), this.registerPhoneDTO.getNumber());
		} else {
			throw new InvalidPhoneType(this.registerPhoneDTO.getNumber(), this.registerPhoneDTO.getType());
		}
	}
	
}
