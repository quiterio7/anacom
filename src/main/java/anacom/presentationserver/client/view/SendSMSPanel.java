package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SendSMSPanel extends VerticalPanel {

	private final Label panelLabel;
	private final HorizontalPanel messagePanel;
	private final Label messageLabel;
	private final WidgetWithErrorMessage<TextArea> messageTextArea;
	private final Button sendButton;

	/**
	 * Constructor
	 */
	public SendSMSPanel() {
		GWT.log("presentationserver.client.view.SendSMSPanel::constructor()");
		
		this.panelLabel = new Label("Send SMS");
		this.messagePanel = new HorizontalPanel();
		this.messageLabel = new Label("Message");
		this.messageTextArea = new WidgetWithErrorMessage<TextArea>(new TextArea());
		this.sendButton = new Button("Send");
		
		
		this.messagePanel.add(this.messageLabel);
		this.messagePanel.add(this.messageTextArea);
		
		this.add(this.panelLabel);
		this.add(this.messagePanel);
		this.add(this.sendButton);
	}

	
	/**
	 * @return	the message input text area widget
	 */
	public WidgetWithErrorMessage<TextArea> getMessageTextArea() {
		return this.messageTextArea;
	}
	
	/**
	 * @return	the send button widget
	 */
	public Button getSendButton() { return this.sendButton; }
	
}
