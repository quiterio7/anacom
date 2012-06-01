package anacom.presentationserver.client.handlers;

import anacom.presentationserver.client.AnacomServiceAsync;
import anacom.presentationserver.client.view.CallCommunicationPanel;
import anacom.presentationserver.client.view.CommunicationPanel;
import anacom.presentationserver.client.view.StatusPanel;
import anacom.shared.dto.FinishCallDTO;
import anacom.shared.exceptions.communication.DurationNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.invalidState.InvalidStateFinishMakingCall;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class TerminateCommunicationHandler implements ClickHandler {
	
	private final AnacomServiceAsync anacomService;
	private final CallCommunicationPanel callCommunicationPanel;
	private final CommunicationPanel communicationPanel;
	private final StatusPanel statusPanel;
	

	public TerminateCommunicationHandler(AnacomServiceAsync anacomService,
						  					final CallCommunicationPanel callCommunicationPan,
						  					final CommunicationPanel communicationPanel,
						  					final StatusPanel statusPanel) {
		this.anacomService = anacomService;
		this.callCommunicationPanel = callCommunicationPan;
		this.communicationPanel = communicationPanel;
		this.statusPanel = statusPanel;
		this.callCommunicationPanel.getTerminateCommunicationButton().getWidget().addClickHandler(this);
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
		this.sendRequestsToServer(from, to);
	}
	
	private void sendRequestsToServer(String from, String to) {
		@SuppressWarnings("unused")
		String communicationType = this.callCommunicationPanel.getCommunicationType();
		FinishCallDTO dto = new FinishCallDTO(from);
		this.anacomService.finishCall(dto, new AsyncCallback<Void>() {
			public void onSuccess(Void response) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.finishCall succeded");
				communicationPanel.getDestinationTextBox().getWidget().setText("");
				communicationPanel.getCommunicationTypeListBox().setEnabled(true);
				callCommunicationPanel.getBeginCommunicationButton().getWidget().setEnabled(true);
				callCommunicationPanel.getTerminateCommunicationButton().getWidget().setEnabled(false);			
			}
			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.finishCall failed");
				if (caught instanceof UnrecognisedPrefix) {
					UnrecognisedPrefix ex = (UnrecognisedPrefix) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("There's no " +
							"operator in the network with prefix '" + ex.getPrefix() + "'.");
				} else if (caught instanceof PhoneNotExists) {
					PhoneNotExists ex = (PhoneNotExists) caught;
					communicationPanel.getDestinationTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' does not exist.");
				} else if (caught instanceof DurationNotValid) {
					@SuppressWarnings("unused")
					DurationNotValid ex = (DurationNotValid) caught;
				} else if (caught instanceof InvalidStateFinishMakingCall) {
					@SuppressWarnings("unused")
					InvalidStateFinishMakingCall ex = (InvalidStateFinishMakingCall) caught;
				} 
			}
		});
	}
	
}
