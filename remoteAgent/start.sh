#!/bin/sh

dirpath=$(dirname $0);

if [ "$#" -ne 1 ]; then
    echo "Illegal number of parameters. Usage : /bin/sh start.sh <port>";
    exit 1;
fi

port=$1

cmd="java -cp $dirpath/target/shell-agent-0.0.1-jar-with-dependencies.jar com.playgames.remote.Agent $port"

echo "Executing $cmd";

`$cmd &`

exit 0
