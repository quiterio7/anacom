package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.CallDTO;
import anacom.shared.exceptions.communication.InvalidCallType;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.CantMakeVideoCall;
import anacom.shared.exceptions.phone.CantMakeVoiceCall;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;

public class MakeCallService extends AnacomService {
	
	private CallDTO callDTO;
	
	public MakeCallService(CallDTO dto) { 
		this.callDTO = dto; 
	}
	
	/**
	 * @throws UnrecognisedPrefix		if the prefix is not recognised (source)
	 * @throws PhoneNotExists			if the phone does not exist (source)
	 * @throws PhoneNumberNotValid		if the given destination number isn't a valid
	 * 									Phone number
	 * @throws NotPositiveBalance		if the source phone does not have enough balance
	 * @throws InvalidStateMakeVoice	only on and silent states allow the phone to
	 * 									make voice calls
	 * @throws InvalidStateMakeVideo	only on and silent states allow the phone to
	 * 									make Video calls
	 * @throws CantMakeVoiceCall		if the source is not a phone capable of making
	 * 									voice calls
	 * @throws CantMakeVideoCall		if the source is not a phone capable of making
	 * 									Video calls
	 * @throws InvalidCallType			if the given call type isn't Voice or Video
	 */
	@Override
	public void dispatch()
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid, 
				NotPositiveBalance, InvalidStateMakeVoice, InvalidStateMakeVideo,
				CantMakeVoiceCall, CantMakeVideoCall, InvalidCallType {
		Network network = FenixFramework.getRoot();
		CommunicationRepresentation types = CommunicationRepresentation.getInstance();
		if (this.callDTO.getType().equals(types.getVoiceCommunication())) { 
			network.makeVoiceCall(this.callDTO.getSource(),					 
								  this.callDTO.getDestination(),			
								  this.callDTO.getStartTime());				
		} else if (this.callDTO.getType().equals(types.getVideoCommunication())) {
			network.makeVideoCall(this.callDTO.getSource(),
								  this.callDTO.getDestination(),
								  this.callDTO.getStartTime());
		} else {
			throw new InvalidCallType(this.callDTO.getDestination(),
									  this.callDTO.getType());
		}
	}
}
