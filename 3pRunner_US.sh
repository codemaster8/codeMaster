#!/bin/sh


while true 
 do
echo "Execution started at : " $(date)

/home/scripts/1getStatus_US.sh
/home/scripts/2mkProp_US.sh   
/home/scripts/soapRunner/soapRunner.sh
/home/scripts/7getCat.sh
/home/scripts/7getClu.sh
/home/scripts/7getSum.sh

echo "Exectution Completed Sucessfully !!! Waiting for 2 seconds before refreshing"
sleep 2
 done
