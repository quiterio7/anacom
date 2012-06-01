package anacom.presentationserver.client.handlers;

import anacom.presentationserver.client.AnacomServiceAsync;
import anacom.presentationserver.client.view.RevogateCertificatePanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RevogateCertificateHandler implements ClickHandler, KeyUpHandler {

	
	private final AnacomServiceAsync anacomService;
	private final RevogateCertificatePanel revogatePanel;
	
	public RevogateCertificateHandler(AnacomServiceAsync anacomService,
								final RevogateCertificatePanel revogatePanel) {
		this.anacomService = anacomService;
		this.revogatePanel = revogatePanel;
		this.revogatePanel.getRevogateButton().addClickHandler(this);
		this.revogatePanel.getPrefixTextBox().getWidget().addKeyUpHandler(this);
	}
	
	@Override
	public void onClick(ClickEvent event) {
		this.handleRevogation();
	}
	
	/**
	 * Handle Revogation
	 */
	private void handleRevogation() {
		this.revogateCertificateService();
	}
	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			this.revogateCertificateService();
		}
	}
	
	/**
	 * Send request to server
	 */
	private void revogateCertificateService() {
		this.revogatePanel.clearErrorMessage();
		String prefix
		= this.revogatePanel.getPrefixTextBox().getWidget().getText();
		/**
		 * This method is only used for testing revogate certificates - SD Mode
		 * It isn't part of Project's Logic Implementation.
		 * We use this to revogate a Operator certificate using GWT interface.
		 * In normal ways we use a console in Application Server side to revogate
		 * their own certificates.
		 * Not pretty!!!
		 */
		this.anacomService.revogateCertificate(prefix, new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				revogatePanel.
				setRevogatePanelLabel("Revogation success");
			}
			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::" +
						"rpcService.revogatePanel");
				revogatePanel.
				setRevogatePanelLabel("Error in communication");
			}
		});
	}
}
