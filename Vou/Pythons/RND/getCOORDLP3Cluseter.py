import csv
import subprocess

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

with open('/opt/RND/CSV/coord.csv') as f:
    clusterName = dict(filter(None, csv.reader(f)))
clusterName = SortedDisplayDict(clusterName) 
print clusterName
dictionary = {}
for key in sorted(clusterName):
	try :
        	machineName = clusterName[key]
        	cluster_value = subprocess.Popen('sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@{0} grep clu /opt/jetty_coordinator/jetty/conf/dece.properties'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)

       		output = cluster_value.communicate()
        	if (output[0].find("clu1")== -1):
			dictionary[key] = "Cluster 2"
		else:
			dictionary[key] = "Cluster 1"
	except IOError:
		print "****************Error while doing SSH over hostnames *********" 		
			
f = open('/var/www/html/getLP3Details.html','w')

message = """<!DOCTYPE html>
<html>
<head>
</head>
<body>
<table border=5 style="width:75%" CELLPADDING="4" CELLSPACING="3">
  <tr style="background-color:Aqua ;">
        <th>Machine Name</th>
	<th>Environment Name</th>
        <th>DB Details </th>
  </tr>
"""

for key in sorted(dictionary):
	message+= "<tr><td>{0}</td><td>{1}</td><td>{2}</td></tr>".format( clusterName[key], key, dictionary[key])	
print message

f.write(message)
f.close()

