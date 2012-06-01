package anacom.presentationserver.client.handlers;

import anacom.presentationserver.client.AnacomServiceAsync;
import anacom.presentationserver.client.view.CommunicationPanel;
import anacom.presentationserver.client.view.PhoneNumberInput;
import anacom.presentationserver.client.view.SendSMSPanel;
import anacom.presentationserver.client.view.StatusPanel;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;
import anacom.shared.misc.FieldVerifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class is not a KeyUpHandler so that the user can send messages with 'newline'
 * (real phones allows this).
 */
public class SendSMSHandler implements ClickHandler {
	
	private final AnacomServiceAsync anacomService;
	
	private final PhoneNumberInput phoneNumberInput;
	private final SendSMSPanel sendSMSPanel;
	private final StatusPanel statusPanel;
	private final CommunicationPanel communicationPanel;
	
	/**
	 * Constructor
	 * @param anacomService			the asynchronous service interface
	 * @param sendSMSPanel			the send SMS panel. This handler adds itself 
	 * 								to the sendSMSPanel ClickHandlers and KeyUpHandlers
	 * @param statusPanel			the status display panel. This panel will be updated
	 * 								by the handler if the user changes the current phone
	 */
	public SendSMSHandler(AnacomServiceAsync anacomService,
						  final PhoneNumberInput phoneNumberInput,
						  final CommunicationPanel communicationPanel,
						  final SendSMSPanel sendSMSPanel,
						  final StatusPanel statusPanel) {
		this.anacomService = anacomService;
		this.phoneNumberInput = phoneNumberInput;
		this.sendSMSPanel = sendSMSPanel;
		this.statusPanel = statusPanel;
		this.communicationPanel = communicationPanel;
		
		this.sendSMSPanel.getSendButton().addClickHandler(this);
		this.communicationPanel.getDestinationTextBox().getWidget().addKeyUpHandler(
				new KeyUpHandler() {
					public void onKeyUp(KeyUpEvent event) {
						if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
							sendSMSPanel.getMessageTextArea().getWidget().setFocus(true);
						}
					}
				});
	}
	
	/**
	 * Handles a click event by sending an SMS
	 * @param event		the event that fired the handler
	 */
	@Override
	public void onClick(ClickEvent event) {
		this.handleSendSMS();
	}

	/**
	 * Sends an SMS from the current Phone. If the current phone is not set or the
	 * fields "To" or "Message" are not valid, shows an error message.
	 */
	private void handleSendSMS() {
		String from = this.statusPanel.getCurrentPhoneLabel().getText();
		String to = this.communicationPanel.getDestinationTextBox().getWidget().getText();
		String message = this.sendSMSPanel.getMessageTextArea().getWidget().getText();
		
		FieldVerifier fieldVerifier = FieldVerifier.getInstance();
		// Check if given Phone number is valid
		if (fieldVerifier.validPhoneNumber(to)) {
			//Check if given message is valid
			if (fieldVerifier.validMessage(message)) {
				if (!from.equals("")) {
					this.communicationPanel.getDestinationTextBox().hideErrorMessage();
					this.sendSMSPanel.getMessageTextArea().hideErrorMessage();
					this.sendRequestsToServer(from, to, message);
				} else {
					this.phoneNumberInput.getNumberTextBox().displayErrorMessage("The current " +
							"phone is not set. Please set the current phone first.");
				}
			} else {
				this.sendSMSPanel.getMessageTextArea().displayErrorMessage("The message " +
						"must not be empty.");
			}
		} else {
			this.communicationPanel.getDestinationTextBox().displayErrorMessage("The given " +
					"number isn't a valid phone number. The characters must be " +
					"numbers and it must be exactly 9 digits long.");
		}
	}
	
	/**
	 * Sends request to server for sending an SMS
	 * @param from		the phone sending the message
	 * @param to		the receiver of the message
	 * @param message	the message to be sent
	 */
	private void sendRequestsToServer(String from, String to, String message) {
		SendSMSDTO sendSMSDTO = new SendSMSDTO(message,from, to);
		PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(from);
		this.sendSMS(sendSMSDTO);
		this.getPhoneBalance(phoneNumberDTO);
	}

	/**
	 * Sends an SMS from a phone to another
	 * @param sendSMSDTO 	the dto holding the information of the message (sender,
	 * 						receiver and message)
	 */
	private void sendSMS(SendSMSDTO sendSMSDTO) {
		this.anacomService.sendSMS(sendSMSDTO, new AsyncCallback<Void>() {
			public void onSuccess(Void response) {
				communicationPanel.getDestinationTextBox().getWidget().setText("");
				sendSMSPanel.getMessageTextArea().getWidget().setText("");
			}
			
			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.sendSMS");
				if (caught instanceof NotPositiveBalance) {
					NotPositiveBalance ex = (NotPositiveBalance) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' does not have enough " +
							"balance to send an SMS. It must be positive.");
				} else if (caught instanceof InvalidStateSendSMS) {
					InvalidStateSendSMS ex = (InvalidStateSendSMS) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' must be On or Silent " +
							"to send an SMS.");
				} else if (caught instanceof SMSMessageNotValid) {
					sendSMSPanel.getMessageTextArea().displayErrorMessage("The message " +
							"must not be empty.");
				} else if (caught instanceof InvalidAmount) {
					communicationPanel.getDestinationTextBox().displayErrorMessage("There was an " +
							"error calculating the cost of the SMS. Please try again.");
				} else {
					handleGetPhoneInfoException(caught);					
				}
			}
		});
	}
	
	/**
	 * Requests the balance of the phone with the given number from the server
	 * @param phoneNumberDTO	dto containing the phone's number
	 */
	private void getPhoneBalance(PhoneNumberDTO phoneNumberDTO) {
		this.anacomService.getPhoneBalance(phoneNumberDTO, new AsyncCallback<PhoneDTO>() {
			public void onSuccess(PhoneDTO response) {
				statusPanel.setBalanceLabel(response.getBalance() + "");
			}

			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.getPhoneBalance");
				handleGetPhoneInfoException(caught);
			}
		});
	}
	
	/**
	 * This function is used to handle exceptions that are common to all server
	 * requests.
	 * @param caught	the thrown exception
	 */
	private void handleGetPhoneInfoException(Throwable caught) {
		GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
		if (caught instanceof UnrecognisedPrefix) {
			UnrecognisedPrefix ex = (UnrecognisedPrefix) caught;
			this.communicationPanel.getDestinationTextBox().displayErrorMessage("There's no " +
					"operator in the network with prefix '" + ex.getPrefix() + "'.");
		} else if (caught instanceof PhoneNotExists) {
			PhoneNotExists ex = (PhoneNotExists) caught;
			this.communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
					"with number '" + ex.getNumber() + "' does not exist.");
		} else {
			this.sendSMSPanel.getMessageTextArea().displayErrorMessage("A strange " +
					"error occurred while sending the SMS, please try again.");
		}	
	}
}
