package anacom.shared.dto;

import java.util.List;

public class ListPhonesDTO extends AnacomDTO {
	
	private static final long serialVersionUID = 1L;
	
	private List<PhoneDTO> phones;
	
	public ListPhonesDTO() {
	}
	
	/**
	 * Constructor
	 * @param phones	a list of PhoneDTOs
	 */
	public ListPhonesDTO(List<PhoneDTO> phones) {
		this.phones = phones;
	}

	/**
	 * Gets the list of PhoneDTOs
	 * @return		the list of PhoneDTOs
	 */
	public List<PhoneDTO> getPhoneList()
	{
		return this.phones;
	}
	
	/**
	 * @param o						any java object
	 * @return 						true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof ListPhonesDTO) {
			ListPhonesDTO dto = (ListPhonesDTO) o;
			if(dto.getPhoneList().equals(this.getPhoneList())) {
				return true;
			}
		}
		return false;
	}
}