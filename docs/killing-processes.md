# Finding & Killing Processes

It's very annoying when you're trying to run your postgres server on docker-compose but you can't start it because the port is already in use. To fix this:

## Mac OS

1. netstat -vanp tcp | grep <PORT_NUM>
2. Grab the PID of te conflicting process you want to kill
3. sudo kill -9 <PID_OF_PROCESS_TO_KILL>
