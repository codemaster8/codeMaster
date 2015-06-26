import csv
import subprocess
import sys

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

with open('/opt/RND/CSV/coord.csv') as f:
	clusterName = dict(filter(None, csv.reader(f)))
clusterName = SortedDisplayDict(clusterName)
grepString = "VCPs0AofmIgAABVwTjMAAAAY"
terminalENV = "QALP1"
print grepString
print terminalENV  
print clusterName
dictionary = {}
for key in sorted(clusterName):
       	machineName = clusterName[key].lower()
	if terminalENV in key.lower():
        	cluster_value = subprocess.Popen('sshpass -p "Kn0od!e" ssh -o StrictHostKeyChecking=no -t deceapp@{0} grep -n {1} /opt/jetty_coordinator/jetty/logs/* '.format(machineName,grepString), stdout=subprocess.PIPE, stderr=None, shell=True)
	        output = cluster_value.communicate()
        	dictionary[key] = output[0]
		print output[0]
		
for key in sorted(dictionary):
	print key


