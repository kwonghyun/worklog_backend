#!/bin/bash

BUILD_JAR="/home/ubuntu/worklog_backend/build/libs/*.jar"
JAR_NAME=$(basename $BUILD_JAR)

NOW=$(date +%c)
START_LOG="/home/ubuntu/worklog_backend/start.log"
ERROR_LOG="/home/ubuntu/worklog_backend/err.log"

echo "[$NOW] > build 파일명: $JAR_NAME" >>

echo "[$NOW] > build 파일 복사" >> START_LOG
DEPLOY_PATH=/home/ubuntu/worklog_backend/
cp $BUILD_JAR $DEPLOY_PATH

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
nohup java -jar $DEPLOY_JAR >> START_LOG 2>ERROR_LOG &
SERVICE_PID=$(pgrep -f $JAR_NAME)
echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG
