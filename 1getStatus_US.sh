#!/bin/sh
enginePropPath=/var/www/html/engine.properties
myHtmlFile=/var/www/html/institutionMonitor.html
myFile=tempPro.html
est=$(date)
TZ='Asia/Kolkata'
export TZ
myNextExec=$(date --date='130 seconds')
echo "<html>
 <head>
 <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js">
 </script>
 <script>
 \$(document).ready(function(){
 setInterval(function(){cache_clear()},8000);
 });
 function cache_clear()
{
 window.location.reload(true);
}
</script>
</head>
<body style=font-family:Courier> <font size =2  color =#ff8000>">$myFile 
echo "<style>
table, th , td  {
  border: 1px solid grey;
  border-collapse: collapse;
  padding: 5px;
}
table tr:nth-child(odd)	{
  background-color: #f1f1f1;
}
table tr:nth-child(even) {
  background-color: #ffffff;
}
</style>">>$myFile
echo "<iframe src='http://free.timeanddate.com/clock/i4ywmlff/n1038/fn8/tcccc/ftb/bo2/tt0/tw1/tm1/th1/ta1' frameborder='2' width='310' height='21'></iframe>">>$myFile

echo "<br>< < Last UPDATED : $(date)">>$myFile
TZ='Asia/Kolkata'
export TZ
echo  "    ||    $est) >  >                  GET HELP >>">>$myFile
echo "<img title='Cick to Read Help !!' src=help.png onclick=location.href='props/Help.html' style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer'>" |tee -a $myFile
javascript:location.href='http://www.uol.com.br/'

percentage=100
#$(df -ha /data/homes/|grep homes|awk '{print $4}'|grep %)
val=$(echo $percentage |sed 's/%//')
percent=$(($val / 10))
echo "<br><b>< Disk usage :</b> <progress value="$percent" max="100"></progress> Free Space $percentage">>$myFile
echo "<table border=2 bgcolor=#d9d9d9 style=font-size:13> <tbody>">>$myFile

echo "<b><tr><td><b>Institution</td><td><b>Wars Installed</td>><td><b>Tomcat Status</td></tr> <tr>">>$myFile

while read line;
do
insti=$(echo $line|cut -d',' -f1)
myIp=$(echo $line|cut -d',' -f2)
assignee="<b> "$(echo $line|cut -d',' -f3)
engineVersion=$(cat $enginePropPath|grep $insti|cut -d',' -f2)

instId=$(echo $line|cut -d',' -f4)
purpose=$(echo $line|cut -d',' -f5)


echo "**************************>>>> " $engineVersion
echo "<td><b>$insti</b>-$assignee-$instId-<br>$myIp<br>$engineVersion</td>">>$myFile
echo "<td>">>$myFile
############################# LOGGING 

echo "Executing on INSITUTION : $insti   $myIp  $instId"  
# GETTING WARS INSTALLED
0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp cat  /LocalWeb/tomcat/instances/$insti/conf/server.xml| sed '/<!--.*-->/d' | sed '/<!--/,/-->/d'  |grep 'Context path'|cut -d'"' -f4|tee -a $myFile
wars=$(tail -3 $myFile|grep -v common)
echo "######################" $insti
# GETTING TOMCAT STATUS
echo "</td><td>" >>$myFile

if [ -z "$instId"  ]
then 
	echo "--"|tee -a $myFile 
else
	0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp ps -efa|grep $insti/tomcat|grep -v grep|wc -l|tee -a $myFile #|awk '{print $2}'|tee -a $myFile
	myCmdForToolTip="grep -i 'ConfidenceLevel\|activation\|output\|workFlow\|license\|capd\|Engine\|Url\|nmsServerURL' /LocalWeb/properties/$insti/clu.properties|grep -v '#'"
     	tooltip=$( 0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp $myCmdForToolTip)
########## LINK PROPERTY EDITOR

# CHECKING IF SERVER IS RUNNING
ud=$(tail -1 $myFile)
#echo "##################!!!!!!!!!!!!!!!$ud"

url=http://clu.nuancehce.com/$insti/clu/sdk/session/clu-session.wsdl
content=$(curl -i --max-time 2 $url | grep HTTP/1.1 | tail -1|awk '{print $2}' )
#echo "wsdl=$content" >>$myFile
if [ ! -z $content ] && [ $content -eq 200 ]
then
 echo "<img title='WSDL is PUBLISHED STATUS=$content OK' src=GreenW.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=location.href='$url'>" |tee -a $myFile
