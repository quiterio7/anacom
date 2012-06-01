package anacom.service.test;

import pt.ist.fenixframework.pstm.Transaction;
import anacom.domain.Operator;
import anacom.services.IncreasePhoneBalanceService;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.BalanceLimitExceeded;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.PhoneNotExists;

public class IncreasePhoneBalanceServiceTest extends AnacomServiceTestCase {
	
	/**
	 * Operator DATA 
	 */
	private final static String OPERATOR_NAME = "TMN";
	private final static String OPERATOR_PREFIX = "91";
	private final static int SMS_COST = 10;
	private final static int VOICE_COST = 10;
	private final static int VIDEO_COST = 10;
	private final static int TAX = 10;
	private final static int BONUS = 50;
	private final static int NO_BONUS = 0;
	
	/**
	 * Phone DATA
	 */
	private final static String EXISTING_NUMBER = "911311111";
	private final static String NON_EXISTING_NUMBER = "911111111";
	private final static String NON_EXISTING_OPERATOR = "961111111";
	private final static int VALID_INCREASE = 10;
	private final static int OVERFLOW_INCREASE = 10001;
	private final static int INVALID_INCREASE = -10;
	private final static int BONUS_OVERFLOW_INCREASE = 9000;
	
	/**
	 * Test Constructor 
	 * @param name the name of the Test Case. For dynamic invocation.
	 */
	public IncreasePhoneBalanceServiceTest(String name) {
		super(name);
	}
	
	/**
	 * Test Constructor 
	 */
	public IncreasePhoneBalanceServiceTest(){
		super();
	}
	
	/**
	 * Adding by default one Operator because we need to register
	 * new phones to test adding
	 */
	@Override
	public void setUp() {
		super.setUp();
		addOperator(
				OPERATOR_NAME,
				OPERATOR_PREFIX,
				SMS_COST,
				VOICE_COST,
				VIDEO_COST,
				TAX,
				NO_BONUS);
		addPhone2G(EXISTING_NUMBER);
	}
	
	/**
	 * Calculates the balance increase with bonus
	 * @param amount	the amount given to the increase balance service
	 * @return			the amount that should be added to the Phone's
	 * 					balance
	 */
	private int addBonus(int amount) {
		return amount + (amount*BONUS)/100;
	}
	
