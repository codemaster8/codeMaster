import csv
import subprocess

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

with open('/opt/RND/CSV/coord.csv') as f:
    clusterName = dict(filter(None, csv.reader(f)))
clusterName = SortedDisplayDict(clusterName) 
print clusterName
dictionary_1 = {}
dictionary_2 = {}
for key in sorted(clusterName):
        machineName = clusterName[key]
	print key 
	if 'QALP1' in key:
		 cluster_value_1 = subprocess.Popen('sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@{0} unzip -q -c /opt/jetty_coordinator/coordinator*/lib/coordinator-*.jar META-INF/MANIFEST.MF | grep Implementation-Version:'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)
        else :
		cluster_value_1 = subprocess.Popen('sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@{0} unzip -c /opt/jetty_coordinator/jetty/wars/dece-1.11.war  META-INF/MANIFEST.MF | grep Implementation-Version:'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)
	if 'QALP1' in key:
		cluster_value_2 = subprocess.Popen('sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@{0} unzip -q -c /opt/jetty_coordinator/coordinator*/lib/coordinator-*.jar META-INF/MANIFEST.MF | grep Implementation-Version:'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)
        else:
		cluster_value_2 = subprocess.Popen('sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@{0} unzip -c /opt/jetty_coordinator/jetty/wars/dece-2015.02.war META-INF/MANIFEST.MF | grep Implementation-Version:'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)
        output_1 = cluster_value_1.communicate()
        output_2 = cluster_value_2.communicate()
	dictionary_1[key] = output_1[0]
	dictionary_2[key] = output_2[0]
		
f = open('/var/www/html/getCOORDVersion.html','w')

message = """<!DOCTYPE html>
<html>
<head>
</head>
<body>
<table border=5 style="width:75%" CELLPADDING="4" CELLSPACING="3">
  <tr style="background-color:Aqua ;">
        <th>Machine Name</th>
	<th>Environment Name</th>
        <th>Current Version (1/11)</th>
	<th>Current Version (2015/02)</th>
  </tr>
"""

for key in sorted(dictionary_1):
	if (dictionary_1[key]==dictionary_2[key]):
		message+= "<tr style='background-color:LightGreen'><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td></tr>".format( clusterName[key], key, dictionary_1[key],dictionary_2[key])
	else:
		 message+= "<tr style='background-color:Red' ><td>{0}</td><td>{1}</td><td>{2}</td><td>{3}</td></tr>".format( clusterName[key], key, dictionary_1[key],dictionary_2[key])

print message

f.write(message)
f.close()

