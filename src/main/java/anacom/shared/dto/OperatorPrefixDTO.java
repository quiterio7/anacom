package anacom.shared.dto;

public class OperatorPrefixDTO extends AnacomDTO {

	private static final long serialVersionUID = 1L;
	
	private String prefix;
	
	public OperatorPrefixDTO() {
	}
	
	/**
	 * Constructor
	 * @param prefix	the prefix of the Operator
	 */
	public OperatorPrefixDTO(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * Gets the prefix stored in this DTO
	 * @return	the prefix of the Operator represented in this DTO
	 */
	public String getPrefix() {
		return this.prefix;
	}
	
	/**
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof OperatorPrefixDTO) {
			OperatorPrefixDTO dto = (OperatorPrefixDTO) o;
			if(dto.getPrefix().equals(this.getPrefix())) {
				return true;
			}
		}
		return false;
	}
}