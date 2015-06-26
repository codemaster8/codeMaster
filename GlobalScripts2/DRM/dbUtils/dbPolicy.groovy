package dbUtils;

/**
 * Provides Utilities to Interact with a UVVU Policy.
 * 
 * @author Bhavin Bharat Joshi 
 * @version 1.0
 */
class dbPolicy extends dbUtils 
{   	
	private log = null;
	private dbIdentifier = null;
	
	dbPolicy(envIdentifier) 
	{
	    super(envIdentifier);			
		this.dbIdentifier = envIdentifier;
	}	
	dbPolicy(log, envIdentifier) 
	{
	    super(log, envIdentifier);
		this.log = log;		
		this.dbIdentifier = envIdentifier;
	}
	
	def getPoliciesForAUser(String userName, String policiesToGet = null, String policyStatus = null) 
	{   
		private String sqlStr = "";
		private String[][] sqlResults = "";
		private String policyList = "";
		private String[] policyClassesToGet = "";
		
		if(policyStatus == null)
		{
			if(policiesToGet == null)
			{
				sqlStr = """select * from
					(select 
					--p.POLICY_OID,  
					p.POLICY_CLASS,
					pc.POLICY_CLASS_NAME,
					p.STATUS,
					p.CREATED_BY_TYPE,
					p.CREATED_BY CREATED_BY,
					prel.POLICY_ACTOR POLICY_ACTOR,
					--p.CREATED_DATE,
					prel.POLICY_ACTOR_TYPE,
					pre.ENTITY_TYPE,
					pre.ENTITY_ID,
					pres.RESOURCE_TYPE,
					pres.RESOURCE_ID,
					p.POLICY_UPDATED_BY,
					p.POLICY_UPDATED_BY_TYPE,
					p.COUNTRY
					from 
					POLICY p
					inner join
					POLICY_CLASS pc
					on
					pc.POLICY_CLASS_REF = p.POLICY_CLASS
					left outer join
					POLICY_RELATIONSHIP prel
					on
					p.POLICY_OID = prel.POLICY_OID
					left outer join
					POLICY_REQUESTING_ENTITY pre
					on
					p.POLICY_OID = pre.POLICY_OID
					left outer join				
					POLICY_RESOURCE pres
					on
					p.POLICY_OID = pres.POLICY_OID				
					order by 
					p.CREATED_DATE desc) a
					where 
					(a.POLICY_ACTOR = (select user_oid from account_user where lower(username) = lower('""" + userName + """')) or a.POLICY_ACTOR = (select account_oid from account_user where lower(username) = lower('""" + userName + """')))
					and
					a.STATUS = 'active'
					order by
					a.POLICY_CLASS asc""";
			}
			else
			{
				policyClassesToGet = policiesToGet.split(",");
				policyList = policyClassesToGet[0].trim();
				for(int j = 1; j < policyClassesToGet.size(); j++)
				{
					policyList = policyList + "," + policyClassesToGet[j].trim();				
				}				
				sqlStr = """select * from
					(select 
					--p.POLICY_OID,  
					p.POLICY_CLASS,
					pc.POLICY_CLASS_NAME,
					p.STATUS,
					p.CREATED_BY_TYPE,
					p.CREATED_BY CREATED_BY,
					prel.POLICY_ACTOR POLICY_ACTOR,
					--p.CREATED_DATE,
					prel.POLICY_ACTOR_TYPE,
					pre.ENTITY_TYPE,
					pre.ENTITY_ID,
					pres.RESOURCE_TYPE,
					pres.RESOURCE_ID,
					p.POLICY_UPDATED_BY,
					p.POLICY_UPDATED_BY_TYPE,
					p.COUNTRY
					from 
					POLICY p
					inner join
					POLICY_CLASS pc
					on
					pc.POLICY_CLASS_REF = p.POLICY_CLASS
					left outer join
					POLICY_RELATIONSHIP prel
					on
					p.POLICY_OID = prel.POLICY_OID
					left outer join
					POLICY_REQUESTING_ENTITY pre
					on
					p.POLICY_OID = pre.POLICY_OID
					left outer join				
					POLICY_RESOURCE pres
					on
					p.POLICY_OID = pres.POLICY_OID				
					order by 
					p.CREATED_DATE desc) a
					where 
					(a.POLICY_ACTOR = (select user_oid from account_user where lower(username) = lower('""" + userName + """')) or a.POLICY_ACTOR = (select account_oid from account_user where lower(username) = lower('""" + userName + """')))
					and
					a.STATUS = 'active'
					and
					a.POLICY_CLASS in (""" + policyList + """)					
					order by
					a.POLICY_CLASS asc""";					
			}
		}
		else
		{
			if(policiesToGet == null)
			{
				sqlStr = """select * from
					(select 
					--p.POLICY_OID,  
					p.POLICY_CLASS,
					pc.POLICY_CLASS_NAME,
					p.STATUS,
					p.CREATED_BY_TYPE,
					p.CREATED_BY CREATED_BY,
					prel.POLICY_ACTOR POLICY_ACTOR,
					--p.CREATED_DATE,
					prel.POLICY_ACTOR_TYPE,
					pre.ENTITY_TYPE,
					pre.ENTITY_ID,
					pres.RESOURCE_TYPE,
					pres.RESOURCE_ID,
					p.POLICY_UPDATED_BY,
					p.POLICY_UPDATED_BY_TYPE,
					p.COUNTRY
					from 
					POLICY p
					inner join
					POLICY_CLASS pc
					on
					pc.POLICY_CLASS_REF = p.POLICY_CLASS
					left outer join
					POLICY_RELATIONSHIP prel
					on
					p.POLICY_OID = prel.POLICY_OID
					left outer join
					POLICY_REQUESTING_ENTITY pre
					on
					p.POLICY_OID = pre.POLICY_OID
					left outer join				
					POLICY_RESOURCE pres
					on
					p.POLICY_OID = pres.POLICY_OID				
					order by 
					p.CREATED_DATE desc) a
					where 
					(a.POLICY_ACTOR = (select user_oid from account_user where lower(username) = lower('""" + userName + """')) or a.POLICY_ACTOR = (select account_oid from account_user where lower(username) = lower('""" + userName + """')))
					and
					a.STATUS = '""" + policyStatus + """'
					order by
					a.POLICY_CLASS asc""";
			}
			else
			{
				policyClassesToGet = policiesToGet.split(",");
				policyList = policyClassesToGet[0].trim();
				for(int j = 1; j < policyClassesToGet.size(); j++)
				{
					policyList = policyList + "," + policyClassesToGet[j].trim();				
				}
				sqlStr = """select * from
					(select 
					--p.POLICY_OID,  
					p.POLICY_CLASS,
					pc.POLICY_CLASS_NAME,
					p.STATUS,
					p.CREATED_BY_TYPE,
					p.CREATED_BY CREATED_BY,
					prel.POLICY_ACTOR POLICY_ACTOR,
					--p.CREATED_DATE,
					prel.POLICY_ACTOR_TYPE,
					pre.ENTITY_TYPE,
					pre.ENTITY_ID,
					pres.RESOURCE_TYPE,
					pres.RESOURCE_ID,
					p.POLICY_UPDATED_BY,
					p.POLICY_UPDATED_BY_TYPE,
					p.COUNTRY
					from 
					POLICY p
					inner join
					POLICY_CLASS pc
					on
					pc.POLICY_CLASS_REF = p.POLICY_CLASS
					left outer join
					POLICY_RELATIONSHIP prel
					on
					p.POLICY_OID = prel.POLICY_OID
					left outer join
					POLICY_REQUESTING_ENTITY pre
					on
					p.POLICY_OID = pre.POLICY_OID
					left outer join				
					POLICY_RESOURCE pres
					on
					p.POLICY_OID = pres.POLICY_OID				
					order by 
					p.CREATED_DATE desc) a
					where 
					(a.POLICY_ACTOR = (select user_oid from account_user where lower(username) = lower('""" + userName + """')) or a.POLICY_ACTOR = (select account_oid from account_user where lower(username) = lower('""" + userName + """')))
					and
					a.STATUS = '""" + policyStatus + """"'
					and
					a.POLICY_CLASS in (""" + policyList + """)
					order by
					a.POLICY_CLASS asc""";
			}
		}   
		
		try 
		{		
			sqlResults = selectFromDBArray(sqlStr, this.dbIdentifier);			
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}		
		return sqlResults;
	}
    
