package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.shared.dto.OperatorDetailedDTO;
import anacom.shared.exceptions.BonusValueNotValid;
import anacom.shared.exceptions.operator.OperatorNameAlreadyExists;
import anacom.shared.exceptions.operator.OperatorNameNotValid;
import anacom.shared.exceptions.operator.OperatorPrefixAlreadyExists;
import anacom.shared.exceptions.operator.OperatorPrefixNotValid;


public class CreateNewOperatorService extends AnacomService  {
	
	private OperatorDetailedDTO dto;
	/**
	 * Constructor
	 * @param dto	the OperatorDetailedDTO
	 */
	public CreateNewOperatorService(OperatorDetailedDTO dto) {
		this.dto = dto;
	}
	
	
	/**
	 * Assigns a DTO of Operator Information 
	 * to be registered in Network
	 * @throws OperatorNameAlreadyExists	if Operator Name already exists 
	 * @throws OperatorPrefixAlreadyExists  if Operator Prefix already exists
	 * @throws OperatorPrefixNotValid		if Operator Prefix isn't valid
	 * @throws OperatorNameNotValid			if Operator Name isn't valid
	 * @throws BonusValueNotValid			if Operator Tax Bonus isn't valid
	 */
	public final void dispatch()
			throws OperatorNameAlreadyExists, OperatorPrefixAlreadyExists,
				OperatorPrefixNotValid, OperatorNameNotValid, BonusValueNotValid {
		Network network = FenixFramework.getRoot();
		network.addOperator(
				this.dto.getName(), 
				this.dto.getPrefix(), 
				this.dto.getSmsCost(),
				this.dto.getVoiceCost(),
				this.dto.getVideoCost(),
				this.dto.getTax(),
				this.dto.getBonus());
	}
	
}