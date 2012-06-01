package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InteractionPanel extends DecoratorPanel {
	
	private final VerticalPanel verticalPanel;
	
	private final PhoneNumberInput currentPhonePanel;
	private final ChargePhoneInput chargePhonePanel;
	private final ChangePhoneStatePanel changePhoneStatePanel;
	private final CommunicationPanel communicationPanel;
	
	/**
	 * Constructor
	 */
	public InteractionPanel() {
		GWT.log("presentationserver.client.view.InteractionPanel::constructor()");
		
		this.currentPhonePanel = new PhoneNumberInput();
		this.chargePhonePanel = new ChargePhoneInput();
		this.changePhoneStatePanel = new ChangePhoneStatePanel();
		this.communicationPanel = new CommunicationPanel();
		
		this.verticalPanel = new VerticalPanel();
		
		this.verticalPanel.add(this.currentPhonePanel);
		this.verticalPanel.add(this.chargePhonePanel);
		this.verticalPanel.add(this.changePhoneStatePanel);
		this.verticalPanel.add(this.communicationPanel);
		
		this.add(this.verticalPanel);
	}

	/**
	 * @return	the phone number input panel
	 */
	public PhoneNumberInput getCurrentPhonePanel() { return this.currentPhonePanel; }
	
	/**
	 * @return	the charge phone input panel
	 */
	public ChargePhoneInput getChargePhonePanel() { return this.chargePhonePanel; }
	
	
	/**
	 * @return	the change phone state panel
	 */
	public ChangePhoneStatePanel getChangePhoneStatePanel() { return this.changePhoneStatePanel; }
	
	/**
	 * @return	the Communication Panel
	 */
	public CommunicationPanel getCommunicationPanel() {
		return this.communicationPanel;
	}
	
	
}
