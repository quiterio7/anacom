package anacom.presentationserver.client.handlers;

import anacom.presentationserver.client.AnacomServiceAsync;
import anacom.presentationserver.client.view.LastCommunicationPanel;
import anacom.presentationserver.client.view.PhoneNumberInput;
import anacom.presentationserver.client.view.StatusPanel;
import anacom.shared.dto.LastMadeCommunicationDTO;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.PhoneReceivedSMSListDTO;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.dto.ReceivedSMSDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.NoMadeCommunication;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.misc.FieldVerifier;
import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class is very similar to the RefreshStatusHandler, there are some
 * differences in how the server response and the exceptions are handled which
 * would make the code too complicated to understand if this extended the
 * referred handler.
 * This way, it's easier to add different behavior to either of the handlers
 * in a more controllable way.
 */
public class CurrentPhoneHandler implements ClickHandler, KeyUpHandler {
	
	private final AnacomServiceAsync anacomService;
	
	private final PhoneNumberInput phoneNumberInput;
	private final StatusPanel statusPanel;
	private final LastCommunicationPanel lastCommunicationPanel;

	/**
	 * Constructor
	 * @param anacomService		the asynchronous service interface
	 * @param phoneNumberInput	the phone number input panel. This handler adds itself
	 * 							to the phoneNumberInput KeyUpHandlers and ClickHandlers
	 * @param statusPanel		the status display panel. This panel will be updated
	 * 							by the handler if the user changes the current phone
	 */
	public CurrentPhoneHandler(AnacomServiceAsync anacomService,
							   final PhoneNumberInput phoneNumberInput, 
							   final StatusPanel statusPanel) {
		this.anacomService = anacomService;
		this.phoneNumberInput = phoneNumberInput;
		this.statusPanel = statusPanel;
		this.lastCommunicationPanel = this.statusPanel.getLastCommunicationPanel();
		this.phoneNumberInput.getNumberTextBox().getWidget().addKeyUpHandler(this);
		this.phoneNumberInput.getSetButton().addClickHandler(this);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.handleCurrentPhoneChange();
	}
	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			this.handleCurrentPhoneChange();
		}
	}
	
	/**
	 * Changes the current phone to the one inputed by the user.
	 * In case of error or invalid input, an appropriate message is displayed.
	 */
	private void handleCurrentPhoneChange() {
		String phoneNumber
			= this.phoneNumberInput.getNumberTextBox().getWidget().getText();
		
		// Check if given Phone number is valid
		if (FieldVerifier.getInstance().validPhoneNumber(phoneNumber)) {
			this.phoneNumberInput.getNumberTextBox().hideErrorMessage();
			this.sendRequestsToServer(phoneNumber);
		} else {
			this.phoneNumberInput.getNumberTextBox().displayErrorMessage("The given " +
					"number isn't a valid phone number. The characters must be " +
					"numbers and it must be exactly 9 digits long.");
		}
	}
	
	/**
	 * Sends requests to server for the information on the Phone with the given number
	 * @param phoneNumber	the desired Phone's number
	 */
	private void sendRequestsToServer(String phoneNumber) {
		final PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO(phoneNumber);
		
		this.getPhoneBalance(phoneNumberDTO);
		this.getPhoneState(phoneNumberDTO);
		this.getPhoneReceivedSMS(phoneNumberDTO);
		this.getLastMadeCommunication(phoneNumberDTO);
	}
	
	/**
	 * Requests the balance of the phone with the given number from the server
	 * @param phoneNumberDTO	dto containing the phone's number
	 */
	protected void getPhoneBalance(final PhoneNumberDTO phoneNumberDTO) {
		this.anacomService.getPhoneBalance(phoneNumberDTO, new AsyncCallback<PhoneDTO>() {
			public void onSuccess(PhoneDTO response) {
				statusPanel.setCurrentPhoneLabel(response.getNumber());
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
	 * Requests the Last communication of a specific Phone Number
	 * @param phoneNumberDTO	DTO containing the phone's number
	 */
	private void getLastMadeCommunication(final PhoneNumberDTO phoneNumberDTO) {
		this.anacomService.getLastMadeCommunication(phoneNumberDTO, new AsyncCallback<LastMadeCommunicationDTO>() {
			public void onSuccess(LastMadeCommunicationDTO response) {
				lastCommunicationPanel.hideNullMessageCommunicationLabel();
				lastCommunicationPanel.setDestinationLabel(response.getDestination());
				lastCommunicationPanel.setCommunicationTypeLabel(response.getCommunicationType());
				lastCommunicationPanel.setCostLabel("" + response.getCost());
				lastCommunicationPanel.setTotalLabel("" + response.getSize());
				
				if(CommunicationRepresentation.
						getInstance().getSMSCommunication().
						equals(response.getCommunicationType()))
					lastCommunicationPanel.setTotalFieldLabel("SMS lenght:");
				else if(CommunicationRepresentation.
							getInstance().getVoiceCommunication().
							equals(response.getCommunicationType()) || 
						CommunicationRepresentation.
							getInstance().getVideoCommunication().
							equals(response.getCommunicationType()))
					lastCommunicationPanel.setTotalFieldLabel("Call duration:");
			}

			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.getLastMadeCommunication");
				handleGetPhoneInfoException(caught);
			}
		});
	}
	
	/**
	 * Requests the state of the phone with the given number from the server
	 * @param phoneNumberDTO	dto containing the phone's number
	 */
	private void getPhoneState(PhoneNumberDTO phoneNumberDTO) {
		this.anacomService.getPhoneState(phoneNumberDTO, new AsyncCallback<PhoneStateDTO>() {
			public void onSuccess(PhoneStateDTO response) {
				statusPanel.setCurrentPhoneLabel(response.getNumber());
				statusPanel.setPhoneStateLabel(response.getState());
			}

			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.getPhoneState");
				handleGetPhoneInfoException(caught);
			}
		});
	}
	
	/**
	 * Requests the received sms of the phone with the given number from the
	 * server
	 * @param phoneNumberDTO	dto containing the phone's number
	 */
	private void getPhoneReceivedSMS(PhoneNumberDTO phoneNumberDTO) {
		this.anacomService.getPhoneSMSReceivedMessages(phoneNumberDTO,
						new AsyncCallback<PhoneReceivedSMSListDTO>() {
			public void onSuccess(PhoneReceivedSMSListDTO response) {
				statusPanel.setCurrentPhoneLabel(response.getNumber());
				statusPanel.getReceivedSMSTable().clearTable();
				for (ReceivedSMSDTO sms : response.getSMSList()) {
					statusPanel.getReceivedSMSTable().addSMS(sms.getSource(),
															 sms.getMessage());
				}
			}
			
			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.getPhoneReceivedSMS");
				handleGetPhoneInfoException(caught);
			}
		});
	}
	
	/**
	 * This function is used to handle exceptions on the service invocation
	 * callbacks. The exceptions and how they are handled is pretty much the
	 * same for all services invoked on this handler.
	 * @param caught	the thrown exception
	 */
	
	private void handleGetPhoneInfoException(Throwable caught) {
		GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
		if (caught instanceof UnrecognisedPrefix) {
			UnrecognisedPrefix ex = (UnrecognisedPrefix) caught;
			this.phoneNumberInput.getNumberTextBox().displayErrorMessage("There's no " +
					"operator in the network with prefix '" + ex.getPrefix() + "'.");
		} else if (caught instanceof PhoneNotExists) {
			PhoneNotExists ex = (PhoneNotExists) caught;
			this.phoneNumberInput.getNumberTextBox().displayErrorMessage("The phone " +
					"with number '" + ex.getNumber() + "' does not exist.");
		} else if (caught instanceof NoMadeCommunication) {
			NoMadeCommunication ex = (NoMadeCommunication) caught;
			this.lastCommunicationPanel.showNullMessageCommunicationLabel("This number " + 
			ex.getNumber() + " did not make any kind of communications");
			
		} else {
			this.phoneNumberInput.getNumberTextBox().displayErrorMessage("A strange " +
					"error occurred while retrieving the information of the given " +
					"phone, please try again.");
		}	
	}
	
}
