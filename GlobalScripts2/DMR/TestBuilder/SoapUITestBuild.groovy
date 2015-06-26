// Copyright (c) 2012 Sarvjeet Chhinna - Neustar, Inc. All Rights Reserved.

package SoapUITestBuild;

// SOAPUI
import com.eviware.soapui.SoapUIProTestCaseRunner;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.support.*;
import com.eviware.soapui.model.*;
import com.eviware.soapui.impl.wsdl.*;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestSuiteRunner;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCaseRunner;
import com.eviware.soapui.impl.wsdl.WsdlTestSuite;
import com.eviware.soapui.model.project.*;
import com.eviware.soapui.*;
import jxl.*; 

public class SoapUITestBuild 
{   
		def log;
		def Tc;
		def testProject;
		def testSuite;
		String TcName;
		String TcDesc;
		String TestStepName;
		
		// Constructors
		
		SoapUITestBuild(log) 
		{
			this.log = log;
		}	
		SoapUITestBuild() 
		{
		}
   
			// Methods  
		def void setUPSoapUItestSuite(String TCsFeedPath, String TestSuite, String TestProject)
		{   	
				testProject = SoapUI.getWorkspace().getProjectByName(TestProject);
				testSuite = testProject.getTestSuiteByName(TestSuite);
						
					File TCsFiletoFeed = new File(TCsFeedPath);
					this.log.info "File Found at :" + TCsFeedPath;
			if(TCsFiletoFeed.exists())
			{					
					try
					{
						Workbook inputFile = Workbook.getWorkbook(TCsFiletoFeed);
						Sheet inputWorkSheet = inputFile.getSheet("Sheet1");

						int rowcount = inputWorkSheet.getRows();
						int colcount = inputWorkSheet.getColumns();

						for( tc_row in 1..rowcount-1){

							Cell TestCaseName = inputWorkSheet.getCell(0,tc_row);
							Cell TestCaseDescription = inputWorkSheet.getCell(1,tc_row);

							TcName = TestCaseName.getContents();
							TcDesc = TestCaseDescription.getContents();

							//add New testCases
							testSuite.addNewTestCase("TC_MERC_000$tc_row"+"_"+ TcName);
							 Tc = testSuite.getTestCaseAt(tc_row-1);
					  
							// add Description to test Cases
						
							if( Tc in testSuite.testCaseList){
							Tc.setDescription(TcDesc);
							log.info Tc.getName() + "	:" + Tc.getDescription();

							}
						
						// adding New test steps to TestCases
					
							for( tc_col in 2..colcount-1){	
							Cell TestSteps = inputWorkSheet.getCell(tc_col,tc_row);
							TestStepName = TestSteps.getContents();
							log.info TestStepName
							Tc.addTestStep("groovy", TestStepName );
							}			
						}
					} catch(Exception e) {
						System.out.println("Error: " + e);
						e.printStackTrace();
						}
			}   
			else
			{
				if (this.log) 
				{
                this.log.info "File to Feed Test Builder'" + TCsFeedPath + "' Not Found.";
				}
			}   
		}
		def void UpdateTCdescr(String TCsFeedPath, String TestSuite, String TestProject)
		{
				testProject = SoapUI.getWorkspace().getProjectByName(TestProject);
					testSuite = testProject.getTestSuiteByName(TestSuite);
							
						File TCsFiletoFeed = new File(TCsFeedPath);
						this.log.info "File Found at :" + TCsFeedPath;
				if(TCsFiletoFeed.exists())
				{					
						try
					{
		
		
						Workbook inputFile = Workbook.getWorkbook(TCsFiletoFeed);
						Sheet inputWorkSheet = inputFile.getSheet("Sheet2");

						int rowcount = inputWorkSheet.getRows();
						int colcount = inputWorkSheet.getColumns();

						for( tc_row in 1..rowcount-1){

							Cell TestCaseName = inputWorkSheet.getCell(0,tc_row);
							Cell TestCaseDescription = inputWorkSheet.getCell(1,tc_row);

							TcName = TestCaseName.getContents();
							TcDesc = TestCaseDescription.getContents();
							log.info TcName;						
							// Update TC Description 
							//Tc = testSuite.getTestCaseAt(tc_row-1);
							  Tc = testSuite.getTestCaseByName(TcName);
							if( Tc in testSuite.testCaseList){
							Tc.setDescription(TcDesc);
							log.info Tc.getName() + "	:" + Tc.getDescription();
							}
						}
					} catch(Exception e) {
						System.out.println("Error: " + e);
						e.printStackTrace();
						}
				}   
			else
			{
				if (this.log) 
				{
                this.log.info "File to Feed Test Builder'" + TCsFeedPath + "' Not Found.";
				}	
			}
		}	
}		