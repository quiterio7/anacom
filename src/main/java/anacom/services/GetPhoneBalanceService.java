package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.domain.Operator;
import anacom.shared.dto.PhoneDTO;
import anacom.shared.dto.PhoneNumberDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;

public class GetPhoneBalanceService extends AnacomService  {
	private PhoneDTO phoneDTO;
	private PhoneNumberDTO numberDTO;
	
	/**
	 * Constructor
	 * @param number	the number of the Phone
	 */

	public GetPhoneBalanceService(PhoneNumberDTO number){
		this.numberDTO = number;
	}
	
	/**
	 * Gets an Operator from the number received in the constructor;
	 * Creates a new DTO holding the number and balance of all those Phones;
	 * @throws UnrecognisedPrefix 	if the given prefix does not match any 
	 * 							  	operator in the network
	 * @throws PhoneNotExists		if the Phone with given number doesn't exist
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 */

	public final void dispatch() throws UnrecognisedPrefix, PhoneNotExists, PhoneNumberNotValid {
		Network network = FenixFramework.getRoot();
		
		Operator operator = network.getOperatorFromNumber(this.numberDTO.getNumber());

		this.phoneDTO = new PhoneDTO(
				this.numberDTO.getNumber(),
				operator.getPhoneBalance(this.numberDTO.getNumber()));
	}
	
	/**
	 * Gets the information of the Phone (number and balance)
	 * @return	Phone information.
	 */
	public PhoneDTO getPhoneDTO(){
		return this.phoneDTO;
	}
}
