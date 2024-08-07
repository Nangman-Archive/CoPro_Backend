#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/test/app"
JAR_FILE="/home/ubuntu/test/co-pro-0.0.1-SNAPSHOT.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

# Pinpoint 관련 설정
PINPOINT_AGENT="/home/ubuntu/pinpoint-agent-2.5.3/pinpoint-bootstrap-2.5.3.jar"
PINPOINT_CONFIG="/home/ubuntu/pinpoint-agent-2.5.3/pinpoint-root.config"
PINPOINT_AGENT_ID="dev"  # 각 애플리케이션에 맞게 변경
PINPOINT_APP_NAME="DevApplication"  # 각 애플리케이션에 맞게 변경

TIME_NOW=$(date +%c)

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar /home/ubuntu/test

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
cd /home/ubuntu/test
nohup java -javaagent:$PINPOINT_AGENT \
     -Dpinpoint.agentId=$PINPOINT_AGENT_ID \
     -Dpinpoint.applicationName=$PINPOINT_APP_NAME \
     -Dpinpoint.config=$PINPOINT_CONFIG \
     -Dspring.profiles.active=dev \
     -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG < /dev/null &

CURRENT_PID=$(lsof -ti tcp:9091)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
