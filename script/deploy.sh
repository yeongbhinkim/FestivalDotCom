#!/bin/bash

# 블루-그린 배포를 위한 설정
BLUE_IP="172.31.0.10"
GREEN_IP="172.31.0.11"

# 데이터베이스와 Redis 컨테이너가 실행 중인지 확인합니다.
if [ $(docker ps | grep -c "db") -eq 0 ]; then
  echo "### Starting database ###"
  docker-compose up -d db
else
  echo "Database is already running"
fi

if [ $(docker ps | grep -c "redis") -eq 0 ]; then
  echo "### Starting redis ###"
  docker-compose up -d redis
else
  echo "Redis is already running"
fi

# 현재 활성화된 컨테이너 확인
IS_GREEN=$(docker ps | grep -c "green")
IS_BLUE=$(docker ps | grep -c "blue")

# Blue 컨테이너가 실행 중이지 않은 경우 (즉, Green이 실행 중일 때)
if [ "$IS_BLUE" -eq 0 ]; then
  echo "### Deploying Blue ###"

  # Blue 이미지 Pull 및 실행
  docker-compose pull blue
  docker-compose up -d blue

  # Blue 컨테이너 헬스 체크
  while true; do
    REQUEST=$(curl -s -o /dev/null -w "%{http_code}" http://$BLUE_IP:8080)
    if [ "$REQUEST" -eq 200 ]; then
      echo "Blue health check successful"
      break
    fi
    sleep 3
  done

  # Nginx 설정 변경 - Blue 활성화
  echo "Adjusting Nginx to point to Blue"
  sudo sed -i "s/server $GREEN_IP:8080 weight=5;/server $GREEN_IP:8080 weight=0;/" /etc/nginx/nginx.conf
  sudo sed -i "s/server $BLUE_IP:8080 weight=0;/server $BLUE_IP:8080 weight=5;/" /etc/nginx/nginx.conf

  # Nginx 리로드
  echo "Reloading Nginx configuration"
  sudo nginx -s reload

  # Green 컨테이너 중지
  echo "Stopping Green container"
  docker-compose stop green

# Green 컨테이너가 실행 중이지 않은 경우 (즉, Blue가 실행 중일 때)
elif [ "$IS_GREEN" -eq 0 ]; then
  echo "### Deploying Green ###"

  # Green 이미지 Pull 및 실행
  docker-compose pull green
  docker-compose up -d green

  # Green 컨테이너 헬스 체크
  while true; do
    REQUEST=$(curl -s -o /dev/null -w "%{http_code}" http://$GREEN_IP:8080)
    if [ "$REQUEST" -eq 200 ]; then
      echo "Green health check successful"
      break
    fi
    sleep 3
  done

  # Nginx 설정 변경 - Green 활성화
  echo "Adjusting Nginx to point to Green"
  sudo sed -i "s/server $BLUE_IP:8080 weight=5;/server $BLUE_IP:8080 weight=0;/" /etc/nginx/nginx.conf
  sudo sed -i "s/server $GREEN_IP:8080 weight=0;/server $GREEN_IP:8080 weight=5;/" /etc/nginx/nginx.conf

  # Nginx 리로드
  echo "Reloading Nginx configuration"
  sudo nginx -s reload

  # Blue 컨테이너 중지
  echo "Stopping Blue container"
  docker-compose stop blue
fi

# 두 컨테이너가 모두 배포된 후 로드 밸런싱
echo "### Setting up load balancing ###"

# 현재 설정된 weight 값을 블루와 그린에 대해 5:5로 조정
sudo sed -i "s/server $BLUE_IP:8080 weight=[0-9]*/server $BLUE_IP:8080 weight=5;/" /etc/nginx/nginx.conf
sudo sed -i "s/server $GREEN_IP:8080 weight=[0-9]*/server $GREEN_IP:8080 weight=5;/" /etc/nginx/nginx.conf

# Nginx 리로드
echo "Reloading Nginx configuration for load balancing"
sudo nginx -s reload

echo "Deployment completed successfully!"
