from getNodeDetails import *

try:
	QALP1 = 'DEQA1_APP/lp1db_23@stdeqp-clu1-scan.va.neustar.com:2115/stdecqpdb_stdecqa.neustar.com';
	SQLQALP1 = "select n.node_id, nr.role, n.NODE_STATUS from node n, node_role nr where n.node_oid = nr.node_oid and ORG_OID = hextoraw('C0CEC7C018DCD3D8E0401F0A059931CF')"
	FILEPATH1 = '/var/www/html/getQALP1NodeDetails.html'
	obj = nodeDetails1( FILEPATH1, QALP1, SQLQALP1 )

except IOError:
	print "Exception while running test on QALP1...."

try:
        QALP2 = 'DEQA2_APP/lp2db_23@stdeqp-clu1-scan.va.neustar.com:2115/stdecqpdb_stdecqa.neustar.com';
        SQLQALP2 = "select n.node_id, nr.role, n.NODE_STATUS from node n, node_role nr where n.node_oid = nr.node_oid and ORG_OID = hextoraw('C0CEC7C018DCD3D8E0401F0A059931CF')"
        FILEPATH2 = '/var/www/html/getQALP2NodeDetails.html'
        obj = nodeDetails1( FILEPATH2, QALP2, SQLQALP2 )
except IOError:
        print "Exception while running test on QALP2...."

