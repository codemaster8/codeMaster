import csv
import re
import subprocess

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

with open('/opt/RND/CSV/executeonvms.csv') as f:
    clusterName = dict(filter(None, csv.reader(f)))
clusterName = SortedDisplayDict(clusterName) 
print "Cluster Names ===> {0}".format(clusterName);
dictionary = {}
for key in sorted(clusterName):
	machineName = clusterName[key]
	command = key 
	print machineName

        cluster_value = subprocess.Popen('sshpass -p "qauv13" ssh -o StrictHostKeyChecking=no -t QAUV@{1} {0}'.format(machineName, command), stdout=subprocess.PIPE, stderr=None, shell=True)

	output = cluster_value.communicate()
	print output[0]
