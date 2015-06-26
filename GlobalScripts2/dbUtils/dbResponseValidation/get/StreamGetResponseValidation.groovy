//Copyright (c) 2014 Sanjeet Singh - Neustar, Inc. All Rights Reserved.
package dbUtils.dbResponseValidation.get;


public class StreamGetResponseValidation extends dbUtils.dbUtils{
	def log = null;
	def sql = null;

	//Below 3 variables need to be set before calling this groovy.
	def accountOid = null;
	def nodeUserId = null;
	def deleteStreamForStatusChg = null;

	def expStreamHandleOid = null;
	def expStreamClientNickname = null;
	def expRequestingUserID = null;
	def expRightsTokenID = null;
	def expExpirationDateTime = null;
	def expSGConfidence = null;
	def expSGCalculationMethod = null;
	def expSubDividedGeolocation = null;
	def expViaProxy = null;
	def expRSCurrentCreationDate = null;
	def expRSCurrentCreatedBy = null;
	def expRSCurrentDeletionDate = null;
	def expRSCurrentDeletedBy = null;		
	def expRSCurrentValue = null;
	def expRSCurrentDescription = null;
	def expRSHistoryModificationDate = null;
	def expRSHistoryModifiedBy = null;
	def expRSHistoryValue = null;
	def expRSHistoryDescription = null;



	public StreamGetResponseValidation(log, envIdentifier){
		super(log, envIdentifier);
		this.log = log;
		this.log.info("StreamGetResponseValidation object created successfully");
	}

