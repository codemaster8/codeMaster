package dbUtils; 

/**
 * Provides Utilities to Interact with CID data.
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
           catch() blocks.   Added new methods per standards. PJG
  1/25/11:  Fixed bug in sqlAssetStarttime() where variable 'g' 
			  was not defined.  pjg.
  2/9/11:  Removed references too dbIdentifier and verified dbConnClose();

----------------------------------------------------
*/

import groovy.sql.Sql;
import java.util.regex.Matcher
import java.util.regex.Pattern

class dbCID extends dbUtils 
{
	/*
	*  This is rating data from the MD_CONTENT_RATING_REF
	*  table.
	*/
	/** OID mapping for rating */
	def NotRatedOID = 0;
	/** OID mapping for rating */
	def G_OID = 1;
	/** OID mapping for rating */
	def PG_OID = 2;
	/** OID mapping for rating */
	def PG13_OID = 3;
	/** OID mapping for rating */
	def R_OID = 4;
	/** OID mapping for rating */
	def NC17_OID = 5;
	/** OID mapping for rating */
	def NR_OID = 6;
	/** OID mapping for rating */
	def M_OID = 7;
	/** OID mapping for rating */
	def GP_OID = 8;
	/** OID mapping for rating */
	def SMA_OID = 9;
	/** OID mapping for rating */
	def X_OID = 10;
	/** OID mapping for rating */

