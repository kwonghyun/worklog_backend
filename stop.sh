#!/bin/bash

BUILD_JAR="/home/ubuntu/worklog_backend/build/libs/worklog-0.0.1-SNAPSHOT.jar"

STOP_LOG="/home/ubuntu/worklog_backend/stop.log"
SERVICE_PID=$(pgrep -f worklog-0.0.1-SNAPSHOT.jar) # 실행중인 Spring 서버의 PID

if [ -z "$SERVICE_PID" ]; then
  echo "서비스 NouFound" >> $STOP_LOG
else
  echo "서비스 종료 " >> $STOP_LOG
  kill "$SERVICE_PID"
  # kill -9 $SERVICE_PID # 강제 종료를 하고 싶다면 이 명령어 사용
fi
