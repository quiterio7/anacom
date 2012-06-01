package anacom.presentationserver.server.replication;

import java.util.ArrayList;
import java.util.Collection;

import anacom.presentationserver.server.AnacomPortTypeFactory;
import anacom.presentationserver.server.ThreadContext;
import anacom.shared.UDDI.UDDIQuery;

import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.UDDI.QueryServiceException;
import anacom.shared.exceptions.UDDI.UDDICommunicationError;
import anacom.shared.misc.Constants;
import anacom.shared.stubs.client.Anacom;
import anacom.shared.stubs.client.AnacomApplicationServerPortType;
import anacom.shared.stubs.client.BalanceLimitExceededException;
import anacom.shared.stubs.client.CantChangeStateException;
import anacom.shared.stubs.client.CantMakeVideoCallException;
import anacom.shared.stubs.client.CantMakeVoiceCallException;
import anacom.shared.stubs.client.CantReceiveVideoCallException;
import anacom.shared.stubs.client.CantReceiveVoiceCallException;
import anacom.shared.stubs.client.DurationNotValidException;
import anacom.shared.stubs.client.FinishCallOnDestinationType;
import anacom.shared.stubs.client.FinishCallType;
import anacom.shared.stubs.client.IncompatiblePrefixException;
import anacom.shared.stubs.client.IncreasePhoneBalanceType;
import anacom.shared.stubs.client.InvalidAmountException;
import anacom.shared.stubs.client.InvalidCallTypeException;
import anacom.shared.stubs.client.InvalidStateException;
import anacom.shared.stubs.client.InvalidStateFinishMakingCallException;
import anacom.shared.stubs.client.InvalidStateFinishReceivingCallException;
import anacom.shared.stubs.client.InvalidStateMakeVideoException;
import anacom.shared.stubs.client.InvalidStateMakeVoiceException;
import anacom.shared.stubs.client.InvalidStateReceiveVideoException;
import anacom.shared.stubs.client.InvalidStateReceiveVoiceException;
import anacom.shared.stubs.client.InvalidStateSendSMSException;
import anacom.shared.stubs.client.LastMadeCommunicationType;
import anacom.shared.stubs.client.MakeCallType;
import anacom.shared.stubs.client.NoMadeCommunicationException;
import anacom.shared.stubs.client.NotPositiveBalanceException;
import anacom.shared.stubs.client.OperatorDetailedType;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsException;
import anacom.shared.stubs.client.OperatorPrefixNotValidException;
import anacom.shared.stubs.client.OperatorNameAlreadyExistsException;
import anacom.shared.stubs.client.OperatorNameNotValidException;
import anacom.shared.stubs.client.CommunicationErrorException;
import anacom.shared.stubs.client.OperatorPrefixType;
import anacom.shared.stubs.client.PhoneAlreadyExistsException;
import anacom.shared.stubs.client.PhoneListType;
import anacom.shared.stubs.client.PhoneNotExistsException;
import anacom.shared.stubs.client.PhoneNumberNotValidException;
import anacom.shared.stubs.client.PhoneNumberType;
import anacom.shared.stubs.client.PhoneStateType;
import anacom.shared.stubs.client.PhoneType;
import anacom.shared.stubs.client.ReceiveSMSType;
import anacom.shared.stubs.client.ReceivedSMSListType;
import anacom.shared.stubs.client.RegisterPhoneType;
import anacom.shared.stubs.client.SMSMessageNotValidException;
import anacom.shared.stubs.client.SendSMSType;
import anacom.shared.stubs.client.UnrecognisedPrefixException;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import anacom.shared.stubs.client.BonusValueNotValidException;
import anacom.shared.stubs.client.InvalidPhoneTypeException;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.registry.JAXRException;

/**
 * This class encapsulates the UDDI Query object and abstracts how the
 * communication with the application server is done. This class is also meant
 * to be used as a bridge to allow different communication architectures
 * (with and without replication for example).
 */
public abstract class WSCommunication {

	/**
	 * UDDIQuery's user name parameter.
	 */
	private static final String USERNAME = "username";
	
	/**
	 * UDDIQuery's password parameter.
	 */
	private static final String PASSWORD = "password";
	
	/**
	 * UDDI's .xml configuration file name.
	 */
	private static final String UDDI_PARAMETERS_XML = "uddi.xml";
	
