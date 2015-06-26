#/bin/bash

#export SUDO_ASKPASS=askpass

cmd=$@
echo $cmd
echo "--------------------------"

echo "On stdecqavap3"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@stdecqavap3 $cmd

echo "-----------------------------"

echo "On stdecqavap4"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@stdecqavap4 $cmd

echo "-----------------------------"

echo "On stdecqavap7"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@stdecqavap7 $cmd

echo "-----------------------------"

echo "On stdecqavap8"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@stdecqavap8 $cmd

echo "-----------------------------"

echo "On stdecqavap11"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@stdecqavap11 $cmd

echo "-----------------------------"

echo "On stdecqavap12"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@stdecqavap12 $cmd

echo "-----------------------------"

echo "On chdecqavap1"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@chdecqavap1.nc.neustar.com $cmd

echo "-----------------------------"

echo "On chdecqavap2"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@chdecqavap2.nc.neustar.com $cmd

echo "-----------------------------"

echo "On chdecqavap3"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@chdecqavap3.nc.neustar.com $cmd

echo "-----------------------------"

echo "On chdecqavap4"
sshpass -p 'Neustar@28' ssh -o StrictHostKeyChecking=no -t okhatav@chdecqavap4.nc.neustar.com $cmd

echo "-----------------------------"


