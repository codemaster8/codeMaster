package dbUtils;
/**
 * Provides Utilities to Interact with Device Data.
 * 
 * @author Sarvjeet Chhinna
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
   * 4/16/11: Establishment of class.  Jeet 
   * 4/16/11: Added javadoc comments.  Jeet 
   * 4/21/11: Added DeviceGetRowsByDRMClientOID & DeviceSetStatus.  pjg
   * 4/29/11: Added PurgeDeviceByAccountOID amb
   * 5/3/11: Added DEVICE_HISTORY row deletes to PurgeDeviceByAccountOID pjg
   * 5/3/11: Added DeviceDeleteAttestionData. pjg

 
-----------------------------------------------------------
*/
import groovy.sql.Sql;
import java.util.regex.Matcher
import java.util.regex.Pattern

class dbDevice extends dbUtils 
{
	def sql = null;
	def log = null;

	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbDevice<code> object.
	*/
	dbDevice() {
	}
	/**
	*  Creates a new <code>dbDevice<code> object with
	*  ability to write to the log.
	*/
	dbDevice(log) {
		this.log = log;
	}
	/**
	*  Creates a new <code>dbDevice<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbDevice(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	}

	/*
	*   Private methods
	*/
	/**
	*   Private method for constructing SQL
	* @version 1.0
	*/
	private dbSetStatusSQL(String DeviceId, String status) {
		if (DeviceId =~ /urn:dece:org:org:dece:/) {
			this.sql = "update DEVICE set DEVICE_STATUS = '%%STATUS%%' where DEVICE_ID = '%%DEVICEOID%%'";
			this.sql = sql.replaceAll("%%STATUS%%", status);
			this.sql = sql.replaceAll("%%DEVICEOID%%", DeviceId);
		} else {
			this.sql = "update DEVICE set DEVICE_STATUS = '%%STATUS%%' where DEVICE_OID = '%%DEVICEOID%%'";
			this.sql = sql.replaceAll("%%STATUS%%", status);
			this.sql = sql.replaceAll("%%DEVICEOID%%", DeviceId);
		}

		if (this.log) {
			this.log.info "dbSetStatusSQL: SQL is " + this.sql;
		}
	}

	/**
	*   Private method for constructing SQL
	* @version 1.0
	*/
	private dbDoUpdate() {
		int rc = 0;

		try {
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty()) {
				if (this.log) 
				{
					this.log.info "dbDoUpdate: sql is " + this.sql;
					this.log.info "dbDoUpdate: rc is " + rc; 
				}			
			} 
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}


	/*
	*  GET Methods
	*/
	
	/**
	* Extract the DEVICE row associated with the DEVICE_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The DEVICE.DEVICE_OID to extract. 
	* @return XML encapsulated ACCOUNT row.
	*
	*/
	def DeviceGet(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(DEVICE_OID)) DEVICE_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"MODEL, " +
		"DISPLAY_NAME, " +
		"SERIAL_NO, " +
		"IMAGE_URI, " +
		"IMAGE_HEIGHT, " +
		"CREATED_DATE, " +
		"IMAGE_WIDTH, " +
		"IMAGE_MIME_TYPE, " +
		"STATUS, " +
		"MANUFACTURER, " +
		"BRAND, " +
		"BRAND_LANGUAGE_ID, " +
		"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +
		"CREATED_DATE, " +
		"UPDATED_DATE, " +
		"UNVERIFIED_LEAVE_DATE " +
		"from DEVICE where DEVICE_OID  = '" + oid + "'";

