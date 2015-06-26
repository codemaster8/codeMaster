
#/bin/bash


#
#Please Set below variables
#
versionMajor=2015
versionMinor=02

ENV=QALP1
Appender='803'

regressionStartTime=`date +"%F::%H-%M-%S"`
folderToExportResultsTo='/opt/dece/SoapUIReports/'$versionMinor'_'$Appender'_'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
timeStampsFile='/opt/dece/timeStamps/timeStampsFile_201502-'$regressionStartTime
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/201502/DMR'

echo "201502 Projects Started at $currentTime" >> $timeStampsFile

cd /opt/smartbear/SoapUI-Pro-4.6.1/bin/

mkdir $folderToExportResultsTo

project=$projectPath'/DiscreteMediaRightCreate.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightCreate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightCreate.log

echo "Execution OF THE FIRST PROJECT IS DONE"
currentTime=`date`
echo "201502 Project1 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightConsume.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightConsume  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightConsume.log

echo "Execution OF THE SECOND PROJECT IS DONE"
currentTime=`date`
echo "201502 Project2 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightUpdate.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightUpdate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightUpdate.log

echo "Execution OF THE THIRD PROJECT IS DONE"
currentTime=`date`
echo "201502 Project3 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightGet.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightGet  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightGet.log

echo "Execution OF THE FOURTH PROJECT IS DONE"
currentTime=`date`
echo "201502 Project4 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightDelete.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightDelete  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightDelete.log

echo "Execution OF THE FIFTH PROJECT IS DONE"
currentTime=`date`
echo "201502 Project5 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightList.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightList  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightList.log

echo "Execution OF THE SIXTH PROJECT IS DONE"
currentTime=`date`
echo "201502 Project6 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightLeaseCreate.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightLeaseCreate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightLeaseCreate.log
echo "Execution OF THE SEVENTH PROJECT IS DONE"
currentTime=`date`
echo "201502 Project7 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightLeaseConsume.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightLeaseConsume  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightLeaseConsume.log

echo "Execution OF THE EIGHT PROJECT IS DONE"
currentTime=`date`
echo "201502 Project8 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightLeaseRelease.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightLeaseRelease  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightLeaseRelease.log

echo "Execution OF THE NINETH PROJECT IS DONE"
currentTime=`date`
echo "201502 Project9 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightLeaseRenew.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightLeaseRenew  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project | tee -a $folderToExportResultsTo/DiscreteMediaRightLeaseRenew.log
echo "Execution OF THE TENTH PROJECT IS DONE"
currentTime=`date`
echo "201502 Project10 Completed at $currentTime" >> $timeStampsFile

currentTime=`date +"%F##%H-%M-%S"`
echo "111 Projects Completed at $currentTime" >> $timeStampsFile

