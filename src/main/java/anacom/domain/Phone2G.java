package anacom.domain;

import anacom.shared.exceptions.phone.PhoneNumberNotValid;

public class Phone2G extends Phone2G_Base {
    
    public Phone2G(String number)
    		throws PhoneNumberNotValid {
    	super();
    	this.init(number);
    }
    
    /**
     * @return	true if this Phone supports SMS, false otherwise
     */
    @Override
    public boolean supportsSMS() { return true; }
    
    /**
     * @return	true if this Phone supports Voice communications, false otherwise
     */
    @Override
    public boolean supportsVoice() { return true; }
    
    /**
     * @return	true if this Phone supports Video communications, false otherwise
     */
    @Override
    public boolean supportsVideo() { return false; }
    
}
