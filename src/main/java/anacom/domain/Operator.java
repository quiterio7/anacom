package anacom.domain;

import java.util.Iterator;
import java.util.List;

import anacom.shared.exceptions.BonusValueNotValid;
import anacom.shared.exceptions.IncompatiblePrefix;
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.operator.OperatorNameAlreadyExists;
import anacom.shared.exceptions.operator.OperatorNameNotValid;
import anacom.shared.exceptions.operator.OperatorPrefixAlreadyExists;
import anacom.shared.exceptions.operator.OperatorPrefixNotValid;
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
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishReceivingCall;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;
import anacom.shared.misc.FieldVerifier;

public class Operator extends Operator_Base {
    
	/**
	 * Constructor
	 * @param name		the name of the Operator
	 * @param prefix	the prefix of the Operator
	 * @param smsCost	the cost of a SMS per 100 characters
	 * @param voiceCost	the cost of a Voice communication per second
	 * @param videoCost	the cost of a Video communication per second
	 * @param tax		the tax of a communication with another Operator
	 * @throws OperatorPrefixNotValid	if the given prefix is not a String with
	 * 									exactly 2 numerical digits
	 * @throws OperatorNameNotValid		if the given name is null or an empty
	 * 									String
     * @throws BonusValueNotValid		if the given bonus tax is minor than 0
	 */
	public Operator(String name,
					String prefix,
					int smsCost,
					int voiceCost,
					int videoCost,
					int tax,
					int bonus)
			throws OperatorPrefixNotValid, OperatorNameNotValid,
				BonusValueNotValid {
        super();
        
        // Check correct input
        FieldVerifier verifier = FieldVerifier.getInstance();
        if (!verifier.validOperatorPrefix(prefix)) {
        	throw new OperatorPrefixNotValid(prefix);
        }
        if (!verifier.validOperatorName(name)) {
        	throw new OperatorNameNotValid(name);
        }
        
        if(!verifier.validBonusTax(bonus)){
        	throw new BonusValueNotValid(bonus);
        }
        
        this.setName(name);
        this.setPrefix(prefix);
        this.setSmsCost(smsCost);
        this.setVoiceCost(voiceCost);
        this.setVideoCost(videoCost);
        this.setTax(tax);
        this.setBonus(bonus);
    }
    
	/**
	 * Adds a phone to the operator
	 * @param phone					the phone to be added
	 * @throws PhoneAlreadyExists	if a phone with the same number as the one 
	 * 								passed to this function already exists
	 * @throws IncompatiblePrefix	if the phone's prefix and the operator's isn't
	 * 								the same
	 */
	@Override
	public void addPhone(Phone phone) throws PhoneAlreadyExists, IncompatiblePrefix {
		if (!phone.getPrefix().equals(this.getPrefix())) {
			throw new IncompatiblePrefix(this.getPrefix(), phone.getPrefix());
		} else if (this.hasPhoneWithNumber(phone.getNumber())) {
			throw new PhoneAlreadyExists(phone.getNumber());
		} else {
			super.addPhone(phone);
		}
	}
	
	/**
	 * Removes a phone from this operator
	 * @param number			the phone to be removed
	 * @throws PhoneNotExists	if the phone to be removed doesn't exist
	 */
	public void removePhone(String number) throws PhoneNotExists {
		for (Iterator<Phone> it = this.getPhoneSet().iterator(); it.hasNext();) {
			Phone phone = it.next();
			if (phone.getNumber().equals(number)) {
				it.remove();
				return;
			}
		}
		throw new PhoneNotExists(number);
	}
	
