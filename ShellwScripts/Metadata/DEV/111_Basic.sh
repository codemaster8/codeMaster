#!/bin/sh

#
#Please Set below variables
#
versionMinor=11
versionMajor=1
ENV=DEV

regressionStartTime=`date +"%F::%H-%M-%S"`
folderToExportResultsTo='/opt/dece/SoapUIReports/'$versionMajor$versionMinor'/'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/'$versionMajor$versionMinor'/Metadata'

cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin


echo starting Metadata regression $versionMinor...
echo $regressionStartTime


project=$projectPath'/BasicList-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/BasicList -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Metadata -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project

sleep 20

project=$projectPath'/Bundle-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo/Bundle -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Metadata -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project

echo Finished Running Metadata Regression for $versionMajor/$versionMinor endpoint

