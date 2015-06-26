//Copyright (c) 2014 Sanjeet Singh - Neustar, Inc. All Rights Reserved.

package dbUtils.dbResponseValidation.delete;


public class StreamDeleteResponseValidation extends dbUtils.dbUtils{
	def log = null;
	def sql = null;
	def streamHandleOid = null;
	def expStreamStatus = "deleted";


	public StreamDeleteResponseValidation(log, envIdentifier){
		super(log, envIdentifier);
		this.log = log;
		this.log.info("StreamDeleteResponseValidation object created successfully");
	}

	public verifyStreamHandleOidDeletedInDBTables() throws Exception{
		this.log.info("Input data provided!!!");
		this.log.info("streamHandleOid : " +streamHandleOid );
		this.log.info("expStreamStatus : " +expStreamStatus );

		//Call Method 'verifyStreamStatusInDBTableStreamData'
		verifyStreamStatusInDBTableStreamData();

		//Call Method 'verifyStreamStatusInDBTableStreamDataHistory'
		verifyStreamStatusInDBTableStreamDataHistory();
	}

	public verifyStreamStatusInDBTableStreamData() throws Exception{
		log.info("Verify Stream Status in DB table 'Stream_Data'");
		String sql = "select status from stream_data where stream_handle_oid=hextoraw('" + streamHandleOid + "')";
		log.info("Sql : '" + sql + "'");
		def streamStatusInXmlFormat = SelectFromDB(sql);
		log.info "Xml Data returned by DB : '" + streamStatusInXmlFormat + "'";

		def xmlSlurperObject = new XmlSlurper().parseText(streamStatusInXmlFormat);
		def actStreamStatus = xmlSlurperObject.ResultSet.Row.STATUS.text();
		try{
			assert 	actStreamStatus == 	expStreamStatus;
		}catch(Throwable e){
			String errorMessage = e.toString();
			errorMessage = errorMessage + "\n\n Stream Status assertion in table Stream_Data failed!!!!\n\n";
			throw new Exception(errorMessage);
		}	
	}




	public verifyStreamStatusInDBTableStreamDataHistory() throws Exception{
		log.info("Verify Stream Status in DB table 'Stream_Data_history'");
		String sql = "select status from (select status from stream_data_history where stream_handle_oid=hextoraw('"+streamHandleOid+"') order by GGS_LAST_MODIFIED_DATE desc) where rownum=1"
		log.info("Sql : '" + sql + "'");
		def streamStatusInXmlFormat = SelectFromDB(sql);
		log.info "Xml Data returned by DB : '" + streamStatusInXmlFormat + "'";

		def xmlSlurperObject = new XmlSlurper().parseText(streamStatusInXmlFormat);
		def actStreamStatus = xmlSlurperObject.ResultSet.Row.STATUS.text();
		try{
			assert 	actStreamStatus == 	expStreamStatus;
		}catch(Throwable e){
			String errorMessage = e.toString();
			errorMessage = errorMessage + "\n\n Stream Status assertion in table Stream_Data_History failed!!!!\n\n";
			throw new Exception(errorMessage);
		}
	}
}