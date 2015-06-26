//Copyright (c) 2014 Sanjeet Singh - Neustar, Inc. All Rights Reserved.
package dbUtils.dbResponseValidation.update;


public class StreamUpdateResponseValidation extends dbUtils.dbUtils{
	def log = null;
	def sql = null;
	
	/*
	 * This are input data required before  calling any methods in this groovy.
	*/
	def accountOid = null;
	def nodeUserId = null;
	
	def expStreamHandleOid = null;
	def expStreamClientNickname = null;
	def expRequestingUserID = null;
	def expRightsTokenID = null;
	def expExpirationDateTime = null;
	def expSGConfidence = null;
	def expSGCalculationMethod = null;
	def expSubDividedGeolocation = null;
	def expViaProxy = null;
	def expRSCurrentModificationDate = null;
	def expRSCurrentModifiedBy = null;
	def expRSCurrentCreationDate = null;
	def expRSCurrentCreatedBy = null;	
	def expRSCurrentValue = null;
	def expRSCurrentDescription = null;
	def expRSHistoryModificationDate = null;
	def expRSHistoryModifiedBy = null;
	def expRSHistoryValue = null;
	def expRSHistoryDescription = null;

	public StreamUpdateResponseValidation(log, envIdentifier){
		super(log, envIdentifier);
		this.log = log;
		this.log.info("StreamUpdateResponseValidation object created successfully");
	}

