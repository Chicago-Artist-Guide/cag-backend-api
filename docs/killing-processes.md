# Finding & Killing Processes

It's very annoying when you're trying to run your postgres server on docker-compose but you can't start it because the port is already in use. To fix this:

## Mac OS

1. netstat -vanp tcp | grep <PORT_NUM>
2. Grab the PID of te conflicting process you want to kill
3. sudo kill -9 <PID_OF_PROCESS_TO_KILL>

NOTE: On my Mac I had a postgres process that kept respawning after I would try and kill it so I couldn't run my docker-compose since the port was occupied. I was able to solve the problem by killing all process in one go with: **sudo pkill -u postgres**.