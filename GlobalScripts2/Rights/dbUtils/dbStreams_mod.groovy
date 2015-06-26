package dbUtils; 
/**
 * Provides Utilities to Interact with Stream Data.
 * 
 * @author Pat Gentry
 * @version 1.0
 *
*/

/*    
-----------------------------------------------------------
  C H A N G E     L O G
  Version
    Change   Person

  1.0
  ---
   	1/11/11: Establishment of class.  PJG 
	1/13/11: Added javadoc comments.  PJG
	1/25/11: Removed all $ variable expansions from SQL.  pjg
	2/4/11:  Added method streamDiffCDateandTOD() pjg
	2/9/11:  Modification to all classes to remove dbIdentifier. pjg
-----------------------------------------------------------
*/


import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbStreams extends dbUtils 
{
	def sql = null;
	def streamHandle = null;
	def log = null;
	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbStream<code> object.
	*/
	dbStreams() {
	    super();   
	}
	/**
	*  Creates a new <code>dbStream<code> object with
	*  ability to write to the log.
	*/
	dbStreams(log) {
	    super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbStream<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbStreams(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	} 

	/*
	*  Private Methods
	*/

	/**
	*   Private method for constructing SQL
	*/
	private dbSetStatusExpireSQL(String streamhandle, String timeStr) {
		this.sql = " update STREAM_DATA set EXPIRATION_DATE = %%TIME%% " + 
			" where STREAM_HANDLE_OID = '%%SH%%'";	
		this.sql = sql.replaceAll("%%SH%%",   streamhandle);
		this.sql = sql.replaceAll("%%TIME%%", timeStr);

		if (this.log) {
			this.log.info "SQL[" + this.sql + "]";
		}
	}

	/*
	*   GET Methods
	*/

	/**
	* Extract the STREAM_DATA row associated with the STREAM_HANDLE_OID 
	* supplied. 
	*
	* @param oid The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	* @return XML encapsulated STREAM_DATA row.
	*/
	def streamGetRow(String oid) {
		//  Returns the stream's row.
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(STREAM_HANDLE_OID)) STREAM_HANDLE_OID, " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"to_char(rawtohex(USER_OID)) USER_OID, " +
		"TRANSACTION_ID, " +
		"STATUS, " +
		"STREAM_CLIENT_NICKNAME, " +
		"EXPIRATION_DATE, " +
		"CLOSED_DATE, " +
		"CLOSED_BY_ORG_ID, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY)) CREATED_BY, " +
		"UPDATED_DATE, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY " +
		"from STREAM_DATA where STREAM_HANDLE_OID  = '" + oid + "'";

		xmlData = this.SelectFromDB(this.sql);
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "streamGetRow: sql is " + this.sql;
					this.log.info "streamGetRow: xmlData.length() is " + xmlData.length();
					this.log.info "streamGetRow: xmlData is $xmlData";
				}			
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			return xmlData;
		}
	}
	/**
	* Gets the STREAM_DATA.CREATED_DATE column associated with the STREAM_HANDLE_OID 
	* supplied. 
	*
	* @param oid The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	* @return String containing the STREAM_DATA.CREATED_DATE. 
	*/
	def streamGetCreatedDate(String streamHandle) { 
		String cd = "";
		this.sql = "select CREATED_DATE from STREAM_DATA where " + 
			"STREAM_HANDLE_OID = '" + streamHandle + "'"; 
		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					cd = rowData[0].CREATED_DATE;
					if (this.log) 
					{
						this.log.info "streamGetCreatedDate: sql is " + this.sql;
						this.log.info "streamGetCreatedDate: rowData.size is " + rowData.size;
						this.log.info "streamGetCreatedDate: rowData[0] is " + rowData[0];
						this.log.info "streamGetCreatedDate: cd is " + cd; 
					}			
				}
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			return cd;
		}
	}
	/**
	* Gets the STREAM_DATA.EXPIRATION_DATE column in  YYYY/MM/DD HH24:MI:SS
	* format for the STREAM_HANDLE_OID supplied in streamHandle.
	*
	* @param streamHandle The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	* @return String containing the STREAM_DATA.EXPIRATION_DATE. 
	*/
	def streamGetExpireDate(String streamHandle) { 
		String ed = "";

		this.sql = "SELECT TO_CHAR(EXPIRATION_DATE, 'YYYY/MM/DD HH24:MI:SS') AS TIME " +
			"FROM STREAM_DATA where STREAM_HANDLE_OID = '" + streamHandle + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					ed = rowData[0].TIME;
					if (this.log) 
					{
						this.log.info "streamGetExpireDate: sql is " + this.sql;
						this.log.info "streamGetExpireDate: rowData.size is " + rowData.size;
						this.log.info "streamGetExpireDate: rowData[0] is " + rowData[0];
						this.log.info "streamGetExpireDate: ed is " + ed; 
					}			
				}
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			return ed;
		}
	}

	/*
	*  STREAM Data Methods
	*/

	/**
	* Returns the number of hours difference between the 
	* STREAM_DATA.CREATED_DATE and the STREAM_DATA.EXPIRATION_DATE 
	* columns. 
	*
	* @param streamHandle The STREAM_DATA.STREAM_HANDLE_OID to target. 
	* @return Number of hours difference between the CREATED_DATE and EXPIRATION_DATE. 
	*/
	def streamHandleVerifyExpiration(String streamHandle) { 
		// Returns the number of hours difference between the 
		// stream creation date and the expiration date. 
	
		int rc = 0;	
		Float g = 0.0;
		String r = "";

		this.sql = "SELECT EXTRACT (DAY FROM (expiration_date - created_date))*06+ " + 
                    "EXTRACT (HOUR   FROM (expiration_date - created_date))+" +
                    "EXTRACT (MINUTE FROM (expiration_date - created_date))/60+" +
                    "EXTRACT (SECOND FROM (expiration_date - created_date))/3600 DELTA" +
    		    " from STREAM_DATA where STREAM_HANDLE_OID = '" + streamHandle + "'"; 

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					r = rowData[0];
					g = r.substring(r.lastIndexOf("=")+1, r.length() - 1).toFloat().round();
					if (this.log) 
					{
						this.log.info "streamHandleVerifyExpiration: g [$g]";
						this.log.info "streamHandleVerifyExpiration: rowData.size is " + rowData.size;
						this.log.info "streamHandleVerifyExpiration: rowData[0] is " + rowData[0];
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
			return g;
		}
	}
	/**
	* Returns the number of seconds difference between TOD and 
	* STREAM_DATA.CREATED_DATE. 
	*
	* @param streamHandle The STREAM_DATA.STREAM_HANDLE_OID to target. 
	* @return Number of seconds difference between the CREATED_DATE and TOD. 
	*/
	def streamDiffCDateandTOD(String streamHandle) { 
	
		int rc = 0;	
		Float g = 0.0;
		String r = "";

		this.sql = "SELECT (extract(DAY FROM sysdate - CREATED_DATE)*06*60*60)+ " + 
			"(extract(HOUR FROM sysdate - CREATED_DATE)*60*60)+" +
			"(extract(MINUTE FROM sysdate - CREATED_DATE)*60)+" +
			"extract(SECOND FROM sysdate - CREATED_DATE)" +
			"as sec FROM STREAM_DATA where STREAM_HANDLE_OID = '" + streamHandle + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					r = rowData[0];
					this.log.info "PATTY r is " + r;
					g = r.substring(r.lastIndexOf("=")+1, r.length() - 1).toFloat().round();
					if (this.log) 
					{
						this.log.info "streamDiffCDateandTOD: g [$g]";
						this.log.info "streamDiffCDateandTOD: rowData.size is " + rowData.size;
						this.log.info "streamDiffCDateandTOD: rowData[0] is " + rowData[0];
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
			return g;
		}
	}
	/**
	* Returns whether or not the stream supplied in streamHandle 
	* has a STREAM_DATA.STATUS value of 'active'. 
	*
	* @param streamHandle The STREAM_DATA.STREAM_HANDLE_OID to target. 
	* @return 1 if the Stream's STATUS is 'active', zero otherwise. 
	*/
	def streamIsActive(String streamHandle) { 
		int rc = 0;
		string cnt = "";

		this.sql = "select count(*) as CNT from STREAM_DATA where " + 
			"STREAM_HANDLE_OID = '" + streamHandle + "' and STATUS = 'active'"; 

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					if (rowData[0].CNT == 1) {
						rc = 1;
					}

					if (this.log) 
					{
						this.log.info "streamIsActive: sql is " + this.sql;
						this.log.info "streamIsActive: rowData.size is " + rowData.size;
						this.log.info "streamIsActive: rowData[0] is " + rowData[0];
						this.log.info "streamIsActive: rc is " + rc; 
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
			return rc; 
		}
	}
	/**
	* Returns whether or not the stream supplied in streamHandle 
	* has a STREAM_DATA.STATUS value of 'deleted'. 
	*
	* @param streamHandle The STREAM_DATA.STREAM_HANDLE_OID to target. 
	* @return 1 if the Stream's STATUS is 'deleted', zero otherwise. 
	*/
	def streamIsDeleted(String streamHandle) { 
		int rc = 0;
		string cnt = "";

		this.sql = "select count(*) as CNT from STREAM_DATA where " + 
			"STREAM_HANDLE_OID = '" + streamHandle + "' and STATUS = 'deleted'"; 

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					if (rowData[0].CNT == 1) {
						rc = 1;
					}

					if (this.log) 
					{
						this.log.info "streamIsDeleted: sql is " + this.sql;
						this.log.info "streamIsDeleted: rowData.size is " + rowData.size;
						this.log.info "streamIsDeleted: rowData[0] is " + rowData[0];
						this.log.info "streamIsDeleted: rc is " + rc; 
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
			return rc; 
		}
	}

	/**
	* Returns the number of STREAM_DATA_HISTORY rows that are  
	* associated to the account supplied via accountoid.
	*
	* @param accountoid The ACCOUNT_OID to target. 
	* @return Int of number of STREAM_DATA_HISTORY rows associated with the ACCOUNT_OID supplied
	*  
	*/
	def streamGetStreamDataHistoryRowCount(String accountoid) { 
		String cnt = "";

		this.sql = "select count(*) as CNT from STREAM_DATA_HISTORY where ACCOUNT_OID = '" + accountoid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					cnt = rowData[0].CNT;

					if (this.log) 
					{
						this.log.info "streamGetStreamDataHistoryRowCount: sql is " + this.sql;
						this.log.info "streamGetStreamDataHistoryRowCount: rowData.size is " + rowData.size;
						this.log.info "streamGetStreamDataHistoryRowCount: rowData[0] is " + rowData[0];
						this.log.info "streamGetStreamDataHistoryRowCount: cnt is " + cnt; 
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
			return cnt.toInteger(); 
		}
	}
	/**
	* Returns the number of STREAM_DATA rows that are  
	* associated to the account supplied via accountoid.
	*
	* @param accountoid The ACCOUNT_OID to target. 
	* @return Int of number of STREAM_DATA rows associated with the ACCOUNT_OID supplied
	*  
	*/
	def streamGetStreamDataRowCount(String accountoid) { 
		String cnt = "";

		this.sql = "select count(*) as CNT from STREAM_DATA where ACCOUNT_OID = '" + accountoid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					cnt = rowData[0].CNT;

					if (this.log) 
					{
						this.log.info "streamGetStreamDataRowCount: sql is " + this.sql;
						this.log.info "streamGetStreamDataRowCount: rowData.size is " + rowData.size;
						this.log.info "streamGetStreamDataRowCount: rowData[0] is " + rowData[0];
						this.log.info "streamGetStreamDataRowCount: cnt is " + cnt; 
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
			return cnt.toInteger(); 
		}
	}
	/**
	*  START HERE
	*/
	def streamExpireDateEqualTOD(String streamHandle) { 
		// Returns 1 if the stream handle supplied has a
		// STREAM_DATA.STATUS value of 'active'.  Otherwise,
		// returns 0. 
		//

		String Qsql = "SELECT TO_CHAR(EXPIRATION_DATE, 'YYYY/MM/DD HH06') AS TIME " +
			", TO_CHAR(sysdate - 2/06,'YYYY/MM/DD HH06') as TOD " +
			"FROM STREAM_DATA where STREAM_HANDLE_OID = '" + streamHandle + "'";

		def rowData = this.dbConnExecuteQuery(Qsql);

		this.dbConnClose();

		if (this.log) {
			this.log.info "streamGetExpireDate: sql is : $Qsql"; 
			this.log.info "streamGetExpireDate: rowData.size is " + rowData.size;
			this.log.info "streamGetExpireDate: rowData[0].TIME is " + rowData[0].TIME;
			this.log.info "streamGetExpireDate: rowData[0].TOD is " + rowData[0].TOD;
		}	

		if (rowData[0].TIME == rowData[0].TOD) {
			return 1;
		} else {
			return 0;
		}
	}

	/*
	* STREAM SET Methods 
	*/

	/**
	* Update the STREAM_DATA.EXPIRATION_DATE column associated with the STREAM_HANDLE_OID 
	* supplied using the unit and value supplied.
	* @verson 1.0
	* @param oid The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	* @param unit Either "year", "day", "hour", or "minute". 
	* @param value A positive or negative integer 
	*
	* @return The number of rows updated.  Should be one. 
	*/
	def streamSetExpireTime(String streamHandle, String unit, float value) { 
		int rc = 0;
		String delimit = null;
		String buf = null;
		
		switch (unit) {
			case "hour":
			case "hours":
				buf = "$value/06";
				break;
			case "day":
			case "days":
				buf = "$value";
				break;
			case "minute":
			case "minutes":
				buf = "$value/1440";
				break;
			case "second":
			case "seconds":
				buf = "$value/86400";
				break;
			case "year":
			case "years":
				buf = "$value * 365";
				break;
		}
				
		if (value > 0) {
			this.sql = "UPDATE STREAM_DATA set EXPIRATION_DATE = sysdate + " + buf + " where STREAM_HANDLE_OID = '" + streamHandle + "'";
		} else {
			this.sql = "UPDATE STREAM_DATA set EXPIRATION_DATE = sysdate " + buf  + " where STREAM_HANDLE_OID = '" + streamHandle + "'";

		}
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamSetExpireTime: streamHandle is "  + streamHandle;
					this.log.info "streamSetExpireTime: rc is "  + rc;
					this.log.info "streamSetExpireTime: sql is "  + this.sql;
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc; 
		}
	}
	/**
	* Update the STREAM_DATA.STATUS column to 'deleted'. 
	*
	* @param oid The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	*
	* @return The number of rows updated.  Should be one. 
	*/
	def streamSetStatusDeleted(String streamHandle) { 
		//  Returns number of rows updated.  Should always
		//  be one.	
		int rc = 0;

		this.sql = "update STREAM_DATA set STATUS = 'deleted' where STREAM_HANDLE_OID = '" + streamHandle + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamSetStatusDeleted: streamHandle is "  + streamHandle;
					this.log.info "streamSetStatusDeleted: rc is "  + rc;
					this.log.info "streamSetStatusDeleted: sql is "  + this.sql;
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc; 
		}
	}
	/**
	* Update the STREAM_DATA.STATUS column to 'active'. 
	*
	* @param oid The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	*
	* @return The number of rows updated.  Should be one. 
	*/
	def streamSetStatusActive(String streamHandle) { 
		//  Returns number of rows updated.  Should always
		//  be one.	
		int rc = 0;

		this.sql = "update STREAM_DATA set STATUS = 'active',EXPIRATION_DATE = sysdate + 1 where STREAM_HANDLE_OID = '" + streamHandle + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamSetStatusActive: streamHandle is "  + streamHandle;
					this.log.info "streamSetStatusActive: rc is "  + rc;
					this.log.info "streamSetStatusActive: sql is "  + this.sql;
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc; 
		}
	}
	/**
	* Update the STREAM_DATA.ACCOUNT_OID column for the 
	* STREAM_HANDLE_OID supplied in streamHandle to the ACCOUNT_OID 
	* value supplied in oid. 
	*
	* @param streamHandle The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	* @param oid The ACCOUNT_OID to set. 
	*
	* @return The number of rows updated.  Should be one. 
	*/
	def streamSetAccountOID(String streamHandle,String oid) { 
		/*
		*  Returns the number of rows updated, should always be one.
		*/
		int rc = 0;

		this.sql = "update STREAM_DATA set ACCOUNT_OID = '" + oid + "' where STREAM_HANDLE_OID = '" + streamHandle + "'"; 

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamSetAccountOID: streamHandle is "  + streamHandle;
					this.log.info "streamSetAccountOID: rc is "  + rc;
					this.log.info "streamSetAccountOID: sql is "  + this.sql;
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc; 
		}
	}
	/**
	* Update the STREAM_DATA.CREATED_BY column for the 
	* STREAM_HANDLE_OID supplied in streamHandle to the ACCOUNT_OID 
	* value supplied in oid. 
	*
	* @param streamHandle The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	* @param oid The ACCOUNT_OID to set in CREATED_BY. 
	*
	* @return The number of rows updated.  Should be one. 
	*/
	def streamSetCreatedBy(String streamHandle,String nodeid) { 
		int rc = 0;

		this.sql = "update STREAM_DATA set CREATED_BY = '" + nodeid + "' where STREAM_HANDLE_OID = '" + streamHandle + "'"; 

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamSetCreatedBy: streamHandle is "  + streamHandle;
					this.log.info "streamSetCreatedBy: rc is "  + rc;
					this.log.info "streamSetCreatedBy: sql is "  + this.sql;
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc; 
		}

	}
	/**
	* Physically removes the STREAM_DATA_HISTORY and STREAM_DATA
	* rows for the STREAM_HANDLE_OID supplied in streamHandle. 
	*
	* @param streamHandle The STREAM_DATA.STREAM_HANDLE_OID to extract. 
	*
	* @return The number of rows deleted.  Should be zero or greater. 
	*/
	def streamDeleteHandle(String streamHandle) { 
		int rc = 0;

		this.sql = "delete from STREAM_DATA_HISTORY where STREAM_HANDLE_OID = '" + streamHandle + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamDeleteHandle: streamHandle is "  + streamHandle;
					this.log.info "streamDeleteHandle: rc is "  + rc;
					this.log.info "streamDeleteHandle: sql is "  + this.sql;
				}
		
				/*
				*  Now remove the STREAM_DATA_HISTORY row.
				*/
				this.sql = "delete from STREAM_DATA where STREAM_HANDLE_OID = '" + streamHandle + "'";

				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "streamDeleteHandle: rc is "  + rc;
						this.log.info "streamDeleteHandle: sql is "  + this.sql;
					}
				} 
			}
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			return rc;	
		}
	}
	/**
	* Physically removes all STREAM_DATA_HISTORY and STREAM_DATA
	* rows for the ACCOUNT_OID supplied in oid. 
	*
	* @param oid The ACCOUNT_OID used in the delete. 
	*
	* @return The number of rows deleted.  Should be zero or greater. 
	*/
	def streamDeleteAccountOid(String oid) { 
		// Will return the number of rows deleted.  
		int rc = 0;

		this.sql = "delete from STREAM_DATA_HISTORY where ACCOUNT_OID = '" + oid + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamDeleteAccountOid: streamHandle is "  + streamHandle;
					this.log.info "streamDeleteAccountOid: rc is "  + rc;
					this.log.info "streamDeleteAccountOid: sql is "  + this.sql;
				}

				this.sql = "delete from STREAM_DATA where ACCOUNT_OID = '" + oid + "'";

				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "streamDeleteAccountOid: streamHandle is "  + streamHandle;
						this.log.info "streamDeleteAccountOid: rc is "  + rc;
						this.log.info "streamDeleteAccountOid: sql is "  + this.sql;
					}
				} 
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			return rc;
		}
	}
	/**
	* Updates the STREAM_DATA.EXPIRATION_DATE value to 
	* sysdate - years supplied in years. 
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	* @param years Number of years minus sysdate to regress. 
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamExpireYears(String streamHandle, int years) { 
		//
		// Returns the number of rows updated.
		//
		int rc = 0;
		int toDays = (int) years * 365;
		String a = "sysdate - $toDays"; 
	
		dbSetStatusExpireSQL(streamHandle, a);
	
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "streamExpireYears: streamHandle is "  + streamHandle;
						this.log.info "streamExpireYears: years is "  + years;
						this.log.info "streamExpireYears: rc is "  + rc;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				return rc;
			}
			finally {
				this.dbConnClose();
				return rc;
			}
		} else {
			return rc;
		}
	}
	/**
	* Updates the STREAM_DATA.EXPIRATION_DATE value to 
	* sysdate - months supplied in months. 
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	* @param months Number of months minus sysdate to regress. 
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamExpireMonths(String streamHandle, int months) { 
		//
		// Returns the number of rows updated.
		//
		int rc = 0;
		int toDays = (int) months * 30;
		String a = "sysdate - $toDays"; 
	
		dbSetStatusExpireSQL(streamHandle, a);
	
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "streamExpireMonths: streamHandle is "  + streamHandle;
						this.log.info "streamExpireMonths: months is "  + months;
						this.log.info "streamExpireMonths: rc is "  + rc;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				return rc;
			}
			finally {
				this.dbConnClose();
				return rc;
			}
		} else {
			return rc;
		}
	}
	/**
	* Updates the STREAM_DATA.EXPIRATION_DATE value to 
	* sysdate - weeks supplied in weeks. 
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	* @param weeks Number of weeks minus sysdate to regress. 
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamExpireWeeks(String streamHandle, int weeks) { 
		//
		// Returns the number of rows updated.
		//
		int rc;
		int toDays = (int) weeks * 7;
		String a = "sysdate - $toDays"; 
	
		dbSetStatusExpireSQL(streamHandle, a);
	
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "streamExpireWeeks: streamHandle is "  + streamHandle;
						this.log.info "streamExpireWeeks: weeks is "  + weeks;
						this.log.info "streamExpireWeeks: rc is "  + rc;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				return rc;
			}
			finally {
				this.dbConnClose();
				return rc;
			}
		} else {
			return rc;
		}
	}
	/**
	* Updates the STREAM_DATA.EXPIRATION_DATE value to 
	* sysdate - days supplied in days. 
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	* @param days Number of days minus sysdate to regress. 
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamExpireDays(String streamHandle, int days) { 
		//
		// Returns the number of rows updated.
		//
		int rc = 0;
		String a = "sysdate - $days"; 
	
		dbSetStatusExpireSQL(streamHandle, a);
	
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "streamExpireDays: streamHandle is "  + streamHandle;
						this.log.info "streamExpireDays: days is "  + days;
						this.log.info "streamExpireDays: rc is "  + rc;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				return rc;
			}
			finally {
				this.dbConnClose();
				return rc;
			}
		} else {
			return rc;
		}
	}
	/**
	* Updates the STREAM_DATA.EXPIRATION_DATE value to 
	* sysdate - hours supplied in hours. 
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	* @param hours Number of hours minus sysdate to regress. 
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamExpireHours(String streamHandle, int hours) { 
		//
		// Returns the number of rows updated.
		//
		int rc = 0;
		String a = "sysdate - $hours/06"; 
	
		dbSetStatusExpireSQL(streamHandle, a);
	
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "streamExpireHours: streamHandle is "  + streamHandle;
						this.log.info "streamExpireHours: hours is "  + hours;
						this.log.info "streamExpireHours: rc is "  + rc;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				return rc;
			}
			finally {
				this.dbConnClose();
				return rc;
			}
		} else {
			return rc;
		}
	}
	/**
	* Updates the STREAM_DATA.EXPIRATION_DATE value to 
	* sysdate - minutes supplied in minutes. 
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	* @param mins Number of minutes minus sysdate to regress. 
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamExpireMinutes(String streamHandle, int mins) { 
		//
		// Returns the number of rows updated.
		//
		int rc = 0;
		String a = "sysdate - $mins/1440"; 
	
		dbSetStatusExpireSQL(streamHandle, a);
	
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "streamExpireMinutes: streamHandle is "  + streamHandle;
						this.log.info "streamExpireMinutes: mins is "  + mins;
						this.log.info "streamExpireMinutes: rc is "  + rc;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				return rc;
			}
			finally {
				this.dbConnClose();
				return rc;
			}
		} else {
			return rc;
		}
	}
	/**
	* @deprecated Replaced by <code>streamSetStatusActive(String)</code>

	*
	* Updates the STREAM_DATA.STATUS value to 'active' for the STREAM_HANDLE_OID
	* referenced by streamHandle 
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamStatusActive(String streamHandle) { 
		this.sql = "select STATUS from STREAM_DATA where " + 
			"STREAM_HANDLE_OID = '" + streamHandle + "' and " +
			"STATUS = 'active'";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamStatusActive: streamHandle is "  + streamHandle;
					this.log.info "streamStatusActive: mins is "  + mins;
					this.log.info "streamStatusActive: rc is "  + rc;
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* @deprecated Replaced by <code>streamSetStatusDeleted(String)</code>

	*
	* Updates the STREAM_DATA.STATUS value to 'deleted' for the STREAM_HANDLE_OID
	* referenced by streamHandle 
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamStatusDeleted(String streamHandle) { 
		this.sql = "select STATUS from STREAM_DATA where " + 
			"STREAM_HANDLE_OID = '" + streamHandle + "' and " +
			"STATUS = 'deleted'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamStatusDeleted: streamHandle is "  + streamHandle;
					this.log.info "streamStatusDeleted: mins is "  + mins;
					this.log.info "streamStatusDeleted: rc is "  + rc;
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* Updates the STREAM_DATA.CREATED_DATE associated with the STREAM_HANDLE_OID 
	* supplied in streamHandle to sysdate plus/minus days supplied in days.
	*  
	*
	* @param streamHandle The STREAM_HANDLE_OID used in the update. 
	* @param days Positive/negative integer of the number of days to add/subtract from sysdate.
	*
	* @return The number of rows updated.  Should be one.
	*/
	def streamSetCreatedDate(String streamHandle,int days) { 
		String b = "";
		int rc = 0;	

		if (days >= 0) {
			b = "+";
		}  

		this.sql = "update STREAM_DATA set CREATED_DATE = " + 
		   "sysdate $b $days where STREAM_HANDLE_OID = '" + streamHandle + "'"; 

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "streamSetCreatedDate: sql is "  + this.sql;
					this.log.info "streamSetCreatedDate: streamHandle is "  + streamHandle;
					this.log.info "streamSetCreatedDate: days is "  + days;
					this.log.info "streamSetCreatedDate: rc is "  + rc;
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return rc;
		}
	}

}
