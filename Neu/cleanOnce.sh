#!/bin/sh


echo "Trimming InstiMonitor Logs"
echo "Trimmed at $(date)" > nohup.out  

echo "Trimming Soap Runner Logs"
echo "Trimmed at $(date)" >soapRunner/nohup.out  
echo "Cleaning Logs"

rm -rRf *.log *.out
rm -rf soapRunner/*.log

echo  "cleaning Completed !!! "
