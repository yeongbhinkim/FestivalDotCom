# 베이스 이미지를 지정합니다. 여기서는 Eclipse Temurin 21 버전을 사용하여 Java 런타임 환경을 제공합니다.
FROM eclipse-temurin:21

# Sentry Auth Token을 빌드 시에 전달받을 수 있게 ARG로 설정합니다.
ARG SENTRY_AUTH_TOKEN

# 필요한 패키지들을 업데이트하고 sentry-cli를 설치합니다.
RUN apt-get update && apt-get install -y curl unzip \
    && curl -sL https://sentry.io/get-cli/ | bash

# .env 파일을 컨테이너로 복사합니다.
COPY .env /app/.env

# .env 파일을 읽고 올바른 형식의 변수만 환경 변수로 설정
RUN grep -v '^#' /app/.env | grep -v '^$' | grep -E '^[a-zA-Z_][a-zA-Z0-9_]*=' | while read -r line; do export "$line"; done

# Sentry CLI를 사용하여 소스 번들을 업로드하는 명령어. ARG로 받은 SENTRY_AUTH_TOKEN 사용
RUN sentry-cli --auth-token "$SENTRY_AUTH_TOKEN" upload-sourcemaps /app/target/sourcemaps

# 빌드 중 사용할 JAR 파일의 경로를 ARG로 지정합니다. 기본값은 build/libs/FestivalDotCom-1.0.2.jar입니다.
ARG JAR_FILE=build/libs/FestivalDotCom-1.0.2.jar

# 여러 환경 변수를 설정합니다. 이 변수들은 애플리케이션이 실행되는 동안 사용됩니다.
ENV DB_URL=$DB_URL \
    DB_USERNAME=$DB_USERNAME \
    DB_PASSWORD=$DB_PASSWORD \
    DB_DATABASE=$DB_DATABASE \
    DB_ROOT_PASSWORD=$DB_ROOT_PASSWORD \
    SLACK_WEBHOOK=$SLACK_WEBHOOK \
    SENTRY_DSN=$SENTRY_DSN \
    SENTRY_AUTH_TOKEN=$SENTRY_AUTH_TOKEN \
    REDIS_HOST=$REDIS_HOST \
    REDIS_PORT=$REDIS_PORT \
    JWT_SECRET=$JWT_SECRET \
    APP_BASEURL=$APP_BASEURL \
    BASEURL=$BASEURL \
    GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID \
    GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET \
    NAVER_CLIENT_ID=$NAVER_CLIENT_ID \
    NAVER_CLIENT_SECRET=$NAVER_CLIENT_SECRET \
    KAKAO_CLIENT_ID=$KAKAO_CLIENT_ID \
    KAKAO_CLIENT_SECRET=$KAKAO_CLIENT_SECRET \
    LOG_FILE_PATH=$LOG_FILE_PATH \
    PROFILE_IMG_URL_PATH=$PROFILE_IMG_URL_PATH \
    PROFILE_IMG_URL_PATH_HANDLER=$PROFILE_IMG_URL_PATH_HANDLER \
    FESTIVAL_IMG_PATH=$FESTIVAL_IMG_PATH \
    FESTIVAL_SECRET_KEY=$FESTIVAL_SECRET_KEY \
    STABLE_DIFFUSION_URL=$STABLE_DIFFUSION_URL \
    MAIL_USERNAME=$MAIL_USERNAME \
    MAIL_PASSWORD=$MAIL_PASSWORD

# Maven을 설치
RUN apt-get update && apt-get install -y maven

# 컨테이너 내에서 작업할 디렉토리를 /app으로 설정합니다. 이후의 작업들은 이 디렉토리에서 수행됩니다.
WORKDIR /app

# 호스트 머신의 모든 파일을 컨테이너의 /app 디렉토리로 복사합니다.
COPY . /app

# Maven을 사용하여 애플리케이션을 빌드합니다.
RUN mvn clean package

# 컨테이너에서 8080 포트를 개방합니다. 이 포트는 애플리케이션이 HTTP 요청을 받을 포트입니다.
EXPOSE 8080

# 컨테이너가 시작될 때 실행될 명령어를 지정합니다. 생성된 JAR 파일을 실행하여 애플리케이션을 시작합니다.
CMD ["java", "-jar", "$JAR_FILE"]

# 운영 환경 또는 개발 환경에 맞게 프로파일을 설정할 수 있는 ENTRYPOINT입니다.
# ACTIVE_PROFILE 환경 변수에 따라 Spring Boot 프로파일을 활성화하여 실행할 수 있습니다.
# ENTRYPOINT ["java", "-Dspring.profiles.active=$ACTIVE_PROFILE", "-jar", "$JAR_FILE"]
