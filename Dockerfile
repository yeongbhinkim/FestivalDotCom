# 1단계: 빌드 스테이지
FROM eclipse-temurin:21 AS build

# 컨테이너 내에서 작업할 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 애플리케이션 소스 코드를 컨테이너의 /app 디렉토리로 복사합니다.
COPY . /app

# Maven Wrapper에 실행 권한을 부여합니다.
RUN chmod +x ./mvnw

# Maven을 사용해 애플리케이션을 빌드합니다. (-DskipTests 옵션 추가로 테스트 실행을 생략)
RUN ./mvnw clean package -DskipTests

# 2단계: 런타임 스테이지
FROM eclipse-temurin:21-jre AS runtime

# 빌드된 JAR 파일을 복사합니다.
# 빌드 스테이지에서 타겟 디렉토리에 빌드된 JAR 파일을 런타임 스테이지로 복사합니다.
COPY --from=build /app/target/FestivalDotCom-0.0.1-SNAPSHOT.jar /app/FestivalDotCom.jar

# 컨테이너 시작 시 실행할 명령을 지정합니다.
ENTRYPOINT ["java", "-jar", "/app/FestivalDotCom.jar"]
