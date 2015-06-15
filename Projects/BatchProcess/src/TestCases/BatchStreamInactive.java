package TestCases;

//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.Connection;
//import java.sql.SQLException;

import java.sql.SQLException;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
//import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import SystemKeywords.BatchKeywords;
import SystemKeywords.ExecCommandOS;
import SystemKeywords.SSHCommand;
import SystemKeywords.StreamBatchProcess;
import SystemKeywords.SystemInformation;
import SystemKeywords.dbConnection;
import SystemKeywords.Utils;


public class BatchStreamInactive {
	/*
	 * PREREQUISIT
	 * 
	 * Current config in /opt/java7_DECE_batch/config/batchProcessParams.properties
	 * 
	 * timeDifferenceStreamInactiveMinutes=720
	 * stopContinuesJobMinutes=30
	 * 
	 * Change it to
	 * 
	 * timeDifferenceStreamInactiveMinutes=10
	 * stopContinuesJobMinutes=5
	 * 
	 */
	private int inactiveStreamCount;
	private String logtime;
	private String logtime_hms;
	private String[] stream;
	
	SSHCommand SSH = new SSHCommand();
	ExecCommandOS ExeCmdWindow = new ExecCommandOS(); 
	SystemInformation SysInfo = new SystemInformation();
	BatchKeywords batchKW = new BatchKeywords();
	dbConnection dbconn = new dbConnection();
	Utils utils= new Utils();
	StreamBatchProcess StreamBP = new StreamBatchProcess();
		
	@BeforeSuite
	public void SuiteLevelSetup(){
		System.out.print("\t TEST Suite Setup\n");
		SSH.setHostname("stdecqavot4.va.neustar.com");
		SSH.setUsername("deceapp");
		SSH.setPassword("Kn0od!e");
		SSH.sshConnect();
			
		SysInfo.systemHostname(SSH);
		SysInfo.systemPlatform(SSH);
		//System.out.println("\n");
		batchKW.batchInformation(SSH);
		batchKW.creatBatchBackup(SSH);
	}
	
	@BeforeTest
	public void getDatbaseInformation(){
		System.out.println("******************************************************");
		System.out.print("\t TEST get databae Information\n");
		String[] databaseInfo = batchKW.getDatabaseInfo(SSH);
		
		String dburl = databaseInfo[0];
		String dbusername = databaseInfo[1];
		String dbpassword = databaseInfo[2];
		
		dbconn.setHostname(dburl);
		dbconn.setUsername(dbusername);
		dbconn.setPassword(dbpassword);
		
		
		System.out.println("\nDatabase  Name : " + (dburl.split("@"))[1].split(":")[0]);
		System.out.println("Login Username : " + dbusername);
		//System.out.println("Login Password : " + dbpassword + "\n");
	}
	
	@BeforeMethod
	public void seperator(){
		System.out.println("******************************************************");
	}
	
	@Test(enabled = true,priority=1)
	public void validateBatchProcesIsRunning_test01(){
		/*
		 * This test is placeholder for environment updates
		 * for future
		 */
		
		System.out.print("\tTEST001 validate batch process is running\n\n");
		Boolean process = batchKW.checkBatchRunning(SSH);
		if (process == true){
			System.out.println("\t``` BATCH PROCESS RUNNING ```");
		}
		else{
			System.out.println("\t??? BATCH PROCESS NOT RUNNING ???");
			Assert.fail("BATCH PROCESS NOT RUNNING");
		}
	}
	
	@Test(enabled = true, priority=2)
	public void getStreamInactive_test02(){
		/*
		 * In this test step we get number of inactive stream available in database 
		 */
		
		System.out.print("\t TEST002 get Stream Inactive\n\n");
		Connection Conn = dbconn.getConnection();
		String no_of_inactive_stream = dbconn.getStreamInactiveCount(Conn);
		inactiveStreamCount = Integer.parseInt(no_of_inactive_stream);
		System.out.println("\tNumber of INACTIVE STREAM in DB : " + inactiveStreamCount);
		if (inactiveStreamCount >= 500){
			System.out.println("\n ----------------------------------------\n");
			System.out.println("|      Too many inactive data in DB      |");
			System.out.println("|     Automation will take long time     |");
			System.out.println("\n ----------------------------------------\n");
			Assert.fail("Automation will take longtime");
		}
	}
	
