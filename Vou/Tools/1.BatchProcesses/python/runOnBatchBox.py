import os
import pxssh
import sys
 
total = len(sys.argv)
cmdargs = str(sys.argv)
print ("Received BATCH JOB Arguement NAME : %s" % str(sys.argv[1]))



username='deceapp'
password='Kn0od!e'
hostname='10.31.152.197'
cmd_cd_batch_dir='cd ../../opt/DECE_batch/'
cmd_Exec_batch_Inact_acc='java -classpath config/:dece-batch.jar:lib/* biz.neustar.batch.util.BatchProcessLauncher ' + str(sys.argv[1])
print ("### Executing Command  : %s" % cmd_Exec_batch_Inact_acc)



s = pxssh.pxssh()
if not s.login (hostname, username, password):
        print "#### SSH login to Batch Processing Box Failed.  !!!"
        print str(s)
else:
        print "#### SSH session login successful to batch Processing Box: 10.31.152.197"
	s.sendline(cmd_cd_batch_dir)
        print "#### CD into Batch Directory : /opt/DECE_batch/  sucessfull"
	
	s.sendline(cmd_Exec_batch_Inact_acc)
	print "#### Execution of Batch Process Completed : %s"  % str(sys.argv[1])
        s.prompt()
        print s.before
        s.logout()

print "#### Exiting !"
