package anacom.shared.exceptions.operator;

public class OperatorAlreadyExists extends OperatorException {

	private static final long serialVersionUID = 1L;
	private int smsCost;
	private int voiceCost;
	private int videoCost;
	private int tax;
	private int bonus;
	
	public OperatorAlreadyExists() {}
	
	/**
	 * Constructor
	 * @param name			the name of the existing operator
	 * @param prefix		the prefix of the existing operator
	 * @param smsCost		the sms cost of the existing operator
	 * @param voiceCost		the voice cost of the existing operator
	 * @param videoCost		the video cost of the existing operator
	 * @param tax			the tax of the existing operator
	 * @param bonus			the bonus of the existing operator
	 */
	public OperatorAlreadyExists(String name,
								 String prefix,
								 int smsCost,
								 int voiceCost,
								 int videoCost,
								 int tax,
								 int bonus) {
		super(name, prefix);
		this.smsCost = smsCost;
		this.voiceCost = voiceCost;
		this.videoCost = videoCost;
		this.tax = tax;
		this.bonus = bonus;
	}
	
	/**
	 * @return	the operator's sms cost
	 */
	public int getSMSCost() { 
		return this.smsCost; 
	}

	/**
	 * @return	the operator's voice cost
	 */
	public int getVoiceCost() { 
		return this.voiceCost; 
	}

	/**
	 * @return	the operator's video cost
	 */
	public int getVideoCost() { 
		return this.videoCost; 
	}

	/**
	 * @return	the operator's tax
	 */
	public int getTax() { 
		return this.tax; 
	}
	
	/**
	 * @return	the operator's bonus
	 */
	public int getBonus() { 
		return this.bonus; 
	}
}
