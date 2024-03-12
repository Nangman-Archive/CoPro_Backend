#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/test"
JAR_FILE="co-pro-0.0.1-SNAPSHOT.jar"
JAR_PATH="$PROJECT_ROOT/$JAR_FILE"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# build 파일에서 현재 디렉토리로 .jar 파일 복사
echo "$TIME_NOW > build/libs/*.jar 파일을 $JAR_PATH 로 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $PROJECT_ROOT/

# jar 파일 실행
echo "$TIME_NOW > $JAR_PATH 파일 실행" >> $DEPLOY_LOG
cd $PROJECT_ROOT
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG < /dev/null &

# 실행된 프로세스 아이디 확인을 위해 잠시 대기
sleep 2

CURRENT_PID=$(lsof -ti tcp:9091)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
