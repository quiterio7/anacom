package anacom.domain;

import java.util.List;

import anacom.shared.exceptions.BonusValueNotValid;
import anacom.shared.exceptions.IncompatiblePrefix;
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.operator.OperatorNameAlreadyExists;
import anacom.shared.exceptions.operator.OperatorNameNotValid;
import anacom.shared.exceptions.operator.OperatorPrefixAlreadyExists;
import anacom.shared.exceptions.operator.OperatorPrefixNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
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

public class Network extends Network_Base {
    
	/**
	 * Constructor
	 */
    public  Network() { super(); }
 
    /**
     * Adds an operator to the network
     * @param operator						the operator to be added do the network
     * @throws OperatorNameAlreadyExists	if an operator with given name already
     * 										exists in the network 
     * @throws OperatorPrefixAlreadyExists	if an operator with given prefix already
     * 										exists in the network
     */
    // Attention - calling this method from other package than this one should
    // be avoided.
    @Override
    public void addOperator(Operator operator)
    		throws OperatorNameAlreadyExists, OperatorPrefixAlreadyExists {
    	for(Operator op : this.getOperatorSet()) {
    	    if(op.getName().equals(operator.getName()))
    			throw new OperatorNameAlreadyExists(op.getName(),
    												op.getPrefix(),
    												op.getSmsCost(),
    												op.getVoiceCost(),
    												op.getVideoCost(),
    												op.getTax(),
    												op.getBonus());
    	    else if (op.getPrefix().equals(operator.getPrefix()))
    	    	throw new OperatorPrefixAlreadyExists(op.getName(),
													  op.getPrefix(),
													  op.getSmsCost(),
													  op.getVoiceCost(),
													  op.getVideoCost(),
													  op.getTax(),
													  op.getBonus());
    	}
    	super.addOperator(operator);
    }
    
    /**
     * Adds an operator to the network
     * @param name		the name of Operator
     * @param prefix	the prefix of Operator
     * @param SMScost	the SMS Cost base 
     * @param VOICEcost the VOICE Cost base
     * @param VIDEOcost the VIDEO Cost base
     * @param tax		the tax base
     * @param bonus		the bonus base
     * @throws OperatorNameAlreadyExists 	if an operator with given name already
     * 									 	exists in the network
     * @throws OperatorPrefixAlreadyExists 	if an operator with given prefix already
     * 										exists in the network
     * @throws OperatorPrefixNotValid		if the given prefix is not a valid one
     * @throws OperatorNameNotValid			if the given name is not a valid one
     * @throws BonusValueNotValid			if the given bonus tax is minor than 0
     */
    public void addOperator (String name,
    						 String prefix,
						 	 int smsCost,
						 	 int voiceCost,
						 	 int videoCost,
						 	 int tax,
						 	 int bonus)
    		throws OperatorNameAlreadyExists, OperatorPrefixAlreadyExists, 
    			OperatorPrefixNotValid, OperatorNameNotValid, BonusValueNotValid {
    	this.addOperator(new Operator(name, prefix, smsCost, voiceCost, videoCost, tax, bonus));
    }

    /**
     * Register given phone in operator with given prefix
     * @param prefix				the prefix of the operator
     * @param phone					the phone to be registered
     * @throws UnrecognisedPrefix	if an operator with given prefix doesn't exist in
     * 								this network
     * @throws PhoneAlreadyExists	if there's already a phone registered with the
     * 								given number
     * @throws IncompatiblePrefix	if the given operator's prefix and the phone's
     * 								isn't the same
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    private void registerPhone(String prefix, Phone phone)
			  throws UnrecognisedPrefix, PhoneAlreadyExists, 
			  IncompatiblePrefix, PhoneNumberNotValid {
    	Operator targetOperator = this.getOperatorFromPrefix(prefix);
    	targetOperator.addPhone(phone);
    }
    
    /**
     * Adds a new 3G Phone to the network.
     * @param prefix the prefix of the Operator on which to register the phone
     * @param number the Phone number to be added to the network
     * @throws UnrecognisedPrefix if there's no Operator with the given prefix
     * on this network
     * @throws PhoneAlreadyExists if there's already a Phone with the given
     * number on this network
     * @throws PhoneNumberNotValid if the given number isn't a valid Phone
     * number
     * @throws IncompatiblePrefix if the given prefix is not the same as the
     * number's prefix
     */
    public void registerPhone3G(String prefix, String number)
    		throws UnrecognisedPrefix, PhoneAlreadyExists, PhoneNumberNotValid,
    			IncompatiblePrefix {
    	this.registerPhone(prefix, new Phone3G(number));
    }
    
