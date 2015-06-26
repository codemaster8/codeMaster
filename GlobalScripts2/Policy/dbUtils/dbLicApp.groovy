package dbUtils; 
/**
 * Provides Utilities to Interact with LicApp Coordinator Tables.
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
   	4/19/11: Establishment of class.  pjg
	4/25/11: Added dmLicAppGetClientTrigger.  pjg
	4/25/11: Added dmLicAppClientTriggerCreateDatetoTOD, dmLicAppGetClientTriggerData. pjg
	4/25/11: Added coorLicAppGet. pjg

-----------------------------------------------------------
*/


import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbLicApp extends dbUtils 
{
	def sql = null;
	def log = null;
	def dbIdentifier = null;
	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbLicApp<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbLicApp(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
		this.dbIdentifier = dbIdentifier;
	} 
	
	/*
	*  GET Methods
	*/
	
	
	/**
	* Extract the LIC_APP row associated with the LIC_APP_OID and 
	* LICAPP_RESOURCE supplied. 
	* @version 1.0
	*
	* @param oid The LIC_APP.LIC_APP_OID to extract. 
	* @return XML encapsulated LIC_APP row.
	*
	*/
	def coorLicAppGet(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(LIC_APP_OID)) LIC_APP_OID, " +
		"STATUS, " +
		"IMAGE_HEIGHT, " +
		"IMAGE_WIDTH, " +
		"IMAGE_URI, " +
		"IMAGE_MIME_TYPE, " +
		"LIC_APP_HANDLE," + 
		"EMBEDDED," + 
		"APPLICATION," + 
		"DISPLAY_NAME," + 
		"MANUFACTURER," + 
		"MODEL," +
		"BRAND," +
		"BRAND_LANGUAGE," +
		"DECE_DOMAIN_ID," +
		"SERIAL_NO," +
		"to_char(rawtohex(CREATING_USER_OID)) CREATING_USER_OID, " +
		"to_char(rawtohex(ACTIVE_USER_OID)) ACTIVE_USER_OID, " +
		"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"to_char(rawtohex(DEVICE_OID)) DEVICE_OID, " +
		"CREATED_DATE," +
		"DRM_OID," +
		"UPDATE_DATE," +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY " +
		"from LIC_APP where LIC_APP_OID  = '" + oid + "'"; 
		
		try
		{	
			xmlData = this.selectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "coorLicAppGet: sql is " + this.sql;
					this.log.info "coorLicAppGet: xmlData.length() is " + xmlData.length();
					this.log.info "coorLicAppGet: xmlData is $xmlData";
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
	* Extract the DRM_CLIENT_TRIGGER row associated with the DEVICE_RESOURCE and 
	* LICAPP_RESOURCE supplied. 
	* @version 1.0
	*
	* @param deviceurn The DRM_CLIENT_TRIGGER.DEVICE_RESOURCE to extract. 
	* @param licappurn The DRM_CLIENT_TRIGGER.LICAPP_RESOURCE to extract. 
	* @return XML encapsulated ACCOUNT row.
	*
	*/
	def dmLicAppGetClientTrigger(String deviceurn, String licappurn) {
		String xmlData = "";

		this.sql = "select * from " + 
        "(select " +
		"to_char(rawtohex(DRM_CLIENT_TRIGGER_OID)) DRM_CLIENT_TRIGGER_OID, " +
		"to_char(rawtohex(DRM_DOMAIN_OID)) DRM_DOMAIN_OID, " +
		"DRM_ID, " +
		"to_char(rawtohex(DRM_CLIENT_TRIG_TYPE_REF_OID)) DRM_CLIENT_TRIG_TYPE_REF_OID, " +
		"DRM_CLIENT_ID, " +
		"DEVICE_RESOURCE, " +
		"LICAPP_RESOURCE, " +
		"CREATED_DATE, " +
		"SESSION_ID " +
		"from DRM_CLIENT_TRIGGER where DEVICE_RESOURCE  = '" + deviceurn + "' and " + 
		" LICAPP_RESOURCE  = '" + licappurn + "' order by created_date desc) a where rownum < 2";
		
		try
		{	
			xmlData = this.selectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "dmLicAppGetClientTrigger: sql is " + this.sql;
					this.log.info "dmLicAppGetClientTrigger: xmlData.length() is " + xmlData.length();
					this.log.info "dmLicAppGetClientTrigger: xmlData is $xmlData";
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
	* Extract the DRM_CLIENT_TRIGGER_DATA row associated with the DRM_CLIENT_TRIGGER_DATA_OID and 
	* LICAPP_RESOURCE supplied. 
	* @version 1.0
	*
	* @param oid The DRM_CLIENT_TRIGGER_DATA.DRM_CLIENT_TRIGGER_DATA_OID to extract. 
	* @return XML encapsulated ACCOUNT row.
	*
	*/
	def dmLicAppGetClientTriggerData (String oid) {
		String xmlData = "";
		
		this.sql = "select " +
		"to_char(rawtohex(DRM_CLIENT_TRIGGER_DATA_OID)) DRM_CLIENT_TRIGGER_DATA_OID, " +
		"to_char(rawtohex(DRM_CLIENT_TRIGGER_OID)) DRM_CLIENT_TRIGGER_OID, " +
		"utl_raw.cast_to_varchar2(dbms_lob.substr(TRIGGER_DATA)) TRIGGER_DATA, " +
		"CREATED_DATE " + 
		"from DRM_CLIENT_TRIGGER_DATA where DRM_CLIENT_TRIGGER_OID  = '" + oid + "'";
		
		log.info this.sql;
		
		try
		{	
			xmlData = this.selectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "dmLicAppGetClientTriggerData: sql is " + this.sql;
					this.log.info "dmLicAppGetClientTriggerData: xmlData.length() is " + xmlData.length();
					this.log.info "dmLicAppGetClientTriggerData: xmlData is $xmlData";
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
	* Returns the number of seconds difference between TOD and 
	* DRM_CLIENT_TRIGGER.CREATED_DATE. 
	*
	* @param oid The DRM_CLIENT_TRIGGER.DRM_CLIENT_TRIGGER_OID to target. 
	* @return FLOAT Number of seconds difference between the CREATED_DATE and TOD. 
	*/
	def dmLicAppClientTriggerCreateDatetoTOD(String oid) { 
	
		int rc = 0;	
		Float g = 0.0;
		String r = "";

		this.sql = "SELECT (extract(DAY FROM sysdate - CREATED_DATE)*24*60*60)+ " + 
			"(extract(HOUR FROM sysdate - CREATED_DATE)*60*60)+" +
			"(extract(MINUTE FROM sysdate - CREATED_DATE)*60)+" +
			"extract(SECOND FROM sysdate - CREATED_DATE)" +
			"as sec FROM DRM_CLIENT_TRIGGER where DRM_CLIENT_TRIGGER_OID = '" + oid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql,this.dbIdentifier);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					r = rowData[0];
					g = r.substring(r.lastIndexOf("=")+1, r.length() - 1).toFloat().round();
					if (this.log) 
					{
						this.log.info "dmLicAppClientTriggerCreateDatetoTOD: g [$g]";
						this.log.info "dmLicAppClientTriggerCreateDatetoTOD: rowData.size is " + rowData.size;
						this.log.info "dmLicAppClientTriggerCreateDatetoTOD: rowData[0] is " + rowData[0];
					}			
				}
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			return g;
		}
	}
	
	
	/****************************
	*	SET Methods
	*/
	

	/**
	* Deletes the/all LIC_APP, LIC_APP_MEDIA_PROFILE, and LIC_APP_STATUS_HISTORY rows associated with
	* the ACCOUNT_ID provided.
	* @param String accountoid represent the LIC_APP.ACCOUNT_OID to action against.
	* @return int Total number of rows deleted from all tables.
	*  
	*/
	def dmLicAppPurgeByAccount(String accountoid) { 
		int rc = 0;
		int rowCnt = 0;
		String oid = "";
		String triggeroid = "";
				
		try
		{	
			this.sql = "select to_char(rawtohex(LIC_APP_OID)) LIC_APP_OID from LIC_APP where ACCOUNT_OID = '" + accountoid + "'";

			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size >= 1) 
				{
					rowData.each {
						oid = it.LIC_APP_OID;
						
						/*
						*  LIC_APP_STATUS_HISTORY Table
						*/
						this.sql = "DELETE from LIC_APP_STATUS_HISTORY where " +
							"LIC_APP_STATUS_HISTORY.LIC_APP_OID = '" + oid + "'";

						try {
							rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
						}
						catch (Throwable e) {
							if (this.log) {
								this.log.info "ERROR - LIC_APP_STATUS_HISTORY - dmLicAppPurgeByAccount!";
								this.log.info "ERROR - LIC_APP_STATUS_HISTORY - sql[" + this.sql + "]";
							}
						}
						finally {
							if (this.log) {
								this.log.info "SUCCESS - LIC_APP_STATUS_HISTORY - dmLicAppPurgeByAccount!";
								this.log.info "SUCCESS - LIC_APP_STATUS_HISTORY - sql[" + this.sql + "]";
							}
							rowCnt+=rc;
						}
						
						
						/*
						*  LIC_APP_MEDIA_PROFILE Table
						*/
						this.sql = "DELETE from LIC_APP_MEDIA_PROFILE where " +
							"LIC_APP_MEDIA_PROFILE.LIC_APP_OID = '" + oid + "'";

						try {
							rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
						}
						catch (Throwable e) {
							if (this.log) {
								this.log.info "ERROR - LIC_APP_MEDIA_PROFILE - dmLicAppPurgeByAccount!";
								this.log.info "ERROR - sql[" + this.sql + "]";
							}
						}
						finally {
							if (this.log) {
								this.log.info "SUCCESS - LIC_APP_MEDIA_PROFILE - dmLicAppPurgeByAccount!";
								this.log.info "SUCCESS - sql[" + this.sql + "]";
							}
							rowCnt+=rc;
						}
						
						/*
						*  LICAPP_DRMCLNT_ATTESTATION Table
						*/
						this.sql = "DELETE from LICAPP_DRMCLNT_ATTESTATION where " +
							"LICAPP_DRMCLNT_ATTESTATION.LIC_APP_OID = '" + oid + "'";

						try {
							rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
						}
						catch (Throwable e) {
							if (this.log) {
								this.log.info "ERROR - LICAPP_DRMCLNT_ATTESTATION - dmLicAppPurgeByAccount!";
								this.log.info "ERROR - sql[" + this.sql + "]";
							}
						}
						finally {
							if (this.log) {
								this.log.info "SUCCESS - LICAPP_DRMCLNT_ATTESTATION - dmLicAppPurgeByAccount!";
								this.log.info "SUCCESS - sql[" + this.sql + "]";
							}
							rowCnt+=rc;
						}
						
						/*
						*  LIC_APP_ASSERTION
						*/
						this.sql = "DELETE from LIC_APP_ASSERTION where " +
							"LIC_APP_ASSERTION.LIC_APP_OID = '" + oid + "'";

						try {
							rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
						}
						catch (Throwable e) {
							if (this.log) {
								this.log.info "ERROR - LIC_APP_ASSERTION - dmLicAppPurgeByAccount!";
								this.log.info "ERROR - sql[" + this.sql + "]";
							}
						}
						finally {
							if (this.log) {
								this.log.info "SUCCESS - LIC_APP_ASSERTION - dmLicAppPurgeByAccount!";
								this.log.info "SUCCESS - sql[" + this.sql + "]";
							}
							rowCnt+=rc;
						}
						
						/*
						*  LIC_APP Table
						*/
						this.sql = "DELETE from LIC_APP where " +
							"LIC_APP.LIC_APP_OID = '" + oid + "'";

						try {
							rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
						}
						catch (Throwable e) {
							if (this.log) {
								this.log.info "ERROR - LIC_APP - dmLicAppPurgeByAccount!";
								this.log.info "ERROR - sql[" + this.sql + "]";
							}
						}
						finally {
							if (this.log) {
								this.log.info "SUCCESS - LIC_APP - dmLicAppPurgeByAccount!";
								this.log.info "SUCCESS - sql[" + this.sql + "]";
							}
							rowCnt+=rc;
						}
					}
				}
			}
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			return rowCnt;
		}
	}
	
	/**
	* Deletes the/all DRM_DOMAIN_LIST rows associated with
	* the ACCOUNT_ID provided.
	* @param String accountoid represent the DRM_DOMAIN_LIST.ACCOUNT_OID to action against.
	* @return int Total number of rows deleted from all tables.
	*  
	*/
	def dmLicAppPurgeDRMDomainList(String accountoid) { 
		int rc = 0;
		String oid = "";
		
		this.sql = "DELETE from DRM_DOMAIN_LIST where " +
			"DRM_DOMAIN_LIST.ACCOUNT_OID = '" + accountoid + "'";

		try {
			rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
		}
		catch (Throwable e) {
			if (this.log) {
				this.log.info "ERROR - DRM_DOMAIN_LIST - dmLicAppPurgeDRMDomainList!";
				this.log.info "ERROR - DRM_DOMAIN_LIST - sql[" + this.sql + "]";
			}
		}
		finally {
			if (this.log) {
				this.log.info "SUCCESS - DRM_DOMAIN_LIST - dmLicAppPurgeDRMDomainList!";
				this.log.info "SUCCESS - DRM_DOMAIN_LIST - sql[" + this.sql + "]";
			}
			return rc;
		}
	}
}


