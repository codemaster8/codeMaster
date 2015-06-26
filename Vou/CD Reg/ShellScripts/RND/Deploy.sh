echo "RUN SKIPPED :" $1
if $1
then
        sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@stdecopvutl3.va.neustar.com ./ok_deployer_SkipSteps.sh $2 $3 $4
else
	sshpass -p "Vertis@28" ssh -o StrictHostKeyChecking=no -t okhatav@stdecopvutl3.va.neustar.com ./ok_deployer_1.sh $2 $3 $4

fi
