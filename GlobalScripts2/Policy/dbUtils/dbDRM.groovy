package dbUtils; 
/**
 * Provides Utilities to Interact with DRM Coordinator Tables.
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
   	4/20/11: Establishment of class.  pjg
	4/22/11 : updated drmGetDRMClientRow for extracting DRM_ID Jeet

-----------------------------------------------------------
*/


import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbDRM extends dbUtils 
{
	def sql = null;
	def log = null;
	def dbIdentifier = null;
	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>DRM<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbDRM(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
		this.dbIdentifier = dbIdentifier;
	} 
	
	/*
	*  GET Methods
	*/
	
	/**
	* Extract the DRM_CLIENT row associated with the DRM_CLIENT_OID 
	* supplied. 
	*
	* @param oid The DRM_CLIENT.DRM_CLIENT_OID to extract. 
	* @return XML encapsulated DRM_CLIENT row.
	*/
	def drmGetDRMClientRow(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(DRM_CLIENT.DRM_CLIENT_OID)) DRM_CLIENT_OID, " +
		"DRM_CLIENT.DECE_DOMAIN_ID, " +
		"DRM_CLIENT.NATIVE_DRM_CLIENT_ID, " +
		"to_char(rawtohex(DRM_CLIENT.DEVICE_OID)) DEVICE_OID, " +
		"to_char(rawtohex(DRM_CLIENT.CREATED_BY)) CREATED_BY, " +
		"DRM_CLIENT.CREATED_DATE, " +
		"to_char(rawtohex(DRM_CLIENT.UPDATED_BY)) UPDATED_BY, " +
		"DRM_CLIENT.UPDATED_DATE, " +
		"DRM_CLIENT.STATUS, " +
		"DRM_CLIENT.DRM_OID, " +
		"DRM_DATA_REF.DRM_ID "  +
		"from DRM_CLIENT, DRM_DATA_REF where " +
		"DRM_CLIENT.DRM_OID = DRM_DATA_REF.DRM_OID " +
		"and DRM_CLIENT_OID  = '" + oid + "'";

		xmlData = this.selectFromDB(this.sql);
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "drmGetDRMClientRow: sql is " + this.sql;
					this.log.info "drmGetDRMClientRow: xmlData.length() is " + xmlData.length();
					this.log.info "drmGetDRMClientRow: xmlData is $xmlData";
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
	* Extract the DRM_CLIENT History row associated with the DRM_CLIENT_OID 
	* supplied. 
	*
	* @param oid The DRM_CLIENT_STATUS_HISTORY.DRM_CLIENT_OID to extract. 
	* @return XML encapsulated DRM_CLIENT_STATUS_HISTORY rows ordered by CREATED_DATE.
	*/
	def drmGetDRMClientHistoryRow(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(HISTORY_OID)) HISTORY_OID, " +
		"to_char(rawtohex(DRM_CLIENT_OID)) DRM_CLIENT_OID, " +
		"STATUS, " +
		"DESCRIPTION, " + 
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID " +
		"from DRM_CLIENT_STATUS_HISTORY where DRM_CLIENT_OID  = '" + oid + "' " + 
		"ORDER BY CREATED_DATE DESC";

		xmlData = this.selectFromDB(this.sql);
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "drmGetDRMClientHistoryRow: sql is " + this.sql;
					this.log.info "drmGetDRMClientHistoryRow: xmlData.length() is " + xmlData.length();
					this.log.info "drmGetDRMClientHistoryRow: xmlData is $xmlData";
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
	
	/*
	*	SET Methods
	*/
	
	/**
	* Deletes the/all DRM_CLIENT rows associated with
	* the DECE_DOMAIN_ID provided.
	* @param String deceDomainId represent the DRM_CLIENT.DECE_DOMAIN_ID to action against.
	* @return int Total number of rows deleted from table(s).
	*  
	*/
	def drmClientDeleteByDECEDomainId(String deceDomainId) { 
		int rc = 0;
		int rowCnt = 0;
		String oid = "";
				
		this.sql = "SELECT to_char(rawtohex(DEVICE_OID)) DEVICE_OID from DRM_CLIENT where " +
			"DECE_DOMAIN_ID = '" + deceDomainId + "'";
		
		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size >= 1) 
				{
					rowData.each {
						oid = it.DEVICE_OID;	

						/*
						*  DEVICE TABLE
						*/
						this.sql = "DELETE from DEVICE where " +
							"DEVICE_OID = '" + oid + "'";
							
						try {
							rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
						}
						catch (Throwable e) {
							if (this.log) {
								this.log.info "ERROR - DRM_DOMAIN_LIST - drmClientDeleteByDECEDomainId!";
								this.log.info "ERROR - DRM_DOMAIN_LIST - sql[" + this.sql + "]";
							}					
						}
						finally {
							if (this.log) {
								this.log.info "SUCCESS - DRM_DOMAIN_LIST - drmClientDeleteByDECEDomainId!";
								this.log.info "SUCCESS - DRM_DOMAIN_LIST - sql[" + this.sql + "]";
							}
							rowCnt+=rc;
						}
						
						/*
						*  DEVICE_STATUS_HISTORY TABLE
						*/
						this.sql = "DELETE from DEVICE_STATUS_HISTORY where " +
							"DEVICE_OID = '" + oid + "'";
							
						try {
							rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
						}
						catch (Throwable e) {
							if (this.log) {
								this.log.info "ERROR - DEVICE_STATUS_HISTORY - drmClientDeleteByDECEDomainId!";
								this.log.info "ERROR - DEVICE_STATUS_HISTORY - sql[" + this.sql + "]";
							}					
						}
						finally {
							if (this.log) {
								this.log.info "SUCCESS - DEVICE_STATUS_HISTORY - drmClientDeleteByDECEDomainId!";
								this.log.info "SUCCESS - DEVICE_STATUS_HISTORY - sql[" + this.sql + "]";
							}
							rowCnt+=rc;
						}
					}
				}
			}
		}
		catch (Throwable e) {
			if (this.log) {
				this.log.info "ERROR - DRM_CLIENT - drmClientDeleteByDECEDomainId!";
				this.log.info "ERROR - DRM_CLIENT - sql[" + this.sql + "]";
			}
		}	
		finally {
			if (this.log) {
				this.log.info "SUCCESS - DRM_CLIENT - drmClientDeleteByDECEDomainId!";
				this.log.info "SUCCESS - DRM_CLIENT - sql[" + this.sql + "]";
			}
			rowCnt+=rc;
		}
				
		/*
		*  DRM_DOMAIN_LIST
		*/
		this.sql = "DELETE from DRM_DOMAIN_LIST where DECE_DOMAIN_ID = '" + deceDomainId + "'";
					
		try {
				rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
		}
		catch (Throwable e) {
			if (this.log) {
				this.log.info "ERROR - DRM_DOMAIN_LIST - drmClientDeleteByDECEDomainId!";
				this.log.info "ERROR - DRM_DOMAIN_LIST - sql[" + this.sql + "]";
			}
		}
		finally {
			if (this.log) {
				this.log.info "SUCCESS - DRM_DOMAIN_LIST - drmClientDeleteByDECEDomainId!";
				this.log.info "SUCCESS - DRM_DOMAIN_LIST - sql[" + this.sql + "]";
			}
			rowCnt+=rc;
		}
				
		/*
		*  DRM_CLIENT
		*/
		this.sql = "DELETE from DRM_CLIENT where " +
			"DRM_CLIENT.DECE_DOMAIN_ID = '" + deceDomainId + "'";

		try {
			rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
		}
		catch (Throwable e) {
			if (this.log) {
				this.log.info "ERROR - DRM_CLIENT - drmClientDeleteByDECEDomainId!";
				this.log.info "ERROR - DRM_CLIENT - sql[" + this.sql + "]";
			}
		}
		finally {
			if (this.log) {
				this.log.info "SUCCESS - DRM_CLIENT - drmClientDeleteByDECEDomainId!";
				this.log.info "SUCCESS - DRM_CLIENT - sql[" + this.sql + "]";
			}
			rowCnt+=rc;
		}
		
		return rowCnt;
	}
	
	/**
	* Sets the DRM_CLIENT.STATUS column to the value supplied in 'value' for the DRM_CLIENT_OID
	* supplied in drmclientoid.
	* @param String deceDomainId represent the DRM_CLIENT.DRM_CLIENT_OID to action against.
	* @return int Total number of rows deleted from table(s).
	*  
	*/
	def drmClientSetStatus(String drmclientoid, String status) { 
		int rc = 0;
		
		this.sql = "UPDATE DRM_CLIENT set STATUS = '" + status + "' " +
			"where DRM_CLIENT_OID = '" + drmclientoid + "'";

		try {
			rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
		}
		catch (Throwable e) {
			if (this.log) {
				this.log.info "ERROR - DRM_CLIENT - drmClientSetStatus!";
				this.log.info "ERROR - DRM_CLIENT - sql[" + this.sql + "]";
			}
		}
		finally {
			if (this.log) {
				this.log.info "SUCCESS - DRM_CLIENT - drmClientSetStatus!";
				this.log.info "SUCCESS - DRM_CLIENT - sql[" + this.sql + "]";
			}
			return rc;
		}
	}
}


