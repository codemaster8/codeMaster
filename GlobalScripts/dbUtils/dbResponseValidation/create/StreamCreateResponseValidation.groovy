//Copyright (c) 2014 Sanjeet Singh - Neustar, Inc. All Rights Reserved.

package dbUtils.dbResponseValidation.create;


public class StreamCreateResponseValidation extends dbUtils.dbUtils{
	def log = null;
	def sql = null;
	def expStreamHandleOid = null;
	def expRightsTokenOid = null;
	def expAccountOid = null;
	def expUSerOid = null;
	def expStatus = null;
	def expCreatedBy = null;
	def expStreamClientNickName = null;
	def expExpirationDate = null;
	def expGeoLocationValue = null;


	public StreamCreateResponseValidation(log, envIdentifier){
		super(log, envIdentifier);
		this.log = log;
		this.log.info("StreamCreateResponseValidation object created successfully");
	}

	public verifyStreamHandleOidInDBTables() throws Exception{
		this.log.info("Input data provided!!!");
		this.log.info("expStreamHandleOid : " +expStreamHandleOid );
		this.log.info("expRightsTokenOid : " +expRightsTokenOid );
		this.log.info("expAccountOid : " +expAccountOid );
		this.log.info("expUSerOid : " +expUSerOid );
		this.log.info("expStatus : " +expStatus );
		this.log.info("expCreatedBy : " +expCreatedBy );
		this.log.info("expStreamClientNickName : " +expStreamClientNickName );
		this.log.info("expExpirationDate : " +expExpirationDate );
		this.log.info("expGeoLocationValue : " +expGeoLocationValue );

		verifyStreamDataInDBTableStreamData();
		verifyStreamDataInDBTableStreamDataHistory();
	}

	public verifyStreamDataInDBTableStreamData() throws Exception{
		log.info("Verify StreamData in DB table 'Stream_Data'");
		String sql = "select to_char(rawtohex(Rights_Token_Oid)) Rights_Token_Oid,to_char(rawtohex(Account_Oid)) Account_Oid,to_char(rawtohex(User_Oid)) User_Oid,Status,to_char(rawtohex(Created_By)) Created_By,Stream_Client_NickName,Expiration_Date,Geo_Location_Value from stream_data where stream_handle_oid=hextoraw('" + expStreamHandleOid + "')";
		log.info("Sql : '" + sql + "'");
		def streamCreateDataInXmlFormat = SelectFromDB(sql);
		log.info "Xml Data returned by DB : '" + streamCreateDataInXmlFormat + "'";

		def xmlSlurperObject = new XmlSlurper().parseText(streamCreateDataInXmlFormat);

		def actRightsTokenOid = xmlSlurperObject.ResultSet.Row.RIGHTS_TOKEN_OID.text();
		def actAccountOid = xmlSlurperObject.ResultSet.Row.ACCOUNT_OID.text();
		def actUSerOid = xmlSlurperObject.ResultSet.Row.USER_OID.text();
		def actStatus = xmlSlurperObject.ResultSet.Row.STATUS.text();
		def actCreatedBy = xmlSlurperObject.ResultSet.Row.CREATED_BY.text();
		def actStreamClientNickName = xmlSlurperObject.ResultSet.Row.STREAM_CLIENT_NICKNAME.text();
		def actExpirationDate = xmlSlurperObject.ResultSet.Row.EXPIRATION_DATE.text();
		def actGeoLocationValue = xmlSlurperObject.ResultSet.Row.GEO_LOCATION_VALUE.text();

		this.log.info("Actual Data returned by DB!!!");
		this.log.info("actRightsTokenOid : " +actRightsTokenOid );
		this.log.info("actAccountOid : " +actAccountOid );
		this.log.info("actUSerOid : " +actUSerOid );
		this.log.info("actStatus : " +actStatus );
		this.log.info("actCreatedBy : " +actCreatedBy );
		this.log.info("actStreamClientNickName : " +actStreamClientNickName );
		this.log.info("actExpirationDate : " +actExpirationDate );
		this.log.info("actGeoLocationValue : " +actGeoLocationValue );


		log.info("Assert on Expected and Actual data!!!");
		try{
			assert expRightsTokenOid == actRightsTokenOid;
			assert expAccountOid == actAccountOid;
			assert expUSerOid == actUSerOid;
			assert expStatus == actStatus;
			assert expCreatedBy == actCreatedBy;
			assert expStreamClientNickName == actStreamClientNickName;
			assert expGeoLocationValue == actGeoLocationValue;

			/*
			 * Expected and Actual Expiry date has different format. So we need to break and compare the values.
			 */
			String expYYYYDDMM = expExpirationDate.toString().split("T")[0];
			String expTimeWithZ = expExpirationDate.toString().split("T")[1];
			String expTime = expTimeWithZ.substring(0,expTimeWithZ.length()-1);

			String actYYYYDDMM = actExpirationDate.toString().split(" ")[0];
			String actTime = actExpirationDate.toString().split(" ")[1];

			assert expYYYYDDMM == actYYYYDDMM;
			assert expTime == actTime;
			log.info("All assertion in table Stream_data passed!!!!");				
		}catch(Throwable e){
			String errorMessage = e.toString();
			errorMessage = errorMessage + "\n\n Stream data assertion in table Stream_Data failed!!!!\n\n";
			throw new Exception(errorMessage);
		}	
	}




