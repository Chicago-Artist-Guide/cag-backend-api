# Docker Commands

### docker-compose up -d
Run your docker compose file in the background so you can still use the terminal for other things. You will not be able to see the logs using this command.

### docker-compose up
Run your docker compose file in your terminal and log to your terminal as well. You will no longer be able to use your terminal for other things.

### docker-compose rm -svf
Kill and remove all running containers in the docker-compose file. You must run this command in the directory that has the docker-compose file. 

### docker ps
View all running containers with their names, ids, and ports.

### docker kill {id}
Kill a single running container. You can get the container Id from "docker ps". The container you killed will still exist until you remove it using "docker rm {id}". 

### docker rm {id}
Remove a killed container. When you remove a container it is gone for good and you can start fresh the next time you run your "docker-compose up" command. This is important because sometimes when you kill a postgres container or kafka container it will not start fresh the next time you run your container and it can cause problems.
