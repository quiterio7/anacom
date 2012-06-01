package anacom.shared.exceptions.UDDI;

public class UDDICommunicationError extends UDDIException {

	private static final long serialVersionUID = 1L;
	private final String stackTrace;
	
	// FIXME: as excepcoes do Java ja teem um construtor que permitem passar
	// um throwable como sendo o que causou esta excepcao. Possivelmente
	// e melhor do que andar a passar o stackTrace duma pa outro - neves
	// - fiz isto so por ser mais simples, barato e consistente com o resto do
	// - codigo. de qualquer maneira nao tenho nada contra a tua alternativa.
	
	/**
	 * Constructor
	 * @param stackTrace	the stack trace with some helpful information. 	
	 */
	public UDDICommunicationError(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	/**
	 * @return	the stack trace	
	 */
	public String getStack() { return this.stackTrace; }
	

}