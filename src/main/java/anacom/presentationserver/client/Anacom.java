package anacom.presentationserver.client;


import anacom.presentationserver.client.handlers.BeginCommunicationHandler;
import anacom.presentationserver.client.handlers.ChangeStateHandler;
import anacom.presentationserver.client.handlers.ChargePhoneHandler;
import anacom.presentationserver.client.handlers.CurrentPhoneHandler;
import anacom.presentationserver.client.handlers.RefreshStatusHandler;
import anacom.presentationserver.client.handlers.RevogateCertificateHandler;
import anacom.presentationserver.client.handlers.SelectCommunicationTypeHandler;
import anacom.presentationserver.client.handlers.SendSMSHandler;
import anacom.presentationserver.client.handlers.TerminateCommunicationHandler;
import anacom.presentationserver.client.view.InteractionPanel;
import anacom.presentationserver.client.view.RevogateCertificatePanel;
import anacom.presentationserver.client.view.StatusPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Anacom implements EntryPoint {
	
	private static final String remoteServerType = "ES+SD";
	private static final String localServerType = "ES-only";

	/**
	 * Create a remote service proxy to talk to the presentation server service.
	 */
	private final AnacomServiceAsync anacomService = GWT.create(AnacomService.class);
	
	private final Label serverTypeLabel = new Label("Anacom");
	private final InteractionPanel interactionPanel = new InteractionPanel();
	private final StatusPanel statusPanel = new StatusPanel();
	private final RevogateCertificatePanel revogatePanel = 
												new RevogateCertificatePanel();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.log("presentationserver.client.Anacom::onModuleLoad() - begin");
		
		// create label with mode type
		this.serverTypeLabel.setStyleName("h1");
		String serverType; // depends on type of running
		if (RootPanel.get(remoteServerType) != null) {
			GWT.log("presentationserver.client.Anacom::onModuleLoad() running on remote mode");
			this.serverTypeLabel.setText("Anacom - Remote mode");
			serverType = remoteServerType;
		} else { // default: local - even if it is misspelled
			GWT.log("presentationserver.client.Anacom::onModuleLoad() running on local mode");
			this.serverTypeLabel.setText("Anacom - Local mode");
			serverType = localServerType;
		}
		
		RootPanel typeRootPanel = RootPanel.get(serverType);
		typeRootPanel.add(this.serverTypeLabel);
		this.serverTypeLabel.setWidth("100%");
		
		RootPanel interactionRootPanel = RootPanel.get("interactionContainer");
		interactionRootPanel.add(this.interactionPanel);
		this.interactionPanel.setWidth("100%");
		
		this.interactionPanel.getCurrentPhonePanel().getNumberTextBox().getWidget().setFocus(true);
		
		RootPanel statusRootPanel = RootPanel.get("statusContainer");
		statusRootPanel.add(this.statusPanel);
		this.statusPanel.setWidth("100%");
		
		RootPanel revogateRootPanel = RootPanel.get("statusContainer");
		revogateRootPanel.add(this.revogatePanel);
		this.statusPanel.setWidth("100%");
		
		// Launch event handlers
		new CurrentPhoneHandler(this.anacomService,
				this.interactionPanel.getCurrentPhonePanel(), this.statusPanel);
		new ChangeStateHandler(this.anacomService,
				this.interactionPanel.getCurrentPhonePanel(),
				this.interactionPanel.getChangePhoneStatePanel(), this.statusPanel);
		new ChargePhoneHandler(this.anacomService,
				this.interactionPanel.getCurrentPhonePanel(),
				this.interactionPanel.getChargePhonePanel(), this.statusPanel);
		new SendSMSHandler(this.anacomService,
				this.interactionPanel.getCurrentPhonePanel(),
				this.interactionPanel.getCommunicationPanel(),
				this.interactionPanel.getCommunicationPanel().getSendSMSPanelPanel(), 
				this.statusPanel);
		new RefreshStatusHandler(this.anacomService, this.statusPanel);	
		new SelectCommunicationTypeHandler(this.interactionPanel.getCommunicationPanel());
		new BeginCommunicationHandler(this.anacomService, 
				this.interactionPanel.getCurrentPhonePanel(),
				this.interactionPanel.getCommunicationPanel().getVideoCallPanel(), 
				this.interactionPanel.getCommunicationPanel(), 
				this.statusPanel);
		new TerminateCommunicationHandler(this.anacomService, 
				this.interactionPanel.getCommunicationPanel().getVideoCallPanel(), 
				this.interactionPanel.getCommunicationPanel(), 
				this.statusPanel);
		new BeginCommunicationHandler(this.anacomService, 
				this.interactionPanel.getCurrentPhonePanel(),
				this.interactionPanel.getCommunicationPanel().getVoiceCallPanel(), 
				this.interactionPanel.getCommunicationPanel(), 
				this.statusPanel);
		new TerminateCommunicationHandler(this.anacomService, 
				this.interactionPanel.getCommunicationPanel().getVoiceCallPanel(), 
				this.interactionPanel.getCommunicationPanel(), 
				this.statusPanel);
		
		new RevogateCertificateHandler(	this.anacomService, 
				this.revogatePanel);
		
		this.anacomService.initBridge(serverType, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("presentationserver.client.Anacom::onModuleLoad()::anacomService.initBridge");
				GWT.log("-- Throwable: '" + caught.getClass().getName() + "'");
				Window.alert("Not able to init application server bridge: " + caught.getMessage());

			}
		});
		
		GWT.log("presentationserver.client.Anacom::onModuleLoad() - done!");
	}
	
}
