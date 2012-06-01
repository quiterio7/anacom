package anacom.service.test;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;
import anacom.domain.Communication;
import anacom.domain.Network;
import anacom.domain.OccupiedState;
import anacom.domain.OffState;
import anacom.domain.OnState;
import anacom.domain.Operator;
import anacom.domain.Phone;
import anacom.domain.PhoneState;
import anacom.domain.SilentState;
import anacom.shared.exceptions.phone.invalidState.InvalidState;
import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

/**
 * JUNIT
 * Abstract Class to test Services of Anacom
 *
 */
public abstract class AnacomServiceTestCase extends TestCase {
	static {
    	if(FenixFramework.getConfig()==null) {
    		FenixFramework.initialize(new Config() {{
                dbAlias = "test-db"; 
                domainModelPath="src/main/dml/anacom.dml";
                repositoryType=RepositoryType.BERKELEYDB;
                rootClass = Network.class;
    		}});
    	}
    }
	
	/**
	 * Constructor
	 * @param name	the name of the Test Case. For dynamic invocation.
	 */
	protected AnacomServiceTestCase(String msg) {
    	super(msg);
    }
    
	/**
	 * Constructor
	 */
    protected AnacomServiceTestCase() {
    	super();
    }
    
    /**
     * Sets up the test by cleaning all the information in the Network.
     * This method is always called before each test.
     */
    @Override
    protected void setUp() {
		this.cleanNetwork();
	}	
    
    /**
     * Cleans Network after each test in order to 
     * perform new tests with the same values
     */
    @Override
	protected void tearDown() {
		this.cleanNetwork();
	}
	
	/**
	 *	Cleans the Network 
	 *	All Operators from the Network are removed.
	 */
    protected void cleanNetwork() {
    	boolean committed = false;
    	try {
    		Transaction.begin();
    		Network network= FenixFramework.getRoot();
    		Set<Operator> allOperators = network.getOperatorSet();
    		allOperators.clear();
    		Transaction.commit();
    		committed = true;
    	} finally { if (!committed) { Transaction.abort(); } }
    }
	
	/**
	 * Checks if the Operator exists in this Network
	 * @param operatorName	the name of the operator
	 * @return 				true if the Operator exists, false otherwise
	 */
	protected boolean operatorNameExists(String operatorName) {
		boolean committed = false;
    	try {
    		boolean res = false;
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		res = network.hasOperatorWithName(operatorName);
    		Transaction.commit();
    		committed = true;
    		return res; 
    	} finally { if (!committed) { Transaction.abort(); } }
    }
	
	/**
	 * Checks if the Operator exists in this Network
	 * @param operatorName	the prefix of the Operator
	 * @return 				true if the Operator exists, false otherwise
	 */
	protected boolean operatorPrefixExists(String operatorPrefix) {
    	boolean committed = false;
    	try {
    		boolean res = false;
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		res = network.hasOperatorWithPrefix(operatorPrefix);
    		Transaction.commit();
    		committed = true;
    		return res;
    	} finally { if (!committed) { Transaction.abort(); } }
    }
	
	/**
	 * Gets a specific Operator
	 * @param operatorPrefix	the prefix of the operator
	 * @return the Operator
	 * @throws Exception 
	 */
	protected Operator getOperator(String operatorPrefix) {
    	boolean committed = false;
    	try {
    		Operator res = null;
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		res = network.getOperatorFromPrefix(operatorPrefix);
    		Transaction.commit();
    		committed = true;
    		return res;
    	} finally { if (!committed) { Transaction.abort(); } }
    }
	
	/**
	 * Checks if a Phone exists in some Operator in the Network
	 * @param number  	the number of the phone
	 * @return 			true if the Phone exists, false otherwise
	 */
	protected boolean checkPhone(String number) {
    	boolean committed = false;
    	try {
    		boolean res = false;
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		Operator operator = network.getOperatorFromPrefix(number.substring(0, 1));
    		res = operator.hasPhoneWithNumber(number);
    		Transaction.commit();
    		committed = true;
    		return res;
    	} finally { if (!committed) { Transaction.abort(); } }
    }
	
