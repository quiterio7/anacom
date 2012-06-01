package anacom.domain;

import java.util.ArrayList;
import java.util.List;

import anacom.shared.exceptions.IncompatiblePrefix;
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.phone.BalanceLimitExceeded;
import anacom.shared.exceptions.phone.CantChangeState;
import anacom.shared.exceptions.phone.CantMakeVideoCall;
import anacom.shared.exceptions.phone.CantMakeVoiceCall;
import anacom.shared.exceptions.phone.CantReceiveSMS;
import anacom.shared.exceptions.phone.CantReceiveVideoCall;
import anacom.shared.exceptions.phone.CantReceiveVoiceCall;
import anacom.shared.exceptions.phone.CantSendSMS;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.NoMadeCommunication;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneAlreadyExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;
import anacom.shared.misc.Constants;
import anacom.shared.misc.FieldVerifier;

public abstract class Phone extends Phone_Base {
	
	/**
	 * Constructor
	 * Do not use this constructor, a Phone should always have a number
	 * It only exists for compatibility with the Fenix Framework
	 */
	public Phone() { super(); }
    
	/**
	 * This creates a new Phone instance.
	 * @param number the desired Phone's number
	 * @throws PhoneNumberNotValid if the number isn't a String with exactly 9
	 * numerical digits
	 */
    public Phone(String number)
    		throws PhoneNumberNotValid {
        super();
        this.init(number);
    }
    
    /**
     * Used by the constructor to initialize this Phone. Meant to avoid code
     * repetition in the subclasses' constructors
     * @param number the desired Phone's number
	 * @throws PhoneNumberNotValid if the number isn't a String with exactly 9
	 * numerical digits
	 */
    protected void init(String number)
    		throws PhoneNumberNotValid {
    	if (!FieldVerifier.getInstance().validPhoneNumber(number)) {
    		throw new PhoneNumberNotValid(number);
    	}

        this.setNumber(number);
        this.setBalance(0);
        this.setState(new OffState());
        this.setLastMadeCommunication(null);
    }
    
    /**
     * Gets the prefix of the phone
     * @return	the phone's prefix
     */  
    public String getPrefix() {
    	return Network.getOperatorPrefixfromNumber(this.getNumber());
    }
    
    /**
     * Register this phone on given operator
     * @param op					the operator on which to register the Phone
     * @throws PhoneAlreadyExists	if given operator already has a Phone with
     * 								given number
     */
    @Override
    public void setOperator(Operator op)
    		throws PhoneAlreadyExists, IncompatiblePrefix {
    	op.addPhone(this);
    }
    
    /**
     * @return	true if the balance is positive, false otherwise
     */
    boolean balanceIsPositive() { return this.getBalance() > 0; }
    
    /**
     * Increases the balance of this Phone by the given amount with the given
     * bonus.
     * @param amount the amount to increment to be added the Phone's balance
     * @param bonus	the bonus tax to add to the Phone's balance
     * @throws BalanceLimitExceeded if the balance limit was exceeded
     * @throws InvalidAmount if the given amount is 0 or negative
     */
    void increaseBalance(int amount, int bonus)
    		throws BalanceLimitExceeded, InvalidAmount {
    	int amountWithBonus = amount + (amount*bonus)/100;
    	if (amount <= 0) {
			throw new InvalidAmount(getNumber(), amount);
    	} else if (this.getBalance() + amountWithBonus
    			> Constants.MAX_BALANCE_AMOUNT) {
    		throw new BalanceLimitExceeded(this.getNumber());
    	}
		this.setBalance(this.getBalance() + amountWithBonus);
    }
    
    /**
     * Removes a given amount from the Phone's balance.
     * @param amount amount to be removed
     * @throws NotPositiveBalance if this Phone doesn't have enough balance
     * @throws InvalidAmount if the given amount is 0 or negative
     */
    void decreaseBalance(int amount)
    		throws NotPositiveBalance, InvalidAmount {
    	if (!this.balanceIsPositive()) {
    		throw new NotPositiveBalance(this.getNumber(), this.getBalance());
    	} if (amount <= 0) {
			throw new InvalidAmount(this.getNumber(), amount);
    	}
    	this.setBalance(getBalance() - amount);
    }
    
    /**
     * @return true if this Phone supports SMS, false otherwise.
     */
    public abstract boolean supportsSMS();
    
    /**
     * @return true if this Phone supports Voice communications, false
     * otherwise.
     */
    public abstract boolean supportsVoice();
    
    /**
     * @return true if this Phone supports Video communications, false
     * otherwise.
     */
    public abstract boolean supportsVideo();
    
    /**
     * Used to send a SMS from this Phone.
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
     * @throws CantSendSMS
     * 		if this Phone doesn't support the send SMS operation
     * @throws PhoneNumberNotValid
     * 		if the given destination number isn't a valid phone number
     * @throws SMSMessageNotValid
     * 		if the given message isn't a valid SMS
     */
    SMS sendSMS(String destination, String message)
    		throws NotPositiveBalance, InvalidAmount, InvalidStateSendSMS,
    			CantSendSMS, PhoneNumberNotValid, SMSMessageNotValid {
    	if (this.supportsSMS()) {
    		return this.getState().sendSMS(destination, message);
    	} else {
    		throw new CantSendSMS(this.getNumber());
    	}
    }
    
