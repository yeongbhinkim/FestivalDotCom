# 1단계: 빌드 스테이지
# 빌드 환경에서 Eclipse Temurin 21을 사용하여 빌드합니다.
FROM eclipse-temurin:21 AS build

# 빌드 중 사용할 JAR 파일의 경로를 ARG로 지정합니다.
ARG JAR_FILE=/var/jenkins_home/workspace/festivalDotCom_docker/build/libs/FestivalDotCom-1.0.2.jar

# 컨테이너 내에서 작업할 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 애플리케이션 소스 코드를 컨테이너의 /app 디렉토리로 복사합니다.
COPY . /app

# Maven Wrapper에 실행 권한을 부여합니다.
RUN chmod +x ./mvnw

# Maven을 사용해 애플리케이션을 빌드합니다.
RUN ./mvnw clean package

# 2단계: 런타임 스테이지
# 경량화된 Java 21 런타임 이미지를 사용합니다.
FROM eclipse-temurin:21-jre

# 빌드 단계에서 만든 JAR 파일을 복사합니다.
ARG JAR_FILE=/app/target/FestivalDotCom-1.0.2.jar
COPY --from=build ${JAR_FILE} /app/FestivalDotCom.jar

# 여러 환경 변수를 설정합니다.
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

# 애플리케이션이 실행될 디렉토리로 이동합니다.
WORKDIR /app

# 애플리케이션 실행
CMD ["java", "-jar", "/app/FestivalDotCom.jar"]
