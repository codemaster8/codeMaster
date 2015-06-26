#!/bin/sh

#Checkout Latest Code From Git
#
cd /opt/dece/GitRepo/uv-coordinator-api-regression
git clean -f -d
git reset --hard
git pull origin master

#To run Regression in LP3 comment last 2 line and to run regression on LP1 comment first two lines

#gnome-terminal -x /home/QAUV/Desktop/Regression_Batchfiles/home/R4Part1.sh
#gnome-terminal -x /home/QAUV/Desktop/Regression_Batchfiles/R4Part2.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_Batchfiles/R6Part1.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_Batchfiles/R6Part2.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_Batchfiles/R201502Part1.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_Batchfiles/R201502Part2.sh
