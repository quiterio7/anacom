package anacom.services;

import java.util.ArrayList;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.domain.Operator;
import anacom.domain.Phone;
import anacom.shared.dto.ListPhonesDTO;
import anacom.shared.dto.OperatorPrefixDTO;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;

public class ListOperatorPhonesService extends AnacomService {
	
	private OperatorPrefixDTO operatorPrefixDTO;
	private ListPhonesDTO listPhonesDTO;
	
	/**
	 * Constructor
	 * @param prefix	the prefix of the Operator
	 */
	public ListOperatorPhonesService(OperatorPrefixDTO operator) {
		this.operatorPrefixDTO = operator;
	}
	
	/**
	 * Assigns a DTO holding the list of Phones from an Operator
	 * to a class variable so that it can be retrieved.
	 * @throws UnrecognisedPrefix if the given prefix does not match any 
	 * 							  operator in the network
	 */
	public final void dispatch() throws UnrecognisedPrefix {
		Network network = FenixFramework.getRoot();
		ArrayList<PhoneDTO> phones = new ArrayList<PhoneDTO>();
		
		Operator operator = network.getOperatorFromPrefix(this.operatorPrefixDTO.getPrefix());
    	
		for(Phone phone : operator.getPhone()) {
			PhoneDTO tmp = new PhoneDTO(phone.getNumber(),phone.getBalance());
			phones.add(tmp);
		}
		
		this.listPhonesDTO = new ListPhonesDTO(phones);
	}
	
	/**
	 * Gets the list of all the Phones registered in an Operator
	 * @return	the list of Phones
	 */
	public ListPhonesDTO getOperatorPhones() {
		return this.listPhonesDTO;
	}
}