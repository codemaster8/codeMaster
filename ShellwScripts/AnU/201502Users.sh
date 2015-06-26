#/bin/bash
ENV=QALP1
versionMajor=2015
versionMinor=02


echo "#### Running on Env : "  $ENV

regressionStartTime=`date +"%F::%H-%M-%S"`
currentTime=`date +"%F::%H-%M-%S"`

folderToExportResultsTo='/opt/dece/soapUIReports/Milk_Reports'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
timeStampsFile='/opt/dece/timeStamps/timeStampsFile_201502_Users-'$regressionStartTime
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/201502/Accounts_Users'


echo "R6 Projects Started at $currentTime" >> $timeStampsFile

cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin/

mkdir $folderToExportResultsTo
project=$projectPath'/R6_UserCreate_Retailer_Portal.xml'

./testrunner.sh -r -j -f$folderToExportResultsTo/UserCreate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project  | tee -a $folderToExportResultsTo/UserCreate.log

echo "Execution OF THE R6 UserCreate PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 UserCreate PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_UVTC_ACCUSERNAME.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/UVTC_ACCUSERNAME  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/UVTC_ACCUSERNAME.log

echo "Execution OF THE R6 UVTC_ACCUSERNAME PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 UVTC_ACCUSERNAME PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_Resource_Property_Query.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/RPQ  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project  | tee -a $folderToExportResultsTo/RPQ.log

echo "Execution OF THE R6 Resource_Property_Query PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 Resource_Property_Query PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_UserGet_Retailer.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/UserGet_Retailer  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/UserGet_Retailer.log

echo "Execution OF THE R6 UserGet_Retailer PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 UserGet_Retailer PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_UserGet_Portal.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/UserGet_Portal  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/UserGet_Portal.log

echo "Execution OF THE R6 UserGet_Portal PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 UserGet_Portal PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20


project=$projectPath'/R6_AccountUserCreate.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/R6_AccountUserCreate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/UserGet_Portal.log

echo "Execution OF THE R6_AccountUserCreate PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6_AccountUserCreate PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

currentTime=`date +"%F::%H-%M-%S"`
echo "R6 Projects Completed at $currentTime" >> $timeStampsFile
