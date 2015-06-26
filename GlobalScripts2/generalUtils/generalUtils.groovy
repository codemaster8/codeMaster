package generalUtils; 

import org.apache.commons.lang.RandomStringUtils;
import java.text.SimpleDateFormat;
import java.util.UUID;
 
class generalUtils {
	def log;
	def targetSystem;
	private static String validChars ="ABCDEF123456789"
	private int IDlength=32
	private static String alphanumerics ="ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz"
	private static String numerics ="1234567890"
	private static String alphas ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
	private static String mixed ="ABCDEFG!@%^()*:;,./|[]HIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"


	generalUtils(log) {
		this.log = log;
	}
	
	static randomHexString(int strSize) {
		String resultStr = "";

		int maxIndex = this.validChars.length()

		java.util.Random rnd = new java.util.Random(System.currentTimeMillis()*(new java.util.Random().nextInt()))
		strSize.times {
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);
			resultStr += validChars.charAt(rndPos);
		}
		return resultStr;
	} 
	
	String randomAlphaNumericString(int size) {
		String resultStr = "";

		int maxIndex = this.alphanumerics.length()

		java.util.Random rnd = new java.util.Random(System.currentTimeMillis()*(new java.util.Random().nextInt()))
		size.times {
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);
			resultStr += this.alphanumerics.charAt(rndPos);
		}
		return resultStr;
	}
	
	String randomMixedCharacterString(int size) {
		String resultStr = "";

		int maxIndex = this.mixed.length()

		java.util.Random rnd = new java.util.Random(System.currentTimeMillis()*(new java.util.Random().nextInt()))
		size.times {
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);
			resultStr += this.mixed.charAt(rndPos);
		}
		return resultStr;
	}
	
	
	
	String randomNumericString(int size) {
		String resultStr = "";

		int maxIndex = this.numerics.length()

		java.util.Random rnd = new java.util.Random(System.currentTimeMillis()*(new java.util.Random().nextInt()))
		size.times {
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);
			resultStr += this.numerics.charAt(rndPos);
		}
		return resultStr;
	}
	
	String randomAlphaString(int size) {
		String resultStr = "";

		int maxIndex = this.alphas.length()

		java.util.Random rnd = new java.util.Random(System.currentTimeMillis()*(new java.util.Random().nextInt()))
		size.times {
			int rndPos = Math.abs(rnd.nextInt() % maxIndex);
			resultStr += this.alphas.charAt(rndPos);
		}
		return resultStr;
	}
	
	
	int randomNumber(int minRange = 1, int maxRange = 100) {
		Random generator = new java.util.Random();
		return generator.nextInt(maxRange) + minRange;
	}
	
	String randomUUID() { 
		return  UUID.randomUUID();
	}

	def dateDiff(String dateOne, String dateTwo, String unit = "hours") {
		//  Returns the difference between two dates in
		//  seconds.
		Date inputDateOne = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateOne);
		Date inputDateTwo = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateTwo);
		
		long m = inputDateTwo.getTime() - inputDateOne.getTime();

		this.log.info "m[$m]";
		this.log.info "dateDiff - dateOne[$dateOne] - dateTwo[$dateTwo]";

		if (m > 0) {
			switch (unit) {
				case "second":
				case "seconds":
					return (int) m/1000;
					break;
				case "minutes":
				case "minute":
					return (int) m/(1000 * 60);
					break;
				case "day":
				case "days":
					return (int) m/(1000 * 60 * 60) / 24;
					break;
				case "hour":
				case "hours":
				case "default":
					return (int) m/(1000 * 60 * 60);
					break;
			}
		} else {
			return m;
		}
	}
	
	def String randomizeStringCase(String inputString)
	{
        String outputString = ""; 
        try
        {  
            Random randomIntGenerator = new java.util.Random();         
            for (int i = 0; i < inputString.length(); i++)
            { 
                int randomInt = randomIntGenerator.nextInt(100) + 1;
                if (randomInt%2 != 0)
                {
                    outputString = outputString + inputString[i].toUpperCase();
                }
                else
                {
                    outputString = outputString + inputString[i].toLowerCase();
                }
            } 
            if (this.log)
    		{
    			this.log.info "The Output String with Randomized Case for your Input String is '" + outputString + "'.";
    		}
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
            if (this.log)
    		{
    			this.log.info "Err! Returning Empty String.";
    		}
        }         
		
        return outputString;              
	}
}
