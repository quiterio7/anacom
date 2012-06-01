package anacom.shared.dto;

public class PhoneStateDTO  extends AnacomDTO {

	private static final long serialVersionUID = -3143585616222374225L;
	
	private String number;
	private String state;
	
	/**
	 * 	Constructor
	 * @param number	The phone Number
	 * @param newState	The phone State
	 */
	public PhoneStateDTO(String number, String newState) {
		this.number = number;
		this.state = newState;
	}
	
	// Note that this is needed because of GWT...
	public PhoneStateDTO() {
		
	}
	
	/**
	 * Getter of the Phone's Number
	 * @return the Number of the Phone
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
	 * Getter of the Phone's State
	 * @return the State of the Phone
	 */
	public String getState() {
		return this.state;
	}
	
	/**
	 * @param o						any java object
	 * @return 						true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof PhoneStateDTO) {
			PhoneStateDTO dto = (PhoneStateDTO) o;
			if(		dto.getNumber().equals(this.getNumber()) && 
					dto.getState().equals(this.getState())) {
				return true;
			}
		}
		return false;
	}
}