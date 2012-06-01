package anacom.domain;

import anacom.shared.misc.externRepresentation.phoneState.PhoneStateRepresentation;

public class OnState extends OnState_Base {
    
    public  OnState() {
        super();
    }
	
	/**
	 * @return	"On" string
	 */
    public String getStateType() {
    	return PhoneStateRepresentation.getInstance().getOnState();  
    }
    
}
