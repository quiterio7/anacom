package anacom.presentationserver.client.handlers;

import anacom.presentationserver.client.AnacomServiceAsync;
import anacom.presentationserver.client.view.CallCommunicationPanel;
import anacom.presentationserver.client.view.CommunicationPanel;
import anacom.presentationserver.client.view.PhoneNumberInput;
import anacom.presentationserver.client.view.StatusPanel;
import anacom.shared.dto.CallDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.CantMakeVideoCall;
import anacom.shared.exceptions.phone.CantMakeVoiceCall;
import anacom.shared.exceptions.phone.CantReceiveVideoCall;
import anacom.shared.exceptions.phone.CantReceiveVoiceCall;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateMakeVoice;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVideo;
import anacom.shared.exceptions.phone.invalidState.InvalidStateReceiveVoice;
import anacom.shared.misc.FieldVerifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class BeginCommunicationHandler implements ClickHandler {
	
	private final AnacomServiceAsync anacomService;
	
	private final PhoneNumberInput phoneNumberInput;
	private final CallCommunicationPanel callCommunicationPanel;
	private final CommunicationPanel communicationPanel;
	private final StatusPanel statusPanel;
	

	public BeginCommunicationHandler(AnacomServiceAsync anacomService,
									 	final PhoneNumberInput phoneNumberInput,
						  				final CallCommunicationPanel callCommunicationPan,
						  				final CommunicationPanel communicationPanel,
						  				final StatusPanel statusPanel) {
		this.anacomService = anacomService;
		this.phoneNumberInput = phoneNumberInput;
		this.callCommunicationPanel = callCommunicationPan;
		this.communicationPanel = communicationPanel;
		this.statusPanel = statusPanel;
		this.callCommunicationPanel.getBeginCommunicationButton().getWidget().addClickHandler(this);
	}
	
	// FIXME - javadoc
	@Override
	public void onClick(ClickEvent event) {
		this.handleBeginCall();
	}

	// FIXME - javadoc
	private void handleBeginCall() {
		String from = this.statusPanel.getCurrentPhoneLabel().getText();
		String to = this.communicationPanel.getDestinationTextBox().getWidget().getText();
		
		FieldVerifier fieldVerifier = FieldVerifier.getInstance();
		// Check if given Phone number is valid
		if (fieldVerifier.validPhoneNumber(to)) {
			
			if (!from.equals("")) {
				this.communicationPanel.getDestinationTextBox().hideErrorMessage();
				this.sendRequestsToServer(from, to);
			} else {
				this.phoneNumberInput.getNumberTextBox().displayErrorMessage("The current " +
						"phone is not set. Please set the current phone first.");
			}
			
		} else {
			this.communicationPanel.getDestinationTextBox().displayErrorMessage("The given " +
					"number isn't a valid phone number. The characters must be " +
					"numbers and it must be exactly 9 digits long.");
		}
	}
	
	// FIXME - javadoc
	private void sendRequestsToServer(String from, String to) {
		String communicationType = this.callCommunicationPanel.getCommunicationType();
		CallDTO dto = new CallDTO(from, to, communicationType);
		this.anacomService.makeCall(dto, new AsyncCallback<Void>() {
			public void onSuccess(Void response) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.makeCall succeded");
				communicationPanel.getDestinationTextBox().getWidget().setText("");
				communicationPanel.getCommunicationTypeListBox().setEnabled(false);
				callCommunicationPanel.getBeginCommunicationButton().getWidget().setEnabled(false);
				callCommunicationPanel.getTerminateCommunicationButton().getWidget().setEnabled(true);
			}
			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.makeCall failed");
				if (caught instanceof UnrecognisedPrefix) {
					UnrecognisedPrefix ex = (UnrecognisedPrefix) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("There's no " +
							"operator in the network with prefix '" + ex.getPrefix() + "'.");
				} else if (caught instanceof PhoneNotExists) {
					PhoneNotExists ex = (PhoneNotExists) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' does not exist.");
				} else if (caught instanceof PhoneNumberNotValid) {
					PhoneNumberNotValid ex = (PhoneNumberNotValid) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' is not valid.");
				} else if (caught instanceof NotPositiveBalance) {
					NotPositiveBalance ex = (NotPositiveBalance) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' does not have a positive balance (" +
							ex.getBalance() + ").");
				} else if (caught instanceof InvalidStateMakeVoice) {
					InvalidStateMakeVoice ex = (InvalidStateMakeVoice) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' is not in a valid state to " +
							"make a voice call (" + ex.getState()+ ").");
				} else if (caught instanceof InvalidStateMakeVideo) {
					InvalidStateMakeVideo ex = (InvalidStateMakeVideo) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' is not in a valid state to " +
							"make a video call (" + ex.getState()+ ").");
				} else if (caught instanceof CantMakeVoiceCall) {
					CantMakeVoiceCall ex = (CantMakeVoiceCall) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' is can't make the call.");
				} else if (caught instanceof CantMakeVideoCall) {
					CantMakeVideoCall ex = (CantMakeVideoCall) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' can't make the call (2G).");
				} else if (caught instanceof CantReceiveVideoCall) {
					CantReceiveVideoCall ex = (CantReceiveVideoCall) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' can't receive the call (2G).");
				} else if (caught instanceof CantReceiveVoiceCall) {
					CantReceiveVoiceCall ex = (CantReceiveVoiceCall) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' can't receive the call.");
				} else if (caught instanceof InvalidStateReceiveVoice) {
					InvalidStateMakeVoice ex = (InvalidStateMakeVoice) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' is not in a valid state to " +
							"receive a voice call (" + ex.getState()+ ").");
				} else if (caught instanceof InvalidStateReceiveVideo) {
					InvalidStateMakeVideo ex = (InvalidStateMakeVideo) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' is not in a valid state to " +
							"receive a video call (" + ex.getState()+ ").");
				} else {
					communicationPanel.getDestinationTextBox().displayErrorMessage("A strange " +
							"error occurred while establishing the communication, please try again.");
				}	
			}
		});
	}
	
}
