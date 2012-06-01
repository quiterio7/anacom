package anacom.shared.exceptions.phone;


public class InvalidAmount extends PhoneException {

	private static final long serialVersionUID = 1L;
	private int amount;
	
	public InvalidAmount() {}
	
	/**
	 * Constructor
	 * @param number	the number of the phone
	 * @param amount	the amount that caused the exception
	 */
	public InvalidAmount(String number, int amount) {
		super(number);
		this.amount = amount;
	}
	
	/**
	 * Gets the amount that caused the exception
	 * @return	the amount
	 */
	public int getAmount() { 
		return this.amount; 
	}

}