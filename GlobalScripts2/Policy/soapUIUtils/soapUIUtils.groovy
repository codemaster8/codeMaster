// Copyright (c) 2010 Bhavin Bharat Joshi - Neustar, Inc. All Rights Reserved.

package soapUIUtils;

// SOAPUI
import com.eviware.soapui.SoapUI;
import com.eviware.soapui.settings.SSLSettings; 

import com.eviware.soapui.model.testsuite.*;
//import com.eviware.soapui.impl.wsdl.teststeps.*;
import com.eviware.soapui.model.testsuite.TestRunContext;
import com.eviware.soapui.impl.wsdl.testcase.*;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestRequestStep;
import com.eviware.soapui.impl.wsdl.teststeps.RestTestRequestStep;
import com.eviware.soapui.impl.WorkspaceImpl;
import com.eviware.soapui.model.project.*;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

import au.com.bytecode.opencsv.CSVReader;

//import com.eviware.soapui.impl.wsdl.support.wss.DefaultWssContainer;

public class soapUIUtils 
{   
    def log;
    
	// Constructors
	soapUIUtils(log) 
    {
		this.log = log;
	}	
	soapUIUtils() 
    {
	}
    
    // Methods  
    def void setSSLSettingsKeyStore(String keyStorePath)
    {   
        keyStorePath.replace("\\", "\\\\");
        File keyStoreFile = new File(keyStorePath);
        if(keyStoreFile.exists())
        {
            try
            {
                SoapUI.getSettings().setString(SSLSettings.KEYSTORE, keyStorePath);        
				this.log.info "Done Setting SSL Keystore to " + keyStoreFile.name;                               				
            }
            catch(Throwable e)
            {
                if (this.log) 
                {
                    this.log.info e;
                }
            }
        } 
        else
        {
            if (this.log) 
            {
                this.log.info "File '" + keyStorePath + "' Not Found. KeyStore Not Set.";
            }
        }   
        SoapUI.saveSettings();                                                                          
    }
    
    
    
    
    
    def void setHTTPBasicAuthCredentials(RestTestRequestStep testStep, String userName, String passWord, String domain)
    {      
        try
        {  
            testStep.getTestRequest().setUsername(userName); 
            testStep.getTestRequest().setPassword(passWord);
            testStep.getTestRequest().setDomain(domain);
            
            this.log.info "Done Setting Username for test step '" + testStep.name + "' to '" + userName + "', password to '" + passWord + "' and domain to '" + domain + "'."                                
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
        }                                                                                         
    }
    
    def void setStepSSLSettingsKeyStore(RestTestRequestStep testStep, String keyStorePath)
    {
        /*def testProject;
        def wssContainer;*/ 		  
        keyStorePath.replace("\\", "\\\\");
        File keyStoreFile = new File(keyStorePath);                      
        if(keyStoreFile.exists())
        {
            
            try
            {  
                /*testProject = SoapUI.getWorkspace().getProjectByName("UseCase APIs");
				wssContainer = new DefaultWssContainer(testProject.getModelItem(), testProject.getConfig().addNewWssContainer());
				x.addCrypto("I:\\deceapi\\certs\\35001005_concat.pem", "testing");*/
                testStep.getTestRequest().setSslKeystore(keyStoreFile.name); 
                this.log.info "Done Setting SSL Keystore to " + keyStoreFile.name;                               
            }
            catch(Throwable e)
            {
                if (this.log) 
                {
                    this.log.info e;
                }
            }
        } 
        else
        {
            if (this.log) 
            {
                this.log.info "File '" + keyStorePath + "' Not Found. KeyStore Not Set.";
            }
        }                                                                                  
    }
    
    def void dumpRESTRawRequestResponse(String resultsDir, String testCaseName, Vector<RestTestRequestStep> testSteps, boolean runLoop = false)
    {      
        try
        {               
//     		DateFormat ymdhms = new SimpleDateFormat("yyyyMMdd_HHmmss");
//     		Date date = new Date();
//     		String ymdhmsTime = ymdhms.format(date); 
    		
    		RestTestRequestStep testStep = null;
    		
    		StringBuffer rawRequestResponse = new StringBuffer();
    		String rawRequest = "";
    		String rawResponse = ""; 
    		
//     		def resultsLocation = new File(resultsDir + "\\" + testCaseName).isDirectory();
//     		if(resultsLocation != true)
//     		{
//                 new File(resultsDir + "\\" + testCaseName).mkdirs();   
//          }          
            for(int i = 0; i < testSteps.size(); i++)
            {
                testStep = testSteps.get(i);
                
                rawRequest = new String(testStep.httpRequest.response.getRawRequestData());
                rawResponse = new String(testStep.httpRequest.response.getRawResponseData());
                
                rawRequestResponse.append("--------------------------------------------------------------------------------\n");
                rawRequestResponse.append(testStep.getName());
                rawRequestResponse.append("\n--------------------------------------------------------------------------------\n\n");
                rawRequestResponse.append("Request:\n\n");
                rawRequestResponse.append(rawRequest);
                rawRequestResponse.append("\n\n");
                rawRequestResponse.append("Response:\n\n");
                rawRequestResponse.append(rawResponse);                
                rawRequestResponse.append("\n");
                
                this.log.info "Done writing Raw Request for test step '" + testStep.getName() + "' to '" + resultsDir + "\\" + testCaseName + "."                                
                this.log.info "Done writing Raw Response for test step '" + testStep.getName() + "' to '" + resultsDir + "\\" + testCaseName + "." 
            } 
//          new File(resultsDir + "\\" + testCaseName + "\\" + "Run_" + ymdhmsTime + ".txt" ) << rawRequestResponse.toString();
            File resultsFile = new File(resultsDir + "\\" + testCaseName + ".txt"); 
            if(!runLoop || runLoop == null)
            {
                resultsFile.newWriter();
            }
            resultsFile << rawRequestResponse.toString();
        }
        catch(Throwable e)
        {
            if (this.log) 
            {
                this.log.info e;
            }
        }                                                                                         
    }    
}
