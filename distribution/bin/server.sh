#!/bin/sh

if [[ -z "${JAVA_HOME}" ]]; then
  echo "JAVA_HOME not installed. Please install it first"
  exit 1
else
  JAVA="${JAVA_HOME}/bin/java"
fi

cd ../;

SERVER_HOME=$PWD
OPTS="-Drevolut.config.path=${SERVER_HOME}/config/server.properties"

case $1 in
    start)
        if test -f "${SERVER_HOME}"/application.pid; then
            echo "Already running";
            exit 1
        else
            "$JAVA" -jar ${OPTS} ${SERVER_HOME}/lib/server.jar &
        fi

    ;;
    stop)
        if test -f "${SERVER_HOME}"/application.pid; then
            kill -9 $(cat "${SERVER_HOME}/application.pid")
            rm -f "${SERVER_HOME}/application.pid"
        else
            echo "Server is not running"
            exit 1
        fi
    ;;
    *)
        echo "Illegal application lifecycle param. Expected {start, stop}"
    ;;
esac
