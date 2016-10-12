#!/bin/sh


while true 
do
echo "y Runner Execution started at : " $(date)
/home/scripts/7getCat.sh
/home/scripts/7getClu.sh
/home/scripts/7getSum.sh
done
