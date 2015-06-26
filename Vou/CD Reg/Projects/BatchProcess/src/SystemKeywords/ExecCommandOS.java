package SystemKeywords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
 
public class ExecCommandOS {
 
	/*public static void main(String CMD) {
 
		ExecuteCommandOS obj = new ExecuteCommandOS();
 
		//String domainName = "google.com";
 
		//in mac oxs
		//String command = "ping -c 3 " + domainName;
 
		//in windows
		//String command = "ping -n 3 " + domainName;
		
		String command = CMD;
 
		String output = obj.executeCommand(command);
 
		System.out.println(output);
 
	}*/
 
	public String executeCommandWindows(String command) {
 
		StringBuffer output = new StringBuffer();
 
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));
 
                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 
		return output.toString();
 
	}
	
	public int runBatchFile(String file){
		int exitVal = 1;
		String cmdsoapui = "cmd /c start /wait " + file;
		try {
			final Process process = Runtime.getRuntime().exec(cmdsoapui);
			try {
				exitVal = process.waitFor();
				//System.out.println("\t ~~~ .bat file execution finished : " + exitVal + "  ~~~");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exitVal;
	}
	
	/*public void runBatchFile(final String batchpath) throws Exception {
		// The batch file to execute
		final File batchFile = new File(batchpath);

		// The output file. All activity is written to this file
		final File outputFile = new File(String.format(".\\SoapUI\\Output\\bat\\Batch_Stream_%tY%<tm%<td_%<tH%<tM%<tS.txt",
				System.currentTimeMillis()));

		// Create the process
		final ProcessBuilder processBuilder = new ProcessBuilder(batchFile.getAbsolutePath());
		// Redirect any output (including error) to a file. This avoids deadlocks
		// when the buffers get full. 
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectOutput(outputFile);

		// Add a new environment variable
		processBuilder.environment().put("message", "Example of process builder");

		// Set the working directory. The batch file will run as if you are in this
		// directory.
		processBuilder.directory(new File("./"));

		// Start the process and wait for it to finish. 
		final Process process = processBuilder.start();
		final int exitStatus = process.waitFor();
		System.out.println("Processed finished with status: " + exitStatus);
  }*/
 
}