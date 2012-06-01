package anacom.shared.exceptions.phone;


public class NotPositiveBalance extends PhoneException {
	
	private static final long serialVersionUID = 1L;
	private int balance;
	
	public NotPositiveBalance() {}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone that caused the exception
	 * @param balance	the balance of the Phone
	 */
	public NotPositiveBalance(String number, int balance) {
		super(number);
		this.balance = balance;
	}
	
	/**
	 * @return	the Phone's balance
	 */
	public int getBalance() { 
		return this.balance; 
	}
	
}
