package anacom.service.test;

import pt.ist.fenixframework.pstm.Transaction;
import anacom.domain.Operator;
import anacom.services.CreateNewOperatorService;
import anacom.shared.dto.OperatorDetailedDTO;
import anacom.shared.exceptions.BonusValueNotValid;
import anacom.shared.exceptions.operator.OperatorNameAlreadyExists;
import anacom.shared.exceptions.operator.OperatorNameNotValid;
import anacom.shared.exceptions.operator.OperatorPrefixAlreadyExists;
import anacom.shared.exceptions.operator.OperatorPrefixNotValid;

public class CreateNewOperatorServiceTest extends AnacomServiceTestCase {
	
	private final static String EXISTING_OPERATOR_NAME = "TMN";
	private final static String NON_EXISTING_OPERATOR_NAME = "VODAFONE";
	 
	private final static String EXISTING_OPERATOR_PREFIX = "96";
	private final static String NON_EXISTING_OPERATOR_PREFIX = "91";
	
	private final static int SMS_COST = 1;
	private final static int VOICE_COST = 2;
	private final static int VIDEO_COST = 2;
	private final static int TAX = 1;
	private final static int BONUS = 0;
	
	private final static String INVALID_NULL_PREFIX = null;
	private final static String INVALID_SMALLER_PREFIX = "9";
	private final static String INVALID_BIGGER_PREFIX = "911";
	private final static String INVALID_LETTERS_PREFIX = "aa";
	private final static int INVALID_BONUS = -1;
	
	// Used to verify if already existing Operator remains the same
	private final static OperatorDetailedDTO existingOperatorDTO
		= new OperatorDetailedDTO(EXISTING_OPERATOR_NAME,
								  EXISTING_OPERATOR_PREFIX,
								  SMS_COST,
								  VOICE_COST,
								  VIDEO_COST,
								  TAX,
								  BONUS);
	
	/**
	 * Constructor
	 * @param name	the name of the Test Case. For dynamic invocation.
	 */
	public CreateNewOperatorServiceTest(String name) {
		super(name);
	}
	
	/**
	 * Constructor
	 */
	public CreateNewOperatorServiceTest() {
		super();
	}
	
	/**
     * Sets up the test by cleaning all the information in the Network
     * and by adding a known Operator.
     * This method is always called before each test.
     */
	@Override
	public void setUp() {
		super.setUp();
		addOperator(
				EXISTING_OPERATOR_NAME,
				EXISTING_OPERATOR_PREFIX,
				SMS_COST,
				VOICE_COST,
				VIDEO_COST,
				TAX,
				BONUS);
	}
	
	/**
	 * Compares an Operator with the information given in an
	 * OperatorDetailedDTO. This is used to guarantee that the operator
	 * is created with the correct data.
	 * @param op	the Operator
	 * @param dto	the OperatorDetailedDTO used in the creation of the
	 * 				given Operator
	 * @return		true if data in both is equal, false otherwise
	 */
	private boolean operatorEqualsDTO(Operator op, OperatorDetailedDTO dto) {
		boolean commited = false;
		try {
			boolean res = false;
			Transaction.begin();
			if (op.getName().equals(dto.getName()) &&
					op.getPrefix().equals(dto.getPrefix()) &&
					op.getSmsCost() == dto.getSmsCost() &&
					op.getVoiceCost() == dto.getVoiceCost() &&
					op.getVideoCost() == dto.getVideoCost() &&
					op.getTax() == dto.getTax() &&
					op.getBonus() == dto.getBonus()) {
				res = true;
			}
			Transaction.commit();
			commited = true;
			return res;
		} finally { if (!commited) { Transaction.abort(); } }
	}
	