    /**
     * Adds a new 2G Phone to the network.
     * @param prefix the prefix of the Operator on which to register the phone
     * @param number the Phone number to be added to the network
     * @throws UnrecognisedPrefix if there's no Operator with the given prefix
     * on this network
     * @throws PhoneAlreadyExists if there's already a Phone with the given
     * number on this network
     * @throws PhoneNumberNotValid if the given number isn't a valid Phone
     * number
     * @throws IncompatiblePrefix if the given prefix is not the same as the
     * number's prefix
     */
    public void registerPhone2G(String prefix, String number)
    		throws UnrecognisedPrefix, PhoneAlreadyExists, PhoneNumberNotValid,
    			IncompatiblePrefix {
    	this.registerPhone(prefix, new Phone2G(number));
    }
    
    /**
     * Deletes a Phone from the Network and all the data associated with it
     * @param number	the number of the phone to be deleted from the network
     * @throws UnrecognisedPrefix	if there's no Operator on this Network with
     * 								the prefix of the given number
     * @throws PhoneNotExists		if there's no Phone with the given number
     * 								on the Operator
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public void unregisterPhone(String number)
    		throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
    	Operator operator = this.getOperatorFromNumber(number);
    	operator.removePhone(number);
    }
    
    
    /**
     * Increases the balance of the Phone with the given number by the given
     * amount with the given bonus.
     * @param amount the amount to increment to be added the Phone's balance
     * @param bonus	the bonus tax to add to the Phone's balance
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given number's prefix
     * @throws BalanceLimitExceeded if the balance limit was exceeded
     * @throws PhoneNotExists if there's no Phone with the given number in this
     * Operator
     * @throws InvalidAmount if the given amount is 0 or negative
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public void increasePhoneBalance(String number, int amount)
    		throws UnrecognisedPrefix, PhoneNotExists, BalanceLimitExceeded,
    			InvalidAmount {
    	Operator operator = this.getOperatorFromNumber(number);
    	operator.increasePhoneBalance(number, amount);    
	}
    
    /**
     * Returns the operator identified by the given prefix
     * @param prefix				the prefix of the desired operator
     * @return						the operator identified by the given prefix
     * @throws UnrecognisedPrefix	if the prefix does not exist in the network
     */
    public Operator getOperatorFromPrefix(String prefix)
    		throws UnrecognisedPrefix {
    	for(Operator operator : this.getOperatorSet()) {
    		if(operator.getPrefix().equals(prefix)) {
    			return operator;
    		}
    	}
    	throw new UnrecognisedPrefix(prefix);
    }
    
