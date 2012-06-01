package anacom.presentationserver.server.replication.protocols;

import anacom.presentationserver.server.replication.GenericComparator;
import anacom.presentationserver.server.replication.TimestampMaster;

public class ByzantineFailureTolerator extends FailureTolerator {

	@Override
	public void determineResponse() {
		if(this.hasTerminated()) {
			Object[] array = getCurrentResponses().toArray();
			int size = getCurrentResponses().size();
			if (size > 2) {
				for (int x = 0; x < size; x++) {
					for (int y = x + 1; y < size; y++) {
						if ((new GenericComparator()).equals(array[x],array[y])) {
							setResponse(array[x]);
							return;
						}
					}	
				}
			} else {
				if (size == 1 ||
						(new TimestampMaster()).getTimestamp(array[0]).compare(
						(new TimestampMaster()).getTimestamp(array[1])) > 0) {
					setResponse(array[0]);
				} else {
					setResponse(array[1]);
				}
			}
		}
	}

	/**
	 * @return true if the protocol has terminated, false otherwise
	 */
	@Override
	public boolean hasTerminated() {
		return this.getCurrentResponses().size() >=
				this.getReplicationDegree()/2 + 2 ||
				(this.getReplicationDegree() < 3 &&
					this.getReplicationDegree() ==
					this.getCurrentResponses().size());
	}

}
