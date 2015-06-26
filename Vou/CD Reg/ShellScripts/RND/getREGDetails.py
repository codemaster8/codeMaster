import csv
import re
import subprocess

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

with open('/opt/RND/CSV/regress.csv') as f:
    clusterName = dict(filter(None, csv.reader(f)))
clusterName = SortedDisplayDict(clusterName) 
print clusterName
dictionary = {}
for key in sorted(clusterName):
        machineName = clusterName[key]
        #cluster_value = subprocess.Popen('sshpass -p "qauv13" ssh -o StrictHostKeyChecking=no -t QAUV@{0} ps -ef | grep soap -1'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)

        cluster_value = subprocess.Popen('sshpass -p "qauv13" ssh -o StrictHostKeyChecking=no -t QAUV@{0} ps -ef| grep "testrunner" | grep -v grep'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)

	output = cluster_value.communicate()
        if not output[0]:
		dictionary[key]= "Regression Not Running"
	else:
		dictionary[key] = output[0].replace("./testrunner.sh","<b>./testrunner.sh</b>").replace("QAUV","<br><hr>QAUV")
		dictionary[key] = " ".join((dictionary[key],"<hr><br><br>")) 
	print dictionary[key]

f = open('/var/www/html/getREGDetails.html','w')

message = """<!DOCTYPE html>
<html>
<head>
<meta http-equiv="refresh" content="30">
</head>
<body>
<button onclick="onTimer()"><b>Update ME&nbsp;</b></button></b></h3>
<div id="mycounter"><b><script>
i = 20
requestText = " <b><br>Please wait for ";
timeText = " sec .....";

function onTimer() {
    if( i == 20  ){
    var a = document.createElement("a");
    a.href = "http://10.31.15.38:8080/job/RUN_SCRIPT/buildWithParameters?pythonFileName=getREGDetails.py";
    var evt = document.createEvent("MouseEvents");
    //the tenth parameter of initMouseEvent sets ctrl key
    evt.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0,
                                true, false, false, false, 0, null);
    a.dispatchEvent(evt);
}


document.getElementById('mycounter').innerHTML = requestText + i + timeText ;
  i--;
  if (i < 0) {

 document.getElementById('mycounter').innerHTML = "<b> Page is updated now Please Refresh Page  ... ";
  }
  else {
    setTimeout(onTimer, 1000);
  }
}
</script>
</b><br>
<table border=5 style="width:80%">
  <tr style="background-color:Aqua ;">
        <th>Machine Name</th>
	<th>Environment Name</th>
        <th>Process Details </th>
  </tr>
"""

for key in sorted(dictionary):
	message+= "<tr><td>{0}</td><td>{1}</td><td>{2}</td></tr>".format( clusterName[key], key, dictionary[key])	
print message

f.write(message)
f.close()

