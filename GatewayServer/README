#GatewayServer

This is a play 2.2 project. 

API Description :
Below is the REST API for running a command on the shell of a remote server (target).

URL : http://<shell-server-ip>:9005/run
HTTP Request type : POST
Request Sample :
	{
		"target":"127.0.0.1",
		"command":"pwd"
	}
Success Response : 
	{
		"status": "SUCCESS"
		"output": "/home/amulya/playgames/shell-remote-agent/shell-agent"
	}
Error Response :
	{
		"status": "FAILURE"
		"error": "Missing target in the request"
	}

Pre-requisites :
1. Java 7
2. Play 2.2 framework
3. Add "port.<ip>=<shell-agent-port>" in the conf/application.conf for enabling connectivity between server and the remote agent.
   Example : port.127.0.0.1=9002

To compile :
play compile

To run on port 9005:
play run 9005
