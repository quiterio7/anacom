package anacom.shared.dto;

import java.io.Serializable;

public abstract class AnacomDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public AnacomDTO() {}

	/**
	 * @param o		any java object
	 * @return 		true if the dto has the same info.
	 */
	@Override
	public abstract boolean equals(Object o);	
	
	protected String getPrefixFromNumber(String number){
		return number.substring(0, 2);
	}
}
