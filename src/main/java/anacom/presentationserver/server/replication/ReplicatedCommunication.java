package anacom.presentationserver.server.replication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.AsyncHandler;

import cacert.shared.stubs.CertificateType;

import anacom.presentationserver.server.ThreadContext;
import anacom.presentationserver.server.WaitingResponses;
import anacom.presentationserver.server.replication.protocols.FailureTolerator;
import anacom.shared.exceptions.CommunicationError;
import anacom.shared.exceptions.UDDI.QueryServiceException;
import anacom.shared.exceptions.UDDI.UDDICommunicationError;
import anacom.shared.stubs.client.BalanceLimitExceededException;
import anacom.shared.stubs.client.BonusValueNotValidException;
import anacom.shared.stubs.client.CantChangeStateException;
import anacom.shared.stubs.client.CantMakeVideoCallException;
import anacom.shared.stubs.client.CantMakeVoiceCallException;
import anacom.shared.stubs.client.CantReceiveVideoCallException;
import anacom.shared.stubs.client.CantReceiveVoiceCallException;
import anacom.shared.stubs.client.CommunicationErrorException;
import anacom.shared.stubs.client.DurationNotValidException;
import anacom.shared.stubs.client.FinishCallOnDestinationType;
import anacom.shared.stubs.client.FinishCallType;
import anacom.shared.stubs.client.IncompatiblePrefixException;
import anacom.shared.stubs.client.IncreasePhoneBalanceType;
import anacom.shared.stubs.client.InvalidAmountException;
import anacom.shared.stubs.client.InvalidCallTypeException;
import anacom.shared.stubs.client.InvalidPhoneTypeException;
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
import anacom.shared.stubs.client.OperatorNameAlreadyExistsException;
import anacom.shared.stubs.client.OperatorNameNotValidException;
import anacom.shared.stubs.client.OperatorPrefixAlreadyExistsException;
import anacom.shared.stubs.client.OperatorPrefixNotValidException;
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
import anacom.shared.stubs.client.VoidResponseType;

public class ReplicatedCommunication extends WSCommunication {
	
	/**
	 * Abstraction used to allow for different types of replication.
	 * WARNING: this works only because the presentation server serializes
	 * requests.
	 */
	private FailureTolerator protocol = null;
	
	/**
	 * Synchronization lock to avoid multiple executions on the same handle.
	 */
	Lock lock = new ReentrantLock();	
	
	/**
	 * Generic read used to handle responses to services. This handler
	 * invokes the FailureTolerator protocol object.
	 * @param <ResponseType> the type of the expected response
	 */
	private class Handler<ResponseType>
	implements AsyncHandler<ResponseType> {
		
		private boolean answerReady = false;
		private boolean terminated = false;
		
		private List<CertificateType> certificates = 
				new ArrayList<CertificateType>();
		
		boolean isReady() { return this.answerReady; }
		
		void waitForResponse() { this.answerReady = false; }
		
		@Override
		public void handleResponse(Response<ResponseType> res) {
			try {
				lock.lock();
				ResponseType response = res.get();
				CertificateType certificate =
						ThreadContext.getInstance().getCertificate();
				CertificateComparator certificateComparator = 
						new CertificateComparator();
				boolean duplicatedTest = false;
				for(CertificateType testCertificate : this.certificates) {
					if(certificateComparator.equals(certificate, testCertificate)) {
						duplicatedTest = true;
						break;
					}
				}
				if(duplicatedTest) {
					System.out.println("Replicated Certificate");
					return;
				}
				certificates.add(certificate);
				
				if (this.terminated) {
					return;
				}
				protocol.handleResponse(response);
			} catch (InterruptedException e) {			
				try {
					throw e.getCause();
				} catch(WebServiceException wse) {
					answerReady = true;
				} catch(Throwable t) {
					protocol.handleResponse(t);	
				}
			} catch (ExecutionException e) {
				try {
					throw e.getCause();
				} catch(WebServiceException wse) {
					answerReady = true;
				} catch(Throwable t) {
					protocol.handleResponse(t);	
				}
			} finally {
				if (protocol.hasTerminated()) {
					terminated = true;
					answerReady = true;
				}
				lock.unlock();
			}
		}
	}
	
