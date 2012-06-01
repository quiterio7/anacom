package anacom.shared.misc.externRepresentation.phoneType;

/**
 * This class is used to get a String representation of the different Phone
 * Types
 */
public class PhoneTypeRepresentation {

	private static PhoneTypeRepresentation instance;
	
	private PhoneTypeRepresentation() {}
	
	public static PhoneTypeRepresentation getInstance() {
		if(instance == null) {
			instance = new PhoneTypeRepresentation();
		}
		return instance;
	}
	
	public String get2GType() { 
		return "2G"; 
	}
	
	public String get3GType() { 
		return "3G"; 
	}
	
}
