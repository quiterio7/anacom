package cacert.services;

import cacert.shared.exceptions.CacertException;
import jvstm.Atomic;

public abstract class CacertService {
	
	@Atomic
	public void execute() throws CacertException{
		dispatch();
	}
	
	public abstract void dispatch() throws CacertException;
}