    /**
     * Checks if this network has operator with given name
     * @param name		the name of the operator
     * @return			true if the operator exists in this network, false
     * 					otherwise
     */
    public boolean hasOperatorWithName(String name) {
    	for(Operator operator : this.getOperatorSet()) {
    		if(operator.getName().equals(name)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Checks if this network has operator with given prefix
     * @param prefix	the prefix of the operator
     * @return			true if the operator exists in this network, false
     * 					otherwise
     */
    public boolean hasOperatorWithPrefix(String prefix) {
    	for(Operator operator : this.getOperatorSet()) {
    		if(operator.getPrefix().equals(prefix)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
	 * Sends a SMS from the Phone with the given source number.
	 * @param message
	 * 		the SMS' content
	 * @param source
	 * 		the source's Phone number
	 * @param destination
	 * 		the destination's Phone number
     * @return
     * 		the SMS communication object
     * @throws UnrecognisedPrefix
     * 		if there's no Operator on this network with the given source's
     * 		prefix
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
     * 		if the given source or destination number isn't a valid phone
     * 		number
     * @throws SMSMessageNotValid
     * 		if the given message isn't a valid SMS
	 */
    public SMS sendSMS(String message, String source, String destination) 
    		throws UnrecognisedPrefix, PhoneNotExists, NotPositiveBalance,
    			InvalidStateSendSMS, SMSMessageNotValid, InvalidAmount, 
    			PhoneNumberNotValid {
    	return this.getOperatorFromNumber(source).sendSMS(
    			source,
    			destination,
    			message);
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
     * @throws UnrecognisedPrefix
     * 		if there's no Operator on this network with the given destination's
     * 		prefix
     * @throws PhoneNotExists
     * 		if there's no Phone with the given destination number
     * @throws CantReceiveSMS
     * 		if this Phone doesn't support the receive SMS operation
     * @throws PhoneNumberNotValid
     * 		if the source or destination number isn't a valid Phone number
     */
    public void receiveSms(	String message,
    						String source,
    						String destination,
    						int cost)
    		throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
    	this.getOperatorFromNumber(destination).receiveSMS(
    			source,
    			destination,
    			message,
    			cost);
    }
    
    /**
     * Establishes a Voice call from the Phone with the given source number.
     * @param source the source's Phone number
     * @param destination the destination's Phone number
     * @param startTime	the time at which the Voice call was established in
     * seconds
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given source's prefix
     * @throws PhoneNotExists if there's no Phone with the given source number
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVoice if the Phone is in the Off or Occupied
     * state
     * @throws NotPositiveBalance if this Phone's balance isn't positive
     * @throws CantMakeVoiceCall if this Phone doesn't support the make Voice
     * call operation
     */
    public void makeVoiceCall(String source,
    						String destination,
    						long startTime) 
    		throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid,
    			NotPositiveBalance, InvalidStateMakeVoice, CantMakeVoiceCall {
    	Operator sourceOp = this.getOperatorFromNumber(source);
    	sourceOp.makeVoiceCall(source, destination, startTime);
    }
    
    /**
     * Receives a Voice call on the Phone with the given destination number.
     * @param source the source's Phone number
     * @param destination the destination's Phone number
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given destination's prefix
     * @throws PhoneNotExists if there's no Phone with the given destination
     * number
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVoice if the Phone is in the Off, Occupied or
     * Silent state
     * @throws CantReceiveVoiceCall	if this Phone doesn't support the receive
     * Voice call operation
     */
    public void receiveVoiceCall(String source, String destination) 
    		throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid,
    				InvalidStateReceiveVoice, CantReceiveVoiceCall {
    	Operator operator = this.getOperatorFromNumber(destination);
    	operator.receiveVoiceCall(source, destination);
    }
    
    /**
     * Establishes a Video call from the Phone with the given source number.
     * @param source the source's Phone number
     * @param destination the destination's Phone number
     * @param startTime	the time at which the Voice call was established in
     * seconds
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given source's prefix
     * @throws PhoneNotExists if there's no Phone with the given source number
     * @throws PhoneNumberNotValid if the given destination number is not a
     * valid Phone number
     * @throws InvalidStateMakeVideo if the Phone is in the Off or Occupied
     * state
     * @throws NotPositiveBalance if this Phone's balance isn't positive
     * @throws CantMakeVideoCall if this Phone doesn't support the make Video
     * call operation
     */
    public void makeVideoCall(String source,
    						String destination,
    						long startTime) 
    		throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid,
    			NotPositiveBalance, InvalidStateMakeVideo, CantMakeVideoCall {
    	Operator operator = this.getOperatorFromNumber(source);
    	operator.makeVideoCall(source, destination, startTime);
    }
    
    /**
     * Receives a Video call on the Phone with the given destination number.
     * @param source the source's Phone number
     * @param destination the destination's Phone number
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given source's prefix
     * @throws PhoneNotExists if there's no Phone with the given destination
     * number
     * @throws PhoneNumberNotValid if the given source number is not a valid
     * Phone number
     * @throws InvalidStateReceiveVideo if the Phone is in the Off, Occupied or
     * Silent state
     * @throws CantReceiveVideoCall	if this Phone doesn't support the receive
     * Video call operation
     */
    public void receiveVideoCall(String source, String destination) 
    	throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid,
    			InvalidStateReceiveVideo, CantReceiveVideoCall {
    	Operator operator = this.getOperatorFromNumber(destination);
    	operator.receiveVideoCall(source, destination);
    }
    
    /**
     * Finish an undergoing call. This is called on the source Phone.
     * @param source the source's Phone number
     * @param endTime the call's ending time in seconds
     * @return the Call communication object
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given source's prefix
     * @throws PhoneNotExists if there's no Phone with the given source number
     * @throws DurationNotValid if the given call duration is not valid
     * @throws InvalidStateFinishMakingCall	if the Phone is not currently
     * making a call
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public Call finishCall(String source, long endTime) 
    		throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
    			InvalidStateFinishMakingCall {
    	Operator operator = this.getOperatorFromNumber(source);
    	return operator.finishCall(source, endTime);
    }
    
    /**
     * Finish an undergoing call. This is called on the destination Phone.
     * @param destination the destination's Phone number
     * @param duration the call's duration
     * @param cost the call's cost
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given destination's prefix
     * @throws PhoneNotExists if there's no Phone with the given source number
     * @throws DurationNotValid if the given call duration is not a valid
     * @throws InvalidStateFinishReceivingCall if the Phone is not currently
     * receiving a call
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public void finishCall(String destination, int duration, int cost) 
    		throws UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
    			InvalidStateFinishReceivingCall {
    	Operator operator = this.getOperatorFromNumber(destination);
    	operator.finishCall(destination, duration, cost);
    }
    
    /**
     * Turn on the Phone with the given number.
     * @param number the desired Phone's number
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given number's prefix
     * @throws PhoneNotExits if there's no Phone with the given number
	 * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be turned on
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public void turnOnPhone(String number) 
    		throws UnrecognisedPrefix, PhoneNotExists, 
    		CantChangeState, PhoneNumberNotValid {
    	Operator operator = this.getOperatorFromNumber(number);
    	operator.turnOnPhone(number);
    }

    /**
     * Turn off the Phone with the given number.
     * @param number the desired Phone's number
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given number's prefix
     * @throws PhoneNotExits if there's no Phone with the given number
	 * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be turned off
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public void turnOffPhone(String number) 
    		throws UnrecognisedPrefix, PhoneNotExists, 
    		CantChangeState, PhoneNumberNotValid {
    	Operator operator = this.getOperatorFromNumber(number);
    	operator.turnOffPhone(number);
    }
    
    /**
     * Silence the Phone with the given number.
     * @param number the desired Phone's number
     * @throws UnrecognisedPrefix if there's no Operator on this network with
     * the given number's prefix
     * @throws PhoneNotExits if there's no Phone with the given number
	 * @throws CantChangeState if the current state doesn't allow the Phone to
	 * be silenced
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public void silencePhone(String number) 
    		throws UnrecognisedPrefix, PhoneNotExists, 
    		CantChangeState, PhoneNumberNotValid {
    	this.getOperatorFromNumber(number).silencePhone(number);
    }
    
    /**
     * 
     * @param number	the number which we want to know what is it last communication
     * @return	the last communication performed by the Phone number
     * @throws UnrecognisedPrefix		if the Prefix used isn't recognised by the network
     * @throws PhoneNotExists			if the phone doesn't exist in Operator Context
     * @throws NoMadeCommunication	if there is no communication until now
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public Communication getLastMadeCommunication(String number) 
    		throws UnrecognisedPrefix, PhoneNotExists, 
    		NoMadeCommunication, PhoneNumberNotValid {
    	Operator operator = this.getOperatorFromNumber(number);
    	return operator.getLastMadeCommunication(number);
    }
    
    /**
     * Getter of All SMS Communications of a Phone Number
     * @param number	the number which we want to know the list of SMS.
     * @return the list of all SMS of a phone number
     * @throws UnrecognisedPrefix	if the Prefix used isn't recognised by the network
     * @throws PhoneNotExists		if the phone doesn't exist in Operator Context
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public List<SMS> getSMSListfromNumber(String number) 
    		throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
    	return this.getOperatorFromNumber(number).getSMSListFromNumber(number);
    }
    
    /**
     * Getter the Operator of a Specific Phone Number Prefix
     * @param number	the number which we need to know it prefix Operator
     * @return		the Operator of a specific Number
     * @throws UnrecognisedPrefix	if the Prefix used isn't recognised by the network
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    public Operator getOperatorFromNumber(String number)
    		throws UnrecognisedPrefix, PhoneNumberNotValid {
    	return this.getOperatorFromPrefix(getOperatorPrefixfromNumber(number));
    }
    
    /**
     * @param number	the number which we need to know it prefix Operator
     * @return	the OperatorPrefix of a specific Number
     * @throws PhoneNumberNotValid if the number isn't valid in Network Context
     */
    protected static String getOperatorPrefixfromNumber(String number) 
    		throws PhoneNumberNotValid {
    	if(FieldVerifier.getInstance().validPhoneNumber(number)) {
    		return number.substring(0, 2);
    	}
    	throw new PhoneNumberNotValid(number);
    }
}
