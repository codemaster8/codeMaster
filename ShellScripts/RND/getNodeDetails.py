#!/usr/bin/python

import cx_Oracle

class nodeDetails:
        def __init__(self, htmlPath, output):
            try:
                f = open(htmlPath,'w')
                message = """<!DOCTYPE html>
                <html>
                <head>
                </head>
                <body>
                <table border=5 style="width:75%" CELLPADDING="4" CELLSPACING="3">
                <tr style="background-color:Aqua ;">
                <th> Node ID </th>
                <th> Node Role </th>
                <th> Node Status </th>
                </tr>"""
                for key in sorted(output):
                    message+= "<tr><td>{0}</td><td>{1}</td><td>{2}</td></tr>".format( key, output[key][0], output[key][1])
                    print message
                f.write(message)
            except IOError:
                print 'Getting an error while connecting to Oracle Database or Writing into files'
            finally:
                print 'Closing the html file  ...'
                f.close()
##################################### FOR QALP1 ################################
try:
        connection = cx_Oracle.connect('DEQA1_APP/lp1db_23@stdeqp-clu1-scan.va.neustar.com:2115/stdecqpdb_stdecqa.neustar.com')
        SQL = "select node.node_id, node_role.role, node.NODE_STATUS from node , node_role where node.node_oid = node_role.node_oid and ORG_OID in ( select org_oid from ORGANIZATION_DISPLAY_NAME where display_name = 'ISC_R1')" 
	cursor = connection.cursor()
        cursor.execute(SQL)
        dictonary  = {}
        for row in cursor:
                dictonary[row[0]] = [row[1],row[2]]
        print dictonary
        obj = nodeDetails("/var/www/html/getQALP1NodeDetails.html", dictonary)

except IOError:
        print 'Closing the connection ...'
        connection.close()
##################################### FOR QALP2 ################################

try:
        connection = cx_Oracle.connect('DEQA2_APP/lp2db_23@stdeqp-clu1-scan.va.neustar.com:2115/stdecqpdb_stdecqa.neustar.com')
        SQL = "select node.node_id, node_role.role, node.NODE_STATUS from node , node_role where node.node_oid = node_role.node_oid and ORG_OID in ( select org_oid from ORGANIZATION_DISPLAY_NAME where display_name = 'ISC_R1')"
        cursor = connection.cursor()
        cursor.execute(SQL)
        dictonary  = {}
        for row in cursor:
                dictonary[row[0]] = [row[1],row[2]]
        print dictonary
        obj = nodeDetails("/var/www/html/getQALP2NodeDetails.html", dictonary)

except IOError:
        print 'Closing the connection ...'
        connection.close()
##################################### FOR QALP3 ST Cluster 1 #############################

try:
        connection = cx_Oracle.connect('deadm_app/lp3db_23@stdeqp-clu1-scan.va.neustar.com:2115/stdecqlp.neustar.com')
        SQL = "select node.node_id, node_role.role, node.NODE_STATUS from node , node_role where node.node_oid = node_role.node_oid and ORG_OID in ( select org_oid from ORGANIZATION_DISPLAY_NAME where display_name = 'ISC_R1')"
        cursor = connection.cursor()
        cursor.execute(SQL)
        dictonary  = {}
        for row in cursor:
                dictonary[row[0]] = [row[1],row[2]]
        print dictonary
        obj = nodeDetails("/var/www/html/getQALP3STCluster1NodeDetails.html", dictonary)

except IOError:
        print 'Closing the connection ...'
        connection.close()
##################################### FOR QALP3 ST Cluster 2 #############################

try:
        connection = cx_Oracle.connect('deadm_app/lp3db_23@stdeqp-clu2-scan.va.neustar.com:2115/stdecqlp.neustar.com')
	SQL = "select node.node_id, node_role.role, node.NODE_STATUS from node , node_role where node.node_oid = node_role.node_oid and ORG_OID in ( select org_oid from ORGANIZATION_DISPLAY_NAME where display_name = 'ISC_R1')"
        cursor = connection.cursor()
        cursor.execute(SQL)
        dictonary  = {}
        for row in cursor:
                dictonary[row[0]] = [row[1],row[2]]
        print dictonary
        obj = nodeDetails("/var/www/html/getQALP3STCluster2NodeDetails.html", dictonary)

except IOError:
        print 'Closing the connection ...'
        connection.close()
##################################### FOR QALP3 CH Cluster 1  #############################

try:
        connection = cx_Oracle.connect('deadm_app/lp3db_23@chdeqp-clu1-scan.nc.neustar.com:2115/chdecqlp.neustar.com')
        SQL = "select node.node_id, node_role.role, node.NODE_STATUS from node , node_role where node.node_oid = node_role.node_oid and ORG_OID in ( select org_oid from ORGANIZATION_DISPLAY_NAME where display_name = 'ISC_R1')"
        cursor = connection.cursor()
        cursor.execute(SQL)
        dictonary  = {}
        for row in cursor:
                dictonary[row[0]] = [row[1],row[2]]
        print dictonary
        obj = nodeDetails("/var/www/html/getQALP3CHCluster1NodeDetails.html", dictonary)

except IOError:
        print 'Closing the connection ...'
        connection.close()
##################################### FOR QALP3 CH Cluster 2  #############################

try:
        connection = cx_Oracle.connect('deadm_app/lp3db_23@chdeqp-clu2-scan.nc.neustar.com:2115/chdecqlp.neustar.com')
        SQL = "select node.node_id, node_role.role, node.NODE_STATUS from node , node_role where node.node_oid = node_role.node_oid and ORG_OID in ( select org_oid from ORGANIZATION_DISPLAY_NAME where display_name = 'ISC_R1')"
        cursor = connection.cursor()
        cursor.execute(SQL)
        dictonary  = {}
        for row in cursor:
                dictonary[row[0]] = [row[1],row[2]]
        print dictonary
        obj = nodeDetails("/var/www/html/getQALP3CHCluster2NodeDetails.html", dictonary)

except IOError:
        print 'Closing the connection ...'
        connection.close()

