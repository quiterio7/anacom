package anacom.shared.dto;

public class RegisterPhoneDTO extends AnacomDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String type, number, operatorPrefix;
	
	public RegisterPhoneDTO() {}
	
	/**
	 * Constructor
	 * @param type		the type of the new Phone
	 * @param number	the number of the Phone
	 * @param type		the type of the Phone
	 */
	public RegisterPhoneDTO(String type, String number, String operatorPrefix) {
		this.type = type;
		this.number = number;
		this.operatorPrefix = operatorPrefix;
	}
	
	/**
	 * Gets the type of the Phone to be created
	 * @return	the Phone type
	 */
	public String getType() { 
		return this.type; 
	}

	/**
	 * Gets the number of the PhoneCreateDTO
	 * @return	the number of the PhoneCreateDTO
	 */
	public String getNumber() { 
		return this.number; 
	}
	
	/**
	 * Gets the prefix of the PhoneCreateDTO
	 * @return	the prefix of the PhoneCreateDTO
	 */
	public String getOperatorPrefix() { 
		return this.operatorPrefix;
	}
	
	/**
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof RegisterPhoneDTO) {
			RegisterPhoneDTO dto = (RegisterPhoneDTO) o;
			if(		dto.getNumber().equals(this.getNumber()) &&
					dto.getOperatorPrefix().equals(this.getOperatorPrefix()) &&
					dto.getType().equals(this.getType())) {
				return true;
			}
		}
		return false;
	}
}