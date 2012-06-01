package anacom.services;

import pt.ist.fenixframework.FenixFramework;

import anacom.domain.Communication;
import anacom.domain.Network;
import anacom.shared.dto.LastMadeCommunicationDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.NoMadeCommunication;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;

public class GetLastMadeCommunicationService extends AnacomService {

	private PhoneNumberDTO numberDTO;
	private LastMadeCommunicationDTO lastCommunication;
	
	/**
	 * 	Constructor of GetLastCommunicationService
	 * @param number	the number which we want to know it last communication
	 */
	public GetLastMadeCommunicationService(PhoneNumberDTO number) {
		this.numberDTO = number;
		this.lastCommunication = null;
	}
	
	/**
	 * Get the Last communication of a specific Phone Number
	 * @throws UnrecognisedPrefix	if the Phone's Prefix isn't valid
	 * @throws PhoneNotExists		if the Phone number does not exist
	 * @throws NoMadeCommunication 	if the Phone didn't make any kind of
	 * 								communication yet
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 */
	@Override
	public void dispatch() 
			throws UnrecognisedPrefix, PhoneNotExists, NoMadeCommunication, 
			PhoneNumberNotValid {
		CommunicationRepresentation representation = 
										CommunicationRepresentation.getInstance();
		Network network = (Network) FenixFramework.getRoot();
		Communication target = 
					network.getLastMadeCommunication(this.numberDTO.getNumber());

		//this should not happen
		String communicationType = representation.getUnknownCommunication();
		
		if(representation.getSMSCommunication().
				equals(target.getCommunicationType())) {
			communicationType = representation.getSMSCommunication();
		} else if(representation.getVoiceCommunication().
				equals(target.getCommunicationType())) {
			communicationType = representation.getVoiceCommunication();
		} else if(representation.getVideoCommunication().
				equals(target.getCommunicationType())) { 
			communicationType = representation.getVideoCommunication();
		}
		this.lastCommunication = new LastMadeCommunicationDTO(
				target.getOtherParty(), 
				communicationType,
				target.getCost(), 
				target.getCommunicationLength());
	}
	
	/**
	 *Getter of LastMadeCommunicationDTO  
	 * @return	return the last communication performed by a specific Number
	 */
	public LastMadeCommunicationDTO getLastCommunication() {
		return this.lastCommunication;
	}
}