	@Test(enabled = false, priority=3)
	public void creatStreamData_test03(){
		/*
		 * Based on number of inactive stream (from test001), 
		 * we choose which test values to use further in out test
		 */
		
		System.out.print("\t TEST003 Create Stream Data\n\n");
		StreamBP.createStream();
	}
	
	@Test(enabled = true,priority=4)
	public void validateStreamDataCreation_test04(){
		/*
		 * This will validate if Stream data are created or not. 
		 */
		
		System.out.print("\t TEST004 validate Stream data creation\n\n");
		try {
			stream = StreamBP.getStreamCreate();
			if (stream == null){
				System.out.println("\tStream Creation Failed");
				System.out.println("\tPlease verify SOAPUI is working fine");
				Assert.fail("STREAM INACTIVE FAILED DUE TO SOAPUI");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n");
	}
	
	@Test(enabled = false, priority=5)
	public void changeExpirationDateOfStream_test05(){
		/*
		 * This teststep will change the expiration date 
		 * of stream to yesterday's date
		 */
		
		System.out.print("\t TEST005 change expiration date of all stream\n\n");
		//Connection Conn = dbconn.getConnection();
		for (int i = 0 ; i < stream.length; i++){
			int x = i + 1;
			String y = Integer.toString(x);
			try {
				Connection Conn = dbconn.getConnection();
				dbconn.updateExpirationDate(Conn, stream[i], y);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n");
		}
	
	@Test(enabled = true, priority = 6)
	public void setLogTime_test06(){
		/*
		 * Setting up clock for batch log
		 * Time in batch log is in format "2014-08-11 21:54:51"
		 */
		System.out.print("\t TEST006 set log time and date\n\n");
		String cmd = "date +'%Y-%m-%d %H:%M:%S'";
		logtime = SSH.executeCommand(cmd).trim();
		System.out.println("CURRENT TIME: " + logtime);
		
		String cmdtime = "date +'%H:%M:%S'";
		logtime_hms = SSH.executeCommand(cmdtime).trim();
		System.out.println("logtime_hms:" + logtime_hms + "\n");
	}
	
	@Test(enabled = true,priority = 7)
	public void setStreamInactivebatchCron_test07(){
		/*
		 * This test step will set cron for Steam Inactive approx. one minute from now 
		 */
		
		System.out.print("\t TEST007 set Stream Inactive batch Cron\n\n");
		batchKW.setBatchTime(SSH);
	}
	
	@Test(enabled = true,priority = 8)
	public void validateStreamInactiveStart_test08(){
		/*
		 * This will validate if STREAM INACTIVE has been started properly or not 
		 * by verifying log message
		 */
		
		System.out.print("\tTEST008 Validate Stream Inactive job starts\n\n");
		int l_time = utils.convertTimeToSecond(SSH, logtime_hms);
		String date_today = SSH.executeCommand("date +%Y-%m-%d");
		String loglocation = "/opt/java7_DECE_batch/logs/java7_batch_process.log";
		String timecmd = "grep 'Starting the batch Process of STREAM_INACTIVE' " + loglocation +"|awk '{print $2}'";
		//SSH.executeCommand(timecmd);
		//String datecmd = "grep 'Starting the batch Process of STREAM_INACTIVE' " + loglocation +"|awk '{print $1}'";
		boolean teststatus = false;
		System.out.println(teststatus);
		for (int i =0; i < 6 ; i++){
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			teststatus = utils.verifyLogMessage(SSH, timecmd, l_time);
			System.out.println(teststatus);
			if (teststatus == true){
				System.out.println("\n\t --------------------------");
				System.out.println("\t|     Log message found     |");
				System.out.println("\t|  STREAM INACTIVE started  |");
				System.out.println("\t --------------------------\n");
				break;
			}
			else{
				continue;
			}
		}
		if (teststatus == false){
			Assert.fail("STREAM INACTIVE failed to start");
		}
	}
	
	@Test(enabled = true,priority =9)
	public void validateStreamInactiveStop_test09(){
		/*
		 * This will validate if STREAM INACTIVE has been started properly or not 
		 * by verifying log message
		 */
		
		System.out.print("\t TEST009 validate Stream Inactive stop\n\n");
		int l_time = utils.convertTimeToSecond(SSH, logtime_hms);
		String loglocation = "/opt/java7_DECE_batch/logs/java7_batch_process.log";
		String timecmd = "grep 'End of the batch Process - STREAM_INACTIVE' " + loglocation +"|awk '{print $2}'";
		//String datecmd = "grep 'Starting the batch Process of STREAM_INACTIVE' " + loglocation +"|awk '{print $1}'";
		boolean teststatus = false;
		for (int i =0; i < 60 ; i++){
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			teststatus = utils.verifyLogMessage(SSH, timecmd, l_time);
			if (teststatus == true){
				System.out.println("\n\t ----------------------------");
				System.out.println("\t|     Log message found      |");
				System.out.println("\t|  STREAM INACTIVE stopped   |");
				System.out.println("\t ----------------------------\n");
				teststatus = true;
				break;
			}
		}
		if (teststatus == false){
			Assert.fail("STREAM INACTIVE stopped");
		}
	}
	
	@Test(enabled = true,priority=10)
	public void validateStreamDataIsUpdated_test10(){
		/*
		 * This test will verify if STREAM HANDLE is updated or not
		 */
		
		System.out.print("\t TEST010 validate table stream daye is updated\n\n");
		String[] streamData = new String[3];
		Connection Conn = dbconn.getConnection();  //connect to DB
		String timestamp = dbconn.getSystemTimeStamp(Conn);
		timestamp = timestamp.split(" ")[0];
		System.out.println(timestamp);
		for (int i =0; i < stream.length; i++){
			System.out.println(stream[i]);
			Connection Con = dbconn.getConnection();
			streamData = dbconn.getStreamData(Con, stream[i]);
			System.out.println(streamData[0]);
			System.out.println(streamData[1]);
			System.out.println(streamData[2]);
			System.out.println("\n````````````````````````````````````````````````````\n");
			System.out.println("STREAM NAME : " + stream[i]);
			if (streamData[0].equals("deleted")){
				System.out.println("STATUS set as delete for " + stream[i]);
				String closedate = streamData[1].split(" ")[0];
				if (closedate.equals(timestamp)){
					System.out.println("CLOSED_DATE set as delete for " + stream[i]);
					if (streamData[2].equals("urn:dece:org:org:dece:1")){
						System.out.println("CLOSED_BY_ORG_ID set to urn:dece:org:org:dece:1 for" + stream[i]);
					}
					else{
						System.out.println("CLOSED_BY_ORG_ID not set to urn:dece:org:org:dece:1 for" + stream[i]);
						Assert.fail("CLOSED_BY_ORG_ID not set properly in STREAM_DATA");
					}
				}
				else{
						System.out.println("CLOSED_DATE not set to " + timestamp + " for" + stream[i]);
						Assert.fail("CLOSED_DATE not set properly in STREAM_DATA");
					}
				}
			else{
				System.out.println("STATUS not set to DELETED for " + stream[i]);
				Assert.fail("STATUS not set properly in STREAM_DATA");
			}
		}
	}
	
	@AfterSuite
	public void SuiteLevelTeardown(){
		System.out.print("\t TEST Suite Teardown");
		batchKW.restoreOriginalBatch(SSH);
		//batchKW.removeBackupBatch(SSH);
		SSH.sshDisconnect();
	}

}
