#!/bin/bash

BUILD_JAR="/home/ubuntu/worklog_backend/build/libs/worklog-0.0.1-SNAPSHOT.jar"



nohup java -jar -Dspring.profiles.active=prod $BUILD_JAR > /home/ubuntu/worklog_backend/nohup.out 2>&1 &

