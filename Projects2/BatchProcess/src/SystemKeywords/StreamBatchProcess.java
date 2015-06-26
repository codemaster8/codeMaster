package SystemKeywords;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.testng.Assert;

public class StreamBatchProcess {
	private static String filename = "SteamInactive_Batch.txt";
	ExecCommandOS ExeCmdWindow = new ExecCommandOS();
	
	public void createStream(){
		
		try {
			//Clear file before creating stream
			PrintWriter writer = new PrintWriter(filename);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String cmdsoapui = "StreamCreatTest.bat";
		ExeCmdWindow.runBatchFile(cmdsoapui);
		System.out.println("\tStream Creation DONE ");
	}
	
	public String[] getStreamCreate() throws IOException{
		FileReader fr;
		
		fr = new FileReader(filename);
		BufferedReader textReader = new BufferedReader(fr);
			
		int numberOfLines = 8;
		String[] streamData = new String[numberOfLines];
			
		for (int i=0; i < numberOfLines; i++) {
				streamData[ i ] = textReader.readLine();
				if (streamData[i] ==null){
					Assert.fail("Stream Data not created");
				}
				System.out.println(streamData[ i ]);
		}
		
		textReader.close();
		return streamData;
	}
	
}
