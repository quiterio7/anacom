package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CallCommunicationPanel extends VerticalPanel {

	private final HorizontalPanel callPanel;
	private final Label label;
	private final WidgetWithErrorMessage<Button> beginCommunicationButton;
	private final WidgetWithErrorMessage<Button> terminateCommunicationButton;

	/**
	 * Constructor
	 */
	public CallCommunicationPanel(String typecommunication) {
		GWT.log("presentationserver.client.view.SendSMSPanel::constructor()");
				
		this.callPanel = new HorizontalPanel();
		this.label = new Label(typecommunication);
		this.beginCommunicationButton = new WidgetWithErrorMessage<Button>(new Button("Begin"));
		this.terminateCommunicationButton = new WidgetWithErrorMessage<Button>(new Button("Terminate"));
		
		this.callPanel.add(this.label);
		this.callPanel.add(this.beginCommunicationButton);
		this.callPanel.add(this.terminateCommunicationButton);
		this.add(this.callPanel);
		this.terminateCommunicationButton.getWidget().setEnabled(false);
	}
	
	/**
	 * @return	the begin communication widget
	 */
	public WidgetWithErrorMessage<Button> getBeginCommunicationButton() {
		return this.beginCommunicationButton;
	}
	
	/**
	 * @return	the terminate communication widget
	 */
	public WidgetWithErrorMessage<Button> getTerminateCommunicationButton() {
		return this.terminateCommunicationButton;
	}
	
	/**
	 * @return communication type (string) see anacom.shared.misc.CommunicationRepresentation
	 */
	public String getCommunicationType() {
		return this.label.getText();
	}
	
}