	/**
	 * Adds an Operator to the Network
	 * @param name		the Operator's name
	 * @param prefix	the Operator's prefix
	 * @param smsCost	the Operator's smsCost
	 * @param voiceCost	the Operator's voiceCost
	 * @param videoCost	the Operator's videoCost
	 * @param tax		the Operator's tax
	 * @param bonus		the Operator's bonus
	 */
	protected void addOperator(String name,
							   String prefix,
							   int smsCost,
							   int voiceCost,
							   int videoCost,
							   int tax,
							   int bonus) {
    	boolean committed = false;
    	try { 
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		network.addOperator(new Operator(name,
    										 prefix,
    										 smsCost,
    										 voiceCost,
    										 videoCost,
    										 tax,
    										 bonus));
    		Transaction.commit(); 
    		committed = true;
    	} finally { if (!committed) { Transaction.abort(); } }
    }
	
	/**
	 * Registers a 2G Phone in an Operator with given number's prefix
	 * @param number	the number of the Phone
	 */
	protected void addPhone2G(String number) {
    	boolean committed = false;
    	try {
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		network.registerPhone2G(number.substring(0, 2), number); 
    		Transaction.commit();
    		committed = true;
    	} finally { if (!committed) { Transaction.abort(); } }
    }
	
	/**
	 * Registers a 3G Phone in an Operator with given number's prefix 
	 * @param number	the number of the Phone
	 */
	protected void addPhone3G(String number) {
    	boolean committed = false;
    	try {
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		network.registerPhone3G(number.substring(0, 2), number); 
    		Transaction.commit();
    		committed = true;
    	} finally { if (!committed) { Transaction.abort(); } }
    }
	
	/**
	 * Gets the number of Operators in the Network
	 * @return the number of Operators
	 */
	protected int countOperatorsInNetwork() {
    	boolean committed = false;
    	try { 
    		int res;
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		res = network.getOperatorCount();
    		Transaction.commit();
    		committed = true;
    		return res;
    	} finally { if (!committed) { Transaction.abort(); } }
	}
	
	/**
	 * Gets the balance of a Phone
	 * @param number	the number of the Phone
	 * @return 			the balance of the Phone
	 */
	protected int getPhoneBalance(String number) {
    	boolean committed = false;
    	try { 
    		int res;
    		Transaction.begin();
	    	Network network = FenixFramework.getRoot();
	    	Operator operator = network.getOperatorFromPrefix(number.substring(0,2));
	    	res = operator.getPhoneBalance(number);
		   	Transaction.commit();
			committed = true;
			return res;
    	} finally { if (!committed) { Transaction.abort(); } }
	}
	
