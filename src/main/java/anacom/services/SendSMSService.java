package anacom.services;

import pt.ist.fenixframework.FenixFramework;
import anacom.domain.Network;
import anacom.domain.SMS;
import anacom.shared.dto.SMSDTO;
import anacom.shared.dto.SendSMSDTO;
import anacom.shared.exceptions.communication.SMSMessageNotValid;
import anacom.shared.exceptions.operator.UnrecognisedPrefix;
import anacom.shared.exceptions.phone.InvalidAmount;
import anacom.shared.exceptions.phone.NotPositiveBalance;
import anacom.shared.exceptions.phone.PhoneNotExists;
import anacom.shared.exceptions.phone.PhoneNumberNotValid;
import anacom.shared.exceptions.phone.invalidState.InvalidStateSendSMS;

public class SendSMSService extends AnacomService  {
	
	private SendSMSDTO 	sendSmsDTO;
	private SMSDTO smsDTO;
	
	public SendSMSService(SendSMSDTO dto) { 
		this.sendSmsDTO = dto; 
	}

	/**
     * @throws UnrecognisedPrefix
     * 		if the prefix is not recognized (source or destination's operator)	
     * @throws PhoneNotExists
     * 		if the phone does not exist (source)
     * @throws NotPositiveBalance
     * 		if the source phone does not have enough balance
     * @throws InvalidStateSendSMS
     * 		offstate and occupied state doen't allow phones to send SMSs
     * @throws SMSMessageNotValid
     * 		if the given message isn't valid SMS
     * @throws InvalidAmount
     * 		if there was an error calculating the SMS' cost that resulted in 0
     * 		or a negative cost
     * @throws PhoneNumberNotValid
     * 		if the given source or destination number isn't valid
	 */
	@Override
	public void dispatch() 
			throws UnrecognisedPrefix, PhoneNotExists, NotPositiveBalance,
				InvalidStateSendSMS, SMSMessageNotValid, InvalidAmount, 
				PhoneNumberNotValid {
		Network network = FenixFramework.getRoot();
		SMS sms = network.sendSMS(
				this.sendSmsDTO.getMessage(),
				this.sendSmsDTO.getSource(),
				this.sendSmsDTO.getDestination());
		this.smsDTO = 
				new SMSDTO(	sms.getMessage(), 
							sms.getOrigin().getNumber(),
							sms.getOtherParty(),
							0);
	}
	
	public SMSDTO getSMSDTO() {
		return this.smsDTO;
	}
	
}
