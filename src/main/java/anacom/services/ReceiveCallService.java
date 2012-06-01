package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.CallDTO;
import anacom.shared.exceptions.communication.InvalidCallType;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.CantReceiveVideoCall;
import anacom.shared.exceptions.phone.CantReceiveVoiceCall;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;

public class ReceiveCallService extends AnacomService {

	private CallDTO callDTO;
	
	public ReceiveCallService(CallDTO dto) { 
		this.callDTO = dto; 
	}
	
	/**
	 * @throws UnrecognisedPrefix		if the prefix is not recognised (source)
	 * @throws PhoneNotExists			if the phone does not exist (source)
	 * @throws PhoneNumberNotValid		if the given source number isn't a valid
	 * 									Phone number
	 * @throws InvalidStateReceiveVoice	only on and silent states allow the phone to
	 * 									receive voice calls
	 * @throws InvalidStateReceiveVideo	only on and silent states allow the phone to
	 * 									receive Video calls
	 * @throws CantReceiveVoiceCall		if the source is not a phone capable of
	 * 									receiving voice calls
	 * @throws CantReceiveVideoCall		if the source is not a phone capable of
	 * 									receiving Video calls
	 * @throws InvalidCallType			if the given call type isn't Voice or Video
	 */
	@Override
	public void dispatch()
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid,
				InvalidStateReceiveVoice, InvalidStateReceiveVideo, CantReceiveVoiceCall,
				CantReceiveVideoCall, InvalidCallType {
		Network network = FenixFramework.getRoot();
		CommunicationRepresentation types = CommunicationRepresentation.getInstance();
		if (this.callDTO.getType().equals(types.getVoiceCommunication())) {
			network.receiveVoiceCall(this.callDTO.getSource(), this.callDTO.getDestination());
		} else if (this.callDTO.getType().equals(types.getVideoCommunication())) {
			network.receiveVideoCall(this.callDTO.getSource(), this.callDTO.getDestination());
		} else {
			throw new InvalidCallType(this.callDTO.getSource(), this.callDTO.getType());
		}
	}

}
