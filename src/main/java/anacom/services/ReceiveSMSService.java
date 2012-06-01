package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.SMSDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;

public class ReceiveSMSService extends AnacomService {

	private SMSDTO smsDTO;
	
	public ReceiveSMSService(SMSDTO dto) { 
		this.smsDTO = dto; 
	}
 
	/**
     * @throws UnrecognisedPrefix	if the prefix is not recognized	
     * @throws PhoneNotExists		if the phone does not exist (destination)
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
	@Override
	public void dispatch() throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
		Network network = FenixFramework.getRoot();
		network.receiveSms(
				smsDTO.getMessage(), 
				smsDTO.getSource(), 
				smsDTO.getDestination(), 
				smsDTO.getCost());
	}
}
