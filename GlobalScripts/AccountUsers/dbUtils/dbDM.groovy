package dbUtils; 
/**
 * Provides Utilities to Interact with DM (Domain Manager) DB.
 * 
 * @author Al Braza
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
   	3/31/11: Establishment of class.  AMB
		 Count rows in drm_client_status_history for a DRMClient_OID.
		 Count rows in device_status_history for a Device_OID.
	04/11:  Added lots of APIs for DECEDomain and DRMDomain APIs

-----------------------------------------------------------
*/


import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbDM extends dbUtils 
{
	def sql = null;
	def log = null;
	def dbIdentifier = null;
	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbStream<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbDM(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
		this.dbIdentifier = dbIdentifier;
	} 

	/**
	* Returns the number of drm_client_history rows that are  
	* associated to the drm_client supplied via drmclientoid.
	*
	* @param drmclientoid The DRM_CLIENT_OID to target. 
	* @return Int of number of drm_client_history rows associated with the DRM_CLIENT_OID supplied.
	*  
	*/
	def dmGetDRMClientHistoryRowCount(String drmclientoid) { 
		String cnt = "";

		this.sql = "select count(*) from drm_client_status_history where drm_client_oid = '" + drmclientoid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					cnt = rowData[0];

					if (this.log) 
					{
						this.log.info "dmGetDRMClientHistoryRowCount: sql is " + this.sql;
						this.log.info "dmGetDRMClientHistoryRowCount: rowData.size is " + rowData.size;
						this.log.info "dmGetDRMClientHistoryRowCount: rowData[0] is " + rowData[0];
						this.log.info "dmGetDRMClientHistoryRowCount: cnt is " + cnt; 
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
			return cnt; 
		}
	}


	/**
	* Returns the number of device_status_history rows that are  
	* associated to the device id supplied via device_oid.
	*
	* @param deviceoid The DEVICE_OID to target. 
	* @return Int of number of device_status_history rows associated with the DEVICE_OID supplied.
	*  
	*/
	def dmGetDeviceStatusHistoryRowCount(String deviceoid) { 
		String cnt = "";

		this.sql = "select count(*) from device_status_history where device_oid = '" + deviceoid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					cnt = rowData[0];

					if (this.log) 
					{
						this.log.info "dmGetDeviceStatusHistoryRowCount: sql is " + this.sql;
						this.log.info "dmGetDeviceStatusHistoryRowCount: rowData.size is " + rowData.size;
						this.log.info "dmGetDeviceStatusHistoryRowCount: rowData[0] is " + rowData[0];
						this.log.info "dmGetDeviceStatusHistoryRowCount: cnt is " + cnt; 
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
			return cnt; 
		}
	}
	
	/*
	*  DECE_DOMAIN Table Methods
	*/
	
	/**
	* Sets the the DECE_DOMAIN.STATUS column associated with the ACCOUNT_ID provided
	* to the status provided.
	*
	* @param oid accountid represent the DECE_DOMAIN.ACCOUNT_ID to invoke against.
	* @param status String to populate DECE_DOMAIN.STATUS with.
	* @return Number of rows updated.
	*  
	*/
	def dmDECEDomainSetStatus(String oid,String status) {
		int rc = 0;

		this.sql = "update DECE_DOMAIN set STATUS = '" + status + "' where DECE_DOMAIN_OID  = '" + oid + "'";
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "dmDECEDomainSetStatus: accountid is "  + accountid;
					this.log.info "dmDECEDomainSetStatus: rc is "  + rc;
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
	* Returns the DECE_DOMAIN row associated with the DECE_DOMAIN_OID provided.
	*
	* @param String accountid represent the DECE_DOMAIN.DECE_DOMAIN_OID to count against.
	* @return String xml formatted DECE_DOMAIN row.
	*  
	*/
	def dmDECEDomainGetRow(String oid) {
		//  Returns the stream's row.
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(DECE_DOMAIN_OID)) DECE_DOMAIN_OID, " +
		"ACCOUNT_ID, " +
		"STATUS, " +
		"CREATED_DATE, " +
		"UPDATED_DATE " +
		"from DECE_DOMAIN where DECE_DOMAIN_OID  = '" + oid + "'";
		
		xmlData = this.SelectFromDB(this.sql);
		
		log.info "xmlData is " + xmlData;
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "dmDECEDomainGetRow: sql is " + this.sql;
					this.log.info "dmDECEDomainGetRow: xmlData.length() is " + xmlData.length();
					this.log.info "dmDECEDomainGetRow: xmlData is $xmlData";
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
	* Returns the DECE_DOMAIN_STATUS_HISTORY row associated with the DECE_DOMAIN_OID provided.
	*
	* @param String accountid represent the DECE_DOMAIN_STATUS_HISTORY.DECE_DOMAIN_OID to count against.
	* @return String xml formatted DECE_DOMAIN_STATUS_HISTORY row.
	*  
	*/
	def dmDECEDomainGetHistoryRow(String oid) {
		//  Returns the stream's row.
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(DECE_DOMAIN_STATUS_HISTORY_OID)) DECE_DOMAIN_STATUS_HISTORY_OID, " +
		"to_char(rawtohex(DECE_DOMAIN_OID)) DECE_DOMAIN_OID, " +
		"STATUS, " +
		"DESCRIPTION, " +
		"CREATED_DATE " +
		"from DECE_DOMAIN_STATUS_HISTORY where DECE_DOMAIN_OID  = '" + oid + "'";
		
		xmlData = this.SelectFromDB(this.sql);
		
		log.info "xmlData is " + xmlData;
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "dmDECEDomainGetHistoryRow: sql is " + this.sql;
					this.log.info "dmDECEDomainGetHistoryRow: xmlData.length() is " + xmlData.length();
					this.log.info "dmDECEDomainGetHistoryRow: xmlData is $xmlData";
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
	* Returns the number of DECE_DOMAIN rows associated with the ACCOUNT_ID provided.
	*
	* @param String accountid represent the DECE_DOMAIN.ACCOUNT_ID to count against.
	* @return Int of number of DECE_DOMAIN rows associated with the DECE_DOMAIN.ACCOUNT_ID supplied.
	*  
	*/
	def dmDECEDomainRowCount(String accountid) { 
		String cnt = "";

		this.sql = "select count(*) as CNT from DECE_DOMAIN where ACCOUNT_ID = '" + accountid + "'";

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
						this.log.info "dmDECEDomainRowCount: sql is " + this.sql;
						this.log.info "dmDECEDomainRowCount: rowData.size is " + rowData.size;
						this.log.info "dmDECEDomainRowCount: rowData[0] is " + rowData[0];
						this.log.info "dmDECEDomainRowCount: cnt is " + cnt; 
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
	* Returns the number of DECE_DOMAIN_STATUS_HISTORY rows associated with the DECE_DOMAIN_OID provided.
	*
	* @param String oid represent the DECE_DOMAIN_STATUS_HISTORY.DECE_DOMAIN_OID to count against.
	* @return Int of number of DECE_DOMAIN_STATUS_HISTORY rows associated with the DECE_DOMAIN_STATUS_HISTORY.DECE_DOMAIN_OID supplied.
	*  
	*/
	def dmDECEDomainHistoryRowCount(String oid) { 
		String cnt = "";

		this.sql = "select count(*) as CNT from DECE_DOMAIN_STATUS_HISTORY where DECE_DOMAIN_OID = '" + oid + "'";

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
						this.log.info "dmDECEDomainHistoryRowCount: sql is " + this.sql;
						this.log.info "dmDECEDomainHistoryRowCount: rowData.size is " + rowData.size;
						this.log.info "dmDECEDomainHistoryRowCount: rowData[0] is " + rowData[0];
						this.log.info "dmDECEDomainHistoryRowCount: cnt is " + cnt; 
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
	* Sets the the DECE_DOMAIN.ACCOUNT_ID column associated with the DECE_DOMAIN_OID provided
	* to the ACCOUNT_ID value provided in value.
	*
	* @param oid Represent the DECE_DOMAIN.DECE_DOMAIN_OID to invoke against.
	* @param value String to populate DECE_DOMAIN.ACCOUNT_ID with.
	* @return Number of rows updated.
	*  
	*/
	def dmDECEDomainSetAccountId(String oid,String value) {
		int rc = 0;

		this.sql = "update DECE_DOMAIN set ACCOUNT_ID = '" + value + "' where DECE_DOMAIN_OID  = '" + oid + "'";
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "dmDECEDomainSetAccountId: oid is "  + oid;
					this.log.info "dmDECEDomainSetAccountId: value is "  + value;

					this.log.info "dmDECEDomainSetAccountId: rc is "  + rc;
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
	* Deletes the/all DECE_DOMAIN and DECE_DOMAIN_STATUS_HISTORY row associated with
	* the ACCOUNT_ID provided.
	* @param String accountid represent the DECE_DOMAIN and DECE_DOMAIN_STATUS_HISTORY to action against.
	* @return int of number of DECE_DOMAIN and DECE_DOMAIN_STATUS_HISTORY rows deleted.
	*  
	*/
	def dmDECEDomainDeleteRowsByCount(String accountid) { 
		int rc = 0;
		int rowCnt = 0;
		String oid = "";
		String triggeroid = "";
				
		try
		{	
			this.sql = "select to_char(rawtohex(DECE_DOMAIN_OID)) DECE_DOMAIN_OID from DECE_DOMAIN where ACCOUNT_ID = '" + accountid + "'";

			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size >= 1) 
				{
					rowData.each {
						oid = it.DECE_DOMAIN_OID;
						
						this.log.info "dmDECEDomainDeleteRowsByCount: oid is $oid";
						
						this.sql = "select to_char(rawtohex(DRM_DOMAIN_OID))DRM_DOMAIN_OID from DRM_DOMAIN " + 
							" where DECE_DOMAIN_OID = '" + oid + "'";
						
						def rowTriggerData = this.dbConnExecuteQuery(this.sql);
						
						if(!rowTriggerData.isEmpty()) {
							rowTriggerData.each {
							
								/*
								*  DRM_CLIENT_TRIGGER
								*/
								triggeroid = it.DRM_DOMAIN_OID;
								
								this.sql = "DELETE from DRM_DOMAIN_STATUS_HISTORY where " +
										"DRM_DOMAIN_OID = '" + 	triggeroid + "'";
											
								rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
								
								rowCnt+=rc;
								
								this.sql = "select to_char(rawtohex(DRM_CLIENT_TRIGGER_OID)) DRM_CLIENT_TRIGGER_OID " +
									"from DRM_CLIENT_TRIGGER where DRM_DOMAIN_OID = '" + triggeroid + "'";
								
								def rowTriggerDataReg = this.dbConnExecuteQuery(this.sql);
								
								if(!rowTriggerDataReg.isEmpty()) {
									rowTriggerDataReg.each {
										def ctd = it.DRM_CLIENT_TRIGGER_OID;

										this.sql = "DELETE from DRM_CLIENT_TRIGGER_DATA where " +
											"DRM_CLIENT_TRIGGER_OID = '" + 	ctd + "'";
											
										rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
										
										rowCnt+=rc;										
										
										this.sql = "DELETE from DRM_CLIENT_TRIGGER where " +
											"DRM_CLIENT_TRIGGER_OID = '" + 	ctd + "'";
											
										rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);
										
										rowCnt+=rc;


															
									}
								}
							
							} 
						}
						
						/*
						*
						*  DECE_DOMAIN_STATUS_HISTORY Table
						*/
						this.sql = "DELETE from DECE_DOMAIN_STATUS_HISTORY where DECE_DOMAIN_OID = '" + oid + "'";

						rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);

						if(rc != 0) 
						{
							if (this.log) 
							{
								this.log.info "dmDECEDomainDeleteRowsByCount: accountid is "  + accountid;
								this.log.info "dmDECEDomainDeleteRowsByCount: rc is "  + rc;
							}
						} 
						
						rowCnt+=rc;
						
				
				
	 					/*
						*  DRM_DOMAIN Table
						*/
						this.sql = "DELETE from DRM_DOMAIN where DRM_DOMAIN.DECE_DOMAIN_OID = '" + oid + "'";

						rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);

						if(rc != 0) 
						{
							if (this.log) 
							{
								this.log.info "dmDECEDomainDeleteRowsByCount: accountid is "  + accountid;
								this.log.info "dmDECEDomainDeleteRowsByCount: rc is "  + rc;
							}
						} 

						rowCnt+=rc;
						
						/*
						*  DECE_DOMAIN Table
						*/
						this.sql = "DELETE from DECE_DOMAIN where DECE_DOMAIN_OID = '" + oid + "'";
			
						rowCnt+=rc;

							rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);

						if(rc != 0) 
						{
							if (this.log) 
							{
								this.log.info "dmDECEDomainDeleteRowsByCount: rc is "  + rc;
							}
						} 
			
						rowCnt+=rc;
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
	* Returns the different between TOD and DECE_DOMAIN.CREATED_DATE 
	* for DECE_DOMAIN_OID supplied. 
	*
	* @param oid The DECE_DOMAIN.DECE_DOMAIN_OID to extract. 
	* @return Number of milliseconds difference
	*
	*/
	def dmDECEDomainDiffCdateToTOD(String oid,String table = null) { 
	
		int rc = 0;	
		Float g = 0.0;
		String r = "";
		
		if (table == "") {
			table = "DECE_DOMAIN";
		} else {
			table = "DECE_DOMAIN_STATUS_HISTORY";
		}
		

		this.sql = "SELECT (extract(DAY FROM sysdate - CREATED_DATE)*24*60*60)+ " + 
			"(extract(HOUR FROM sysdate - CREATED_DATE)*60*60)+" +
			"(extract(MINUTE FROM sysdate - CREATED_DATE)*60)+" +
			"extract(SECOND FROM sysdate - CREATED_DATE)" +
			"as sec FROM " + table + " where DECE_DOMAIN_OID = '" + oid + "'" +
			" and STATUS = 'active'";

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
						this.log.info "dmDECEDomainDiffCdateToTOD: g [$g]";
						this.log.info "dmDECEDomainDiffCdateToTOD: rowData.size is " + rowData.size;
						this.log.info "dmDECEDomainDiffCdateToTOD: rowData[0] is " + rowData[0];
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
	
	/*
	*  DRM_DOMAIN APIS
	*/
	
	/**
	* Returns the DRM_DOMAIN row associated with the DRM_ID and DRM_DM_ID provided.
	*
	* @param String drmid represent the DRM_DOMAIN.DRM_ID to count against.
	* @param String drmdmid represent the DRM_DOMAIN.DRM_DM_ID to count against.
	* @return String xml formatted DRM_DOMAIN row.
	*  
	*/
	def dmDRMDomainGetRow(String drmid, String drmdmid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(DRM_DOMAIN_OID)) DRM_DOMAIN_OID, " +
		"to_char(rawtohex(DECE_DOMAIN_OID)) DECE_DOMAIN_OID, " +
		"DRM_ID, " +
		"DRM_DM_ID, " +
		"utl_raw.cast_to_varchar2(dbms_lob.substr(DRM_KEY)) DRM_KEY, " +
		"STATUS, " +
		"CREATED_DATE " +
		"from DRM_DOMAIN where DRM_ID  = '" + drmid + "'" +
		"and DRM_DM_ID = '" + drmdmid + "'";
		
		xmlData = this.SelectFromDB(this.sql);
		
		log.info "xmlData is " + xmlData;
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "dmDRMDomainGetRow: sql is " + this.sql;
					this.log.info "dmDRMDomainGetRow: xmlData.length() is " + xmlData.length();
					this.log.info "dmDRMDomainGetRow: xmlData is $xmlData";
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
	* Returns the DRM_DOMAIN rows associated with the DECE_DOMAIN_OID rovided.
	*
	* @param String domainoid represent the DRM_DOMAIN.DECE_DOMAIN_OID to count against.
	* @return String xml formatted DRM_DOMAIN rows.
	*  
	*/
	def dmDRMDomainGetRows(String domainoid) {
		String xmlData = "";
		
		this.sql = "select " +
		"to_char(rawtohex(DRM_DOMAIN_OID)) DRM_DOMAIN_OID, " +
		"to_char(rawtohex(DECE_DOMAIN_OID)) DECE_DOMAIN_OID, " +
		"DRM_ID, " +
		"DRM_DM_ID, " +
		"utl_raw.cast_to_varchar2(dbms_lob.substr(DRM_KEY)) DRM_KEY, " +
		"STATUS, " +
		"CREATED_DATE " +
		"from DRM_DOMAIN where DECE_DOMAIN_OID  = '" + domainoid + "' " +
		"and STATUS = 'active'";
		
		xmlData = this.SelectFromDB(this.sql);
		
		log.info "xmlData is " + xmlData;
		
		try
		{	if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "dmDRMDomainGetRows: sql is " + this.sql;
					this.log.info "dmDRMDomainGetRows: xmlData.length() is " + xmlData.length();
					this.log.info "dmDRMDomainGetRows: xmlData is $xmlData";
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
	* Returns the number of DRM_DOMAIN.STATUS associated with the DRM_DM_ID provided.
	*
	* @param String key represent the DRM_DOMAIN.DRM_DM_ID to count against.
	* @return String the DRM_DOMAIN.STATUS column value.
	*  
	*/
	def dmDRMDomainGetStatus(String drmdmid) { 
		String stat = "";

		this.sql = "select STATUS from DRM_DOMAIN where DRM_DM_ID = '" + drmdmid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					stat = rowData[0].STATUS;

					if (this.log) 
					{
						this.log.info "dmDRMDomainGetStatus: rowData[0] is " + rowData[0];
						this.log.info "dmDRMDomainGetStatus: stat is " + stat; 
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
			return stat; 
		}
	}
	
	/**
	* Returns the different between TOD and DRM_DOMAIN.CREATED_DATE 
	* for DRM_ID and DRM_DM_ID supplied. 
	*
	* @param String drmid represent the DRM_DOMAIN.DRM_ID to count against.
	* @param String drmdmid represent the DRM_DOMAIN.DRM_DM_ID to count against.
	* @return Number of milliseconds difference
	*
	*/
	def dmDRMDomainDiffCdateToTOD(String drmid, String drmdmid) { 
	
		int rc = 0;	
		Float g = 0.0;
		String r = "";

		this.sql = "SELECT (extract(DAY FROM sysdate - CREATED_DATE)*24*60*60)+ " + 
			"(extract(HOUR FROM sysdate - CREATED_DATE)*60*60)+" +
			"(extract(MINUTE FROM sysdate - CREATED_DATE)*60)+" +
			"extract(SECOND FROM sysdate - CREATED_DATE)" +
			"as sec FROM DRM_DOMAIN where DRM_ID = '" + drmid + "'" +
			" and DRM_DM_ID = '" + drmdmid + "'";

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
						this.log.info "dmDRMDomainDiffCdateToTOD: g [$g]";
						this.log.info "dmDRMDomainDiffCdateToTOD: rowData.size is " + rowData.size;
						this.log.info "dmDRMDomainDiffCdateToTOD: rowData[0] is " + rowData[0];
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
	* Returns the number of DRM_DOMAIN rows associated with the 
	* DECE DomainID supplied and the status supplied.
	*
	* @param String decedomainid represent the DRM_DOMAIN.DECE_DOMAIN_ID to count against.
	* @param String status represent the status filter value to supply.
	* @return Number of rows selected.
	*
	*/
	def dmDRMCountDECEDomainStatus(String decedomainid, String status) { 
		int cnt = 0;
		
		this.sql = "SELECT count(*) as CNT from DRM_DOMAIN where " +
			"DRM_DOMAIN.STATUS = '" + status + "' and DECE_DOMAIN_OID = '" +
			decedomainid + "'";

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					cnt = rowData[0].CNT.toInteger();
					if (this.log) 
					{
						this.log.info "dmDRMCountDECEDomainStatus: rowData.size is " + rowData.size;
						this.log.info "dmDRMCountDECEDomainStatus: rowData[0] is " + rowData[0];
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
			return cnt;
		}
	}
	
	
	/**
	* Returns the number of DRM_DOMAIN rows associated with the DECE_DOMAIN_OID provided.
	*
	* @param String oid represent the DRM_DOMAIN.DECE_DOMAIN_OID to count against.
	* @return Int of number of DRM_DOMAIN rows associated with the DRM_DOMAIN.DECE_DOMAIN_OID supplied.
	*  
	*/
	def dmDRMDomainRowCount(String oid) { 
		String cnt = "";

		this.sql = "select count(*) as CNT from DRM_DOMAIN where DECE_DOMAIN_OID = '" + oid + "'";

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
						this.log.info "dmDRMDomainRowCount: sql is " + this.sql;
						this.log.info "dmDRMDomainRowCount: rowData.size is " + rowData.size;
						this.log.info "dmDRMDomainRowCount: rowData[0] is " + rowData[0];
						this.log.info "dmDRMDomainRowCount: cnt is " + cnt; 
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
	* Returns the number of DRM_DOMAIN rows associated with the DRM_DM_ID provided.
	*
	* @param String key represent the DRM_DOMAIN.DRM_DM_ID to count against.
	* @return Int of number of DRM_DOMAIN rows associated with the DRM_DOMAIN.DRM_DM_ID supplied.
	*  
	*/
	def dmDRMDomainRowCountbyDRMID(String key) { 
		String cnt = "";

		this.sql = "select count(*) as CNT from DRM_DOMAIN where DRM_DM_ID = '" + key + "'";

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
						this.log.info "dmDRMDomainRowCountbyDRMID: sql is " + this.sql;
						this.log.info "dmDRMDomainRowCountbyDRMID: rowData.size is " + rowData.size;
						this.log.info "dmDRMDomainRowCountbyDRMID: rowData[0] is " + rowData[0];
						this.log.info "dmDRMDomainRowCountbyDRMID: cnt is " + cnt; 
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
	* Sets the the DRM_DOMAIN.STATUS column associated with the  
	* DRM_ID and DRM_DM_ID provided to the status provided.
	*
	* @param drmid represent the DRM_DOMAIN.DRM_ID to invoke against.
	* @param drmdmid represent the DRM_DOMAIN.DRM_DM_ID to invoke against.
	* @param status String to populate DRM_DOMAIN.STATUS with.
	* @return Number of rows updated.
	*  
	*/
	def dmDRMDomainSetStatus(String drmid, String drmdmid, String status) {
		int rc = 0;

		this.sql = "update DRM_DOMAIN set STATUS = '" + status + "' where " +
			"DRM_ID  = '" + drmid + "' and DRM_DM_ID = '" + drmdmid + "'";
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql, this.dbIdentifier);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "dmDRMDomainSetStatus: accountid is "  + accountid;
					this.log.info "dmDRMDomainSetStatus: rc is "  + rc;
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
}


