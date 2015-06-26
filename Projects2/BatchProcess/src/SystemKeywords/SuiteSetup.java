package SystemKeywords;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

public class SuiteSetup {
	/*
	 *This function will setup suite level variables
	 * 
	 */
	//private static SSHFunctions SuiteSSH = new SSHFunctions();
	//private static Session session = SuiteSetup.SuiteSSH.sshConnect("deceapp", "Kn0od!e", "stdecqavot6.va.neustar.com");
	//private static Session session = null;
	private String hostname;
	private String username;
	private String password;
	private Session session;
	private Channel channel;
	private String CMD;
	
	public String getHostname(){
		return hostname;
	}
	public void setHostname(String hostname){
		this.hostname = hostname;
	}
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public String getPassword(){
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public Session getSession(){
		return session;
	}
	public void setSession(Session session){
		this.session = session;
	}
	public Channel getChannel(){
		return channel;
	}
	public void setChannel(Channel channel){
		this.channel = channel;
	}
	public String getCMD(){
		return CMD;
	}
	public void setCMD(String CMD){
		this.CMD = CMD;
	}
}
