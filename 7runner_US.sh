#!/bin/sh


while true 
 do
echo "Execution started at : " $(date)
./7getClu.sh
./7getCat.sh
./7getSum.sh
echo "Exectution Completed Sucessfully !!! Waiting for 10 seconds before refreshing"
sleep 10
 done
