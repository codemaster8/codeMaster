package TestCases;

import java.sql.Connection;
import java.sql.SQLException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import SystemKeywords.BatchKeywords;
import SystemKeywords.ExecCommandOS;
import SystemKeywords.SSHCommand;
import SystemKeywords.SystemInformation;
import SystemKeywords.dbConnection;

public class BatchStreamDelete {
	SSHCommand SSH = new SSHCommand();
	ExecCommandOS ExeCmdWindow = new ExecCommandOS(); 
	SystemInformation SysInfo = new SystemInformation();
	BatchKeywords batchKW = new BatchKeywords();
	dbConnection dbconn = new dbConnection();
		
	@BeforeSuite
	public void SuiteLevelSetup(){
		SSH.setHostname("stdecqavot6.va.neustar.com");
		SSH.setUsername("deceapp");
		SSH.setPassword("Kn0od!e");
		SSH.sshConnect();
			
		SysInfo.systemHostname(SSH);
		SysInfo.systemPlatform(SSH);
		batchKW.batchInformation(SSH);
		batchKW.creatBatchBackup(SSH);
	}
		
	@Test
	public void getHostname_test(){
		//Result output = null;
		String cmd = "hostname";
		//SSH.executeCommand(cmd);
		String output = SSH.executeCommand(cmd);
		System.out.println(output);
		//batchKW.creatBatchBackup(SSH);
		String outputW = ExeCmdWindow.executeCommandWindows("ping -n 3 google.com");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(outputW);
		
		Connection Conn = dbconn.getConnection();
		String query = "select NODE_OID from Node where NODE_STATUS = 'deleted'";
		try {
			String db_output[] = dbconn.viewTable(Conn, query);
			for(int i = 0; i < db_output.length; i++){
				System.out.println(db_output[i]);}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}

	@AfterSuite
	public void SuiteLevelTeardown(){
		batchKW.restoreOriginalBatch(SSH);
		batchKW.removeBackupBatch(SSH);
		SSH.sshDisconnect();
	}

}
