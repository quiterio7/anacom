package anacom.replication.test;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.registry.JAXRException;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import javax.xml.ws.WebServiceException;

import org.jmock.Expectations;
import org.jmock.Mockery;

import anacom.presentationserver.server.AnacomPortTypeFactory;
import anacom.presentationserver.server.replication.protocols.FailureTolerator;
import anacom.presentationserver.server.replication.protocols.SilentFailureTolerator;
import anacom.shared.UDDI.UDDIQuery;
import anacom.shared.exceptions.UDDI.QueryServiceException;
import anacom.shared.stubs.client.AnacomApplicationServerPortType;
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


public class PacketReplicatorTestCase extends AnacomReplicationTestCase {
	
	/**
	 * This class will be used to control the calls to the port type factory.
	 * This class will have no behavior associated to it.
	 */
	class DummyAnacomPortTypeFactory
	extends AnacomPortTypeFactory {
		/**
		 * This object will be used to store the servers that the client tried to
		 * communicate. We use this list because apparently jmock does not allow
		 * mocking concrete classes... 
		 */
		private ArrayList<String> serverList = new ArrayList<String>();
		
		/**
		 * This method will be used to the set the target and return the 
		 * dummy anacom port type.
		 * @param url
		 * 		the new target.
		 * @return AnacomApplicationServerPortType
		 * 		the dummy anacom.
		 */
		@Override
		public AnacomApplicationServerPortType setServer(String url) {
			anacom.setTarget(url);
			serverList.add(url);
			return anacom;
		}
		
		/**
		 * This method is used to return the list of servers were sent to the setServer
		 * method.
		 * @return ArrayList<String>
		 */
		public ArrayList<String> getServerList() {
			return serverList;
		}
	}
	
	/**
	 * This class will be used to test the packet replication.
	 * Situations like an operator replic changed his address will be also tested.
	 *
	 */
	class DummyReplicatedCommunication 
	extends anacom.presentationserver.server.replication.ReplicatedCommunication {

		/**
		 * This exception will be used to store any exception catched during the 
		 * execution of the thread.
		 */
		private Exception exception = null;
		
		public DummyReplicatedCommunication(
				FailureTolerator protocol, 
				DummyUDDIQuery uddi,
				DummyAnacomPortTypeFactory portTypeFactory) {
			super(protocol);
			super.setUDDIQuery(uddi);
			super.setAnacomPortTypeFactory(portTypeFactory);
		}
		

		/**
		 * Method for setting the thread exception
		 * I know that this should be using some kind of locks but as this is a test
		 * and the execution environment will be controlled i decided not to use it.
		 * @param e
		 * 		the catched exception.
		 */
		public void setThreadException(Exception e) {
			this.exception = e;
		}
		
		/**
		 * Method for getting the thread exception
		 * I know that this should be using some kind of locks but as this is a test
		 * and the execution environment will be controlled i decided not to use it.
		 * @return Exception
		 * 		the catched exception.
		 */	
		public Exception getThreadException() {
			return this.exception;
		}
		
		/**
		 * This method is used to allow the parallel execution of the method 
		 * getPhoneBalance.
		 * @param dto
		 * 		the dto that will be parameter of the getPhoneBalance call.
		 * @param operatorPrefix
		 * 		the prefix that will be parameter of the getPhoneBalance call.
		 * @return Thread
		 * 		the thread whose run method will call the getPhoneBalance method.
		 */ 
		public Runnable getPhoneBalanceRunnable(
				final PhoneNumberType dto, final String operatorPrefix) {
			return new Runnable() {
				@Override
				public void run() {
					try {
						DummyReplicatedCommunication.this.getPhoneBalance(dto, operatorPrefix);
					} catch (Exception e) {
						DummyReplicatedCommunication.this.setThreadException(e);
					}
				}
			};};
		
	}
	
	/**
	 * This class will be used to test the calls to the web service interface. 
	 * It will have no behavior associated.
	 * Only a few methods of this class will be used by the tests so the others
	 * will be left incomplete.
	 */
	class DummyAnacom implements AnacomApplicationServerPortType {
		
