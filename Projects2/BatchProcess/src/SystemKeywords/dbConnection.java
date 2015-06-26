package SystemKeywords;

//import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;
import java.util.Properties;

import org.testng.Assert;


public class dbConnection {
	
	private String url;
	private String username;
	private String password;
	
	public void setHostname(String url){
		this.url = url;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public void setPassword(String password){
		this.password = password;
	}
	
	/*public Connection setConnection(){
		// declare a resultset that uses as a table for output data from the
		// table.
		//startTime = time;
		//logger.info("Starting Time :: " + startTime);
		try {
			
			//String driverName = "oracle.jdbc.driver.OracleDriver";
			//Class.forName(driverName);
			System.out.println("##### Connecting to LP3 Charelotte");
			/*
			 * Create a connection by using getConnection() method that takes
			 * parameters of string type connection url, user name and password
			 * to connect to database.
			 *//*

			conn = DriverManager.getConnection("jdbc:oracle:thin://@"
					+ hostname + ":2115/" + servername, username,
					password);

			System.out.println("Connected to the database");
			//logger.info("Connected to the database");

		} catch (SQLException e) {
			System.err.println("SQl Exception");
			e.printStackTrace();
			//logger.error("" + e);
		}

		catch (Exception e) {
			System.out.println("\nAnother Error");
			//logger.error("" + e);
		}
		return conn;
	}*/
	
	public Connection getConnection(){
		//System.out.println(url);
		//System.out.println(username);
		//System.out.println(password);
		
	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", username);
	    connectionProps.put("password", password);

	    String driverName = "oracle.jdbc.driver.OracleDriver";
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
	    try {
			conn = DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    //System.out.println("Connected to database");
	    return conn;
	}
	
	public String getStreamInactiveCount(Connection conn){
		Statement statement = null;
		String no_of_inactive_stream = null;
		String sql_query = "select count(USER_OID) from STREAM_DATA where expiration_date < = SYSDATE - 0  and expiration_date > SYSDATE - 7 and status ='active'";
		try{
	    	statement = conn.createStatement();
	    	ResultSet rs = statement.executeQuery(sql_query);
	        while (rs.next()) {
	            no_of_inactive_stream = rs.getNString("COUNT(USER_OID)");
	        }
	    } catch (SQLException e ) {
	    	System.out.println("Error executing SQL query");
	    	Assert.fail("Error executing SQL query");
	    } finally {
	    	//try { rs.close(); } catch (Exception e) { /* ignored */ }
	    	try { statement.close(); } catch (Exception e) { /* ignored */ }
	    	try { conn.close(); } catch (Exception e) { /* ignored */ }
	    	    
	        if (statement != null) { 
	        	try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
	        }
	    }
		return no_of_inactive_stream;
	}
	
	public String[] getStreamData(Connection conn, String stream_oid){
		Statement statement = null;
		String[] output= new String[3];
		String sql_query = "select STATUS, closed_date, closed_by_org_id from stream_data where stream_handle_oid = '" + stream_oid + "'";
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
		try{
	    	statement = conn.createStatement();
	    	ResultSet rs = statement.executeQuery(sql_query);
	        while (rs.next()) {
	            output[0] = rs.getNString("STATUS");
	            output[1] = rs.getNString("CLOSED_DATE");
	            output[2] = rs.getNString("CLOSED_BY_ORG_ID");
	        }
		} catch (SQLException e ) {
	    	System.out.println("Error executing SQL query");
	    	Assert.fail("Error executing SQL query");
	    } finally {
	    	//try { rs.close(); } catch (Exception e) { /* ignored */ }
	    	try { statement.close(); } catch (Exception e) { /* ignored */ }
	    	try { conn.close(); } catch (Exception e) { /* ignored */ }
	    	    
	        if (statement != null) { 
	        	try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
	        }
	    }
		return output;
	}
	
	public String getSystemTimeStamp(Connection conn){
		Statement statement = null;
		String timestamp = null;
		String sql_query = "select DISTINCT systimestamp from STREAM_DATA";
		System.out.println(sql_query);
		try{
	    	statement = conn.createStatement();
	    	ResultSet rs = statement.executeQuery(sql_query);
	        while (rs.next()) {
	            timestamp = rs.getNString("SYSTIMESTAMP");
	        }
		} catch (SQLException e ) {
	    	System.out.println("Error executing SQL query");
	    	Assert.fail("Error executing SQL query");
	    } finally {
	    	//try { rs.close(); } catch (Exception e) { /* ignored */ }
	    	try { statement.close(); } catch (Exception e) { /* ignored */ }
	    	try { conn.close(); } catch (Exception e) { /* ignored */ }
	    	    
	        if (statement != null) { 
	        	try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
	        }
	    }
		return timestamp;
	}
	
	public void setExpirationDate(Connection Conn, String sql_query){
		Statement statement = null;
		//String timestamp = null;
		//String sql_query = "update stream_data set expiration_Date = SYSDATE - 1 where STREAM_HANDLE_OID = '" + stream_oid + "'";
		//System.out.println('\n' + sql_query);
		//Connection Conn = getConnection();
		try{
	    	Statement stmt = Conn.createStatement();
	    	int rs = stmt.executeUpdate(sql_query);
	    	System.out.println(rs);
	    	//rs.next();
	    	//rs.updateRow();
	        //while (rs.next()) {
	            //timestamp = rs.getNString("SYSTIMESTAMP");
	        //}
	    } catch (SQLException e ) {
	    	System.out.println("Error executing SQL query");
	    	Assert.fail("Error executing SQL query");
	    } finally {
	    	//try { rs.close(); } catch (Exception e) { /* ignored */ //}
	    	//try { Statement.close(); } catch (Exception e) { /* ignored */ }
	    	//try { Conn.close(); } catch (Exception e) { /* ignored */ }
	    	    
	        if (statement != null) { 
	        	try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
	        }
	    }
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}	
	
	public void updateExpirationDate(Connection conn, String streams, String i) throws SQLException {
        
		String sql = 
				   "UPDATE STREAM_DATA " + 
				   "SET EXPIRATION_DATE = SYSDATE - " + i + 
				   " WHERE STREAM_HANDLE_OID = '" + streams + "'";
		//System.out.println(sql);
		PreparedStatement pstmt = conn.prepareStatement(sql);
		//pstmt.setString(1, "SYSDATE - 1");
		//pstmt.setString(2, streams);
		//System.out.println(sql);
		System.out.println("\tChanging expiration date of stream : " + streams + " by: " + i + " day");
		pstmt.executeUpdate();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		conn.commit();
   }    
	
	
	public String[] viewTable(Connection conn, String query)
		    throws SQLException {

		    Statement statement = null;
		    String[] nodeOID = null;
		    int i = 0;
		    //String query = "select node_oid from Node where node_id = 'urn:dece:org:org:dece:0701'";
		    //String query = "select NODE_OID from Node where NODE_STATUS = 'deleted'";
		    System.out.println(query);
		    
		    try{
		    	statement = conn.createStatement();
		        
		    	ResultSet rs = statement.executeQuery(query);
		        while (rs.next()) {
		            nodeOID[i] = rs.getString("NODE_OID");
		            i ++;
		            //nodeOID = append(arr, "4");
		            System.out.println(nodeOID);
		        }
		    } catch (SQLException e ) {
		    	System.out.println("HEEEEEEEEEEEE");
		        //JDBCTutorialUtilities.printSQLException(e);
		    } finally {
		        if (statement != null) { statement.close(); }
		    }
		    return nodeOID;
		}
	
}