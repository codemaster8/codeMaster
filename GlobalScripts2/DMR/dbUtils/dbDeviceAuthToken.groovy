package dbUtils;
/**
 * Provides Utilities to Interact with DEVICE_AUTH_TOKEN data.
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
  3/23/11: Created class.  PJG
  05/25/11: Added method deviceAuthTokenGetStatus.  pjg
----------------------------------------------------
*/

import groovy.sql.Sql;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbDeviceAuthToken extends dbUtils
{
	def sql = null;
	def log = null;
	def dbIdentifier = null;

	/*
	*  Constructors
	*/	
	/**
	*  Creates a new <code>dbDeviceAuthToken<code> object with
	*  ability to write to the log.
	*/
	dbDeviceAuthToken(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
		this.dbIdentifier = dbIdentifier;
	}
	
	
	/*
	*  GET Methods
	*/
		
	/**
	* Extract the dbDeviceAuthToken row associated with the DEVICE_AUTH_TOKEN_OID 
	* supplied. 
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to extract. 
	* @return XML encapsulated DEVICE_AUTH_TOKEN row.
	*
	*/
	def deviceAuthTokenGetRow(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(DEVICE_AUTH_TOKEN_OID)) DEVICE_AUTH_TOKEN_OID, " +
		"EXPIRATION_DATE, " +
		"CREATED_DATE, " +
		"DEVICE_AUTH_CODE, " +
		"to_char(rawtohex(USER_OID)) USER_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"STATUS, " +
		"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +
		"IS_GENERATED " +
		"from DEVICE_AUTH_TOKEN where DEVICE_AUTH_TOKEN_OID = '$oid'";

		xmlData = this.SelectFromDB(this.sql);
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "deviceAuthTokenGetRow: sql is " + this.sql;
					this.log.info "deviceAuthTokenGetRow: xmlData.length() is " + xmlData.length();
					this.log.info "deviceAuthTokenGetRow: xmlData is $xmlData";
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
	* Returns the DEVICE_AUTH_TOKEN.STATUS of the requested 
	* DEVICE_AUTH_TOKEN_OID.
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to extract. 
	* @return DEVICE_AUTH_TOKEN.STATUS
	*
	*/
	def deviceAuthTokenGetStatus(String oid) { 
	
		String r = "";

		this.sql = "SELECT STATUS as " +
			"STATUS FROM DEVICE_AUTH_TOKEN where DEVICE_AUTH_TOKEN_OID = '" + oid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					r = rowData[0].STATUS;
					if (this.log) 
					{
						this.log.info "deviceAuthTokenGetStatus: sql is " + this.sql;
						this.log.info "deviceAuthTokenGetStatus: rowData.size is " + rowData.size;
						this.log.info "deviceAuthTokenGetStatus: rowData[0] is " + rowData[0];
						this.log.info "deviceAuthTokenGetStatus: r is " + r; 
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
			return r;
		}
	}
	/**
	* Returns the different between TOD and DEVICE_AUTH_TOKEN.CREATED_DATE 
	* for DEVICE_AUTH_TOKEN_OID supplied. 
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to extract. 
	* @return Number of milliseconds difference
	*
	*/
	def deviceCreateDateTODdiff(String oid) { 
	
		int rc = 0;	
		Float g = 0.0;
		String r = "";

		this.sql = "SELECT (extract(DAY FROM sysdate - CREATED_DATE)*24*60*60)+ " + 
			"(extract(HOUR FROM sysdate - CREATED_DATE)*60*60)+" +
			"(extract(MINUTE FROM sysdate - CREATED_DATE)*60)+" +
			"extract(SECOND FROM sysdate - CREATED_DATE)" +
			"as sec FROM DEVICE_AUTH_TOKEN where DEVICE_AUTH_TOKEN_OID = '" + oid + "'";

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
						this.log.info "deviceCreateDateTODdiff: g [$g]";
						this.log.info "deviceCreateDateTODdiff: rowData.size is " + rowData.size;
						this.log.info "deviceCreateDateTODdiff: rowData[0] is " + rowData[0];
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
	* Returns the different between DEVICE_AUTH_TOKEN.EXPIRATION_DATE and 
	* DEVICE_AUTH_TOKEN.CREATED_DATE for DEVICE_AUTH_TOKEN_OID supplied. 
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to extract. 
	* @return Number of seconds difference
	*
	*/
	def deviceCreateDateExpirediff(String oid) { 
	
		int rc = 0;	
		Float g = 0.0;
		String r = "";

		this.sql = "SELECT (extract(DAY FROM EXPIRATION_DATE - CREATED_DATE)*24*60*60)+ " + 
			"(extract(HOUR FROM EXPIRATION_DATE - CREATED_DATE)*60*60)+" +
			"(extract(MINUTE FROM EXPIRATION_DATE - CREATED_DATE)*60)+" +
			"extract(SECOND FROM EXPIRATION_DATE - CREATED_DATE)" +
			"as sec FROM DEVICE_AUTH_TOKEN where DEVICE_AUTH_TOKEN_OID = '" + oid + "'";

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
						this.log.info "deviceCreateDateExpirediff: g [$g]";
						this.log.info "deviceCreateDateExpirediff: rowData.size is " + rowData.size;
						this.log.info "deviceCreateDateExpirediff: rowData[0] is " + rowData[0];
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
	* Returns the DEVICE_AUTH_TOKEN.EXPIRATION_DATE in the requested 
	* format.
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to extract. 
	* @return DEVICE_AUTH_TOKEN.EXPIRATION_DATE
	*
	*/
	def deviceGetExpirationDate(String oid,String format = "STANDARD") { 
	
		String r = "";

		this.sql = "SELECT to_char(EXPIRATION_DATE, 'yyyy/mm/dd HH24:MI:SS') as " +
			"EXPIRATION_DATE FROM DEVICE_AUTH_TOKEN where DEVICE_AUTH_TOKEN_OID = '" + oid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					r = rowData[0].EXPIRATION_DATE;
					if (this.log) 
					{
						this.log.info "deviceGetExpirationDate: sql is " + this.sql;
						this.log.info "deviceGetExpirationDate: rowData.size is " + rowData.size;
						this.log.info "deviceGetExpirationDate: rowData[0] is " + rowData[0];
						this.log.info "deviceGetExpirationDate: r is " + r; 
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
			return r;
		}
	}
	/**
	* Returns the DEVICE_AUTH_TOKEN.DEVICE_AUTH_CODE of the requested 
	* DEVICE_AUTH_TOKEN_OID.
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to extract. 
	* @return DEVICE_AUTH_TOKEN.DEVICE_AUTH_CODE
	*
	*/
	def deviceGetAuthCode(String oid) { 
	
		String r = "";

		this.sql = "SELECT DEVICE_AUTH_CODE as " +
			"DEVICE_AUTH_CODE FROM DEVICE_AUTH_TOKEN where DEVICE_AUTH_TOKEN_OID = '" + oid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					r = rowData[0].DEVICE_AUTH_CODE;
					if (this.log) 
					{
						this.log.info "deviceGetAuthCode: sql is " + this.sql;
						this.log.info "deviceGetAuthCode: rowData.size is " + rowData.size;
						this.log.info "deviceGetAuthCode: rowData[0] is " + rowData[0];
						this.log.info "deviceGetAuthCode: r is " + r; 
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
			return r;
		}
	}
	/**
	* Returns Oracle's sysdate modified either positive/negative measure
	* units as supplied in units.
	*
	* @param measure Positive or negative integer.
	* @return sysdate string
	*
	*/
	def deviceGetSysdate(int measure = null) { 
		String del = "";
		String r = "";
		
		if (measure > 0) {
			del = " + ";
		}
		
		this.sql = "SELECT to_char(sysdate" + del + measure + "/1440,'YYYY-MM-DD HH24:MI:SS')" + "as rdate FROM dual";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					r = rowData[0].rdate;
					if (this.log) 
					{
						this.log.info "deviceGetSysdate: sql is " + this.sql;
						this.log.info "deviceGetSysdate: rowData.size is " + rowData.size;
						this.log.info "deviceGetSysdate: rowData[0] is " + rowData[0];
						this.log.info "deviceGetSysdate: r is " + r; 
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
			return r;
		}
	}
	
	/**
	* Returns an Oracle's sysguid() string.
	*
	* @return An Oracle sys_guid() string.
	*
	*/
	def deviceGetsysguid() { 
		String r = "";
		
		this.sql = "SELECT RAWTOHEX('sys_guid()') as guid FROM dual";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					r = rowData[0].guid;
					if (this.log) 
					{
						this.log.info "deviceGetsysguid: sql is " + this.sql;
						this.log.info "deviceGetsysguid: rowData.size is " + rowData.size;
						this.log.info "deviceGetsysguid: rowData[0] is " + rowData[0];
						this.log.info "deviceGetsysguid: r is " + r; 
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
			return r;
		}
	}
	
	/*
	*  SET Methods
	*/
	
	/**
	* Updates the DEVICE_AUTH_TOKEN row of the DEVICE_AUTH_TOKEN_OID supplied
	* to the status supplied via the status argument.
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to employ in the select. 
	* @param status The DEVICE_AUTH_TOKEN.STATUS value to set.
	*
	* @return Number of DEVICE_AUTH_TOKEN updated.
	*
	*/
	def deviceAuthSetStatus(String oid, String status) { 
	
		int rc = 0;

		this.sql = "UPDATE DEVICE_AUTH_TOKEN set STATUS = '" + status + "' where DEVICE_AUTH_TOKEN_OID = '" + oid + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0)
			{	
				if (this.log) 
					{
						this.log.info "deviceAuthSetStatus: rc [$rc]";

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
	* Updates the DEVICE_AUTH_TOKEN row of the DEVICE_AUTH_TOKEN_OID supplied
	* setting the EXPIRATION_DATE to sydate +- minutes.
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to employ in the select. 
	* @param minutes Minutes from sysdate to set; can be positive or negative.
	*
	* @return Number of DEVICE_AUTH_TOKEN rows updated.
	*
	*/
	def deviceAuthSetExpire(String oid, int minutes) { 
		String del = "";
		int rc = 0;
		
		if (minutes < 0) {
			del = "$minutes/1440";
		} else if (minutes > 0) {
			del = "+ $minutes/1440";
		} else {
			del = "";
		}

		this.sql = "UPDATE DEVICE_AUTH_TOKEN set EXPIRATION_DATE = sysdate " + del + " where DEVICE_AUTH_TOKEN_OID = '" + oid + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0)
			{	
				if (this.log) 
					{
						this.log.info "deviceAuthSetExpire: rc [$rc]";
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
	* Updates the DEVICE_AUTH_TOKEN row of the CREATED_BY_NODE_OID supplied
	* to the node supplied via the node argument.
	*
	* @param oid The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN_OID to employ in the select. 
	* @param node The DEVICE_AUTH_TOKEN.DEVICE_AUTH_TOKEN value to set.
	*
	* @return Number of DEVICE_AUTH_TOKEN updated.
	*
	*/
	def deviceAuthSetCreatedByNode(String oid, String node) { 
	
		int rc = 0;

		this.sql = "UPDATE DEVICE_AUTH_TOKEN set CREATED_BY_NODE_OID = '" + node + "' where DEVICE_AUTH_TOKEN_OID = '" + oid + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0)
			{	
				if (this.log) 
					{
						this.log.info "deviceAuthSetCreatedByNode: rc [$rc]";
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
	* Deletes all DEVICE_AUTH_TOKEN roles for the CREATED_BY_NODE_OID supplied.
	*
	* @param oid The DEVICE_AUTH_TOKEN.CREATED_BY_NODE_OID to employ in the delete. 
	*
	*/
	def deviceDeleteTokensByNodeOid(String oid) { 
	
		int rc = 0;	

		this.sql = "DELETE FROM DEVICE_AUTH_TOKEN where CREATED_BY_NODE_OID = '" + oid + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0)
			{	
				if (this.log) 
					{
						this.log.info "deviceDeleteTokensByNodeOid: rc [$rc]";
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
	* Deletes all DEVICE_AUTH_TOKEN roles for the ACCOUNT_OID supplied.
	*
	* @param oid The DEVICE_AUTH_TOKEN.ACCOUNT_OID to employ in the delete. 
	*
	*/
	def deviceDeleteTokensByAccountOid(String oid) { 
	
		int rc = 0;	

		this.sql = "DELETE FROM DEVICE_AUTH_TOKEN where ACCOUNT_OID = '" + oid + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0)
			{	
				if (this.log) 
					{
						this.log.info "deviceDeleteTokensByAccountOid: rc [$rc]";
						this.log.info "deviceDeleteTokensByAccountOid: rowData.size is " + rowData.size;
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
	* Deletes all DEVICE rows for the ACCOUNT_OID supplied.
	*
	* @param oid The DEVICE.ACCOUNT_OID to employ in the delete. 
	*
	*/
	def deviceDeleteDeviceByAccount(String oid) { 
	
		int rc = 0;	

		this.sql = "DELETE FROM DEVICE where ACCOUNT_OID = '" + oid + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0)
			{	
				if (this.log) 
					{
						this.log.info "deviceDeleteDeviceByAccount: rc [$rc]";
						this.log.info "deviceDeleteDeviceByAccount: rowData.size is " + rowData.size;
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
	* Returns the count of the DEVICE_AUTH_TOKEN rows currently owned
	* by the ACCOUNT_OID supplied in argument oid.
	*
	* @param oid The DEVICE_AUTH_TOKEN.ACCOUNT_OID to employ in the select. 
	* @return Number of DEVICE_AUTH_TOKEN owned by the account.
	*
	*/
	def deviceAuthTokenCountByNode(String oid) { 
	
		int cnt = 0;

		this.sql = "SELECT count(*) as CNT FROM DEVICE_AUTH_TOKEN where ACCOUNT_OID = '" + oid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql,this.dbIdentifier);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					cnt = rowData[0].CNT;
					if (this.log) 
					{
						this.log.info "deviceAuthTokenCountByNode: sql is " + this.sql;
						this.log.info "deviceAuthTokenCountByNode: rowData.size is " + rowData.size;
						this.log.info "deviceAuthTokenCountByNode: rowData[0] is " + rowData[0];
						this.log.info "deviceAuthTokenCountByNode: cnt is " + cnt; 
					}			
				}
			}	
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			return cnt;
		}
	}
}
