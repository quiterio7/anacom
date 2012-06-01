package anacom.shared.dto;

public class PhoneNumberDTO extends AnacomDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String number;
	
	public PhoneNumberDTO() {
	}
	
	/**
	 * Constructor
	 * @param number	the number of the Phone
	 */
	public PhoneNumberDTO(String number) { 
		this.number = number; 
	}

	/**
	 * Gets the number of the PhoneDTO
	 * @return	the number of the PhoneDTO
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
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof PhoneNumberDTO) {
			PhoneNumberDTO dto = (PhoneNumberDTO) o;
			if(dto.getNumber().equals(this.getNumber())) {
				return true;
			}
		}
		return false;
	}

}