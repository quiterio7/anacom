package anacom.domain;

import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.phone.CantChangeState;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;
import anacom.shared.misc.FieldVerifier;

public abstract class PhoneState extends PhoneState_Base {
    
    public  PhoneState() { super(); }
    
    /**
	 * Set phone
	 * @param phone
	 */
    @Override
    public final void setPhone(Phone phone) { phone.setState(this); }
    
    /**
	 * Changes the Phone state to On.
	 * @throws CantChangeState if this state doesn't allow the Phone to be
	 * turned on
	 */
    void turnOn() throws CantChangeState {
    	this.getPhone().setState(new OnState());
    }
    
    /**
	 * Changes the Phone state to Off.
	 * @throws CantChangeState if this state doesn't allow the Phone to be
	 * turned off
	 */
    void turnOff() throws CantChangeState {
    	this.getPhone().setState(new OffState());
    }
    
    /**
	 * Changes the Phone state to Silent.
	 * @throws CantChangeState if this state doesn't allow the Phone to be
	 * silenced
	 */
    void silence() throws CantChangeState {
    	this.getPhone().setState(new SilentState());
    }
    
    /**
     * Sends a SMS from this state's Phone.
     * @param destination
     * 		the destination's Phone number
     * @param message
     * 		the SMS' content
     * @return
     * 		the SMS communication object
     * @throws NotPositiveBalance
     * 		if the source's balance is 0 or negative
	 * @throws InvalidStateSendSMS
	 * 		if the Phone is in the Off or Occupied state
	 * @throws InvalidAmount
	 * 		if the SMS' cost is 0 or negative (this shouldn't happen)
	 * @throws PhoneNumberNotValid
	 * 		if the given destination number isn't a valid Phone number
	 * @throws SMSMessageNotValid
	 * 		if the given message isn't a valid SMS
     */
    SMS sendSMS(String destination, String message)
    		throws InvalidStateSendSMS, NotPositiveBalance, InvalidAmount,
    			PhoneNumberNotValid, SMSMessageNotValid {
    	int cost = 
    			this.getPhone().getOperator().calcSMSCost(message, destination);
    	SMS sms = new SMS(message, cost, destination);
		this.getPhone().addEstablishedCalls(sms);
		this.getPhone().setLastMadeCommunication(sms);
		this.getPhone().decreaseBalance(cost);
    	return sms;
    }
	
    /**
	 * Receives a SMS on this state's Phone.
     * @param source
     * 		the source's Phone number
     * @param message
     * 		the SMS' content
     * @param cost
     * 		the SMS' cost
     * @throws PhoneNumberNotValid
     * 		if the given source number isn't a valid Phone number
	 */
    void receiveSMS(String source, String message, int cost) 
    		throws PhoneNumberNotValid {
    	SMS sms = new SMS(message, cost, source);
   		this.getPhone().addReceivedCalls(sms);
    }
    
    /**
     * Makes a Voice call from this state's Phone.
     * @param otherParty the number of the Phone receiving the Voice call
     * @param startTime	the time at which the Voice call was established in
     * seconds
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVoice if a Phone can't make a Voice call in this
     * state
     * @throws NotPositiveBalance if this state's Phone balance isn't positive
     */
    void makeVoiceCall(String otherParty, long startTime) 
    		throws PhoneNumberNotValid, InvalidStateMakeVoice,
    			NotPositiveBalance {
    	if(FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
	    	if (this.getPhone().balanceIsPositive()) {
	    		this.getPhone().setState(new MakingVoiceCallState(
	    				otherParty,
	    				startTime,
	    				this));
	    	} else {
	    		throw new NotPositiveBalance(
	    				this.getPhone().getNumber(),
	    				this.getPhone().getBalance());
	    	}
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
     * @throws NotPositiveBalance if this state's Phone balance isn't positive
     */
    void makeVideoCall(String otherParty, long startTime) 
    		throws PhoneNumberNotValid, InvalidStateMakeVideo,
    			NotPositiveBalance {
    	if(FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
	    	if (this.getPhone().balanceIsPositive()) {
	    		this.getPhone().setState(new MakingVideoCallState(
	    				otherParty,
	    				startTime,
	    				this));
	    	} else {
	    		throw new NotPositiveBalance(
	    				this.getPhone().getNumber(),
	    				this.getPhone().getBalance());
	    	}
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
    void receiveVoiceCall(String otherParty)
    		throws PhoneNumberNotValid, InvalidStateReceiveVoice {
    	if(FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
    		this.getPhone().setState(new ReceivingVoiceCallState(
    				otherParty,
    				this));
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
    void receiveVideoCall(String otherParty)
    		throws PhoneNumberNotValid, InvalidStateReceiveVideo {
    	if(FieldVerifier.getInstance().validPhoneNumber(otherParty)) {
    		this.getPhone().setState(new ReceivingVideoCallState(
    				otherParty,
    				this));
    	} else {
    		throw new PhoneNumberNotValid(otherParty);
    	}
    }
    
    /**
     * Finish an undergoing call. This is called on the source Phone.
     * @param endTime the call's ending time in seconds
     * @return the Call communication object
     * @throws DurationNotValid if the given call duration is not valid
     * @throws InvalidStateFinishMakingCall	if the Phone is not currently
     * making a call
     */
    Call finishCall(long endTime) 
    		throws DurationNotValid, InvalidStateFinishMakingCall {
    	throw new InvalidStateFinishMakingCall(
    			this.getPhone().getNumber(),
    			this.getStateType());
    }
    
    /**
     * Finish an undergoing call. This is called on the destination Phone.
     * @param duration the call's duration
     * @param cost the call's cost
     * @throws DurationNotValid if the given call duration is not a valid
     * @throws InvalidStateFinishReceivingCall if the Phone is not currently
     * receiving a call
     */
    void finishCall(int duration, int cost) 
    		throws DurationNotValid, InvalidStateFinishReceivingCall {
    	throw new InvalidStateFinishReceivingCall(
    			this.getPhone().getNumber(),
    			this.getStateType());
    }
    
    /**
     * @return	string representation of this phone state
     */
    public abstract String getStateType();
    
}