	public verifyStreamUpdateResponseData(def testSuiteName, def testCaseName, def testStepName, def testRunner, def context) throws Exception{
		/*
		 * Prepare expected values from response
		 */
		def restTestStep = testRunner.testCase.testSuite.project.getTestSuiteByName(testSuiteName).getTestCaseByName(testCaseName).getTestStepByName(testStepName);
		def streamUpdateResponseInXml = restTestStep.getTestRequest().getResponseContentAsXml();
		def xmlSlurperObject = new XmlSlurper().parseText(streamUpdateResponseInXml);

		expStreamHandleOid = xmlSlurperObject.@StreamHandleID.text();
		expStreamClientNickname = xmlSlurperObject.StreamClientNickname.text();
		expRequestingUserID = xmlSlurperObject.RequestingUserID.text();
		expRightsTokenID = xmlSlurperObject.RightsTokenID.text();
		expExpirationDateTime = xmlSlurperObject.ExpirationDateTime.text();
		expSGConfidence = xmlSlurperObject.SubDividedGeolocation.@Confidence.text();
		expSGCalculationMethod = xmlSlurperObject.SubDividedGeolocation.@CalculationMethod.text();
		expSubDividedGeolocation = xmlSlurperObject.SubDividedGeolocation.text();
		expViaProxy = xmlSlurperObject.SubDividedGeolocation.@ViaProxy.text();
		expRSCurrentModificationDate = xmlSlurperObject.ResourceStatus.Current.@ModificationDate.text();
		expRSCurrentModifiedBy = xmlSlurperObject.ResourceStatus.Current.@ModifiedBy.text();
		expRSCurrentCreationDate = xmlSlurperObject.ResourceStatus.Current.@CreationDate.text();
		expRSCurrentCreatedBy = xmlSlurperObject.ResourceStatus.Current.@CreatedBy.text();
		expRSCurrentValue = xmlSlurperObject.ResourceStatus.Current.Value.text();
		expRSCurrentDescription = xmlSlurperObject.ResourceStatus.Current.Description.text();		
		expRSHistoryModificationDate = xmlSlurperObject.ResourceStatus.History.Prior.@ModificationDate.text();
		expRSHistoryModifiedBy = xmlSlurperObject.ResourceStatus.History.Prior.@ModifiedBy.text();
		expRSHistoryValue = xmlSlurperObject.ResourceStatus.History.Prior.Value.text();
		expRSHistoryDescription = xmlSlurperObject.ResourceStatus.History.Prior.Description.text();

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
		this.log.info("expRSCurrentModificationDate :  '" + expRSCurrentModificationDate + "'");
		this.log.info("expRSCurrentModifiedBy :  '" + expRSCurrentModifiedBy + "'");
		this.log.info("expRSCurrentCreationDate :  '" + expRSCurrentCreationDate + "'");
		this.log.info("expRSCurrentCreatedBy :  '" + expRSCurrentCreatedBy + "'");
		this.log.info("expRSCurrentValue :  '" + expRSCurrentValue + "'");
		this.log.info("expRSHistoryModificationDate :  '" + expRSHistoryModificationDate + "'");
		this.log.info("expRSCurrentDescription :  '" + expRSCurrentDescription + "'");
		this.log.info("expRSHistoryModifiedBy :  '" + expRSHistoryModifiedBy + "'");
		this.log.info("expRSHistoryValue :  '" + expRSHistoryValue + "'");
		this.log.info("expRSHistoryDescription :  '" + expRSHistoryDescription + "'");

		String sql = "select to_char(rawtohex(STREAM_HANDLE_OID)) STREAM_HANDLE_OID,STREAM_CLIENT_NICKNAME,to_char(rawtohex(USER_OID)) USER_OID,to_char(rawtohex(RIGHTS_TOKEN_OID)) RIGHTS_TOKEN_OID, EXPIRATION_DATE,CONFIDENCE,CALCULATION_METHOD,GEO_LOCATION_VALUE,VIA_PROXY,CREATED_DATE,UPDATED_DATE,to_char(rawtohex(CREATED_BY)) CREATED_BY,STATUS from Stream_data where account_oid=hextoraw('" + accountOid +"')"
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
		def actRSCurrentModificationDate = xmlSlurperObject.ResultSet.Row.UPDATED_DATE.text();
		def actRSCurrentCreationDate = xmlSlurperObject.ResultSet.Row.CREATED_DATE.text();
		def actRSCurrentCreatedByNodeOid = xmlSlurperObject.ResultSet.Row.CREATED_BY.text();
		def actRSCurrentValue = "urn:dece:type:status:" + xmlSlurperObject.ResultSet.Row.STATUS.text();
		def actRSCurrentDescription = xmlSlurperObject.ResultSet.Row.STATUS.text();
		
		//Get NodeId for given NodeOid, from DB
		def sqlToGetNodeIDFrmNodeOid = "select Node_Id from node where node_oid=hextoraw('"+actRSCurrentCreatedByNodeOid+"')";
		log.info("Sql : '" + sqlToGetNodeIDFrmNodeOid + "'");
		def NodeIdInXml = SelectFromDB(sqlToGetNodeIDFrmNodeOid);
		log.info("NodeId from DB in XML format : '" + NodeIdInXml + "'");
		xmlSlurperObject = new XmlSlurper().parseText(NodeIdInXml);
		def actRSCurrentCreatedBy = xmlSlurperObject.ResultSet.Row.NODE_ID.text();
		def actRSHistoryModifiedBy = actRSCurrentCreatedBy;
		def actRSCurrentModifiedBy = actRSCurrentCreatedBy
		//Get actual values for History Tag
		sql = "select created_date,status from stream_data_history where account_oid=hextoraw('" + accountOid +"') and rownum =1 order by  GGS_LAST_MODIFIED_DATE desc";
		
		log.info("Sql to get history tag elements value : '" + sql + "'");
		def historyTagInXml = SelectFromDB(sql);
		log.info("HistoryTag from DB in XML format : '" + historyTagInXml + "'");
		xmlSlurperObject = new XmlSlurper().parseText(historyTagInXml);
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
		this.log.info("actRSCurrentModificationDate :  '" + actRSCurrentModificationDate + "'");
		this.log.info("actRSCurrentModifiedBy :  '" + actRSCurrentModifiedBy + "'");
		this.log.info("actRSCurrentCreationDate :  '" + actRSCurrentCreationDate + "'");
		this.log.info("actRSCurrentCreatedBy :  '" + actRSCurrentCreatedBy + "'");
		this.log.info("actRSCurrentValue :  '" + actRSCurrentValue + "'");
		this.log.info("actRSCurrentDescription :  '" + actRSCurrentDescription + "'");
		this.log.info("actRSHistoryModificationDate :  '" + actRSHistoryModificationDate + "'");
		this.log.info("actRSHistoryModifiedBy :  '" + actRSHistoryModifiedBy + "'");
		this.log.info("actRSHistoryDescription :  '" + actRSHistoryDescription + "'");
		this.log.info("actRSHistoryValue :  '" + actRSHistoryValue + "'");
		
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
		assert actRSHistoryModifiedBy == expRSHistoryModifiedBy;
		assert actRSHistoryDescription == expRSHistoryDescription;
		assert actRSHistoryValue ==  expRSHistoryValue;
		assert actRSCurrentModifiedBy == expRSCurrentModifiedBy;
			/*
			 * Expiration and create data has different format. So we need to break and compare the values.
			*/
			
			//Verify Expected and Actual Expiration Date
			String expExpirationDateTimeYYYYDDMM = expExpirationDateTime.toString().split("T")[0];
			String expExpirationDateTimeHHMMSSWithZ = expExpirationDateTime.toString().split("T")[1];
			String expExpirationDateTimeHHMMSS = expExpirationDateTimeHHMMSSWithZ.substring(0,expExpirationDateTimeHHMMSSWithZ.length()-1);

			String actExpirationDateTimeYYYYDDMM = actExpirationDateTime.toString().split(" ")[0];
			String actExpirationDateTimeHHMMSS = actExpirationDateTime.toString().split(" ")[1];

			assert expExpirationDateTimeYYYYDDMM == actExpirationDateTimeYYYYDDMM;
			
			String[]  expExpirationDateTimeHHMMSSArr = expExpirationDateTimeHHMMSS.split(":");
			String[]  actExpirationDateTimeHHMMSSArr = actExpirationDateTimeHHMMSS.split(":");
			assert expExpirationDateTimeHHMMSSArr[0] == actExpirationDateTimeHHMMSSArr[0];
			assert expExpirationDateTimeHHMMSSArr[1] == actExpirationDateTimeHHMMSSArr[1];
			//assert expExpirationDateTimeHHMMSSArr[2] == actExpirationDateTimeHHMMSSArr[2];
			
			
			//Verify Expected and Actual Created Date
			String expRSCurrentCreationDateYYYYDDMM = expRSCurrentCreationDate.toString().split("T")[0];
			String expRSCurrentCreationDateHHMMSSWithZ = expRSCurrentCreationDate.toString().split("T")[1];
			String expRSCurrentCreationDateHHMMSS = expRSCurrentCreationDateHHMMSSWithZ.substring(0,expExpirationDateTimeHHMMSSWithZ.length()-1);

			String actRSCurrentCreationDateYYYYDDMM = actRSCurrentCreationDate.toString().split(" ")[0];
			String actRSCurrentCreationDateHHMMSS = actRSCurrentCreationDate.toString().split(" ")[1];

			assert expRSCurrentCreationDateYYYYDDMM == actRSCurrentCreationDateYYYYDDMM;
			
			String[]  expRSCurrentCreationDateHHMMSSArr = expRSCurrentCreationDateHHMMSS.split(":");
			String[]  actRSCurrentCreationDateHHMMSSArr = actRSCurrentCreationDateHHMMSS.split(":");
			assert expRSCurrentCreationDateHHMMSSArr[0] == actRSCurrentCreationDateHHMMSSArr[0];
			assert expRSCurrentCreationDateHHMMSSArr[1] == actRSCurrentCreationDateHHMMSSArr[1];
			//assert expRSCurrentCreationDateHHMMSSArr[2] == actRSCurrentCreationDateHHMMSSArr[2];
			
			
			
			//Verify Expected and Actual Current-ModificationDate Date
			String expRSCurrentModificationDateYYYYDDMM = expRSCurrentModificationDate.toString().split("T")[0];
			String expRSCurrentModificationDateHHMMSSWithZ = expRSCurrentModificationDate.toString().split("T")[1];
			String expRSCurrentModificationDateHHMMSS = expRSCurrentModificationDateHHMMSSWithZ.substring(0,expRSCurrentModificationDateHHMMSSWithZ.length()-1);

			String actRSCurrentModificationDateYYYYDDMM = actRSCurrentModificationDate.toString().split(" ")[0];
			String actRSCurrentModificationDateHHMMSS = actRSCurrentModificationDate.toString().split(" ")[1];

			assert expRSCurrentModificationDateYYYYDDMM == actRSCurrentModificationDateYYYYDDMM;
			
			String[]  expRSCurrentModificationDateHHMMSSArr = expRSCurrentModificationDateHHMMSS.split(":");
			String[]  actRSCurrentModificationDateHHMMSSArr = actRSCurrentModificationDateHHMMSS.split(":");
			assert expRSCurrentModificationDateHHMMSSArr[0] == actRSCurrentModificationDateHHMMSSArr[0];
			assert expRSCurrentModificationDateHHMMSSArr[1] == actRSCurrentModificationDateHHMMSSArr[1];
			//assert expRSCurrentModificationDateHHMMSSArr[2] == actRSCurrentModificationDateHHMMSSArr[2];
			
			
			
			//Verify Expected and Actual History-ModificationDate Date
			String expRSHistoryModificationDateYYYYDDMM = expRSHistoryModificationDate.toString().split("T")[0];
			String expRSHistoryModificationDateHHMMSSWithZ = expRSHistoryModificationDate.toString().split("T")[1];
			String expRSHistoryModificationDateHHMMSS = expRSHistoryModificationDateHHMMSSWithZ.substring(0,expRSHistoryModificationDateHHMMSSWithZ.length()-1);

			String actRSHistoryModificationDateYYYYDDMM = actRSHistoryModificationDate.toString().split(" ")[0];
			String actRSHistoryModificationDateHHMMSS = actRSHistoryModificationDate.toString().split(" ")[1];

			assert expRSHistoryModificationDateYYYYDDMM == actRSHistoryModificationDateYYYYDDMM;
			
			String[]  expRSHistoryModificationDateHHMMSSArr = expRSHistoryModificationDateHHMMSS.split(":");
			String[]  actRSHistoryModificationDateHHMMSSArr = actRSHistoryModificationDateHHMMSS.split(":");
			assert expRSHistoryModificationDateHHMMSSArr[0] == actRSHistoryModificationDateHHMMSSArr[0];
			assert expRSHistoryModificationDateHHMMSSArr[1] == actRSHistoryModificationDateHHMMSSArr[1];
			//assert expRSHistoryModificationDateHHMMSSArr[2] == actRSHistoryModificationDateHHMMSSArr[2];
			
			
			
			this.log.info("StreamView respone data DB validation passed!!!");
		}catch(Throwable e){
			String errorMessage = e.toString();
			errorMessage = errorMessage + "\n\n StreamView respone data DB validation failed!!!\n\n";
			throw new Exception(errorMessage);
		}

	}
}