else
 echo "<img title='WSDL is NOT PUBLISHED STATUS=$content NOT OK' src=RedW.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=location.href='$url'>" |tee -a $myFile
fi
 echo "<img title='Get latest Clu Doc for $insti' src=docTable.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=location.href='DbDetails/$insti.html'>" |tee -a $myFile
 echo "<img title='Get latest Patient Visit for $insti' src=patientTable.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=location.href='DbDetails/$insti\_pv.html'>" |tee -a $myFile
#PUTTING LINK IN THE IMAGE
if [ $ud -eq 1 ]; then
         echo "<img title='Cick to Restart Tomcat on  institution $myIp : $insti ... !!!' src=pass.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=callJob('Commander','4restartTomcat.sh','$insti','$myIp')>" |tee -a $myFile
    else
         echo "<img title='Click to Restart Tomcat on institution $myIp : $insti... !!!' src=fail.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=callJob('Commander','4restartTomcat.sh','$insti','$myIp')>" |tee -a $myFile
fi

#ADDING CONSOLE OUTPUTS
    echo "<img title='Cick to view catalina.out file [last 1000 lines]'  src=catalina.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=location.href='logs/catalina/$insti.html'>"|tee -a $myFile
    echo "<img title='Cick to view clu-cas-summary.log file [last 1000 lines]'  src=summary.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=location.href='logs/summary/$insti.html'>"|tee -a $myFile
    echo "<img title='Cick to view clu-cas.log file [last 1000 lines]'  src=clucas.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=location.href='logs/clucas/$insti.html'>"|tee -a $myFile
    echo "<img title='Cick to Modify properties&#13 Listing Imp Properties &#13 $tooltip'  src=props.png style=width:25px;height:25px; onMouseOver=this.style.cursor='pointer' onclick=location.href='props/$insti.html'>"|tee -a $myFile
fi
echo "</td></tr>" >>$myFile

done <myInstitution.csv
echo "</tbody></font></table>" >>$myFile

######################## ON BUTTON CLICK FUNCTION 
echo "<script>" >>$myFile
echo "function callJob(job,shellName,inst, ip) {">>$myFile
echo "message=' Restart Institution : ' +inst;">>$myFile
echo "if(job == 'RefreshInstiList'){">>$myFile
echo "message = ' Refresh  Data';}">>$myFile
echo "if (confirm('Are you sure you want to ' +message ) == true) {">>$myFile  
echo "wnd = window.open('http://pune-srv-ubut01.nuance.com:9090/job/'+job+'/buildWithParameters?delay=0sec&shellFn='+shellName+'&insti='+inst+'&instIp='+ip,'_blank');">>$myFile
echo "alert('DATA Will be updated in less than 20 secs..!! Please wait the page will auto refresh every 15 secs!!!');">>$myFile
echo "wnd.close();location.reload();callRefresh.preventDefault();">>$myFile
echo "}};">>$myFile

echo "

function popupModifyProps(insti)
{alert(insti)
}
</script>">>$myFile
echo $myStr>>$myFile
echo "Color legend : cac-server, capd-server, muse-server <br><br><a href=mailto:utkarsh.sinha@nuance.com>Contact / Suggest changes /New Feature request</a>">>$myFile 

echo "</body></html>">>$myFile
echo "FINALIZING FILE #################### !!! $myHtmlFile"
#sed 's/war/war<br>/g' $myFile >$myHtmlFile
#sed -e 's/war/war<br>/g' $myFile >tempeUs
 
 sed  -e 's/war/war<br>/g' -e 's/clu360-server/<mark style=background-color:#ffc299 >clu360-server<\/mark>/g' -e 's/cac-server/<mark style=background-color:#9999ff >cac-server<\/mark>/g' -e 's/capd-server/<mark style=background-color:#ffffcc>capd-server<\/mark>/g' -e 's/muse-server/<mark style=background-color:#ccffcc>muse-server<\/mark>/g' $myFile >$myHtmlFile

rm -rf $myFile
# note :http://pune-srv-ubut01.nuance.com:9090/job/testParam/buildWithParameters?delay=0sec&insti=clucasqa3&instIp=pune-srv-ubut01.nuance.com



