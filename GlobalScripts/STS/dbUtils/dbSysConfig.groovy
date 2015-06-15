package dbUtils;  

/**
 * Provides Utilities to Interact with SYS_CONFIG table.
 * 
 * @author Pat Gentry
 * @version 1.0
 *
 *
*/
/*
----------------------------------------------------
 C H A N G E     L O G
 Version
    Date: Change   Person

 1.0
 ---
  1/13/11: Updated all methods adding finally() block and 
	   removed dbConnClose and return from all
           catch() blocks.   PJG
  1/18/11: Added * @version 1.0 to APIs.  pjg
  1/20/11: added super(log) to (log) constructor. pjg
  2/09/11: Removed references to dbIdentifier and verified dbConnClose()
----------------------------------------------------
*/

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbSysConfig extends dbUtils 
{
	def log = null;
	def sql = null;
	def streamHardLimit = null;

	/*
	*  Constructors
	*/

	/**
	*  Creates a new <code>dbSysConf<code> object with
	*  ability to write to the log.
	*/
	dbSysConfig(log) {
		super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbSysConfig<code> object.
	*/
	dbSysConfig() {
	}
	/**
	*  Creates a new <code>dbSysConfig<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbSysConfig(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	}

	/*
	*  Private Methods
	*/
	/**
	* Private method to execute UPDATE SQL. 
	* @version 1.0
	*/
	private dbDoUpdate() {
		int rc = 0;

		if (!this.sql.isEmpty()) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(!rc.toString().isEmpty())
				{
					if (this.log) 
					{
						this.log.info "dbDoUpdate: rc is "  + rc;
						this.log.info "dbDoUpdate: sql is " + this.sql;
					}
				}
			}
			catch(Throwable e)
			{
				this.log.info e;
			} 
			finally 
			{
				this.dbConnClose();
				return(rc);
			}
		} else {
			return(rc);
		}
	}



	/*
	*  Get Methods
	*/

	/**
	* Extract the SYS_CONFIG row associated with the NAME 
	* supplied via the name argument. 
	* @version 1.0
	*
	* @param name The SYS_CONFIG.NAME value to extract. 
	* @return The XML-encapsulated SYS_CONFIG row for the NAME provided.
	*/
	def sysconfGetRow(String name) {
		String xmlData = "";

		this.sql = "select " +
		"NAME, " +
		"CLASS, " +
		"TYPE, " +
		"VALUE, " +
		"CREATED_DATE, " +
		"UPDATED_DATE " +
		"from SYS_CONFIG where NAME = '" + name + "'";
		
		try
		{	
			xmlData = this.SelectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "sysconGetRow: sql is " + this.sql;
					this.log.info "sysconGetRow: xmlData.length() is " + xmlData.length();
					this.log.info "sysconGetRow: xmlData is $xmlData";
				}			
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return xmlData;
		}
	}
	/**
	* Extract the SYS_CONFIG.VALUE value associated with the NAME 
	* supplied via the name argument. 
	* @version 1.0
	*
	* @param name The SYS_CONFIG.NAME whos VALUE to extract. 
	* @return A String contain the NAME's associated VALUE. 
	*/
	def sysconfGetValue(String name) {
		String value = "";

		this.sql = "select VALUE from SYS_CONFIG where " +
			"NAME = '" + name + "'"; 

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					value = rowData[0].VALUE;
					if (this.log) 
					{
						this.log.info "sysconfGetValue: sql is " + this.sql;
						this.log.info "sysconfGetValue: rowData.size is " + rowData.size;
						this.log.info "sysconfGetValue: rowData[0] is " + rowData[0];
						this.log.info "sysconfGetValue: value is " + value; 
					}			
				}
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return value;
		}
	}
	/**
	* Extract the SYS_CONFIG.TYPE value associated with the NAME 
	* supplied via the name argument. 
	* @version 1.0
	*
	* @param name The SYS_CONFIG.NAME whos VALUE to extract. 
	* @return A String contain the NAME's associated SYS_CONFIG.TYPE. 
	*/
	def sysconfGetType(String name) {
		String type = "";

		this.sql = "select TYPE from SYS_CONFIG where " +
			"NAME = '" + name + "'"; 

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					type = rowData[0].TYPE;
					if (this.log) 
					{
						this.log.info "sysconfGetType: sql is " + this.sql;
						this.log.info "sysconfGetType: rowData.size is " + rowData.size;
						this.log.info "sysconfGetType: rowData[0] is " + rowData[0];
						this.log.info "sysconfGetType: type is " + type; 
					}			
				}
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return type;
		}
	}

	/*
	*  Set Methods
	*/

	/**
	* Update the SYS_CONFIG.NAME row associated with the NAME 
	* with the value supplied via value.  
	* @version 1.0
	*
	* @param name The SYS_CONFIG.NAME to update. 
	* @param value The SYS_CONFIG.VALUE to set. 
	* @return Number of rows updated.  Should be one. 
	*/
	def sysconfSetValue(String name, String value) {
		int rc = 0;	

		this.sql = "update SYS_CONFIG set " +
		"VALUE = '" + value + "' where NAME = '" + name + "'"; 

		try
		{	
			rc = dbDoUpdate();
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			dbConnClose();
			return rc; 
		}
	}
	/**
	* Update the SYS_CONFIG.NAME row associated with the NAME 
	* with the value supplied via value.  
	* @version 1.0
	*
	* @param name The SYS_CONFIG.NAME to update. 
	* @param value The SYS_CONFIG.VALUE to set. 
	* @return Number of rows updated.  Should be one. 
	*/
	def sysconfSetValue(String name, int value) {
		int rc = 0;	

		this.sql = "update SYS_CONFIG set " +
		"VALUE = '" + value + "' where NAME = '" + name + "'"; 

		try
		{	
			rc = dbDoUpdate();
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			dbConnClose();
			return rc; 
		}
	}
	/**
	* Update the SYS_CONFIG.NAME row associated with the NAME 
	* with the value supplied via value.  
	* @version 1.0
	*
	* @param name The SYS_CONFIG.NAME to update. 
	* @param value The SYS_CONFIG.VALUE to set. 
	* @return Number of rows updated.  Should be one. 
	*/
	def sysconfSetValue(String name, boolean value) {
		int rc = 0;	
		
		this.sql = "update SYS_CONFIG set " +
		"VALUE = '" + value + "' where NAME = '" + name + "'"; 

		try
		{	
			rc = dbDoUpdate();
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			dbConnClose();
			return rc; 
		}
	}
}

