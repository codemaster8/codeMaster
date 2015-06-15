package dateTimeUtils; 

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
 
class dateTimeUtils 
{
	def log;
	def pastDate;
	def currDate;
	
	dateTimeUtils(log) 
	{			
		this.log = log;
	}

	dateTimeUtils(oldDate, newDate) {
		// We do milliseconds.  If we encounter milliseconds 
		// remove them.                                      
		//
		pastDate = oldDate.replaceAll(/\.[0-9][0-9][0-9]/, "") 
		currDate = newDate.replaceAll(/\.[0-9][0-9][0-9]/, "") 
		
	}
	dateTimeUtils(oldDate, newDate, log) {
		// We do milliseconds.  If we encounter milliseconds 
		// remove them.                                      
		//
		pastDate = oldDate.replaceAll(/\.[0-9][0-9][0-9]/, "") 
		currDate = newDate.replaceAll(/\.[0-9][0-9][0-9]/, "") 
		
		this.log = log;
	}

	def diffHours() {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		def beforeDt = Date.parse("yyyy-MM-dd hh:mm:ss", pastDate);
		def afterDt  = Date.parse("yyyy-MM-dd hh:mm:ss", currDate);

		def tsBefore = beforeDt.toTimestamp().getTime();
		def tsAfter  = afterDt.toTimestamp().getTime();

		def differHours = (tsBefore - tsAfter) / (60 * 60 * 1000);
	}
	
	def getCurrentTimestampXML()
	{
        def currentTimestamp = new Date();
        def currentTimestampXML = currentTimestamp.format("yyyy-MM-dd") + "T" + currentTimestamp.format("hh:mm:ss");
        if(this.log)
        {
            this.log.info("Current Timestamp in XML format is '" + currentTimestampXML + "'.");
        }				
		return currentTimestampXML;
	}
}