	public verifyStreamViewResponseData(def testSuiteName, def testCaseName, def testStepName, def testRunner, def context) throws Exception{
		/*
		 * Prepare expected values from response
		 */
		def restTestStep = testRunner.testCase.testSuite.project.getTestSuiteByName(testSuiteName).getTestCaseByName(testCaseName).getTestStepByName(testStepName);
		def streamViewResponseInXml = restTestStep.getTestRequest().getResponseContentAsXml();
		def xmlSlurperObject = new XmlSlurper().parseText(streamViewResponseInXml);

		expStreamHandleOid = xmlSlurperObject.@StreamHandleID.text();
		expStreamClientNickname = xmlSlurperObject.StreamClientNickname.text();
		expRequestingUserID = xmlSlurperObject.RequestingUserID.text();
		expRightsTokenID = xmlSlurperObject.RightsTokenID.text();
		expExpirationDateTime = xmlSlurperObject.ExpirationDateTime.text();
		expSGConfidence = xmlSlurperObject.SubDividedGeolocation.@Confidence.text();
		expSGCalculationMethod = xmlSlurperObject.SubDividedGeolocation.@CalculationMethod.text();
		expSubDividedGeolocation = xmlSlurperObject.SubDividedGeolocation.text();
		expViaProxy = xmlSlurperObject.SubDividedGeolocation.@ViaProxy.text();
		expRSCurrentCreationDate = xmlSlurperObject.ResourceStatus.Current.@CreationDate.text();
		expRSCurrentCreatedBy = xmlSlurperObject.ResourceStatus.Current.@CreatedBy.text();
		expRSCurrentValue = xmlSlurperObject.ResourceStatus.Current.Value.text();
		expRSCurrentDescription = xmlSlurperObject.ResourceStatus.Current.Description.text();
		expRSCurrentDeletionDate = xmlSlurperObject.ResourceStatus.Current.@DeletionDate.text();
		expRSCurrentDeletedBy = xmlSlurperObject.ResourceStatus.Current.@DeletedBy.text();
		expRSHistoryModificationDate =  xmlSlurperObject.ResourceStatus.History.Prior.@ModificationDate.text();
		expRSHistoryModifiedBy =   xmlSlurperObject.ResourceStatus.History.Prior.@ModifiedBy.text();
		expRSHistoryValue =   xmlSlurperObject.ResourceStatus.History.Prior.Value.text();
		expRSHistoryDescription =   xmlSlurperObject.ResourceStatus.History.Prior.Description.text();


		this.log.info("Expected values extracted from StreamView response!!!!");
		this.log.info("expStreamHandleOid :  '" + expStreamHandleOid + "'");
		this.log.info("expStreamClientNickname :  '" + expStreamClientNickname + "'");
		this.log.info("expRequestingUserID :  '" + expRequestingUserID + "'");
		this.log.info("expRightsTokenID :  '" + expRightsTokenID + "'");
		this.log.info("expExpirationDateTime :  '" + expExpirationDateTime + "'");
		this.log.info("expSGConfidence :  '" + expSGConfidence + "'");
		this.log.info("expSGCalculationMethod :  '" + expSGCalculationMethod + "'");
		this.log.info("expSubDividedGeolocation :  '" + expSubDividedGeolocation + "'");
		this.log.info("expViaProxy :  '" + expViaProxy + "'");
		this.log.info("expRSCurrentCreationDate :  '" + expRSCurrentCreationDate + "'");
		this.log.info("expRSCurrentCreatedBy :  '" + expRSCurrentCreatedBy + "'");
		this.log.info("expRSCurrentValue :  '" + expRSCurrentValue + "'");
		this.log.info("expRSCurrentDescription :  '" + expRSCurrentDescription + "'");
		this.log.info("expRSCurrentDeletionDate :  '" + expRSCurrentDeletionDate + "'");
		this.log.info("expRSCurrentDeletedBy :  '" + expRSCurrentDeletedBy + "'");
		this.log.info("expRSHistoryModificationDate :  '" + expRSHistoryModificationDate + "'");
		this.log.info("expRSHistoryModifiedBy :  '" + expRSHistoryModifiedBy + "'");
		this.log.info("expRSHistoryValue :  '" + expRSHistoryValue + "'");
		this.log.info("expRSHistoryDescription :  '" + expRSHistoryDescription + "'");

		String sql = "select to_char(rawtohex(STREAM_HANDLE_OID)) STREAM_HANDLE_OID,STREAM_CLIENT_NICKNAME,to_char(rawtohex(USER_OID)) USER_OID,to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, EXPIRATION_DATE,CONFIDENCE,CALCULATION_METHOD,GEO_LOCATION_VALUE,VIA_PROXY,CREATED_DATE,to_char(rawtohex(CREATED_BY)) CREATED_BY,STATUS,Closed_Date,Closed_By_Org_Id from Stream_data where account_oid=hextoraw('" + accountOid +"')"
				this.log.info("Get Actual data from database");
		this.log.info("Sql : '" + sql + "'");

		def streamViewRespDataInXmlFormat = SelectFromDB(sql);
		this.log.info "Xml Data returned by DB : '" + streamViewRespDataInXmlFormat + "'";
		xmlSlurperObject = new XmlSlurper().parseText(streamViewRespDataInXmlFormat);
		def actStreamHandleOid = "urn:dece:streamhandleid:org:dece:" +xmlSlurperObject.ResultSet.Row.STREAM_HANDLE_OID.text();
		def actStreamClientNickname = xmlSlurperObject.ResultSet.Row.STREAM_CLIENT_NICKNAME.text();
		def actRequestingUserID = "urn:dece:userid:org:dece:" + nodeUserId;
		def actRightsTokenID = "urn:dece:rightstokenid:org:dece:" +xmlSlurperObject.ResultSet.Row.RIGHTS_TOKEN_OID.text();
		def actExpirationDateTime = xmlSlurperObject.ResultSet.Row.EXPIRATION_DATE.text();
		def actSGConfidence = xmlSlurperObject.ResultSet.Row.CONFIDENCE.text();
		def actSGCalculationMethod = "urn:dece:type:geoloc:" + xmlSlurperObject.ResultSet.Row.CALCULATION_METHOD.text();
		def actSubDividedGeolocation = xmlSlurperObject.ResultSet.Row.GEO_LOCATION_VALUE.text();
		def actViaProxy = xmlSlurperObject.ResultSet.Row.VIA_PROXY.text();
		def actRSCurrentCreationDate = xmlSlurperObject.ResultSet.Row.CREATED_DATE.text();
		def actRSCurrentCreatedByNodeOid = xmlSlurperObject.ResultSet.Row.CREATED_BY.text();
		def actRSCurrentValue = "urn:dece:type:status:" + xmlSlurperObject.ResultSet.Row.STATUS.text();
		def actRSCurrentDescription = xmlSlurperObject.ResultSet.Row.STATUS.text();
		def actRSCurrentDeletionDate = xmlSlurperObject.ResultSet.Row.CLOSED_DATE.text();
		def actRSCurrentDeletedBy = xmlSlurperObject.ResultSet.Row.CLOSED_BY_ORG_ID.text();


		//Get NodeId for given NodeOid, from DB
		def sqlToGetNodeIDFrmNodeOid = "select Node_Id from node where node_oid=hextoraw('"+actRSCurrentCreatedByNodeOid+"')";
		log.info("Sql : '" + sqlToGetNodeIDFrmNodeOid + "'");
		def NodeIdInXml = SelectFromDB(sqlToGetNodeIDFrmNodeOid);
		log.info("NodeId from DB in XML format : '" + NodeIdInXml + "'");
		xmlSlurperObject = new XmlSlurper().parseText(NodeIdInXml);
		def actRSCurrentCreatedBy = xmlSlurperObject.ResultSet.Row.NODE_ID.text();
		def actRSHistoryModifiedBy = actRSCurrentCreatedBy;


		//Get History Tag Values from table 'Stream_Data_History'
		sql = "select status,created_date from (select * from stream_data_history where account_oid=hextoraw('"+accountOid+"') order by GGS_LAST_MODIFIED_DATE) where rownum=1";
		log.info("Get History Tag values from database");
		log.info("Sql : '" + sql + "'");
		def HistoryInXml = SelectFromDB(sql);
		log.info("HistoryInXml from DB in XML format : '" + HistoryInXml + "'");
		xmlSlurperObject = new XmlSlurper().parseText(HistoryInXml);		

		def actRSHistoryModificationDate = xmlSlurperObject.ResultSet.Row.CREATED_DATE.text();
		def actRSHistoryDescription = xmlSlurperObject.ResultSet.Row.STATUS.text();
		def actRSHistoryValue = "urn:dece:type:status:" + actRSHistoryDescription;



		this.log.info("Actual values extracted from DB for StreamView response validation!!!!");
		this.log.info("actStreamHandleOid :  '" + actStreamHandleOid + "'");
		this.log.info("actStreamClientNickname :  '" + actStreamClientNickname + "'");
		this.log.info("actRequestingUserID :  '" + actRequestingUserID + "'");
		this.log.info("actRightsTokenID :  '" + actRightsTokenID + "'");
		this.log.info("actExpirationDateTime :  '" + actExpirationDateTime + "'");
		this.log.info("actSGConfidence :  '" + actSGConfidence + "'");
		this.log.info("actSGCalculationMethod :  '" + actSGCalculationMethod + "'");
		this.log.info("actSubDividedGeolocation :  '" + actSubDividedGeolocation + "'");
		this.log.info("actViaProxy :  '" + actViaProxy + "'");
		this.log.info("actRSCurrentCreationDate :  '" + actRSCurrentCreationDate + "'");
		this.log.info("actRSCurrentCreatedBy :  '" + actRSCurrentCreatedBy + "'");
		this.log.info("actRSCurrentValue :  '" + actRSCurrentValue + "'");
		this.log.info("actRSCurrentDescription :  '" + actRSCurrentDescription + "'");
		this.log.info("actRSCurrentDeletionDate  :  '" + actRSCurrentDeletionDate  + "'");
		this.log.info("actRSCurrentDeletedBy  :  '" + actRSCurrentDeletedBy  + "'");
		this.log.info("actRSHistoryModifiedBy  :  '" + actRSHistoryModifiedBy  + "'");
		this.log.info("actRSHistoryModificationDate  :  '" + actRSHistoryModificationDate  + "'");
		this.log.info("actRSHistoryDescription  :  '" + actRSHistoryDescription  + "'");
		this.log.info("actRSHistoryValue  :  '" + actRSHistoryValue  + "'");

		try{
			assert actStreamHandleOid==expStreamHandleOid;
			assert actStreamClientNickname==expStreamClientNickname;
			assert actRequestingUserID==expRequestingUserID;
			assert actRightsTokenID==expRightsTokenID;
			assert actSGConfidence==expSGConfidence;
			assert actSGCalculationMethod==expSGCalculationMethod;
			assert actSubDividedGeolocation==expSubDividedGeolocation;
			assert actViaProxy==expViaProxy;
			assert actRSCurrentCreatedBy==expRSCurrentCreatedBy;
			assert actRSCurrentValue==expRSCurrentValue;
			assert actRSCurrentDescription==expRSCurrentDescription;
			
			//Assert below only when stream is delted i.e when deleteStreamForStatusChg = 'Yes'.
			//As when stream is deleted then only we will have  history tag, and deletion attribute in current tag.
			log.info("deleteStreamForStatusChg : " + deleteStreamForStatusChg);
			
			if(deleteStreamForStatusChg.toString().toLowerCase() == "yes"){
				this.log.info("Assert equal for attributes which appears upon stream deletion");
				assert actRSCurrentDeletedBy == expRSCurrentDeletedBy;
				assert actRSHistoryModifiedBy == expRSHistoryModifiedBy;
				assert actRSHistoryDescription == expRSHistoryDescription;
				assert actRSHistoryValue == expRSHistoryValue;
			}else{
				this.log.info("Assert not-equal for attributes, since stream is not deleted.");
				assert actRSCurrentDeletedBy != expRSCurrentDeletedBy;
				assert actRSHistoryModifiedBy != expRSHistoryModifiedBy;
				assert actRSHistoryDescription != expRSHistoryDescription;
				assert actRSHistoryValue != expRSHistoryValue;
			}
			
			/*
			 * Dates has different format. So we need to break and compare the values.
			*/
			compareDatesEqual(expRSCurrentCreationDate,actRSCurrentCreationDate);
			compareDatesEqual(expExpirationDateTime,actExpirationDateTime);
			
			if(deleteStreamForStatusChg.toString().toLowerCase() == "yes"){
				this.log.info("Assert equal for attributes which appears upon stream deletion");
				compareDatesEqual(expRSCurrentDeletionDate,actRSCurrentDeletionDate);			
				compareDatesEqual(expRSHistoryModificationDate,actRSHistoryModificationDate);
			}else{
				this.log.info("Assert not-equal for attributes,since stream is not deleted.");
				compareDatesUnEqual(expRSCurrentDeletionDate,actRSCurrentDeletionDate);			
				compareDatesUnEqual(expRSHistoryModificationDate,actRSHistoryModificationDate);
			}
			
			this.log.info("StreamView respone data DB validation passed!!!");
		}catch(Throwable e){
			String errorMessage = e.toString();
			errorMessage = errorMessage + "\n\n StreamView respone data DB validation failed!!!\n\n";
			throw new Exception(errorMessage);
		}

	}


