# 1단계: 빌드 스테이지
# 빌드 환경에서 Eclipse Temurin 21을 사용하여 빌드합니다.
FROM eclipse-temurin:21 AS build

# 빌드 중 사용할 JAR 파일의 경로를 ARG로 지정합니다.
ARG JAR_FILE=/var/jenkins_home/workspace/festivalDotCom_docker/build/libs/FestivalDotCom-0.0.1-SNAPSHOT.jar

# 컨테이너 내에서 작업할 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 애플리케이션 소스 코드를 컨테이너의 /app 디렉토리로 복사합니다.
COPY . /app

# Maven Wrapper에 실행 권한을 부여합니다.
RUN chmod +x ./mvnw

# Maven을 사용해 애플리케이션을 빌드합니다. (-DskipTests 옵션 추가로 테스트 실행을 생략)
RUN ./mvnw clean package -DskipTests

# 2단계: 런타임 스테이지는 생략, JAR 파일을 빌드하는 단계까지만 진행
# 필요한 빌드 파일을 복사
FROM eclipse-temurin:21-jre AS runtime

# 빌드된 JAR 파일을 복사합니다.
ARG JAR_FILE=/app/target/FestivalDotCom-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} /app/FestivalDotCom.jar

