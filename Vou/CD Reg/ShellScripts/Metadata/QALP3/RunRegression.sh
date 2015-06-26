#!/bin/bash

cd /opt/dece/GitRepo/uv-coordinator-api-regression
git clean -f -d
git reset --hard
git pull origin master


gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/QALP3/107_Asset.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/QALP3/107_Basic.sh

gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/QALP3/111_Asset.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/QALP3/111_Basic.sh

gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/QALP3/201502_Asset.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/QALP3/201502_Basic.sh