	/**
	 * The organization's name in the UDDI registry.
	 */
	public static final String ORGANIZATION = "Anacom";
	
	/**
	 * The prefix of the last successfully searched Operator.
	 */
	private String currentPrefix = Constants.NULL_OPERATOR_PREFIX;
	
	/**
	 * The instance's UDDIQuery object used for UDDI searches.
	 */
	private UDDIQuery uddi;
	
	/**
	 * A collection of the last successfully searched Operator's servers' URIs.
	 * This is kept in this class for efficiency.
	 */
	private Collection<String> currentServers = new ArrayList<String>();
	
	/**
	 * This object will be the one that will create and configure the 
	 * AnacomPortType.
	 */
	private AnacomPortTypeFactory portTypeFactory;
	
	/**
	 * @throws UDDICommunicationError if there was an error communicating with
	 * the UDDI registry
	 */
	public WSCommunication(){
		try {
		    this.uddi = new UDDIQuery(
		    		USERNAME,
					PASSWORD,
					System.getProperty(UDDI_PARAMETERS_XML));
		    this.portTypeFactory = new AnacomPortTypeFactory();
		} catch (JAXRException jaxre) {
			throw new UDDICommunicationError(jaxre.getStackTrace().toString());
		}
	}
	
	/**
	 * @param uddi
	 * 		the new uddi query object.
	 */
	public void setUDDIQuery(UDDIQuery uddi) {
		this.uddi = uddi;
	}
	
	/**
	 * @param portTypeFactory
	 * 		the new port type factory.
	 */
	public void setAnacomPortTypeFactory(AnacomPortTypeFactory portTypeFactory) {
		this.portTypeFactory = portTypeFactory;
	}
	
	/**
	 * @return a collection of the last successfully searched Operator's
	 * servers' URIs
	 */
	public Collection<String> getCurrentServers() { return this.currentServers; }
	
	/**
	 * Queries the UDDI registry for the server URIs of the Operator with the
	 * given prefix.
	 * @param prefix the Operator's prefix
	 * @return a Collection of the Operator's servers' URIs
	 * @throws UDDICommunicationError if there was an error communicating with
	 * the UDDI registry
	 * @throws QueryServiceException if there's no registered Operator with the
	 * given prefix
	 */
	public Collection<String> query(String prefix)
			throws UDDICommunicationError, QueryServiceException {
		
		if (!prefix.equals(this.currentPrefix)) {
			try {
				this.currentServers = this.uddi.query(ORGANIZATION, prefix);
				this.currentPrefix = prefix;
			} catch (JAXRException jaxre) {
				throw new UDDICommunicationError(
						jaxre.getStackTrace().toString());
			}
		}
		
		return this.currentServers;
	}
	
	/**
	 * Clears the local UDDI registry data.
	 */
	public void clearCache() { this.uddi.clearCache(ORGANIZATION); }
	
	/**
	 * Clears the local UDDI registry data for the Operator with the given
	 * prefix. In case that an Operator's server may have been moved, the
	 * cache should be cleared before a query so that the URIs may be refreshed.
	 * @param prefix the desired Operator's prefix
	 */
	public void clearCache(String prefix) {
		this.uddi.clearCache(ORGANIZATION, prefix);
	}
	
	/**
	 * Clears the local UDDI registry data of the Operator with the given
	 * prefix and queries the remote registry for the actualized server URIs.
	 * @param prefix the prefix of the Operator to be refreshed
	 * @return a Collection of the Operator's servers' URIs
	 * @throws UDDICommunicationError if there was an error communicating with
	 * the UDDI registry
	 * @throws QueryServiceException if there's no registered Operator with the
	 * given prefix
	 */
	public Collection<String> refresh(String prefix)
			throws UDDICommunicationError, QueryServiceException {
		this.uddi.clearCache(ORGANIZATION, prefix);
		if (this.currentPrefix.equals(prefix)) {
			this.currentPrefix = Constants.NULL_OPERATOR_PREFIX; // the query might fail
			this.currentServers = new ArrayList<String>();
		}
		return this.query(prefix);
	}
	
    /**
     * Changes the end point address to the given url.
     * @param url the new end point url
     * @return the anacom application server port
     */
	protected AnacomApplicationServerPortType setServer(String url) {
		return this.portTypeFactory.setServer(url);
	}
	
