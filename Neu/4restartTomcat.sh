
#!/bin/sh
myIp=$1
insti=$2
echo "myIp= $myIp"
echo "insti= $insti"

cmd0="chmod +x /LocalWeb/tomcat/instances/$insti/bin/*.sh;"

cmd1="/LocalWeb/tomcat/instances/$insti/bin/catalina.sh stop -force;" 
cmd2="/LocalWeb/tomcat/instances/$insti/bin/catalina.sh start;"


0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp $cmd0


0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp $cmd1

0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp $cmd2
echo "ReStArT Completed !!!"



#ps -ef | grep $insti | grep -v grep | awk '{print $2}' | xargs kill -9 ;
#kill -9 $(cat /LocalWeb/tomcat/instances/$insti/bin/catalina.pid);
