@echo off

echo "Start Executing"


"C:\Program Files\SmartBear\SoapUI-Pro-5.0.0\bin\testrunner.bat" -Dsoapui.https.protocols=TLSv1.2 -sBatchProcess_DataCreation -cTC_Steam_Create -a -j -f.\SoapUI\Output -EDefault .\SoapUI\Batch_Porcess.xml & exit