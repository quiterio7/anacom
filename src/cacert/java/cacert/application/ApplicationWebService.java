package cacert.application;

import cacert.services.AddToBlockedListService;
import cacert.services.GetBlockedListService;
import cacert.services.SignCertificateService;
import cacert.shared.dto.BlockCertificateDTO;
import cacert.shared.dto.BlockedSerialDTO;
import cacert.shared.dto.CertificateDTO;
import cacert.shared.dto.SignCertificateDTO;
import cacert.shared.exceptions.CantCreateCertificateException;
import cacert.shared.stubs.BlockCertificateType;
import cacert.shared.stubs.BlockedSerialCertificateType;
import cacert.shared.stubs.CacertApplicationServerPortType;
import cacert.shared.stubs.CertificateSerialsListType;
import cacert.shared.stubs.CertificateType;
import cacert.shared.stubs.SignCertificateType;
import cacert.shared.stubs.CertificateRemoteException;

@javax.jws.WebService(
		endpointInterface="cacert.shared.stubs.CacertApplicationServerPortType", 
		wsdlLocation="/cacert.wsdl",
		name="CacertApplicationServerPortType",
		portName="CacertApplicationServicePort",
		targetNamespace="http://cacert",
		serviceName="cacert"
)
@javax.jws.HandlerChain(file="handler-chain.xml")
public class ApplicationWebService  implements CacertApplicationServerPortType {
	
	/**
     * Gets the list of blocked certificates in this CA
     * @return	the list with all the blocked certificates
     */
	@Override
	public CertificateSerialsListType getBlockedList() {
		GetBlockedListService service = new GetBlockedListService();
		service.execute();
		
		CertificateSerialsListType remoteList = 
				new CertificateSerialsListType();
		
		for(BlockedSerialDTO blockedSerial : 
			service.getCertificatesList().getCertificatesList()) {
			BlockedSerialCertificateType tmp = 
					new BlockedSerialCertificateType();
			tmp.setSerial(blockedSerial.getSerial());
			remoteList.getSerialsDTOList().add(tmp);
		}
		
		return remoteList;
	}

	/**
     * Creates a new certificate signed by this Certificate Authority
     * @param parameters				a class holding	the entity requesting 
     * 									the certificate and the public key of 
     * 									the entity requesting the certificate
     * @return							a new certificate signed by this 
     * 									Certificate Authority
     * @throws CertificateRemoteException   	if there were problems on 
     * 												the generation of 
     * 												the certificate
     */
	@Override
	public CertificateType signCertificate(SignCertificateType parameters) 
			throws CertificateRemoteException {

		SignCertificateDTO dto = 
				new SignCertificateDTO(parameters.getEntityName(), 
						parameters.getPublicKey());
		
		try {
			SignCertificateService service = new SignCertificateService(dto);
			service.execute();
			
			CertificateDTO local = service.getCertificateDTO();
			
			CertificateType remote = new CertificateType();
			remote.setSerial(local.getSerial());
			remote.setSubject(local.getSubject());
			remote.setSignatureAlgorithm(local.getSignatureAlgorithm());
			remote.setSignature(local.getSignature());
			remote.setIssuer(local.getIssuer());
			remote.setValidFrom(local.getValidFrom());
			remote.setValidTo(local.getValidTo());
			remote.setKeyUsage(local.getKeyUsage());
			remote.setPublicKey(local.getPublicKey());
			remote.setThumbPrintAlgorithm(local.getThumbPrintAlgorithm());
			remote.setThumbPrint(local.getThumbPrint());

			return remote;
			
		}
		catch(CantCreateCertificateException ce){
			throw new CertificateRemoteException(ce.getMessage());
		}
	}

	/**
     * Adds the serial identified by oldSerial to the list of blocked serials 
     * (blocked certificates). Removes the old public key from the list of 
     * known server public keys and adds the new public key to it. Returns a 
     * new certificate for newPublicKey
     * @param parameters		a class holding	the serial number of the 
     * 							certificate to block and the public key of the 
     * 							new certificate
     * @return					a new certificate for newPublicKey
     * @throws CantCreateCertificateException	if there were problems on the 
     * 											generation of the certificate
     */
	@Override
	public CertificateType blockCertificate(BlockCertificateType parameters) 
			throws CertificateRemoteException {

		BlockCertificateDTO dto = 
				new BlockCertificateDTO(parameters.getOldSerial(), 
				parameters.getEntityName(), parameters.getOldPublicKey(), 
				parameters.getNewPublicKey());
		
		try{
			AddToBlockedListService service = new AddToBlockedListService(dto);
			service.execute();
			/*
			 * TODO: esta linha de codigo permite atualizar a lista de certificados
			 * revogados do Security Info
			 */
			SecurityInfo.getInstance().getBlackList().add(parameters.getOldSerial());
			
			CertificateDTO local = service.getCertificateDTO();
			
			CertificateType remote = new CertificateType();
			remote.setSerial(local.getSerial());
			remote.setSubject(local.getSubject());
			remote.setSignatureAlgorithm(local.getSignatureAlgorithm());
			remote.setSignature(local.getSignature());
			remote.setIssuer(local.getIssuer());
			remote.setValidFrom(local.getValidFrom());
			remote.setValidTo(local.getValidTo());
			remote.setKeyUsage(local.getKeyUsage());
			remote.setPublicKey(local.getPublicKey());
			remote.setThumbPrintAlgorithm(local.getThumbPrintAlgorithm());
			remote.setThumbPrint(local.getThumbPrint());

			return remote;
		}
		catch(CantCreateCertificateException ce){
			throw new CertificateRemoteException(ce.getMessage());
		}		
	}

}
