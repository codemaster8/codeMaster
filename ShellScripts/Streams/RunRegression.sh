#!/bin/bash



cd /opt/dece/GitRepo/uv-coordinator-api-regression
git clean -f -d
git reset --hard
git pull origin master


gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/107_batch_1.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/107_batch_2.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/111_batch_1.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/111_batch_2.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/201502_batch_1.sh
gnome-terminal -x /home/QAUV/Desktop/Regression_BatchFiles/201502_batch_2.sh
