package anacom.shared.misc;

import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;
import anacom.shared.misc.externRepresentation.phoneType.PhoneTypeRepresentation;

/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is not translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */

public class FieldVerifier {

	private static FieldVerifier instance;
	
	/**
	 * Private constructor (for singleton)
	 */
	private FieldVerifier() {}
	
	/**
	 * Gets an instance of FieldVerifier (singleton)
	 * @return	an instance of FieldVerifier
	 */
	public static FieldVerifier getInstance() {
		if(instance == null) {
			instance = new FieldVerifier();
		}
		return instance;
	}
	
	/**
	 * @param str	the desired string
	 * @return		true if given string is null or an empty string, false
	 * 				otherwise
	 */
	private boolean emptyString(String str) {
		return str == null || str.length() == 0;
	}
	
	/**
	 * @param name	the given operator name
	 * @return		true if the operator name is valid, false otherwise
	 */
	public boolean validOperatorName(String name) {
		return !emptyString(name);
	}
	
	/**
	 * @param prefix	the given prefix
	 * @return			true if the operator prefix is valid, false otherwise
	 */
	public boolean validOperatorPrefix(String prefix) {
		return prefix != null && prefix.length() == 2 && prefix.matches("[0-9]*");
	}
	
	/**
	 * @param number	the given number
	 * @return			true if the Phone number is valid, false otherwise
	 */
	public boolean validPhoneNumber(String number) {
		return number != null && number.length() == 9 && number.matches("[0-9]*");
	}
	
	/**
	 * @param type	the given Phone type
	 * @return		true if given Phone type is a valid type, false otherwise
	 */
	public boolean validPhoneType(String type) {
		PhoneTypeRepresentation types = PhoneTypeRepresentation.getInstance();
		return type.equals(types.get2GType()) ||
				type.equals(types.get3GType());
	}
	
	/**
	 * @param state		the given Phone state
	 * @return			true if given Phone state is a valid state, false otherwise
	 */
	public boolean validPhoneState(String state) {
		PhoneStateRepresentation states = PhoneStateRepresentation.getInstance();
		return state.equals(states.getOnState()) ||
				state.equals(states.getOffState()) ||
				state.equals(states.getOccupiedState()) ||
				state.equals(states.getSilentState());
	}
	
	/**
	 * @param sms	the given sms message
	 * @return		true if the sms message is valid, false otherwise
	 */
	public boolean validMessage(String sms) {
		return !emptyString(sms);
	}
	
	/**
	 * Checks if a given duration is valid
	 * @param duration	the given duration
	 * @return			true if the given duration is valid, false otherwise
	 */
	public boolean validDuration(int duration) {
		return duration > 0;
	}
	
	/**
	 * @param amount	the amount to be charged to the phone
	 * @return			true if the amount is bigger than 0, false otherwise
	 */
	public boolean validChargeAmount(int amount) {
		return amount > 0;
	}
	
	/**
	 * @param amount	the amount to be charge to the phone in a String
	 * @return			true if the amount is a valid Integer and bigger
	 * 					than 0, false otherwise
	 */
	public boolean validChargeAmount(String amount) {
		try {
			int amountInt = Integer.parseInt(amount);
			return validChargeAmount(amountInt);
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	/**
	 * @param bonus		the bonus to be used on Operator
	 * @return			true if the bonus is bigger or equal
	 * 					than 0, false otherwise
	 */
	public boolean validBonusTax(int bonus) {
		return bonus >= 0;
	}
	
}
