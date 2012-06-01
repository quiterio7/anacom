package anacom.shared.misc;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import anacom.applicationserver.SecurityInfo;

import cacert.shared.stubs.BlockedSerialCertificateType;
import cacert.shared.stubs.CacertApplicationServerPortType;
import cacert.shared.stubs.CertificateSerialsListType;

public class GetBlackList  {
		
	private Timer timer;
	
	public GetBlackList(CacertApplicationServerPortType cacert) {
		
		timer = new Timer();
		timer.scheduleAtFixedRate(new Task(cacert), 0, Constants.UPDATE_LIST_TIME);
	}	    
	
	//Logic implementation
	private class Task extends TimerTask {

		private ArrayList<Long> list = new ArrayList<Long>(); 

		private CacertApplicationServerPortType cacert;
		
		public Task(CacertApplicationServerPortType cacert){
			
			this.cacert = cacert;
		
		}
		
		public void run() {
			
				list.clear();

				CertificateSerialsListType dto = cacert.getBlockedList();

				for(BlockedSerialCertificateType blockedSerial : dto.getSerialsDTOList()){
					list.add(blockedSerial.getSerial());
				}
				
				SecurityInfo.getInstance().setBlackList(list);
		}
		
	}
	
}
