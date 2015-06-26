package dbUtils;
/**
 * Provides Utilities to Interact with Node Data.
 * 
 * @author Pat Gentry
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
   * 1/11/11: Establishment of class.  PJG 
   * 1/13/11: Added javadoc comments.  PJG 
   * 2/9/11:  Removed references to dbIdentifier verified dbConnClose() pjg
   * 10/18/2011: Added nodeGetNodeId() pjg
   * 10/18/2011: Added nodeSetCredentialNotAfter() and nodeSetCredentialNotBefore() pjg
   * 10/18/2011: Added nodeGetNodeCredential() pjg
   * 10/18/2011: Added nodeSetCredentialIssuer() pjg
   * 10/19/2011: Added nodeSetCredentialSerial() pjg
-----------------------------------------------------------
*/
import groovy.sql.Sql;
import java.util.regex.Matcher
import java.util.regex.Pattern

class dbNode extends dbUtils 
{
	def sql = null;
	def log = null;

	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbNode<code> object.
	*/
	dbNode() {
	}
	/**
	*  Creates a new <code>dbNode<code> object with
	*  ability to write to the log.
	*/
	dbNode(log) {
		this.log = log;
	}
	/**
	*  Creates a new <code>dbNode<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbNode(log, String dbIdentifier) {
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
	private dbSetStatusSQL(String nodeId, String status) {
		if (nodeId =~ /urn:dece:org:org:dece:/) {
			this.sql = "update NODE set NODE_STATUS = '%%STATUS%%' where NODE_ID = '%%NODEOID%%'";
			this.sql = sql.replaceAll("%%STATUS%%", status);
			this.sql = sql.replaceAll("%%NODEOID%%", nodeId);
		} else {
			this.sql = "update NODE set NODE_STATUS = '%%STATUS%%' where NODE_OID = hextoraw('%%NODEOID%%')";
			this.sql = sql.replaceAll("%%STATUS%%", status);
			this.sql = sql.replaceAll("%%NODEOID%%", nodeId);
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
	* Extract the NODE row associated with the NODE_OID 
	* supplied. 
	* @version 1.0
	*
	* @param oid The NODE.NODE_OID to extract. 
	* @return XML encapsulated ACCOUNT row.
	*
	*/
	def nodeGet(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(NODE_OID)) NODE_OID, " +
		"to_char(rawtohex(ORG_OID)) ORG_OID, " +
		"NODE_STATUS, " +
		"NODE_ID, " +
		"to_char(rawtohex(PROXY_ORG_ID)) PROXY_ORG_ID, " +
		"ACTIVATION_DATE, " +
		"CREATED_DATE, " +
		"to_char(rawtohex(CREATED_BY)) CREATED_BY, " +
		"UPDATED_DATE, " +
		"UPDATED_BY, " +
		"DEVICE_MANAGEMENT_URL " +
		"from NODE where NODE_OID  = hextoraw('" + oid + "')";