	public verifyStreamDataInDBTableStreamDataHistory() throws Exception{
		this.log.info("Verify StreamData in DB table 'Stream_Data_history'");
		String sql = "select to_char(rawtohex(Stream_History_Oid)) Stream_History_Oid,to_char(rawtohex(Rights_Token_Oid)) Rights_Token_Oid,to_char(rawtohex(Account_Oid)) Account_Oid,to_char(rawtohex(Created_By_User_Oid)) Created_By_User_Oid,Status,to_char(rawtohex(Created_By_Node_Oid)) Created_By_Node_Oid,Stream_Client_NickName,Expiration_Date,Geo_Location_Value from stream_data_history where stream_handle_oid=hextoraw('" + expStreamHandleOid + "')";
		this.log.info("Sql : '" + sql + "'");
		def streamCreateDataInXmlFormat = SelectFromDB(sql);
		this.log.info "Xml Data returned by DB : '" + streamCreateDataInXmlFormat + "'";

		def xmlSlurperObject = new XmlSlurper().parseText(streamCreateDataInXmlFormat);

		def actStreamHistoryOId = xmlSlurperObject.ResultSet.Row.STREAM_HISTORY_OID.text();
		def actRightsTokenOid = xmlSlurperObject.ResultSet.Row.RIGHTS_TOKEN_OID.text();
		def actAccountOid = xmlSlurperObject.ResultSet.Row.ACCOUNT_OID.text();
		def actUSerOid = xmlSlurperObject.ResultSet.Row.CREATED_BY_USER_OID.text();
		def actStatus = xmlSlurperObject.ResultSet.Row.STATUS.text();
		def actCreatedBy = xmlSlurperObject.ResultSet.Row.CREATED_BY_NODE_OID.text();
		def actStreamClientNickName = xmlSlurperObject.ResultSet.Row.STREAM_CLIENT_NICKNAME.text();
		def actExpirationDate = xmlSlurperObject.ResultSet.Row.EXPIRATION_DATE.text();
		def actGeoLocationValue = xmlSlurperObject.ResultSet.Row.GEO_LOCATION_VALUE.text();

		this.log.info("Actual Data returned by DB!!!");
		this.log.info("actStreamHistoryOId : " + actStreamHistoryOId);
		this.log.info("actRightsTokenOid : " +actRightsTokenOid );
		this.log.info("actAccountOid : " +actAccountOid );
		this.log.info("actUSerOid : " +actUSerOid );
		this.log.info("actStatus : " +actStatus );
		this.log.info("actCreatedBy : " +actCreatedBy );
		this.log.info("actStreamClientNickName : " +actStreamClientNickName );
		this.log.info("actExpirationDate : " +actExpirationDate );
		this.log.info("actGeoLocationValue : " +actGeoLocationValue );


		log.info("Assert on Expected and Actual data!!!");
		try{	
			assert actStreamHistoryOId != null;
			assert expRightsTokenOid == actRightsTokenOid;
			assert expAccountOid == actAccountOid;
			assert expUSerOid == actUSerOid;
			assert expStatus == actStatus;
			assert expCreatedBy == actCreatedBy;
			assert expStreamClientNickName == actStreamClientNickName;
			assert expGeoLocationValue == actGeoLocationValue;

			/*
			 * Expected and Actual Expiry date has different format. So we need to break and compare the values.
			 */
			String expYYYYDDMM = expExpirationDate.toString().split("T")[0];
			String expTimeWithZ = expExpirationDate.toString().split("T")[1];
			String expTime = expTimeWithZ.substring(0,expTimeWithZ.length()-1);

			String actYYYYDDMM = actExpirationDate.toString().split(" ")[0];
			String actTime = actExpirationDate.toString().split(" ")[1];

			assert expYYYYDDMM == actYYYYDDMM;
			assert expTime == actTime;
			log.info("All assertion in table Stream_data_history passed!!!!");				
		}catch(Throwable e){
			String errorMessage = e.toString();
			errorMessage = errorMessage + "\n\n Stream data assertion in table Stream_Data_history failed!!!!\n\n";
			throw new Exception(errorMessage);
		}	
	}
}