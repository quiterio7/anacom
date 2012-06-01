package anacom.services;

import jvstm.Atomic;
import anacom.shared.exceptions.AnacomException;

public abstract class AnacomService {

	@Atomic
	public void execute() throws AnacomException {
		this.dispatch();
	}
	
	public abstract void dispatch() throws AnacomException;
}
