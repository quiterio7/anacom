package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class StatusPanel extends DecoratorPanel {
	
	private final VerticalPanel verticalPanel;
	
	private final HorizontalPanel currentPhonePanel;
	private final HorizontalPanel phoneStatePanel;
	private final HorizontalPanel balancePanel;

	private final Label currentPhoneFieldLabel;
	private final Label currentPhoneLabel;
	
	private final Label phoneStateFieldLabel;
	private final Label phoneStateLabel;
	
	private final Label balanceFieldLabel;
	private final Label balanceLabel;
	
	private final Label receivedSMSLabel;
	private final SMSTable receivedSMSTable;
	
	private final Button refreshButton;
	
	private final LastCommunicationPanel lastCommunicationPanel;
	
	/**
	 * Constructor
	 */
	public StatusPanel() {
		GWT.log("presentationserver.client.view.StatusPanel::constructor()");
	
		this.verticalPanel = new VerticalPanel();
		this.currentPhonePanel = new HorizontalPanel();
		this.phoneStatePanel = new HorizontalPanel();
		this.balancePanel = new HorizontalPanel();
		this.lastCommunicationPanel = new LastCommunicationPanel();
		
		this.currentPhoneFieldLabel = new Label("Current Phone:");
		this.currentPhoneLabel = new Label("");
		this.currentPhonePanel.add(this.currentPhoneFieldLabel);
		this.currentPhonePanel.add(this.currentPhoneLabel);
		
		this.phoneStateFieldLabel = new Label("State:");
		this.phoneStateLabel = new Label("");
		this.phoneStatePanel.add(this.phoneStateFieldLabel);
		this.phoneStatePanel.add(this.phoneStateLabel);
		
		this.balanceFieldLabel = new Label("Balance:");
		this.balanceLabel = new Label("");
		this.balancePanel.add(this.balanceFieldLabel);
		this.balancePanel.add(this.balanceLabel);
		
		this.receivedSMSLabel = new Label("---- Received SMS Table -----");
		this.receivedSMSTable = new SMSTable();
		
		this.refreshButton = new Button("Refresh");
		
		this.verticalPanel.add(this.currentPhonePanel);
		this.verticalPanel.add(this.phoneStatePanel);
		this.verticalPanel.add(this.balancePanel);
		this.verticalPanel.add(this.lastCommunicationPanel);
		this.verticalPanel.add(this.receivedSMSLabel);
		this.verticalPanel.add(this.receivedSMSTable);
		this.verticalPanel.add(this.refreshButton);
		
		this.add(this.verticalPanel);		
	}
	
	public Label getCurrentPhoneLabel() {
		return this.currentPhoneLabel;
	}

	public void setCurrentPhoneLabel(String text) {
		this.currentPhoneLabel.setText(text);
	}

	public Label getPhoneStateLabel() {
		return this.phoneStateLabel;
	}

	public void setPhoneStateLabel(String text) {
		this.phoneStateLabel.setText(text);
	}
	
	public Label getBalanceLabel() {
		return this.balanceLabel;
	}
	
	public LastCommunicationPanel getLastCommunicationPanel() {
		return this.lastCommunicationPanel;
	}

	public void setBalanceLabel(String text) {
		this.balanceLabel.setText(text);
	}

	public SMSTable getReceivedSMSTable() {
		return this.receivedSMSTable;
	}
	
	public Button getRefreshButton() { 
		return this.refreshButton;
	}
		
}
