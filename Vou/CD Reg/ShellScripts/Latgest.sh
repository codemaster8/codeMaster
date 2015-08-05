#/bin/bash
#Make Sure to modify the below 2 lines
printf "\033]0;EXECUTING USR REGRESSION on 2015/02 \007"
projectPath='/opt/dece/GitRepo/uv-coordinator-api-regression/Projects/201502/AccountUsers'

ENV=$1
Appender=$2
folderToExportResultsTo=$3

versionMajor=2015
versionMinor=02

soapuiSettingsFile='/home/QAUV/soapui-settings.xml'
timeStampsFile=$folderToExportResultsTo'/REGRESSION_TIMESTAMPS.log'
cd /opt/dece/SmartBear/SoapUI-Pro-4.6.1/bin

#########################################################################
fileName=R15_UserCreate_Retailer_Portal
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG USERS EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_UserDelete_Portal
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG USERS EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_UserDelete_Retailer
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG USERS EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_UserDelete_Retailer_Basic
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG USERS EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_UserGetList
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG USERS EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_UserGet_Portal
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG USERS EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_UserGet_Retailer
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG USERS EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_UserUpdate
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG USERS EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
#ACCOUNT PROJECTS
#########################################################################
fileName=R15_AccountCreate
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_AccountDelete
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_AccountMergeUndoTest
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_AccountUpdate
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_AccountUserCreate
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_AccountUserPolicyCreate
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_Account_Get
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_Backward_Compatibility
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_Cache_Account_User
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################
fileName=R15_Resource_Property_Query
echo $fileName" Project Started at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
project=$projectPath'/'$fileName'.xml'
printf "\033]0;REG ACC EXECUTING $fileName \007"
./testrunner.sh -r -j -f$folderToExportResultsTo'/'$fileName  -EDefault -I -Dsoapui.scripting.library=/opt/dece/GitRepo/uv-coordinator-api-regression/GlobalScripts/AccountUsers -PversionMajor=$versionMajor -PversionMinor=$versionMinor  -PENV=$ENV -t$soapuiSettingsFile -I  $project | tee -a $folderToExportResultsTo'/'$fileName.log
echo $fileName" Project Completed at "`date +"%F::%H-%M-%S"`>> $timeStampsFile
#########################################################################


