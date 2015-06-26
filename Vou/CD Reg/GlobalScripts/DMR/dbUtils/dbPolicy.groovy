package dbUtils; 

/**
 * Provides Utilities to Interact with Policy Data.
 * 
 * @author Pat Gentry
 * @version 1.0
 *
 *
*/

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*    
-----------------------------------------------------------
  C H A N G E     L O G
  Version
    Change   Person

  1.0
  ---
   1/18/11: Establishment of class.  PJG 
   2/9/11:  Removed dbIdentifier and verified dbConnClose().  pjg
-----------------------------------------------------------
*/


class dbPolicy extends dbUtils 
{
	def sql = null;
	private log = null;

	/*
	*  Constructors
	*/
	/**
	*  Creates a new <code>dbPolicy<code> object.
	*/
	dbPolicy(log) {
		super(log);
		this.log = log;
	}
	/**
	*  Creates a new <code>dbPolicy<code> object.
	*/
	dbPolicy() {
	}
	/**
	*  Creates a new <code>dbPolicy<code> object with
	*  ability to write to the log and execute methods 
	*  against the specified QA DB.
	*/
	dbPolicy(log, String dbIdentifier) {
		super(log, dbIdentifier);
		this.log = log;
	}


	/*
	*  Private Methods
	*/
	/**
	* Private method for constructing SQL
	* @version 1.0
	*/
	private sqlPolicyStatus(String oid, String status) {
		//this.sql = "update POLICY set STATUS = '" + status + "' where POLICY_OID = hextoraw( " + oid + ")"; 
		this.sql = "update POLICY set STATUS = '" + status + "' where POLICY_OID = hextoraw('" + oid + "')"; 
	}

	/*
	*  GET Policy Methods
	*/
	/**
	* Extract the POLICY.STATUS value associated with the 
	* POLICY_OID supplied via oid. 
	* @version 1.0
	*
	* @param oid The POLICY.POLICY_OID of the target policy. 
	* @return The policy's STATUS as a String. 
	*/
	def policyGetStatus(String oid) {
		String rData = "";

		this.sql = "SELECT STATUS FROM POLICY WHERE POLICY_OID = hextoraw('" + oid + "')";

		try {
			def rowData = dbConnExecuteQuery(this.sql);
			
			if(!rowData.isEmpty())
			{	
				if (rowData.size == 1) 
				{
					rData = rowData[0].STATUS;
					if (this.log) 
					{
						this.log.info "policyGetSTatus: rowData.size is " + rowData.size;
						this.log.info "policyGetSTatus: rowData[0] is " + rowData[0];
						this.log.info "policyGetSTatus: rData is $rData";
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
	*  Set Policy Methods
	*/
	/**
	* For the POLICY_OID supplied set the POLICY.STATUS to the value supplied in status.
	* @version 1.0
	*
	* @param oid The POLICY.POLICY_OID to update. 
	* @param stats The status string to insert into POLICY.STATUS.
	* @return Number of rows updated.  Should be one.
	*
	*/
	def policySetStatus(String oid, String status) { 
		int rc = 0;

		sqlPolicyStatus(oid, status);
		
		try {
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty()) {
				if (this.log) 
				{
					this.log.info "policySetStatus: rc is " + rc; 
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
	* For the POLICY_ACTOR and POLICY_CLASS supplied set the POLICY.STATUS to the value supplied in policyStatus.
	* @author Bhavin Bharat Joshi
	* @version 1.0
	*
	* @param policyClass The POLICY.POLICY_CLASS to update.
	* @param policyActor The POLICY_RELATIONSHIP.POLICY_ACTOR to update.
	* @param policyStatus The status string to insert into POLICY.STATUS.
	* @return Number of rows updated.  Should be one.
	*
	*/
	def policySetStatusActor(Integer policyClass, String policyActor, String policyStatus) 
	{
		private sqlStr = "";
		
		sqlStr = """update policy
		 set status = '""" + policyStatus + """'
		 where
		 policy_oid in
		 (select
		 prel.policy_oid
		 from
		 policy p,
		 policy_relationship prel
		 where
		 p.policy_oid = prel.policy_oid(+)
		 and
		 policy_actor = hextoraw('""" + policyActor + """'))
		 and
		 policy_class = """ + policyClass
		
		int rowCount = 0;	
		
		try 
		{
			rowCount = UpdateDB(sqlStr);
			if(this.log)
			{
				this.log.info "'" + sqlStr + "' updated " + rowCount + " records.";
			}
		}
		catch(Throwable e) 
		{
			if(this.log) 
			{
				this.log.info e;
			}
		}
		finally 
		{
			this.dbConnClose();
			return rowCount;
		}
	}
	
	/**
	* For the POLICY_OID supplied set the POLICY.STATUS to the 'active'.
	* @version 1.0
	*
	* @param oid The POLICY.POLICY_OID to update. 
	* @return Number of rows updated.  Should be one.
	*
	*/
	def policySetActive(String policyOid) { 
		int rc = 0;

		sqlPolicyStatus(policyOid, 'active');
		
		try {
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty()) {
				if (this.log) 
				{
					this.log.info "policySetActive: sql is " + this.sql;
					this.log.info "policySetActive: rc is " + rc; 
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
	* For the POLICY_OID supplied set the POLICY.STATUS to the 'deleted'.
	* @version 1.0
	*
	* @param oid The POLICY.POLICY_OID to update. 
	* @return Number of rows updated.  Should be one.
	*
	*/
	def policySetDeleted(String policyOid) { 
	int rc = 0;

		sqlPolicyStatus(policyOid, 'deleted');
		
		try {
			rc = this.dbConnExecuteUpdate(this.sql);

			if(!rc.toString().isEmpty()) {
				if (this.log) 
				{
					this.log.info "policySetDeleted: rc is " + rc; 
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
}

