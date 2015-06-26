package dbUtils; 

import groovy.sql.Sql;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class dbSAMLAssertion extends dbUtils 
{
	def log = null;
	def sql = null;

	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbUser<code> object.
	*
	*/
	dbSAMLAssertion(log) {
		super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbUser<code> object.
	*
	*/
	dbSAMLAssertion() {
	}
	/**
	*  Creates a new <code>dbUser<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbSAMLAssertion(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	}

	/*
	*   SQL methods
	*/
	/**
	* PRIVATE method for constructing sql.
	* @version 1.0
	*
	* @param uuid NODE_USER_SAML_ASSERTION.NODE_USER_SAML_UUID 
	* @param status String representing the status. 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	private dbSetStatusSQL(String node, String nodeuser, String nodeaccount, String status) {
		this.sql =  "update NODE_USER_SAML_ASSERTION set STATUS = '%%STATUS%%' where NODE_OID = '%%NODE%%' and NODE_USER_ID = '%%NODEUSER%%' and NODE_ACCOUNT_ID = '%%NODEACCOUNT%%'";
		this.sql = sql.replaceAll("%%STATUS%%", status);
		this.sql = sql.replaceAll("%%NODE%%", node);
		this.sql = sql.replaceAll("%%NODEUSER%%", nodeuser);
		this.sql = sql.replaceAll("%%NODEACCOUNT%%", nodeaccount);

		if (this.log) {
			this.log.info "dbSetStatusSQL: SQL is " + this.sql;
		}
	}
	/*
	*   GET METHODS
	*/
	/**
	* Return the NODE_USER_SAML_ASSERTION row associated with the UUID
	* provided in uuid.
	*
	* @version 1.0
	*
	* @param uuid NODE_USER_SAML_ASSERTION.NODE_USER_SAML_UUID 
	*
	* @return NODE_USER_SAML_ASSERTION row for uuid. 
	*
	*/
	def samlGetAssertionRow(String uuid) { 
	
		String xmlData = "";

		this.sql = " select to_char(rawtohex(NODE_USER_SAML_ASSERTION_OID)) NODE_USER_SAML_ASSERTION_OID, " +
		  "to_char(rawtohex(NODE_OID)) NODE_OID," +
		  "to_char(DBMS_LOB.substr(NODE_USER_SAML_ASSERTION,4000)) NODE_USER_SAML_ASSERTION_PART1," +
  		  "to_char(DBMS_LOB.substr(NODE_USER_SAML_ASSERTION,4000,4001)) NODE_USER_SAML_ASSERTION_PART2," +
  		  "NODE_USER_SAML_UUID," +
  		  "CREATED_DATE," +
		  "to_char(rawtohex(NODE_USER_ID)) NODE_USER_ID," +
		  "to_char(rawtohex(NODE_ACCOUNT_ID)) NODE_ACCOUNT_ID," +
  		  "STATUS," +
  		  "EXPIRATION_DATE" +
		  " from NODE_USER_SAML_ASSERTION where NODE_USER_SAML_UUID = '" + uuid + "'";
		  
		  this.log.info this.sql;
		
		try
		{	
			xmlData = this.SelectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "samlGetAssertionRow: sql is " + this.sql;
					this.log.info "samlGetAssertionRow: xmlData.length() is " + xmlData.length();
					this.log.info "samlGetAssertionRow: xmlData is $xmlData";
				}			
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			dbConnClose();
			return xmlData;
		}
	}
		/**
	* Return the NODE_USER_SAML_ASSERTION EXPIRATION_DATE row associated with the UUID and NODE_OID
	* provided in uuid, nodeid.
	*
	* @version 1.0
	*
	* @param uuid NODE_USER_SAML_ASSERTION.NODE_USER_SAML_UUID and NODE_OID.
	*
	* @return NODE_USER_SAML_ASSERTION.EXPIRATION_DATE for uuid and nodeid. 
	*
	*/
	def samlGetAssertionExpDate(String uuid, String nodeid) { 
	
		String xmlData = "";

		this.sql = " select EXPIRATION_DATE " +
		 		  " from NODE_USER_SAML_ASSERTION where NODE_USER_SAML_UUID = '" + uuid + "'" +
				  " and NODE_OID = '" + nodeid + "'";
		  this.log.info this.sql;
		
		try
		{	
			xmlData = this.SelectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "samlGetAssertionExpDate: sql is " + this.sql;
					this.log.info "samlGetAssertionExpDate: xmlData.length() is " + xmlData.length();
					this.log.info "samlGetAssertionExpDate: xmlData is $xmlData";
				}			
			}		 
		}
		catch(Throwable e)
		{
			this.log.info e;
		}
		finally {
			dbConnClose();
			return xmlData;
		}
	}
	
	/**
	* Method for returning NODE_USER_SAML_ASSERTION rowcount for a 
	* particular UUID and NODEOID.
	*
	* @version 1.0
	*
	* @param uuid NODE_USER_SAML_ASSERTION.NODE_USER_SAML_UUID, NODE_USER_SAML_ASSERTION.NODE_OID;
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def samlGetAssertionRowCount(String uuid , nodeid) { 
		// Returns the number of rows found int he NODE_USER_SAML_ASSERTION
		// table that have the supplied UUID in the NODE_USER_SAML_UUID
		// column. 
		this.sql = "select count(*) as cnt from NODE_USER_SAML_ASSERTION " +
			"where NODE_USER_SAML_UUID = '" + uuid + "'" +
			" and NODE_OID = '" + nodeid + "'";

		def dbConn = dbConnGet();
		def row = dbConn.firstRow(this.sql);
		dbConn.close()	

		if (this.log) {
			this.log.info "samlGetAssertionRowCount: sql is " + this.sql;
			this.log.info "samlGetAssertionRowCount: row.cnt is " + row.cnt; 
		}

		return(row.cnt); 
	}
		/**
	* Method for returning NODE_USER_SAML_ASSERTION rowcount for a 
	* particular UUID.
	*
	* @version 1.0
	*
	* @param uuid NODE_USER_SAML_ASSERTION.NODE_USER_SAML_UUID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def samlGetUUIDNodeAssertionRowCount(String uuid, String nodeid) { 
		// Returns the number of rows found int he NODE_USER_SAML_ASSERTION
		// table that have the supplied UUID in the NODE_USER_SAML_UUID
		// column. 
		this.sql = "select count(*) as cnt from NODE_USER_SAML_ASSERTION " +
			"where NODE_USER_SAML_UUID = '" + uuid + "'";

		def dbConn = dbConnGet();
		def row = dbConn.firstRow(this.sql);
		dbConn.close()	

		if (this.log) {
			this.log.info "samlGetUUIDNodeAssertionRowCount: sql is " + this.sql;
			this.log.info "samlGetUUIDNodeAssertionRowCount: row.cnt is " + row.cnt; 
		}

		return(row.cnt); 
	}
	def samlGetNodeUserRowCount(String useroid) { 
		// Returns the number of rows found int he NODE_USER_SAML_ASSERTION
		// table that have the supplied UUID in the NODE_USER_SAML_UUID
		// column. 
		this.sql = "select count(*) as cnt from NODE_USER " +
			"where USER_OID = '$useroid'";

		def dbConn = dbConnGet();
		def row = dbConn.firstRow(this.sql);
		dbConn.close()	

		if (this.log) {
			this.log.info "samlGetNodeUserRowCount: sql is " + this.sql;
			this.log.info "samlGetNodeUserRowCount: row.cnt is " + row.cnt; 
		}

		return(row.cnt); 
	}
	
	def samlGetUUIDNodeUserRowCountbyUUID(String uuid) { 
		// Returns the number of rows found int he NODE_USER_SAML_ASSERTION
		// table that have the supplied UUID in the NODE_USER_SAML_UUID
		// column. 
		this.sql = "select count(*) as cnt from NODE_USER " +
			"where NODE_USER_SAML_UUID = '" + uuid + "'";

		def dbConn = dbConnGet();
		def row = dbConn.firstRow(this.sql);
		dbConn.close()	

		if (this.log) {
			this.log.info "samlGetUUIDNodeUserRowCountbyUUID: sql is " + this.sql;
			this.log.info "samlGetUUIDNodeUserRowCountbyUUID: row.cnt is " + row.cnt; 
		}

		return(row.cnt); 
	}

	/**
	* Set uuid's NODE_USER_SAML_ASSERTION.STATUS to the string supplied 
	* in the status parameter.  Note that value supplied for  
	* status is not validated. 
	* @version 1.0
	*
	* @param uuid NODE_USER_SAML_ASSERTION.NODE_USER_SAML_UUID 
	* @param status String representing the status. 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def samlSetStatus(String uuid, String status) { 
		// Returns the number of rows modified (should be 1)
		int rc = 0;
		
		this.sql = "update NODE_USER_SAML_ASSERTION set STATUS = '" + status + "' where " +
			"NODE_USER_SAML_UUID = '" + uuid + "'";

		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "samlSetStatus: rc is "  + rc;
					this.log.info "samlSetStatus: sql is " + this.sql;
				}
			}
		}
		catch(Throwable e)
		{
			this.log.info e;
		} 
		finally 
		{
			dbConnClose();
			return(rc);
		}
	}

	/**
	* Set uuid's NODE_USER_SAML_ASSERTION.STATUS to 'deleted'.
	* @version 1.0
	*
	* @param uuid NODE_USER_SAML_ASSERTION.NODE_USER_SAML_UUID 
	* @param node owning the assertion.
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def samlSetStatusDeleted(String uuid, String node) { 
		// Returns the number of rows modified (should be 1)
		int rc;

		this.sql = "Update NODE_USER_SAML_ASSERTION set STATUS = 'deleted' " +
			" where NODE_USER_SAML_UUID = '" + uuid + "' and NODE_OID = '$node'"; 
		
		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlSetStatusDeleted: rc is " + rc;
			this.log.info "samlSetStatusDeleted: sql is " + sql;
		}

		dbConnClose();
	
		return(rc);
	}
	/**
	* Set uuid's NODE_USER_SAML_ASSERTION.EXPIRATION_DATE to the date supplied 
	* in the units and period parameter.  Note that value supplied for  
	* status is not validated. 
	* @version 1.0
	*
	* @param uuid NODE_USER_SAML_ASSERTION.NODE_USER_SAML_UUID 
	* @param units int representing unit of measure.
	* @param period is minutes, months, hours, days, years
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def samlSetExpire(String uuid, int units, String period) { 
		// period is "year", "days", "month", "minutes"
		// units is the units associated with the period.
		//
		// If you want negative minutes send in a negative
		// number, if you want positive minutes, prefix the
		// minutes with +.  For example, +60 is 60 minutes into
		// the future.  -120 is two hours in the past.	
		//
		// Returns the number of rows modified (should be 1)	
		int rc;
		String measure;
		String x = " ";

		if (units > 0) {
			x = "+ ";		
		} 

		switch (period) {
			case "minute":	
			case "minutes":	
				measure = x + units + "/1440";	
			break;
			case "month":
			case "months":
				measure = x + (units * 30) ;
			break;
			case "hour":
			case "hours":
				measure = x + units + "/24";
			break;
			case "day":
			case "days":
				measure = x + units;
			break;
			default:
				measure = x + (units * 365);  //Years 
			break;
		}

		this.sql = "Update NODE_USER_SAML_ASSERTION set EXPIRATION_DATE = sysdate " + measure + 
			" where NODE_USER_SAML_UUID = '" + uuid + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlSetExpire: rc is " + rc;
			this.log.info "samlSetExpire: sql is " + this.sql;
		}

		dbConnClose();

	
		return(rc);
	}
	def samlExpireAssertion(String uuid, String nodeid) { 
		// Returns the number of rows modified (should be 1)	
		int rc;

		this.sql = "Update NODE_USER_SAML_ASSERTION set EXPIRATION_DATE = sysdate - 1 " + 
			" where NODE_USER_SAML_UUID = '" + uuid + "' and NODE_OID = '" + nodeid + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlExpireAssertion: rc is " + rc;
			this.log.info "samlExpireAssertion: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	}
	def samlExpireAssertionYear(String uuid, String nodeId, Integer x) { 
		// Returns the number of rows modified (should be 1)	
		int rc;

		this.sql = "Update NODE_USER_SAML_ASSERTION set EXPIRATION_DATE = sysdate - " + x * 365 + 
			" where NODE_USER_SAML_UUID = '" + uuid + "' and NODE_OID = '" + nodeId + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlExpireAssertion: rc is " + rc;
			this.log.info "samlExpireAssertion: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	}
	
	def samlRenewAssertionYear(String uuid, String nodeId, Integer x) { 
		// Returns the number of rows modified (should be 1)	
		int rc;

		this.sql = "Update NODE_USER_SAML_ASSERTION set EXPIRATION_DATE = sysdate + " + x * 365 + 
			" where NODE_USER_SAML_UUID = '" + uuid + "' and NODE_OID = '" + nodeId + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlRenewAssertionYear: rc is " + rc;
			this.log.info "samlRenewAssertionYear: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	
	}
	def samlExpireAssertionMonth(String uuid, String nodeId, Integer x) { 
		// Returns the number of rows modified (should be 1)	
		int rc;

		this.sql = "Update NODE_USER_SAML_ASSERTION set EXPIRATION_DATE = sysdate - $x/12" + 
			" where NODE_USER_SAML_UUID = '" + uuid + "' and NODE_OID = '" + nodeid + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlExpireAssertion: rc is " + rc;
			this.log.info "samlExpireAssertion: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	}
	def samlExpireAssertionDay(String uuid, String nodeId, Integer x) { 
		// Returns the number of rows modified (should be 1)	
		int rc;

		this.sql = "Update NODE_USER_SAML_ASSERTION set EXPIRATION_DATE = sysdate - $x/365 " + 
			" where NODE_USER_SAML_UUID = '" + uuid + "' and NODE_OID = '" + nodeid + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlExpireAssertion: rc is " + rc;
			this.log.info "samlExpireAssertion: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	}
	def samlExpireAssertionHour(String uuid, String nodeId, Integer x ) { 
		// Returns the number of rows modified (should be 1)	
		int rc;

		this.sql = "Update NODE_USER_SAML_ASSERTION set EXPIRATION_DATE = sysdate - $x/24 " + 
			" where NODE_USER_SAML_UUID = '" + uuid + "' and NODE_OID = '" + nodeid + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlExpireAssertion: rc is " + rc;
			this.log.info "samlExpireAssertion: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	}
	def samlExpireAssertionMinute(String uuid, String nodeId, Integer x ) { 
		// Returns the number of rows modified (should be 1)	
		int rc;

		this.sql = "Update NODE_USER_SAML_ASSERTION set EXPIRATION_DATE = sysdate - $x/1440 " + 
			" where NODE_USER_SAML_UUID = '" + uuid + "' and NODE_OID = '" + nodeid + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlExpireAssertion: rc is " + rc;
			this.log.info "samlExpireAssertion: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	}
	def samlDeleteAssertion(String uuid, String nodeid) { 
		/*
		*  Method returns the number of rows deleted.  Which 
		*  should always be one.
		*/	
		int rc;

		this.sql = "DELETE from NODE_USER_SAML_ASSERTION where NODE_OID = '" + nodeid + "' " + 
			" and NODE_USER_SAML_UUID = '" + uuid + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlDeleteAssertion: rc is " + rc;
			this.log.info "samlDeleteAssertion: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	}
	def samlDeleteAssertionByNode(String nodeid) { 
		/*
		*  Method returns the number of rows deleted.  
		*/	
		int rc;

		this.sql = "DELETE from NODE_USER_SAML_ASSERTION where NODE_OID = '" + nodeid + "'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlDeleteAssertionByNode: rc is " + rc;
			this.log.info "samlDeleteAssertionByNode: sql is " + this.sql;
		}

		this.dbConnClose();
	
		return(rc);
	}
	def samlFlushLogin(String nodeid, String useroid, String accountoid) { 
		/*
		*  Method returns nothing 
		*/	
		int rc;

		this.sql = "DELETE from NODE_USER_SAML_ASSERTION where NODE_USER_ID = '$useroid'" + 
			" and NODE_OID != '02' or NODE_OID != '10'"; 
		
		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlFlushLogin: rc is " + rc;
			this.log.info "samlFlushLogin: sql is " + this.sql;
		}

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlFlushLogin: rc is " + rc;
			this.log.info "samlFlushLogin: sql is " + this.sql;
		}

		this.sql = "DELETE from NODE_ACCOUNT where NODE_OID = '" + nodeid + "' " + 
			" and ACCOUNT_OID = '$accountoid'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlFlushLogin: rc is " + rc;
			this.log.info "samlFlushLogin: sql is " + this.sql;
		}

		this.sql = "DELETE from NODE_USER where USER_OID = '$useroid'";

		rc = this.dbConnExecuteUpdate(this.sql);

		if (this.log) {
			this.log.info "samlFlushLogin: rc is " + rc;
			this.log.info "samlFlushLogin: sql is " + this.sql;
		}

		this.dbConnClose();
	}
}