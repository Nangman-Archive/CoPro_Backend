#!/usr/bin/env bash

# 프로젝트 루트와 로그 파일 경로
PROJECT_ROOT="/home/ubuntu/test/app"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

# 현재 시간 기록
TIME_NOW=$(date +%c)

# 현재 구동 중인 애플리케이션의 PID 확인 (11091 포트 사용)
CURRENT_PID=$(lsof -ti tcp:11091)

# 프로세스가 실행 중인지 확인 후, 실행 중이면 종료
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다." >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행중인 애플리케이션 PID($CURRENT_PID) 종료." >> $DEPLOY_LOG
  kill -15 $CURRENT_PID
fi

sleep 4