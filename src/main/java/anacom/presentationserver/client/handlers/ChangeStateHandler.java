package anacom.presentationserver.client.handlers;

import anacom.presentationserver.client.AnacomServiceAsync;
import anacom.presentationserver.client.view.ChangePhoneStatePanel;
import anacom.presentationserver.client.view.PhoneNumberInput;
import anacom.presentationserver.client.view.StatusPanel;
import anacom.shared.dto.PhoneStateDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.CantChangeState;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.invalidState.InvalidState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChangeStateHandler implements ClickHandler {
	
	private final AnacomServiceAsync anacomService;
	
	private final PhoneNumberInput phoneNumberInput;
	private final ChangePhoneStatePanel changePhoneStatePanel;
	private final StatusPanel statusPanel;

	/**
	 * Constructor
	 * @param anacomService			the asynchronous service interface
	 * @param changePhoneStatePanel	the change phone state input panel. This handler adds 
	 * 								itself to the changePhoneStatePanel ClickHandlers
	 * @param statusPanel			the status display panel. This panel will be updated
	 * 								by the handler if the user changes the current phone's
	 * 								state
	 */
	public ChangeStateHandler(AnacomServiceAsync anacomService,
							  final PhoneNumberInput phoneNumberInput,
							  final ChangePhoneStatePanel changePhoneStatePanel, 
							  final StatusPanel statusPanel) {
		this.anacomService = anacomService;
		this.phoneNumberInput = phoneNumberInput;
		this.changePhoneStatePanel = changePhoneStatePanel;
		this.statusPanel = statusPanel;
		
		this.changePhoneStatePanel.getSetStateButton().addClickHandler(this);
	}

	/**
	 * Handles a click event by changing the state of the current Phone
	 * @param event		the event that fired the handler
	 */
	@Override
	public void onClick(ClickEvent event) {
		this.handleStateChange();
	}
	
	/**
	 * Changes the current Phone's state. If the current phone is not set, displays
	 * an error message.
	 */
	private void handleStateChange() {
		int index = this.changePhoneStatePanel.getStateList().getWidget().getSelectedIndex();
		String state = this.changePhoneStatePanel.getStateList().getWidget().getItemText(index);
		String phoneNumber = this.statusPanel.getCurrentPhoneLabel().getText();
		
		if(!phoneNumber.equals("")) {
			this.changePhoneStatePanel.getStateList().hideErrorMessage();
			this.sendRequestsToServer(phoneNumber, state);
		} else {	
			this.phoneNumberInput.getNumberTextBox().displayErrorMessage("The current phone" +
					" is not set. Please set the current phone first.");
		}
	}
	
	/**
	 * Sends request to the server for changing the current Phone's state
	 * @param phoneNumber	the number of the current Phone
	 * @param state			the new state of the current Phone
	 */
	private void sendRequestsToServer(String phoneNumber, String state) {
		final PhoneStateDTO phoneStateDTO = new PhoneStateDTO(phoneNumber,state);
		this.changePhoneState(phoneStateDTO);
	}
	
	/**
	 * Requests the server to change the state of the current Phone
	 * @param phoneNumberDTO	dto containing the phone's number and the new state
	 */
	private void changePhoneState(final PhoneStateDTO phoneStateDTO) {
		this.anacomService.setPhoneState(phoneStateDTO, new AsyncCallback<Void>() {
			public void onSuccess(Void response) {
				statusPanel.setPhoneStateLabel(phoneStateDTO.getState());
			}
			
			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.getPhoneBalance");
				GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
				if (caught instanceof UnrecognisedPrefix) {
					UnrecognisedPrefix ex = (UnrecognisedPrefix) caught;
					changePhoneStatePanel.getStateList().displayErrorMessage("There's " +
							"no operator in the network with prefix '" + ex.getPrefix() + 
							"'. It may have been removed.");
				} else if (caught instanceof PhoneNotExists) {
					PhoneNotExists ex = (PhoneNotExists) caught;
					changePhoneStatePanel.getStateList().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' does not exist. It " +
							"may have been removed.");
				} if (caught instanceof InvalidState) {
					// This could probably be verified on the client side also
					InvalidState ex = (InvalidState) caught;
					changePhoneStatePanel.getStateList().displayErrorMessage("The " +
							"state '" + ex.getState() + "' is not valid.");
				}if (caught instanceof CantChangeState) {
					// This could probably be verified on the client side also
					CantChangeState ex = (CantChangeState) caught;
					changePhoneStatePanel.getStateList().displayErrorMessage("The " +
							"state '" + ex.getInvalidState() + "' is not valid in current State.");
				} else {
					changePhoneStatePanel.getStateList().displayErrorMessage("A strange " +
							"error occurred while setting the state of the current " +
							"phone, please try again.");
				}
			}
		});
	}
	
}
