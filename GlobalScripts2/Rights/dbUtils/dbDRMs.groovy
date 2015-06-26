package dbUtils; 
/**
 * Provides Utilities to Interact with DRMs.
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
   	1/11/11: Establishment of class.  AMB
		 Count rows in drm_client_status_history for a DRMClient_OID.
		 Count rows in device_status_history for a DRMClient_OID.

-----------------------------------------------------------
*/


import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbDRMs extends dbUtils 
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
	dbDRMs() {
	    super();   
	}
	/**
	*  Creates a new <code>dbStream<code> object with
	*  ability to write to the log.
	*/
	dbDRMs(log) {
	    super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbStream<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbDRMs(log, dbIdentifier) {
			
super(log, dbIdentifier);
		this.log = log;
	} 

	/**
	* Returns the number of drm_client_history rows that are  
	* associated to the drm_client supplied via drmclientoid.
	*
	* @param drmclientoid The DRM_CLIENT_OID to target. 
	* @return Int of number of drm_client_history rows associated with the DRM_CLIENT_OID supplied.
	*  
	*/
	def drmGetDRMClientHistoryRowCount(String drmclientoid) { 
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
						this.log.info "drmGetDRMClientHistoryRowCount: sql is " + this.sql;
						this.log.info "drmGetDRMClientHistoryRowCount: rowData.size is " + rowData.size;
						this.log.info "drmGetDRMClientHistoryRowCount: rowData[0] is " + rowData[0];
						this.log.info "drmGetDRMClientHistoryRowCount: cnt is " + cnt; 
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
	def drmGetDeviceStatusHistoryRowCount(String deviceoid) { 
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
						this.log.info "drmGetDeviceStatusHistoryRowCount: sql is " + this.sql;
						this.log.info "drmGetDeviceStatusHistoryRowCount: rowData.size is " + rowData.size;
						this.log.info "drmGetDeviceStatusHistoryRowCount: rowData[0] is " + rowData[0];
						this.log.info "drmGetDeviceStatusHistoryRowCount: cnt is " + cnt; 
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
}
