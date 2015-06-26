package dbUtils;
/**
 * Provides Utilities to Interact with ACCOUNT data.
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
   1/11/11: Updated all methods adding finally() block and 
	   removed dbConnClose and return from all
       catch() blocks.   PJG
   2/9/11: Removed dbIdentifer references and verified dbConnClose().  pjg
   2/9/11: Added method accountGetfromNodeAccountId(). pjg/sg
   04/11/2011 - Updated For  fetching DECE_DOMAIN_ID for an Account. Jeet
   04/18/2011 - Added accountGetDECEDomainId. pjg
   04/19/2011 - Added accountSetDECEDomainId. pjg
   08/19/2011 - Added accountSetCountry. pjg
----------------------------------------------------
*/

import groovy.sql.Sql;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbAccount extends dbUtils
{
	def sql = null;
	def log = null;

	/*
	*  Constructors
	*/	
	/**
	*  Creates a new <code>dbAccount<code> object with
	*  ability to write to the log.
	*/
	dbAccount(log) {
		super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbAccount<code> object.
	*
	*/
	dbAccount() {
	}
	/**
	*  Creates a new <code>dbAccount<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbAccount(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	}
	
	/**
	*   Private method for constructing SQL
	*/
	private dbSetStatusSQL(String acct, String status) {
		this.sql = "update ACCOUNT set STATUS = '%%STATUS%%' where ACCOUNT_OID = hextoraw('%%ACCT%%')";
		this.sql = sql.replaceAll("%%STATUS%%", status);
		this.sql = sql.replaceAll("%%ACCT%%", acct);
	}
	
	/*
	*  GET Methods
	*/
	
	
	/**
	* Extract the ACCOUNT row associated with the ACCOUNT_OID 
	* supplied. 
	*
	* @param oid The ACCOUNT.ACCOUNT_OID to extract. 
	* @return XML encapsulated ACCOUNT row.
	*
	*/
	def accountGetRow(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"STATUS, " +
		"DISPLAY_NAME, " +
		"CREATED_DATE, " +
		"UPDATED_DATE, " +
		"NEWSFEED_CLEAR_TIME, " +
		"COUNTRY," +
		"DECE_DOMAIN_ID from ACCOUNT where ACCOUNT_OID = hextoraw('$oid')";

		xmlData = this.SelectFromDB(this.sql);
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "accountGetRow: sql is " + this.sql;
					this.log.info "accountGetRow: xmlData.length() is " + xmlData.length();
					this.log.info "accountGetRow: xmlData is $xmlData";
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
	* Extract the latest ACCOUNT_STATUS_HISTORY details associated with the ACCOUNT_OID 
	* supplied. 
	* @version 1.0
	*
	* @author Shikha Gupta
	*
	* @param oid The ACCOUNT.ACCOUNT_OID to extract. 
	*
	* @return XML encapsulated ACCOUNT_STATUS_HISTORY row
	*
	*/
	     		def accountGetStatusHistory(String oid) {
	     				
	     		String xmlData = "";
	     
	     		this.sql = "select " +
	     		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
	     		"STATUS, " +
	     		"DESCRIPTION, " +
	     		"CREATED_DATE, " +
	     		"to_char(rawtohex(CREATED_BY_USER_OID)) CREATED_BY_USER_OID , " +
	     		"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID " +
	     		"from ACCOUNT_STATUS_HISTORY where ACCOUNT_OID = '" + oid + "' " +
	     		"and HISTORY_OID = (Select max(HISTORY_OID) from ACCOUNT_STATUS_HISTORY " +
	     		"where ACCOUNT_OID = hextoraw('" + oid + "')) ";
	     		
	     		this.log.info "accountGetStatusHistory: sql is " + this.sql;
	     		
	     		try
	     		{	
	     			xmlData = this.SelectFromDB(this.sql);
	     
	     			if(xmlData.length() > 0)
	     			{	
	     				if (this.log) 
	     				{
	     					this.log.info "accountGetStatusHistory: sql is " + this.sql;
	     					this.log.info "accountGetStatusHistory: xmlData.length() is " + xmlData.length();
	     					this.log.info "accountGetStatusHistory: xmlData is $xmlData";
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
	* Get the RIGHTS_LOCKER.RIGHTS_LOCKER_OID associated with
	* the account.
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	* @return RIGHTS_LOCKER.RIGHTS_LOCKER_OID. 
	*
	*/
	def accountGetRightsLockerId(String oid) {
		String locker = "";

		this.sql = "select " +
		"to_char(rawtohex(RIGHTS_LOCKER_OID)) LOCKER from RIGHTS_LOCKER " +
		"where ACCOUNT_OID = hextoraw('" + oid + "')";

		def rowData = this.dbConnExecuteQuery(this.sql);
		
		try
		{	if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					locker = rowData[0].LOCKER;
					if (this.log) 
					{
						this.log.info "accountGetRightsLockerId: sql is " + this.sql;
						this.log.info "accountGetRightsLockerId: rowData.size is " + rowData.size;
						this.log.info "accountGetRightsLockerId: rowData[0] is " + rowData[0];
						this.log.info "accountGetRightsLockerId: locker[$locker]"; 
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
			return locker;
		}
	}
	
	/**
	* Get the DEVICE.DEVICE_OID associated with
	* the account.
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	* @return DEVICE.DEVICE_OID. 
	*
	*/
	def accountGetDeviceId(String oid) {
		String DeviceId = "";

		this.sql = "select " +
		"to_char(rawtohex(DEVICE_OID)) DEVICE_OID from DEVICE " +
		"where ACCOUNT_OID = hextoraw'" + oid + "')";

		def rowData = this.dbConnExecuteQuery(this.sql);
		
		try
		{	if(!rowData.isEmpty())
			{	
				if (rowData.size()) 
				{
					DeviceId = rowData.DEVICE_OID;
					if (this.log) 
					{
						this.log.info "accountGetDeviceId: sql is " + this.sql;
						this.log.info "accountGetDeviceId: rowData.size is " + rowData.size;
						this.log.info "accountGetDeviceId: rowData[0] is " + rowData[0];
						this.log.info "accountGetDeviceId: DeviceId[$DeviceId]"; 
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
			return DeviceId;
		}
	}
	
	
	
	
	
	/**
	* Get the NODE_ACCOUNT.ACCOUNT_OID associated with
	* the NODE_ACCOUNT_ID supplied in the oid parameter.
	*
	* @param nodeacctid The NODE_ACCOUNT.NODE_ACCOUNT_ID 
	* @return NODE_ACCOUNT.ACCOUNT_OID
	*
	*/
	def accountGetfromNodeAccountId (String nodeacctid) {
		def mdata = "";

		this.sql = "select " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID from NODE_ACCOUNT " +
		"where NODE_ACCOUNT_ID = hextoraw('" + nodeacctid + "')";

		def rowData = this.dbConnExecuteQuery(this.sql);
				
		try
		{	if(!rowData.isEmpty())
			{	
				if (rowData.size()) 
				{
					mdata = rowData[0].ACCOUNT_OID;
					if (this.log) 
					{
						this.log.info "accountGetfromNodeAccountId: sql is " + this.sql;
						this.log.info "accountGetfromNodeAccountId: rowData.size is " + rowData.size();
						this.log.info "accountGetfromNodeAccountId: rowData[0] is " + rowData[0];
						this.log.info "accountGetfromNodeAccountId: ACCOUNT_OID[$mdata]"; 
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
			return mdata;
		}
	}
	
	/**
	* Get the ACCOUNT.DECE_DOMAIN_ID associated with
	* the account.
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	* @return ACCOUNT.DECE_DOMAIN_ID. 
	*
	*/
	def accountGetDECEDomainId(String oid) {
		String id = "";

		this.sql = "select " +
		"DECE_DOMAIN_ID from ACCOUNT " +
		"where ACCOUNT_OID = hextoraw('" + oid + "')";

		def rowData = this.dbConnExecuteQuery(this.sql);
		
		try
		{	if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					id = rowData[0].DECE_DOMAIN_ID;
					if (this.log) 
					{
						this.log.info "accountGetDECEDomainId: DECE_DOMAIN_ID[$id]"; 
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
			return id;
		}
	}


	/*
	*  SET Methods
	*/
	
	/**
	* Set the ACCOUNT.COUNTRY associated with the account.
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	* @param country The ACCOUNT.COUNTRY to set.  
	*
	* @return Number of rows updated. 
	*
	*/
	def accountSetCountry(String oid, String country) {
		def rc = 0;
		
		this.sql = "UPDATE ACCOUNT set COUNTRY = '" + country + "' " +
"where ACCOUNT_OID = hextoraw('" + oid + "')";

		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetCountry: rc is "  + rc;
					this.log.info "accountSetCountry: sql is " + this.sql;
				}
			}
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return(rc);
		}
	}
	
	
	
	/**
	* Set the ACCOUNT.DECE_DOMAIN_ID associated with
	* the account.
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	* @param value The ACCOUNT.DECE_DOMAIN_ID to set.  If non is
	*    supplied will be set to null.
	* @return Number of rows updated. 
	*
	*/
	def accountSetDECEDomainId(String oid, String value = "") {
		def rc = 0;
		
		this.sql = "UPDATE ACCOUNT set DECE_DOMAIN_ID = '" + value + "' " +
			"where ACCOUNT_OID = hextoraw('" + oid + "')";

		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetDECEDomainId: rc is "  + rc;
					this.log.info "accountSetDECEDomainId: sql is " + this.sql;
				}
			}
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return(rc);
		}
	}

	/**
	* Sets the ACCOUNT.STATUS value to the string supplied via
	* parameter status.  
	* @version 1.0
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	* @param status The string to insert into ACCOUNT.STATUS 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatus(String oid, String status) {
		def rc = 0;

		dbSetStatusSQL(oid,status);
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
				}
			}
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return(rc);
		}
	}

	/**
	* Sets the ACCOUNT.STATUS value to 'active'.  
	*
	* @version 1.0
	* @param oid The ACCOUNT.ACCOUNT_OID 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatusActive(String acct) {
		def rc = 0;

		dbSetStatusSQL(acct,"active");
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
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
	}

	/**
	* Sets the ACCOUNT.STATUS value to 'blocked'.  
	* @version 1.0
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatusBlocked(String oid) {
		def rc = 0;

		dbSetStatusSQL(oid,"blocked");
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
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
	}

	/**
	* Sets the ACCOUNT.STATUS value to 'deleted'.  
	* @version 1.0
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatusDeleted(String oid) {
		private int rc;

		dbSetStatusSQL(oid, "deleted");
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
				}
			}
		}
		catch(Throwable e)
		{
			if(this.log) {
				this.log.info e;
				this.log.info "Returning empty string for Result Set."
			}
			rc = 0;
		}
		finally {
			this.dbConnClose();
			return(rc);
		}
	}

	/**
	* Sets the ACCOUNT.STATUS value to 'archived'.  
	* @version 1.0
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatusArchived(String oid) {
		def rc = 0;

		dbSetStatusSQL(oid,"archived");
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
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
	}

	/**
	* Sets the ACCOUNT.STATUS value to 'forceddelete'.  
	* @version 1.0
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatusForceddelete(String oid) {
		def rc = 0;

		dbSetStatusSQL(oid,"forcedeleted");
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
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
	}

	/**
	* Sets the ACCOUNT.STATUS value to 'suspended'.  
	* @version 1.0
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatusSuspended(String oid) {
		def rc = 0;

		dbSetStatusSQL(oid,"suspended");
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
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
	}

	/**
	* Sets the ACCOUNT.STATUS value to 'pending'.  
	* @version 1.0
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatusPending(String oid) {
		def rc = 0;

		dbSetStatusSQL(oid,"pending");
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
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
	}

	/**
	* Sets the ACCOUNT.STATUS value to 'other'.  
	* @version 1.0
	*
	* @param oid The ACCOUNT.ACCOUNT_OID 
	*
	* @return Number of rows updated, should be one. 
	*
	*/
	def accountSetStatusOther(String oid) {
		def rc = 0;

		dbSetStatusSQL(oid,"other");
		
		rc = this.dbConnExecuteUpdate(this.sql);
		
		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "accountSetStatus: rc is "  + rc;
					this.log.info "accountSetStatus: sql is " + this.sql;
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
	}
}
