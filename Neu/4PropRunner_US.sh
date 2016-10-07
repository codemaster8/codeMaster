#!/bin/sh


while true 
 do
echo "Execution started at : " $(date)

/home/scripts/2mkProp_US.sh   
echo "Exectution Completed Sucessfully !!! Waiting for 10 seconds before refreshing"
sleep 10
 done