		try
		{	
			xmlData = this.SelectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "nodeGet: sql is " + this.sql;
					this.log.info "nodeGet: xmlData.length() is " + xmlData.length();
					this.log.info "nodeGet: xmlData is $xmlData";
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
	* Extract the NODE_CREDENTIAL row associated with the NODE_OID 
	* supplied. (but not blob)
	* @version 1.0
	*
	* @param oid The NODE.NODE_OID to extract. 
	* @return XML encapsulated ACCOUNT row.
	*
	*/
	def nodeGetNodeCredential(String oid) {
		String xmlData = "";

		this.sql = "select " +
		"to_char(rawtohex(CREDENTIAL_ID)) CREDENTIAL_ID, " +
		"to_char(rawtohex(NODE_OID)) NODE_OID, " +
		"X509_SERIAL_NUMBER, " +
		"X509_ISSUER, " +
		"X509_NOT_BEFORE, " +
		"X509_NOT_AFTER, " +
		"CREATED_DATE " +
		"from NODE_CREDENTIAL where NODE_OID  = hextoraw( '" + oid + "')";

		try
		{	
			xmlData = this.SelectFromDB(this.sql);

			if(xmlData.length() > 0)
			{	
				if (this.log) 
				{
					this.log.info "nodeGetNodeCredential: sql is " + this.sql;
					this.log.info "nodeGetNodeCredential: xmlData.length() is " + xmlData.length();
					this.log.info "nodeGetNodeCredential: xmlData is $xmlData";
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
	* Extract the NODE_ROLE.ROLE value associated with the NODE_OID 
	* supplied. 
	*
	* @param oid The NODE.NODE_OID to extract. 
	* @return The node's role as a String. 
	* @version 1.0
	*/
	def nodeGetRole(String oid) {
		String rData = "";

		this.sql = "SELECT ROLE FROM NODE_ROLE WHERE NODE_OID = hextoraw('" + oid + "')";

		try {
			def rowData = dbConnExecuteQuery(this.sql);
			rData = rowData[0].ROLE;
			
			this.log.info rData
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].ROLE;
					if (this.log) 
					{
						this.log.info "nodeGetRole: sql is " + this.sql;
						this.log.info "nodeGetRole: rowData.size is " + rowData.size;
						this.log.info "nodeGetRole: rowData[0] is " + rowData[0];
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
	* Extract the NODE_ACCOUNT.NODE_OID value associated with the 
	* NODE_ACCOUNT_ID supplied via oid. 
	* @version 1.0
	*
	* @param oid The NODE_ACCOUNT.NODE_ACCOUNT_ID to be extracted. 
	* @return The node's oid as a String. 
	*/
	def nodeGetbyNodeAccountId(String oid) {
		/*
		*  This method will return the nodeid string for the NodeAccountId Passed.
		*/
		String rData = "";

		this.sql = "select to_char(rawtohex(node_oid)) NODE_OID from NODE_ACCOUNT where NODE_ACCOUNT_ID=hextoraw('" + oid + "')";

		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].NODE_OID;
					if (this.log) 
					{
						this.log.info "nodeGetRole: sql is " + this.sql;
						this.log.info "nodeGetRole: rowData.size is " + rowData.size;
						this.log.info "nodeGetRole: rowData[0] is " + rowData[0];
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
	* Extract the NODE.NODE_STATUS value associated with the 
	* NODE_OID supplied via oid. 
	* @version 1.0
	*
	* @param oid The NODE.NODE_OID of the target node. 
	* @return The node's status as a String. 
	*/
	def nodeGetStatus(String oid) {
		/*
		*  This method will return the NODE_ROLE.ROLE string 
		*/
		String rData = "";

		this.sql = "SELECT NODE_STATUS FROM NODE WHERE NODE_OID =hextoraw( '" + oid + "')";

		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].NODE_STATUS;
					if (this.log) 
					{
						this.log.info "nodeGetStatus: sql is " + this.sql;
						this.log.info "nodeGetStatus: rowData.size is " + rowData.size;
						this.log.info "nodeGetStatus: rowData[0] is " + rowData[0];
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
	* Extract the NODE.NODE_OID value associated with the 
	* NODE_ID supplied via id. 
	* @version 1.0
	*
	* @param id The NODE.NODE_ID of the target node. 
	* @return The node's NODE_OID. 
	*/
	def nodeGetNodeId(String id) {
		/*
		*  This method will return the NODE_ROLE.ROLE string 
		*/
		String rData = "";

		this.sql = "SELECT to_char(rawtohex(NODE_OID)) NODE_OID FROM NODE WHERE NODE_ID = '" + id + "'";

		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].NODE_OID;
					if (this.log) 
					{
						this.log.info "nodeGetNodeId: rowData.size is " + rowData.size;
						this.log.info "nodeGetNodeId: rowData[0] is " + rowData[0];
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
	* Extract the node's NODE_CREDENTIAL.X509_NOT_AFTER value. 
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return The node's NODE_CREDENTIAL.X509_NOT_AFTER value
	*/
	def nodeGetCredentialNotAfter(String oid) {
		String rData = "";

		this.sql = "SELECT X509_NOT_AFTER FROM NODE_CREDENTIAL WHERE NODE_OID = hextoraw('" + oid + "')";

		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].X509_NOT_AFTER;
					if (this.log) 
					{
						this.log.info "nodeGetCredentialNotAfter: sql is " + this.sql;
						this.log.info "nodeGetCredentialNotAfter: rowData.size is " + rowData.size;
						this.log.info "nodeGetCredentialNotAfter: rowData[0] is " + rowData[0];
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
	* Extract the node's NODE_CREDENTIAL.X509_NOT_BEFORE value. 
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return The node's NODE_CREDENTIAL.X509_NOT_BEFORE value
	*/
	def nodeGetCredentialNotBefore(String oid) {
		String rData = "";

		this.sql = "SELECT X509_NOT_BEFORE FROM NODE_CREDENTIAL WHERE NODE_OID = hextoraw('" + oid + "')";

		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].X509_NOT_BEFORE;
					if (this.log) 
					{
						this.log.info "nodeGetCredentialNotBefore: sql is " + this.sql;
						this.log.info "nodeGetCredentialNotBefore: rowData.size is " + rowData.size;
						this.log.info "nodeGetCredentialNotBefore: rowData[0] is " + rowData[0];
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
	
	
	/*
	*    Set Methods
	*/

	/**
	* Update the node's NODE_ROLE.ROLE value.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @param role A string representing the role. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetRole(String oid, String role) { 
		int rc = 0;

		this.sql = "UPDATE NODE_ROLE SET ROLE = '" + role + "' where NODE_OID = hextoraw('" + oid + "')";

		try {
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty()) {
				if (this.log) 
				{
					this.log.info "nodeSetRole: sql is " + this.sql;
					this.log.info "nodeSetRole: rc is " + rc; 
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

	/**
	* Update the node's NODE_ACCOUNT.NODE_ACCOUNT_ID value.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @param NodeAccountId A string representing the NODE_ACCOUNT_ID which to set. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetNodeAccountId(String oid, String NodeAccountId) { 
		int rc = 0;

		this.sql = "update NODE_ACCOUNT set NODE_OID='" + oid + "'where NODE_ACCOUNT_ID =hextoraw('" + NodeAccountId + "')";

		try {
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty()) {
				if (this.log) 
				{
					this.log.info "nodeSetNodeAccountId: sql is " + this.sql;
					this.log.info "nodeSetNodeAccountId: rc is " + rc; 
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

	/**
	* Update the node's NODE.NODE_STATUS value.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @param status A string representing the NODE_STATUS which to set. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetStatus(String oid, String status) { 
		int rc = 0;

		try {
			dbSetStatusSQL(oid,status);
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}

	/**
	* Update the node's NODE.NODE_STATUS value to 'active'.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetStatusActive(String oid) { 
		int rc = 0;

		try {
			dbSetStatusSQL(oid,"active");
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* Update the node's NODE.NODE_STATUS value to 'deleted'.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetStatusDeleted(String oid) { 
		int rc = 0;

		try {
			dbSetStatusSQL(oid,"deleted");
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* Update the node's NODE.NODE_STATUS value to 'pending'.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetStatusPending(String oid) { 
		int rc = 0;

		try {
			dbSetStatusSQL(oid,"pending");

			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* Update the node's NODE.NODE_STATUS value to 'suspended'.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetStatusSuspended(String oid) { 
		int rc = 0;

		try {
			dbSetStatusSQL(oid,"suspended");
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* Update the node's NODE.NODE_STATUS value to 'expired'.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetCredentialExpired(String oid) { 
		int rc = 0;

		this.sql = "UPDATE NODE_CREDENTIAL set X509_NOT_AFTER = sysdate - 1 " +
			"where NODE_OID = hextoraw('" + oid + "')";
		
		try {
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* Update the node's NODE_CREDENTIAL.x509_NOT_AFTER value to 
	* sysdate + one year.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetCredentialUnExpired(String oid) { 
		int rc = 0;

		this.sql = "UPDATE NODE_CREDENTIAL set X509_NOT_AFTER = sysdate + 365 " +
			"where NODE_OID = hextoraw('" + oid + "')";
		
		try {
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* Update the node's NODE_CREDENTIAL.x509_NOT_BEFORE value to 
	* sysdate + one day.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetCredentialNotBeforeFuture(String oid) { 
		int rc;

		this.sql = "UPDATE NODE_CREDENTIAL set X509_NOT_BEFORE = sysdate + 1 " +
			"where NODE_OID = hextoraw('" + oid + "')";

		try {
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
		
	}
	/**
	* Update the node's NODE_CREDENTIAL.x509_NOT_BEFORE value to 
	* sysdate - one day.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetCredentialNotBeforePast(String oid) { 
		int rc;

		this.sql = "UPDATE NODE_CREDENTIAL set X509_NOT_BEFORE = sysdate - 1 " +
			"where NODE_OID = hextoraw('" + oid + "')";

		try {
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			this.dbConnClose();
			return rc;
		}
	}
	/**
	* Update the node's NODE_CREDENTIAL.x509_NOT_AFTER value to 
	* sysdate +/- minutes.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @param minutes The number of minutes +/- from sysdate.
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetCredentialNotAfter(String oid, int minutes) { 
		int rc;
		char delim;
		
		if (minutes >= 0) {
			delim = "+";
		} 
		
		this.sql = "UPDATE NODE_CREDENTIAL set X509_NOT_AFTER = sysdate " +
			delim +
			" " + minutes + "/1440 " +
			"where NODE_OID = hextoraw('" + oid + "')";

		try {
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			return rc;
		}
	}
	/**
	* Update the node's NODE_CREDENTIAL.x509_NOT_BEFORE value to 
	* sysdate +/- minutes.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @param minutes The number of minutes +/- from sysdate.
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetCredentialNotBefore(String oid, int minutes) { 
		int rc;
		char delim;
		
		if (minutes >= 0) {
			delim = "+";
		} 
		
		this.sql = "UPDATE NODE_CREDENTIAL set X509_NOT_BEFORE = sysdate " +
			delim +
			" " + minutes + "/1440 " +
			"where NODE_OID = hextoraw('" + oid + "')";

		try {
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			return rc;
		}
	}
	/**
	* Update the node's NODE_CREDENTIAL.X509_ISSUER value to 
	* value supplied in issuer.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @param issuer Issuer string to set.
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetCredentialIssuer(String oid, String issuer) { 
		int rc;
		
		this.sql = "UPDATE NODE_CREDENTIAL set X509_ISSUER = '" +
			issuer + "' " +
			"where NODE_OID = hextoraw('" + oid + "')";

		try {
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			return rc;
		}
	}
	/**
	* Update the node's NODE_CREDENTIAL.X509_SERIAL_NUMBER value to 
	* value supplied in serial.
	* @version 1.0
	*
	* @param oid The NODE_OID of the target node. 
	* @param serial Serail string to set.
	* @return Number or rows updated.  Should be one. 
	*/
	def nodeSetCredentialSerial(String oid, String serial) { 
		int rc;
		
		this.sql = "UPDATE NODE_CREDENTIAL set X509_SERIAL_NUMBER = '" +
			serial + "' " +
			"where NODE_OID = hextoraw('" + oid + "')";

		try {
			rc = dbDoUpdate();
		}
		catch(Throwable e) {
			this.log.info e;
		} 
		finally {
			return rc;
		}
	}
}
