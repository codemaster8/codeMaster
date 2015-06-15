#!/bin/sh


#
#Please Set below variables
#
versionMinor=07
versionMajor=1
ENV=QALP3LBS

regressionStartTime=`date +"%F::%H-%M-%S"`
folderToExportResultsTo='/opt/dece/SoapUIReports/'$versionMinor'/'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/107/Streams'

cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin


echo starting STREAMS regression 107...
echo $regressionStartTime

project=$projectPath'/StreamView_change_in_org-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM VIEW PROJECT IS DONE"

project=$projectPath'/StreamView_LLP_change_in_org-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM VIEW LLP PROJECT IS DONE"

project=$projectPath'/StreamView_ISCPortalNode_change_in_org-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM VIEW ISC PORTAL PROJECT IS DONE"

project=$projectPath'/StreamListView-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM LIST VIEW PROJECT IS DONE"

project=$projectPath'/StreamListView_DECECS_COORDCS-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM LIST VIEW DECECS AND COORDCS PROJECT IS DONE"

project=$projectPath'/StreamListView-LLP-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM LIST VIEW LLP PROJECT IS DONE"

project=$projectPath'/StreamListView_ISCPortalNode-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Streams -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE STREAM VIEW CHANGE IN ORG PROJECT IS DONE"