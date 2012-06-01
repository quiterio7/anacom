package anacom;

import anacom.domain.Network;
import pt.ist.fenixframework.Config;
import pt.ist.fenixframework.FenixFramework;
import jvstm.Atomic;

public class SetupDomain {

	public static void main(String[] args) {
		FenixFramework.initialize(new Config() {
			{
				dbAlias = "db";
				domainModelPath = "src/main/dml/anacom.dml";
				repositoryType = RepositoryType.BERKELEYDB;
				rootClass = Network.class;

			}
		});
		populateDomain();
	}

	/**
	 * ES POPULATE DOMAIN
	 */
	@Atomic
	public static void populateDomain() {
		Network network = FenixFramework.getRoot();
		
		// Add Operators
		network.addOperator("Red", "91", 10, 5, 9, 120, 10);
		network.addOperator("Orange", "93", 10, 2, 4, 20, 0);
		
		String phone911 = new String("911111111");
		String phone912 = new String("911111112");
		String phone931 = new String("931111111");
		String operator91 = new String("91");
		String operator93 = new String("93"); 
		
		//Add Phones
		network.registerPhone2G(operator91, phone911);
		network.turnOnPhone(phone911);
		network.registerPhone2G(operator91, phone912);
		network.turnOnPhone(phone912);
		network.registerPhone2G(operator93, phone931);
		network.turnOnPhone(phone931);
		
	}

}
