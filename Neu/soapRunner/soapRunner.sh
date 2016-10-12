#!/bin/sh


while true
 do
echo "Execution started at : " $(date)
/opt/SoapUI-5.0.0/bin/testrunner.sh -stestS -c"Get_DBDetails" /home/scripts/soapRunner/getDbDetails.xml
/opt/SoapUI-5.0.0/bin/testrunner.sh -stestS -c"GetEngines" /home/scripts/soapRunner/getEngines.xml

sleep 1 
 done
echo "Execution ends @@@@@@@ "
