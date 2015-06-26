import csv
import subprocess,os
import sys
import string

class SortedDisplayDict(dict):
   def __str__(self):
       return "{" + ", ".join("%r: %r" % (key, self[key]) for key in sorted(self)) + "}"

def getlog(searchString, ENV, AfterLines):
	with open('/opt/RND/CSV/coord.csv') as f:
    		clusterName = dict(filter(None, csv.reader(f)))
	clusterName = SortedDisplayDict(clusterName)
	grepString = searchString
	terminalENV = ENV
	AL = int(float(AfterLines))
	print grepString
	print terminalENV  
	print clusterName
	dictionary = {}
	for key in sorted(clusterName):
        	machineName = clusterName[key].lower()
		if key.find(terminalENV)!=-1:
			try:
	        		cluster_value = subprocess.Popen("sshpass -p 'Vertis@28' ssh -o StrictHostKeyChecking=no -t  okhatav@{0} grep -A {2} -n {1} /opt/jetty_coordinator/coo*/logs/*".format(machineName,grepString,AL), stdout=subprocess.PIPE, stderr=None, shell=True)
				output = cluster_value.communicate()
				
#			cmd="sshpass -p 'Vertis@28' ssh -o StrictHostKeyChecking=no -t  okhatav@{0} grep -R -n {1} /opt/jetty_coordinator/coo*/logs/*".format(machineName,grepString)
				#sout, sin, serr=os.popen3(cmd)
				#output=sin.readlines()
				
				key+= " ---- {0} ".format(machineName) 
				if len(output)<1:
					dictionary[key] = "Logs Not found"
				else:
        				dictionary[key] = output
				print output
				
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

def getCSVTOHTMLConverter():
	with open( '/opt/RND/CSV/impurl.csv', 'rb') as csvfile:
    		table_string = ""
   		reader = csv.reader( csvfile )
        	for row in reader:
        		row[1]="<a href='{0}' target='_blank'>{1}</a>".format(row[1],row[1]);
			table_string += "<tr>" + \
                        	  "<td>" + \
                             	 string.join( row, "</td><td>" ) + \
                         	 "</td>" + \
                        	"</tr>\n"
    
    			sys.stdout.write( table_string )
	return str(table_string)

def getErrorCount(error_no,machineName):
	try:
		cmd="sshpass -p 'Vertis@28' ssh -o StrictHostKeyChecking=no -t okhatav@{0} grep --color 'urn:dece:org:org:dece:[a-Z]\*[[:space:]]{1}[[:space:]].*' /opt/dece_apache/logs/*".format(machineName,error_no)
		print 'After Command'
		print cmd
                sout, sin, serr=os.popen3(cmd)
                output=sin.readlines()
                print output
	except:
                output = "Unknown Error at {0}".format(machineName)

        return str(output)

	
