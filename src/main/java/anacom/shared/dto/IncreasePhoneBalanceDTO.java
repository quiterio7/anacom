package anacom.shared.dto;

public class IncreasePhoneBalanceDTO extends AnacomDTO {

	private static final long serialVersionUID = 1L;
	private String number;
	private int amount;
	
	public IncreasePhoneBalanceDTO() {
	}
	
	/**
	 * @param number
	 * @param amount
	 */
	public IncreasePhoneBalanceDTO(String number, int amount) {
		this.number = number;
		this.amount = amount;
	}
	
	/**
	 * 
	 * @return the Number of Phone
	 */
	public String getNumber() {
		return this.number;
	}

	/**
	 * 
	 * @return the prefix of Operator Phone
	 */
	public String getPrefix() {
		return getPrefixFromNumber(this.getNumber());
	}
	
	/**
	 * @return the amount you want increase.
	 */
	public int getAmount() {
		return this.amount;
	}
	
	/**
	 * @param obj	an Object to which to compare this SMSDTO
	 * @return		true if obj is a SMSDTO equal to this one, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IncreasePhoneBalanceDTO) {
			IncreasePhoneBalanceDTO dto = (IncreasePhoneBalanceDTO)obj;
			if (	dto.getAmount() == this.getAmount() &&
					dto.getNumber().equals(this.getNumber())) {
				return true;
			}
		}
		return false;
	}
}