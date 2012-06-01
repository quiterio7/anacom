package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WidgetWithErrorMessage<WidgetType extends Widget> extends VerticalPanel {

	private final WidgetType widget;
	private final Label errorMessage;
	
	/**
	 * Constructor
	 * @param widget	the widget with the error message. DO NOT pass a null widget,
	 * 					if you do so you're almost certainly doing something wrong
	 */
	public WidgetWithErrorMessage(WidgetType widget) {
		GWT.log("presentationserver.client.view.WidgetWithErrorMessage::" +
				"constructor(" + widget.getClass() + ")");
		
		this.widget = widget;
		this.errorMessage = new Label();
		
		this.errorMessage.setVisible(false);
		
		this.add(this.widget);
		this.add(this.errorMessage);
	}
	
	/**
	 * @return	the widget
	 */
	public WidgetType getWidget() { return this.widget; }
	
	/**
	 * Displays an error message below the widget.
	 * @param message	the message to be displayed
	 */
	public void displayErrorMessage(String message) {
		this.errorMessage.setVisible(true);
		this.errorMessage.setText(message);
	}
	
	/**
	 * Hides the currently displayed error message if there was one.
	 */
	public void hideErrorMessage() { this.errorMessage.setVisible(false); }
	
}
