package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RevogateCertificatePanel extends DecoratorPanel {
	
	private final VerticalPanel verticalPanel;
	private final Button revogateButton;
	private final Label CertificateFieldLabel;
	private final WidgetWithErrorMessage<TextBox> prefixTextBox;
	
	
	public RevogateCertificatePanel() {
		GWT.log("presentationserver.client.view.RevogateCertificate::constructor()");
		
		//creating widgets
		this.verticalPanel = new VerticalPanel();
		this.CertificateFieldLabel = new Label("Operator Server:");
		this.prefixTextBox = new WidgetWithErrorMessage<TextBox>(new TextBox());
		this.revogateButton = new Button("Revogate Certificate");
		
		//adding to vertical Panel
		this.verticalPanel.add(this.CertificateFieldLabel);
		this.verticalPanel.add(this.prefixTextBox);
		this.verticalPanel.add(this.revogateButton);
		
		this.add(this.verticalPanel);
	}
	
	/**
	 * Getter of the button Revogate Certificate
	 * @return
	 */
	public Button getRevogateButton() {
		return this.revogateButton;
	}
	
	/**
	 * Set the current message to show in UI
	 * @param text
	 */
	public void setRevogatePanelLabel(String text) {
		this.prefixTextBox.displayErrorMessage(text);
	}
	
	public void clearErrorMessage() {
		this.prefixTextBox.displayErrorMessage("");
	}
	
	public WidgetWithErrorMessage<TextBox> getPrefixTextBox() {
		return this.prefixTextBox;
	}
}
