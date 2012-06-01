package cacert.webservice.handlers;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;

import cacert.webservice.SecurityInfo;
import security.CryptoManager;
import cacert.shared.stubs.CertificateType;

public class SecurityHandler implements SOAPHandler<SOAPMessageContext> {

	private final String THUMBPRINT_ALGORITHM = "MD5";
	private final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

	private SecurityInfo securityInfo;

	/**
	 * Constructor
	 */
	public SecurityHandler() {
		this.securityInfo = SecurityInfo.getInstance();
		System.out.println("Created Security Handler");
	}

	/**
	 * Handles a closing message
	 * 
	 * @param context
	 *            a closing message
	 */
	@Override
	public void close(MessageContext context) {
		System.out.println("Handle Close");

	}

	/**
	 * Handles a fault message
	 * 
	 * @param context
	 *            a fault message
	 * @return true if handled correctly, false otherwise
	 */
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("Handle Fault");
		return this.handleMessage(context);
	}

	/**
	 * Handles a normal message
	 * 
	 * @param context
	 *            a normal message
	 * @return true if handled correctly, false otherwise
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		System.out.println("Handle Message");

		Boolean outboundProperty = (Boolean) context
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outboundProperty) {
			return this.handleOutboundMessage(context);
		} else {
			return this.handleInboundMessage(context);
		}
	}

	/**
	 * Handles an outgoing message. Attaches the certificate of this entity.
	 * Attaches the digital signature of this entity.
	 * 
	 * @param context
	 *            an outgoing message
	 * @return true if handled correctly, false otherwise
	 */
	protected boolean handleOutboundMessage(SOAPMessageContext context) {
		System.out.println("Handle Outbound Message");

		boolean answer = true;
		try {
			if (isSignCertificateRequestMessage(context)) {
				System.out.println("Sending SignCertificate Request:"
						+ " skipping validation");
				return true;
			}
			
			SOAPPart soapPart = context.getMessage().getSOAPPart();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

			SOAPHeader soapHeader = soapEnvelope.getHeader();
			if (soapHeader == null) {
				soapHeader = soapEnvelope.addHeader();
			}

			answer = answer && this.attachCertificate(soapEnvelope, soapHeader);
			answer = answer
					&& this.attachDigitalSignature(context, soapEnvelope,
							soapHeader);

		} catch (SOAPException e) {
			System.out.println("(Handle Outbound Message) "
					+ "Can't handle SOAP Message");
			e.printStackTrace();
			answer = false;
		}
		return answer;
	}

	/**
	 * Attaches this entity certificate to soapHeader
	 * 
	 * @param soapEnvelope
	 *            necessary for creating node names
	 * @param soapHeader
	 *            the header to which the certificate will be attached
	 * @throws SOAPException
	 *             if the message does not have the expected structure
	 */
	private boolean attachCertificate(SOAPEnvelope soapEnvelope,
			SOAPHeader soapHeader) throws SOAPException {
		CertificateType certificate = this.securityInfo.getCertificate();

		if (certificate == null) {
			return false;
		}
		
		this.attachSOAPElement("serial", certificate.getSerial() + "",
				soapEnvelope, soapHeader);
		this.attachSOAPElement("subject", certificate.getSubject(),
				soapEnvelope, soapHeader);
		this.attachSOAPElement("issuer", certificate.getIssuer(), soapEnvelope,
				soapHeader);
		this.attachSOAPElement("keyUsage", certificate.getKeyUsage(),
				soapEnvelope, soapHeader);
		this.attachSOAPElement("publicKey", certificate.getPublicKey(),
				soapEnvelope, soapHeader);
		this.attachSOAPElement("CASignature", certificate.getSignature(),
				soapEnvelope, soapHeader);
		this.attachSOAPElement("validFrom", certificate.getValidFrom() + "",
				soapEnvelope, soapHeader);
		this.attachSOAPElement("validTo", certificate.getValidTo() + "",
				soapEnvelope, soapHeader);
		this.attachSOAPElement("thumbPrintAlgorithm",
				certificate.getThumbPrintAlgorithm(), soapEnvelope, soapHeader);
		this.attachSOAPElement("thumbPrint", certificate.getThumbPrint(),
				soapEnvelope, soapHeader);
		this.attachSOAPElement("signatureAlgorithm",
				certificate.getSignatureAlgorithm(), soapEnvelope, soapHeader);

		return true;
	}

	/**
	 * Attaches this entity digital signature to soapHeader
	 * 
	 * @param context
	 *            for generating the signature from the context body
	 * @param soapEnvelope
	 *            necessary for creating node names
	 * @param soapHeader
	 *            the header to which the certificate will be attached
	 * @throws SOAPException
	 *             if the message does not have the expected structure
	 */
	private boolean attachDigitalSignature(SOAPMessageContext context,
			SOAPEnvelope soapEnvelope, SOAPHeader soapHeader)
			throws SOAPException {
		boolean answer = false;
		CryptoManager manager = CryptoManager.getInstance();
		String bodyContent = context.getMessage().getSOAPPart().getEnvelope()
				.getBody().getTextContent();
		String messageThumbPrint;
		try {
			messageThumbPrint = manager.makeThumbPrint(bodyContent,
					THUMBPRINT_ALGORITHM);
			String messageSignature = manager.makeDigitalSignature(
					this.securityInfo.getThisEntityPrivateKey(),
					messageThumbPrint, CIPHER_ALGORITHM);
			this.attachSOAPElement("messageSignature", messageSignature,
					soapEnvelope, soapHeader);
			this.attachSOAPElement("messageSignatureAlgorithm",
					CIPHER_ALGORITHM, soapEnvelope, soapHeader);
			answer = true;
		} catch (NoSuchAlgorithmException e) {
			System.out.println("This should never happen");
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			System.out.println("This should never happen");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			System.out.println("This should never happen");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Invalid key specification");
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			System.out.println("Invalid key specification");
			e.printStackTrace();
		} catch (BadPaddingException e) {
			System.out.println("Invalid key specification");
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			System.out.println("Invalid key specification");
			e.printStackTrace();
		}
		return answer;
	}

	/**
	 * Attaches a SOAP Element with the given name and value to header
	 * 
	 * @param name
	 *            the name of the element
	 * @param value
	 *            the value of the element
	 * @param envelope
	 *            necessary for creating node names
	 * @param header
	 *            the header to which the element will be attached
	 * @throws SOAPException
	 *             if the message does not have the expected structure
	 */
	private void attachSOAPElement(String name, String value,
			SOAPEnvelope envelope, SOAPHeader header) throws SOAPException {
		Name elementName;
		SOAPElement element;
		elementName = envelope.createName(name, "ca", "http://www.sd.com/");
		element = header.addChildElement(elementName);
		element.addTextNode(value);
	}

	/**
	 * Handles an in message. If the message is of the type SignCertificate,
	 * does not validate. Else checks the certificate, the digital signature,
	 * the time validity and if the certificate is not in the black list
	 * 
	 * @param context
	 *            an in message
	 * @return true if handled correctly, false otherwise
	 */
	protected boolean handleInboundMessage(SOAPMessageContext context) {

		try {
			if (isSignCertificateResponseMessage(context)) {
				System.out.println("Receiving SignCertificate Response:"
						+ " skipping validation");
				return true;
			}
			SOAPHeader header = this.getSecurityHeader(context);

			NodeList nodeList = header.getChildNodes();
			CertificateType certificate = this
					.recreateCertificate(nodeList);
			MessageSignature messageSignature = this
					.recreateMessageSignature(nodeList);

			boolean answer = this.verifyCertificateSignature(certificate);
			answer = answer
					&& verifyTimePeriod(certificate.getValidFrom(),
							certificate.getValidTo());
			answer = answer
					&& verifyMessageSignature(context, messageSignature,
							certificate);
			answer = answer && verifyBlackList(certificate.getSerial());
			return answer;
		} catch (SOAPException e) {
			System.out.println("Can't handle SOAP Message");
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
			return false;
		}
	}

	/**
	 * Tests if a message is of the type Sign Certificate
	 * 
	 * @param context
	 *            the message to test
	 * @return true if the message is of the type Sign Certificate false
	 *         otherwise
	 * @throws SOAPException
	 *             if the message does not have the expected structure
	 */
	private boolean isSignCertificateRequestMessage(SOAPMessageContext context)
			throws SOAPException {
		NodeList nodeList = context.getMessage().getSOAPBody().getChildNodes();
		if (nodeList.getLength() == 1
				&& nodeList.item(0).getNodeName().equals("signCertificate")) {
			return true;
		}
		return false;
	}
	
	private boolean isSignCertificateResponseMessage(SOAPMessageContext context)
			throws SOAPException {
		NodeList nodeList = context.getMessage().getSOAPBody().getChildNodes();
		if (nodeList.getLength() == 1
				&& nodeList.item(0).getNodeName().equals("certificate1")) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the header of the security attachments of a SOAP Message
	 * 
	 * @param message
	 *            the message in which we are interested
	 * @return the header of the security attachments of a SOAP Message
	 * @throws SOAPException
	 *             if the message does not have the expected structure
	 */
	private SOAPHeader getSecurityHeader(SOAPMessageContext message)
			throws SOAPException {
		SOAPPart soapPart = message.getMessage().getSOAPPart();
		SOAPEnvelope envelope = soapPart.getEnvelope();
		return envelope.getHeader();
	}

	/**
	 * Creates a certificate from a list of nodes. The certificate will have
	 * invalid fields if the list of nodes does not contain all of the X509
	 * fields.
	 * 
	 * @param nodeList
	 *            the list of nodes containing the certificate
	 * @return the certificate represented by the list of nodes
	 */
	private CertificateType recreateCertificate(NodeList nodeList) {
		CertificateType certificate = new CertificateType();
		for (int i = 0; i < nodeList.getLength(); i++) {

			String nodeName = nodeList.item(i).getLocalName();
			String nodeValue = nodeList.item(i).getTextContent();

			if (nodeName.equals("serial")) {
				certificate.setSerial(Long.parseLong(nodeValue));
			} else if (nodeName.equals("subject")) {
				certificate.setSubject(nodeValue);
			} else if (nodeName.equals("issuer")) {
				certificate.setIssuer(nodeValue);
			} else if (nodeName.equals("keyUsage")) {
				certificate.setKeyUsage(nodeValue);
			} else if (nodeName.equals("publicKey")) {
				certificate.setPublicKey(nodeValue);
			} else if (nodeName.equals("CASignature")) {
				certificate.setSignature(nodeValue);
			} else if (nodeName.equals("validFrom")) {
				certificate.setValidFrom(Long.parseLong(nodeValue));
			} else if (nodeName.equals("validTo")) {
				certificate.setValidTo(Long.parseLong(nodeValue));
			} else if (nodeName.equals("thumbPrintAlgorithm")) {
				certificate.setThumbPrintAlgorithm(nodeValue);
			} else if (nodeName.equals("thumbPrint")) {
				certificate.setThumbPrint(nodeValue);
			} else if (nodeName.equals("signatureAlgorithm")) {
				certificate.setSignatureAlgorithm(nodeValue);
			}
		}
		return certificate;
	}

	/**
	 * Creates a message signature from a list of nodes. The message signature
	 * will have invalid fields if the list of nodes does not contain a
	 * messageSignature and a messageSignatureAlgorithm
	 * 
	 * @param nodeList
	 *            the list of nodes containing the message signature
	 * @return the message signature represented by the list of nodes
	 */
	private MessageSignature recreateMessageSignature(NodeList nodeList) {
		MessageSignature messageSignature = new MessageSignature();
		for (int i = 0; i < nodeList.getLength(); i++) {

			String nodeName = nodeList.item(i).getLocalName();
			String nodeValue = nodeList.item(i).getTextContent();

			if (nodeName.equals("messageSignature")) {
				messageSignature.setSignature(nodeValue);
			} else if (nodeName.equals("messageSignatureAlgorithm")) {
				messageSignature.setSignatureAlgorithm(nodeValue);
			}
		}
		return messageSignature;
	}

	/**
	 * Verifies if a certificate has a valid signature.
	 * 
	 * @param certificate
	 *            the certificate to verify
	 * @return true if the certificate's signature is valid, false otherwise
	 * @throws NoSuchAlgorithmException
	 *             if the cipher algorithm is not correctly specified
	 * @throws IOException
	 *             if the key is not correctly specified
	 * @throws InvalidKeyException
	 *             if the key is not correctly specified
	 * @throws NoSuchPaddingException
	 *             if the cipher algorithm is not correctly specified
	 * @throws InvalidKeySpecException
	 *             if the cipher algorithm is not correctly specified
	 * @throws IllegalBlockSizeException
	 *             if the key is not correctly specified
	 * @throws BadPaddingException
	 *             if the key is not correctly specified
	 */
	private boolean verifyCertificateSignature(CertificateType certificate)
			throws NoSuchAlgorithmException, IOException, InvalidKeyException,
			NoSuchPaddingException, InvalidKeySpecException,
			IllegalBlockSizeException, BadPaddingException {
		boolean answer;
		CryptoManager manager = CryptoManager.getInstance();

		String certificateThumbPrint = this
				.getCertificateThumbPrint(certificate);
		answer = manager.verifyDigitalSignature(certificate.getSignature(),
				certificateThumbPrint,
				this.securityInfo.getCertificateAPublicKey(),
				certificate.getSignatureAlgorithm());
		System.out.println("CA signature is "
				+ (answer ? "correct" : "incorrect"));
		return answer;
	}

	/**
	 * Gets a thumbprint from a certificate
	 * 
	 * @param certificate
	 *            the certificate from which to obtain a thumbprint
	 * @return the thumbprint of the certificate
	 * @throws NoSuchAlgorithmException
	 *             if the thumbrpint algorithm is not correctly specified
	 * @throws IOException
	 *             if the key is not correctly specified
	 */
	private String getCertificateThumbPrint(CertificateType certificate)
			throws NoSuchAlgorithmException, IOException {
		String uniqueCertificateRepresentatin = certificate.getSerial()
				+ certificate.getSubject() + certificate.getIssuer()
				+ certificate.getKeyUsage() + certificate.getPublicKey()
				+ certificate.getValidFrom() + certificate.getValidTo()
				+ certificate.getThumbPrintAlgorithm()
				+ certificate.getSignatureAlgorithm();
		;
		CryptoManager manager = CryptoManager.getInstance();
		String thumbPrint = manager.makeThumbPrint(
				uniqueCertificateRepresentatin, THUMBPRINT_ALGORITHM);
		return thumbPrint;
	}

	/**
	 * Verifies the validity of the time period. A time period is valid if the
	 * current time period is higher than validFrom and lower than validTo.
	 * 
	 * @param validFrom
	 *            the starting valid time
	 * @param validTo
	 *            the ending valid time
	 * @return true if valid, false otherwise
	 */
	private boolean verifyTimePeriod(long validFrom, long validTo) {
		boolean answer;
		
		long currentDate = new Date().getTime();
		answer = currentDate > validFrom && currentDate < validTo;
		System.out.println("Time validity is "
				+ (answer ? "correct" : "incorrect"));

		return answer;
	}

	/**
	 * Verifies the digital signature of a message
	 * 
	 * @param message
	 *            the digitally signed SOAP Message
	 * @param messageSignature
	 *            the signature of the message
	 * @param certificate
	 *            the certificate of the entity that signed the message
	 * @return true if valid, false otherwise
	 * @throws InvalidKeyException
	 *             if the key is not correctly specified
	 * @throws NoSuchAlgorithmException
	 *             if the cipher algorithm is not correctly specified
	 * @throws NoSuchPaddingException
	 *             if the cipher algorithm is not correctly specified
	 * @throws InvalidKeySpecException
	 *             if the key is not correctly specified
	 * @throws IllegalBlockSizeException
	 *             if the key is not correctly specified
	 * @throws BadPaddingException
	 *             if the key is not correctly specified
	 * @throws IOException
	 *             if the key is not correctly specified
	 * @throws DOMException
	 *             if the message does not have the expected structure
	 * @throws SOAPException
	 *             if the message does not have the expected structure
	 */
	private boolean verifyMessageSignature(SOAPMessageContext message,
			MessageSignature messageSignature, CertificateType certificate)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException,
			IllegalBlockSizeException, BadPaddingException, IOException,
			DOMException, SOAPException {
		boolean answer;

		CryptoManager manager = CryptoManager.getInstance();
		String bodyContent = message.getMessage().getSOAPBody()
				.getTextContent();
		String messageThumbPrint = manager.makeThumbPrint(bodyContent,
				THUMBPRINT_ALGORITHM);
		answer = manager.verifyDigitalSignature(
				messageSignature.getSignature(), messageThumbPrint,
				certificate.getPublicKey(),
				messageSignature.getSignatureAlgorithm());
		System.out.println("Message signature is "
				+ (answer ? "correct" : "incorrect"));
		return answer;
	}

	/**
	 * Verifies if a certificate is not in the list of blocked certificates
	 * 
	 * @param serial
	 *            the serial of the certificate
	 * @return true if it is, false otherwise
	 */
	private boolean verifyBlackList(long serial) {
		boolean answer;
		answer = !this.securityInfo.getBlackList().contains(serial);
		System.out.println("Black List validity is "
				+ (answer ? "correct" : "incorrect"));
		return answer;
	}

	@Override
	public Set<QName> getHeaders() {
		return Collections.emptySet();
	}

	private class MessageSignature {
		private String signature;
		private String signatureAlgorithm;

		/**
		 * Gets the signature of the message
		 * 
		 * @return the signature of the message
		 */
		public String getSignature() {
			return signature;
		}

		/**
		 * Gets the signature algorithm of the message
		 * 
		 * @return
		 */
		public String getSignatureAlgorithm() {
			return signatureAlgorithm;
		}

		/**
		 * Sets the signature of the message
		 * 
		 * @param signature
		 *            the signature to assign
		 */
		public void setSignature(String signature) {
			this.signature = signature;
		}

		/**
		 * Sets the signature algorithm of the message
		 * 
		 * @param signatureAlgorithm
		 *            the signature algorithm to assign
		 */
		public void setSignatureAlgorithm(String signatureAlgorithm) {
			this.signatureAlgorithm = signatureAlgorithm;
		}

	}
}
