package SystemKeywords;

import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import SSH.*;

public class SSHCommand{
	
	private String hostname;
	private String username;
	private String password;
	//private Session session;
	//private Channel channel;
	//private String CMD;
	
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
	/*public Session getSession(){
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
	}*/
	
	public static SSHExec ssh;
	
	public void sshConnect(){
		//SSHExec ssh = null;
		try {
			//SSHExec.setOption(IOptionName.INTEVAL_TIME_BETWEEN_TASKS, "1000");
			//SSHExec.setOption(IOptionName.TIMEOUT, "36000");
			SSHExec.showEnvConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ConnBean cb = new ConnBean(hostname, username, password);
		ssh = SSHExec.getInstance(cb);
		ssh.connect();
	}
	
	public String executeCommand(String command){
		Result output = null;
		try{
			CustomTask task = new ExecCommand(command);
			output = ssh.exec(task);
			
		}catch (TaskExecFailException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			}
		//Result output;
		//System.out.println(output.error_msg);
		return output.sysout;
	}
	
	public void sshDisconnect(){
		ssh.disconnect();	
	}
	
	public void executeScript(String script){ 
		   try 
		   {
		      JSch jsch = new JSch();
		      String host = null;

		      host = this.username + "@" + this.hostname;
		      String user = host.substring(0, host.indexOf('@'));
		      host = host.substring(host.indexOf('@') + 1);

		      Session session = jsch.getSession(user, host, 22);
		      session.setConfig("StrictHostKeyChecking", "no");
		      session.setPassword(this.password);
		      session.connect();

		      String command=script;

		      com.jcraft.jsch.Channel channel = session.openChannel("exec");
		      ((ChannelExec) channel).setCommand(command);
		      ((ChannelExec) channel).setPty(true);

		      InputStream in = channel.getInputStream();
		      OutputStream out = channel.getOutputStream();
		      ((ChannelExec) channel).setErrStream(System.err);
		      channel.connect();

		      byte[] tmp = new byte[1024];
		      while (true) 
		      {
		            while (in.available() > 0) {
		            int i = in.read(tmp, 0, 1024);
		            if (i < 0)
		                  break;
		            System.out.print(new String(tmp, 0, i));
		            }
		            if (channel.isClosed()) 
		            {
		                  //System.out.println("exit-status: " + channel.getExitStatus());
		                  break;
		            }
		            try {
		                  Thread.sleep(1000);
		            } 
		            catch (Exception ee) 
		            {
		            }
		      }
		      channel.disconnect();
		      session.disconnect();
		   } 
		   catch (Exception e) {
		      System.out.println(e);
		   }
		}

}
	
	
	/*public static void main(String[] args) {
		SSHExec ssh = null;
		try {
			SSHExec.setOption(IOptionName.HALT_ON_FAILURE, true);
			SSHExec.setOption(IOptionName.SSH_PORT_NUMBER, 22);
			SSHExec.setOption(IOptionName.ERROR_MSG_BUFFER_TEMP_FILE_PATH, "c:\\123.err");
			SSHExec.setOption(IOptionName.INTEVAL_TIME_BETWEEN_TASKS, "1000");
			SSHExec.setOption(IOptionName.TIMEOUT, "36000");
			SSHExec.showEnvConfig();
			
			ConnBean cb = new ConnBean("stdecqavot6.va.neustar.com", "deceapp","Kn0od!e");
			ssh = SSHExec.getInstance(cb);		
			
			CustomTask task1 = new ExecCommand("echo 123");
			CustomTask task2 = new ExecCommand("saklasa");
			CustomTask task3 = new ExecCommand("pwd");
			ssh.connect();
			ssh.exec(task1);
			ssh.exec(task2);
			System.out.println("This should not print!");
			ssh.exec(task3);
			System.out.println("Task3 does not execute");
		} catch (TaskExecFailException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			ssh.disconnect();	
		}
	}

}*/