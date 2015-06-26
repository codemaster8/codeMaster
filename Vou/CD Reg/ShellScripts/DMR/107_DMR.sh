#/bin/bash

#
#Please Set below variables
#
versionMinor=07
versionMajor=1
ENV=QALP3LBS

regressionStartTime=`date +"%F::%H-%M-%S"`
folderToExportResultsTo='/opt/dece/SoapUIReports/'$versionMinor'/'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
timeStampsFile='/opt/dece/timeStamps/timeStampsFile_107-'$regressionStartTime
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/107/DMR'

echo "107 Projects Started at $currentTime" >> $timeStampsFile

cd /opt/smartbear/SoapUI-Pro-4.6.1/bin/


project=$projectPath'/DiscreteMediaRightCreate.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightCreate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project

echo "Execution OF THE FIRST PROJECT IS DONE"
currentTime=`date`
echo "107 Project1 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightConsume.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightConsume  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project

echo "Execution OF THE SECOND PROJECT IS DONE"
currentTime=`date`
echo "107 Project2 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightUpdate.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightUpdate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project

echo "Execution OF THE THIRD PROJECT IS DONE"
currentTime=`date`
echo "107 Project3 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightGet.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightGet  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project

echo "Execution OF THE FOURTH PROJECT IS DONE"
currentTime=`date`
echo "107 Project4 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightDelete.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightDelete  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project

echo "Execution OF THE FIFTH PROJECT IS DONE"
currentTime=`date`
echo "107 Project5 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightList.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightList  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project

echo "Execution OF THE SIXTH PROJECT IS DONE"
currentTime=`date`
echo "107 Project6 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightLeaseCreate.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightLeaseCreate  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project
echo "Execution OF THE SEVENTH PROJECT IS DONE"
currentTime=`date`
echo "107 Project7 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightLeaseConsume.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightLeaseConsume  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project

echo "Execution OF THE EIGHT PROJECT IS DONE"
currentTime=`date`
echo "107 Project8 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightLeaseRelease.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightLeaseRelease  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project

echo "Execution OF THE NINETH PROJECT IS DONE"
currentTime=`date`
echo "107 Project9 Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/DiscreteMediaRightLeaseRenew.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/DiscreteMediaRightLeaseRenew  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/DMR -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor  -t$soapuiSettingsFile -i $project
echo "Execution OF THE TENTH PROJECT IS DONE"
currentTime=`date`
echo "107 Project10 Completed at $currentTime" >> $timeStampsFile

currentTime=`date +"%F##%H-%M-%S"`
echo "107 Projects Completed at $currentTime" >> $timeStampsFile