	/**
	 * Checks if the operator has a phone with the given number
	 * @param number	the number to test with
	 * @return			true if the operator has a phone with the given number, 
	 * 					false otherwise
	 */
	public boolean hasPhoneWithNumber(String number) {
		for(Phone phone : this.getPhoneSet()) {
			if(phone.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Set network
	 * @param network		the network to be linked to the operator
	 * @throws OperatorNameAlreadyExists	if an operator with given name already
	 * 										exists in the network 
	 * @throws OperatorPrefixAlreadyExists	if an operator with given prefix already
	 * 										exists in the network
	 */
	@Override
	public void setNetwork(Network network) 
			throws OperatorNameAlreadyExists, OperatorPrefixAlreadyExists {
		network.addOperator(this);
	}
	
	/**
	 * Get the phone balance with the given number
	 * @param number			the number of the desired phone
	 * @return balance 			the balance of the phone
	 * @throws PhoneNotExists	if phone with given number doesn't exist
	 */
	public int getPhoneBalance(String number) throws PhoneNotExists {
		for (Phone phone : this.getPhoneSet()) {
			if (phone.getNumber().equals(number)) {
				return phone.getBalance();
			}
		}
		throw new PhoneNotExists(number);
	}
	
	/**
	 * Gets a Phone by its number
	 * @param number			the number of the Phone
	 * @return					the Phone with the given number
	 * @throws PhoneNotExists	if there is no Phone with the given number
	 */
	public Phone getPhonebyNumber(String number) throws PhoneNotExists {
		for(Phone phone : this.getPhoneSet()) {
			if(phone.getNumber().equals(number)) {
				return phone;
			}
		}
		throw new PhoneNotExists(number);
	}
	
	/**
     * Increases the balance of the Phone with the given number by the given
     * amount with the given bonus.
     * @param amount the amount to increment to be added the Phone's balance
     * @param bonus	the bonus tax to add to the Phone's balance
     * @throws BalanceLimitExceeded if the balance limit was exceeded
     * @throws PhoneNotExists if there's no Phone with the given number in this
     * Operator
     * @throws InvalidAmount if the given amount is 0 or negative
     */
	void increasePhoneBalance(String number, int amount)
			throws BalanceLimitExceeded, PhoneNotExists, InvalidAmount {
		this.getPhonebyNumber(number).increaseBalance(
				amount,
				this.getBonus());
	}
	
	/**
     * Removes a given amount from the given Phone's balance.
     * @param amount amount to be removed
     * @throws NotPositiveBalance if this Phone doesn't have enough balance
     * @throws PhoneNotExists if there's no Phone with the given number in this
     * Operator
     * @throws InvalidAmount if the given amount is 0 or negative
     */
	void decreasePhoneBalance(String number, int amount)
			throws NotPositiveBalance, PhoneNotExists, InvalidAmount {
		this.getPhonebyNumber(number).decreaseBalance(amount);
	}
	
    /**
     * Calculates the cost of a SMS.
     * @param string
     * 		the text message
     * @param destination
     * 		the destination number
     * @return
     * 		the cost of the SMS
     * @throws SMSMessageNotValid
     * 		if the given message isn't a valid SMS
     * @throws PhoneNumberNotValid
     * 		if the given destination number isn't a valid Phone number
     */
	public int calcSMSCost(String message, String destination)
			throws SMSMessageNotValid, PhoneNumberNotValid {
		String destinationPrefix =
				Network.getOperatorPrefixfromNumber(destination);
		if (!FieldVerifier.getInstance().validMessage(message)) {
			throw new SMSMessageNotValid(destination, message);
		} else if (!FieldVerifier.getInstance().validPhoneNumber(destination)) {
			throw new PhoneNumberNotValid(destination);
		}
		int cost = this.getSmsCost() * ((message.length() - 1)/100 + 1);
		if (!this.getPrefix().equals(destinationPrefix)) {
			cost += (cost * getTax())/100;
		}
		return cost;
	}
	
	/**
	 * Calculates the cost of a voice call
	 * @param duration			the duration of the voice call
	 * @param originPrefix		the origin's operator prefix
	 * @param destinationPrefix	the destination's operator prefix
	 * @return	the cost of the voice call
	 */
	public int calcVoiceCost(
			int duration,
			String originPrefix,
			String destinationPrefix) {
		int cost = this.getVoiceCost() * duration;
		if (!originPrefix.equals(destinationPrefix)) {
			cost += (cost * getTax())/100;
		}
		return cost;
	}
	
	/**
	 * Calculates the cost of a video call
	 * @param duration			the duration of the video call
	 * @param originPrefix		the origin's operator prefix
	 * @param destinationPrefix	the destination's operator prefix
	 * @return	the cost of the video call
	 */
	public int calcVideoCost(
			int duration,
			String originPrefix,
			String destinationPrefix) {
		int cost = this.getVideoCost() * duration;
		if (!originPrefix.equals(destinationPrefix)) {
			cost += (cost * getTax())/100;
		}
		return cost;
	}
	
	/**
	 * Sends a SMS from the Phone with the given source number.
	 * @param source
	 * 		the source's Phone number
	 * @param destination
	 * 		the destination's Phone number
     * @param message
     * 		the SMS' content
     * @return
     * 		the SMS communication object
	 * @throws PhoneNotExists
	 * 		if there's no Phone with the given source number
	 * @throws NotPositiveBalance
	 * 		if the source's balance is 0 or negative
	 * @throws InvalidStateSendSMS
	 * 		if the Phone is in the Off or Occupied state
     * @throws CantSendSMS
     * 		if this Phone doesn't support the send SMS operation
	 * @throws InvalidAmount
	 * 		if the SMS' cost is 0 or negative (this shouldn't happen)
	 * @throws PhoneNumberNotValid
     * 		if the given destination number isn't a valid phone number
     * @throws SMSMessageNotValid
     * 		if the given message isn't a valid SMS
	 */
	SMS sendSMS(String source, String destination, String message) 
			throws PhoneNotExists, NotPositiveBalance, InvalidStateSendSMS,
				CantSendSMS, InvalidAmount, PhoneNumberNotValid,
				SMSMessageNotValid {
		return this.getPhonebyNumber(source).sendSMS(destination, message);
	}
	
	/**
     * Receives a SMS to be passed to the Phone with the given destination
     * number.
     * @param source
     * 		the source's Phone number
     * @param destination
     * 		the destination's Phone number
     * @param message
     * 		the SMS' content
     * @param cost
     * 		the SMS' cost
     * @throws PhoneNotExists
     * 		if there's no Phone with the given destination number
     * @throws CantReceiveSMS
     * 		if this Phone doesn't support the receive SMS operation
     * @throws PhoneNumberNotValid
     * 		if the given source or destination number isn't a valid Phone
     * 		number
     */
    void receiveSMS(String source,
    				String destination,
    				String message,
		    		int cost)
		    throws PhoneNotExists, CantReceiveSMS, PhoneNumberNotValid {
    	this.getPhonebyNumber(destination).receiveSMS(
    			source,
    			message,
    			cost);
    }
    
    /**
     * Establishes a Voice call from the Phone with the given source number.
     * @param source the source's Phone number
     * @param destination the destination's Phone number
     * @param startTime	the time at which the Voice call was established in
     * seconds
     * @throws PhoneNotExists if there's no Phone with the given source number
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVoice if the Phone is in the Off or Occupied
     * state
     * @throws NotPositiveBalance if this Phone's balance isn't positive
     * @throws CantMakeVoiceCall if this Phone doesn't support the make Voice
     * call operation
     */
    void makeVoiceCall(String source, String destination, long startTime) 
    		throws PhoneNotExists, PhoneNumberNotValid, NotPositiveBalance, 
    			InvalidStateMakeVoice, CantMakeVoiceCall {
    	Phone phone = this.getPhonebyNumber(source);
    	phone.makeVoiceCall(destination, startTime);
    }
    
    /**
     * Receives a Voice call on the Phone with the given destination number.
     * @param source the source's Phone number
     * @param destination the destination's Phone number
     * @throws PhoneNotExists if there's no Phone with the given destination
     * number
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVoice if the Phone is in the Off, Occupied or
     * Silent state
     * @throws CantReceiveVoiceCall	if this Phone doesn't support the receive
     * Voice call operation
     */
    void receiveVoiceCall(String source, String destination) 
    		throws PhoneNotExists, PhoneNumberNotValid, 
    			InvalidStateReceiveVoice, CantReceiveVoiceCall {
    	Phone phone = this.getPhonebyNumber(destination);
    	phone.receiveVoiceCall(source);
    }
    
    /**
     * Establishes a Video call from the Phone with the given source number.
     * @param source the source's Phone number
     * @param destination the destination's Phone number
     * @param startTime	the time at which the Voice call was established in
     * seconds
     * @throws PhoneNotExists if there's no Phone with the given source number
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVideo if the Phone is in the Off or Occupied
     * state
     * @throws NotPositiveBalance if this Phone's balance isn't positive
     * @throws CantMakeVideoCall if this Phone doesn't support the make Video
     * call operation
     */
    void makeVideoCall(String source, String destination, long startTime) 
    		throws PhoneNotExists, PhoneNumberNotValid, NotPositiveBalance,
    			InvalidStateMakeVideo, CantMakeVideoCall {
    	Phone phone = this.getPhonebyNumber(source);
    	phone.makeVideoCall(destination, startTime);
    }
    
    /**
     * Receives a Video call on the Phone with the given destination number.
     * @param source the source's Phone number
     * @param destination the destination's Phone number
     * @throws PhoneNotExists if there's no Phone with the given destination
     * number
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVideo if the Phone is in the Off, Occupied or
     * Silent state
     * @throws CantReceiveVideoCall	if this Phone doesn't support the receive
     * Video call operation
     */
    void receiveVideoCall(String source, String destination) 
    		throws PhoneNotExists, PhoneNumberNotValid,
    				InvalidStateReceiveVideo, CantReceiveVideoCall {
    	Phone phone = this.getPhonebyNumber(destination);
    	phone.receiveVideoCall(source);
    }
    
    /**
     * Finish an undergoing call. This is called on the source Phone.
     * @param source the source's Phone number
     * @param endTime the call's ending time in seconds
     * @return the Call communication object
     * @throws PhoneNotExists if there's no Phone with the given source number
     * @throws DurationNotValid if the given call duration is not valid
     * @throws InvalidStateFinishMakingCall	if the Phone is not currently
     * making a call
     */
    Call finishCall(String source, long endTime) 
    		throws PhoneNotExists, DurationNotValid,
    			InvalidStateFinishMakingCall {
    	Phone phone = this.getPhonebyNumber(source);
    	return phone.finishCall(endTime);
    }
    
    /**
     * Finish an undergoing call. This is called on the destination Phone.
     * @param destination the destination's Phone number
     * @param duration the call's duration
     * @param cost the call's cost
     * @throws PhoneNotExists if there's no Phone with the given source number
     * @throws DurationNotValid if the given call duration is not a valid
     * @throws InvalidStateFinishReceivingCall if the Phone is not currently
     * receiving a call
     */
    void finishCall(String destination, int duration, int cost) 
    		throws PhoneNotExists, DurationNotValid,
    			InvalidStateFinishReceivingCall {
    	Phone phone = this.getPhonebyNumber(destination);
    	phone.finishCall(duration, cost);
    }
    
    /**
     * Getter of the Last Made Communication
     * @param number	the Number which we want 
     * 					to know this last communication
     * @return			The Last Communication executed by a specific phone
     * @throws NoMadeCommunication	if there is no communication until now
     * @throws PhoneNotExists	if the phone does not exists
     */
    Communication getLastMadeCommunication(String number) 
    		throws PhoneNotExists, NoMadeCommunication {
    	Phone phone = this.getPhonebyNumber(number);
    	return phone.lastMadeCommunication();
    }
    
    /**
     * Getter of All SMS Communications of a Phone Number
     * @param number	the number which we want to know the list of SMS.
     * @return the list of all SMS of a phone number
     * @throws PhoneNotExists if the phone doesn't exist in Operator Context
     */
    List<SMS> getSMSListFromNumber(String number) throws PhoneNotExists {
    	Phone phone = this.getPhonebyNumber(number);
    	return phone.getSMSList();
    }
    
    /**
     * Turn on the Phone with the given number.
     * @param number the desired Phone's number
     * @throws PhoneNotExits if there's no Phone with the given number
	 * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be turned on
     */
    void turnOnPhone(String number)
    		throws PhoneNotExists, CantChangeState {
    	Phone phone = this.getPhonebyNumber(number);
    	phone.turnOn();
    }
   
    /**
     * Turn off the Phone with the given number.
     * @param number the desired Phone's number
     * @throws PhoneNotExits if there's no Phone with the given number
	 * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be turned off
     */
    void turnOffPhone(String number)
    		throws PhoneNotExists, CantChangeState {
    	Phone phone = this.getPhonebyNumber(number);
    	phone.turnOff();
    }
    
    /**
     * Silence the Phone with the given number.
     * @param number the desired Phone's number
     * @throws PhoneNotExits if there's no Phone with the given number
	 * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be silenced
     */
    void silencePhone(String number)
    		throws PhoneNotExists , CantChangeState {
    	Phone phone = this.getPhonebyNumber(number);
    	phone.silence();
    }
}