    /**
     * Used to receive a SMS on this Phone.
     * @param source
     * 		the source's Phone number
     * @param message
     * 		the SMS' content
     * @param cost
     * 		the SMS' cost
     * @throws CantReceiveSMS
     * 		if this Phone doesn't support the receive SMS operation
     * @throws PhoneNumberNotValid
     * 		if the given source number isn't a valid Phone Number
     */
    void receiveSMS(String source, String message, int cost)
    		throws CantReceiveSMS, PhoneNumberNotValid {
    	if (this.supportsSMS()) {
    		this.getState().receiveSMS(source, message, cost);
    	} else {
    		throw new CantReceiveSMS(this.getNumber());
    	}
    }
    
    /**
     * Used to establish a Voice call.
     * @param otherParty the number of the Phone receiving the Voice call
     * @param startTime	the time at which the Voice call was established in
     * seconds
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVoice if the Phone is in the Off or Occupied
     * state
     * @throws NotPositiveBalance if this Phone's balance isn't positive
     * @throws CantMakeVoiceCall if this Phone doesn't support the make Voice
     * call operation
     */
    void makeVoiceCall(String otherParty, long startTime)
    		throws PhoneNumberNotValid, InvalidStateMakeVoice,
    			NotPositiveBalance, CantMakeVoiceCall {
    	if(this.supportsVoice()) {
    		this.getState().makeVoiceCall(otherParty, startTime);
    	} else {
    		throw new CantMakeVoiceCall(this.getNumber());
    	}
    }
    
    /**
     * Used to receive a Voice call.
     * @param otherParty the number of the Phone making the Voice call
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVoice if the Phone is in the Off, Occupied or
     * Silent state
     * @throws CantReceiveVoiceCall	if this Phone doesn't support the receive
     * Voice call operation
     */
    void receiveVoiceCall(String otherParty) 
    		throws PhoneNumberNotValid, InvalidStateReceiveVoice, CantReceiveVoiceCall {
    	if (this.supportsVoice()) {
    		this.getState().receiveVoiceCall(otherParty);
    	} else {
    		throw new CantReceiveVoiceCall(this.getNumber());
    	}
    }
    
    /**
     * Used to establish a Video call.
     * @param otherParty the number of the Phone receiving the Video call
     * @param startTime	the time at which the Video call was established in
     * seconds
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVideo if the Phone is in the Off or Occupied
     * state
     * @throws NotPositiveBalance if this Phone's balance isn't positive
     * @throws CantMakeVideoCall if this Phone doesn't support the make Video
     * call operation
     */
    void makeVideoCall(String otherParty, long startTime)
    		throws PhoneNumberNotValid, InvalidStateMakeVideo,
    			NotPositiveBalance, CantMakeVideoCall {
    	if(this.supportsVideo()) {
    		this.getState().makeVideoCall(otherParty, startTime);
    	} else {
    		throw new CantMakeVideoCall(this.getNumber());
    	}
    }
    
    /**
     * Used to receive a Video call.
     * @param otherParty the number of the Phone making the Video call
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVideo if the Phone is in the Off, Occupied or
     * Silent state
     * @throws CantReceiveVideoCall	if this Phone doesn't support the receive
     * Video call operation
     */
    void receiveVideoCall(String otherParty) 
    		throws PhoneNumberNotValid, InvalidStateReceiveVideo,
    			CantReceiveVideoCall {
    	if (this.supportsVideo()) {
    		this.getState().receiveVideoCall(otherParty);
    	} else {
    		throw new CantReceiveVideoCall(this.getNumber());
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
    	return this.getState().finishCall(endTime);
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
    	this.getState().finishCall(duration, cost);
    }
    
    /**
     * Turn this Phone on.
	 * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be turned on
     */
    public void turnOn() throws CantChangeState {
    	this.getState().turnOn();
    }
    
    /**
     * Turn this Phone off.
	 * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be turned off
     */
    public void turnOff() throws CantChangeState {
    	this.getState().turnOff();
    }
    
    /**
     * Silence this Phone.
     * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be silenced
     */
    public void silence() throws CantChangeState {
    	this.getState().silence();
    }
    
    /**
     * Gets the last communication made by this Phone.
     * @return the last communication made by this Phone
     * @throws NoMadeCommunication if this Phone hasn't established a
     * communication so far
     */
    public Communication lastMadeCommunication() throws NoMadeCommunication {
    	if (this.getLastMadeCommunication() != null) {
    		return this.getLastMadeCommunication();
    	} else {
    		throw new NoMadeCommunication(this.getNumber());
    	}
    }
    
    /**
     * Gets all SMS communications made by this Phone.
     * @return the list of all SMS communications made by this Phone
     */
    public List<SMS> getSMSList() {
    	List<SMS> SMSList = new ArrayList<SMS>();
    	for (Communication communication : this.getReceivedCalls()) {
    		if(communication instanceof SMS) {
    			SMS sms = (SMS) communication;
    			SMSList.add(sms);
    		}
    	}
    	return SMSList;
    }
}
