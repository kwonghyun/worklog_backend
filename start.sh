#!/bin/bash

BUILD_JAR="/home/ubuntu/worklog_backend/build/libs/worklog-0.0.1-SNAPSHOT.jar"

#NOW=$(date +%c)
START_LOG="/home/ubuntu/worklog_backend/start.log"
#ERROR_LOG="/home/ubuntu/worklog_backend/err.log"

nohup java -jar $BUILD_JAR >> START_LOG

