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
    
    def void setStepSSLSettingsKeyStore(RestTestRequestStep testStep, String keyStorePath)
    {   
        keyStorePath.replace("\\", "\\\\");
        File keyStoreFile = new File(keyStorePath);                      
        if(keyStoreFile.exists())
        {
            
            try
            {                   
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
	
	def Integer countTestCases(String projectName)	
	{
		Integer TCCount = 0;		
		Integer TSCount = 0;
		Integer TSTCCount = 0;
		Integer currentTSTCCount = 0;
		def testProject;
		def TSList;
		def TCList;
		def TSName;
		def workSpaceName = SoapUI.getWorkspace().name;		
		try
		{						
			testProject = SoapUI.getWorkspace().getProjectByName(projectName);				
			TSList = testProject.getTestSuiteList();
			TSCount = testProject.getTestSuiteCount(); 			
			for(Integer i = 0; i <= TSCount - 1; i++)
			{
				if(TSList[i].isDisabled() == false)
				{
					currentTSTCCount = 0;
					TSName = TSList[i].name;
					TCList = TSList[i].getTestCaseList();
					TSTCCount = TSList[i].getTestCaseCount();
					for(Integer j = 0; j <= TSTCCount - 1; j++)
					{
						if(TCList[j].isDisabled() == false)
						{
							currentTSTCCount = currentTSTCCount + 1;
							TCCount = TCCount + 1;
						}
					}
					if (this.log)
					{
						this.log.info "The total number of enabled test Cases in enabled Test Suite '" + TSName + "' in Project '" + projectName + "' is " + currentTSTCCount + ".";
					}
				}				
			}
		}
		catch(Throwable e)
		{
			if (this.log)
			{
				this.log.info e;
				this.log.info "Please ensure that the Project name is valid and that the project is open in the workspace '" + workSpaceName + "'.";
				this.log.info "Returning 0 for test case count."
			}
		}
		if (this.log)
		{
			this.log.info "The total number of enabled test Cases in all enabled Test Suites in Project '" + projectName + "' is " + TCCount + ".";
		}
		return TCCount;
	}
}
