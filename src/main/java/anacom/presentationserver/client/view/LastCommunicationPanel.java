package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LastCommunicationPanel extends VerticalPanel {
	
	private final HorizontalPanel destinationPanel;
	private final HorizontalPanel communicationTypePanel;
	private final HorizontalPanel costPanel;
	private final HorizontalPanel totalPanel;

	private final Label destinationFieldLabel;
	private final Label destinationLabel;
	
	private final Label communicationTypeFieldLabel;
	private final Label communicationTypeLabel;
	
	private final Label costFieldLabel;
	private final Label costLabel;
	
	private final Label totalFieldLabel;
	private final Label totalLabel;
	private final Label nullCommunicationLabel;
	
	/**
	 * Constructor
	 */
	public LastCommunicationPanel() {
		GWT.log("presentationserver.client.view.LastCommunicationPanel::constructor()");
		
		this.destinationPanel = new HorizontalPanel();
		this.communicationTypePanel = new HorizontalPanel();
		this.costPanel = new HorizontalPanel();
		this.totalPanel = new HorizontalPanel();
		
		this.destinationFieldLabel = new Label("Destination:");
		this.destinationLabel = new Label("");
		this.destinationPanel.add(this.destinationFieldLabel);
		this.destinationPanel.add(this.destinationLabel);
		
		this.communicationTypeFieldLabel = new Label("Communication Type:");
		this.communicationTypeLabel = new Label("");
		this.communicationTypePanel.add(this.communicationTypeFieldLabel);
		this.communicationTypePanel.add(this.communicationTypeLabel);
		
		this.nullCommunicationLabel = new Label();
		this.nullCommunicationLabel.setVisible(false);
		this.costFieldLabel = new Label("Cost:");
		this.costLabel = new Label("");
		this.costPanel.add(this.costFieldLabel);
		this.costPanel.add(this.costLabel);
		this.totalFieldLabel = new Label("Communication length/duration:");
		this.totalLabel = new Label("");
		this.totalPanel.add(this.totalFieldLabel);
		this.totalPanel.add(this.totalLabel);
		
		this.add(new Label("---- Last Communication Status ----"));
		this.add(this.nullCommunicationLabel);
		this.add(this.destinationPanel);
		this.add(this.communicationTypePanel);
		this.add(this.costPanel);
		this.add(this.totalPanel);
	}
	
	public Label getDestinationLabel() {
		return this.destinationLabel;
	}

	public void setDestinationLabel(String text) {
		this.destinationLabel.setText(text);
	}

	public Label getCommunicationTypeLabel() {
		return this.communicationTypeLabel;
	}

	public void setCommunicationTypeLabel(String text) {
		this.communicationTypeLabel.setText(text);
	}

	public Label getCostLabel() {
		return this.costLabel;
	}

	public void setCostLabel(String text) {
		this.costLabel.setText(text);
	}

	public Label getTotalLabel() {
		return this.totalLabel;
	}
	
	public void setTotalLabel(String text) {
		this.totalLabel.setText(text);
	}
	
	public void setTotalFieldLabel(String text) {
		this.totalFieldLabel.setText(text);
	}
	
	public void hideNullMessageCommunicationLabel() {
		this.setVisibility(true);
	}
	
	public void showNullMessageCommunicationLabel (String text) {
		this.nullCommunicationLabel.setText(text);
		this.setVisibility(false);
	}
	
	private void setVisibility(boolean visible) {
		this.nullCommunicationLabel.setVisible(!visible);
		this.destinationPanel.setVisible(visible);
		this.communicationTypePanel.setVisible(visible);
		this.costPanel.setVisible(visible);
		this.totalPanel.setVisible(visible);
	}
		
}