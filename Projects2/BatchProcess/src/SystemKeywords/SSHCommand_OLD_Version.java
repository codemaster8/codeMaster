package SystemKeywords;

import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHCommand_OLD_Version {
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
  
  public void sshConnect(){
	  
	  java.util.Properties config = new java.util.Properties(); 
      config.put("StrictHostKeyChecking", "no");
      JSch jsch = new JSch();
      Session session = null;
	  try {
		  session = jsch.getSession(username, hostname, 22);
		  session.setPassword(password);
		  session.setConfig(config);
		  session.connect();
		  System.out.println("Connected");
		  //return session;
	  } catch (JSchException e) {
	      e.printStackTrace();
	  } 
	  setSession(session);
  }
  
  public String executeCommand(){
	  String output = null;
	  
	  try{
		  Channel channel=getSession().openChannel("exec");
		  setChannel(channel);
		  ((ChannelExec)channel).setCommand(getCMD());
	      channel.setInputStream(null);
	      ((ChannelExec)channel).setErrStream(System.err);
	      
	      InputStream in=channel.getInputStream();
	      channel.connect();
	      //byte[] tmp; 
	      byte[] tmp=new byte[1024];
	      while(true){
	        while(in.available()>0){
	        	int i=in.read(tmp, 0, 1024);
	        	output = new String(tmp, 0, i);
	        	if(i<0)break;
	        	//output = new String(tmp, 0, i);
	        	//System.out.print(new String(tmp, 0, i));
	          output = new  String(tmp, 0, i);
	        }
	        if(channel.isClosed()){
	          //System.out.println("exit-status: "+channel.getExitStatus());
	          channel.disconnect();
	          break;
	        }
	        try{Thread.sleep(2000);}catch(Exception ee){}
	      }
	  	}
	  catch(Exception e){
          e.printStackTrace();
      }
	  return output;
  }
  
  public void sshDisconnect(){
      getChannel().disconnect();
	  getSession().disconnect();
      System.out.println("Disconnected");
  }
  
  

}
