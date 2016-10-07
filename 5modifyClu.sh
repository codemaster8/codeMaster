#!/bin/bash



myIp=$1
insti=$2
prop=$3
value=$4
cluFile="/LocalWeb/properties/$insti/clu.properties"
echo "################################################################"
echo "#### Checking Arguements" 
if [ "$#" -eq 4 ]; then
echo "Thanks for entering valid no of params"
else
echo "WARN: Parameter parse Error, Please enter valid #No. of parameters" 
echo "Usage   :./modifyClu.sh <host ip> <institution> <Property> <Value> "
echo "Example :./modifyClu.sh 10.56.31.40 clucasqa6 security false"
echo "################################################################"
fi


echo "Modifying property $prop = $value on $myIp :: $insti"   
echo "UPDATING PROPERTY !!! "

cmd="sed -i 's/$prop=/$prop=$value\\n#/g' $cluFile"



#0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp sed -i "s/$prop=/$prop=$value\\\n#/g" $cluFile
0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp $cmd $cluFile

echo "Verifying property set correctly on host"
echo "Expected Value : "
echo "$3=$4" 
echo "Actual  Value : "

0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp cat $cluFile | grep $3



