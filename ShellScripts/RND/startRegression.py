import csv
import subprocess

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

with open('/opt/RND/CSV/QA_Box.csv') as f:
    clusterName = dict(filter(None, csv.reader(f)))
clusterName = SortedDisplayDict(clusterName) 
print clusterName
dictionary_1 = {}
for key in sorted(clusterName):
        machineName = clusterName[key]
        cluster_value_1 = subprocess.Popen('sshpass -p "qauv13" ssh -o StrictHostKeyChecking=no -t QAUV@{0} screen -dm -S sh /home/QAUV/Desktop/Regression_BatchFiles/RunRegressionAfter_GIT_PULL.sh'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)
        output_1 = cluster_value_1.communicate()
	dictionary_1[key] = output_1[0]
	print dictionary_1[key]
		
