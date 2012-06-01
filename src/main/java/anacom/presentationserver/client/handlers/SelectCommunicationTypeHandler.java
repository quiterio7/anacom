package anacom.presentationserver.client.handlers;

import anacom.presentationserver.client.view.CommunicationPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class SelectCommunicationTypeHandler implements ClickHandler {
	
	private final CommunicationPanel communicationPanel;
	
	/**
	 * Constructor
	 * @param anacomService		the asynchronous service interface
	 * @param statusPanel		the status display panel. This panel be updated
	 * 							by the handler if the refresh button is pressed
	 */
	public SelectCommunicationTypeHandler(final CommunicationPanel statusPanel) {
		this.communicationPanel = statusPanel;
		
		this.communicationPanel.getSelectButton().addClickHandler(this);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		String type = this.communicationPanel.getCommunicationTypeListBox().
			getValue(communicationPanel.getCommunicationTypeListBox().
					getSelectedIndex());
		
		if(type.equals("SMS")){
			this.communicationPanel.getSendSMSPanelPanel().setVisible(true);
			this.communicationPanel.getVoiceCallPanel().setVisible(false);
			this.communicationPanel.getVideoCallPanel().setVisible(false);
		} else if(type.equals("Voice Call")){
			this.communicationPanel.getSendSMSPanelPanel().setVisible(false);
			this.communicationPanel.getVoiceCallPanel().setVisible(true);
			this.communicationPanel.getVideoCallPanel().setVisible(false);
		} else {
			this.communicationPanel.getSendSMSPanelPanel().setVisible(false);
			this.communicationPanel.getVoiceCallPanel().setVisible(false);
			this.communicationPanel.getVideoCallPanel().setVisible(true);
		}
		
		
	}
	
}
