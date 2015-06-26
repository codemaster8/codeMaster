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
		* Extract the DEVICE.DEVICE_OID value associated with the LIC_APP_OID 
		* supplied. 
		*
		* @param oid The LIC_APP.LIC_APP_OID  to extract. 
		* @return The Device's DEVICE_OID as a String. 
		* @version 1.0
		* @Author:  Shikha Gupta
		*/
		def LicAppGetDeviceId(String oid) {
			String rData = "";
	
			this.sql = "SELECT to_char(rawtohex(DEVICE_OID)) DEVICE_OID FROM LIC_APP WHERE LIC_APP_OID = '" + oid + "'";
	
			try {
				def rowData = dbConnExecuteQuery(this.sql);
				
				if(!rowData.isEmpty())
				{	
					if (rowData.size == 1) 
					{
						rData = rowData[0].LIC_APP_OID;
						if (this.log) 
						{
							this.log.info "LicAppGetDeviceId: sql is " + this.sql;
							this.log.info "LicAppGetDeviceId: rowData.size is " + rowData.size;
							this.log.info "LicAppGetDeviceId: rowData[0] is " + rowData[0];
						}			
					} 
				} 
			}
			catch(Throwable e) {
				this.log.info e;
			} 
			finally {
				this.dbConnClose();
				return rData;
			}
	}
	
	
		/**
		* Extract the LIC_APP row associated with the LIC_APP_OID 
		* supplied. 
		* @version 1.0
		*
		* @param oid The LIC_APP.LIC_APP_OID to extract. 
		* @return XML encapsulated LIC_APP row.
		*
		*/
		def LicAppGet(String oid) {
			String xmlData = "";
	
			this.sql = "select " +
			"to_char(rawtohex(LIC_APP_OID)) LIC_APP_OID, " +
			"STATUS, " +
			"IMAGE_HEIGHT, " +
			"IMAGE_WIDTH, " +
			"IMAGE_URI, " +
			"IMAGE_MIME_TYPE, " +
			"LIC_APP_HANDLE, " +
			"EMBEDDED, " +
			"APPLICATION, " +
			"DISPLAY_NAME, " +
			"MANUFACTURER, " +
			"MODEL, " +
			"BRAND, " +
			"BRAND_LANGUAGE, " +
			"DECE_DOMAIN_ID, " +
			"SERIAL_NO, " +
			"to_char(rawtohex(CREATING_USER_OID)) CREATING_USER_OID, " +
			"to_char(rawtohex(ACTIVE_USER_OID)) ACTIVE_USER_OID, " +
			"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID, " +
			"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
			"to_char(rawtohex(DEVICE_OID)) DEVICE_OID, " +
			"CREATED_DATE, " +
			"DRM_OID, " +
			"UPDATE_DATE, " +
			"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +			
			"from LIC_APP where LIC_APP_OID  = '" + oid + "'";
	
			try
			{	
				xmlData = this.SelectFromDB(this.sql);
	
				if(xmlData.length() > 0)
				{	
					if (this.log) 
					{
						this.log.info "LicAppGet: sql is " + this.sql;
						this.log.info "LicAppGet: xmlData.length() is " + xmlData.length();
						this.log.info "LicAppGet: xmlData is $xmlData";
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
			* Extract the LIC_APP_STATUS_HISTORY row associated with the LIC_APP_OID 
			* supplied. 
			* @version 1.0
			*
			* @param oid The LIC_APP.LIC_APP_OID to extract. 
			* @return XML encapsulated LIC_APP_STATUS_HISTORY row.
			*
			*/
			
			
			def LicAppGetStatusHistory(String oid) {
				String xmlData = "";
		
				this.sql = "select " +
				"to_char(rawtohex(LIC_APP_OID)) LIC_APP_OID, " +
				"to_char(rawtohex(HISTORY_OID)) HISTORY_OID, " +
				"STATUS, " +
				"DESCRIPTION, " +
				"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID, " +
				"CREATED_DATE, " +
				"from LIC_APP_STATUS_HISTORY where LIC_APP_OID  = '" + oid + "'";
		
				try
				{	
					xmlData = this.SelectFromDB(this.sql);
		
					if(xmlData.length() > 0)
					{	
						if (this.log) 
						{
							this.log.info "LicAppGetStatusHistory: sql is " + this.sql;
							this.log.info "LicAppGetStatusHistory: xmlData.length() is " + xmlData.length();
							this.log.info "LicAppGetStatusHistory: xmlData is $xmlData";
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
	
	/*
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


