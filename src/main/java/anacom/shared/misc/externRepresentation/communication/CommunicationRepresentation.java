package anacom.shared.misc.externRepresentation.communication;

/**
 * This class is used to get a String representation of the
 * different types of Communications
 */
public class CommunicationRepresentation {

	private static CommunicationRepresentation instance;
	
	private CommunicationRepresentation() {}
	
	public static CommunicationRepresentation getInstance() {
		if(instance == null) {
			instance = new CommunicationRepresentation();
		}
		return instance;
	}
	
	public String getSMSCommunication() { 
		return "SMS"; 
	}
	
	public String getVoiceCommunication() { 
		return "Voice"; 
	}
	
	public String getVideoCommunication() { 
		return "Video"; 
	}
	
	public String getUnknownCommunication() { 
		return "Unknown"; 
	}
}
