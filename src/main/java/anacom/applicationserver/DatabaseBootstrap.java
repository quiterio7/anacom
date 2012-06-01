package anacom.applicationserver;

import anacom.domain.Network;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;

/**
 * a self-sustaining Class that proceeds without external help.
 *
 */
public class DatabaseBootstrap {
    private static boolean notInitialized = true;

    /**
     * static function Init initialize a test database
     */
    public static void init() {
	if (notInitialized) {
	    FenixFramework.initialize(new Config() {
		    {
			dbAlias = "test-db";
			domainModelPath = "src/main/dml/anacom.dml";
			repositoryType = RepositoryType.BERKELEYDB;
			rootClass = Network.class;
		    }
		});
	}
	notInitialized = false;
    }

    /**
     * static function setup installs all the configurations needed to use the service
     * Eg:. Populate domain
     */
    public static void setup() {
    	try {
			anacom.SetupDomain.populateDomain();
		} catch (anacom.shared.exceptions.AnacomException ex) {
			System.out.println("Error while populating anacom application: "
					+ ex);
		}
    }
}