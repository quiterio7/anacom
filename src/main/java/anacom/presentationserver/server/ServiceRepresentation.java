package anacom.presentationserver.server;

public class ServiceRepresentation {

	private static ServiceRepresentation instance;
	
	public final String SENDSMS_SERVICE = "sendSMS";
	public final String INCREASEPHONEBALANCE_SERVICE = "increasePhoneBalance";
	public final String GETPHONEBALANCE_SERVICE = "getPhoneBalance";
	public final String SETPHONESTATE_SERVICE = "setPhoneState";
	public final String GETPHONESTATE_SERVICE = "getPhoneState";
	public final String GETPHONESMSRECEIVEDMESSAGES_SERVICE = "getPhoneSMSReceivedMessages";
	public final String GETLASTMADECOMMUNICATION_SERVICE = "getLastMadeCommunication";
	public final String MAKECALL_SERVICE = "makeCall";
	public final String FINISHCALL_SERVICE = "finishCall";
	
	private ServiceRepresentation() {
	}
	
	public static ServiceRepresentation getInstance() {
		if(instance == null) {
			instance = new ServiceRepresentation();
		}
		return instance;
	}
}
