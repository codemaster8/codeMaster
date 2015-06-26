#/bin/bash
ENV='QALP3PC'
Appender='R4_PC_Reports'
echo "#### Running on Env : "  $ENV '  ' $Appender

regressionStartTime=`date +"%F::%H-%M-%S"`
currentTime=`date +"%F::%H-%M-%S"`

folderToExportResultsTo='/opt/dece/soapUIReports/'$Appender$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
timeStampsFile='/opt/dece/timeStamps/timeStampsFile_R4_Users-'$regressionStartTime
projectPath='/opt/dece/GitRepo/Projects/107/Accounts_Users'


echo "R4 Projects Started at $currentTime" >> $timeStampsFile

cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin/

project=$projectPath'/R4_UserCreate_Retailer_Portal.xml'
echo ./testrunner.sh -r -j -f$folderToExportResultsTo/UserCreate  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project
sleep 20
./testrunner.sh -r -j -f$folderToExportResultsTo/UserCreate  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4 UserCreate PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 UserCreate PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R4_UVTC_ACCUSERNAME.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/UVTC_ACCUSERNAME  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4 UVTC_ACCUSERNAME PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 UVTC_ACCUSERNAME PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R4_Resource_Property_Query.xml'
#./testrunner.sh -r -j -f$folderToExportResultsTo/RPQ  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4 Resource_Property_Query PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 Resource_Property_Query PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R4_UserGet_Retailer.xml'
#./testrunner.sh -r -j -f$folderToExportResultsTo/UserGet_Retailer  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4 UserGet_Retailer PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 UserGet_Retailer PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/R4_UserGet_Portal.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/UserGet_Portal  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4 UserGet_Portal PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4 UserGet_Portal PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20


project=$projectPath'/R4_AccountUserCreate.xml'
#./testrunner.sh -r -j -f$folderToExportResultsTo/R4_AccountUserCreate  -EDefault -I  -PENV=$ENV -t$soapuiSettingsFile -i $project

echo "Execution OF THE R4_AccountUserCreate PROJECT IS DONE"
currentTime=`date`
echo "Execution OF THE R4_AccountUserCreate PROJECT Completed at $currentTime" >> $timeStampsFile
sleep 20

currentTime=`date +"%F::%H-%M-%S"`
echo "R4 Projects Completed at $currentTime" >> $timeStampsFile
