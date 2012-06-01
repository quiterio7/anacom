package anacom.presentationserver.client.view;

import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ChangePhoneStatePanel extends VerticalPanel {

	private final Label label;
	private final HorizontalPanel statePanel;
	private final WidgetWithErrorMessage<ListBox> stateList;
	private final Button setStateButton;
	
	/**
	 * Constructor
	 */
	public ChangePhoneStatePanel() {
		GWT.log("presentationserver.client.view.ChangePhoneStatePanel::constructor()");
		
		this.label = new Label("Change Phone State");
		this.statePanel = new HorizontalPanel();
		this.stateList = new WidgetWithErrorMessage<ListBox>(new ListBox());
		this.setStateButton = new Button("Ok");
		
		PhoneStateRepresentation phoneStateRepresentation = PhoneStateRepresentation.getInstance();
		this.stateList.getWidget().addItem(phoneStateRepresentation.getOnState());
		this.stateList.getWidget().addItem(phoneStateRepresentation.getSilentState());
		this.stateList.getWidget().addItem(phoneStateRepresentation.getOffState());
		
		this.stateList.getWidget().setItemSelected(2, true);
		
		this.statePanel.add(stateList);
		this.statePanel.add(setStateButton);
		
		this.add(this.label);
		this.add(this.statePanel);
	}
	
	/**
	 * @return	the state list box widget
	 */
	public WidgetWithErrorMessage<ListBox> getStateList() { return this.stateList; }
	
	/**
	 * @return	the set state button widget
	 */
	public Button getSetStateButton() { return this.setStateButton; }
	
}
