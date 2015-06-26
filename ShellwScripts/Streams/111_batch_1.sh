#!/bin/sh

#
#Please Set below variables
#
versionMinor=11
versionMajor=1
ENV=QALP3LBS

regressionStartTime=`date +"%F::%H-%M-%S"`
folderToExportResultsTo='/opt/dece/SoapUIReports/'$versionMinor'/'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/111/Streams'

cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin


echo starting STREAMS regression 111...
echo $regressionStartTime

project=$projectPath'/StreamCreate-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM CREATE LASP PROJECT IS DONE"

project=$projectPath'/StreamCreate-LLP-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM CREATE LLP PROJECT IS DONE"

project=$projectPath'/StreamDelete-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM DELETE LASP PROJECT IS DONE"

project=$projectPath'/StreamDelete-LLP-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM DELETE LLP PROJECT IS DONE"

project=$projectPath'/StreamRenew-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM RENEW LASP PROJECT IS DONE"

project=$projectPath'/StreamRenew-LLP-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM RENEW LLP PROJECT IS DONE"






