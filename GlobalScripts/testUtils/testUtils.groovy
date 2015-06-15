// Copyright (c) 2010 Bhavin Bharat Joshi - Neustar, Inc. All Rights Reserved.

package testUtils;

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
public class testUtils
{
	private log;

	testUtils(log)
	{
		this.log = log;
		this.log.info("Object Initialized  successfully")
	}
	
	def printArray(){
	
		for(int i=0; i<10 ; i++){
			this.log.info("Array Integer : '" + i+ "'");
		}	
	}
}	