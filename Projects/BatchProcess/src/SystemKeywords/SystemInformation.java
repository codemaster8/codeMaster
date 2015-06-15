package SystemKeywords;

public class SystemInformation {
	//SuiteSetup systemSetup = new SuiteSetup();
	//SSHCommand systemSSH = new SSHCommand();

	public String systemHostname(SSHCommand SSH){
		String cmd = "hostname";
		String hostname = SSH.executeCommand(cmd);
		System.out.println("HOSTNAME   :   "+hostname.trim());
		return hostname;
	}

	public String systemPlatform(SSHCommand SSH){
		//systemSSH.setCMD("uname");
		String platform = SSH.executeCommand("uname");
		System.out.println("PLATFORM   :   "+platform.trim());
		return platform.trim();
	}
	
}
