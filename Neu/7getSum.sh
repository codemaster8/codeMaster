#!/bin/sh

fileToMonitor=clu-cas-summary.log
myFile=temp_$fileToMonitor
est=$(date)
TZ='Asia/Kolkata'
export TZ
myNextExec=$(date --date='130 seconds')


while read line;
do
insti=$(echo $line|cut -d',' -f1)
myIp=$(echo $line|cut -d',' -f2)
myHtmlFile="/var/www/html/logs/summary/"$insti".html"

instId=$(echo $line|cut -d',' -f4)


if [  -z "$instId" ]
then
echo "Empty Institution Not Executing"
else

echo "">$myFile
echo "<html> <head>
 <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js">
 </script>
 <script>
 \$(document).ready(function(){
 setInterval(function(){cache_clear()},15000);
 });
 function cache_clear()
{
 window.location.reload(true);
}
</script>
<script type=text/javascript>
onload = function()
{
    document.getElementById( 'bottom' ).scrollIntoView();
    window.setTimeout( function () { top(); }, 2000 );
};
</script>
</head>
<body bgcolor=FFFFCC text=Lime style=font-family:Courier>">>$myFile

echo "<table align=center style=font-size:11 font-family:Lucida Console bgcolor=black border=20 width=890> <tbody>">>$myFile
echo "Executing on INSITUTION : $insti   $myIp  $instId"  
echo "<tr><td> <b> <font size=3 color=cream>Catalina.out on $insti</font>  </b> </td></tr><tr><td id=toCopy>">>$myFile
emptyLog=$(0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp tail -1 /LocalWeb/tomcat/instances/$insti/logs/$fileToMonitor)
if [ -z  "$emptyLog" ]
then
echo "<font size=3 color=red>No Logs Found !!!</font>">>$myFile
else
	0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp tail -1000  /LocalWeb/tomcat/instances/$insti/logs/$fileToMonitor|sed 's/$/<br>/'| sed -e 's/error/<mark style=background-color:#ff0000>error<\/mark>/g' | sed -e 's/ERROR/<mark style=background-color:#ff0000>ERROR<\/mark>/g'| sed -e 's/WARN/<mark>WARN<\/mark>/g'| sed -e 's/Warning/<mark>Warning<\/mark>/g'| sed -e 's/warn/<mark>warn<\/mark>/g'|sed -e 's/EXCEPTION/<mark style=background-color:#ffd24d>EXCEPTION<\/mark>/g'|sed -e 's/Exception/<mark style=background-color:#ffd24d>Exception<\/mark>/g'|tee -a $myFile
fi
echo "</td></tr><tr><td>
<button class=button id=copy-button data-clipboard-target=#toCopy onclick=run()>Copy</button>
</td></tr></tbody></font></table>">>$myFile
echo "<script src=//cdnjs.cloudflare.com/ajax/libs/clipboard.js/1.4.0/clipboard.min.js></script>
<script>(function(){new Clipboard('#copy-button');})();</script>
</body><div id=bottom>.</div></html>" >>$myFile
cat  $myFile>$myHtmlFile
echo "Finalized File $myHtmlFile"
cat $myHtmlFile
fi
done </home/scripts/myInstitution.csv
