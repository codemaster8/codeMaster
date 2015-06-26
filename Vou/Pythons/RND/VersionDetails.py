#!/usr/bin/python
import subprocess

clusterName = {"1":"stdecqavot4.va.neustar.com","2":"stdecqavot1.va.neustar.com","3":"stdecqavot2.va.neustar.com","4":"stdecqavot5.va.neustar.com","5":"stdecqavot3.va.neustar.com","6":"stdecqavot6.va.neustar.com","7":"chdecqavot1.nc.neustar.com","8":"chdecqavot1.nc.neustar.com"}
dictionary = {}
for index in range(len(clusterName)):
        index = str(index+1)
        machineName = clusterName[index]
        cluster_value = subprocess.Popen('sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@{0} unzip -c /opt/jetty_shost/jetty/wars/shost.war META-INF/MANIFEST.MF | grep Implementation-Version'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)
        output = cluster_value.communicate()
	dictionary[index] = output[0].split(':')[-1]
	print  dictionary[index] 

for index in range(len(dictionary)):
        index = str(index+1)
        print 'Machine Name :', dictionary[index]

f = open('/var/www/html/GetVersionDetails.html','w')
		
message =  """<!DOCTYPE html>
<html>
<head>
</head>
<body>
<table border=5 style="width:75%" CELLPADDING="4" CELLSPACING="3">
  <tr style="background-color:Aqua ;">
	<th>Machine Name</th>
        <th>Current Version </th>
  </tr>
  <tr>
    <td>stdecqavot4.va.neustar.com (SHOST-QALP1 )</td>
    <td>{0}</td>
  </tr>
  <tr>
    <td>stdecqavot1.va.neustar.com (SHOST-QALP1 )</td>
    <td>{1}</td>
  </tr> 
  <tr>
    <td>stdecqavot2.va.neustar.com (SHOST-QALP2 )</td>
    <td>{2}</td>
  </tr>
  <tr>
    <td>stdecqavot5.va.neustar.com (SHOST-QALP2 )</td>
    <td>{3}</td>
  </tr>
  <tr>
    <td>stdecqavot3.va.neustar.com (SHOST-QALP3 ST )</td>
    <td>{4}</td>
  </tr>
  <tr>
    <td>stdecqavot6.va.neustar.com (SHOST-QALP3 ST )</td>
    <td>{5}</td>
  </tr>
  </tr>
  <tr>
    <td>chdecqavot1.nc.neustar.com (SHOST-QALP3 CH )</td>
    <td>{6}</td>
  </tr>
  <tr>
    <td>chdecqavot2.nc.neustar.com (SHOST-QALP3 CH )</td>
    <td>{7}</td>
  </tr>
""".format(dictionary['1'],dictionary['2'],dictionary['3'],dictionary['4'],dictionary['5'],dictionary['6'],dictionary['7'],dictionary['8'])
 
f.write(message)
f.close()

  

