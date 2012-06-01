package anacom.shared.misc.externRepresentation.phoneState;

/**
 * For the record, if we have more than 4 Modes it works perfectly
 * When we have to create a new Phone, the standard used is enumeration type
 * This class will be used in both sides (Client and Server)
 * There is a better solution to justify the presence of this class?
 */
public class PhoneStateRepresentation {

	private static PhoneStateRepresentation instance;
	
	private PhoneStateRepresentation() {}
	
	public static PhoneStateRepresentation getInstance() {
		if(instance == null) {
			instance = new PhoneStateRepresentation();
		}
		return instance;
	}
	
	public String getOnState() { 
		return "On"; 
	}
	
	public String getOffState() { 
		return "Off"; 
	}
	
	public String getSilentState() { 
		return "Silent"; 
	}
	
	public String getOccupiedState() { 
		return "Occupied"; 
	}

	public String getMakingCallState() { 
		return "MakingCall"; 
	}
	
	public String getMakingVoiceCallState() { 
		return "MakingVoiceCall"; 
	}

	public String getMakingVideoCallState() { 
		return "MakingVideoCall"; 
	}
	
	public String getReceivingVoiceCallState() { 
		return "ReceivingVoiceCall"; 
	}
	
	public String getReceivingVideoCallState() { 
		return "ReceivingVideoCall"; 
	}
	
	public String getUnknownState() { 
		return "Unknown"; 
	}

}
