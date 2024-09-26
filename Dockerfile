# 베이스 이미지를 지정합니다. 여기서는 Eclipse Temurin 21 버전을 사용하여 Java 런타임 환경을 제공합니다.
FROM eclipse-temurin:21

# 빌드 중 사용할 JAR 파일의 경로를 ARG로 지정합니다. 기본값은 build/libs/FestivalDotCom-1.0.2.jar입니다.
ARG JAR_FILE=/var/jenkins_home/workspace/festivalDotCom_docke/build/libs/FestivalDotCom-1.0.2.jar

# 여러 환경 변수를 설정합니다. 이 변수들은 애플리케이션이 실행되는 동안 데이터베이스, Redis, JWT, 외부 API 설정 등에 사용됩니다.
ENV DB_URL=${DB_URL} \
    DB_USERNAME=${DB_USERNAME} \
    DB_PASSWORD=${DB_PASSWORD} \
    DB_DATABASE=${DB_DATABASE} \
    DB_ROOT_PASSWORD=${DB_ROOT_PASSWORD} \
    SLACK_WEBHOOK=${SLACK_WEBHOOK} \
    SENTRY_DSN=${SENTRY_DSN} \
    SENTRY_AUTH_TOKEN=${SENTRY_AUTH_TOKEN} \
    REDIS_HOST=${REDIS_HOST} \
    REDIS_PORT=${REDIS_PORT} \
    JWT_SECRET=${JWT_SECRET} \
    APP_BASEURL=${APP_BASEURL} \
    BASEURL=${BASEURL} \
    GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID} \
    GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET} \
    NAVER_CLIENT_ID=${NAVER_CLIENT_ID} \
    NAVER_CLIENT_SECRET=${NAVER_CLIENT_SECRET} \
    KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID} \
    KAKAO_CLIENT_SECRET=${KAKAO_CLIENT_SECRET} \
    LOG_FILE_PATH=${LOG_FILE_PATH} \
    PROFILE_IMG_URL_PATH=${PROFILE_IMG_URL_PATH} \
    PROFILE_IMG_URL_PATH_HANDLER=${PROFILE_IMG_URL_PATH_HANDLER} \
    FESTIVAL_IMG_PATH=${FESTIVAL_IMG_PATH} \
    FESTIVAL_SECRET_KEY=${FESTIVAL_SECRET_KEY} \
    STABLE_DIFFUSION_URL=${STABLE_DIFFUSION_URL} \
    MAIL_USERNAME=${MAIL_USERNAME} \
    MAIL_PASSWORD=${MAIL_PASSWORD}

# Maven을 설치합니다.
RUN apt-get update && apt-get install -y maven

# 컨테이너 내에서 작업할 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 애플리케이션 소스 코드를 컨테이너의 /app 디렉토리로 복사합니다.
COPY . /app

# Maven Wrapper에 실행 권한을 부여합니다.
RUN chmod +x ./mvnw

# Maven Wrapper를 사용해 애플리케이션을 빌드합니다.
RUN ./mvnw clean package

