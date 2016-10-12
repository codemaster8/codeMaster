#!/bin/sh


while true 
do
echo "xRunner Execution started at : " $(date)
/home/scripts/1getStatus_US.sh
/home/scripts/2mkProp_US.sh   
done
