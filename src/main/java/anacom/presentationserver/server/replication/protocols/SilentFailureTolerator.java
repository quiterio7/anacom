package anacom.presentationserver.server.replication.protocols;

public class SilentFailureTolerator extends FailureTolerator {

	@Override
	public void determineResponse() {
		if (this.hasTerminated()) {
			setResponse(getCurrentResponses().iterator().next());
		}
	}

	/**
	 * @return true if the protocol has terminated, false otherwise
	 */
	@Override
	public boolean hasTerminated() {
		return this.getCurrentResponses().size() >=
				this.getReplicationDegree()/2 + 1;
	}

}