		/**
		 * This string will identify the destination of the next web service call.
		 */
		private String target = null;
		/**
		 * This object will be used to apply jmock testing to the 
		 * AnacomApplicationServerPortType.
		 * I decided to do this because of:
		 * reason 1: although mocking concrete classes is not recommended, it 
		 * would give me much more work and complexity to test without using
		 * this class.
		 * reason 2: it is supposed to be possible to mock concrete classes 
		 * but i couldn't get it running...
		 */
		private final AnacomApplicationServerPortType mockedAnacom;
		
		public DummyAnacom(AnacomApplicationServerPortType mockedAnacom) {
			this.mockedAnacom = mockedAnacom;
		}
		
		/**
		 * This method is used to set the target URL.
		 * @param url
		 */
		public void setTarget(String target) {
			this.target = target; 	
		}
		
		/**
		 * Returns the mocked anacom
		 * @return AnacomApplicationServerPortType
		 */
		public AnacomApplicationServerPortType getMockedAnacom() {
			return this.mockedAnacom;
		}
		
		@Override
		public Response<VoidResponseType> createNewOperatorAsync(
				OperatorDetailedType parameters) {
			return null;
		}

		@Override
		public Future<?> createNewOperatorAsync(OperatorDetailedType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {
			return null;
		}

		@Override
		public VoidResponseType createNewOperator(OperatorDetailedType parameters)
				throws BonusValueNotValidException,
				OperatorNameAlreadyExistsException, OperatorNameNotValidException,
				OperatorPrefixNotValidException, CommunicationErrorException,
				OperatorPrefixAlreadyExistsException {
			return null;
		}

		@Override
		public Response<VoidResponseType> receiveSMSAsync(ReceiveSMSType parameters) {
			return null;
		}

		@Override
		public Future<?> receiveSMSAsync(ReceiveSMSType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {
			return null;
		}

		@Override
		public VoidResponseType receiveSMS(ReceiveSMSType parameters)
				throws UnrecognisedPrefixException, PhoneNumberNotValidException,
				PhoneNotExistsException, CommunicationErrorException {
			return null;
		}

		@Override
		public Response<PhoneType> getPhoneBalanceAsync(PhoneNumberType parameters) {
			return null;
		}
		

		/**
		 * This is not very good programming since we are changing an inner class
		 * from another inner class. I would be very pleased if you had a better
		 * idea.
		 */
		@Override
		public Future<?> getPhoneBalanceAsync(PhoneNumberType parameters,
				final AsyncHandler<PhoneType> asyncHandler) {
			mockedAnacom.getPhoneBalanceAsync(parameters, asyncHandler);
			Thread t = new Thread() {
							public void run() {
								asyncHandler.handleResponse(
									new DummyResponse<PhoneType>());			
						}
				}; 
			if(target.equals(URL_DOWN))
				t.run();
			else if(target.equals(URL_OLD)) {
				PacketReplicatorTestCase.this.uddi.removeURL(URL_OLD);
				PacketReplicatorTestCase.this.uddi.addURL(URL_NEW);
				t.run();
			}
			return null;
		}

		@Override
		public PhoneType getPhoneBalance(PhoneNumberType parameters)
				throws UnrecognisedPrefixException, PhoneNumberNotValidException,
				PhoneNotExistsException, CommunicationErrorException {
			return null;
		}

		@Override
		public Response<VoidResponseType> registerPhoneAsync(
				RegisterPhoneType parameters) {
			return null;
		}

		@Override
		public Future<?> registerPhoneAsync(RegisterPhoneType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {
			return null;
		}

		@Override
		public VoidResponseType registerPhone(RegisterPhoneType parameters)
				throws UnrecognisedPrefixException, PhoneNumberNotValidException,
				InvalidPhoneTypeException, PhoneAlreadyExistsException,
				CommunicationErrorException, IncompatiblePrefixException {
			return null;
		}

		@Override
		public Response<ReceivedSMSListType> getPhoneSMSReceivedMessagesAsync(
				PhoneNumberType parameters) {
			return null;
		}

		@Override
		public Future<?> getPhoneSMSReceivedMessagesAsync(
				PhoneNumberType parameters,
				AsyncHandler<ReceivedSMSListType> asyncHandler) {
			return null;
		}

		@Override
		public ReceivedSMSListType getPhoneSMSReceivedMessages(
				PhoneNumberType parameters) throws UnrecognisedPrefixException,
				PhoneNumberNotValidException, PhoneNotExistsException,
				CommunicationErrorException {
			return null;
		}

		@Override
		public Response<FinishCallOnDestinationType> finishCallAsync(
				FinishCallType parameters) {
			return null;
		}

		@Override
		public Future<?> finishCallAsync(FinishCallType parameters,
				AsyncHandler<FinishCallOnDestinationType> asyncHandler) {
			return null;
		}

		@Override
		public FinishCallOnDestinationType finishCall(FinishCallType parameters)
				throws UnrecognisedPrefixException, PhoneNumberNotValidException,
				InvalidStateFinishMakingCallException, PhoneNotExistsException,
				DurationNotValidException, CommunicationErrorException {
			return null;
		}

		@Override
		public Response<VoidResponseType> setPhoneStateAsync(
				PhoneStateType parameters) {
			return null;
		}

		@Override
		public Future<?> setPhoneStateAsync(PhoneStateType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {
			return null;
		}

		@Override
		public VoidResponseType setPhoneState(PhoneStateType parameters)
				throws InvalidStateException, UnrecognisedPrefixException,
				PhoneNumberNotValidException, PhoneNotExistsException,
				CantChangeStateException, CommunicationErrorException {
			return null;
		}

		@Override
		public Response<ReceiveSMSType> sendSMSAsync(SendSMSType parameters) {
			return null;
		}

		@Override
		public Future<?> sendSMSAsync(SendSMSType parameters,
				AsyncHandler<ReceiveSMSType> asyncHandler) {
			return null;
		}

		@Override
		public ReceiveSMSType sendSMS(SendSMSType parameters)
				throws NotPositiveBalanceException, UnrecognisedPrefixException,
				PhoneNumberNotValidException, PhoneNotExistsException,
				SMSMessageNotValidException, CommunicationErrorException,
				InvalidStateSendSMSException, InvalidAmountException {
			return null;
		}

		@Override
		public Response<VoidResponseType> makeCallAsync(MakeCallType parameters) {
			return null;
		}

		@Override
		public Future<?> makeCallAsync(MakeCallType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {
			return null;
		}

		@Override
		public VoidResponseType makeCall(MakeCallType parameters)
				throws NotPositiveBalanceException, 
				UnrecognisedPrefixException, PhoneNumberNotValidException,
				InvalidCallTypeException, InvalidStateMakeVoiceException,
				CantMakeVoiceCallException, PhoneNotExistsException,
				CommunicationErrorException, InvalidStateMakeVideoException,
				CantMakeVideoCallException {
			return null;
		}

		@Override
		public Response<VoidResponseType> cancelPhoneRegistryAsync(
				PhoneNumberType parameters) {
			return null;
		}

		@Override
		public Future<?> cancelPhoneRegistryAsync(PhoneNumberType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {
			return null;
		}

		@Override
		public VoidResponseType cancelPhoneRegistry(PhoneNumberType parameters)
				throws UnrecognisedPrefixException, PhoneNumberNotValidException,
				PhoneNotExistsException, CommunicationErrorException {
			return null;
		}

		@Override
		public Response<PhoneStateType> getPhoneStateAsync(
				PhoneNumberType parameters) {
			return null;
		}

		@Override
		public Future<?> getPhoneStateAsync(PhoneNumberType parameters,
				AsyncHandler<PhoneStateType> asyncHandler) {
			return null;
		}

		@Override
		public PhoneStateType getPhoneState(PhoneNumberType parameters)
				throws UnrecognisedPrefixException, PhoneNumberNotValidException,
				PhoneNotExistsException, CommunicationErrorException {
			return null;
		}

		@Override
		public Response<LastMadeCommunicationType> getLastMadeCommunicationAsync(
				PhoneNumberType parameters) {		
			return null;
		}

		@Override
		public Future<?> getLastMadeCommunicationAsync(PhoneNumberType parameters,
				AsyncHandler<LastMadeCommunicationType> asyncHandler) {		
			return null;
		}

		@Override
		public LastMadeCommunicationType getLastMadeCommunication(
				PhoneNumberType parameters) throws UnrecognisedPrefixException,
				PhoneNumberNotValidException, PhoneNotExistsException,
				NoMadeCommunicationException, CommunicationErrorException {		
			return null;
		}

		@Override
		public Response<VoidResponseType> finishCallOnDestinationAsync(
				FinishCallOnDestinationType parameters) {		
			return null;
		}

		@Override
		public Future<?> finishCallOnDestinationAsync(
				FinishCallOnDestinationType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {		
			return null;
		}

		@Override
		public VoidResponseType finishCallOnDestination(
				FinishCallOnDestinationType parameters)
				throws UnrecognisedPrefixException, PhoneNumberNotValidException,
				InvalidStateFinishReceivingCallException, PhoneNotExistsException,
				DurationNotValidException, CommunicationErrorException {		
			return null;
		}

		@Override
		public Response<VoidResponseType> receiveCallAsync(MakeCallType parameters) {
			return null;
		}

		@Override
		public Future<?> receiveCallAsync(MakeCallType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {		
			return null;
		}

		@Override
		public VoidResponseType receiveCall(MakeCallType parameters)
				throws CantReceiveVideoCallException, UnrecognisedPrefixException,
				PhoneNumberNotValidException, InvalidCallTypeException,
				PhoneNotExistsException, CantReceiveVoiceCallException,
				InvalidStateReceiveVoiceException, CommunicationErrorException,
				InvalidStateReceiveVideoException {		
			return null;
		}

		@Override
		public Response<VoidResponseType> increasePhoneBalanceAsync(
				IncreasePhoneBalanceType parameters) {		
			return null;
		}

		@Override
		public Future<?> increasePhoneBalanceAsync(
				IncreasePhoneBalanceType parameters,
				AsyncHandler<VoidResponseType> asyncHandler) {		
			return null;
		}

		@Override
		public VoidResponseType increasePhoneBalance(
				IncreasePhoneBalanceType parameters)
				throws UnrecognisedPrefixException, PhoneNumberNotValidException,
				PhoneNotExistsException, CommunicationErrorException,
				BalanceLimitExceededException, InvalidAmountException {		
			return null;
		}

		@Override
		public Response<PhoneListType> listOperatorPhonesAsync(
				OperatorPrefixType parameters) {		
			return null;
		}

		@Override
		public Future<?> listOperatorPhonesAsync(OperatorPrefixType parameters,
				AsyncHandler<PhoneListType> asyncHandler) {		
			return null;
		}

		@Override
		public PhoneListType listOperatorPhones(OperatorPrefixType parameters)
				throws UnrecognisedPrefixException, CommunicationErrorException {		
			return null;
		}

		@Override
		public Response<?> revogateCertificateAsync() {
			// this should be empty
			return null;
		}

		@Override
		public Future<?> revogateCertificateAsync(AsyncHandler<?> asyncHandler) {
			// this should be empty
			return null;
		}

		@Override
		public void revogateCertificate() throws CommunicationErrorException {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * This class is a dummy version of the Response object that is given to the
	 * handlers used in the asynchronous communication.
	 * This handler is used to simulate errors that would happen if the server does
	 * not answer.
	 * Most methods will be left empty because the are not needed for these tests.
	 * @param <ResponseType>
	 */
	class DummyResponse<ResponseType> implements Response<ResponseType> {

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return false;
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean isDone() {
			return false;
		}

		/**
		 * This method returns an exception simulating the fact that the server 
		 * does not answer.
		 */
		@Override
		public ResponseType get() throws InterruptedException, ExecutionException {
			InterruptedException ie = new InterruptedException();
			ie.initCause(new WebServiceException());
			throw ie;
		}

		@Override
		public ResponseType get(long timeout, TimeUnit unit)
				throws InterruptedException, ExecutionException, TimeoutException {
			return null;
		}

		@Override
		public Map<String, Object> getContext() {
			return null;
		}
		
	}
	
	/**
	 * This class will be used to test the calls to the UDDI server. It will have
	 * no behavior returning dummy results only. 
	 */
	class DummyUDDIQuery extends UDDIQuery {

		/**
		 * This list will simulate the actual UDDI cache.
		 */
		private ArrayList<String> urls;
		
		/**
		 * This constructor calls a special constructor of the super class.
		 * This special constructor does not create a new UDDI server connection.
		 */
		public DummyUDDIQuery() {
			super();
			this.urls = new ArrayList<String>();
		}
		
		/**
		 * Sets the url list.
		 * @param urls
		 * 		the new server list.
		 */
		public void setList(ArrayList<String> urls) {
			this.urls = urls;
		}
		
		/**
		 * This method returns the current urls on the dummy UDDI.
		 * @return	ArrayList<String>
		 */
		public ArrayList<String> getURLs() {
			return this.urls;
		}
		
		/**
		 * Adds a new URL to the list.
		 * @param url
		 * 		the URL to be added.
		 */
		public void addURL(String url) {
			this.urls.add(url);
		}
		
		/**
		 * Removes an URL from the list
		 * @param url
		 * 		the URL to be deleted.
		 */
		public void removeURL(String url) {
			this.urls.remove(url);
		}
		
		/**
		 * This method returns the operators URL list.
		 *  @param organizationName
		 *  	this will not be used.
		 *  @param serviceName
		 *  	this will not be used.
		 *  @return ArrayList<String>
		 *  	the URL list.
		 */
		@Override
		public ArrayList<String> query(String organizationName, String serviceName)
				throws JAXRException, QueryServiceException {
			return urls;
		}
	}
	
	private static PhoneNumberType createPhoneNumberType() 
			throws RuntimeException {
		try {
			PhoneNumberType pnt = new PhoneNumberType();
			pnt.setNumber(PHONE_NUMBER);
			pnt.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar());
			return pnt;
		} catch (DatatypeConfigurationException dce) {
			throw new RuntimeException(dce); // KILL TEST
		}
	}

	
	/**
	 * Objects that are common to all PacketReplicatorTestCase instances.
	 */
	private static final PhoneNumberType PHONE_NUMBER_TYPE = createPhoneNumberType();
	private static final String PHONE_NUMBER = "998765432";
	private static final String OPERATOR_PREFIX = "99";
	private static final FailureTolerator PROTOCOL = new SilentFailureTolerator();
	private static final String URL1 = "http:\\url1";
	private static final String URL2 = "http:\\url2";
	private static final String URL3 = "http:\\url3";
	private static final String URL_DOWN = "http:\\url_down";
	private static final String URL_OLD = "http:\\url_old";
	private static final String URL_NEW = "http:\\url_new";

	/**
	 * Objects that may change from test to test.
	 */
	private Mockery context = new Mockery();
	private DummyUDDIQuery uddi = null;
	private DummyAnacom anacom = null;
	private DummyReplicatedCommunication communication = null;
	private DummyAnacomPortTypeFactory portTypeFactory = null;
	
	public PacketReplicatorTestCase() {
		super();
	}
	
	public PacketReplicatorTestCase(String msg) {
		super(msg);
	}
	
	/**
	 * this method will set up the objects that will be used during the test.
	 */
	@Override
	protected void setUp() {
		uddi = new DummyUDDIQuery();
		anacom = new DummyAnacom(
				context.mock(AnacomApplicationServerPortType.class));
		portTypeFactory = new DummyAnacomPortTypeFactory();
		communication = new DummyReplicatedCommunication(
				PROTOCOL, uddi, portTypeFactory);
	}
	
	/**
	 * This method will be used by the assertEqualList to assert about the size
	 * of the two lists.
	 * @param al1
	 * @param al2
	 */
	protected void assertSameSize(ArrayList<String> al1, ArrayList<String> al2) {
		assertTrue("The list of servers on the uddi should have the same size of" +
				"the list of servers that were communicated", al1.size()==al2.size());
	}
	
	/**
	 * This method is used to compare two array lists of strings.
	 * This will be used when comparing the list of servers that were communicated
	 * against the server list on the uddi.
	 * @param al1
	 * @param al2
	 */
	protected void assertEqualList(ArrayList<String> al1, ArrayList<String> al2) {
		boolean eq = true;
		
		assertSameSize(al1, al2);
		
		for(int x = 0; x < al1.size(); x++)
			if(!al2.contains(al1.get(x)))
				eq = false;
		assertTrue("The actual list of servers should be the same as the list" +
				"of server that were communicated", eq);
		
	}
	
	/**
	 * This method returns the basic URL list.
	 * @return ArrayList<String>
	 */
	public ArrayList<String> createServerList() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(URL1);
		list.add(URL2);
		list.add(URL3);
		return list;
	}	
	
	
	/**
	 * Test 1 - Tests the correct packet replication. With all servers up.
	 */
	public void testPacketReplication() {
		uddi.setList(createServerList());
        
		context.checking(new Expectations() {{
            exactly(createServerList().size()).
            	of(anacom.getMockedAnacom()).
            		getPhoneBalanceAsync(
            				with(any(PhoneNumberType.class)), 
            				with(any(AsyncHandler.class)));
        }});

		Runnable runnable = communication.getPhoneBalanceRunnable(
				PHONE_NUMBER_TYPE, 
				OPERATOR_PREFIX);
		
		Thread t = new Thread(runnable);
		t.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			fail("The test was interrupted unexpectedly...");
		}
		
		assertTrue("An exception was raised unexpectedly...", 
				communication.getThreadException() == null);
		
		assertEqualList(portTypeFactory.getServerList(), uddi.getURLs());
		
        context.assertIsSatisfied();
	}
	
	/**
	 * Test 2 - Tests the correct packet replication while one server changed
	 * his address.	
	 */
	public void testServerChangedAddress() {
		uddi.setList(createServerList());
		uddi.addURL(URL_OLD);
        
		context.checking(new Expectations() {{
            exactly(createServerList().size()+2). //+2 because of the URL_OLD and URL_NEW
            	of(anacom.getMockedAnacom()).
            		getPhoneBalanceAsync(
            				with(any(PhoneNumberType.class)), 
            				with(any(AsyncHandler.class)));
        }});

		Runnable runnable = communication.getPhoneBalanceRunnable(
				PHONE_NUMBER_TYPE, 
				OPERATOR_PREFIX);
		
		Thread t = new Thread(runnable);
		t.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			fail("The test was interrupted unexpectedly...");
		}
		
		assertTrue("An exception was raised unexpectedly...", 
				communication.getThreadException() == null);
		
		ArrayList<String> newList = createServerList();
		newList.add(URL_OLD);	// the old URL (was deleted)
		newList.add(URL_NEW);	// the new URL (was inserted)
		assertEqualList(portTypeFactory.getServerList(), newList);
		
        context.assertIsSatisfied();	
	}
	
	/**
	 * Teste 3 - Tests the correct packet replication while one server is down.
	 */
	public void testServerDown() {
		uddi.setList(createServerList());
        uddi.addURL(URL_DOWN);
		
		context.checking(new Expectations() {{
            exactly(createServerList().size()+1). //+1 because of the URL_OLD
            	of(anacom.getMockedAnacom()).
            		getPhoneBalanceAsync(
            				with(any(PhoneNumberType.class)), 
            				with(any(AsyncHandler.class)));
        }});

		Runnable runnable = communication.getPhoneBalanceRunnable(
								PHONE_NUMBER_TYPE, 
								OPERATOR_PREFIX);

		Thread t = new Thread(runnable);
		t.start();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			fail("The test was interrupted unexpectedly...");
		}
		
		Exception e = communication.getThreadException();

		assertTrue("An Web Service Exception should not had been thrown",
				e == null);
		
		assertEqualList(portTypeFactory.getServerList(), uddi.getURLs());
			
        context.assertIsSatisfied();		
	}
}
