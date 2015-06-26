package CacheTestManager;

/**
* Provides Utilities to Interact/Verify the DECE HTTP Cache Functionality.
*
* @author Sarvjeet Chhinna
* @version 1.0
*
*
*/

import groovy.*
import groovy.xml.*
import groovy.util.*;
import com.eviware.soapui.SoapUIProTestCaseRunner;
import com.eviware.soapui.tools.SoapUITestCaseRunner;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.support.*;
import com.eviware.soapui.model.*;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.impl.wsdl.teststeps.*
import com.eviware.soapui.model.testsuite.TestRunner;
import com.eviware.soapui.impl.wsdl.teststeps.RestTestRequestStep;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestSuiteRunner;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCaseRunner;
import com.eviware.soapui.impl.wsdl.WsdlTestSuite;
import com.eviware.soapui.support.types.StringToStringMap
import com.eviware.soapui.model.project.*;
import com.eviware.soapui.*;

public class CacheTestManager
{   
		def log;
		def TestName;
		def testProject;
		def tSuite;
		def tCase;
		def tStep;
		def Etag;
		def status;
		def Date;
		def ContentType;
		def ContentLength;
		def ContentEncoding;
		def LastModified;
		def CacheControl;
		def Server;
		def key, value;
		Map ResHeader;
		Map ReqHeader;
		def CacheHeaders = new Properties();  
		def headers = new StringToStringMap();
		def testrunner = new SoapUITestCaseRunner();
		def Xmlwriter = new StringWriter();
		def Xmlbuilder = new groovy.xml.MarkupBuilder(Xmlwriter);
		def DependencyURLConfig; 
		def DependencyUrls = new Properties();
		def tstepProp  = new Properties();
		
			
		// Constructors
		
		CacheTestManager(log) 
		{
			this.log = log;
		}
		
		CacheTestManager(){
		}
		
		CacheTestManager(def TestProject, String TestSuite, String TestCase, String TestStep) 
		{
		TestName = "Cache Testing";
		testProject = TestProject ; //SoapUI.getWorkspace().getProjectByName(TestProject);
		tSuite = testProject.getTestSuiteByName(TestSuite);
		tCase = testProject.getTestSuiteByName(TestSuite).getTestCaseByName(TestCase);
		tStep = testProject.getTestSuiteByName(TestSuite).getTestCaseByName(TestCase).getTestStepByName(TestStep);
		DependencyUrls = tCase.getTestStepByName('DependencyUrls').getProperties();
		
		}
		
	// Methods  
		
		/**
		* Returns the CacheHeaders for Resource API response.
		* @param CacheHeaderKey to extract.
		* @return CacheHeaderKey Value.
		*/
		
		def getCacheHeaders(String CacheHeader = null)
		{   	
		try{
			ResHeader = tStep.httpRequest.response.getResponseHeaders();
			if(!CacheHeader){
						
				return "Select the CacheHeader Key in -> $ResHeader"
			}
			else {
						ResHeader.each() { header ->
						CacheHeaders.put("${header.key}", "${header.value[0]}"); 
						}
				return CacheHeaders.get("$CacheHeader");
			}
            } catch(Exception e){     		
				e.printStackTrace();
				}
		}	
		
		/**
		* Method to set the CacheHeaders for required API Resource request with cache validators.
		* @param Cache Validator, CacheHeaderKey to set.
		* @return Cache Validator and Cache Header API in request.
		*/
		
		def setReqHeaders(String cacheValidator = null, String cacheHeader)
		{
			try{
				if( cacheValidator == null || cacheValidator.size() == 0) {
					
					headers.put("If-None-Match", cacheHeader);
				}
				else {
					headers.put(cacheValidator, cacheHeader);
				}
					tStep.testRequest.setRequestHeaders(headers);
				
					ReqHeader = tStep.httpRequest.getRequestHeaders();
					return "Request Header set to -> $ReqHeader";
			}catch(Exception e){     		
				e.printStackTrace();
				}
		}  
	
		/**
		* Method to Contruct the Expected API dependencyGraph.
		* @param API Class, Resource Method.
		* @return XML-formatted DependencyGraph .
		*/
				
		def checkAPIsDependency(String APIclass, String ResourceMethod){
			
			if(ResourceMethod == "POST" || ResourceMethod == "PUT" || ResourceMethod == "DELETE"){
				
				Xmlbuilder.DependencyURLConfig() {
				
				 "$APIclass"() {
					for (url in DependencyUrls.values().value) { "${APIclass}Resource"(url) }
								 
					}
				}
			
				DependencyURLConfig =  Xmlwriter.toString()
	 			return DependencyURLConfig;
				 
			}
			else{
				
				return "No Dependency Exists for this resource"
		}
	 }	
		
		/**
		* Method to Get Resource Property for API dependencyGraph.
		* @param Resource Property, Resource Request.
		* @return Resource PropertyValues.
		*/
		
		def getAPIResourceInfo(String Property, String tstepName = null){
			
			switch(Property){
				
				case "PATH":
				return tStep.httpRequest.getResource().getPath();
				break;
				case "METHOD":
				return tStep.httpRequest.method;
				break; 
				case "readProp":
				tstepProp = tCase.getTestStepByName(tstepName).getProperties();
				return tstepProp.values().value;
				break;
			}
			 
		}
		/**
		* Method to get API dependencyGraph URL Properties.
		**** Pending to add other functionality *******
		*/
		
		def readAPIdependency(){
			
			return DependencyUrls.keySet();
			
		}
}		