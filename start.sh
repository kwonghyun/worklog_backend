#!/bin/bash

BUILD_JAR="/home/ubuntu/worklog_backend/build/libs/worklog-0.0.1-SNAPSHOT.jar"
JAR_FILE="/home/ubuntu/spring.jar"
#NOW=$(date +%c)
START_LOG="/home/ubuntu/worklog_backend/start.log"
#ERROR_LOG="/home/ubuntu/worklog_backend/err.log"
cp $BUILD_JAR $JAR_FILE
cd /home/ubuntu
nohup java -jar spring.jar &

