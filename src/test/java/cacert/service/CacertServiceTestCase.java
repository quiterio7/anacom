package cacert.service;

import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.pstm.Transaction;
import cacert.domain.CAManager;
import security.CryptoManager;
import cacert.services.SetKeysService;
import cacert.services.SetValidPeriodService;
import cacert.shared.dto.SetKeysDTO;
import cacert.shared.dto.ValidPeriodDTO;

public class CacertServiceTestCase extends TestCase {
	
	static {
    	if(FenixFramework.getConfig()==null) {
    		FenixFramework.initialize(new Config() {{
                dbAlias = "test-db"; 
                domainModelPath="src/cacert/dml/cacert.dml";
                repositoryType=RepositoryType.BERKELEYDB;
                rootClass = CAManager.class;
    		}});
    	}
    }
	
	protected final int VALID_PERIOD = 3;
	
	protected String publicKey;
	protected String privatekey;
	
	/**
	 * Constructor
	 * @param name	the name of the Test Case. For dynamic invocation.
	 */
	protected CacertServiceTestCase(String msg) {
    	super(msg);
    }
    
	/**
	 * Constructor
	 */
    protected CacertServiceTestCase() {
    	super();
    }
    
    /**
     * Sets up the test by cleaning all the information in the Network.
     * This method is always called before each test.
     */
    @Override
    protected void setUp() {
		cleanCacert();
		setKeys();
		setValidPeriod();
	}	
    
    /**
     * Cleans Network after each test in order to 
     * perform new tests with the same values
     */
    @Override
	protected void tearDown() {
		cleanCacert();
	}
    
    protected void setKeys(){
    	try{
    	
    		CryptoManager manager = CryptoManager.getInstance();
    		
    		String[] key = manager.generateKeys();
    		SetKeysDTO keys = new SetKeysDTO(key[0], key[1]);
    		
    		this.publicKey = key[0];
    		this.privatekey = key[1];
    		
    		new SetKeysService(keys).execute();
    		
    	}catch(NoSuchAlgorithmException nse){
    		System.err.println("Error this set Keys method");
    	}
    }
    
    protected void setValidPeriod() {
    	ValidPeriodDTO validPeriodDTO = new ValidPeriodDTO(VALID_PERIOD);
    	new SetValidPeriodService(validPeriodDTO).execute();
    }
	
	/**
	 *	Cleans the CA Manager
	 *	All Operators from the Network are removed.
	 */
    protected void cleanCacert() {
    	boolean committed = false;
    	try {
    		Transaction.begin();
    		CAManager manager = (CAManager) FenixFramework.getRoot();
    		manager.getBlockedCertificatesSet().clear();
    		manager.setKeys(null, null);
    		manager.setValidPeriod(0);
    		Transaction.commit();
    		committed = true;
    	} finally { if (!committed) { Transaction.abort(); } }
    }
    
    /**
     * Get the list with all blocked certificates
     */
    protected int getBlockedListCount() {
    	boolean committed = false;
    	int answer = 0;
    	try {
    		Transaction.begin();
    		CAManager manager = (CAManager) FenixFramework.getRoot();
    		answer = manager.getBlockedCertificatesCount();
    		Transaction.commit();
    		committed = true;
    		return answer;
    	} finally { if(!committed) {Transaction.abort(); } }
    }
    
    /**
     * Get the public key of CAManager
     */
    public String getCacertPublicKey() {
		boolean committed = false;
    	String key = null;
		try {
    		Transaction.begin();
    		CAManager manager = (CAManager) FenixFramework.getRoot();
    		
    		key = manager.getPublicKey();
    		
    		Transaction.commit();
    		committed = true;
    		return key;
    	} finally { if(!committed) {Transaction.abort(); } }
	}
    
    /**
     * Get the private key of CAManager
     */
	public String getCacertPrivateKey() {
		boolean committed = false;
    	String privKey = null;
		try {
    		Transaction.begin();
    		CAManager manager = (CAManager) FenixFramework.getRoot();
    		
    		privKey = manager.getPrivateKey();
    		
    		Transaction.commit();
    		committed = true;
    		return privKey;
    	} finally { if(!committed) {Transaction.abort(); } }
	}
	
	/*
	 * Get the valid period of CAManager
	 */
	public long getValidPeriod() {
		boolean committed = false;
    	long period = 0;
		try {
    		Transaction.begin();
    		CAManager manager = (CAManager) FenixFramework.getRoot();
    		
    		period = manager.getValidPeriod();
    		
    		Transaction.commit();
    		committed = true;
    		return period;
    	} finally { if(!committed) {Transaction.abort(); } }
	}
}
