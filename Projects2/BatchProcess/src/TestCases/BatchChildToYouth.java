package TestCases;

//import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
//import org.testng.fail;

import SystemKeywords.SSHCommand;
import SystemKeywords.SystemInformation;
import SystemKeywords.BatchKeywords;

public class BatchChildToYouth {
	//SuiteSetup setup = new SuiteSetup();
	//SSHCommand SSH = new SSHCommand();
	SSHCommand SSH = new SSHCommand();
	SystemInformation SysInfo = new SystemInformation();
	BatchKeywords batchKW = new BatchKeywords();
	
	@BeforeSuite
	public void SuiteLevelSetup(){
		SSH.setHostname("stdecqavot6.va.neustar.com");
		SSH.setUsername("deceapp");
		SSH.setPassword("Kn0od!e");
		SSH.sshConnect();
		
		//SysInfo.systemHostname(SSH);
		//SysInfo.systemPlatform(SSH);
		//batchKW.checkBatchFile(SSH);
		//batchKW.batchInformation(SSH);
		//batchKW.creatBatchBackup(SSH);
	}
	
	@Test
	public void getHostname_test(){
		String cmd = "hostname";
		String output = SSH.executeCommand(cmd);
		System.out.println(output);
		//batchKW.creatBatchBackup(SSH);
	}

	@AfterSuite
	public void SuiteLevelTeardown(){
		//batchKW.restoreOriginalBatch(SSH);
		//batchKW.removeBackupBatch(SSH);
		SSH.sshDisconnect();
	}
	

}
