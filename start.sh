#!/bin/bash

BUILD_JAR="/home/ubuntu/worklog_backend/build/libs/worklog-0.0.1-SNAPSHOT.jar"


NOW=$(date +%c)
START_LOG="/home/ubuntu/worklog_backend/start.log"
ERROR_LOG="/home/ubuntu/worklog_backend/err.log"

echo "[$NOW] > build 파일 복사" >> START_LOG

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $BUILD_JAR >> START_LOG 2>ERROR_LOG &

