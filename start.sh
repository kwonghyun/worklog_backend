#!/bin/bash

BUILD_JAR="/home/ubuntu/worklog_backend/build/libs/worklog-0.0.1-SNAPSHOT.jar"
JAR_FILE="/home/worklog_backend/spring.jar"
#NOW=$(date +%c)
START_LOG="/home/ubuntu/worklog_backend/start.log"
#ERROR_LOG="/home/ubuntu/worklog_backend/err.log"
cp $BUILD_JAR $JAR_FILE
nohup java -jar $JAR_FILE >> START_LOG

