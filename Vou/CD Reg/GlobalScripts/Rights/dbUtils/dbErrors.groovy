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
	private sqlStr = "";
	private log;

	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbErrors<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbErrors(log, String dbIdentifier) 
	{
		super(log, dbIdentifier);
		this.log = log;
	}

	/*
	*   Private methods
	*/
	

	/*
	*  GET Methods
	*/
	/**
	* Extract the NODE_ROLE.ROLE value associated with the NODE_OID 
	* supplied. 
	*
	* @param oid The NODE.NODE_OID to extract. 
	* @return The node's role as a String. 
	* @version 1.0
	*/
	def errGetIdInfo(String errID, String acctLanguage = null) 
	{
		String xmlData = "";
		
		if(acctLanguage == null) 
		{
			acctLanguage = "en";
		}
		
		this.sqlStr = "SELECT " +
				"eid.error_id, " +
				"einfo.REASON, " +
				"eid.STATUS_CODE, " +
				"einfo.language_id " +
				"FROM " +
				"ERROR_ID eid, " +
				"ERROR_INFO einfo, " +
				"language_ref lref " +
				"WHERE " +
				"eid.ERROR_ID = einfo.ERROR_ID " +
				"and " +
				"einfo.language_id = lref.language_id " +
				"and " +
				"eid.ERROR_ID = (select error_id from qa_error_info where qa_error_id = '" + errID + "')" +
				"and " +
				"einfo.LANGUAGE_ID = '" + acctLanguage + "'";	
		
		try	
		{
			xmlData = SelectFromDB(this.sqlStr);
			if(xmlData.length() > 0) 
			{	
				if (this.log) 
				{
					this.log.info "errGetIdInfo: sql is '" + this.sqlStr + "'";
					this.log.info "errGetIdInfo: xmlData.length() is '" + xmlData.length() + "'";
					this.log.info "errGetIdInfo: xmlData is '$xmlData'";
				}			
			}
			else
			{
				if (this.log)
				{
					this.log.info "errGetIdInfo: sql is '" + this.sqlStr + "'";
					this.log.info "Returning empty string for XML Result."
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
}