#!/bin/sh

myFile=tempProps
est=$(date)
TZ='Asia/Kolkata'
export TZ
myNextExec=$(date --date='130 seconds')


while read line;
do
insti=$(echo $line|cut -d',' -f1)
myIp=$(echo $line|cut -d',' -f2)
myHtmlFile="/var/www/html/props/"$insti".html"
instId=$(echo $line|cut -d',' -f4)
propFile=/LocalWeb/properties/$insti/clu.properties


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
</head>
<body style=font-family:Courier> <font size =2  color =#000000>">$myFile
echo "<a><font color =red> [CAUTION: For special characters please update as per examples given below by adding a \  backslash before all forward slashes /,  stars * and all spaces and enclosing it in quotes]<br>
<br>Example: purgeSchedule prop value should be \"0\ 0\ 0\ \*\ \*\ \?\" note: space should be escaped too<br>
Similarly for Urls escape all the forward slashes \"https:\/\/nms-qa2.nuancehce.com\/NMS\/Platform\/\"
</a><br>">>$myFile 
echo "<table border=2 bgcolor=#fffff style=font-size:13> <tbody>">>$myFile
echo "Executing on INSITUTION : $insti   $myIp  $instId"  
echo "<tr><td><b>Property</td>><td><b>Value</td></tr>">>$myFile
0</dev/null sshpass -p "D3wuN0?" ssh -o StrictHostKeyChecking=no -t -T servlet@$myIp cat $propFile | grep -v "#"|egrep -v '^[[:space:]]*$'|sort |while read line;
do
 prop=$(echo  $line|cut -d'=' -f1|tr -d '[[:space:]]')
 value=$(echo $line|cut -d'=' -f2|tr -d '[[:space:]]')
 if [  -z "$prop" ]
 then
  echo "Empty Property Found:##### "
 else
  echo "<tr><td>$prop</td><td><input id=$prop value=$value><input type=button value=Update onclick=modifyProp('$myIp','$insti','$prop','$value')> </td></tr>" >>$myFile
 fi

done
echo "</tbody></font></table>
<script>
function modifyProp(myIp,insti,prop,value) {
message=' Update property on  ' +insti +' '+ prop + ' == to=> ' +document.getElementById(prop).value ;
if (confirm('Are you sure you want to ' +message ) == true) {
wnd = window.open('http://pune-srv-ubut01.nuance.com:9090/job/Commander/buildWithParameters?delay=0sec&shellFn=5modifyClu.sh'+'&insti='+insti+'&instIp='+myIp+'&prop='+prop+'&value='+document.getElementById(prop).value ,'_blank');
alert('Property Updated !!!');
wnd.close();location.reload();callRefresh.preventDefault();
}};
</script>
</body></html>" >>$myFile
cat  $myFile>$myHtmlFile
echo "Finalized File $myHtmlFile"
fi
done </home/scripts/myInstitution.csv
