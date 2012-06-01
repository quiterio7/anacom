package anacom.domain;

import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;
import anacom.shared.misc.FieldVerifier;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

public class OffState extends OffState_Base {
    
    public  OffState() {
        super();
    }

    /**
     * Sends a SMS from this state's Phone.
     * @param destination the destination's Phone number
     * @param message the SMS' content
     * @return SMS the communication object
     * @throws InvalidStateSendSMS if a Phone can't send a SMS in this state
     */
	@Override
	SMS sendSMS(String destination, String message)
    		throws InvalidStateSendSMS {
		throw new InvalidStateSendSMS(
				this.getPhone().getNumber(),
				this.getStateType());
	}
	
	/**
     * Makes a Video call from this state's Phone.
     * @param otherParty the number of the Phone receiving the Voice call
     * @param startTime	the time at which the Voice call was established in
     * seconds
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVoice if a Phone can't make a Voice call in this
     * state
     */
	@Override
	void makeVoiceCall(String otherParty, long startTime)
			throws PhoneNumberNotValid, InvalidStateMakeVoice {
		if (FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
			throw new InvalidStateMakeVoice(
					this.getPhone().getNumber(),
					this.getStateType());
		} else {
			throw new PhoneNumberNotValid(otherParty);
		}
	}
	
	/**
     * Makes a Video call from this state's Phone.
     * @param otherParty the number of the Phone receiving the Video call
     * @param startTime	the time at which the Video call was established in
     * seconds
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVideo if a Phone can't make a Video call in this
     * state
     */
	@Override
	void makeVideoCall(String otherParty, long startTime) 
			throws PhoneNumberNotValid, InvalidStateMakeVideo {
		if(FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
			throw new InvalidStateMakeVideo(
					this.getPhone().getNumber(),
					this.getStateType());
		} else {
			throw new PhoneNumberNotValid(otherParty);
		}
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
	 * @return	"Off" string
	 */
    public String getStateType() {
    	return PhoneStateRepresentation.getInstance().getOffState(); 
    }
    
}
