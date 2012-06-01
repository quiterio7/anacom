package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class PhoneNumberInput extends HorizontalPanel {

	private final Label label;
	private final WidgetWithErrorMessage<TextBox> numberTextBox;
	private final Button setButton;
	
	/**
	 * Constructor
	 */
	public PhoneNumberInput() {
		GWT.log("presentationserver.client.view.PhoneNumberInput::constructor()");
		
		this.label = new Label("Current Phone:");
		this.numberTextBox = new WidgetWithErrorMessage<TextBox>(new TextBox());
		this.setButton = new Button("Set");
		
		this.numberTextBox.getWidget().setMaxLength(9);
		
		this.add(this.label);
		this.add(this.numberTextBox);
		this.add(this.setButton);
	}
	
	/**
	 * @return	the number text input box widget
	 */
	public WidgetWithErrorMessage<TextBox> getNumberTextBox() {
		return this.numberTextBox;
	}
	
	/**
	 * @return	the set button widget
	 */
	public Button getSetButton() { return this.setButton; }
	
}
