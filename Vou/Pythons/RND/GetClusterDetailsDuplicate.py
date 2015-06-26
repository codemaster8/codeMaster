#!/usr/bin/python
import subprocess

clusterName = {"1":"stdecqavap3.va.neustar.com", "2":"stdecqavap4.va.neustar.com", "3":"stdecqavap7.va.neustar.com",
                                "4":"stdecqavap8.va.neustar.com","5":"stdecqavap11.va.neustar.com","6":"stdecqavap12.va.neustar.com","7":"chdecqavap1.nc.neustar.com","8":"chdecqavap2.nc.neustar.com","9":"chdecqavap3.nc.neustar.com","10":"chdecqavap4.nc.neustar.com"   }
dictionary = {}
for index in range(len(clusterName)):
        index = str(index+1)
        machineName = clusterName[index]
        cluster_value = subprocess.Popen('sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@{0} grep clu1 /opt/jetty_coordinator/jetty/conf/dece.properties'.format(machineName), stdout=subprocess.PIPE, stderr=None, shell=True)
        output = cluster_value.communicate()
	dictionary[index] = output[0]
	print  dictionary[index] 

for index in range(len(dictionary)):
        index = str(index+1)
        print 'Machine Name :', dictionary[index]

f = open('/var/www/html/GetLP3Details.html','w')
		
message =  """<!DOCTYPE html>
<html>
<head>
</head>
<body>
<p id="demo">&nbsp;</p>
<script>
document.getElementById("demo").innerHTML ="Last Updated On : "+ Date();
</script>
<table border=5 style="width:75%" CELLPADDING="4" CELLSPACING="3">
  <tr style="background-color:Aqua ;">
	<th>Machine Name</th>
        <th>Pointed Cluster Name </th>
  </tr>
  <tr>
    <td>stdecqavap3.va.neustar.com (ST Co-ordinator)</td>
    <td>{0}</td>
  </tr>
  <tr>
    <td>stdecqavap4.va.neustar.com (ST Co-ordinator)</td>
    <td>{1}</td>
  </tr>
  <tr>
    <td>stdecqavap7.va.neustar.com (ST Co-ordinator)</td>
    <td>{2}</td>
  </tr>
   <tr>
    <td>stdecqavap8.va.neustar.com (ST Co-ordinator)</td>
    <td>{3}</td>
  </tr>
  <tr>
    <td>stdecqavap11.va.neustar.com (ST Co-ordinator)</td>
    <td>{4}</td>
  </tr>
   <tr>
    <td>stdecqavap12.va.neustar.com (ST Co-ordinator)</td>
    <td>{5}</td>
  </tr>
   <tr>
    <td>chdecqavap1.nc.neustar.com (CH Co-ordinator)</td>
    <td>{6}</td>
  </tr>
   <tr>
    <td>chdecqavap2.nc.neustar.com (CH Co-ordinator)</td>
    <td>{7}</td>
  </tr>
   <tr>
    <td>chdecqavap3.nc.neustar.com (CH Co-ordinator)</td>
    <td>{8}</td>
  </tr>
   <tr>
    <td>chdecqavap4.nc.neustar.com (CH Co-ordinator)</td>
    <td>{9}</td>
   </tr> """.format(dictionary['1'],dictionary['2'],dictionary['3'],
dictionary['4'],dictionary['5'],dictionary['6'],
dictionary['7'],dictionary['8'],dictionary['9'],dictionary['10'])
 
f.write(message)
f.close()

  