	protected void setMessageContextToWrite(XMLGregorianCalendar timestamp,
											String operatorPrefix) {
		ThreadContext threadContext = ThreadContext.getInstance();
		threadContext.setCurrentRequestTimestamp(timestamp);
		threadContext.setCurrentOperatorPrefix(operatorPrefix);
		threadContext.setCurrentMessageToWrite();
	}
		
	protected void setMessageContextToRead() {
		ThreadContext.getInstance().setCurrentMessageToRead();
	}
	
	public abstract void registerOperator(	OperatorDetailedType dto,
											String operatorPrefix)
			throws 	OperatorNameAlreadyExistsException,
					OperatorPrefixAlreadyExistsException,
					OperatorPrefixNotValidException,
					OperatorNameNotValidException,
					CommunicationErrorException,
					WebServiceException,
					BonusValueNotValidException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
	
	public abstract void registerPhone(RegisterPhoneType dto, String operatorPrefix)
			throws	InvalidPhoneTypeException,
					UnrecognisedPrefixException, 
					PhoneAlreadyExistsException,
					PhoneNumberNotValidException,
					IncompatiblePrefixException,
					CommunicationErrorException,
					UDDICommunicationError,
					QueryServiceException,
					WebServiceException,
					CommunicationError;

	public abstract PhoneType getPhoneBalance(PhoneNumberType dto, String operatorPrefix)
			throws 	WebServiceException,
					PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					CommunicationErrorException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;

	public abstract void increasePhoneBalance(IncreasePhoneBalanceType dto, String operatorPrefix)
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					InvalidAmountException,
					BalanceLimitExceededException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
	
	public abstract PhoneListType listOperatorPhones(OperatorPrefixType dto, String operatorPrefix)
			throws 	UnrecognisedPrefixException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;

	public abstract void cancelPhoneRegistry(PhoneNumberType dto, String operatorPrefix)
			throws 	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
	
	public abstract ReceiveSMSType sendSMS(SendSMSType dto, String operatorPrefix) 
			throws	PhoneNumberNotValidException,
					PhoneNotExistsException,
					NotPositiveBalanceException,
					UnrecognisedPrefixException,
					InvalidStateSendSMSException,
					SMSMessageNotValidException,
					InvalidAmountException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
	
	public abstract void receiveSMS(ReceiveSMSType dto, String operatorPrefix) 
			throws	PhoneNumberNotValidException,
					PhoneNotExistsException,
					UnrecognisedPrefixException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
		
	public abstract void setPhoneState(PhoneStateType dto, String operatorPrefix)
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					CommunicationErrorException,
					InvalidStateException,
					CantChangeStateException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
			
	public abstract PhoneStateType getPhoneState(PhoneNumberType dto, String operatorPrefix) 
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
			
	public abstract ReceivedSMSListType getPhoneSMSReceivedMessages(PhoneNumberType number, String operatorPrefix)
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
	
	public abstract LastMadeCommunicationType getLastMadeCommunication(PhoneNumberType number, String operatorPrefix)
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					CommunicationErrorException,
					WebServiceException,
					NoMadeCommunicationException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
			
	public abstract void makeCall(MakeCallType call, String operatorPrefix) 
			throws	UnrecognisedPrefixException,
					PhoneNotExistsException,
					PhoneNumberNotValidException,
					NotPositiveBalanceException,
					InvalidStateMakeVoiceException,
					InvalidStateMakeVideoException,
					CantMakeVoiceCallException,
					CantMakeVideoCallException,
					InvalidCallTypeException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
	
	public abstract void receiveCall(MakeCallType call, String operatorPrefix) 
			throws	UnrecognisedPrefixException,
					PhoneNotExistsException,
					PhoneNumberNotValidException,
					InvalidStateReceiveVoiceException,
					InvalidStateReceiveVideoException,
					CantReceiveVoiceCallException,
					CantReceiveVideoCallException,
					InvalidCallTypeException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
			
	public abstract FinishCallOnDestinationType finishCall(FinishCallType finishDto, String operatorPrefix)
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					DurationNotValidException,
					InvalidStateFinishMakingCallException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
	
	public abstract void finishCallOnDestination(FinishCallOnDestinationType finishDto, String operatorPrefix)
			throws	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					DurationNotValidException,
					InvalidStateFinishReceivingCallException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError;
			
}