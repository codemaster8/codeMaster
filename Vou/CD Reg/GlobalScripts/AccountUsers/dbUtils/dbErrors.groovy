package dbUtils;
/**
 * Provides Utilities to Interact with ERRORs Data.
 * 
 * @author Pat Gentry
 * @version 1.0
 *
 *
*/

/*    
-----------------------------------------------------------
  C H A N G E     L O G
  Version
    Change   Person

  1.0
  ---
   * 2/23/11:  Created class. pjg
-----------------------------------------------------------
*/
import groovy.sql.Sql;
import java.util.regex.Matcher
import java.util.regex.Pattern

class dbErrors extends dbUtils 
{
	def sql = null;
	def log = null;

	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbErrors<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbErrors(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	}

	/*
	*   Private methods
	*/
	

	/*
	*  GET Methods
	*
	*/
	/**
	* Extract the NODE_ROLE.ROLE value associated with the NODE_OID 
	* supplied. 
	*
	* @param oid The NODE.NODE_OID to extract. 
	* @return The node's role as a String. 
	* @version 1.0
	*/
	def errGetIdInfo(String errid, String language = null) {
		String xmlData = "";
		
		if (language == null) {
			language = "en";
		}

		this.sql = "SELECT a.STATUS_CODE, b.REASON, b.ERROR_ID FROM ERROR_ID a, ERROR_INFO b " + 
			"WHERE a.ERROR_ID = b.ERROR_ID and a.ERROR_ID='" + errid + "' and b.LANGUAGE_ID = '" + 
			language + "'";
			

		xmlData = this.SelectFromDB(this.sql);
		
		try	{	
			if(xmlData.length() > 0) {	
				if (this.log) 
				{
					this.log.info "errGetIdInfo: sql is " + this.sql;
					this.log.info "errGetIdInfo: xmlData.length() is " + xmlData.length();
					this.log.info "errGetIdInfo: xmlData is $xmlData";
				}			
			}		 
		}
		catch(Throwable e) {
			this.log.info e;
		}
		finally {
			return xmlData;
		}
	}
}