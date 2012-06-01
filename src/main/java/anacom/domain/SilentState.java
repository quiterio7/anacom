package anacom.domain;

import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.misc.FieldVerifier;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

public class SilentState extends SilentState_Base {
    
    public  SilentState() {
        super();
    }
    
    /**
     * Receives a Voice call on this state's Phone.
     * @param otherParty the number of the Phone making the Voice call
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVoice	if a Phone can't receive a Voice call
     * in this state
     */
	@Override
	void receiveVoiceCall(String otherParty) 
			throws PhoneNumberNotValid, InvalidStateReceiveVoice {
		if(FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
			throw new InvalidStateReceiveVoice(
					this.getPhone().getNumber(),
					this.getStateType());
		} else {
			throw new PhoneNumberNotValid(otherParty);
		}
	}
	
	/**
     * Receives a Video call on this state's Phone.
     * @param otherParty the number of the Phone making the Video call
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVideo	if a Phone can't receive a Video call
     * in this state
     */
	@Override
	void receiveVideoCall(String otherParty) 
			throws PhoneNumberNotValid, InvalidStateReceiveVideo {
		if(FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
			throw new InvalidStateReceiveVideo(
					this.getPhone().getNumber(),
					this.getStateType());
		} else {
			throw new PhoneNumberNotValid(otherParty);
		}
	}
	
	/**
	 * @return	"Silent" string
	 */
    public String getStateType() { 
    	return PhoneStateRepresentation.getInstance().getSilentState(); 
    }
    
}