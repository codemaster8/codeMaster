package dbUtils;

import groovy.sql.Sql;
import java.util.regex.Matcher
import java.util.regex.Pattern

class dbErrors extends dbUtils 
{
	private log = null;
	private dbIdentifier = null;
	
	dbErrors(envIdentifier) 
	{
        super(envIdentifier);			
		this.dbIdentifier = envIdentifier;
	}	
	dbErrors(log, envIdentifier) 
	{
        super(log, envIdentifier);
		this.log = log;		
		this.dbIdentifier = envIdentifier;
	}
  
	def errGetIdInfo(String errid, String language = null) 
    {
		String xmlData = "";
		String sqlStr = "";
		
		if (language == null) 
        {
			language = "en";
		}

		sqlStr = "SELECT a.STATUS_CODE, b.REASON, b.ERROR_ID FROM ERROR_ID a, ERROR_INFO b, QA_ERROR_INFO c " + 
			"WHERE a.ERROR_ID = b.ERROR_ID and b.ERROR_ID = c.ERROR_ID and c.QA_ERROR_ID='" + errid + "' and b.LANGUAGE_ID = '" + 
			language + "'";
			
	         
		  this.log.info("Executing SQl: '" + sqlStr + "'");
 
		xmlData = selectFromDB(sqlStr);
		
		try	
        {	
			if(xmlData.length() > 0) 
            {	
				if (this.log) 
				{
					this.log.info "errGetIdInfo: sql is " + sqlStr;
					this.log.info "errGetIdInfo: xmlData.length() is " + xmlData.length();
					this.log.info "errGetIdInfo: xmlData is $xmlData";
				}			
			}		 
		}
		catch(Throwable e) 
        {
			this.log.info e;
		}
		finally 
        {
			return xmlData;
		}
	}
    def dbConnClose() 
    { 
		super.dbConnClose();
	}
}