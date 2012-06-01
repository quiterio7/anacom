package anacom.presentationserver.client;

import anacom.shared.dto.CallDTO;
import anacom.shared.dto.FinishCallDTO;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.dto.LastMadeCommunicationDTO;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.PhoneReceivedSMSListDTO;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.dto.SendSMSDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface AnacomServiceAsync {
	
	/**
	 * Initialises service with the desired bridge
	 * @param serverType	string indicating the type of bridge (this can be
	 * 						"ES-only" or "ES-SD")
	 */
	public void initBridge(String serverType, AsyncCallback<Void> callback);
	
	/**
	 * Sends a SMS
	 * @param dto		sms text plus source and destination information
     * @param callback	callback object responsible for handling the response of
     * 					the server, it should handle the following exceptions:
     * 					UnrecognisedPrefix, PhoneNotExists, NotPositiveBalance,
     * 					InvalidStateSendSMS, SMSMessageNotValid, InvalidAmount,
     * 					CommunicationError
	 */
	public void sendSMS(SendSMSDTO dto, AsyncCallback<Void> callback);
	
	
	/**
	 * Increase the Phone Balance of a Phone with given number
	 * @param phone 	the number of the Phone and the amount you want
	 * 					increase.
	 * @param callback	callback object responsible for handling the response of the
     * 					server, it should handle the following exceptions:
     * 					UnrecognisedPrefix, PhoneNotExists, BalanceLimitExceeded,
     * 					InvalidAmount, CommunicationError
	 */
	public void increasePhoneBalance(IncreasePhoneBalanceDTO dto,
			AsyncCallback<Void> callback);
	
	/**
	 * Gets the Phone Balance of a Phone with given number
	 * @param number 	the number of the Phone
	 * @param callback	callback object responsible for handling the response of the
	 * 					server, it should handle the following exceptions:
	 * 					UnrecognisedPrefix, PhoneNotExists, CommunicationError
	 */
	public void getPhoneBalance(PhoneNumberDTO number,
			AsyncCallback<PhoneDTO> callback);
	
	/**
	 * Change the state of the Phone with a given phone number to a given state
	 * @param newState	dto holding the desired phone number and the new state
	 * @param callback	callback object responsible for handling the response of the
	 * 					server, it should handle the following exceptions:
	 * 					UnrecognisedPrefix, PhoneNotExists, InvalidState,
	 * 					CommunicationError
	 */
	public void setPhoneState(PhoneStateDTO newState, 
			AsyncCallback<Void> callback);
	
	/**
	 * Get the state of the Phone with a given phone number
	 * @param number	dto holding the desired phone number 
	 * @param callback	callback object responsible for handling the response of the
	 * 					server, it should handle the following exceptions:
	 * 					UnrecognisedPrefix, PhoneNotExists, CommunicationError
	 */
	public void getPhoneState(PhoneNumberDTO number, 
			AsyncCallback<PhoneStateDTO> callback);
	
	/** 
	 * Getter of a collection of SMS Communications from a specific Number.
	 * @param number	dto holding the desired phone number
	 * @param callback	callback object responsible for handling the response of the
	 * 					server, it should handle the following exceptions:
	 * 					UnrecognisedPrefix, PhoneNotExists, CommunicationError
	 */
	public void getPhoneSMSReceivedMessages(PhoneNumberDTO number, 
			AsyncCallback<PhoneReceivedSMSListDTO> callback);
	
	/**
	 * Get the Last communication of a specific Phone Number
	 * @param number	the number which we want to know it last communication
	 * @return the last communication performed by a specific Number
	 */
	public void getLastMadeCommunication(PhoneNumberDTO number,
			AsyncCallback<LastMadeCommunicationDTO> callback);
			
	/**
	 * Makes a call
	 * @param call		dto holding the source, destination, 
	 * 					start time and type of the call
	 * @param callback	callback object responsible for handling the response of the
	 * 					server, it should handle the following exceptions:
	 * 					UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid, 
	 *					NotPositiveBalance, InvalidStateMakeVoice, InvalidStateMakeVideo,
	 *					CantMakeVoiceCall, CantMakeVideoCall, InvalidCallType, CommunicationError
	 */
	public void makeCall(CallDTO call,
			AsyncCallback<Void> callback);
	
	/**
	 * Finishes a call
	 * @param finishDto		dto holding the source and end time info
	 * @param callback		callback object responsible for handling the response of the
	 * 						server, it should handle the following exceptions:
	 * 						UnrecognisedPrefix, PhoneNotExists, DurationNotValid,
	 *						InvalidStateFinishMakingCall, InvalidStateFinishReceivingCall,
	 *						CommunicationError 
	 */
	public void finishCall(FinishCallDTO finishDto,
			AsyncCallback<Void> callback);

	
	/**
	 *
	 * This method is only used for testing revogate certificates - SD Mode
	 * It isn't part of Project's Logic Implementation.
	 * We use this to revogate a Operator certificate using GWT interface.
	 * In normal ways we use a console in Application Server side to revogate
	 * their own certificates.
	 * Not pretty!!!
	 *
	 * Revogate a Certificate of a specific Entity
	 * @param callback
	 */
	public void revogateCertificate(String prefix, AsyncCallback<Void> callback);
	
}
