package anacom.shared.dto;

public class PhoneDTO extends AnacomDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String number;
	private int balance;
	
	public PhoneDTO() {
	}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone
	 * @param balance	the balance of the Phone
	 */
	public PhoneDTO(String number, int balance) {
		this.number = number;
		this.balance = balance;
	}

	/**
	 * Gets the number of the PhoneDTO
	 * @return	the number of the PhoneDTO
	 */
	public String getNumber() { 
		return this.number; 
	}
	
	/**
	 * Gets the balance of the PhoneDTO
	 * @return	the balance of the PhoneDTO
	 */
	public int getBalance() { 
		return this.balance; 
	}
	
	/**
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof PhoneDTO) {
			PhoneDTO dto = (PhoneDTO) o;
			if(		dto.getNumber().equals(this.getNumber()) &&
					dto.getBalance() == this.getBalance())
				return true;
		}
		return false;
	}
}