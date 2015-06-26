package generalUtils; 

import org.apache.commons.lang.RandomStringUtils;
import java.text.SimpleDateFormat;
import java.util.UUID;
import au.com.bytecode.opencsv.CSVReader;

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
		this.log.info("GeneralUtil Object initialized");
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

	def getPrecedenceForGivenRatingAndCountry(country, rating, context){
		def nextLine;
		def precedence;
		CSVReader reader = getCommonTestDataRatingCSVFileRef(context);
		while ((nextLine = reader.readNext()) != null) 
		{
			if(nextLine[0] == country){
				if(nextLine[1]== rating){
					precedence = nextLine[2];
				}
			}	
		}
		if(precedence == null){
			String errorMessage = "Precedence for country $country and rating $rating not found";
			throw new Exception(errorMessage);
		}else{	
			this.log.info("Precedence for country $country and rating $rating is $precedence");
		}
		if(reader !=null)
			reader.close();

		return precedence;
	}

	def getAllRatingsForAGivenCountry(country, rating, ratingSystem, context){
		int precedence = Integer.parseInt(getPrecedenceForGivenRatingAndCountry(country, rating, context).toString());
		return getAllRatingsForGivenCountryBasedOnPrecedence(country, precedence, ratingSystem, context);
	}

	def getAllRatingsForGivenCountryBasedOnPrecedence(country, precedence, ratingSystem, context){
		def ratingInXmlFormat = null;
		def nextLine;

		CSVReader reader = getCommonTestDataRatingCSVFileRef(context);
		while ((nextLine = reader.readNext()) != null) 
		{
			//this.log.info("Current country '" + nextLine[0] + "'");
			if(nextLine[0] == country){
				//this.log.info("Current precedence '" + nextLine[2] + "'");
				if(Integer.parseInt(nextLine[2].toString()) <= precedence){
					this.log.info("Current rating system '" + nextLine[3] + "'");
					if(nextLine[3] == ratingSystem){
						this.log.info("Append current in our output rating xml");
						if(ratingInXmlFormat == null){
							ratingInXmlFormat = '<dece:Resource>urn:dece:type:rating:'+country+':'+nextLine[3]+':'+nextLine[1]+'</dece:Resource>\n'
						}else{
							ratingInXmlFormat = ratingInXmlFormat + '<dece:Resource>urn:dece:type:rating:'+country+':'+nextLine[3]+':'+nextLine[1]+'</dece:Resource>\n'
						}
					}
				}	
			}
		}

		if(reader !=null)
			reader.close();

		return ratingInXmlFormat;
	}

	def getCommonTestDataRatingCSVFileRef(context){
		def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context) 
		def projectPath = groovyUtils.projectPath
		log.info("Project Path : '" + projectPath + "'");
		
		if (System.properties['os.name'].toLowerCase().contains('windows')) 
		{
    		log.info "it's Windows OS!!";
    		File temp = new File(projectPath + "\\..\\..\\..\\CommonTestData\\Ratings.csv");
    		String commonTestDataFilePath = temp.getCanonicalPath();
			CSVReader reader = new CSVReader(new FileReader(commonTestDataFilePath));
			this.log.info("Csv TestData file object initialized '" + commonTestDataFilePath + "'");
			return reader;
		} 
		else 
		{
    		log.info "it's Linux OS!!";
			File temp = new File(projectPath + "/../../../CommonTestData/Ratings.csv");
    		String commonTestDataFilePath = temp.getCanonicalPath();
			CSVReader reader = new CSVReader(new FileReader(commonTestDataFilePath));
			this.log.info("Csv TestData file object initialized '" + commonTestDataFilePath + "'");
			return reader;
		}
	}
}
