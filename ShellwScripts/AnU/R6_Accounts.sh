#/bin/bash

ENV=QALP3LBS
versionMajor=1
versionMinor=11


Appender='R6_PC_Reports'
echo "#### Running on Env : "  $ENV '  ' $Appender

regressionStartTime=`date +"%F::%H-%M-%S"`
currentTime=`date +"%F::%H-%M-%S"`

folderToExportResultsTo='/opt/dece/soapUIReports/'$Appender$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
timeStampsFile='/opt/dece/timeStamps/timeStampsFile_R6-'$regressionStartTime
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/111/Accounts_Users'


cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin/

mkdir $folderToExportResultsTo

echo "R6 Projects Started at $currentTime" >> $timeStampsFile
project=$projectPath'/R6_AccountCreate-soapui-project.xml'

./testrunner.sh -r -j -f$folderToExportResultsTo/AccountCreate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/AccountCreate.log

echo "Execution OF THE AccountCreate PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 AccountCreate PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_Account_Get-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/AccountGet  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/AccountGet.log

echo "Execution OF THE Account Get PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 Account Get PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_AccountDelete_SoapUi_Project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/AccountDelete  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/AccountDelete.log 

echo "Execution OF THE AccountDelete PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE AccountDelete PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_AccountUpdate-SoapUI-Project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/AccountUpdate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/AccountUpdate.log

echo "Execution OF THE R6 Account Update PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 Account Update PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_UserGetList.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/UserGetList  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/UserGetList.log

echo "Execution OF THE R6 UserGetList PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6 UserGetList PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_UVTC_PrimaryEmail.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/UVTC_PrimaryEmail  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/UVTC_PrimaryEmail.log

echo "Execution OF THE R6_UVTC_PrimaryEmail PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6_UVTC_PrimaryEmail PROJECT Completed at $currentTime" >> $timeStampsFile


project=$projectPath'/Backward_Compatibility_111_107.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/Backward_Compatibility  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/Backward_Compatibility.log


echo "Execution OF Backward_Compatibility PROJECT IS DONE"
currentTime=`date`
echo "Execution OF Backward_Compatibility PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R6_Cache_Account_User_project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/R6_Cache_Account_User_project  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Users -PversionMajor=$versionMajor -PversionMinor=$versionMinor $project | tee -a $folderToExportResultsTo/R6_Cache_Account_User_project.log


echo "Execution OF THE R6_Cache_Account_User_project PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R6_Cache_Account_User_project PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20


currentTime=`date +"%F::%H-%M-%S"`
echo "R6 Account Projects Completed at $currentTime" >> $timeStampsFile
