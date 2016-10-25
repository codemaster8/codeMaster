color a
@ECHO -institution- %1 
@ECHO -appWarVersion- %2
@ECHO -commonWarVersion- %3 
@ECHO -Security- %4
@ECHO -broker- %5
@ECHO -TomcatJava- %6
@ECHO -suiteToRun- %7
@ECHO -extractionOutputFormat- %8


"C:\Program Files\SmartBear\SoapUI-Pro-5.0.0\bin\testrunner.bat" -Pinstitution=%1 -PappWarVersion=%2 -PcommonWarVersion=%3 -Psecurity=%4 -Pbroker=%5 -PextractionOutputFormat=%8 -PTomcatJava=%6 -s%7 -R"TestCase Report" -EDefault \\pun-srvfile01\Healthcare\xData\REPO\ENV_SETUP\clu-tools-soapui\src\main\groovy\com\nuance\clu\qaAutomation\projects\envSetup\envsetup-soapui-project


