// Copyright (c) 2010 Bhavin Bharat Joshi - Neustar, Inc. All Rights Reserved.

package dbUtils;

import groovy.sql.Sql;
import groovy.xml.MarkupBuilder;

import com.eviware.soapui.SoapUI.*;

/**
 * Provides Utilities to Interact with a Oracle Database.
 * 
 * @author Bhavin Bharat Joshi
 * @author Pat Gentry
 * @version 1.0
 */
public class dbUtils
{
	private log;
	private dbIdentifier = null;	
	private dbUsername = "";
	private dbPassword = "";	
	public dbConn = null;	

	// Constructors
	/**
	 * Creates a new <code>dbUtils</code> object with the ability 
	 * to write to SoapUI Log.
	 *
	 * @param log The SoapUI Log to write messages to.
	 */
	dbUtils(log)
	{
		this.log = log;		
		this.dbIdentifier = "LP2";
		dbConnEstablish(this.dbIdentifier);		
	}
	/**
	 * Creates a new <code>dbUtils</code> object. 
	 */
	dbUtils() 
	{		
		this.dbIdentifier = "LP2";
		dbConnEstablish(this.dbIdentifier);
	}
	/**
	 * Creates a new <code>dbUtils</code> object with the ability
	 * to connect to a specified Oracle Database as well as
	 * write to SoapUI Log.
	 *
	 * @param log The SoapUI Log to write messages to.
	 * @param envIdentifier The Database Identifier.
	 */
	dbUtils(log, envIdentifier) 
	{
		this.log = log;
		this.dbIdentifier = envIdentifier;          		
		dbConnEstablish(this.dbIdentifier);
	}

