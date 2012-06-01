package anacom.services;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.domain.SMS;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.dto.PhoneReceivedSMSListDTO;
import anacom.shared.dto.ReceivedSMSDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;

public class GetPhoneSMSReceivedMessagesService extends AnacomService {

	private PhoneNumberDTO numberDTO;
	private PhoneReceivedSMSListDTO smsListDTO;
	
	/**
	 * Constructor of GetPhoneSMSMessagesService
	 * Getter of all SMS received by a specific Phone
	 * @param number	the phone number which we can view SMS 
	 */
	public GetPhoneSMSReceivedMessagesService(PhoneNumberDTO number) {
		this.numberDTO = number;
		this.smsListDTO = null;
	}
	
	/**
	 * Execute a Service to build a PhoneSMSMessagesDTO 
	 * This method analyzes all the communications received
	 * by a specific number (passed in constructor method) 
	 * and collects only the SMS Communications
	 * @return void 
	 * @throws UnrecognisedPrefix	if the given prefix does not match any 
	 * 								operator in the network
	 * @throws PhoneNotExists 		if the given phone number does not exists
	 * 								in the operator specified
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 */
	@Override
	public void dispatch() 
			throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
		Network network = FenixFramework.getRoot();
		List<SMS> SMSlist = network.getSMSListfromNumber(this.numberDTO.getNumber());
		List <ReceivedSMSDTO> DTOlist = 
				new ArrayList<ReceivedSMSDTO>(SMSlist.size());
		
		for (SMS sms : SMSlist) {
				DTOlist.add(new ReceivedSMSDTO(	sms.getMessage(),
										 		sms.getOtherParty(), 
										 		sms.getCost()));
		}
		this.smsListDTO = new PhoneReceivedSMSListDTO(this.numberDTO.getNumber(), DTOlist);
	}

	/**
	 * Getter of a collection of communications with ReceiveSMSDTOs
	 * @return the List with all SMSs received by a Phone Number
	 */
	public PhoneReceivedSMSListDTO getDTOSMSList() {
		return smsListDTO;
	}
}
