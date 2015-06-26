package SystemKeywords;

public class Utils {
	
	public int convertTimeToSecond(SSHCommand SSH, String hms){
		/*
		 * This keyword  will parse give time in "HH:MM:SS" format to seconds
		 * e.g
		 * 18:40:00 into 67200
		 */
		
		String time[] = hms.split(":");
		int hour = Integer.parseInt(time[0]);
		int min = Integer.parseInt(time[1]);
		int sec = Integer.parseInt(time[2]);
		
		int seconds = hour*60*60 + min*60 + sec;
		return seconds;
		
	}
	
	public boolean verifyLogMessage(SSHCommand SSH, String cmd, int starttime){
		/*
		 * This method will verify log message in its log
		 * 
		 *  cmd = command to grep log
		 *  starttime = time from which log should exists (format HH:MM:SS)
		 *  
		 *  eg:
		 *  cmd = "grep 'Starting the batch Process of STREAM_INACTIVE' /opt/java7_DECE_batch/logs/java7_batch_process.log |awk {print $2}"  
		 *  starttime = 18:42:12
		 *  
		 *  will return true if message is found
		 */
		System.out.println(cmd);
		String time1 = SSH.executeCommand(cmd);
		time1 = time1.trim();
		boolean status = false;
		String singletime[] = time1.split("\\r?\\n");
		for (int i =0; i < singletime.length; i++){
			String temp[] = singletime[i].split(",");
			String stime = temp[0];
			int STIME = convertTimeToSecond(SSH, stime);
			if (STIME > starttime){
				status = true;
				break;
			}
		}
		System.out.println(status);
		return status;
	}

}
