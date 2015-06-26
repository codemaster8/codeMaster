import csv
import subprocess,os
import sys

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

def getlog(searchString, ENV):
	with open('/opt/RND/CSV/coord.csv') as f:
    		clusterName = dict(filter(None, csv.reader(f)))
	clusterName = SortedDisplayDict(clusterName)
	grepString = searchString
	terminalENV = ENV
	print grepString
	print terminalENV  
	print clusterName
	dictionary = {}
	for key in sorted(clusterName):
        	machineName = clusterName[key].lower()
		if key.find(terminalENV)!=-1:
			try:
	        		#cluster_value = subprocess.Popen("sshpass -p 'Vertis@28' ssh -o StrictHostKeyChecking=no -t  okhatav@{0} grep -n {1} /opt/jetty_coordinator/jetty/logs/dece*".format(machineName,grepString), stdout=subprocess.PIPE, stderr=None, shell=True)
				cmd="sshpass -p 'Vertis@28' ssh -o StrictHostKeyChecking=no -t  okhatav@{0} grep -n {1} /opt/jetty_coordinator/jetty/logs/dece*".format(machineName,grepString)
				sout, sin, serr=os.popen3(cmd)
				output=sin.readlines()
	        		#output = cluster_value.communicate()
				if len(output)<10:
					dictionary[key] = "Logs Not found"
				else:
        				dictionary[key] = output
				#print output[0]
				
			except:
				dictionary[key] = "Unknown Exception Occurred"
	return dictionary


