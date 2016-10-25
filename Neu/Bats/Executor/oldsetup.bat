color a
@ECHO -institution- %1 
@ECHO -appWarVersion- %2
@ECHO -commonWarVersion- %3 
@ECHO -suiteToRun- %4 
@ECHO -Security- %5
@ECHO -extractionOutputFormat- %7
@ECHO -broker- %6
@ECHO "Backing Up Soap UI Settings file"
rm C:\Bats\Executor\soapui-settings_latest.xml
cp C:\Users\utkarsh_sinha\soapui-settings.xml C:\Bats\Executor\soapui-settings_latest.xml
del C:\Users\utkarsh_sinha\soapui-settings.xml
@ECHO "Copying Template File"
cp C:\Bats\Executor\soapui-settings_Template.xml C:\Users\utkarsh_sinha\soapui-settings.xml 

"C:\Program Files\SmartBear\SoapUI-Pro-5.0.0\bin\testrunner.bat" -PextractionOutputFormat=%7 -Pbroker=%6 -Pinstitution=%1 -PappWarVersion=%2 -PcommonWarVersion=%3 -Psecurity=%5 -s%4  -R"TestCase Report" -EDefault -Dsoapui.scripting.library=\\pun-srvfile01\Healthcare\xData\REPO\ENV_SETUP\clu-tools-soapui\src\main\groovy \\pun-srvfile01\Healthcare\xData\REPO\ENV_SETUP\clu-tools-soapui\src\main\groovy\com\nuance\clu\qaAutomation\projects\envSetup\envsetup-soapui-project

@echo "Replacing Back settings file"
rm C:\Users\utkarsh_sinha\soapui-settings.xml 
cp C:\Bats\Executor\soapui-settings_latest.xml C:\Users\utkarsh_sinha\soapui-settings.xml 