		try
		{	
			xmlData = this.SelectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "DeviceGet: sql is " + this.sql;
					this.log.info "DeviceGet: xmlData.length() is " + xmlData.length();
					this.log.info "DeviceGet: xmlData is $xmlData";
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
	* Returns the DEVICE_STATUS_HISTORY rows of the DEVICE_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The DEVICE_OID of the row to extract. 
	* @return XML-formatted DEVICE_STATUS_HISTORY rows. 
	*/
	def DeviceStatusHistoryGetRow(String oid) {
		def rowData;		

		this.sql = "select " +
		"to_char(rawtohex(HISTORY_OID)) HISTORY_OID, " +
		"to_char(rawtohex(DEVICE_OID)) DEVICE_OID, " +
		"STATUS, " +
		"DESCRIPTION, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID " +
		"from " +
		"DEVICE_STATUS_HISTORY where " +
		"DEVICE_OID  = '" + oid + "' ";


		try {
			rowData = SelectFromDB(this.sql);
		}
	    catch(Throwable e) {
			this.log.info e;
			return rowData;
		}
		finally {
			this.dbConnClose();
			return(rowData);
		}
	
	}	

	/**
	* Returns the DRM_CLIENT row of the DEVICE_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The DEVICE_OID of the row to extract. 
	* @return XML-formatted DRM_CLIENT row. 
	*/
	def DeviceDRMClientGetRow(String oid) {
		def rowData;		

		this.sql = "select " +
		"to_char(rawtohex(DRM_CLIENT_OID)) DRM_CLIENT_OID, " +
		"to_char(rawtohex(DEVICE_OID)) DEVICE_OID, " +
		"DECE_DOMAIN_ID, " +
		"NATIVE_DRM_CLIENT_ID, " +
		"DRM_OID, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY)) CREATED_BY, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +
		"UPDATED_DATE, " +
		"STATUS " +
		"from " +
		"DRM_CLIENT where " +
		"DEVICE_OID = '" + oid + "'";

		try {
			rowData = SelectFromDB(this.sql);
		}
		catch(Throwable e) {
			this.log.info e;
			return rowData;
		}
						
		finally {
			this.dbConnClose();
			return(rowData);
		}
	}
	
	/**
	* Returns DEVICE rows associated with the DRM_CLIENT_OID
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The DRM_CLIENT_OID of the row to extract. 
	* @return XML-formatted DEVICE rows. 
	*/
	def DeviceGetRowsByDRMClientOID(String oid) {
		def rowData;		

		this.sql = "select " +
		"to_char(rawtohex(DRM_CLIENT_OID)) DRM_CLIENT_OID, " +
		"to_char(rawtohex(DEVICE_OID)) DEVICE_OID, " +
		"DECE_DOMAIN_ID, " +
		"NATIVE_DRM_CLIENT_ID, " +
		"DRM_OID, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY)) CREATED_BY, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +
		"UPDATED_DATE, " +
		"STATUS " +
		"from " +
		"DRM_CLIENT where " +
		"DRM_CLIENT_OID = '" + oid + "'";

		try {
			rowData = SelectFromDB(this.sql);
		}
		catch(Throwable e) {
			this.log.info e;
		}
		finally {
			this.dbConnClose();
			return(rowData);
		}
	}
	
	/**
	* Extract the LIC_APP.LIC_APP_OID value associated with the DEVICE_OID 
	* supplied. 
	*
	* @param oid The Device.DEVICE_OID to extract. 
	* @return The Device's LIC_APP_OID as a String. 
	* @version 1.0
	*/
	def DeviceGetLIC_APP_OID(String oid) {
		String rData = "";

		this.sql = "SELECT to_char(rawtohex(LIC_APP_OID)) LIC_APP_OID FROM LIC_APP WHERE DEVICE_OID = '" + oid + "'";

		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].LIC_APP_OID;
					if (this.log) 
					{
						this.log.info "DeviceGetLIC_APP_OID: sql is " + this.sql;
						this.log.info "DeviceGetLIC_APP_OID: rowData.size is " + rowData.size;
						this.log.info "DeviceGetLIC_APP_OID: rowData[0] is " + rowData[0];
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
	* Extract the DEVICE_ATTESTATION_DATA row associated with the ATTESTATION_ID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The DEVICE_ATTESTATION_DATA.ATTESTATION_ID to extract. 
	* @return XML encapsulated ACCOUNT row.
	*
	*/
	def DeviceGetAttestion(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(DEVICE_ATTESTATION_DATA_OID)) DEVICE_ATTESTATION_DATA_OID, " +
		"MANUFACTURER, " +
		"MODEL, " +
		"ORGANIZATION_ID, " +
		"ATTESTATION_ID, " +
		"EFFECTIVE_DATE, " +
		"EXPIRATION_DATE, " +
		"DRM_OID, " +
		"APPLICATION, " +
		"STATUS " +
		"from DEVICE_ATTESTATION_DATA where ATTESTATION_ID  = '" + oid + "'";

		try
		{	
			xmlData = this.SelectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "DeviceGetAttestion: sql is " + this.sql;
					this.log.info "DeviceGetAttestion: xmlData.length() is " + xmlData.length();
					this.log.info "DeviceGetAttestion: xmlData is $xmlData";
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
	*  SET Methods
	*/
	/**
	* Sets the the DEVICE.STATUS column associated with the  
	* DEVICE_OID provided to the status provided.
	*
	* @param oid represent the DEVICE.DEVICE_OID to invoke against.
	* @param status String to populate DEVICE.STATUS with.
	* @return Number of rows updated.
	*  
	*/
	def DeviceSetStatus(String oid, String status) {
		int rc = 0;

		this.sql = "update DEVICE set STATUS = '" + status + "' where " +
			"DEVICE_OID  = '" + oid + "'";
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "DeviceSetStatus: oid is "  + oid;
					this.log.info "DeviceSetStatus: status is "  + status;
					this.log.info "DeviceSetStatus: rc is "  + rc;
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

	/*
	*  SET Methods
	*/
	/**
	* Deletes the DEVICE rows associated with the ACCOUNT_OID provided.
	* 
	* @param String accountoid represent the Device.ACCOUNT_OID to action against.
	* @return int Total number of rows deleted from the Device Table.
	*  
	*/
	def PurgeDeviceByAccountOID(String accountoid) { 
		int rc = 0;
		int rowcount = 0;
		String oid = "";
		String deviceoid = "";
		
		
		this.sql = "SELECT to_char(rawtohex(DEVICE_OID)) DEVICE_OID from DEVICE where " +
			"ACCOUNT_OID = '" + accountoid + "'";
		
		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size >= 1) 
				{
					rowData.each {
						deviceoid = it.DEVICE_OID;	
						/*
						*  DEVICE
						*/
						this.sql = "DELETE from DEVICE_STATUS_HISTORY where " + "DEVICE_OID = '" + deviceoid + "'";
						try {
							rc = this.dbConnExecuteUpdate(this.sql);
						}
						catch (Throwable e) {
							this.log.info "ERROR - PurgeDeviceByAccountOID - DEVICE_STATUS_HISTORY - $e";
						}
						finally {
							this.log.info "SUCCESS - PurgeDeviceByAccountOID - DEVICE_STATUS_HISTORY - $deviceoid";
						}
						rowcount+= rc;
						
						/*
						*  DEVICE
						*/
						this.sql = "DELETE from DEVICE where " + "DEVICE_OID = '" + deviceoid + "'";
						try {
							rc = this.dbConnExecuteUpdate(this.sql);
						}
						catch (Throwable e) {
							this.log.info "ERROR - PurgeDeviceByAccountOID - DEVICE - $e";
						}
						finally {
							this.log.info "SUCCESS - PurgeDeviceByAccountOID - DEVICE - $deviceoid";
						}
						rowcount+= rc;

					}
				}
			}
		}
		catch(Throwable e) {
			this.log.info "ERROR - PurgeDeviceByAccountOID - $e";
		}
		finally {
			this.log.info "SUCCESS - PurgeDeviceByAccountOID - COMPLETE";
		}
		
		return rowcount;
	}
	/**
	* Deletes the DEVICE_ATTESTATION_DATA rows associated with the ATTESTATION_ID provided.
	* 
	* @attestionid String accountoid represent the DEVICE_ATTESTATION_DATA.ATTESTATION_ID
	* to action against.
	* @return int Total number of rows deleted from the DEVICE_ATTESTATION_DATA Table.
	*  
	*/
	def DeviceDeleteAttestionData(String attestionid) { 
		int rc = 0;
	
		
		this.sql = "DELETE from DEVICE_ATTESTATION_DATA where ATTESTATION_ID = '" +
			attestionid + "'";
			
		try {
			rc = this.dbConnExecuteUpdate(this.sql);
		}
		catch (Throwable e) {
			this.log.info "ERROR - DeviceDeleteAttestionData - DEVICE_ATTESTATION_DATA - $e";
		}
		finally {
			this.log.info "SUCCESS - PurgeDeviceByAccountOID - DEVICE_STATUS_HISTORY - $attestionid";
		}
	}
}
