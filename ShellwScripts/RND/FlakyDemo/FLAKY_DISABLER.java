//Project Properties will be loaded in the step "set ENV Properties"
//Pre-requisite will be to have the project level property enabled : enableFlakyScript=true // then only the following script will execute
def enableFlakyScript = context.expand( '${#Project#enableFlakyScript}' )
log.info "EXECUTE FLAKY DISABLER " + enableFlakyScript

if(enableFlakyScript=='true')
{
//TestCase
def testCase  = testRunner.getTestCase();

ArrayList<String> Suit_lst=new ArrayList<String>();
//Get Arguments

def project = context.testCase.testSuite.project;
//log.info suiteName.name;
for (suiteName in project.testSuiteList) 
Suit_lst.add(suiteName.name);
 
def projectName = project.name;


ArrayList<String> Tstep=new ArrayList<String>();
def count=1;
def f = new File("C:\\FLAKY\\$projectName"+".csv");
f.append("\r\n S.No,Project Name,TestSuite Name,Flaky TestCase Name");

//Looping through suites and test case list
for (String suit : Suit_lst)
{  def testSuite = suit;
     def TCList  = testRunner.testCase.testSuite.project.getTestSuiteByName(testSuite).getTestCaseList();
     def TCCount = testRunner.testCase.testSuite.project.getTestSuiteByName(testSuite).getTestCaseCount();

   for(i = 0; i <= TCCount - 1; i++)
     {          //Looping thorough test cases list
                def targetTestCase = testRunner.testCase.testSuite.project.getTestSuiteByName(testSuite).getTestCaseByName(TCList[i].name);
                def tstSuite=testRunner.testCase.testSuite.project.getTestSuiteByName(testSuite)
                testStepList  = targetTestCase.getTestStepList();
                testStepCount = targetTestCase.getTestStepCount();
               
                                                                if(targetTestCase.name.endsWith("flaky") || targetTestCase.name.endsWith("FLAKY"))
                                                                {   
                                                       		log.info "##################  $tstSuite.name  #####################";
                                                                  log.info targetTestCase.name
	                                                             targetTestCase.setDisabled(true)
													//Appending .csv file with flaky test cases list
                                                                  f.append("\r\n"+count+","+projectName+','+tstSuite.name+","+targetTestCase.name+"");
                                                                  count++;
                                                                }
                
    } 
}                                                                                             
}