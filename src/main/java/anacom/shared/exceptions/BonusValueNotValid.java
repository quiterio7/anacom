package anacom.shared.exceptions;

// FIXME: isto devia ser uma OperatorException - neves
public class BonusValueNotValid extends AnacomException {

	private static final long serialVersionUID = 1L;
	private int bonus;
	
	public BonusValueNotValid() {}
	
	/**  
	 * Constructor
+	 * @param bonus		the bonus that caused the exception
	 */
	public BonusValueNotValid(int bonus) {
		this.bonus = bonus;
	}
	
	/**
	 * Gets the bonus that caused the exception
	 * @return	the bonus
	 */
	public int getBonus() { 
		return this.bonus; 
	}

}