	/** Class var def */
	def log = null;
	/** Class var def */
	def sql = null;


	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbCID<code> object.
	*
	*/
	dbCID() {
	}
	/**
	*  Creates a new <code>dbCID<code> object with
	*  ability to write to the log.
	*/
	dbCID(log) {
		super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbCID<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbCID(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	}

	/*
	*   Private methods
	*/

	/**
	*   Private method for constructing SQL for the other
	*   methods in the class.
	*/
	private dbSetStatusSQL(String cid, String status) {
		this.sql = "update MD_BASIC set STATUS = '%%STATUS%%' where CONTENT_ID = '%%CID%%'"; 
		this.sql = sql.replaceAll("%%STATUS%%", status);
		this.sql = sql.replaceAll("%%CID%%", cid);

		if (this.log) {
			this.log.info "dbSetStatusSQL: SQL is " + this.sql;
		}
	}
	/**
	* @deprecated Replaced by <code>cidGetRating(String)</code>
	*
	* Returns the CONTENT_RATING_OID associated with the CID 
	* supplied. 
	*
	* @param cid  The CID targetted in the request 
	* @return Value of the MD_BASIC_RATING_SET.CONTENT_RATING_OID column. 
	*
	*/
	def sqlGetRatingSet(String cid) {
		String ratingOid;

		this.sql = "select CONTENT_RATING_OID from MD_BASIC_RATING_SET where BASIC_OID = (select " +
			"BASIC_OID from MD_BASIC where CONTENT_ID = '" + cid + "')";
		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{
				if (rowData.size == 1) 
				{
					ratingOid = rowData[0].CONTENT_RATING_OID;
					if (this.log) 
					{
						this.log.info "sqlGetRatingSet: rowData.size is "  + rowData.size;
						this.log.info "sqlGetRatingSet: sql is " + this.sql;
						this.log.info "sqlGetRatingSet: ratingOid is " + ratingOid;
					}
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
			return(ratingOid);
		}

		return(sql);
	}
	/**
	*   Private method for constructing SQL for the other
	*   methods in the class.
	*/
	private sqlGetAssetStream(String cid, String alid, String value) {
		this.sql = "update ASSET_WINDOW set CAN_STREAM = '" + value + "' where MAP_LP_OID = " + 
			"(SELECT MAP_LP_OID from ASSET_MAP_LP where CID = '" + cid + "' and " +
			"ALID = '" + alid + "')";	

		if (this.log) {
			this.log.info "sqlGetAssetStream: sql is " + this.sql;
		}
	}
	/**
	*   Private method for constructing SQL for the other
	*   methods in the class.
	*/
	private sqlAssetStarttime(String cid, String alid, int mins) {
		String g = null;
		if (mins > 0) {
			g = " + $mins/1440";
		} else if (mins < 0) {
			g = "$mins/1440";
		} 

		this.sql = "update ASSET_WINDOW set START_TIME = sysdate " + g + " where MAP_LP_OID = " + 
			"(SELECT MAP_LP_OID from ASSET_MAP_LP where CID = '" + cid + "' and " +
			"ALID = '" + alid + "')";	
	}
	/**
	*   Private method for constructing SQL for the other
	*   methods in the class.
	*/
	private sqlAssetEndtime(String cid, String alid, int mins) {
		String g = null;
		if (mins > 0) {
			g = " + $mins/1440";
		} else if (mins < 0) {
			g = "$mins/1440";
		} 

		this.sql = "update ASSET_WINDOW set END_TIME = sysdate " + g  + "where MAP_LP_OID = " + 
			"(SELECT MAP_LP_OID from ASSET_MAP_LP where CID = '" + cid + "' and " +
			"ALID = '" + alid + "')";	
	}
	/**
	* @deprecated Replaced by <code>cidSetRating(String)</code>
	*
	* Updates the MD_BASIC_RATING_SET associated with the CID 
	* supplied to the CONTENT_RATING_OID associated with the 
	* region, country, system, and rating supplied in the request. 
	*
	* @param cid  The CID targetted in the request 
	* @param region  The region of the rating. 
	* @param country  The country of the rating. 
	* @param system  The rating system of the rating. 
	* @param rating  The rating. 
	* @return Value of the MD_BASIC.STATUS column. 
	*
	*/
	def sqlSetRating(String cid, String region, String country, String system, String rating) {
		int rc = 0;

		this.sql = "update MD_BASIC_RATING_SET set CONTENT_RATING_OID = (select " +
			"CONTENT_RATING_OID from MD_CONTENT_RATING_REF where REGION = " +
			"'" + region + "' and SYSTEM = '" + system + "' and COUNTRY='" + country + "' and VALUE = '" + rating + "') " +
 			"where BASIC_OID = (select BASIC_OID from MD_BASIC where CONTENT_ID = " +
			"'" + cid + "')";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "sqlSetRating: rc is "  + rc;
					this.log.info "sqlSetRating: sql is " + this.sql;
					this.log.info "sqlSetRating: cid is " + cid;
					this.log.info "sqlSetRating: region [" + region + "]"; 
					this.log.info "sqlSetRating: country [" + country + "]"; 
					this.log.info "sqlSetRating: rating [" + rating + "]"; 
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
	*   Private method for constructing/executing SQL for the other
	*   methods in the class.
	*/
	private sqlSetIsAdult(String cid, String setting) {
		int rc = 0;

		this.sql = "update MD_BASIC_RATING_SET set IS_ADULT = '" + setting + "' " +
 			"where BASIC_OID = (select BASIC_OID from MD_BASIC where CONTENT_ID = " +
			"'" + cid + "')";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "sqlSetIsAdult: rc is "  + rc;
					this.log.info "sqlSetIsAdult: sql is " + this.sql;
					this.log.info "sqlSetIsAdult: cid is " + cid;
					this.log.info "sqlSetIsAdult: setting is " + setting;
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

	/*

	/*
	*   GET METHODS
	*/
	/**
	* Extract the MD_BASIC.STATUS value associated with the CID 
	* supplied. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request 
	* @return Value of the MD_BASIC.STATUS column. 
	*
	*/
	def cidGetStatus(String cid) { 
		String status = "";
		
		this.sql = "select STATUS from MD_BASIC where CONTENT_ID = '" + cid + "'"; 

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{
				if (rowData.size == 1) 
				{
					status = rowData[0].STATUS;
					if (this.log) 
					{
						this.log.info "cidGetStatus: rowData.size is "  + rowData.size;
						this.log.info "cidGetStatus: sql is " + this.sql;
						this.log.info "cidGetStatus: status is " + status;
					}
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
			return(status);
		}
	}
	/**
	* Extract the ASSET_WINDOW.CAN_STREAM value associated with the CID 
	* and ALID combo supplied. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request 
	* @param alid The ALID targetted in the request 
	* @return Value of the ASSET_WINDOW.CAN_STREAM column. 
	*
	*/
	def cidGetAssetCanStream(String cid, String alid) { 
		String streamFlag = "";
		
		this.sql = "select CAN_STREAM from ASSET_WINDOW where MAP_LP_OID = " + 
			"(SELECT MAP_LP_OID from ASSET_MAP_LP where CID = '" + cid + "' and " +
			"ALID = '" + alid + "')";	

		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{
				if (rowData.size == 1) 
				{
					streamFlag = rowData[0].CAN_STREAM;
					if (this.log) 
					{
						this.log.info "cidGetAssetCanStream: rowData.size is "  + rowData.size;
						this.log.info "cidGetAssetCanStream: sql is " + this.sql;
						this.log.info "cidGetAssetCanStream: streamFlag is " + streamFlag;
					}
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
			return(streamFlag);
		}
	}
	/**
	* Return the MD_BASIC_RATING_SET.IS_ADULT value associated with the CID 
	* supplied. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request 
	* @return Value of the MD_BASIC_RATING_SET.IS_ADULT column. 
	*
	*/
	def cidGetisAdult(String cid) { 
		String adultFlag = "";
		
		this.sql = "select IS_ADULT from MD_BASIC_RATING_SET "  +
 			"where BASIC_OID = (select BASIC_OID from MD_BASIC where CONTENT_ID = " +
			"'" + cid + "')";
		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{
				if (rowData.size == 1) 
				{
					adultFlag = rowData[0].IS_ADULT;
					if (this.log) 
					{
						this.log.info "cidGetisAdult: rowData.size is "  + rowData.size;
						this.log.info "cidGetisAdult: sql is " + this.sql;
						this.log.info "cidGetisAdult: adultFlag is " + adultFlag;
					}
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
			return(adultFlag);
		}
	}
	/**
	* Returns the CONTENT_RATING_OID associated with the CID 
	* supplied. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request 
	* @return Value of the MD_BASIC_RATING_SET.CONTENT_RATING_OID column. 
	*
	*/
	def cidGetRatingSet(String cid) {
		String ratingOid;

		this.sql = "select CONTENT_RATING_OID from MD_BASIC_RATING_SET where BASIC_OID = (select " +
			"BASIC_OID from MD_BASIC where CONTENT_ID = '" + cid + "')";
		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{
				if (rowData.size == 1) 
				{
					ratingOid = rowData[0].CONTENT_RATING_OID;
					if (this.log) 
					{
						this.log.info "cidGetRatingSet: rowData.size is "  + rowData.size;
						this.log.info "cidGetRatingSet: sql is " + this.sql;
						this.log.info "cidGetRatingSet: ratingOid is " + ratingOid;
					}
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
			return(ratingOid);
		}

		return(sql);
	}

	/*
	*   SET Methods	 
	*/

	/**
	* Update the MD_BASIC.STATUS value associated with the CID 
	* to the value supplied in setting. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param setting  The string representing the value to set. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetStatus(String cid, String setting) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(cid,setting);

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetStatus: rc is "  + rc;
					this.log.info "cidSetStatus: sql is " + this.sql;
					this.log.info "cidSetStatus: cid is " + cid;
					this.log.info "cidSetStatus: setting is " + setting;
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
	* Update the MD_BASIC.STATUS value associated with the CID 
	* to the value 'active' 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetStatusActive(String cid) { 
		int rc = 0;

		dbSetStatusSQL(cid,"active");
		
		rc = this.dbConnExecuteUpdate(this.sql);

		try
		{	if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetStatusActive: rc is "  + rc;
					this.log.info "cidSetStatusActive: sql is " + this.sql;
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
	* Update the MD_BASIC.STATUS value associated with the CID 
	* to the value 'deleted' 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetStatusDeleted(String cid) { 
		int rc = 0;

		dbSetStatusSQL(cid,"deleted");
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetStatusDeleted: rc is "  + rc;
					this.log.info "cidSetStatusDeleted: sql is " + this.sql;
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
	* Update the MD_BASIC.STATUS value associated with the CID 
	* to the value 'suspended' 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetStatusSuspended(String cid) { 
		int rc = 0;

		dbSetStatusSQL(cid,"suspended");

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetStatusSuspended: rc is "  + rc;
					this.log.info "cidSetStatusSuspended: sql is " + this.sql;
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
	* Update the MD_BASIC.STATUS value associated with the CID 
	* to the value 'other'
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetStatusOther(String cid) { 
		int rc = 0;

		dbSetStatusSQL(cid,"other");

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetStatusOther: rc is "  + rc;
					this.log.info "cidSetStatusOther: sql is " + this.sql;
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
	*
	* Updates the ASSET_WINDOW.START_TIME associated of the CID/ALID
	* combo supplied to sysdate +/- mins supplied in mins argument; positive
	* and negative mins values allowed.
	* @version 1.0
	*
	* @param cid  The CID targetted in the request 
	* @param alid  The ALID targetted in the request 
	* @param mins  Minutes (negative/positive) from sysdate to set. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetStartTimeMinutes(String cid, String alid, int mins) { 
		int rc = 0;

		try
		{	
			sqlAssetStarttime(cid, alid, mins); 

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetStartTimeMinutes: rc is "  + rc;
					this.log.info "cidSetAssetStartTimeMinutes: sql is " + this.sql;
					this.log.info "cidSetAssetStartTimeMinutes: cid is " + cid;
					this.log.info "cidSetAssetStartTimeMinutes: mins is " + mins;
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
	*
	* Updates the ASSET_WINDOW.END_TIME associated of the CID/ALID
	* combo supplied to sysdate +/- mins supplied in mins argument; positive
	* and negative mins values allowed.
	* @version 1.0
	*
	* @param cid  The CID targetted in the request 
	* @param alid  The ALID targetted in the request 
	* @param mins  Minutes (negative/positive) from sysdate to set. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetEndTimeMinutes(String cid, String alid, int mins) { 
		int rc = 0;
		
		try
		{	
			sqlAssetEndtime(cid, alid, mins); 

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetEndTimeMinutes: rc is "  + rc;
					this.log.info "cidSetAssetEndTimeMinutes: sql is " + this.sql;
					this.log.info "cidSetAssetEndTimeMinutes: cid is " + cid;
					this.log.info "cidSetAssetEndTimeMinutes: mins is " + mins;
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
	* Update the ASSET_WINDOW.CAN_STREAM value associated with the CID 
	* and ALID to the "n" 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param cid  The ALID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetStreamOff(String cid, String alid) { 
		int rc = 0;

		try
		{	
			sqlGetAssetStream(cid, alid, "N"); 

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetStreamOff: rc is "  + rc;
					this.log.info "cidSetAssetStreamOff: sql is " + this.sql;
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
	* Update the ASSET_WINDOW.CAN_STREAM value associated with the CID 
	* and ALID to the "y" 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param alid  The ALID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetStreamOn(String cid, String alid) { 
		int rc = 0;
		
		try
		{	
			sqlGetAssetStream(cid, alid, "Y"); 

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetStreamOn: rc is "  + rc;
					this.log.info "cidSetAssetStreamOn: sql is " + this.sql;
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
	* Update the ASSET_MAP_LP.STATUS value associated with the CID 
	* and ALID combo supplied to the value supplied in status 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param alid  The ALID targetted in the request. 
	* @param status  The value to set ASSET_MAP_LP.STATUS too. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetMapStatus(String cid, String alid, String status) { 
		int rc = 0;
		
		this.sql = "UPDATE ASSET_MAP_LP set STATUS = '" + status + "' where CID = '" + cid + "'" +
			" and ALID = '" + alid + "'";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetMapStatus: rc is "  + rc;
					this.log.info "cidSetAssetMapStatus: sql is " + this.sql;
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
	* Update the ASSET_MAP_LP.STATUS value associated with the CID 
	* and ALID combo supplied to the 'active'. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param alid  The ALID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetMapStatusActive(String cid, String alid) { 
		int rc = 0;
		
		this.sql = "UPDATE ASSET_MAP_LP set STATUS = 'active' where CID = '" + cid + "'" +
			" and ALID = '" + alid + "'";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetMapStatusActive: rc is "  + rc;
					this.log.info "cidSetAssetMapStatusActive: sql is " + this.sql;
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
	* Update the ASSET_MAP_LP.STATUS value associated with the CID 
	* and ALID combo supplied to the 'other'. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param alid  The ALID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetMapStatusOther(String cid, String alid) { 
		int rc = 0;
		
		this.sql = "UPDATE ASSET_MAP_LP set STATUS = 'other' where CID = '" + cid + "'" +
			" and ALID = '" + alid + "'";
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetMapStatusOther: rc is "  + rc;
					this.log.info "cidSetAssetMapStatusOther: sql is " + this.sql;
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
	* Update the ASSET_MAP_LP.STATUS value associated with the CID 
	* and ALID combo supplied to the 'suspended'. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param alid  The ALID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetMapStatusSuspended(String cid, String alid) { 
		int rc = 0;
		
		this.sql = "UPDATE ASSET_MAP_LP set STATUS = 'suspended' where CID = '" + cid + "'" +
			" and ALID = '" + alid + "'";
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetMapStatusSuspended: rc is "  + rc;
					this.log.info "cidSetAssetMapStatusSuspended: sql is " + this.sql;
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
	* Update the ASSET_MAP_LP.STATUS value associated with the CID 
	* and ALID combo supplied to the 'deleted'. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param alid  The ALID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetAssetMapStatusDeleted(String cid, String alid) { 
		int rc = 0;
		
		this.sql = "UPDATE ASSET_MAP_LP set STATUS = 'deleted' where CID = '" + cid + "'" +
			" and ALID = '" + alid + "'";
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetAssetMapStatusDeleted: rc is "  + rc;
					this.log.info "cidSetAssetMapStatusDeleted: sql is " + this.sql;
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
	* Update the MD_BASIC_RATING_SET.IS_ADULT value associated with the CID 
	* supplied to the value supplied via the setting argument. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @param setting  The value to update MD_BASIC_RATING_SET.IS_ADULT with.
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetisAdult(String cid, String setting) { 
		int rc = sqlSetIsAdult(cid, setting );

		return(rc);
	}
	/**
	* Update the MD_BASIC_RATING_SET.IS_ADULT value associated with the CID 
	* supplied to the 'y'. 
	*
	* @param cid  The CID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetisAdultOn(String cid) { 
		int rc = sqlSetIsAdult(cid, "y");

		return(rc);
	}
	/**
	* Update the MD_BASIC_RATING_SET.IS_ADULT value associated with the CID 
	* supplied to the 'n'. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request. 
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def cidSetisAdultOff(String cid) { 
		int rc = sqlSetIsAdult(cid, "n");

		return(rc);
	}
	/**
	*
	* Updates the MD_BASIC_RATING_SET associated with the CID 
	* supplied to the CONTENT_RATING_OID associated with the 
	* region, country, system, and rating supplied in the request. 
	* @version 1.0
	*
	* @param cid  The CID targetted in the request 
	* @param region  The region of the rating. 
	* @param country  The country of the rating. 
	* @param system  The rating system of the rating. 
	* @param rating  The rating. 
	* @return Value of the MD_BASIC.STATUS column. 
	*
	*/
	def cidSetRating(String cid, String region, String country, String system, String rating) {
		int rc = 0;

		this.sql = "update MD_BASIC_RATING_SET set CONTENT_RATING_OID = (select " +
			"CONTENT_RATING_OID from MD_CONTENT_RATING_REF where REGION = " +
			"'" + region + "' and SYSTEM = '" + system + "' and COUNTRY='" + country + "' and VALUE = '" + rating + "') " +
 			"where BASIC_OID = (select BASIC_OID from MD_BASIC where CONTENT_ID = " +
			"'" + cid + "')";
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "cidSetRating: rc is "  + rc;
					this.log.info "cidSetRating: sql is " + this.sql;
					this.log.info "cidSetRating: cid is " + cid;
					this.log.info "cidSetRating: region [" + region + "]"; 
					this.log.info "cidSetRating: country [" + country + "]"; 
					this.log.info "cidSetRating: rating [" + rating + "]"; 
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