	/**
	 * Adds a bonus factor to the test Operator
	 */
	public void addBonusToOperator() {
		Operator op = getOperator(OPERATOR_PREFIX);
		boolean commited = false;
		try {
			Transaction.begin();
			op.setBonus(BONUS);
			Transaction.commit();
			commited = true;
		} finally { if(!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Asserts that the balance of the test Phone remains unchanged
	 * @param initialBalance	the balance before the test
	 */
	public void assertPhoneBalanceUnchanged(int initialBalance) {
		assertEquals("The Phone's balance should be " + initialBalance
				+ " but it is " + getPhoneBalance(EXISTING_NUMBER), initialBalance,
				getPhoneBalance(EXISTING_NUMBER));
	}
	
	/**
	 * Test 1 - test increasing a Phone's balance with a valid amount
	 * without bonus (the balance is increased twice and verified in
	 * each addition)
	 */
	public void testSuccessfulIncrease() {
	    // Arrange
		IncreasePhoneBalanceDTO dto
			= new IncreasePhoneBalanceDTO(EXISTING_NUMBER, VALID_INCREASE);
		IncreasePhoneBalanceService addService = new IncreasePhoneBalanceService(dto);
		
		for (int i = 0, balanceBefore = getPhoneBalance(EXISTING_NUMBER);
				i < 2;
				++i, balanceBefore = getPhoneBalance(EXISTING_NUMBER)) {
			// Act
			addService.execute();
			
			// Assert
			assertEquals("The Phone's balance after increase " + (i + 1)
					+ " should be " + (balanceBefore + VALID_INCREASE) + " but it is "
					+ getPhoneBalance(EXISTING_NUMBER),
					balanceBefore + VALID_INCREASE, getPhoneBalance(EXISTING_NUMBER));
		}
	}
	
	/**
	 * Test 2 - test increasing a Phone's balance with a valid
	 * amount with bonus
	 */
	public void testSuccessfulIncreaseWithBonus() {
		// Arrange
		IncreasePhoneBalanceDTO dto
			= new IncreasePhoneBalanceDTO(EXISTING_NUMBER, VALID_INCREASE);
		IncreasePhoneBalanceService addService = new IncreasePhoneBalanceService(dto);
		addBonusToOperator();
		int balanceBefore = getPhoneBalance(EXISTING_NUMBER);

		// Act
		addService.execute();
					
		// Assert
		assertEquals("The Phone's balance after increase should be "
				+ (balanceBefore + addBonus(VALID_INCREASE)) + " but it is "
				+ getPhoneBalance(EXISTING_NUMBER),
				balanceBefore + addBonus(VALID_INCREASE),
				getPhoneBalance(EXISTING_NUMBER));
	}
	
	/**
	 * Test 3 - test increasing the balance of a non existing
	 * Phone number in an existing Operator
	 */
	public void testInexistentPhone() {
	    // Arrange
		IncreasePhoneBalanceDTO dto
			= new IncreasePhoneBalanceDTO(NON_EXISTING_NUMBER, VALID_INCREASE);
		IncreasePhoneBalanceService addService = new IncreasePhoneBalanceService(dto);
		String exceptionNumber = null;
		int balanceBefore = getPhoneBalance(EXISTING_NUMBER);

		// Act
		try {
			addService.execute();
			fail("Should have thrown PhoneNotExists");
		} catch(PhoneNotExists pne) {
			exceptionNumber = pne.getNumber();
		}
		
	    // Assert
		assertEquals("The number in the exception should be " + NON_EXISTING_NUMBER
				+ " but it was " + exceptionNumber,
				exceptionNumber, NON_EXISTING_NUMBER);
		// existing Phone's balance should remain unchanged
		assertPhoneBalanceUnchanged(balanceBefore);
	}
	
	/**
	 * Test 4 - test increasing the balance of a Phone number
	 * with the prefix of an Operator that doesn't exist
	 */
	public void testInexistentOperator() {
		// Arrange
		IncreasePhoneBalanceDTO dto
			= new IncreasePhoneBalanceDTO(NON_EXISTING_OPERATOR, VALID_INCREASE);
		IncreasePhoneBalanceService addService = new IncreasePhoneBalanceService(dto);
		String exceptionPrefix = null;
		int balanceBefore = getPhoneBalance(EXISTING_NUMBER);
		
		// Act
		try {
			addService.execute();
			fail("Should have thrown UnrecognisedPrefix");
		} catch(UnrecognisedPrefix up) {
			exceptionPrefix = up.getPrefix();
		}
				
	    // Assert
		assertEquals("The prefix in the exception should be "
				+ NON_EXISTING_OPERATOR.substring(0, 2) + " but it was "
				+ exceptionPrefix,
				NON_EXISTING_OPERATOR.subSequence(0, 2), exceptionPrefix);
		// existing Phone's balance should remain unchanged
		assertPhoneBalanceUnchanged(balanceBefore);
	}
	
	/**
	 * Test 5 - test increasing the balance of a Phone with
	 * an invalid amount
	 */
	public void testInvalidAmount() {
		// Arrange
		IncreasePhoneBalanceDTO dto
			= new IncreasePhoneBalanceDTO(EXISTING_NUMBER, INVALID_INCREASE);
		IncreasePhoneBalanceService addService = new IncreasePhoneBalanceService(dto);
		String exceptionNumber = null;
		int exceptionAmount = 0;
		int balanceBefore = getPhoneBalance(EXISTING_NUMBER);
		
		// Act
		try {
			addService.execute();
			fail("Should have thrown InvalidAmount");
		} catch(InvalidAmount ia) {
			exceptionNumber = ia.getNumber();
			exceptionAmount = ia.getAmount();
		}
						
	    // Assert
		assertEquals("The number in the exception should be " + EXISTING_NUMBER
				+ " but it was " + exceptionNumber, exceptionNumber, EXISTING_NUMBER);
		assertEquals("The amount in the exception should be " + INVALID_INCREASE
				+ " but it was " + exceptionAmount, exceptionAmount, INVALID_INCREASE);
		assertPhoneBalanceUnchanged(balanceBefore);
	}
	
	/**
	 * Test 6 - test increasing the balance of a Phone past
	 * its limit without a bonus
	 */
	public void testBalanceOverflow() {
		// Arrange
		IncreasePhoneBalanceDTO dto
			= new IncreasePhoneBalanceDTO(EXISTING_NUMBER, OVERFLOW_INCREASE);
		IncreasePhoneBalanceService addService = new IncreasePhoneBalanceService(dto);
		String exceptionNumber = null;
		int balanceBefore = getPhoneBalance(EXISTING_NUMBER);
				
		// Act
		try {
			addService.execute();
			fail("Should have thrown BalanceLimitExceeded");
		} catch(BalanceLimitExceeded ble) {
			exceptionNumber = ble.getNumber();
		}
								
	    // Assert
		assertEquals("The number in the exception should be " + EXISTING_NUMBER
				+ " but it was " + exceptionNumber, exceptionNumber, EXISTING_NUMBER);
		assertPhoneBalanceUnchanged(balanceBefore);
	}
	
	/**
	 * Test 7 - test increasing the balance of a Phone in
	 * such a way that the added amount wouldn't cause a
	 * balance Overflow, but with the bonus it does
	 */
	public void testBonusOverflow() {
		// Arrange
		IncreasePhoneBalanceDTO dto
			= new IncreasePhoneBalanceDTO(EXISTING_NUMBER, BONUS_OVERFLOW_INCREASE);
		IncreasePhoneBalanceService addService = new IncreasePhoneBalanceService(dto);
		String exceptionNumber = null;
		int balanceBefore = 0;
		
		// Act
		addService.execute(); // without bonus, shouldn't cause an exception
		setUp(); // reset test database
		balanceBefore = getPhoneBalance(EXISTING_NUMBER);
		addBonusToOperator();
		try {
			addService.execute();
			fail("Should have thrown BalanceLimitExceeded");
		} catch(BalanceLimitExceeded ble) {
			exceptionNumber = ble.getNumber();
		}
		
		// Assert
		assertEquals("The number in the exception should be " + EXISTING_NUMBER
				+ " but it was " + exceptionNumber, exceptionNumber, EXISTING_NUMBER);
		assertPhoneBalanceUnchanged(balanceBefore);
	}
	
}
