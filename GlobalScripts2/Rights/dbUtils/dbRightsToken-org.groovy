package dbUtils;

/**
 * Provides Utilities to Interact with RightsToken data.
 * 
 * @author Pat Gentry
 * @author Sarvjeet Chhinna 
 * @version 1.0
 *
 *
*/


/*    
----------------------------------------------
 C H A N G E     L O G
Version
  Date: Change   Person

1.0
---
  * 1/11/11: Establishment of class.  PJG/Jeet
  * 1/13/11: Added javadoc comments.  PJG
  * 1/18/11: Added javadoc @version at API level.  pjg
  * 1/21/11: Added functions for rights operations. Jeet
  * 1/25/11: Removed all $ variable expansions from SQL. pjg
  * 2/9/11:  Removed all references to dbIdentifier.  pjg
  * 2/22/11: Added  more methods for rights opertions. Jeet
  * 3/26/11: Added Methods for MetaData Query associated with a rightstoken. Jeet
  * 6/13/11: Modified/added new methods for RightsToken APIs. Jeet
----------------------------------------------
*/

import groovy.sql.Sql;

class dbRightsToken extends dbUtils 
{
	def sql = null;
	def sql1 = null;
	def sql2 = null;
	def sql3 = null;	
	def log = null;
	
	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbRightsToken<code> object.
	*
	*/
	dbRightsToken() {
	}
	/**
	*  Creates a new <code>dbRightsToken<code> object with
	*  ability to write to the log.
	*/
	dbRightsToken(log) {
		super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbRightsToken<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbRightsToken(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	}
	/*
	*  Private Methods
	*/
	/**
	*   Private method for constructing SQL for the other
	*   methods in the class.
	*   @version 1.0
	*/
	private dbSetStatusSQL(String rightstoken, String status) {
		this.sql = "update RIGHTS_TOKEN set STATUS_ID = '%%STATUS%%' where RIGHTS_TOKEN_OID = '%%RT%%'";
		this.sql = sql.replaceAll("%%STATUS%%", status);
		this.sql = sql.replaceAll("%%RT%%", rightstoken);
	}
	/**
	*   Private method for constructing SQL for the other
	*   methods in the class.
	*   @version 1.0
	*/
	private dbSetStatusExpireSQL(String rightstoken, String timeStr) {
		this.sql = " update RIGHTS_TOKEN set RENTAL_ABSOLUTE_EXPIRATION = %%TIME%% " + 
			"where RIGHTS_TOKEN_OID = '%%RT%%'";	
		this.sql = sql.replaceAll("%%RT%%",   rightstoken);
		this.sql = sql.replaceAll("%%TIME%%", timeStr);
	}
	/**
	*   Private method for constructing SQL for the other
	*   methods in the class.
	*   @version 1.0
	*/
	private dbSetStreamPurchaseProfileSQL(String rightstoken, String setting) {
		this.sql = " update PURCHASE_PROFILE set STREAM = '%%NY%%' " + 
			"where RIGHTS_TOKEN_OID = '%%RT%%'";	
		this.sql = sql.replaceAll("%%RT%%", rightstoken);
		this.sql = sql.replaceAll("%%NY%%", setting);
	}
	/**
	*   Private method for constructing SQL for the other
	*   methods in the class.
	*   @version 1.0
	*/
	private dbSetDownloadPurchaseProfileSQL(String rightstoken, String setting) {
		this.sql = " update PURCHASE_PROFILE set DOWNLOAD = '%%NY%%' " + 
			"where RIGHTS_TOKEN_OID = '%%RT%%'";	
		this.sql = sql.replaceAll("%%RT%%", rightstoken);
		this.sql = sql.replaceAll("%%NY%%", setting);
	}
	/*
	*  RIGHTSTOKEN Get Methods
	*/
	
	
	/**
	* Returns the RIGHTS_TOKEN row of the RIGHTS_TOKEN_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return XML-formatted RIGHTS_TOKEN row. 
	*/
	def rightstokenGetRow(String oid) {
		def rowData;		
		log.info "Executing 1 DB Query"
		this.sql = "select " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"to_char(rawtohex(USER_OID)) USER_OID, " +
		"to_char(rawtohex(RIGHTS_LOCKER_OID)) RIGHTS_LOCKER_OID, " +
		"to_char(rawtohex(NODE_OID)) NODE_OID, " +
		"to_char(rawtohex(PURCHASE_NODE_ACCOUNT_OID)) PURCHASE_NODE_ACCOUNT_OID, " +
		"RETAILER_TRANSACTION, " +
		"TRANSACTION_TYPE, " +
		"to_char(rawtohex(PURCHASE_NODE_USER_OID)) PURCHASE_NODE_USER_OID, " +
		"PURCHASE_TIME, " +
		"ALID, " +
		"CONTENT_ID, " +
		"RENTAL_ABSOLUTE_EXPIRATION, " +
		"RENTAL_DOWNLOAD_TO_PLAY_MAX, " +
		"RENTAL_PLAY_DURATION_MAX, " +
		"LICENSE_ACQ_BASE_LOC, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY)) CREATED_BY, " +
		"UPDATED_DATE, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +
		"STATUS_ID " +
		"from " +
		"RIGHTS_TOKEN where " +
		"RIGHTS_TOKEN_OID = hextoraw('" + oid + "')";
		
		try {
			rowData = SelectFromDB(this.sql);
			//log.info "Execute the first Query"+SelectFromDB(this.sql);
		}
	    catch(Throwable e) {
			this.log.info e;
			return rowData;
		}
		finally {
			this.dbConnClose();
			log.info "Done Executing 1 DB Query"
			return(rowData);
		}
	}

	/**
	* Returns the RIGHTS_TOKEN rows of the ACCOUNT_OID	
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The ACCOUNT_OID of the righs tokens rows to extract. 
	* @return XML-formatted RIGHTS_TOKEN rows. 
	*/
	def rightstokenGetByAccountHandle(String accountoid) {
		def rowData;		

		this.sql = "select " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"to_char(rawtohex(USER_OID)) USER_OID, " +
		"to_char(rawtohex(RIGHTS_LOCKER_OID)) RIGHTS_LOCKER_OID, " +
		"to_char(rawtohex(NODE_OID)) NODE_OID, " +
		"to_char(rawtohex(PURCHASE_NODE_ACCOUNT_OID)) PURCHASE_NODE_ACCOUNT_OID, " +
		"RETAILER_TRANSACTION, " +
		"to_char(rawtohex(PURCHASE_NODE_USER_OID)) PURCHASE_NODE_USER_OID, " +
		"PURCHASE_TIME, " +
		"ALID, " +
		"CONTENT_ID, " +
		"RENTAL_ABSOLUTE_EXPIRATION, " +
		"RENTAL_DOWNLOAD_TO_PLAY_MAX, " +
		"RENTAL_PLAY_DURATION_MAX, " +
		"LICENSE_ACQ_BASE_LOC, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY)) CREATED_BY, " +
		"UPDATED_DATE, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +
		"STATUS_ID " +
		"from " +
		"RIGHTS_TOKEN where " +
		"ACCOUNT_OID = '" + accountoid + "'";
		
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
	* Returns the RIGHTS_TOKEN rows of the ALID	
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The ALID of the righs tokens rows to extract. 
	* @return XML-formatted RIGHTS_TOKEN rows. 
	*/
	def rightstokenGetByAlid(String alid) {
		def rowData;		

		this.sql = "select " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"to_char(rawtohex(USER_OID)) USER_OID, " +
		"to_char(rawtohex(RIGHTS_LOCKER_OID)) RIGHTS_LOCKER_OID, " +
		"to_char(rawtohex(NODE_OID)) NODE_OID, " +
		"to_char(rawtohex(PURCHASE_NODE_ACCOUNT_OID)) PURCHASE_NODE_ACCOUNT_OID, " +
		"RETAILER_TRANSACTION, " +
		"to_char(rawtohex(PURCHASE_NODE_USER_OID)) PURCHASE_NODE_USER_OID, " +
		"PURCHASE_TIME, " +
		"ALID, " +
		"CONTENT_ID, " +
		"RENTAL_ABSOLUTE_EXPIRATION, " +
		"RENTAL_DOWNLOAD_TO_PLAY_MAX, " +
		"RENTAL_PLAY_DURATION_MAX, " +
		"LICENSE_ACQ_BASE_LOC, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY)) CREATED_BY, " +
		"UPDATED_DATE, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +
		"STATUS_ID " +
		"from " +
		"RIGHTS_TOKEN where " +
		"ALID = '" + alid + "'";
		
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
	* Returns the RIGHTS_TOKEN rows of the ALID	and AccountID
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The ALID and AccountID of the righs tokens rows to extract. 
	* @return XML-formatted RIGHTS_TOKEN rows. 
*/
	def rightstokenGetByAlidinAccount(String alid, String accountoid) {
		def rowData;		

		this.sql = "select " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"to_char(rawtohex(USER_OID)) USER_OID, " +
		"to_char(rawtohex(RIGHTS_LOCKER_OID)) RIGHTS_LOCKER_OID, " +
		"to_char(rawtohex(NODE_OID)) NODE_OID, " +
		"to_char(rawtohex(PURCHASE_NODE_ACCOUNT_OID)) PURCHASE_NODE_ACCOUNT_OID, " +
		"RETAILER_TRANSACTION, " +
		"to_char(rawtohex(PURCHASE_NODE_USER_OID)) PURCHASE_NODE_USER_OID, " +
		"PURCHASE_TIME, " +
		"ALID, " +
		"CONTENT_ID, " +
		"RENTAL_ABSOLUTE_EXPIRATION, " +
		"RENTAL_DOWNLOAD_TO_PLAY_MAX, " +
		"RENTAL_PLAY_DURATION_MAX, " +
		"LICENSE_ACQ_BASE_LOC, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY)) CREATED_BY, " +
		"UPDATED_DATE, " +
		"to_char(rawtohex(UPDATED_BY)) UPDATED_BY, " +
		"STATUS_ID " +
		"from " +
		"RIGHTS_TOKEN where " +
		"ALID = '" + alid + "'" +
		"and " +
		"ACCOUNT_OID = '" + accountoid + "'";
		
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
	* Returns the RIGHTS_TOKEN rows of the APID	and ACCOUNT_OID
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The APID and ACCOUNT_OID of the righs tokens rows to extract. 
	* @return XML-formatted RIGHTS_TOKEN rows. 
	*/
	def rightstokenGetByApid(String apid, String accountoid) {
		def rowData;		

		this.sql = "select " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"to_char(rawtohex(USER_OID)) USER_OID, " +
		"to_char(rawtohex(RIGHTS_LOCKER_OID)) RIGHTS_LOCKER_OID, " +
		"to_char(rawtohex(NODE_OID)) NODE_OID, " +
		"to_char(rawtohex(PURCHASE_NODE_ACCOUNT_OID)) PURCHASE_NODE_ACCOUNT_OID, " +
		"RIGHTS_TOKEN.RETAILER_TRANSACTION, " +
		"to_char(rawtohex(PURCHASE_NODE_USER_OID)) PURCHASE_NODE_USER_OID, " +
		"RIGHTS_TOKEN.PURCHASE_TIME, " +
		"RIGHTS_TOKEN.ALID, " +
		"RIGHTS_TOKEN.CONTENT_ID, " +
		"RIGHTS_TOKEN.RENTAL_ABSOLUTE_EXPIRATION, " +
		"RIGHTS_TOKEN.RENTAL_DOWNLOAD_TO_PLAY_MAX, " +
		"RIGHTS_TOKEN.RENTAL_PLAY_DURATION_MAX, " +
		"RIGHTS_TOKEN.LICENSE_ACQ_BASE_LOC, " +
		"RIGHTS_TOKEN.CREATED_DATE, " +
		"to_char(rawtohex(RIGHTS_TOKEN.CREATED_BY)) CREATED_BY, " +
		"RIGHTS_TOKEN.UPDATED_DATE, " +
		"to_char(rawtohex(RIGHTS_TOKEN.UPDATED_BY)) UPDATED_BY, " +
		"RIGHTS_TOKEN.STATUS_ID " +
		"from " +
		"RIGHTS_TOKEN ,ASSET_MAP_LP ,ASSET_FULFILLMENT_GROUP ,ASSET_APID_GROUP ,ASSET_MAP_APID_GROUP " + 
		"where " +
		"RIGHTS_TOKEN.alid = ASSET_MAP_LP.alid " +
		"and " +
		"ASSET_MAP_LP.MAP_LP_OID = ASSET_FULFILLMENT_GROUP.map_lp_oid " + 
		"and " +
		"ASSET_FULFILLMENT_GROUP.asset_fulfillment_group_oid = ASSET_APID_GROUP.asset_fulfillment_group_oid " +
		"and " +
		"ASSET_APID_GROUP.apid_group_id = ASSET_MAP_APID_GROUP.apid_group_id " + 
		"and " +
		"ASSET_MAP_APID_GROUP.apid = '" + apid + "'" +
		"and " +
		"RIGHTS_TOKEN.account_oid = '" + accountoid + "'";
		
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
	* Returns the RIGHTS_SOLD_AS row of the RIGHTS_TOKEN_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return XML-formatted RIGHTS_SOLD_AS row. 
	*/
	def rightstokenSoldAsGetRow(String oid) {
		def rowData;		
		log.info "Executing 2 DB Query"
		this.sql = "select " +
		"to_char(rawtohex(RIGHTS_SOLD_AS_OID)) RIGHTS_SOLD_AS_OID, " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"DISPLAY_NAME, " +
		"CONTENT_ID, " +
		"PRODUCT_ID, " +
		"CREATED_DATE, " +
		"LANGUAGE_ID, " +
		"BUNDLE_ID " +
		"from " +
		"RIGHTS_SOLD_AS where " +
		"RIGHTS_TOKEN_OID = hextoraw('" + oid + "')";

		try {
			rowData = SelectFromDB(this.sql);
		}
		catch(Throwable e) {
			this.log.info e;
			log.info "Done Executing 2 DB Query"
			return rowData;
		}
						
		finally {
			this.dbConnClose();
			return(rowData);
		}
	}
	
	/**
	* Returns the PURCHASE_PROFILE rows of the RIGHTS_TOKEN_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return XML-formatted PURCHASE_PROFILE rows. 
	*/
	def rightstokenPMPPGetRow(String oid) {
		def rowData;		
		log.info "Executing 3 DB Query"
		this.sql = """select 
		to_char(rawtohex(PURCHASE_PROFILE_OID)) as PURCHASE_PROFILE_OID,
		to_char(rawtohex(RIGHTS_TOKEN_OID)) as RIGHTS_TOKEN_OID,
		DOWNLOAD,
		STREAM,
		DISCRETE_MEDIA_RIGHTS_REMAIN,
		PURCHASED_CONTENT_PROFILE,
		CREATED_DATE,
		UPDATED_DATE 
		from PURCHASE_PROFILE
		where to_char(rawtohex(RIGHTS_TOKEN_OID)) = '""" + oid + "'";


		try {
			rowData = SelectFromDB(this.sql);
		}
		catch(Throwable e) {
			this.log.info e;
			return rowData;
		}
						
		finally {
			this.dbConnClose();
			log.info "Done Executing 3 DB Query"
			return(rowData);
		}	
	}

	/**
	* Returns the PURCHASE_PROFILE and PURCHASED_FULFILLMENT_METHOD rows of the RIGHTS_TOKEN_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return XML-formatted PURCHASE_PROFILE and PURCHASED_MEDIA_PROFILE rows. 
	*/
	def rightstokenPFPPGetRow(String oid) {
		def rowData;		

		this.sql = """select 
		to_char(rawtohex(pfm.PURCHASED_FULFILLMENT_MTHD_OID)) as PURCHASED_FULFILLMENT_MTHD_OID,
		pfm.AUTHORIZED_FULFILLMENT_METHOD,
		to_char(rawtohex(pp.PURCHASE_PROFILE_OID)) as PURCHASE_PROFILE_OID,
		to_char(rawtohex(pp.RIGHTS_TOKEN_OID)) as RIGHTS_TOKEN_OID,
		pp.DOWNLOAD,
		pp.STREAM,
		pp.DISCRETE_MEDIA_RIGHTS_REMAIN,
		pp.PURCHASED_CONTENT_PROFILE,
		pp.CREATED_DATE,
		pp.UPDATED_DATE 
		from PURCHASED_FULFILLMENT_METHOD pfm, PURCHASE_PROFILE pp 
		where pfm.PURCHASE_PROFILE_OID = pp.PURCHASE_PROFILE_OID 
		and to_char(rawtohex(pp.RIGHTS_TOKEN_OID)) = '""" + oid + "'";


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
	* Returns the FULFILLMENT_LOC rows of the RIGHTS_TOKEN_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return XML-formatted FULFILLMENT_LOC rows. 
	*/
	def rightstokenFFLocGetRow(String oid) {
		def rowData;		
		log.info "Executing 4 DB Query"
		this.sql = "select " +
		"to_char(rawtohex(FULFILLMENT_LOC_OID)) FULFILLMENT_LOC_OID, " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"TYPE, " +
		"LOCATION, " +
		"PREFERENCE_ORDER, " +
		"CREATED_DATE " +
		"from " +
		"FULFILLMENT_LOC where " +
		"RIGHTS_TOKEN_OID  = hextoraw('" + oid + "')" + " ORDER BY LOCATION,PREFERENCE_ORDER";


		try {
			rowData = SelectFromDB(this.sql);
		}
	    catch(Throwable e) {
			this.log.info e;
			return rowData;
		}
		finally {
			this.dbConnClose();
			log.info "Done Executing 4 DB Query"
			return(rowData);
		}
	}

/**
	* Returns the RIGHTS_STATUS_HISTORY rows of the RIGHTS_TOKEN_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return XML-formatted RIGHTS_STATUS_HISTORY rows. 
	*/
	def rightstokenStatusHistoryGetRow(String oid) {
		def rowData;		
		log.info "Executing 5 DB Query"
		this.sql = "select " +
		"to_char(rawtohex(RIGHTS_STATUS_HISTORY_OID)) RIGHTS_STATUS_HISTORY_OID, " +
		"to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, " +
		"STATUS, " +
		"DESCRIPTION, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID, " +
		"to_char(rawtohex(CREATED_BY_USER_OID)) CREATED_BY_USER_OID " +
		"from " +
		"RIGHTS_STATUS_HISTORY where " +
		"RIGHTS_TOKEN_OID  = hextoraw('" + oid + "')";


		try {
			rowData = SelectFromDB(this.sql);
		}
	    catch(Throwable e) {
			this.log.info e;
			return rowData;
		}
		finally {
			this.dbConnClose();
			log.info "Done Executing 5 DB Query"
			return(rowData);
		}
	
	}

/**
	* Returns the MD_BASIC_INFO rows of the RIGHTS_TOKEN_OID 
	* supplied in XML format.
	*   @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return XML-formatted MD_BASIC_INFO rows. 
	*/
	def rightstokenMDinfoGetRow(String oid) {
		def rowData;		

		this.sql = "select " +
		"MD_BASIC.CONTENT_ID, " +
		"MD_BASIC.RUN_LENGTH, " +
		"MD_BASIC.WORK_TYPE, " +
		"MD_BASIC_INFO.LANGUAGE_ID, " +
		"MD_BASIC_INFO.TITLE_DISPLAY_60, " +
		"MD_BASIC_INFO.TITLE_DISPLAY_19, " +
		"MD_BASIC_INFO.SUMMARY_190, " +
		"MD_BASIC_INFO_ART_REFERENCE.URI, " +
		"MD_BASIC_RATING_SET.IS_ADULT, " +
		"MD_CONTENT_RATING_REF.COUNTRY, " +
		"MD_CONTENT_RATING_REF.REGION, " +
		"MD_CONTENT_RATING_REF.SYSTEM, " +
		"MD_CONTENT_RATING_REF.VALUE, " +
		"MD_CONTENT_RATING_REF.LINK_TO_LOGO " +
		"from " +
		"MD_BASIC, MD_BASIC_INFO, MD_BASIC_INFO_ART_REFERENCE, MD_BASIC_RATING_SET, MD_CONTENT_RATING_REF, RIGHTS_TOKEN " +
		"where " +
		"RIGHTS_TOKEN.CONTENT_ID = MD_BASIC.CONTENT_ID " +
		"and " +
		"MD_BASIC_INFO.BASIC_OID = MD_BASIC.BASIC_OID " +
		"and " +
		"MD_BASIC_INFO_ART_REFERENCE.BASIC_INFO_OID = MD_BASIC_INFO.BASIC_INFO_OID " +
		"and " +
		"MD_BASIC_RATING_SET.BASIC_OID = MD_BASIC.BASIC_OID " +
		"and " +
		"MD_CONTENT_RATING_REF.CONTENT_RATING_OID = MD_BASIC_RATING_SET.CONTENT_RATING_OID " +
		"and " +
		"RIGHTS_TOKEN.RIGHTS_TOKEN_OID = hextoraw('" + oid + "') ";

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
	* Returns the RIGHTS_TOKEN.STATUS_ID row of the RIGHTS_TOKEN_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return The RIGHTS_TOKEN.STATUS_ID value. 
	*/
	def rightstokenGetStatus(String oid) {
		String rData = "";

		this.sql = "select STATUS_ID from RIGHTS_TOKEN where " +
			" RIGHTS_TOKEN_OID = hextoraw('" + oid + "')"; 
		
		try {
			def rowData = dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].STATUS_ID;
					if (this.log) 
					{
						this.log.info "rightstokenGetStatus: sql is " + this.sql;
						this.log.info "rightstokenGetStatus: rowData.size is " + rowData.size;
						this.log.info "rightstokenGetStatus: rowData[0] is " + rowData[0];
					}			
				} 
			} 
		}
		catch(Throwable e) {
			this.log.info e;
			return rData;
		} 
		finally {
			this.dbConnClose();
			return rData;
		}
	}

	/**
	* Returns the RIGHTS_TOKEN.CID row of the RIGHTS_TOKEN_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return The RIGHTS_TOKEN.CID value as a String. 
	*/
	def rightstokenGetCID(String oid) {
		String rData = "";

		this.sql = "select CONTENT_ID from RIGHTS_TOKEN where " +
			" RIGHTS_TOKEN_OID = hextoraw('" + oid + "')"; 
		
		try {
			def rowData = dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].CONTENT_ID;
					if (this.log) 
					{
						this.log.info "rightstokenGetCID: sql is " + this.sql;
						this.log.info "rightstokenGetCID: rowData.size is " + rowData.size;
						this.log.info "rightstokenGetCID: rowData[0] is " + rowData[0];
					}			
				} 
			} 
		}
		catch(Throwable e) {
			this.log.info e;
			return rData;
		}
		finally {
			this.dbConnClose();
			return rData;
		}
	}

	/**
	* Returns the RIGHTS_TOKEN.ALID row of the RIGHTS_TOKEN_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return The RIGHTS_TOKEN.ALID value as a String. 
	*/
	def rightstokenGetALID(String oid) {
		String rData = "";

		this.sql = "select ALID from RIGHTS_TOKEN where " +
			" RIGHTS_TOKEN_OID = hextoraw('" + oid + "')"; 
		
		try {
			def rowData = dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].ALID;
					if (this.log) 
					{
						this.log.info "rightstokenGetALID: sql is " + this.sql;
						this.log.info "rightstokenGetALID: rowData.size is " + rowData.size;
						this.log.info "rightstokenGetALID: rowData[0] is " + rowData[0];
					}			
				} 
			} 
		}
		catch(Throwable e) {
			this.log.info e;
			return rData;
		}
		finally {
			this.dbConnClose();
			return rData;
		}
	}

	
		/**
	* Returns the APID rows of the RIGHTS_TOKEN_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The RIGHTS_TOKEN_OID of the row to extract. 
	* @return The APIDs value as a String associated with RightsToken. 
	*/
	def rightstokenGetAPID(String oid) {
		String rData = "";

		this.sql = "select APID from ASSET_MAP_APID_GROUP where " +
		"APID_GROUP_ID in (Select APID_GROUP_ID from ASSET_APID_GROUP " +
		"where ASSET_FULFILLMENT_GROUP_OID in (Select ASSET_FULFILLMENT_GROUP_OID from ASSET_FULFILLMENT_GROUP " + 
		"where MAP_LP_OID in ( select MAP_LP_OID from ASSET_MAP_LP " +
		"where ALID in ( Select ALID from RIGHTS_TOKEN where RIGHTS_TOKEN_OID=hextoraw('" + oid + "')))))";
					
		try {
			def rowData = dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size()) 
				{
					rData = rowData[0].APID;
					if (this.log) 
					{
						this.log.info "rightstokenGetAPID: sql is " + this.sql;
						this.log.info "rightstokenGetAPID: rowData.size is " + rowData.size;
						this.log.info "rightstokenGetAPID: rowData[0] is " + rowData[0];
					}			
				} 
			} 
		}
		catch(Throwable e) {
			this.log.info e;
			return rData;
		}
		finally {
			this.dbConnClose();
			return rData;
		}
	}
	
	
	
	
	
	
	
	
	/*
	*	 RIGHTSTOKEN Set Methods
	*/

	/**
	* Updates the RIGHTS_TOKEN.ACCOUNT_OID column of the RIGHTS_TOKEN_OID 
	* supplied. 
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @param acct The ACCOUNT_OID to use in the update. 
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenSetAccountOid(String rightstoken, String acct) { 
		int rc = 0;

		this.sql = "UPDATE RIGHTS_TOKEN set ACCOUNT_OID = '" + acct + "' " +
			"where RIGHTS_TOKEN_OID = hextoraw('" + rightstoken + "')"; 

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "rightsTokenSetAccountOid: rc is "  + rc;
					this.log.info "rightsTokenSetAccountOid: sql is " + this.sql;
				}
			} 
		}
		catch(Throwable e) {
			this.log.info e;
		} finally {
			this.dbConnClose();
			return rc;
		}
	}

	/**
	* Updates the RIGHTS_TOKEN.STATUS_ID column of the RIGHTS_TOKEN_OID 
	* supplied with the value supplied in status.
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @param status The STATUS_ID value to use in the update. 
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenSetStatus(String rightstoken, String status) { 
		int rc = 0;

		dbSetStatusSQL(rightstoken, status); 
			
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenSetStatus: rc is "  + rc;
						this.log.info "rightsTokenSetStatus: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
			}
			finally 
			{
				this.dbConnClose();
				return(rc);
			}
		}  else {
			return(rc);
		}
	}

	/**
	* Updates the RIGHTS_TOKEN.STATUS_ID column of the RIGHTS_TOKEN_OID 
	* supplied with a status of 'deleted' 
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenSetStatusDeleted(String rightstoken) { 
		int rc = 0;
	
		dbSetStatusSQL(rightstoken,"deleted");

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenSetStatusDeleted: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenSetStatusDeleted: rc is "  + rc;
						this.log.info "rightsTokenSetStatusDeleted: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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

	/**
	* Updates the RIGHTS_TOKEN.STATUS_ID column of the RIGHTS_TOKEN_OID 
	* supplied with a status of 'suspended' 
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenSetStatusSuspended(String rightstoken) { 
		int rc = 0;
	
		dbSetStatusSQL(rightstoken,"suspended");
		
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenSetStatusSuspended: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenSetStatusSuspended: rc is "  + rc;
						this.log.info "rightsTokenSetStatusSuspended: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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

	/**
	* Updates the RIGHTS_TOKEN.STATUS_ID column of the RIGHTS_TOKEN_OID 
	* supplied with a status of 'other' 
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenSetStatusOther(String rightstoken) { 
		int rc = 0;

		dbSetStatusSQL(rightstoken,"other");

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenSetStatusOther: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenSetStatusOther: rc is "  + rc;
						this.log.info "rightsTokenSetStatusOther: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
			}
			finally 
			{
				this.dbConnClose();

				return(rc);
			}
		}
		else 
		{
			return(rc);
		}
	}

	/**
	* Updates the RIGHTS_TOKEN.STATUS_ID column of the RIGHTS_TOKEN_OID 
	* supplied with a status of 'pending' 
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenSetStatusPending(String rightstoken) { 
		int rc = 0;
	
		dbSetStatusSQL(rightstoken,"pending");
		
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenSetStatusPending: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenSetStatusPending: rc is "  + rc;
						this.log.info "rightsTokenSetStatusPending: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
			}
			finally 
			{
				this.dbConnClose();

				return(rc);
			}
		} 
		else 
		{
			return(rc);
		}
	}

	/**
	* Updates the RIGHTS_TOKEN.STATUS_ID column of the RIGHTS_TOKEN_OID 
	* supplied with a status of 'active' 
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenSetStatusActive(String rightstoken) { 
		int rc = 0;
	
		dbSetStatusSQL(rightstoken,"active");
		
		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenSetStatusActive: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenSetStatusActive: rc is "  + rc;
						this.log.info "rightsTokenSetStatusActive: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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

	/**
	* Updates the RIGHTS_TOKEN.RENTAL_ABSOLUTE_EXPIRATION column of the RIGHTS_TOKEN_OID 
	* supplied with the sysdate +|- time supplid in unit and measure.  Value unit values
	* are "minutes", "hours", "days", "years".  Measure is a postive/negative int. 
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @param unit  "minutes", "hours", "days", or "years".
	* @param measure Positive or negative int. 
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenExpire(String rightstoken, String unit, int measure) { 
		String a  = "";
		int    rc = 0;

		switch (unit.toLowerCase()) {
			case "minutes":
				if (measure < 0) {
					a = "sysdate " + measure + "/1440";
				} else {
					a = "sysdate + " + measure + "/1440";
				}
				break;
			case "hours":
				if (measure < 0) {
					a = "sysdate " + measure + "/24";
				} else {
					a = "sysdate + " + measure + "/24";
				}
				break;
			case "days":
				if (measure < 0) {
					a = "sysdate " + measure;
				} else {
					a = "sysdate + " + measure;
				}
				break;
			case "years":
				if (measure < 0) {
					a = "sysdate " + (measure * 365);
				} else {
					a = "sysdate + " + (measure * 365); 
				}
				break;
			default:
				if (this.log) {
					this.log.info "Method only supports unit values " +
					 	"minutes,hours,days,years";
					return -1;	
				}
				break;
		}

		dbSetStatusExpireSQL(rightstoken, a); 

		if (this.log) {
			this.log.info "rightsTokenModifyExpiration: rt is " + rightstoken + ""; 
			this.log.info "rightsTokenModifyExpiration: measure is $measure"; 
			this.log.info "rightsTokenModifyExpiration: unit is $unit"; 
			this.log.info "rightsTokenModifyExpiration: sql is " + this.sql; 
		}

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);
					
				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenModifyExpiration: rc is "  + rc;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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
	/**
	* Updates the RIGHTS_TOKEN.RENTAL_ABSOLUTE_EXPIRATION column of the RIGHTS_TOKEN_OID 
	* supplied with the sysdate +|- number of hours supplied in hours.
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @param hours Positive or negative int for number of hours to +|-.
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenExpireHours(String rightstoken,int hours) { 
		int    rc = 0;
		String a  = "";

		if (hours < 0) {
			a = "sysdate " + hours + "/24"; 
		} else {
			a = "sysdate + " + hours + "/24";; 
		}

		dbSetStatusExpireSQL(rightstoken, a); 

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);
					
				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenExpireHours: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenExpireHours: rc is "  + rc;
						this.log.info "rightsTokenExpireHours: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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

	/**
	* Updates the RIGHTS_TOKEN.RENTAL_ABSOLUTE_EXPIRATION column of the RIGHTS_TOKEN_OID 
	* supplied with the sysdate +|- number of years supplied in hours.
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @param years Positive or negative int for number of years to +|-.
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenExpireYears(String rightstoken,int years) { 
		int    rc = 0;
		String a  = "";

		if (years < 0) {
			a = "sysdate " + (years * 365);
		} else {
			a = "sysdate + " + (years * 365); 
		}

		dbSetStatusExpireSQL(rightstoken, a); 

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenExpireYears: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenExpireYears: rc is "  + rc;
						this.log.info "rightsTokenExpireYears: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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

	/**
	* Updates the RIGHTS_TOKEN.RENTAL_ABSOLUTE_EXPIRATION column of the RIGHTS_TOKEN_OID 
	* supplied with the sysdate +|- number of minutes supplied in hours.
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @param minutes Positive or negative int for number of minutes to +|-.
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenExpireMinutes(String rightstoken,int minutes) { 
		int    rc = 0;
		String a  = "";

		if (minutes < 0) {
			a = "sysdate " + minutes + "/1440"; 
		} else {
			a = "sysdate + " + minutes + "/1440"; 
		}

		dbSetStatusExpireSQL(rightstoken, a); 

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenExpireMinutes: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenExpireMinutes: rc is "  + rc;
						this.log.info "rightsTokenExpireMinutes: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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
	/**
	* Updates the RIGHTS_TOKEN.RENTAL_ABSOLUTE_EXPIRATION column of the RIGHTS_TOKEN_OID 
	* supplied with the sysdate +|- number of days supplied in hours.
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to extract. 
	* @param days Positive or negative int for number of days to +|-.
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsTokenExpireDays(String rightstoken,int days) { 
		int    rc = 0;
		String a  = "";

		if (days < 0) {
			a = "sysdate " + days; 
		} else {
			a = "sysdate + " + days; 
		}

		dbSetStatusExpireSQL(rightstoken, a); 

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsTokenExpireDays: rightstoken is "  + rightstoken;
						this.log.info "rightsTokenExpireDays: days is "  + days;
						this.log.info "rightsTokenExpireDays: rc is "  + rc;
						this.log.info "rightsTokenExpireDays: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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
	*
	*  RightsToken PURCHASE_PROFILE Methods
	*
	*/

	/**
	* Updates the PURCHASE_PROFILE.STREAM column associated with the 
	* RIGHTS_TOKEN supplied to "Y".
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to update.  
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsPurchaseProfileStreamOn(String rightstoken) { 
		int rc;
		String setting = "Y";

		dbSetStreamPurchaseProfileSQL(rightstoken, setting); 

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsPurchaseProfileStreamOn: rightstoken is "  + rightstoken;
						this.log.info "rightsPurchaseProfileStreamOn: setting is "  + setting;
						this.log.info "rightsPurchaseProfileStreamOn: rc is "  + rc;
						this.log.info "rightsPurchaseProfileStreamOn: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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
	/**
	* Updates the PURCHASE_PROFILE.STREAM column associated with the 
	* RIGHTS_TOKEN supplied to "N".
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to update.  
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsPurchaseProfileStreamOff(String rightstoken) { 
		int rc;
		String setting = "N";

		dbSetStreamPurchaseProfileSQL(rightstoken, setting); 

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsPurchaseProfileStreamOff: rightstoken is "  + rightstoken;
						this.log.info "rightsPurchaseProfileStreamOff: setting is "  + setting;
						this.log.info "rightsPurchaseProfileStreamOff: rc is "  + rc;
						this.log.info "rightsPurchaseProfileStreamOff: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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
	/**
	* Updates the PURCHASE_PROFILE.DOWNLOAD column associated with the 
	* RIGHTS_TOKEN supplied to "Y".
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to update.  
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsPurchaseProfileDownloadOn(String rightstoken) { 
		int rc;
		String setting = "Y";

		dbSetDownloadPurchaseProfileSQL(rightstoken, setting); 

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsPurchaseProfileDownloadOn: rightstoken is "  + rightstoken;
						this.log.info "rightsPurchaseProfileDownloadOn: setting is "  + setting;
						this.log.info "rightsPurchaseProfileDownloadOn: rc is "  + rc;
						this.log.info "rightsPurchaseProfileDownloadOn: sql is " + this.sql;
					}
				} 
			}
			catch(Throwable e)
			{
				this.log.info e;
				rc = 0; 
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
	/**
	* Updates the PURCHASE_PROFILE.DOWNLOAD column associated with the 
	* RIGHTS_TOKEN supplied to "N".
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to update.  
	* @return Number of rows updated.  Should be one. 
	*/
	def rightsPurchaseProfileDownloadOff(String rightstoken) { 
		int rc =0;
		String setting = "N";

		dbSetDownloadPurchaseProfileSQL(rightstoken, setting); 

		if (this.sql.length() > 0) {
			try
			{	
				rc = this.dbConnExecuteUpdate(this.sql);

				if(rc != 0) 
				{
					if (this.log) 
					{
						this.log.info "rightsPurchaseProfileDownloadOff: rightstoken is "  + rightstoken;
						this.log.info "rightsPurchaseProfileDownloadOff: setting is "  + setting;
						this.log.info "rightsPurchaseProfileDownloadOff: rc is "  + rc;
						this.log.info "rightsPurchaseProfileDownloadOff: sql is " + this.sql;
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
	*  Delete RightsToken Methods
	*/

	/**
	* Physically deletes the RIGHTS_TOKEN row associated with the 
	* RIGHTS_TOKEN supplied. 
	* @version 1.0
	*
	* @param rightstoken The RIGHTS_TOKEN_OID of the row to update.  
	* @return Number of rows deleted.  Should be one or greater. 
	*/
	def rightsTokenDeleteByHandle(String rightstoken) {
		def rowDataXML;		
		def myXML;
		def rc = 0;
		
		
	/*
	*  DISCRETE_MEDIA_TOKEN_HISTORY
	*/
		this.sql = "delete DISCRETE_MEDIA_TOKEN_HISTORY where RIGHTS_TOKEN_OID = '" + rightstoken + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "rightsTokenDeleteByHandle: sql is " + this.sql;
					this.log.info "rightsTokenDeleteByHandle: rc is $rc";
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
			rc = 0; 
		}
				
	/*
	*  DISCRETE_MEDIA_TOKEN
	*/
		this.sql = "delete DISCRETE_MEDIA_TOKEN where RIGHTS_TOKEN_OID = '" + rightstoken + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "rightsTokenDeleteByHandle: sql is " + this.sql;
					this.log.info "rightsTokenDeleteByHandle: rc is $rc";
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
			rc = 0; 
		}		
		
		
	/*
	*  PURCHASED_FULFILLMENT_METHOD
	*/	
		this.sql = "select to_char(rawtohex(PURCHASED_FULFILLMENT_MTHD_OID)) PURCHASED_FULFILLMENT_MTHD_OID from PURCHASED_FULFILLMENT_METHOD " +
			"where PURCHASE_PROFILE_OID IN (select PURCHASE_PROFILE_OID from " +
			"PURCHASE_PROFILE where RIGHTS_TOKEN_OID = '" + rightstoken + "')"

		rowDataXML = SelectFromDB(this.sql);
	
		if (!rowDataXML.isEmpty()) {
			try {
				myXML = new XmlSlurper().parseText(rowDataXML);
			}
			catch (Throwable e) {
				this.log.info "foo - " + e;
				return 0;
			}

			def rowsReturned = myXML.ResultSet.Row.size();
			def oids = myXML.ResultSet.Row;

			this.log.info "rowsReturned[$rowsReturned]";

			for (oid in oids.PURCHASED_FULFILLMENT_MTHD_OID) {
				this.log.info "oid is " + oid + "";
				
			// Delete PURCHASED_FULFILLMENT_METHOD
				
				this.sql = "DELETE PURCHASED_FULFILLMENT_METHOD where " +
				"PURCHASED_FULFILLMENT_MTHD_OID = '" + oid + "'";
						
				try
				{	
					rc = this.dbConnExecuteUpdate(this.sql);
					
						
					if(rc != 0) 
					{
						if (this.log) 
						{
							this.log.info "rightsTokenDeleteByHandle: rightstoken is "  + rightstoken;
							this.log.info "rightsTokenDeleteByHandle: rc is "  + rc;
							this.log.info "rightsTokenDeleteByHandle: sql is " + this.sql;
						}
					} 
				}
				catch(Throwable e)
				{
					this.log.info e;
					rc = 0; 
				}
			}
		}
		/*
		*  PURCHASE_PROFILE
		*/
		this.sql = "delete PURCHASE_PROFILE where RIGHTS_TOKEN_OID = '" + rightstoken + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "rightsTokenDeleteByHandle: sql is " + this.sql;
					this.log.info "rightsTokenDeleteByHandle: rc is $rc";
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
			rc = 0; 
		}

		/*
		*  RIGHTS_SOLD_AS
		*/
		this.sql = "delete RIGHTS_SOLD_AS where RIGHTS_TOKEN_OID = '" + rightstoken + "'";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "rightsTokenDeleteByHandle: RIGHTS_SOLD_AS"; 
					this.log.info "rightsTokenDeleteByHandle: sql is " + this.sql;
					this.log.info "rightsTokenDeleteByHandle: rc is $rc";
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
			rc = 0; 
		}
		/*
		*  FULFILLMENT_LOC
		*/
		this.sql = "delete FULFILLMENT_LOC where RIGHTS_TOKEN_OID = '" + rightstoken + "'";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "rightsTokenDeleteByHandle: FULFILLMENT_LOC"; 
					this.log.info "rightsTokenDeleteByHandle: sql is " + this.sql;
					this.log.info "rightsTokenDeleteByHandle: rc is $rc";
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
			rc = 0; 
		}
		/*
		*  RIGHTS_STATUS_HISTORY
		*/
		this.sql = "delete RIGHTS_STATUS_HISTORY where RIGHTS_TOKEN_OID = '" + rightstoken + "'";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "rightsTokenDeleteByHandle: RIGHTS_TOKEN_HISTORY"; 
					this.log.info "rightsTokenDeleteByHandle: sql is " + this.sql;
					this.log.info "rightsTokenDeleteByHandle: rc is $rc";
				}
			} 

		}
		catch(Throwable e)
		{
			this.log.info e;
			rc = 0; 
		}
		/*
		*  RIGHTS_TOKEN
		*/
		this.sql = "delete RIGHTS_TOKEN where RIGHTS_TOKEN_OID = '" + rightstoken + "'";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(rc != 0) 
			{
				if (this.log) 
				{
					this.log.info "rightsTokenDeleteByHandle: RIGHTS_TOKEN"; 
					this.log.info "rightsTokenDeleteByHandle: sql is " + this.sql;
					this.log.info "rightsTokenDeleteByHandle: rc is $rc";
				}
			} 
		}
		catch(Throwable e)
		{
			this.log.info e;
			rc = 0; 
		}
		finally {
			this.dbConnClose();
			return rc;
		}
	}

	/*
	*  RIGHTSTOKEN_DELETE by Account_oid
	*/

	/**
	* Physically deletes all RIGHTS_TOKEN rows associated with the 
	* ACCOUNT_OID supplied. 
	* @version 1.0
	*
	* @param  accountoid The owning ACCOUNT_OID of the rows to delete.  
	* @return Number of rows deleted.  Could be zero or greater. 
	*/
	def rightsTokenDeleteByAccountHandle(String accountoid) {
		def rowsReturned = 0;
		def rowDataXML;		
		def myXML;
		
		this.sql = "select to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID from RIGHTS_TOKEN " +
			"where ACCOUNT_OID = '" + accountoid + "'";
			

		try {
			rowDataXML = SelectFromDB(this.sql);

			if (!rowDataXML.isEmpty()) {
				myXML = new XmlSlurper().parseText(rowDataXML);

				rowsReturned = myXML.ResultSet.Row.size();

				def oids = myXML.ResultSet.Row;

				if (this.log) 
				{
					this.log.info "rightsTokenDeleteByAccountHandle: rowsReturned[$rowsReturned]";
				}

				for (oid in oids.RIGHTS_TOKEN_OID) {
					if (this.log) 
					{
						this.log.info "rightsTokenDeleteByAccountHandle: oid is " + oid + "";
					}
					rightsTokenDeleteByHandle(oid.toString());
				}
			} 
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally 
		{
			this.dbConnClose();
			return rowsReturned;
		}
	}
}
