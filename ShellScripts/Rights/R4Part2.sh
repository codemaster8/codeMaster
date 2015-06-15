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
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/107/Rights'

cd /opt/dece/applications/SmartBear/SoapUI-Pro-4.6.1/bin


echo starting Rights regression R07...
echo $regressionStartTime


project=$projectPath'/RightsLockerDataGet_By_metadata_withoutSAML-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE TENTH PROJECT IS DONE"
currentTime=`date`
echo "R4 RightsLockerDataGet_By_metadata_withoutSAML Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLockerDataGet_By_reference_withoutSAML-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE ELEVENTH PROJECT IS DONE"
currentTime=`date`
echo "R4 RightsLockerDataGet_By_reference_withoutSAML Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLockerDataGet_By_default_withoutSAML-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE TWELTH PROJECT IS DONE"
currentTime=`date`
echo "R4 RightsLockerDataGet_By_default_withoutSAML Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsLockerDataGet_By_download_withoutSAML-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE THIRTEENTH PROJECT IS DONE"
currentTime=`date`
echo "R4 RightsLockerDataGet_By_download_withoutSAML Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsTokenDelete-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE FOURTEENTH PROJECT IS DONE"
currentTime=`date`
echo "R4 RTDelete Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsTokenGet-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE FIFTEENTH PROJECT IS DONE"
currentTime=`date`
echo "R4 RTGet Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsTokenGet_withoutSAML-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE SIXTEENTH PROJECT IS DONE"
currentTime=`date`
echo "R4 RTGetWithoutSAML Completed at $currentTime" >> $timeStampsFile
sleep 20

project=$projectPath'/RightsTokenUpdate-soapui-project.xml'
./testrunner.sh -r -j -f$folderToExportResultsTo -EDefault -I  -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/Rights -t$soapuiSettingsFile -PENV=$ENV -PversionMinor=$versionMinor -PversionMajor=$versionMajor -i $project
echo "Execution OF THE SEVENTEENTH PROJECT IS DONE"
currentTime=`date`
echo "R4 RTUpdate Completed at $currentTime" >> $timeStampsFile
sleep 20


currentTime=`date +"%F:%H-%M-%S"`
echo "Finished Running R4 Rights part 2 Regression for 1/07 endpoint" >> $timeStampsFile


