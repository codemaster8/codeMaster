#!/bin/sh


#
#Please Set below variables
#
versionMinor=2015
versionMajor=02
ENV=QALP3LBS

regressionStartTime=`date +"%F::%H-%M-%S"`
folderToExportResultsTo='/opt/dece/SoapUIReports/'$versionMinor'/'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/201502/Rights'

cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin


echo starting Rights regression R201502...
echo $regressionStartTime


project=$projectPath'/RightsLocketDataGet_By_Reference_Query-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE FIRST PROJECT IS DONE"
currentTime=`date`
echo "R6 RightsLocketDataGet_By_Reference_Query Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsTokenCreate-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE SECOND PROJECT IS DONE"
currentTime=`date`
echo "R6 RTCreate Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsTokenDataGet_By_ALID-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE THIRD PROJECT IS DONE"
currentTime=`date`
echo "R6 RTGByALID Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsTokenDataGet_By_APID-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE FOURTH PROJECT IS DONE"
currentTime=`date`
echo "R6 RTGByAPID Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLockerDataGet_By_DownLoadQuery-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE FIFTH PROJECT IS DONE"
currentTime=`date`
echo "R6 RLDGDownloadQuery Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLockerDataGet_By_Token-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE SIXTH PROJECT IS DONE"
currentTime=`date`
echo "R6 RLDGList  Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLockerDataGet_By_Default-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE SEVENTH PROJECT IS DONE"
currentTime=`date`
echo "R6 RLDGBYDEFAULT_WithoutSAML Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLocketDataGet_By_MetaData_Query-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE EIGHTH PROJECT IS DONE"
currentTime=`date`
echo "R6 RLDGMetadataQuery Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLocketDataGet_By_MetaData_Query-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE EIGHTH PROJECT IS DONE"
currentTime=`date`
echo "R6 RLDGMetadataQuery Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLockerDataGet_By_token_withoutSAML-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE NINETH PROJECT IS DONE"
currentTime=`date`
echo "R6 RightsLockerDataGet_By_token_withoutSAML Completed at $currentTime" >> $timeStampsFile
sleep 20

currentTime=`date +"%F:%H-%M-%S"`
echo "Finished Running R6 Rights part 1 Regression for 2015/02 endpoint" >> $timeStampsFile


