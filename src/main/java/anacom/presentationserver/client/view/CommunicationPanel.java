package anacom.presentationserver.client.view;

import anacom.shared.misc.externRepresentation.communication.CommunicationRepresentation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommunicationPanel extends VerticalPanel {
	
	private final Label communicationPanelLabel;
	
	private final HorizontalPanel communicationTypePanel;
	private final Label communicationTypeLabel;
	private final ListBox communicationType;
	private final Button selectButton;

	private final HorizontalPanel destinationPanel;
	private final Label destinationLabel;
	private final WidgetWithErrorMessage<TextBox> destinationTextBox;
	
	private final SendSMSPanel sendSMSPanel;
	private final CallCommunicationPanel voiceCallPanel;
	private final CallCommunicationPanel videoCallPanel;
	
	/**
	 * Constructor 
	 */
	public CommunicationPanel() {
		GWT.log("presentationserver.client.view." +
				"CommunicationPanel::constructor()");
		
		// Type Panel Initialization
		this.communicationTypePanel = new HorizontalPanel();
		this.communicationTypeLabel = new Label("Choose Type:");
		this.communicationType = new ListBox();
		this.selectButton = new Button("Select");
		this.communicationType.addItem("SMS");
		this.communicationType.addItem("Voice Call");
		this.communicationType.addItem("Video Call");
		this.communicationTypePanel.add(this.communicationTypeLabel);
		this.communicationTypePanel.add(this.communicationType);
		this.communicationTypePanel.add(this.selectButton);
		this.communicationType.setItemSelected(1, true);

		//Destination panel initialization
		this.destinationPanel = new HorizontalPanel();
		this.destinationLabel = new Label("To");
		this.destinationTextBox = new WidgetWithErrorMessage<TextBox>(new TextBox());
		this.destinationTextBox.getWidget().setMaxLength(9);
		this.destinationPanel.add(this.destinationLabel);
		this.destinationPanel.add(this.destinationTextBox);
		
		//Communication Panel initialization
		this.communicationPanelLabel = new Label("---- Communications ----");
		this.sendSMSPanel = new SendSMSPanel();
		this.voiceCallPanel = 
				new CallCommunicationPanel(CommunicationRepresentation.
						getInstance().
						getVoiceCommunication());
		this.videoCallPanel = 
				new CallCommunicationPanel(CommunicationRepresentation.
						getInstance().
						getVideoCommunication());		
		
		this.add(this.communicationPanelLabel);
		this.add(this.communicationTypePanel);
		this.add(this.destinationPanel);
		this.add(this.voiceCallPanel);
		this.voiceCallPanel.setVisible(false);
		this.add(this.videoCallPanel);
		this.videoCallPanel.setVisible(false);
		this.add(this.sendSMSPanel);
		this.sendSMSPanel.setVisible(false);

	}

	public HorizontalPanel getCommunicationTypePanel() { 
		return this.communicationTypePanel; 
	}
	
	public Button getSelectButton() { 
		return this.selectButton; 
	}

	public ListBox getCommunicationTypeListBox() { 
		return this.communicationType; 
	}

	public HorizontalPanel getDestinationPanel() { 
		return this.destinationPanel; 
	}

	public WidgetWithErrorMessage<TextBox> getDestinationTextBox() { 
		return this.destinationTextBox; 
	}

	public SendSMSPanel getSendSMSPanelPanel() { 
		return this.sendSMSPanel; 
	}
	

	public CallCommunicationPanel getVoiceCallPanel() { return this.voiceCallPanel; }
	

	public CallCommunicationPanel getVideoCallPanel() { return this.videoCallPanel; }
	
}
