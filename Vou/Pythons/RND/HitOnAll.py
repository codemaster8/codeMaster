import csv
import re
import subprocess
import sys 

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

with open('/opt/RND/CSV/hitOnAll.csv') as f:
    clusterName = dict(filter(None, csv.reader(f)))
clusterName = SortedDisplayDict(clusterName) 
command = str(sys.argv[1])
for key in sorted(clusterName):
	machineName = key
	print machineName

        cluster_value = subprocess.Popen('sshpass -p "qauv13" ssh -o StrictHostKeyChecking=no -t root@{0} {1}'.format(machineName, command), stdout=subprocess.PIPE, stderr=None, shell=True)

	output = cluster_value.communicate()
	print output[0]
