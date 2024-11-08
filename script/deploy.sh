#!/bin/bash

# 배포 스크립트: 블루-그린 무중단 배포

set -e  # 에러 발생 시 스크립트 종료
set -o pipefail  # 파이프라인 에러 감지

# .env 파일 로드
ENV_FILE="/home/dotcom/java/.env"
if [ -f "$ENV_FILE" ]; then
    set -a
    source "$ENV_FILE"
    set +a
else
    echo ".env 파일을 찾을 수 없습니다: $ENV_FILE"
    exit 1
fi

# 변수 설정
NGINX_CONF="/home/dotcom/config/nginx.conf"  # 호스트의 NGINX 설정 파일 경로
DOCKER_COMPOSE_PATH="/home/dotcom/config/docker-compose.yml"  # Docker Compose 파일 경로
BLUE_IP="172.31.0.10"  # 블루 서비스 IP
GREEN_IP="172.31.0.11"  # 그린 서비스 IP
NGINX_CONTAINER="nginx"  # Docker Compose에서 정의한 NGINX 컨테이너 이름
LOG_FILE="/home/dotcom/deploy.log"  # 사용자 홈 디렉토리 내 로그 파일 경로
DOCKER_IMAGE="abc282v/festivaldotcom"  # Docker 이미지 경로 (예: Docker Hub 또는 개인 레지스트리)

# 로그 함수
log() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "$LOG_FILE"
}

# 충돌하는 컨테이너 제거 함수
remove_conflicting_containers() {
    local containers=("blue" "green")

    for container in "${containers[@]}"; do
        if docker ps -a --format '{{.Names}}' | grep -w "$container" > /dev/null; then
            log "충돌하는 컨테이너 발견: $container"
            docker stop "$container" || log "컨테이너 중지 실패: $container"
            docker rm "$container" || log "컨테이너 제거 실패: $container"
        else
            log "충돌하는 컨테이너 없음: $container"
        fi
    done
}

# NGINX 업스트림 가중치 업데이트 함수
update_nginx_upstream() {
    local blue_weight=$1
    local green_weight=$2

    log "NGINX 업스트림 가중치 업데이트: 블루=${blue_weight}, 그린=${green_weight}"

    sudo cp "$NGINX_CONF" "${NGINX_CONF}.bak"

    if [ "$blue_weight" -eq 0 ]; then
        sudo sed -i "s/server ${BLUE_IP}:8080 .*/server ${BLUE_IP}:8080 down;/" "$NGINX_CONF"
    else
        sudo sed -i "s/server ${BLUE_IP}:8080 .*/server ${BLUE_IP}:8080 weight=${blue_weight};/" "$NGINX_CONF"
    fi

    if [ "$green_weight" -eq 0 ]; then
        sudo sed -i "s/server ${GREEN_IP}:8080 .*/server ${GREEN_IP}:8080 down;/" "$NGINX_CONF"
    else
        sudo sed -i "s/server ${GREEN_IP}:8080 .*/server ${GREEN_IP}:8080 weight=${green_weight};/" "$NGINX_CONF"
    fi

    if ! sudo nginx -t -c "$NGINX_CONF"; then
        log "NGINX 설정 테스트 실패. 롤백을 수행합니다."
        sudo cp "${NGINX_CONF}.bak" "$NGINX_CONF"
        sudo nginx -t -c "$NGINX_CONF" && reload_nginx
        exit 1
    fi

    reload_nginx
    log "NGINX 재로드 완료."
}

# NGINX 재로드 함수
reload_nginx() {
    if docker ps --format '{{.Names}}' | grep -w "$NGINX_CONTAINER" > /dev/null; then
        log "NGINX 컨테이너 재로드 시도: $NGINX_CONTAINER"
        if docker exec "$NGINX_CONTAINER" nginx -s reload; then
            log "NGINX 컨테이너 재로드 성공."
        else
            log "NGINX 컨테이너 재로드 실패."
            exit 1
        fi
    else
        log "NGINX 컨테이너가 실행 중이지 않습니다: $NGINX_CONTAINER"
        exit 1
    fi
}

# 서비스 배포 함수
deploy_service() {
    local service=$1

    log "서비스 배포 시작: $service"

    # Docker 이미지를 풀 (latest 태그 사용)
    if ! docker pull "${DOCKER_IMAGE}:latest"; then
        log "도커 이미지 풀 실패: ${DOCKER_IMAGE}:latest"
        exit 1
    fi

    # 도커 컨테이너 업 (latest 태그 사용)
    if ! docker-compose -f "$DOCKER_COMPOSE_PATH" up -d --force-recreate "$service"; then
        log "도커 컨테이너 업 실패: $service"
        exit 1
    fi

    # 서비스 상태 확인 (최대 60초 대기)
    local retries=12
    local count=0
    while [ $count -lt $retries ]; do
        if docker-compose -f "$DOCKER_COMPOSE_PATH" ps | grep "$service" | grep "Up" > /dev/null; then
            log "서비스 실행 중: $service"
            break
        else
            log "서비스 대기 중: $service"
            sleep 5
            count=$((count + 1))
        fi
    done

    if [ $count -eq $retries ]; then
        log "서비스 실행 확인 실패: $service"
        exit 1
    fi

    log "서비스 배포 완료: $service"
}

# 메인 배포 로직
main() {
    log "블루-그린 배포 시작."

    # 배포 전에 충돌하는 컨테이너 제거
    remove_conflicting_containers

    # 1. 그린에게 트래픽 몰아주기 (블루=0, 그린=10)
    update_nginx_upstream 0 10

    # 2. 블루에 도커 이미지 배포
    deploy_service blue

    # 3. 블루 실행 확인 (deploy_service에서 이미 수행됨)

    # 4. 블루에게 트래픽 몰아주기 (블루=10, 그린=0)
    update_nginx_upstream 10 0

    # 5. 그린에 도커 이미지 배포
    deploy_service green

    # 6. 트래픽 반반 분배 (블루=5, 그린=5)
    update_nginx_upstream 5 5

    log "블루-그린 배포 완료."
}

# 스크립트 실행
main
