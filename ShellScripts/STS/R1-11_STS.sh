#/bin/bash

#
#Please Set below variables
#
versionMinor=11
versionMajor=1
ENV=QALP3LBSPC


regressionStartTime=`date +"%F:%H-%M-%S"`
currentTime=`date +"%F:%H-%M-%S"`

folderToExportResultsTo='/opt/dece/SoapUIReports/'$versionMinor'/'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
timeStampsFile='/opt/dece/timeStamps/timeStampsFile_1-11-'$regressionStartTime
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/111/STS'

cd /home/QAUV/SmartBear/SoapUI-Pro-4.6.1/bin

currentTime=`date`
echo "STSAPI_AccountMerge_TCs_R6_LP1 Project Started at $currentTime" >> $timeStampsFile
project=$projectPath'/STSAPI_AccountMerge_TCs_R6_LP1-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/STSAPI_AccountMerge_TCs_R6_LP1  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/STS -PversionMajor=$versionMajor -PversionMinor=$versionMinor -PENV=$ENV -t$soapuiSettingsFile -i $project
currentTime=`date`
echo "STSAPI_AccountMerge_TCs_R6_LP1 Project Ended at $currentTime" >> $timeStampsFile


sleep 20

currentTime=`date`
echo "STSAPI_Device_P0_P1_P2_TCs_R6_LP1 Project Started at $currentTime" >> $timeStampsFile
project=$projectPath'/STSAPI_Device_P0_P1_P2_TCs_R6_LP1-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/STSAPI_Device_P0_P1_P2_TCs_R6_LP1  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/STS -PversionMajor=$versionMajor -PversionMinor=$versionMinor -PENV=$ENV -t$soapuiSettingsFile -i $project
currentTime=`date`
echo "STSAPI_Device_P0_P1_P2_TCs_R6_LP1 Project Ended at $currentTime" >> $timeStampsFile

sleep 20

currentTime=`date`
echo "STSAPI_FUNC_AffiliatedNodes_SAMLExchange_TCs_R6_LP1 Project Started at $currentTime" >> $timeStampsFile
project=$projectPath'/STSAPI_FUNC_AffiliatedNodes_SAMLExchange_TCs_R6_LP1-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/STSAPI_FUNC_AffiliatedNodes_SAMLExchange_TCs_R6_LP1  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/STS -PversionMajor=$versionMajor -PversionMinor=$versionMinor -PENV=$ENV -t$soapuiSettingsFile -i $project
currentTime=`date`
echo "STSAPI_FUNC_AffiliatedNodes_SAMLExchange_TCs_R6_LP1 Project Ended at $currentTime" >> $timeStampsFile

sleep 20

currentTime=`date`
echo "STSAPI_NodeRole_NonCS_TCs_R6_LP1 Project Started at $currentTime" >> $timeStampsFile
project=$projectPath'/STSAPI_NodeRole_NonCS_TCs_R6_LP1-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/STSAPI_NodeRole_NonCS_TCs_R6_LP1  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/STS -PversionMajor=$versionMajor -PversionMinor=$versionMinor -PENV=$ENV -t$soapuiSettingsFile -i $project
currentTime=`date`
echo "STSAPI_NodeRole_NonCS_TCs_R6_LP1 Project Ended at $currentTime" >> $timeStampsFile

sleep 20

currentTime=`date`
echo "STSAPI_NodeRole_CS_TCs_R6_LP1 Project Started at $currentTime" >> $timeStampsFile
project=$projectPath'/STSAPI_NodeRole_CS_TCs_R6_LP1-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/STSAPI_NodeRole_CS_TCs_R6_LP1  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/STS -PversionMajor=$versionMajor -PversionMinor=$versionMinor -PENV=$ENV -t$soapuiSettingsFile -i $project
currentTime=`date`
echo "STSAPI_NodeRole_CS_TCs_R6_LP1 Project Ended at $currentTime" >> $timeStampsFile

sleep 20

currentTime=`date`
echo "STSAPI_Payload_NodeOId_TCs_R6_LP1 Project Started at $currentTime" >> $timeStampsFile
project=$projectPath'/STSAPI_Payload_NodeOId_TCs_R6_LP1-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/STSAPI_Payload_NodeOId_TCs_R6_LP1  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/STS -PversionMajor=$versionMajor -PversionMinor=$versionMinor -PENV=$ENV -t$soapuiSettingsFile -i $project
currentTime=`date`
echo "STSAPI_Payload_NodeOId_TCs_R6_LP1 Project Ended at $currentTime" >> $timeStampsFile

