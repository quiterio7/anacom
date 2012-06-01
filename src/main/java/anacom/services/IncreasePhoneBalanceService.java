package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.IncreasePhoneBalanceDTO;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.BalanceLimitExceeded;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;

public class IncreasePhoneBalanceService extends AnacomService 
{
	private IncreasePhoneBalanceDTO increasePhoneBalanceDTO;
	
	/**
	 * Constructor
	 * @param dto	the number of the Phone and the amount you want to increase
	 */
	public IncreasePhoneBalanceService(IncreasePhoneBalanceDTO dto) {
		this.increasePhoneBalanceDTO = dto;
	}
	
	/**
	 * Increase the balance of the requested phone
	 * @throws UnrecognisedPrefix 		if the given prefix does not match any 
	 * 							  		operator in the network
	 * @throws PhoneNotExists			if the Phone with given number doesn't exist
	 * @throws BalanceLimitExceeded		Existing balance more amount to increase > 100
	 * @throws InvalidAmount			if the amount is null or negative  
	 * @throws PhoneNumberNotValid if the number isn't valid in Network Context
	 */
	public final void dispatch()
			throws PhoneNotExists, BalanceLimitExceeded, UnrecognisedPrefix,
				InvalidAmount, PhoneNumberNotValid {
		Network network = FenixFramework.getRoot();
		network.increasePhoneBalance(
				this.increasePhoneBalanceDTO.getNumber(), 
				this.increasePhoneBalanceDTO.getAmount());
	}
	
}
