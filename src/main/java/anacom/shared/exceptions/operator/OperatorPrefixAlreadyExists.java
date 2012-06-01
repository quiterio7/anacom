package anacom.shared.exceptions.operator;

public class OperatorPrefixAlreadyExists extends OperatorAlreadyExists {

	private static final long serialVersionUID = 1L;
	
	public OperatorPrefixAlreadyExists() {}
	
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
	public OperatorPrefixAlreadyExists(String name,
									   String prefix,
									   int smsCost,
									   int voiceCost,
									   int videoCost,
									   int tax,
									   int bonus) {
		super(name, prefix, smsCost, voiceCost, videoCost, tax, bonus);
	}
	
}