	/**
	 * The class uses this method to wait for the response to be available.
	 * The thread that will run this method may not be interrupted. If a
	 * response arrives, this method will lock the lock.
	 * @param handler
	 * 		the handler processing the current request
	 */
	@SuppressWarnings("rawtypes")
	private void waitResponse(Handler handler) {
		this.lock.lock();
		while(!handler.isReady()) {
			this.lock.unlock();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				continue; // this thread should never be interrupted
			}
			this.lock.lock();
		}
		handler.waitForResponse();
	}
	
	public ReplicatedCommunication(FailureTolerator protocol) {
		super();
		this.protocol = protocol;
	}
	
	@Override
	public void registerOperator(OperatorDetailedType dto,
								String operatorPrefix)
			throws OperatorNameAlreadyExistsException,
				OperatorPrefixAlreadyExistsException,
				OperatorPrefixNotValidException, OperatorNameNotValidException,
				CommunicationErrorException, WebServiceException,
				BonusValueNotValidException, UDDICommunicationError,
				QueryServiceException {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).createNewOperatorAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while not terminated
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet (WebServiceException)?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createRegiterOperatorException(response);
	}

	@Override
	public void registerPhone(RegisterPhoneType dto, String operatorPrefix)
			throws InvalidPhoneTypeException, UnrecognisedPrefixException,
				PhoneAlreadyExistsException, PhoneNumberNotValidException,
				IncompatiblePrefixException, CommunicationErrorException,
				UDDICommunicationError, QueryServiceException,
				WebServiceException {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).registerPhoneAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createRegisterPhoneException(response);
	}

	@Override
	public PhoneType getPhoneBalance(PhoneNumberType dto, String operatorPrefix)
			throws WebServiceException, PhoneNumberNotValidException,
				UnrecognisedPrefixException, PhoneNotExistsException,
				CommunicationErrorException, UDDICommunicationError,
				QueryServiceException {
		this.lock.lock();
		setMessageContextToRead();
		Collection<String> bindings = super.query(operatorPrefix);
		Handler<PhoneType> callback = new Handler<PhoneType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).getPhoneBalanceAsync(dto, callback);	
			sentURLs.add(binding);
		}
		
		this.lock.unlock();
		waitResponse(callback);
		
		if (!protocol.hasResponse()) { // no response?
			
			// Refresh UDDI and reinvokes
			Collection<String> urls = super.refresh(operatorPrefix);
			int countDiff = bindings.size() - urls.size() - 1;
			protocol.setReplicationDegree(urls.size());
			Iterator<String> it = urls.iterator();
			while(it.hasNext()) {
				String url = it.next();
				if(sentURLs.indexOf(url) == -1) { // new host?
					super.setServer(url).getPhoneBalanceAsync(dto, callback);
				}
			}
			protocol.determineResponse();
			while (!protocol.hasTerminated()) { // while not terminated?
				this.lock.unlock();
				waitResponse(callback);
				if (!protocol.hasResponse()) { // no response yet?
					if (countDiff <= 0) { // all crashed WebServiceExceptions received?
						protocol.setReplicationDegree(
								protocol.getReplicationDegree() - 1);
						protocol.determineResponse();
					} else {
						--countDiff;
					}
				}
			}
		}
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createGetPhoneBalanceException(response);
		return (PhoneType) response;
	}

	@Override
	public void increasePhoneBalance(IncreasePhoneBalanceType dto,
			String operatorPrefix) throws PhoneNumberNotValidException,
			UnrecognisedPrefixException, PhoneNotExistsException,
			InvalidAmountException, BalanceLimitExceededException,
			CommunicationErrorException, WebServiceException,
			UDDICommunicationError, QueryServiceException {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).increasePhoneBalanceAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createIncreasePhoneBalanceException(response);
	}

	@Override
	public PhoneListType listOperatorPhones(OperatorPrefixType dto,
			String operatorPrefix) throws UnrecognisedPrefixException,
			CommunicationErrorException, WebServiceException,
			UDDICommunicationError, QueryServiceException {
		this.lock.lock();
		setMessageContextToRead();
		Collection<String> bindings = super.query(operatorPrefix);
		Handler<PhoneListType> callback = new Handler<PhoneListType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).listOperatorPhonesAsync(dto, callback);	
			sentURLs.add(binding);
		}
		
		this.lock.unlock();
		waitResponse(callback);
		
		if (!protocol.hasResponse()) { // no response?
			
			// Refresh UDDI and reinvokes
			Collection<String> urls = super.refresh(operatorPrefix);
			int countDiff = bindings.size() - urls.size() - 1;
			protocol.setReplicationDegree(urls.size());
			Iterator<String> it = urls.iterator();
			while(it.hasNext()) {
				String url = it.next();
				if(sentURLs.indexOf(url) == -1) { // new host?
					super.setServer(url).listOperatorPhonesAsync(dto, callback);
				}
			}
			protocol.determineResponse();
			while (!protocol.hasTerminated()) { // while no response?
				this.lock.unlock();
				waitResponse(callback);
				if (!protocol.hasResponse()) { // no response yet?
					if (countDiff <= 0) { // all crashed WebServiceExceptions received?
						protocol.setReplicationDegree(
								protocol.getReplicationDegree() - 1);
						protocol.determineResponse();
					} else {
						--countDiff;
					}
				}
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createListoperatorPhonesException(response);
		return (PhoneListType) response;
	}

	@Override
	public void cancelPhoneRegistry(PhoneNumberType dto, String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
			PhoneNotExistsException, CommunicationErrorException,
			WebServiceException, UDDICommunicationError, QueryServiceException {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).cancelPhoneRegistryAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createCancelPhoneRegistryException(response);
	}

	@Override
	public ReceiveSMSType sendSMS(SendSMSType dto, String operatorPrefix)
			throws PhoneNumberNotValidException, PhoneNotExistsException,
			NotPositiveBalanceException, UnrecognisedPrefixException,
			InvalidStateSendSMSException, SMSMessageNotValidException,
			InvalidAmountException, CommunicationErrorException,
			WebServiceException, UDDICommunicationError, QueryServiceException {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<ReceiveSMSType> callback = new Handler<ReceiveSMSType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).sendSMSAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createSendSMSException(response);
		return (ReceiveSMSType) response;
	}

	@Override
	public void setPhoneState(PhoneStateType dto, String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
			PhoneNotExistsException, CommunicationErrorException,
			InvalidStateException, CantChangeStateException,
			WebServiceException, UDDICommunicationError, QueryServiceException {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).setPhoneStateAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createSetPhoneStateException(response);
	}

	@Override
	public PhoneStateType getPhoneState(PhoneNumberType dto,
			String operatorPrefix) throws PhoneNumberNotValidException,
			UnrecognisedPrefixException, PhoneNotExistsException,
			CommunicationErrorException, WebServiceException,
			UDDICommunicationError, QueryServiceException {
		this.lock.lock();
		Collection<String> bindings = super.query(operatorPrefix);
		setMessageContextToRead();
		Handler<PhoneStateType> callback = new Handler<PhoneStateType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).getPhoneStateAsync(dto, callback);	
			sentURLs.add(binding);
		}
		
		this.lock.unlock();
		waitResponse(callback);
		
		if (!protocol.hasResponse()) { // no response?
			
			// Refresh UDDI and reinvokes
			Collection<String> urls = super.refresh(operatorPrefix);
			int countDiff = bindings.size() - urls.size() - 1;
			protocol.setReplicationDegree(urls.size());
			Iterator<String> it = urls.iterator();
			while(it.hasNext()) {
				String url = it.next();
				if(sentURLs.indexOf(url) == -1) { // new host?
					super.setServer(url).getPhoneStateAsync(dto, callback);
				}
			}
			protocol.determineResponse();
			while (!protocol.hasTerminated()) { // while no response?
				this.lock.unlock();
				waitResponse(callback);
				if (!protocol.hasResponse()) { // no response yet?
					if (countDiff <= 0) { // all crashed WebServiceExceptions received?
						protocol.setReplicationDegree(
								protocol.getReplicationDegree() - 1);
						protocol.determineResponse();
					} else {
						--countDiff;
					}
				}
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createGetPhoneState(response);
		return (PhoneStateType) response;
	}

	@Override
	public ReceivedSMSListType getPhoneSMSReceivedMessages(
			PhoneNumberType dto, String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
			PhoneNotExistsException, CommunicationErrorException,
			WebServiceException, UDDICommunicationError, QueryServiceException {
		this.lock.lock();
		Collection<String> bindings = super.query(operatorPrefix);
		setMessageContextToRead();
		Handler<ReceivedSMSListType> callback = new Handler<ReceivedSMSListType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).getPhoneSMSReceivedMessagesAsync(dto, callback);	
			sentURLs.add(binding);
		}
		
		this.lock.unlock();
		waitResponse(callback);
		
		if (!protocol.hasResponse()) { // no response?
			
			// Refresh UDDI and reinvokes
			Collection<String> urls = super.refresh(operatorPrefix);
			int countDiff = bindings.size() - urls.size() - 1;
			protocol.setReplicationDegree(urls.size());
			Iterator<String> it = urls.iterator();
			while(it.hasNext()) {
				String url = it.next();
				if(sentURLs.indexOf(url) == -1) { // new host?
					super.setServer(url).getPhoneSMSReceivedMessagesAsync(dto, callback);
				}
			}
			protocol.determineResponse();
			while (!protocol.hasTerminated()) { // while no response?
				this.lock.unlock();
				waitResponse(callback);
				if (!protocol.hasResponse()) { // no response yet?
					if (countDiff <= 0) { // all crashed WebServiceExceptions received?
						protocol.setReplicationDegree(
								protocol.getReplicationDegree() - 1);
						protocol.determineResponse();
					} else {
						--countDiff;
					}
				}
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createGetPhoneSMSReceivedMessage(response);
		return (ReceivedSMSListType) response;
	}

	@Override
	public LastMadeCommunicationType getLastMadeCommunication(
			PhoneNumberType dto, String operatorPrefix)
			throws PhoneNumberNotValidException, UnrecognisedPrefixException,
			PhoneNotExistsException, CommunicationErrorException,
			WebServiceException, NoMadeCommunicationException,
			UDDICommunicationError, QueryServiceException {
		this.lock.lock();
		Collection<String> bindings = super.query(operatorPrefix);
		setMessageContextToRead();
		Handler<LastMadeCommunicationType> callback = new Handler<LastMadeCommunicationType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).getLastMadeCommunicationAsync(dto, callback);	
			sentURLs.add(binding);
		}
		
		this.lock.unlock();
		waitResponse(callback);
		
		if (!protocol.hasResponse()) { // no response?
			
			// Refresh UDDI and reinvokes
			Collection<String> urls = super.refresh(operatorPrefix);
			int countDiff = bindings.size() - urls.size() - 1;
			protocol.setReplicationDegree(urls.size());
			Iterator<String> it = urls.iterator();
			while(it.hasNext()) {
				String url = it.next();
				if(sentURLs.indexOf(url) == -1) { // new host?
					super.setServer(url).getLastMadeCommunicationAsync(dto, callback);
				}
			}
			protocol.determineResponse();
			while (!protocol.hasTerminated()) { // while no response?
				this.lock.unlock();
				waitResponse(callback);
				if (!protocol.hasResponse()) { // no response yet?
					if (countDiff <= 0) { // all crashed WebServiceExceptions received?
						protocol.setReplicationDegree(
								protocol.getReplicationDegree() - 1);
						protocol.determineResponse();
					} else {
						--countDiff;
					}
				}
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createGetLastMadeCommunication(response);
		return (LastMadeCommunicationType) response;
	}

	@Override
	public void makeCall(MakeCallType dto, String operatorPrefix)
			throws 	UnrecognisedPrefixException,
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
					CommunicationError {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).makeCallAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createMakeCall(response);
	}

	@Override
	public FinishCallOnDestinationType finishCall(FinishCallType dto, 
			String operatorPrefix)
			throws 	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					DurationNotValidException,
					InvalidStateFinishMakingCallException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<FinishCallOnDestinationType> callback = 
				new Handler<FinishCallOnDestinationType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).finishCallAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createFinishCall(response);
		return (FinishCallOnDestinationType) response;
	}

	@Override
	public void receiveSMS(ReceiveSMSType dto, String operatorPrefix)
			throws 	PhoneNumberNotValidException,
					PhoneNotExistsException,
					UnrecognisedPrefixException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).receiveSMSAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createReceiveSMS(response);
	}

	@Override
	public void receiveCall(MakeCallType dto, String operatorPrefix)
			throws 	UnrecognisedPrefixException,
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
					CommunicationError {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).receiveCallAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createReceiveCall(response);
	}

	@Override
	public void finishCallOnDestination(FinishCallOnDestinationType dto,
										String operatorPrefix)
			throws 	PhoneNumberNotValidException,
					UnrecognisedPrefixException,
					PhoneNotExistsException,
					DurationNotValidException,
					InvalidStateFinishReceivingCallException,
					CommunicationErrorException,
					WebServiceException,
					UDDICommunicationError,
					QueryServiceException,
					CommunicationError {
		this.lock.lock();
		Collection<String> bindings = super.refresh(operatorPrefix);
		WaitingResponses.getInstance().addRequest(
				dto.getTimestamp(),
				bindings.size());
		setMessageContextToWrite(dto.getTimestamp(), operatorPrefix);
		Handler<VoidResponseType> callback = new Handler<VoidResponseType>();
		ArrayList<String> sentURLs = new ArrayList<String>();
		protocol.clearResponses();
		protocol.setReplicationDegree(bindings.size());
		for (String binding : bindings) {
			super.setServer(binding).finishCallOnDestinationAsync(dto, callback);	
			sentURLs.add(binding);
		}
		while (!protocol.hasTerminated()) { // while no response
			if (protocol.getReplicationDegree() <= 0) { // no servers left?
				throw new WebServiceException();
			}
			this.lock.unlock();
			waitResponse(callback);
			if (!protocol.hasResponse()) { // no response yet?
				WaitingResponses.getInstance().decreaseReplicationDegree(
						dto.getTimestamp());
				protocol.setReplicationDegree(
						protocol.getReplicationDegree() - 1);
				protocol.determineResponse();
			}
		}
		
		if (!protocol.hasResponse()) {
			this.lock.unlock();
			throw new WebServiceException();
		}
		
		Object response = protocol.getResponse();
		this.lock.unlock();
		new ExceptionFactory().createFinishCallOnDestination(response);
	}


}
