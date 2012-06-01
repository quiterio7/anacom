package anacom.shared.dto;

public class OperatorDetailedDTO extends AnacomDTO {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Operator Data
	 */
	private String name, prefix;
	private int smsCost, voiceCost, videoCost;
	private int tax;
	private int bonus;
	
	public OperatorDetailedDTO() {
	}
	
	/**
	 * Constructor
	 * @param name		Operator Name
	 * @param prefix	Operator Prefix
	 * @param smsCost	Operator SMS COST
	 * @param voiceCost	Operator VOICE COST
	 * @param videoCost	Operator VIDEO COST
	 * @param tax		Operator TAX
	 * @param bonus		Operator TAX BONUS
	 */
	public OperatorDetailedDTO(String name,
							   String prefix,
							   int smsCost,
							   int voiceCost, 
							   int videoCost,
							   int tax,
							   int bonus) {
		this.name = name;
		this.prefix = prefix;
		this.smsCost = smsCost;
		this.voiceCost = voiceCost;
		this.videoCost = videoCost;
		this.tax = tax;
		this.bonus = bonus;
	}
	
	/**
	 *	Get the Name of a Specific Operator  
	 * @return the Name of Operator
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Get the Prefix of a Specific Operator
	 * @return the Prefix of Operator
	 */
	public String getPrefix() {
		return this.prefix;
	}
	
	/**
	 * Get the SMS COST of a Specific Operator
	 * @return sms cost of an Operator
	 */
	public int getSmsCost() {
		return this.smsCost;
	}
	
	/**
	 * Get the VOICE COST of a Specific Operator
	 * @return voice cost of an Operator
	 */
	public int getVoiceCost() {
		return this.voiceCost;
	}
	
	/**
	 * Get the VIDEO COST of a Specific Operator
	 * @return video cost of an Operator
	 */
	public int getVideoCost() {
		return this.videoCost;
	}
	
	/**
	 * Get the TAX of a Specific Operator
	 * @return the Tax of Operator
	 */
	public int getTax() {
		return this.tax;
	}
	
	/**
	 * Get the TAX BONUS of a Specific Operator
	 * @return the Tax Bonus of Operator
	 */
	public int getBonus() {
		return this.bonus;
	}
	
	/**
	 * @param o						any java object
	 * @return 						true if the dto has the same info.
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof OperatorDetailedDTO) {
			OperatorDetailedDTO dto = (OperatorDetailedDTO) o;
			if(		dto.getBonus() == this.getBonus() &&
					dto.getName().equals(this.getName()) &&
					dto.getPrefix().equals(this.getPrefix()) &&
					dto.getSmsCost() == this.getSmsCost() &&
					dto.getTax() == this.getTax() &&
					dto.getVideoCost() == this.getVideoCost() &&
					dto.getVoiceCost() == this.getVoiceCost())
				return true;
		}
		return false;
	}
}
