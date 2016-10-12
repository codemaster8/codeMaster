#!/bin/sh

echo "Starting All services from Shell"
echo "Stating xRunner Check logs/instiLog.out"
nohup ./xRunner_US.sh  >logs/instiLog.out &
echo "Stating yRunner Check logs/propLog.out"
nohup ./yRunner_US.sh  >logs/propLog.out &
echo "Stating zRunner Check logs/soapLog.out"
nohup ./zRunner_US.sh  >logs/soapLog.out &
echo "SERVICES Started !!!!!! "
