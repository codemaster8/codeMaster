#!/bin/bash

cd /opt/dece/GitRepo/uv-coordinator-api-regression
git clean -f -d
git reset --hard
git pull origin master


#gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/R107_PolicyRegression.sh
#gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/R201502_PolicyRegression.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/R111_PolicyRegression.sh
