#!/bin/sh


#
#Please Set below variables
#
versionMinor=07
versionMajor=1
ENV=QALP3LBSPC

regressionStartTime=`date +"%F::%H-%M-%S"`
folderToExportResultsTo='/opt/dece/SoapUIReports/'$versionMinor'/'$regressionStartTime
soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/107/Policy'

cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin


echo starting POLICY regression R107...
echo $regressionStartTime


project=$projectPath'/Policies_Jetty_1-07.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Policy -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project

echo "Finished Running Policies Regression for 1/07 endpoint"
