package SystemKeywords;

//import java.io.InputStream;
//import java.io.OutputStream;
//import java.nio.channels.Channel;

//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.Session;

//import SSH.ExecShellScript;
//import SSH.Result;
//import SSH.SSHExec;


public class BatchKeywords {
	
	private String ORIGINAL = "/opt/java7_DECE_batch/config/batchProcessCronSettings.properties";
	private String BACKUP = "/opt/java7_DECE_batch/config/batchProcessCronSettings.properties_bck";
	private String LOGFILE = "/opt/java7_DECE_batch/logs/java7_batch_process.log";
	
	public void checkBatchFile(SSHCommand SSH){
			SSH.executeCommand("rm -rf ~/tempBatchProcessAuto");
			SSH.executeCommand("mkdir ~/tempBatchProcessAuto");
			SSH.executeCommand("/usr/bin/jar -xvf /opt/java7_DECE_batch/dece-batch.jar");
			SSH.executeCommand("mv biz/ tempBatchProcessAuto/");
			SSH.executeCommand("mv META-INF/ tempBatchProcessAuto/");
		}
	
	public String batchInformation(SSHCommand SSH){
		String metafest = "META-INF";
		String meta_file = SSH.executeCommand("ls /opt/java7_DECE_batch/");
		Boolean found = false;
		
		found = meta_file.contains(metafest);
		String META_file = null;
		if (found == false){
					checkBatchFile(SSH);
					//String cmd = "cat /home/deceapp/META-INF/MANIFEST.MF| grep 'Built-By\\|Implementation-Title\\|Implementation-Version\\|Implementation-Vendor\\|Build-Tag'";
					String cmd = "cat /home/deceapp/tempBatchProcessAuto/META-INF/MANIFEST.MF| grep 'Implementation-Version' |awk '{print $3}'";
					META_file = SSH.executeCommand(cmd);
				}
		else{

			String cmd = "cat /opt/java7_DECE_batch/META-INF/MANIFEST.MF| grep 'Built-By\\|Implementation-Title\\|Implementation-Version\\|Implementation-Vendor\\|Build-Tag'";
			META_file = SSH.executeCommand(cmd);
		}
		System.out.println("BUILT      :   "+META_file.trim());
		return META_file;
	}
	
	public void creatBatchBackup(SSHCommand SSH){
		
		System.out.println("\nCreating backup of batchProcessCronSettings.properties ..........");
		String checkbackupfile = SSH.executeCommand("ls -l /opt/java7_DECE_batch/config/");
		if (checkbackupfile.contains("batchProcessCronSettings.properties_bck") == true){
			System.out.println("Backup already exists. No need to create another backup");
		}
		else{
			String cmd = "cp " + ORIGINAL + ' ' + BACKUP;
			SSH.executeCommand(cmd);
		}
	
	}
	
	public void restoreOriginalBatch(SSHCommand SSH){
		System.out.println("Restoring original batchProcessCronSettings.properties ..........");
		String cmd = "cp " + BACKUP + ' ' + ORIGINAL;
		SSH.executeCommand(cmd);
	
	}
	
	public void removeBackupBatch(SSHCommand SSH){
		System.out.println("\nRemoving backup batchProcessCronSettings.properties ..........");
		String cmd = "rm " + BACKUP;
		SSH.executeCommand(cmd);
	
	}
	
	public void setBatchTime(SSHCommand SSH){
		String time = SSH.executeCommand("date +%H:%M:%S");
		System.out.println("\tCurrent Time : " +time.trim());
		
		String hour = SSH.executeCommand("date +%H");
		String minute = SSH.executeCommand("date +%M");
		int min = Integer.parseInt(minute.trim()) + 2;
		
		String currenttime = getBatchCronTime(SSH, "streamInactive");
		
		String cron = "streamInactive=0 " + min +  " " + hour.trim() +" * * ?";
	    String crontime = "0 " + min +  " " + hour.trim();	// Unix Command to replace srting: sed -i '/^streamInactive=/s/0 0 21/0 33 20/' batchProcessCronSettings.properties
		String cmd = "sed -i '/^streamInactive=/s/" + currenttime + "/" + crontime + "/' " + ORIGINAL;
		System.out.println("\tNew Stream Inactive Cron Time: < " + cron + " >" + "\n");
		SSH.executeCommand(cmd);
		restartBatchProcess(SSH);	//need to restart batchprocess after config is changed
	}
	
	public String getBatchCronTime(SSHCommand SSH, String batchname){
		String settime = SSH.executeCommand("grep -i '" + batchname + "' " + ORIGINAL + "|grep -v '#'");
		settime = settime.split("=")[1];
		settime = settime.split("\\?")[0];
		return settime.trim();
	}
	
	public void restartBatchProcess(SSHCommand SSH){
		SSH.executeScript("/opt/java7_DECE_batch/java7_batch_process.sh restart");
	}
	
	
	
	public void watchLog(SSHCommand SSH,String Log,String Date){
		String cmd = "grep '" + Log + "' " + LOGFILE;
		String output = SSH.executeCommand(cmd);
		System.out.println(output);
	}
	
	public String[] getDatabaseInfo(SSHCommand SSH){
		String[] db;
		db = new String[3];
		
		String batchproperties = "/opt/java7_DECE_batch/config/batchProcess.properties";
		String dburl = "grep -i 'db.url' " + batchproperties;
		String dbusername = "grep -i 'db.username' " + batchproperties;
		String dbpassword = "grep -i 'db.password' " + batchproperties;
		
		String url = SSH.executeCommand(dburl);
		String username = SSH.executeCommand(dbusername);
		String password = SSH.executeCommand(dbpassword);
		
		url = url.trim().split("=")[1];
		username = username.trim().split("=")[1];
		password = password.trim().split("=")[1];
		
		db[0] = url;
		db[1] = username;
		db[2] = password;
		
		return db;
	}
	
	public Boolean checkBatchRunning(SSHCommand SSH){
		String cmd = "ps aux | grep BatchProcessLauncher";
		String batchprocess = SSH.executeCommand(cmd);
		System.out.println(batchprocess);
		Boolean process = false;
		String[] batchP = batchprocess.trim().split("\n");
		for(int i =0; i < batchP.length; i++){
			//System.out.println(batchP[i]);
			boolean b = batchP[i].indexOf("grep") > 0;
			if(b==false){
				process = true;
				break;
			}
			//System.out.println(b);
		}
		return process;
	}
	
}
