#Remote Shell Agent :

This agent listens for socket connections on the port 9000 by default. The port is customizable as shown in the run command. It receives shell commands and writes the output to the socket.

To compile :

mvn clean package

To Run :

./start-agent.sh <port> &