	public compareDatesEqual(String dateWithTAndZ, String dateWithoutTAndZ){
	
		//Verify Expected and Actual Date
		log.info("Inside ComapareDatesEqual method");
		log.info("dateWithTAndZ : "+dateWithTAndZ);
		log.info("dateWithoutTAndZ : "+dateWithoutTAndZ);
		String dateWithTAndZYYDDMM = dateWithTAndZ.toString().split("T")[0];
		String dateWithTAndZOnlyWithZ = dateWithTAndZ.toString().split("T")[1];
		String dateWithTAndZHHMMSS = dateWithTAndZOnlyWithZ.substring(0,dateWithTAndZOnlyWithZ.length()-1);

		String dateWithoutTAndZYYYYDDMM = dateWithoutTAndZ.toString().split(" ")[0];
		String dateWithoutTAndZHHMMSS = dateWithoutTAndZ.toString().split(" ")[1];

		assert dateWithTAndZYYDDMM == dateWithoutTAndZYYYYDDMM;			
		String[]  dateWithTAndZHHMMSSArr = dateWithTAndZHHMMSS.split(":");
		String[]  dateWithoutTAndZHHMMSSArr = dateWithoutTAndZHHMMSS.split(":");
		assert dateWithoutTAndZHHMMSSArr[0] == dateWithTAndZHHMMSSArr[0];
		assert dateWithoutTAndZHHMMSSArr[1] == dateWithTAndZHHMMSSArr[1]; 
	}
	
	
	public compareDatesUnEqual(String dateWithTAndZ, String dateWithoutTAndZ){

		//Verify Expected and Actual Date
		log.info("Inside ComapareDates UnEqual method");
		log.info("dateWithTAndZ : "+dateWithTAndZ);
		log.info("dateWithoutTAndZ : "+dateWithoutTAndZ);
		assert dateWithTAndZ != dateWithoutTAndZ;
		
		/*String dateWithTAndZYYDDMM = dateWithTAndZ.toString().split("T")[0];
		String dateWithTAndZOnlyWithZ = dateWithTAndZ.toString().split("T")[1];
		String dateWithTAndZHHMMSS = dateWithTAndZOnlyWithZ.substring(0,dateWithTAndZOnlyWithZ.length()-1);

		String dateWithoutTAndZYYYYDDMM = dateWithoutTAndZ.toString().split(" ")[0];
		String dateWithoutTAndZHHMMSS = dateWithoutTAndZ.toString().split(" ")[1];

		assert dateWithTAndZYYDDMM != dateWithoutTAndZYYYYDDMM;			
		String[]  dateWithTAndZHHMMSSArr = dateWithTAndZHHMMSS.split(":");
		String[]  dateWithoutTAndZHHMMSSArr = dateWithoutTAndZHHMMSS.split(":");
		assert dateWithoutTAndZHHMMSSArr[0] != dateWithTAndZHHMMSSArr[0];
		assert dateWithoutTAndZHHMMSSArr[1] != dateWithTAndZHHMMSSArr[1]; */
	}
}