	def deletePoliciesForAUser(String userName, String policiesToDelete = null) 
	{   
		private String sqlStr = "";		
		private String[] sqlResult = "";
		private String[] sqlResultPolicyList = "";
		private String existingPolicyOid = "";
		private String existingPolicyListOid = "";
		private String policyList = "";
		private String[] policyClassesToDelete = "";
		int deletedRowCount = 0;
				
		if(policiesToDelete == null)
		{
			sqlStr = """select to_char(rawtohex(POLICY_OID)) POLICY_OID from
				(select 
				p.POLICY_OID,
				prel.POLICY_ACTOR POLICY_ACTOR
				from 
				POLICY p				
				inner join
				POLICY_RELATIONSHIP prel
				on
				p.POLICY_OID = prel.POLICY_OID
				where
				prel.POLICY_ACTOR = (select user_oid from account_user where lower(username) = lower('""" + userName + """')) 
				or 
				prel.POLICY_ACTOR = (select account_oid from account_user where lower(username) = lower('""" + userName + """'))
				)""";				
		}
		else
		{			   
			policyClassesToDelete = policiesToDelete.split(",");
			policyList = policyClassesToDelete[0].trim();
			for(int j = 1; j < policyClassesToDelete.size(); j++)
			{
				policyList = policyList + "," + policyClassesToDelete[j].trim();				
			}						
			sqlStr = """select to_char(rawtohex(POLICY_OID)) POLICY_OID from
				(select 
				p.POLICY_OID,
				p.POLICY_CLASS,
				prel.POLICY_ACTOR POLICY_ACTOR
				from 
				POLICY p				
				inner join
				POLICY_RELATIONSHIP prel
				on
				p.POLICY_OID = prel.POLICY_OID
				where 
				(prel.POLICY_ACTOR = (select user_oid from account_user where lower(username) = lower('""" + userName + """')) 
				or 
				prel.POLICY_ACTOR = (select account_oid from account_user where lower(username) = lower('""" + userName + """')))								
				and 
				p.POLICY_CLASS in (""" + policyList + "))";
		}
		
		try 
		{ 											  
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);			
			for(int i = 0; i < sqlResult.size(); i++)
			{				   			
				existingPolicyOid = sqlResult[i]; 
									
				// POLICY_LIST_GROUP	
				sqlStr = "delete from POLICY_LIST_GROUP where POLICY_OID = hextoraw('" + existingPolicyOid + "')";
				deletedRowCount = deleteFromDB(sqlStr, this.dbIdentifier);
				this.log.info "I deleted '" + deletedRowCount + "' rows using SQL '" + sqlStr + "'.";
				
				sqlStr = "select POLICY_LIST_OID from POLICY_LIST_GROUP where POLICY_OID = hextoraw('" + existingPolicyOid + "')";				
				sqlResultPolicyList = selectFromDBArray(sqlStr, this.dbIdentifier);				
				for(int j = 0; j < sqlResultPolicyList.size(); j++)
				{
					existingPolicyListOid = sqlResultPolicyList[j];
					 
					sqlStr = "delete from POLICY_LIST where policy_list_oid = '" + existingPolicyListOid + "'";
					deletedRowCount = deleteFromDB(sqlStr, this.dbIdentifier);
					this.log.info "I deleted '" + deletedRowCount + "' rows using SQL '" + sqlStr + "'.";					 
				}
				
				// POLICY_RESOURCE
				sqlStr = "delete from POLICY_RESOURCE where POLICY_OID = hextoraw('" + existingPolicyOid + "')";
				deletedRowCount = deleteFromDB(sqlStr, this.dbIdentifier);
				this.log.info "I deleted '" + deletedRowCount + "' rows using SQL '" + sqlStr + "'.";
				
				// POLICY_REQUESTING_ENTITY
				sqlStr = "delete from POLICY_REQUESTING_ENTITY where POLICY_OID = hextoraw('" + existingPolicyOid + "')";
				deletedRowCount = deleteFromDB(sqlStr, this.dbIdentifier);
				this.log.info "I deleted '" + deletedRowCount + "' rows using SQL '" + sqlStr + "'.";
				
				// POLICY_RELATIONSHIP
				sqlStr = "delete from POLICY_RELATIONSHIP where POLICY_OID = hextoraw('" + existingPolicyOid + "')";
				deletedRowCount = deleteFromDB(sqlStr, this.dbIdentifier);
				this.log.info "I deleted '" + deletedRowCount + "' rows using SQL '" + sqlStr + "'.";
				
				// POLICY
				sqlStr = "delete from POLICY where POLICY_OID = hextoraw('" + existingPolicyOid + "')";
				deletedRowCount = deleteFromDB(sqlStr, this.dbIdentifier);
				this.log.info "I deleted '" + deletedRowCount + "' rows using SQL '" + sqlStr + "'.";				
			}						
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}	
	}
    
	def getLatestTOUForAGeography(String consentCountry, String touFormat) 
    {   
		private String sqlStr = "";
		private String[] sqlResult = "";
		private String consentVersion = "";
		private String touVersion = "";
		private String portalBase = "";
		
		sqlStr = "select value from sys_config_106 where name = 'dece.uvvu.content.host'";
		try 
	    {		
		    sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
            for(int i = 0; i < sqlResult.size(); i++)
            {
                portalBase = sqlResult[i]; 
            }						
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}
		sqlStr = """select TOU_VERSION from
			(select
			(CONSENT_VERSION) TOU_VERSION			
			from
			CONSENT
			where
			lower(CONSENT_URN) = lower('urn:dece:type:policy:TermsOfUse')
			and
			COUNTRY = '""" + consentCountry + """'
			and
			effective_date <= sysdate
			order by
			effective_date desc)
			where rownum < 2""";	
		try 
		{		
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				consentVersion = sqlResult[i]; 
			}
			touVersion = portalBase + "Consent/Text/" + consentCountry + "/urn:dece:type:policy:TermsOfUse:" + consentVersion + "/" + touFormat;			
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}		
		return touVersion;
	}
    
    def getLatestTOUForAGeographyLanguage(String consentCountry, String touFormat, String touLanguage) 
    {   
		private String sqlStr = "";
		private String[] sqlResult = "";
		private String consentVersion = "";
		private String touVersion = "";
		private String portalBase = "";
		
		sqlStr = "select value from sys_config_106 where name = 'dece.coord.geo.portalbase'";
		try 
	    {		
		    sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
            for(int i = 0; i < sqlResult.size(); i++)
            {
                portalBase = sqlResult[i]; 
            }						
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}
		sqlStr = """select TOU_VERSION from
			(select
			(CONSENT_VERSION) TOU_VERSION			
			from
			CONSENT
			where
			lower(CONSENT_URN) = lower('urn:dece:type:policy:TermsOfUse')
			and
			COUNTRY = '""" + consentCountry + """'
			and
			effective_date <= sysdate
			order by
			effective_date desc)
			where rownum < 2""";	
		try 
		{		
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				consentVersion = sqlResult[i]; 
			}
			touVersion = portalBase + "Consent/Text/" + consentCountry + "-" + touLanguage + "/urn:dece:type:policy:TermsOfUse:" + consentVersion + "/" + touFormat;			
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}		
		return touVersion;
	}
    
    def getLatestTOUForAGeographyCHost(String consentCountry, String touFormat) 
    {   
		private String sqlStr = "";
		private String[] sqlResult = "";
		private String consentVersion = "";
		private String touVersion = "";
		private String portalBase = "";
		
		sqlStr = "select value from sys_config_107 where name = 'dece.uvvu.content.host'";
		try 
	    {		
		    sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
            for(int i = 0; i < sqlResult.size(); i++)
            {
                portalBase = sqlResult[i]; 
            }						
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}
		sqlStr = """select TOU_VERSION from
			(select
			(CONSENT_VERSION) TOU_VERSION			
			from
			CONSENT
			where
			lower(CONSENT_URN) = lower('urn:dece:type:policy:TermsOfUse')
			and
			COUNTRY = '""" + consentCountry + """'
			and
			effective_date <= sysdate
			order by
			effective_date desc)
			where rownum < 2""";	
		try 
		{		
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				consentVersion = sqlResult[i]; 
			}
			touVersion = portalBase + "Consent/Text/" + consentCountry + "/urn:dece:type:policy:TermsOfUse:" + consentVersion + "/" + touFormat;			
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}		
		return touVersion;
	}
    
    def getLatestTOUForAGeographyLanguageCHost(String consentCountry, String touFormat, String touLanguage) 
    {   
		private String sqlStr = "";
		private String[] sqlResult = "";
		private String consentVersion = "";
		private String touVersion = "";
		private String portalBase = "";
		
		sqlStr = "select value from sys_config_107 where name = 'dece.uvvu.content.host'";
		try 
	    {		
		    sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
            for(int i = 0; i < sqlResult.size(); i++)
            {
                portalBase = sqlResult[i]; 
            }						
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}
		sqlStr = """select TOU_VERSION from
			(select
			(CONSENT_VERSION) TOU_VERSION			
			from
			CONSENT
			where
			lower(CONSENT_URN) = lower('urn:dece:type:policy:TermsOfUse')
			and
			COUNTRY = '""" + consentCountry + """'
			and
			effective_date <= sysdate
			order by
			effective_date desc)
			where rownum < 2""";	
		try 
		{		
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				consentVersion = sqlResult[i]; 
			}
			touVersion = portalBase + "Consent/Text/" + consentCountry + "-" + touLanguage + "/urn:dece:type:policy:TermsOfUse:" + consentVersion + "/" + touFormat;			
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}		
		return touVersion;
	}

    def getLatestTOUForAGeographyDEVSRINT1(String consentCountry, String touFormat) 
    {   
		private String sqlStr = "";
        private String[] sqlResult = "";
		private String consentVersion = "";
		private String touVersion = "";
		private String portalBase = "";
		
		sqlStr = "select value from sys_config_106 where name = 'dece.coord.geo.portalbase'";
		try 
		{		
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				portalBase = sqlResult[i]; 
			}						
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}
		sqlStr = """select TOU_VERSION from
			(select
			(CONSENT_VERSION) TOU_VERSION			
			from
			CONSENT
			where
			lower(CONSENT_URN) = lower('urn:dece:type:policy:TermsOfUse')
			and
			COUNTRY = '""" + consentCountry + """'
			and
			effective_date <= sysdate
			order by
			effective_date desc)
			where rownum < 2""";	
		try 
		{		
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				consentVersion = sqlResult[i]; 
			}
			touVersion = portalBase + "Consent/Text/" + consentCountry + "/urn:dece:type:policy:TermsOfUse:" + consentVersion + "/" + touFormat;			
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}		
		return touVersion;
	}
    
    def getLatestTOUForAGeographyR2(String consentCountry) 
    {   
		private String sqlStr = "";
		private String[] sqlResult = "";
		private String touVersion = "";
		sqlStr = """select TOU_VERSION from
			(select
			('urn:dece:agreement:enduserlicenseagreement:' || CONSENT_OID) TOU_VERSION			
			from
			CONSENT
			where
			lower(CONSENT_URN) = lower('urn:dece:agreement:enduserlicenseagreement:')
			and
			COUNTRY = '""" + consentCountry + """'
			and
			effective_date <= sysdate
			order by
			consent_version desc)
			where rownum < 2""";	
		try 
		{		
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				touVersion = sqlResult[i]; 
			}			
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}		
		return touVersion;
	}
    
    def getLatestTOUForAGeographyR3_2(String consentCountry) 
    {   
		private String sqlStr = "";
		private String[] sqlResult = "";
		private String touVersion = "";
		sqlStr = """select TOU_VERSION from
			(select
			('urn:dece:agreement:enduserlicenseagreement:' || CONSENT_OID) TOU_VERSION			
			from
			CONSENT
			where
			lower(CONSENT_URN) = lower('urn:dece:type:policy:TermsOfUse')
			and
			COUNTRY = '""" + consentCountry + """'
			and
			effective_date <= sysdate
			order by
			consent_version desc)
			where rownum < 2""";	
		try 
		{		
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				touVersion = sqlResult[i]; 
			}			
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}		
		return touVersion;
	}
    
	def Integer setPolicyStatusByActor(Integer policyClass, String policyActor, String policyStatus) 
	{
		private String sqlStr = "";
		private Integer rowCount = 0;
		
		sqlStr = """update POLICY
			set status = '""" + policyStatus + """'
			where
			POLICY_OID in
			(select
			prel.POLICY_OID
			from
			POLICY p
			left outer join
			POLICY_RELATIONSHIP prel
			on
			p.POLICY_OID = prel.POLICY_OID
			where
			POLICY_ACTOR = '""" + policyActor + """')
			and
			POLICY_CLASS = """ + policyClass;		
		
		try 
		{
			rowCount = updateDB(sqlStr);
			this.log.info "'" + sqlStr + "' updated " + rowCount + " record(s).";
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}
		return rowCount;
	}
    
	def Integer getPolicyClassRef(String policyClassName) 
	{
		private String sqlStr = "";
		private String policyClassRef = 0;	
		private String[] sqlResult = "";	
		try 
		{
			sqlStr = """select POLICY_CLASS_REF from POLICY_CLASS
						where POLICY_CLASS_NAME = '""" + policyClassName + "'";
			
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			
			for(int i = 0; i < sqlResult.size(); i++)
			{
				policyClassRef = sqlResult[i]; 
			}
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}
		return policyClassRef.toInteger();
	}
    
	def String[] getPolicyTablesRowCount() 
	{
		private String sqlStr = "";
		private String[] sqlResult = "";
		private String policyListGroupCount = "";
		private String policyListCount = "";
		private String policyCount = "";
		private String policyRelationshipCount = "";
		private String policyResourceCount = "";
		private String policyRequestingEntityCount = "";	
		
		try 
		{
			sqlStr = "select count(*) from POLICY_LIST_GROUP";
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				policyListGroupCount = sqlResult[i]; 
			}
			
			sqlStr = "select count(*) from POLICY_LIST";
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				policyListCount = sqlResult[i]; 
			}
			
			sqlStr = "select count(*) from POLICY";
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				policyCount = sqlResult[i]; 
			}
			
			sqlStr = "select count(*) from POLICY_RELATIONSHIP";
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				policyRelationshipCount = sqlResult[i]; 
			}
			
			sqlStr = "select count(*) from POLICY_RESOURCE";
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				policyResourceCount = sqlResult[i]; 
			}
			
			sqlStr = "select count(*) from POLICY_REQUESTING_ENTITY";
			sqlResult = selectFromDBArray(sqlStr, this.dbIdentifier);
			for(int i = 0; i < sqlResult.size(); i++)
			{
				policyRequestingEntityCount = sqlResult[i]; 
			}
		}
		catch(Throwable e) 
		{
			this.log.info e;
		}
		return [policyListGroupCount, policyListCount, policyCount, policyRelationshipCount, policyResourceCount, policyRequestingEntityCount];
	}
    
	def dbConnClose() 
	{
		super.dbConnClose();
	}
}