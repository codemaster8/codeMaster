package dbUtils;
/**
 * Provides Utilities to Interact with DECE User Data.
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
  1/12/11: Updated all methods adding finally() block and 
	   removed dbConnClose and return from all
           catch() blocks.   PJG
  1/18/11: Added * @version 1.0 to APIs. pjg
  1/20/11: Added super(log) to (log) constructor.  pjg
  1/25/11: Remove all $ variable expansions from sql.
  1/27/11: Added methods userGetAccountOIDbyUsername() and userGetUserOID(). pjg
  2/9/11:  Removed local references to dbIdentifier.  Verified dbConnClose() pjg
  2/9/11:  Added method userGetfromNodeUserId() pjg/sg.
----------------------------------------------------
*/

import groovy.sql.Sql;
import java.util.regex.Matcher
import java.util.regex.Pattern

class dbUser extends dbUtils 
{
	def sql = null;
	def log = null;

	/*
	  Constructors
	*/
	/**
	*  Creates a new <code>dbUser<code> object.
	*
	*/
	dbUser() {
	}
	/**
	*  Creates a new <code>dbUser<code> object with
	*  ability to write to the log.
	*/
	dbUser(log) {
		super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbUser<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbUser(log, String dbIdentifier) {
		super(log, dbIdentifier);		
		this.log = log;
	}

	/*
	   Private Methods
	*/

	/**
	*   Private method for constructing SQL
	* @version 1.0
	*/
	private dbSetStatusSQL(String user, String status) {
		this.sql = "update ACCOUNT_USER set STATUS = '%%STATUS%%' where USER_OID = hextoraw('%%USER%%')"; 
		this.sql = sql.replaceAll("%%STATUS%%", status);
		this.sql = sql.replaceAll("%%USER%%", user);
	}

	/*
	*   GET Methods
	*/

	/**
	* Extract the ACCOUNT_USER row associated with the USER_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The ACCOUNT_USER.USER_OID to extract. 
	* @return XML encapsulated ACCOUNT_USER row.
	*
	*/
	def userGetRow(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(USER_OID)) USER_OID, " +
		"to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID, " +
		"STATUS, " +
		"PRIVILEGE, " +
		"USERNAME, " +
		"PASSWORD, " +
		"GIVEN_NAME, " +
		"SURNAME, " +
		"DISPLAY_IMAGE_URI, " +
		"PRIMARY_EMAIL, " +
		"TELEPHONE, " +
		"MOBILE_TELEPHONE, " +
		"BIRTH_DATE, " +
		"PUBLISH_NEWS_FLAG, " +
		"NEWSFEED_CLEAR_TIME, " +
		"LOGIN_FAILURES, " +
		"LOGIN_TOKEN_FAILURES, " +
		"LOGIN_SECURITY_QA_FAILURES, " +
		"LAST_LOGIN_DATE, " +
		"CREATED_DATE, " +
		"UPDATED_DATE, " +
		"STREAM_CLEAR_TIME, " +
		"CONFIRMATION_ENDPOINT, " +
		"PRIMARYEMAIL_VERIFICATION_DATE, " +
		"to_char(rawtohex(PRIMARYEMAIL_VERIFICATION_ENTY)) PRIMARYEMAIL_VERIFICATION_ENTY, " +
		"PRIMARYEMAIL_VERIFICATION_ID, " +
		"OLD_PRIMARY_EMAIL, " +
		"EMAIL_CLOCK_START_TIME, " +
		"EMAIL_CLOCK_END_TIME " +
		"from ACCOUNT_USER where USER_OID = '" + oid + "'";
		
		try
		{	
			xmlData = this.SelectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "userGetRow: sql is " + this.sql;
					this.log.info "userGetRow: xmlData.length() is " + xmlData.length();
					this.log.info "userGetRow: xmlData is $xmlData";
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
	* Extract the ACCOUNT_USER.STATUS associated with the USER_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The ACCOUNT_USER.USER_OID to extract. 
	*
	* @return ACCOUNT_USER.STATUS value 
	*
	*/
	def userGetStatus(String oid) {
		String status = "";

		this.sql = "select STATUS from ACCOUNT_USER where " +
			"USER_OID = '" + oid + "'"; 
		
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
						this.log.info "userGetStatus: sql is " + this.sql;
						this.log.info "userGetStatus: rowData.size is " + rowData.size;
						this.log.info "userGetStatus: rowData[0] is " + rowData[0];
						this.log.info "userGetStatus: status is " + status; 
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
			return status;
		}
	}
	
	 /**
			* Extract the latest USER_STATUS_HISTORY details associated with the USER_OID 
			* supplied. 
			* @version 1.0
			*
			* @author Shikha Gupta
			*
			* @param oid The ACCOUNT_USER.USER_OID to extract. 
			*
			* @return XML encapsulated USER_STATUS_HISTORY row
			*
			*/
			def userGetStatusHistory(String oid) {
					
			String xmlData = "";
	
			this.sql = "select " +
			"to_char(rawtohex(USER_OID)) USER_OID, " +
			"STATUS, " +
			"DESCRIPTION, " +
			"CREATED_DATE, " +
			"to_char(rawtohex(CREATED_BY_USER_OID)) CREATED_BY_USER_OID , " +
			"to_char(rawtohex(CREATED_BY_NODE_OID)) CREATED_BY_NODE_OID " +
			"from USER_STATUS_HISTORY where USER_OID = '" + oid + "' " +
			"and HISTORY_OID = (Select max(HISTORY_OID) from USER_STATUS_HISTORY " +
			"where USER_OID = '" + oid + "') ";
			
			this.log.info "userGetStatusHistoryRow: sql is " + this.sql;
			
			try
			{	
				xmlData = this.SelectFromDB(this.sql);
	
				if(xmlData.length() > 0)
				{	
					if (this.log) 
					{
						this.log.info "userGetStatusHistoryRow: sql is " + this.sql;
						this.log.info "userGetStatusHistoryRow: xmlData.length() is " + xmlData.length();
						this.log.info "userGetStatusHistoryRow: xmlData is $xmlData";
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
	* Extract the ACCOUNT_USER.USERNAME associated with the USER_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The ACCOUNT_USER.USER_OID 
	*
	* @return ACCOUNT_USER.USERNAME value 
	*
	*/
	def userGetUsername(String oid) {
		String usern = "";

		this.sql = "select USERNAME from ACCOUNT_USER where " +
			"USER_OID = '" + oid + "'"; 

		
		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					usern = rowData[0].USERNAME;
					if (this.log) 
					{
						this.log.info "userGetUsername: sql is " + this.sql;
						this.log.info "userGetUsername: rowData.size is " + rowData.size;
						this.log.info "userGetUsername: rowData[0] is " + rowData[0];
						this.log.info "userGetUsername: usern is " + usern; 
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
			return usern;
		}
	}

	/**
	* Extract the ACCOUNT_USER.USER_OID associated with the USERNAME 
	* supplied. 
	* @version 1.0
	*
	* @param oid The ACCOUNT_USER.USERNAME 
	*
	* @return ACCOUNT_USER.USER_OID value 
	*
	*/
	def userGetUserOID(String username) {
		String usern = "";

		this.sql = "select to_char(rawtohex(USER_OID)) USER_OID from ACCOUNT_USER where " +
			"USERNAME = '" + username + "'"; 

		
		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					usern = rowData[0].USER_OID;
					if (this.log) 
					{
						this.log.info "userGetUsername: sql is " + this.sql;
						this.log.info "userGetUsername: rowData.size is " + rowData.size;
						this.log.info "userGetUsername: rowData[0] is " + rowData[0];
						this.log.info "userGetUsername: usern is " + usern; 
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
			return usern;
		}
	}
	
	/**
	* Extract the ACCOUNT_USER.ACCOUNT_OID associated with the USERNAME 
	* supplied. 
	* @version 1.0
	*
	* @param oid The ACCOUNT_USER.USERNAME 
	*
	* @return ACCOUNT_USER.ACCOUNT_OID value 
	*
	*/
	def userGetAccountOIDbyUsername(String username) {
		String usern = "";

		this.sql = "select to_char(rawtohex(ACCOUNT_OID)) ACCOUNT_OID from ACCOUNT_USER where " +
			"USERNAME = '" + username + "'"; 

		
		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					usern = rowData[0].ACCOUNT_OID;
					if (this.log) 
					{
						this.log.info "userGetAccountOIDbyUsername: sql is " + this.sql;
						this.log.info "userGetAccountOIDbyUsername: rowData.size is " + rowData.size;
						this.log.info "userGetAccountOIDbyUsername: rowData[0] is " + rowData[0];
						this.log.info "userGetAccountOIDbyUsername: usern is " + usern; 
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
			return usern;
		}
	}
	
	/**
	* Extract the NODE_USER.USER_OID associated with the NODE_USER_ID 
	* supplied. 
	* @version 1.0
	*
	* @param usernodeid The NODE_USER.NODE_USER_ID to query against. 
	*
	* @return NODE_USER.USER_OID value 
	*
	*/
	def userGetfromNodeUserId(String usernodeid) {
		String mdata = "";

		this.sql = "select to_char(rawtohex(USER_OID)) USER_OID from NODE_USER where " +
			"NODE_USER_ID = hextoraw('" + usernodeid + "')"; 
		
		try
		{	
			def rowData = this.dbConnExecuteQuery(this.sql);

			if(!rowData.isEmpty())
			{	
				//if (rowData.size == 1) 
				//{
					mdata = rowData[0].USER_OID;
					if (this.log) 
					{
						this.log.info "userGetfromNodeUserId: sql is " + this.sql;
						this.log.info "userGetfromNodeUserId: rowData.size is " + rowData.size;
						this.log.info "userGetfromNodeUserId: rowData[0] is " + rowData[0];
						this.log.info "userGetfromNodeUserId: mdata is " + mdata; 
					}			
				//}
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
	* Extract all the ACCOUNT_USER.USER_OID associated with the ACCOUNT_OID 
	* supplied. 
	* @version 1.0
	*
	* @param accountoid The ACCOUNT.ACCOUNT_OID
	*
	* @return array of ACCOUNT_USER.USER_OID values for the accountoid supplied.
	*
	*/

	def userGetfromAccID(String accountoid) {
			
			         
			         def rowData = "";
			        
					this.sql = "select to_char(rawtohex(USER_OID)) USER_OID from ACCOUNT_USER where " +
					"ACCOUNT_OID = '" + accountoid + "'"; 
		
				
				try
				{	
					 rowData = this.SelectFromDBArray(this.sql);
					
					    if (rowData.size() > 0) 
						{
						    this.log.info "userGetfromAccID: sql is " + this.sql;
						    this.log.info "userGetfromAccID: rowData.size is " + rowData.size();
						    
						   					    							
							for (int i = 0;i< rowData.size();i++)
							  {
							      this.log.info "userGetfromAccID: USER_OID["+ i +"] is " + rowData[i] ;   
								
							  }  
				               }
				       
				}
				catch(Throwable e)
				{
					this.log.info e;
				}
				finally {
					this.dbConnClose();
					return rowData;
				}
		}
	
	
	/*
	*   SET Methods
	*/

	/**
	* Set user's ACCOUNT_USER.STATUS to the string supplied 
	* in the status parameter.  Note that value supplied for  
	* status is not validated. 
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	* @param status String representing the status. 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatus(String oid, String status) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(oid,status);

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatus: rc is "  + rc;
					this.log.info "userSetStatus: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.STATUS to 'active'.  
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatusActive(String oid) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(oid,"active");
			
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatusActive: rc is "  + rc;
					this.log.info "userSetStatusActive: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.STATUS to 'blocked:eula'.  
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatusBlockedeula(String oid) { 
		int rc = 0;

		try
		{	
			dbSetStatusSQL(oid,"blocked:eula");

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatusBlockedeula: rc is "  + rc;
					this.log.info "userSetStatusBlockedeula: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.STATUS to 'blocked'.  
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatusBlocked(String oid) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(oid,"blocked");

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatusBlocked: rc is "  + rc;
					this.log.info "userSetStatusBlocked: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.STATUS to 'forceddelete'.  
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatusForcedDelete(String oid) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(oid,"forceddelete");

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatusForcedDelete: rc is "  + rc;
					this.log.info "userSetStatusForcedDelete: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.STATUS to 'pending'.  
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatusPending(String oid) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(oid,"pending");

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatusPending: rc is "  + rc;
					this.log.info "userSetStatusPending: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.STATUS to 'deleted'.  
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatusDeleted(String oid) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(oid,"deleted");

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatusDeleted: rc is "  + rc;
					this.log.info "userSetStatusDeleted: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.STATUS to 'suspended'.  
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatusSuspended(String oid) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(oid,"suspended");

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatusSuspended: rc is "  + rc;
					this.log.info "userSetStatusSuspended: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.STATUS to 'other'.  
	* @version 1.0
	*
	* @param user ACCOUNT_USER.USER_OID 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetStatusOther(String oid) { 
		int rc = 0;
		
		try
		{	
			dbSetStatusSQL(oid,"other");

			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetStatusOther: rc is "  + rc;
					this.log.info "userSetStatusOther: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.USERNAME value. 
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	* @param username ACCOUNT_USER.USERNAME value to set. 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userChangeUsername(String oid, String username) { 
		int rc = 0;

		this.sql = "UPDATE ACCOUNT_USER set USERNAME = '" + username + "' where USER_OID = '" + oid + "'";	
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userChangeUsername: rc is "  + rc;
					this.log.info "userChangeUsername: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.PASSWORD value.  Note that
	* no encryption of the password string supplied is
	* performed in the method.
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	* @param passwd ACCOUNT_USER.PASSWORD value to set. 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetPassword(String oid, String passwd) { 
		int rc = 0;

		this.sql = "UPDATE ACCOUNT_USER set PASSWORD = '" + passwd + "' where USER_OID = '" + oid + "'";	
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetPassword: rc is "  + rc;
					this.log.info "userSetPassword: sql is " + this.sql;
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
	* @deprecated See method {@link #userSetPassword}
	* Set user's ACCOUNT_USER.PASSWORD value.  Note that
	* no encryption of the password string supplied is
	* performed in the method.
	* @version 1.0
	*
	* @param oid ACCOUNT_USER.USER_OID 
	* @param passwd ACCOUNT_USER.PASSWORD value to set. 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userChangePassword(String oid, String passwd) { 
		int rc = 0;

		this.sql = "UPDATE ACCOUNT_USER set PASSWORD = '" + passwd + "' where USER_OID = '" + oid + "'";	
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userChangePassword: rc is "  + rc;
					this.log.info "userChangePassword: sql is " + this.sql;
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
	* Set user's ACCOUNT_USER.PRIVILEGE value.  
	* @version 1.0
	*
	* @param username ACCOUNT_USER.USERNAME 
	* @param passwd   ACCOUNT_USER.PRIVILEGE value to set. 
	*
	* @return Number of rows updated.  Should be one. 
	*
	*/
	def userSetPrivilege(String username, String privilege) { 
		int rc = 0;

		this.sql = "UPDATE ACCOUNT_USER set PRIVILEGE = '" + privilege + "' where USERNAME = '" + username + "'";	
		
		try
		{	
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty())
			{
				if (this.log) 
				{
					this.log.info "userSetPrivilege: rc is "  + rc;
					this.log.info "userSetPrivilege: sql is " + this.sql;
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
