#/bin/bash

ENV='QALP3PC'
Appender='R4_PC_Reports'
echo "#### Running on Env : "  $ENV '  ' $Appender


regressionStartTime=`date +"%F::%H-%M-%S"`
currentTime=`date +"%F::%H-%M-%S"`

folderToExportResultsTo='/opt/dece/soapUIReports/'$Appender$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
timeStampsFile='/opt/dece/timeStamps/timeStampsFile_R4-'$regressionStartTime
projectPath='/opt/dece/GitRepo/Projects/107/Accounts_Users'


cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin/

echo "R4 Projects Started at $currentTime" >> $timeStampsFile
project=$projectPath'/R4_AccountCreate-soapui-project.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/AccountCreate  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project
sleep 5
./testrunner.sh -r -j -f$folderToExportResultsTo/AccountCreate  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE AccountCreate PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 AccountCreate PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 5

project=$projectPath'/R4_Account_Get-soapui-project.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/AccountGet  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project
sleep 5
./testrunner.sh -r -j -f$folderToExportResultsTo/AccountGet  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE Account Get PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 Account Get PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 5

project=$projectPath'/R4_AccountDelete_SoapUi_Project.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/AccountDelete  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project
sleep 5

./testrunner.sh -r -j -f$folderToExportResultsTo/AccountDelete  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE AccountDelete PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE AccountDelete PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 5

project=$projectPath'/R4_AccountUpdate-SoapUI-Project.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/AccountUpdate  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project 
sleep 5

./testrunner.sh -r -j -f$folderToExportResultsTo/AccountUpdate  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4 Account Update PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 Account Update PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 5

project=$projectPath'/R4_UserGetList.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/UserGetList  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project
sleep 5

./testrunner.sh -r -j -f$folderToExportResultsTo/UserGetList  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4 UserGetList PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 UserGetList PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 5

project=$projectPath'/R4_UVTC_PrimaryEmail.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/UVTC_PrimaryEmail  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project
sleep 5

./testrunner.sh -r -j -f$folderToExportResultsTo/UVTC_PrimaryEmail  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4_UVTC_PrimaryEmail PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4_UVTC_PrimaryEmail PROJECT Completed at $currentTime" >> $timeStampsFile


project=$projectPath'/Backward_Compatibility_111_107.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/Backward_Compatibility  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project
sleep 5

./testrunner.sh -r -j -f$folderToExportResultsTo/Backward_Compatibility  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF Backward_Compatibility PROJECT IS DONE"
currentTime=`date`
echo "Execution OF Backward_Compatibility PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 5

project=$projectPath'/R4_Cache_Account_User_project.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/R4_Cache_Account_User_project  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project
sleep 5

./testrunner.sh -r -j -f$folderToExportResultsTo/R4_Cache_Account_User_project  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4_Cache_Account_User_project PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4_Cache_Account_User_project PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 5


currentTime=`date +"%F::%H-%M-%S"`
echo "R4 Account Projects Completed at $currentTime" >> $timeStampsFile
