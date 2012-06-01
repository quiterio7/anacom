package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class ChargePhoneInput extends HorizontalPanel {

	private final Label label;
	private final WidgetWithErrorMessage<TextBox> amountTextBox;
	private final Button chargeButton;
	
	/**
	 * Constructor
	 */
	public ChargePhoneInput() {
		GWT.log("presentationserver.client.view.ChargePhoneInput::constructor()");
		
		this.label = new Label("Charge Phone:");
		this.amountTextBox = new WidgetWithErrorMessage<TextBox>(new TextBox());
		this.chargeButton = new Button("Ok");
		
		this.add(this.label);
		this.add(this.amountTextBox);
		this.add(this.chargeButton);
	}
	
	/**
	 * @return	the amount input text box widget
	 */
	public WidgetWithErrorMessage<TextBox> getAmountTextBox() {
		return this.amountTextBox;
	}
	
	/**
	 * @return	the charge button widget
	 */
	public Button getChargeButton() { return this.chargeButton; }
	
}
