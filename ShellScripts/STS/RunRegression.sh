#!/bin/bash



cd /opt/dece/GitRepo/uv-coordinator-api-regression
git clean -f -d
git reset --hard
git pull origin master


#gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/R1-07_STS.sh 
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/R1-11_STS.sh 
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/R2015-02_STS.sh 