	/**
	 * @param prefix	an Operator's prefix
	 * @return			true if that Operator's Phone list is empty, false
	 * 					otherwise
	 */
	private boolean isOperatorPhoneListEmpty(String prefix) {
		boolean commited = false;
		try {
			Operator op = getOperator(prefix);
			boolean res = false;
			Transaction.begin();
			res = op.getPhone().isEmpty();
			Transaction.commit();
			commited = true;
			return res;
		} finally { if(!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Convert data in the given Operator to a String
	 * @param op	the desired Operator
	 * @return		a formatted String with the data in the Operator
	 */
	private String operatorToString(Operator op) {
		boolean commited = false;
		try {
			String res = null;
			Transaction.begin();
			res = "Name:" + op.getName() + ", Prefix:" + op.getPrefix()
					+ ", SMSCost:" + op.getSmsCost() + ", VoiceCost:"
					+ op.getVoiceCost() + ", VideoCost:" + op.getVideoCost()
					+ ", Tax:" + op.getTax() + ", Bonus:" + op.getBonus();
			Transaction.commit();
			commited = true;
			return res;
		} finally { if(!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Convert data in the given OperatorDetailedDTO to a String
	 * @param dto	the desired OperatorDetailedDTO
	 * @return		a formatted String with the data in the given DTO
	 */
	private String operatorDetailedDTOToString(OperatorDetailedDTO dto) {
		return "Name:" + dto.getName() + ", Prefix:" + dto.getPrefix() +  ", SMSCost:"
				+ dto.getSmsCost() + ", VoiceCost:" + dto.getVoiceCost() + ", VideoCost:"
			    + dto.getVideoCost() + ", Tax:" + dto.getTax() + ", Bonus:" + dto.getBonus();
	}
	
	/**
	 * Checks if the existing Operator remains unchanged
	 */
	private void assertExistingOperatorUnchanged() {
		assertTrue("Existing operator shouldn't have been removed from network, "
				+ "but it was", operatorPrefixExists(EXISTING_OPERATOR_PREFIX));
		assertTrue("The already existing Operator should have the following data ("
				+ operatorDetailedDTOToString(existingOperatorDTO) + ") but it "
				+ "was (" + operatorToString(getOperator(EXISTING_OPERATOR_PREFIX))
				+ ")",
				operatorEqualsDTO(getOperator(EXISTING_OPERATOR_PREFIX),
				existingOperatorDTO));
		assertTrue("The already existing Operator's Phone list should have "
				+ "remained empty, but it's not",
				isOperatorPhoneListEmpty(EXISTING_OPERATOR_PREFIX));
	}
  
	/**
	 * Test 1 - tests addition of an Operator with a non existing name and
	 * a non existing prefix
	 */
	public void testSuccessfulAddition() {
	    // Arrange
		OperatorDetailedDTO dto
			= new OperatorDetailedDTO(NON_EXISTING_OPERATOR_NAME, 
				  					  NON_EXISTING_OPERATOR_PREFIX, 
				  					  SMS_COST,
				  					  VOICE_COST,
				  					  VIDEO_COST,
				  					  TAX,
				  					  BONUS);
		CreateNewOperatorService addService = new CreateNewOperatorService(dto);
		int operatorsCount = countOperatorsInNetwork();

		// Act
		addService.execute();

	    //Assert
		assertTrue("New operator should have been added to network, but it wasn't",
				   operatorPrefixExists(NON_EXISTING_OPERATOR_PREFIX));
		assertEquals("The number of operators should have been increased by 1, but it was "
				   + "increased by " + (countOperatorsInNetwork() - operatorsCount),
			       operatorsCount + 1, countOperatorsInNetwork());
		assertTrue("The created Operator should have the following data ("
			       + operatorDetailedDTOToString(dto) + ") but it was ("
			       + operatorToString(getOperator(NON_EXISTING_OPERATOR_PREFIX)),
			       operatorEqualsDTO(getOperator(NON_EXISTING_OPERATOR_PREFIX), dto));
		assertTrue("The new Operator's Phone list should be empty, but it's not",
				   isOperatorPhoneListEmpty(NON_EXISTING_OPERATOR_PREFIX));
		assertExistingOperatorUnchanged();
	}
	
	/**
	 * Test 2 - tests the addition of an Operator with an used name and
	 * an used prefix.
	 */
	public void testExistingNameExistingPrefix() {
	    // Arrange		
		OperatorDetailedDTO dto
			= new OperatorDetailedDTO(EXISTING_OPERATOR_NAME, 
				  					  EXISTING_OPERATOR_PREFIX, 
				  					  SMS_COST,
				  					  VOICE_COST,
				  					  VIDEO_COST,
				  					  TAX,
				  					  BONUS);
		CreateNewOperatorService addService = new CreateNewOperatorService(dto);
		String exceptionOperatorName = null;
		String exceptionOperatorPrefix = null;
		int operatorsCount = countOperatorsInNetwork();

		// Act
		try {
			addService.execute();
			fail("Should have thrown OperatorNameAlreadyExists or"
					+ "OperatorPrefixAlreadyExists");
		} catch(OperatorNameAlreadyExists onae) {
			exceptionOperatorName = onae.getName();
			exceptionOperatorPrefix = onae.getPrefix();
		} catch(OperatorPrefixAlreadyExists opae) {
			exceptionOperatorName = opae.getName();
			exceptionOperatorPrefix = opae.getPrefix();
		}

	    //Assert
		assertEquals("The names of the existing Operator in the exception should be "
				+ EXISTING_OPERATOR_NAME + " but it was " + exceptionOperatorName,
				EXISTING_OPERATOR_NAME, exceptionOperatorName);
		assertEquals("The prefix of the existing Operator in the exception should be "
				+ EXISTING_OPERATOR_PREFIX + " but it was " + exceptionOperatorPrefix,
				EXISTING_OPERATOR_PREFIX, exceptionOperatorPrefix);
		assertEquals("The number of Operators should have remained " + operatorsCount
				+ " but it was " + countOperatorsInNetwork(),
				operatorsCount, countOperatorsInNetwork());
		assertExistingOperatorUnchanged();
	}
	
	/**
	 * Test 3 - tests the addition of an Operator with an used name
	 */
	public void testExistingName() {
	    // Arrange		
		OperatorDetailedDTO dto
			= new OperatorDetailedDTO(EXISTING_OPERATOR_NAME, 
				  					  NON_EXISTING_OPERATOR_PREFIX, 
				  					  SMS_COST,
				  					  VOICE_COST,
				  					  VIDEO_COST,
				  					  TAX,
				  					  BONUS);
		CreateNewOperatorService addService = new CreateNewOperatorService(dto);
		String exceptionOperatorName = null;
		int operatorsCount = countOperatorsInNetwork();

		// Act
		try {
			addService.execute();
			fail("Should have thrown OperatorNameAlreadyExists");
		} catch(OperatorNameAlreadyExists onae) {
			exceptionOperatorName = onae.getName();
		}

	    //Assert
		assertEquals("The name of the existing Operator in the exception should be "
				+ EXISTING_OPERATOR_NAME + " but it was " + exceptionOperatorName,
				EXISTING_OPERATOR_NAME, exceptionOperatorName);
		assertEquals("The number of Operators should have remained " + operatorsCount
				+ " but it was " + countOperatorsInNetwork(),
				operatorsCount, countOperatorsInNetwork());
		assertExistingOperatorUnchanged();
	}
	
	/**
	 * Test 4 - tests the addition of an Operator with an used prefix
	 */
	public void testExistingPrefix() {
	    // Arrange		
		OperatorDetailedDTO dto
			= new OperatorDetailedDTO(NON_EXISTING_OPERATOR_NAME, 
				  					  EXISTING_OPERATOR_PREFIX, 
				  					  SMS_COST,
				  					  VOICE_COST,
				  					  VIDEO_COST,
				  					  TAX,
				  					  BONUS);
		CreateNewOperatorService addService = new CreateNewOperatorService(dto);
		String exceptionOperatorPrefix = null;
		int operatorsCount = countOperatorsInNetwork();

		// Act
		try {
			addService.execute();
			fail("Should have thrown OperatorPrefixAlreadyExists");
		} catch(OperatorPrefixAlreadyExists opae) {
			exceptionOperatorPrefix = opae.getPrefix();
		}

		//Assert
		assertEquals("The prefix of the existing Operator in the exception should be "
				+ EXISTING_OPERATOR_PREFIX + " but it was " + exceptionOperatorPrefix,
				EXISTING_OPERATOR_PREFIX, exceptionOperatorPrefix);
		assertEquals("The number of Operators should have remained " + operatorsCount
				+ " but it was " + countOperatorsInNetwork(),
				operatorsCount, countOperatorsInNetwork());
		assertExistingOperatorUnchanged();
	}
	
	/**
	 * Test 5 - tests the addition of an Operator with an invalid prefix
	 */
	public void testPrefixNotValid() {
		// Arrange
		OperatorDetailedDTO nullPrefixDTO
			= new OperatorDetailedDTO(NON_EXISTING_OPERATOR_NAME, 
				  					  INVALID_NULL_PREFIX, 
				  					  SMS_COST,
				  					  VOICE_COST,
									  VIDEO_COST,
	  								  TAX,
	  								  BONUS);
		OperatorDetailedDTO smallerPrefixDTO
			= new OperatorDetailedDTO(NON_EXISTING_OPERATOR_NAME, 
									  INVALID_SMALLER_PREFIX,
									  SMS_COST,
									  VOICE_COST,
									  VIDEO_COST,
									  TAX,
									  BONUS);
		OperatorDetailedDTO biggerPrefixDTO
			= new OperatorDetailedDTO(NON_EXISTING_OPERATOR_NAME, 
									  INVALID_BIGGER_PREFIX, 
									  SMS_COST,
									  VOICE_COST,
									  VIDEO_COST,
									  TAX,
									  BONUS);
		OperatorDetailedDTO lettersPrefixDTO
			= new OperatorDetailedDTO(NON_EXISTING_OPERATOR_NAME, 
									  INVALID_LETTERS_PREFIX, 
									  SMS_COST,
									  VOICE_COST,
									  VIDEO_COST,
									  TAX,
									  BONUS);
		CreateNewOperatorService nullPrefixService
			= new CreateNewOperatorService(nullPrefixDTO);
		CreateNewOperatorService smallerPrefixService
			= new CreateNewOperatorService(smallerPrefixDTO);
		CreateNewOperatorService biggerPrefixService
			= new CreateNewOperatorService(biggerPrefixDTO);
		CreateNewOperatorService lettersPrefixService
			= new CreateNewOperatorService(lettersPrefixDTO);
		int operatorsCount = countOperatorsInNetwork();
		
		// Act
		try {
			nullPrefixService.execute();
			fail("Should have thrown OperatorPrefixNotValid with a null prefix");
		} catch(OperatorPrefixNotValid opnv) { /* Nothing to do here */ }
		try {
			smallerPrefixService.execute();
			fail("Should have thrown OperatorPrefixNotValid with prefix "
					+ smallerPrefixDTO.getPrefix());
		} catch(OperatorPrefixNotValid opnv) { /* Nothing to do here */ }
		try {
			biggerPrefixService.execute();
			fail("Should have thrown OperatorPrefixNotValid with prefix "
					+ biggerPrefixDTO.getPrefix());
		} catch(OperatorPrefixNotValid opnv) { /* Nothing to do here */ }
		try {
			lettersPrefixService.execute();
			fail("Should have thrown OperatorPrefixNotValid with prefix "
					+ lettersPrefixDTO.getPrefix());
		} catch(OperatorPrefixNotValid opnv) { /* Nothing to do here */ }
		
		// Assert
		assertEquals("The number of Operators should have remained " + operatorsCount
				+ " but it was " + countOperatorsInNetwork(),
				operatorsCount, countOperatorsInNetwork());
		assertExistingOperatorUnchanged();
	}
	
	/**
	 * Test 6 - tests the addition of an Operator with an invalid name
	 */
	public void testNameNotValid() {
		// Arrange
		OperatorDetailedDTO nullPrefixDTO
			= new OperatorDetailedDTO(null, 
				  					  NON_EXISTING_OPERATOR_PREFIX, 
				  					  SMS_COST,
				  					  VOICE_COST,
									  VIDEO_COST,
	  								  TAX,
	  								  BONUS);
		CreateNewOperatorService nullPrefixService
			= new CreateNewOperatorService(nullPrefixDTO);
		int operatorsCount = countOperatorsInNetwork();
		
		// Act
		try {
			nullPrefixService.execute();
			fail("Should have thrown OperatorNameNotValid");
		} catch(OperatorNameNotValid onnv) { /* Nothing to do here */ }
		
		// Assert
		assertEquals("The number of Operators should have remained " + operatorsCount
				+ " but it was " + countOperatorsInNetwork(),
				operatorsCount, countOperatorsInNetwork());
		assertExistingOperatorUnchanged();
	}
	
	/**
	 * Test 7 - tests the addition of an Operator with an invalid bonus
	 */
	public void testBonusNotValid() {
		OperatorDetailedDTO wrongBonusValue2dto
			= new OperatorDetailedDTO(NON_EXISTING_OPERATOR_NAME, 
				  					  NON_EXISTING_OPERATOR_PREFIX, 
				  					  SMS_COST,
				  					  VOICE_COST,
				  					  VIDEO_COST,
				  					  TAX,
				  					  INVALID_BONUS);
		CreateNewOperatorService service
			= new CreateNewOperatorService(wrongBonusValue2dto);
		int exceptionBonus = 0;
		int operatorsCount = countOperatorsInNetwork();
		
		try {
			service.execute();
			fail("Should have thrown BonusValueNotValid");
		} catch(BonusValueNotValid bvnv) {
			exceptionBonus = bvnv.getBonus();
		}
		
		// Assert
		assertEquals("The bonus of the existing Operator in the exception should be "
				+ INVALID_BONUS + " but it was " + exceptionBonus,
				INVALID_BONUS, exceptionBonus);
		assertEquals("The number of Operators should have remained " + operatorsCount
				+ " but it was " + countOperatorsInNetwork(),
				operatorsCount, countOperatorsInNetwork());
		assertExistingOperatorUnchanged();
	}
}
