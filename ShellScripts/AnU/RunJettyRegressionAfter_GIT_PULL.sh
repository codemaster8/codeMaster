#/bin/bash

cd /opt/dece/GitRepo/uv-coordinator-api-regression/
git reset --hard
git pull origin master

sleep 30

cd /home/QAUV/Desktop/Regression_BatchFiles


gnome-terminal -x ./R6_Accounts.sh   
gnome-terminal -x ./R6_Users.sh
#gnome-terminal -x ./201502Accounts.sh
#gnome-terminal -x ./201502Users.sh
#gnome-terminal -x ./R4/R4_Accounts.sh
#gnome-terminal -x ./R4/R4_Users.sh



