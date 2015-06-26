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
	        		#cluster_value = subprocess.Popen("sshpass -p 'Kn0od!e' ssh -o StrictHostKeyChecking=no -t  deceapp@{0} grep -n {1} /opt/jetty_coordinator/jetty/logs/dece*".format(machineName,grepString), stdout=subprocess.PIPE, stderr=None, shell=True)
				cmd="sshpass -p 'Kn0od!e' ssh -o StrictHostKeyChecking=no -t  deceapp@{0} grep -n {1} /opt/jetty_coordinator/jetty/logs/dece*".format(machineName,grepString)
				sout, sin, serr=os.popen3(cmd)
				output=sin.readlines()
				key += " ---- {0} ".format(machineName) 
				if len(output)<10:
					dictionary[key] = "Logs Not found"
				else:
        				dictionary[key] = output
				#print output[0]
				
			except:
				dictionary[key] = "Unknown Exception Occurred"
	return dictionary

def getMachineName(instanceID):
	try:
		instanceID = str(instanceID)
		cmd="sshpass -p 'Testing@28' ssh -o StrictHostKeyChecking=no -t okhatav@stdecdvvap5.va.neustar.com 'echo Testing@28 | sudo -S sh test.sh {0}'".format(instanceID)
                print 'After Command '
		sout, sin, serr=os.popen3(cmd)
                output=sin.readlines()
		print output
	except:
		output = "Unknown Error at {0}".format(instanceID)

	return str(output)

def restartENV(terminalENV, ComponentName):
	try:
		terminalENV = str(terminalENV)
		ComponentName = str(ComponentName)
		with open('/opt/RND/CSV/coord.csv') as f:
                	clusterName = dict(filter(None, csv.reader(f)))
	        clusterName = SortedDisplayDict(clusterName)
		dictionary = {}
	        for key in sorted(clusterName):
                	machineName = clusterName[key].lower()
                	if key.find(terminalENV)!=-1:
                        	try:
                               	#cluster_value = subprocess.Popen("sshpass -p 'Kn0od!e' ssh -o StrictHostKeyChecking=no -t  deceapp@{0} grep -n {1} /opt/jetty_coordinator/jetty/logs/dece*".format(machineName,grepString), stdout=subprocess.PIPE, stderr=None, shell=True)
                                	cmd="sshpass -p 'Kn0od!e' ssh -o StrictHostKeyChecking=no -t  deceapp@{0} grep -n {1} /opt/jetty_coordinator/jetty/logs/dece*".format(machineName,ComponentName)
                                sout, sin, serr=os.popen3(cmd)
                                output=sin.readlines()
                                key += " ---- {0} ".format(machineName)
                                if len(output)<10:
                                        dictionary[key] = "Logs Not found"
                                else:
                                        dictionary[key] = output
                                #print output[0]

                        except:
                                dictionary[key] = "Unknown Exception Occurred"
        return dictionary









