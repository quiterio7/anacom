package anacom.presentationserver.client.handlers;

import anacom.presentationserver.client.AnacomServiceAsync;
import anacom.presentationserver.client.view.ChargePhoneInput;
import anacom.presentationserver.client.view.PhoneNumberInput;
import anacom.presentationserver.client.view.StatusPanel;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.BalanceLimitExceeded;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.misc.Constants;
import anacom.shared.misc.FieldVerifier;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChargePhoneHandler implements ClickHandler, KeyUpHandler {
	
	private final AnacomServiceAsync anacomService;
	
	private final PhoneNumberInput phoneNumberInput;
	private final ChargePhoneInput chargePhoneInput;
	private final StatusPanel statusPanel;
	
	/**
	 * Constructor
	 * @param anacomService			the asynchronous service interface
	 * @param chargePhonePanel		the charge phone input panel. This handler adds itself 
	 * 								to the chargePhonePanel ClickHandlers and KeyUpHandlers
	 * @param statusPanel			the status display panel. This panel will be updated
	 * 								by the handler if the user charges the current phone
	 */
	public ChargePhoneHandler(AnacomServiceAsync anacomService,
							  final PhoneNumberInput phoneNumberInput,
							  final ChargePhoneInput chargePhoneInput, 
							  final StatusPanel statusPanel) {
		this.anacomService = anacomService;
		this.phoneNumberInput = phoneNumberInput;
		this.chargePhoneInput = chargePhoneInput;
		this.statusPanel = statusPanel;
		
		this.chargePhoneInput.getChargeButton().addClickHandler(this);
		this.chargePhoneInput.getAmountTextBox().getWidget().addKeyUpHandler(this);
	}
	
	/**
	 * Handles a click event by increasing the balance of the current Phone
	 * @param event		the event that fired the handler
	 */
	@Override
	public void onClick(ClickEvent event) {
		this.handleChargePhone();
	}
	
	/**
	 * Handles a key up event by increasing the balance of the current Phone
	 * if the key is ENTER.
	 * @param event		the event that fired the handler
	 */
	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			this.handleChargePhone();
		}
	}

	/**
	 * Charges the current Phone. If the current phone is not set or the amount
	 * is not valid, displays an error message.
	 */
	private void handleChargePhone() {
		String phoneNumber = this.statusPanel.getCurrentPhoneLabel().getText();
		String amountString
			= this.chargePhoneInput.getAmountTextBox().getWidget().getText();
		int amount;
		
		try {
			amount = Integer.parseInt(amountString);
			if (!FieldVerifier.getInstance().validChargeAmount(amount)) {
				throw new NumberFormatException(); // to display the message in the catch
			} else if (phoneNumber.equals("")) {
				this.phoneNumberInput.getNumberTextBox().displayErrorMessage("The current " +
						"phone is not set. Please set the current phone first.");
			} else {
				this.chargePhoneInput.getAmountTextBox().hideErrorMessage();
				this.sendRequestsToServer(phoneNumber, amount);
			}
		} catch(NumberFormatException nfe) {
			this.chargePhoneInput.getAmountTextBox().displayErrorMessage("The given amount " +
					"isn't valid, it must be a positive integer.");
		}
	}
	
	/**
	 * Sends request to the server for increasing the current Phone's balance
	 * @param phoneNumber	the number of the current Phone
	 * @param amount		the amount of balance to increase
	 */
	private void sendRequestsToServer(String phoneNumber, int amount) {
		final IncreasePhoneBalanceDTO increaseBalanceDTO = new IncreasePhoneBalanceDTO(phoneNumber, amount);
		this.increasePhoneBalance(increaseBalanceDTO);
	}
	
	/**
	 * Requests the server to increase the balance of the current Phone
	 * @param phoneNumberDTO	dto containing the phone's number and the amount to
	 * 							charge
	 */
	private void increasePhoneBalance(final IncreasePhoneBalanceDTO increaseBalanceDTO) {
		this.anacomService.increasePhoneBalance(increaseBalanceDTO, new AsyncCallback<Void>() {
			CurrentPhoneHandler currentPhone = new CurrentPhoneHandler(
															anacomService,
															phoneNumberInput,
															statusPanel);
			String number = increaseBalanceDTO.getNumber();
			PhoneNumberDTO phoneDTO = new PhoneNumberDTO(number);
			public void onSuccess(Void response) {
				try {
					currentPhone.getPhoneBalance(phoneDTO);
					chargePhoneInput.getAmountTextBox().getWidget().setText("");
				} catch(NumberFormatException ex) {
					// This shouldn't happen
					Window.alert("A strange error occurred while updating the " +
							"phone's balance, please refresh the information panel.");
				}
			}
			
			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.getPhoneBalance");
				GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
				if (caught instanceof UnrecognisedPrefix) {
					UnrecognisedPrefix ex = (UnrecognisedPrefix) caught;
					chargePhoneInput.getAmountTextBox().displayErrorMessage("There's no " +
							"operator in the network with prefix '" + ex.getPrefix() + 
							"'. It may have been removed.");
				} else if (caught instanceof PhoneNotExists) {
					PhoneNotExists ex = (PhoneNotExists) caught;
					chargePhoneInput.getAmountTextBox().displayErrorMessage("The phone " +
							"with number '" + ex.getNumber() + "' does not exist. It " +
							"may have been removed.");
				} else if (caught instanceof BalanceLimitExceeded) {
					// This could probably be verified on the client side also
					BalanceLimitExceeded ex = (BalanceLimitExceeded) caught;
					chargePhoneInput.getAmountTextBox().displayErrorMessage("The balance " +
							"limit of the phone '" + ex.getNumber() + "' was exceeded. " +
							"It can't be higher than " + Constants.MAX_BALANCE_AMOUNT +
							".");
				} else if (caught instanceof InvalidAmount) {
					chargePhoneInput.getAmountTextBox().displayErrorMessage("The given " +
							"amount isn't valid, it must be a positive integer.");
				} else {
					chargePhoneInput.getAmountTextBox().displayErrorMessage("A strange " +
							"error occurred while charging the balance of the current " +
							"phone, please try again.");
				}
			}
		});
	}
	
}
