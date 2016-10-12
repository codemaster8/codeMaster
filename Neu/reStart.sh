
echo "Cleaning Logs" 
./cleanOnce.sh
echo "Stopping all services"
./downAll.sh

echo "Starting All Services"
./up.sh

echo "" 
echo "" 
echo "Restart Complete"

echo "" 
echo "" 
echo "" 
echo ""
tail -f logs/instiLog.out 
