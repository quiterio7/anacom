package cacert.shared.exceptions;

public class CacertException extends RuntimeException{

	private static final long serialVersionUID = 9234832432848324L;
	
	public void rethrow() {
		throw this;
	}
}
