package anacom.shared.dto;

import java.util.List;

public class PhoneReceivedSMSListDTO extends AnacomDTO {

	private static final long serialVersionUID = 9123912923L;
	private String number;
	private List<ReceivedSMSDTO> smsList;

	/**
	 * Constructor of SMS Message List of a specific number
	 * @param number	the Phone's Number 
	 * @param list		the Phone's SMS List
	 */
	public PhoneReceivedSMSListDTO(String number, List<ReceivedSMSDTO> list) {
		this.number = number;
		this.smsList = list;
	}
	
	public PhoneReceivedSMSListDTO() {}
	
	/**
	 * Getter of the number of the phone passed by the constructor
	 * @return	the Number of the Phone
	 */
	public String getNumber() {
		return this.number;
	}
	
	/**
	 * Getter of the SMS List of the phone passed by the constructor
	 * @return	the SMS' List of the Phone
	 */
	public List<ReceivedSMSDTO> getSMSList() {
		return this.smsList;
	}
	
	/**
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof PhoneReceivedSMSListDTO) {
			PhoneReceivedSMSListDTO dto = (PhoneReceivedSMSListDTO) o;
			if(		dto.getNumber().equals(this.getNumber()) && 
					dto.getSMSList().equals(this.getSMSList())) {
				return true;
			}
		}
		return false;
	}


}
