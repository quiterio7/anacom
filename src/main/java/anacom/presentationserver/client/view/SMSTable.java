package anacom.presentationserver.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlexTable;

public class SMSTable extends FlexTable {

	/**
	 * Constructor
	 * Creates an empty SMS table.
	 */
	public SMSTable() {
		GWT.log("presentationserver.client.view.SMSTable::constructor()");
		this.setHTML(0, 0, "Number");
		this.setHTML(0, 1, "Message");
	}
	
	/**
	 * Adds the sms entry with given number and message to the table.
	 * The message is escaped to HTML before addition.
	 * @param number	the number of the other party phone
	 * @param message	the message
	 */
	public void addSMS(String number, String message) {
		int row = this.getRowCount();
		this.setHTML(row, 0, number);
		SafeHtml html = (new SafeHtmlBuilder()).appendEscapedLines(message).toSafeHtml();
		this.setHTML(row, 1, html.asString());
	}
	
	/**
	 * Clears the SMS table. All entries are removed.
	 */
	public void clearTable() {
		for (int row = this.getRowCount() - 1; row > 0; row--) {
			this.removeRow(row);
		}
	}
	
}
