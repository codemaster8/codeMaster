import csv
import subprocess

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

with open('/opt/RND/CSV/domain.csv') as f:
    clusterName = dict(filter(None, csv.reader(f)))
clusterName = SortedDisplayDict(clusterName) 
print clusterName
dictionary = {}
for key in sorted(clusterName):
        machineName = clusterName[key]
        cluster_value = subprocess.Popen('sshpass -p "Testing@28" ssh -o StrictHostKeyChecking=no -t okhatav@{0} grep jdbc:oracle /opt/jetty_domain_manager/jetty/conf/decedm.properties'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)
        output = cluster_value.communicate()
        dictionary[key] = output[0]

f = open('/var/www/html/getDOMAINDBDetails.html','w')

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