	/**
	 * Increase the given Phone's balance.
	 * @param number	the Phone's number
	 * @param amount	the amount to be added
	 */
	protected void increasePhoneBalance(String number, int amount) {
		boolean commited = false;
		try {
			Transaction.begin();
			Network network = FenixFramework.getRoot();
			network.increasePhoneBalance(number, amount);
			Transaction.commit();
			commited = true;
		} finally { if (!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Get the number of communications received well by a Phone
	 * @param number	the Phone's number
	 * @return 			the total number of received calls
	 */
	protected int countReceivedCallsInPhone(String number) {
    	boolean commited = false;
    	try {
    		int res;
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
		    Operator operator = network.getOperatorFromPrefix(number.substring(0,2));
		    Phone phone = operator.getPhonebyNumber(number);
		    res = phone.getReceivedCallsCount();
	    	Transaction.commit();
	    	commited = true;
	    	return res;
    	} finally { if (!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Get the number of communications executed well by a source Phone
	 * @param number	the Phone's number
	 * @return 			the total number of established calls
	 */
	protected int countEstablishedCallsInPhone(String number) {
    	boolean commited = false;
    	try {
    		int res;
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		Operator operator = network.getOperatorFromPrefix(number.substring(0,2));
		    Phone phone = operator.getPhonebyNumber(number);
		   	res = phone.getEstablishedCallsCount();
	    	Transaction.commit();
	    	commited = true;
	    	return res;
    	} finally { if (!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Gets the last communication made by a given Phone
	 * We're assuming that FenixFramework lists store objects by order of
	 * addition. Tests that use this method so far don't involve more than
	 * 1 call, so this assumption won't be dangerous for now
	 * @param number	the Phone's number
	 * @return			the last communication made by that Phone
	 */
	protected Communication getLastMadeCommunication(String number) {
		boolean commited = false;
		try {
			Transaction.begin();
			Network network = FenixFramework.getRoot();
			Operator operator = network.getOperatorFromNumber(number);
			Phone phone = operator.getPhonebyNumber(number);
			List<Communication> communications = phone.getEstablishedCalls();
			Communication res = communications.get(communications.size() - 1);
			Transaction.commit();
			commited = true;
			return res;
		} finally { if (!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Gets the last communication received by a given Phone
	 * We're assuming that FenixFramework lists store objects by order of
	 * addition. Tests that use this method so far don't involve more than
	 * 1 call, so this assumption won't be dangerous for now
	 * @param number	the Phone's number
	 * @return			the last communication received by that Phone
	 */
	protected Communication getLastReceivedCommunication(String number) {
		boolean commited = false;
		try {
			Transaction.begin();
			Network network = FenixFramework.getRoot();
			Operator operator = network.getOperatorFromNumber(number);
			Phone phone = operator.getPhonebyNumber(number);
			List<Communication> communications = phone.getReceivedCalls();
			Communication res = communications.get(communications.size() - 1);
			Transaction.commit();
			commited = true;
			return res;
		} finally { if (!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Gets the current state of a given phone
	 * @param number	the number of the desired Phone
	 * @return			a String representation of its current state
	 */
	protected String getPhoneState(String number) {
		boolean commited = false;
		try {
			Transaction.begin();
			Network network = FenixFramework.getRoot();
			Operator op = network.getOperatorFromNumber(number);
			PhoneState state = op.getPhonebyNumber(number).getState();
			String res = null;
			if (state instanceof OnState) {
				res = PhoneStateRepresentation.getInstance().getOnState();
			} else if (state instanceof OffState) {
				res = PhoneStateRepresentation.getInstance().getOffState();
			} else if (state instanceof SilentState) {
				res = PhoneStateRepresentation.getInstance().getSilentState();
			} else if (state instanceof OccupiedState) {
				res = PhoneStateRepresentation.getInstance().getOccupiedState();
			} else {
				res = PhoneStateRepresentation.getInstance().getUnknownState();
			}
			Transaction.commit();
			commited = true;
			return res;
		} finally { if (!commited) { Transaction.abort(); } }
	}
	
	/**
	 * Change the current State of a specific Phone
	 * @param number	the Phone's number
	 * @param state		the String representation of the new State
	 */
	protected void changePhoneState(String number, String state) {
    	boolean commited = false;
    	try { 
    		Transaction.begin();
    		Network network = FenixFramework.getRoot();
    		PhoneStateRepresentation stateStandard
    			= PhoneStateRepresentation.getInstance();
    		if (stateStandard.getOnState().equals(state)) {
		    	network.turnOnPhone(number);
		    } else if(stateStandard.getOffState().equals(state)) {
	    		network.turnOffPhone(number);
	    	} else if(stateStandard.getSilentState().equals(state)) {
	    		network.silencePhone(number);
	    	} else {
	    		throw new InvalidState();
	    	}
	    	Transaction.commit();
	    	commited = true;
    	} finally { if (!commited) { Transaction.abort(); } }
	}
}