	// Methods
	/**
	 * Provides Database Connection Credentials based on the
	 * Identifier provided.
	 * 
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 *  
	 * @return A String Array containing DBUsername and DBPassword.
	 */
	def String[] ReadDBCrecentials(String envIdentifier)
	{
		private boolean dbIdentifierNotFound = false;
		switch (envIdentifier) 
		{
			case "LP1":
				dbUsername = "deqa1";
				dbPassword = "dec2010";
				break;
			case "LP2":
				dbUsername = "deqa2";
				dbPassword = "dece2010";
				break;
			case "LP3":
				dbUsername = "deadm";
				dbPassword = "dec2010";
				break;
			case "IOT":
				dbUsername = "deadm";
				dbPassword = "dec2010";
				break;
			case "LP1DM":
				dbUsername = "dedmqa1";
				dbPassword = "dedmqa1";
				break;
			case "LP2DM":
				dbUsername = "dedmqa2";
				dbPassword = "dedmqa2";
				break;
			case "LP3DM":
				dbUsername = "dedm";
				dbPassword = "dedm";
				break;
			case "IOTDM":			    
				//dbUsername = "DECE_READONLY";
				//dbPassword = "changeme";
				dbUsername = "dedm";
				dbPassword = "dedm";
				break;
			default:
				dbIdentifierNotFound = true
				try 
				{
					assert dbIdentifierNotFound == false;
				}
				catch(Throwable e) 
				{
					dbUsername = "";
					dbPassword = "";
					if(this.log) 
					{
						this.log.info "Unknown DB Identifier '" + envIdentifier + "' provided. Valid DB Identifiers are 'LP1', 'LP2' and 'LP3'.";
						this.log.info "Returning empty strings for both DB Username and DB Password.";
					}
				}
		}
		return [dbUsername, dbPassword];
	}
	/**
	 * Creates a Database Connection Object to Interact with the Database.
	 * 
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'. 
	 * 
	 * @return A Database Connection Object to the Database identified by <strong>envIdentifier</strong>.
	 */
	def dbConnEstablish(String envIdentifier) 
	{			
		private dbCredentials = ReadDBCrecentials(envIdentifier);
		dbUsername = dbCredentials[0];
		dbPassword = dbCredentials[1];		
		private db;
        if(envIdentifier[0..2] == "IOT")
		{
		    try 
    		{
    			db = [url: 'jdbc:oracle:thin:@10.91.52.11:2115/stdeciot.neustar.com', user: dbUsername, password: dbPassword, driver: 'oracle.jdbc.driver.OracleDriver'];
    			this.dbConn = Sql.newInstance(db.url, db.user, db.password, db.driver);
    		}
    		catch(Throwable e) 
    		{
    			if(this.log) 
    			{
    				this.log.info e;   
    			}
    		}    
		}		
		else
		{
    		try 
    		{
    			db = [url: 'jdbc:oracle:thin:@10.31.153.5:2115/stdecqpdb_stdecqa.neustar.com', user: dbUsername, password: dbPassword, driver: 'oracle.jdbc.driver.OracleDriver'];
    			this.dbConn = Sql.newInstance(db.url, db.user, db.password, db.driver);
    		}
    		catch(Throwable e) 
    		{
    			if(this.log) 
    			{
    				this.log.info e;   
    			}
    		}
		} 		
	}
	/**
	 * Closes Database Connection.  
	 */
	def dbConnClose() 
	{
		try 
		{
			this.dbConn.close();
			dbConn = null;
		}
		catch(Throwable e) 
		{
			if(this.log) 
			{
				this.log.info e;
			}
		}
	}
	/**
	 * Provides a well formatted XML String containing the results of
	 * the SQL query provided by the user.
	 * 
	 * @param String sqlStr Any valid SQL 'Select' Statement.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 * 
	 * @return A well formatted XML Result String that can be parsed using
	 * any SAX based XML Parser.
	 */
	def String SelectFromDB(String sqlStr, String envIdentifier = null) 
	{
		if(envIdentifier != null) 
		{						
			dbConnEstablish(envIdentifier);
		}
		else
		{
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);
			}
		}
		private columnList = new ArrayList();
		private fetchSizeValue = 10;
		private rsMetadata;
		private stringWriter;
		try 
		{			
			this.log.info "SelectFromDB - here";

			dbConn.query(sqlStr) 
			{		  
				resultSet ->
				fetchSizeValue = resultSet.getFetchSize();
				rsMetadata = resultSet.metaData;
				if (rsMetadata.columnCount <= 0) 
				{
					this.log.info "SelectFromDB - returning with no rows";
					return;
				}
				for (i in 0..< rsMetadata.columnCount) 
				{
					columnList += "${rsMetadata.getColumnLabel(i+1)}"
				}
			}
		}
		catch(Throwable e) 
		{
			if(this.log) 
			{
				this.log.info e;
				this.log.info "Returning empty string for XML Result."
			}
			stringWriter = "";
		}
		//private fileWriter = new FileWriter("c:\\test.xml");
		//private xmlString = new MarkupBuilder(fileWriter);
		stringWriter = new StringWriter();
		private xmlString = new MarkupBuilder(stringWriter);
		private rowNum = 1;
		private columnName = "";
		private boolean dataFound = false;
		xmlString.Results
		{
			xmlString.ResultSet(fetchSize:fetchSizeValue) 
			{
				try 				
				{					
					dbConn.eachRow(sqlStr) 
					{ 						
						rowData ->
						xmlString.Row(rowNumber:rowNum) 
						{
							for(int i = 0; i < columnList.size(); i++) 
							{
								columnName = columnList[i];
								"$columnName"(rowData[i].toString());
							}
						}
						rowNum = rowNum + 1;
						//dataFound = true;
					}
				}
				catch(Throwable e) 
				{
					if(this.log) 
					{
						this.log.info e;
						this.log.info "Returning empty string for XML Result."
					}
					stringWriter = "";
				}			    
			}
		}
		if(envIdentifier != null) 
		{
			dbConnClose(); 
		}
		/*if(!dataFound)
		{
			stringWriter = "";
		}*/
		return stringWriter.toString();						
	}
	/**
	* Provides a String Array or a 2D String Array containing the results of
	* the SQL query provided by the user.
	*
	* @param String sqlStr Any valid SQL 'Select' Statement.
	* @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	*
	* @return a String Array or a 2D String Array containing the results of
	* the SQL query provided by the user.
	*/
   def SelectFromDBArray(String sqlStr, String envIdentifier = null)
   {
	   if(envIdentifier != null)
	   {
		   dbConnEstablish(envIdentifier);
	   }
	   else
	   {
		   if(dbConn == null)
		   {
			   dbConnEstablish(this.dbIdentifier);
		   }
	   }
	   private sqlResult;	   
	   private columnList = new ArrayList();	   
	   private rsMetadata;	   
	   private String[][] strArray2D = "";
	   private String[] strArray1D = "";
	   try
	   {
		   sqlResult = dbConn.rows(sqlStr);
		   if(sqlResult.size() > 0)
		   {
				dbConn.query(sqlStr)
				{
					resultSet ->
					rsMetadata = resultSet.metaData;
					if (rsMetadata.columnCount <= 0)
					{
						return;
					}
					for (i in 0..< rsMetadata.columnCount)
					{
						columnList += "${rsMetadata.getColumnLabel(i+1)}"
					}
				}
				if(columnList.size() > 1)
				{				
					strArray2D = new String[sqlResult.size()][columnList.size()];
					for(int i = 0; i < sqlResult.size(); i++)
					{
						for(int j = 0; j < columnList.size(); j++)
						{
							strArray2D[i][j] =  sqlResult[i][j];
						}
					}
				}
				else
				{
					strArray1D = new String[sqlResult.size()];
					for(int i = 0; i < sqlResult.size(); i++)
					{
						strArray1D[i] =  sqlResult[i][0];
					}
				}
		   }
		   else
		   {			    
			   if(this.log)
			   {				   
				   this.log.info "Returning empty Array for SQL Result."
			   }		   
		   }		   
	   }
	   catch(Throwable e)
	   {
		   if(this.log)
		   {
			   this.log.info e;
			   this.log.info "Returning empty Array for SQL Result."
		   }		   
	   }
	   if(envIdentifier != null)
	   {
		   dbConnClose();
	   }	   
	   if(columnList.size() > 1)
	   {
		   return(strArray2D);
	   }
	   else
	   {
		   return(strArray1D);
	   }	   
   }
	/**
	 * Performs an Insert operation on the Database based on
	 * the SQL query provided by the user.
	 * 
	 * @param String sqlStr Any valid SQL 'Select' Statement.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 * 
	 * @return The number of rows affected by the Insert operation or
	 * an empty String with appropriate message if Update failed.
	 */
	def Integer InsertIntoDB(String sqlStr, String envIdentifier = null) 
	{
		private Integer rowCount;
		if(envIdentifier != null) 
		{						
			dbConnEstablish(envIdentifier);
		}
		else
		{
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);  
			}
		}
		try 
		{			
			rowCount = dbConn.execute(sqlStr);
		}
		catch(Throwable e) 
		{
			rowCount = 0;
			if(this.log) 
			{
				this.log.info e;
			}
		}
		if(envIdentifier != null) 
		{
			dbConnClose(); 
		}
		return rowCount;
	}
	/**
	 * Performs an Update operation on the Database based on
	 * the SQL query provided by the user.
	 * 
	 * @param String sqlStr Any valid SQL 'Select' Statement.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 * 
	 * @return The number of rows affected by the Update operation or
	 * an empty String with appropriate message if Update failed.
	 */
	def Integer UpdateDB(String sqlStr, String envIdentifier = null) 
	{
		private Integer rowCount;
		if(envIdentifier != null) 
		{				
			dbConnEstablish(envIdentifier);
		}
		else
		{
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);
			}
		}
		try 
		{			
			rowCount = dbConn.executeUpdate(sqlStr);
		}
		catch(Throwable e) 
		{
			rowCount = 0;
			if(this.log) 
			{
				this.log.info e;
			}
		}
		if(envIdentifier != null) 
		{
			dbConnClose();
		}
		return rowCount;
	}
	/**
	 * Performs a Delete operation on the Database based on
	 * the SQL query provided by the user.
	 * 
	 * @param String sqlStr Any valid SQL 'Select' Statement.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 * 
	 * @return The number of rows affected by the Delete operation or
	 * and empty String with appropriate message if Update failed.
	 */
	def Integer DeleteFromDB(String sqlStr, String envIdentifier = null) 
	{
		private Integer rowCount = 0;
		if(envIdentifier != null) 
		{						
			dbConnEstablish(envIdentifier);
		}
		else
		{
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);
			}
		}
		try 
		{	
            if(dbConn.execute(sqlStr) != false)
            {
                rowCount = dbConn.execute(sqlStr);
            }
		}
		catch(Throwable e) 
		{   			
			if(this.log) 
			{
				this.log.info e;
			}
		}
		if(envIdentifier != null) 
		{
			dbConnClose();
		}
		return rowCount;
	}
	/**
	 * Converts a Hexadecimal String to a Decimal String
	 * 
	 * @param String hexNum Any valid SQL 'Select' Statement.
	 *  
	 * @return A Decimal Representation of the provided
	 * Hexadecimal string.
	 */
	def String Hex2Dec(String hexNum) 
	{
		private Integer i;
		private Integer numDigits;
		private char currentDigit;
		private Integer currentDigitDec;
		private Integer decResult;
		private String decResultStr;

		numDigits = hexNum.length();
		decResult = 0;
		for(i = 0; i < numDigits; i++) 
		{
			currentDigit = hexNum[i..i];
			if(currentDigit == 'A' || currentDigit == 'B'|| currentDigit == 'C'|| currentDigit == 'D'|| currentDigit == 'E'|| currentDigit == 'F') 
			{
				currentDigitDec = (int) currentDigit - (int) 'A' + 10;
			}
			else 
			{
				currentDigitDec = (int) currentDigit;
			}
			decResult = (decResult * 16) + currentDigitDec;
		}
		decResultStr = decResult.toString()[0..5];
		return decResultStr;
	}
	/**
	 * Converts a Hexadecimal String to a Decimal String
	 * 
	 * @param String tableName A valid table in the Database identified by <strong>envIdentifier</strong>.
	 * @param String columnName A valid column in a table in the Database identified by <strong>envIdentifier</strong>.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 *  
	 * @return A Decimal String representing the next Primary Key value
	 * in the table.
	 */
	def String getNextPKVal(String tableName, String columnName, String envIdentifier = null) 
	{
		if(envIdentifier != null) 
		{						
			dbConnEstablish(envIdentifier);
		}
		else
		{
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);
			}
		}
		private Integer countTable;
		private String hexPKValue;
		private String strPKValue;
		private Integer decPKValue;
		private String padDecPKValue;
		private String sqlStr;
		private Integer countKeyColumn;
		sqlStr = """select count(*) from $tableName""";		
		dbConn.eachRow(sqlStr)
		{
			countTable = it[0];
		}
		if(countTable == 0) 
		{
			padDecPKValue = '1';
		}
		else 
		{
			sqlStr = """select rawtohex(max($columnName)) from $tableName""";
			dbConn.eachRow(sqlStr)
			{
				hexPKValue = it[0];
			}
			strPKValue = Hex2Dec(hexPKValue);
			decPKValue = strPKValue.toInteger() + 1;
			padDecPKValue = String.format('%06d', decPKValue.abs());
			sqlStr = """select count(*) from $tableName where $columnName = '$padDecPKValue'""";
			dbConn.eachRow(sqlStr)
			{
				countKeyColumn = it[0];
			}
			while(countKeyColumn != 0) 
			{
				decPKValue = decPKValue + 1;
				padDecPKValue = String.format('%06d', decPKValue.abs());
				sqlStr = """select count(*) from $tableName where $columnName = '$padDecPKValue'""";
				dbConn.eachRow(sqlStr)
				{
					countKeyColumn = it[0];
				}
			}
		}
		if(envIdentifier != null) 
		{
			dbConnClose();
		}
		return padDecPKValue;
	}
	/**
	 * Executes a 'Select' SQL query provided by the user
	 * and returns a String Array.
	 * 
	 * @param String sqlStr Any valid SQL 'Select' Statement.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 * 
	 * @return A String Array containing the rows returned by the SQL query.
	 */
	def dbConnExecuteQuery(String sqlStr, String envIdentifier = null) 
	{
		private rowData;        		
		if(envIdentifier != null) 
		{						
			dbConnEstablish(envIdentifier);
		}
		else
		{ 		  
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);				
			}
		}
		if(this.log) 
		{
			this.log.info "dbConnExecuteQuery: $sqlStr";
		}		
		try 
		{			
			rowData = dbConn.rows(sqlStr);
			if(this.log) 
			{
				this.log.info "dbConnExecuteQuery:  rowData is " + rowData;
			}
		}
		catch(Throwable e) 
		{
			if(this.log) 
			{
				this.log.info e;
				this.log.info "Returning empty string for Result Set."
			}
			rowData = "";
		}
		if(envIdentifier != null) 
		{
			dbConnClose();
		}
		return(rowData);
	}
	/**
	 * Executes a 'Select' SQL query provided by the user
	 * and returns a String Array containing a single row.
	 * 
	 * @param String sqlStr Any valid SQL 'Select' Statement.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 * 
	 * @return A String Array containing a single row returned by the SQL query.
	 */
	def dbConnExecuteQuerySingleRow(sqlStr, String envIdentifier = null) 
	{		
		private rowData;
		if(envIdentifier != null) 
		{						
			dbConnEstablish(envIdentifier);
		}
		else
		{
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);
			}
		}
		try 
		{			
			def row = dbConn.firstRow(sqlQ);
		}
		catch(Throwable e) 
		{
			if(this.log) 
			{
				this.log.info e;
				this.log.info "Returning empty string for Result Set."
			}
			rowData = "";
		}
		if(envIdentifier != null) 
		{
			dbConnClose();
		}
		return(rowData);
	}
	/**
	 * Executes a 'Select' SQL query provided by the user and returns
	 * result set with complete Column name and value.
	 * 
	 * @param String sqlStr Any valid SQL 'Select' Statement.
	 * @param String columnName A valid column in a table in the Database identified by <strong>envIdentifier</strong>.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 * 
	 * @return A result set with complete Column name and value.
	 */
	def dbConnReturnColumnValue(String sqlStr, String columnName, String envIdentifier = null) 
	{		
		if(envIdentifier != null) 
		{		
			dbConnEstablish(envIdentifier);
		}
		else
		{
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);
			}
		}
		private row;
		private rowVal;
		try 
		{			
			row    = dbConn.firstRow(sqlStr);
			rowVal = row[columnName].toString();
		}
		catch(Throwable e) 
		{
			if(this.log) 
			{
				this.log.info e;
				this.log.info "Returning empty string for Result Set."
			}
			rowVal = "";
		}
		if(envIdentifier != null) 
		{
			dbConnClose();
		}
		return(rowVal);
	}
	/**
	 * Performs an Update operation on the Database based on
	 * the SQL query provided by the user.
	 *
	 * @param String sqlStr Any valid SQL 'Select' Statement.
	 * @param String envIdentifier Valid Values are 'LP1', 'LP2', 'LP3'.
	 *
	 * @return The number of rows affected by the Update operation or
	 * an empty String with appropriate message if Update failed.
	 * 
	 * @deprecated Replaced by <code>UpdateDB(String)</code>
	 * @see #UpdateDB(String, String)
	 */	
	def dbConnExecuteUpdate(sqlStr, String envIdentifier = null) 
	{
		if(envIdentifier != null) 
		{					
			dbConnEstablish(envIdentifier);
		}
		else
		{
			if(dbConn == null)
			{
				dbConnEstablish(this.dbIdentifier);
			}
		}
		if(this.log) 
		{
			this.log.info "dbConnExecuteUpdate: $sqlStr";
		}
		private int rc;
		try 
		{			
			rc = dbConn.executeUpdate(sqlStr);
			if(this.log) 
			{
				this.log.info "dbConnExecuteUpdate: rc is " + rc;
			}
		}
		catch(Throwable e) 
		{
			if(this.log) 
			{
				this.log.info e;
				this.log.info "Returning empty string for Result Set."
			}
			rc = 0;
		}
		if(envIdentifier != null) 
		{
			dbConnClose();
		}
		return(rc);
	}
	/**
	 * Provides a Database connection object to the user.
	 * 
	 * @return A Database Connection Object for interacting with the database.
	 * 
	 * @deprecated Replaced by <code>dbConnEstablish(String)</code>
	 * @see #dbConnEstablish(String)
	 */
	def dbConnGet() 
	{
		return(dbConn);
	}
}
