./cleanOnce.sh
ps -ef |grep US|grep -v grep|cut -d' ' -f3|xargs kill -9 $1
ps -ef |grep soap|grep -v grep|cut -d' ' -f3|xargs kill -9 $1

pkill US
pkill soap
