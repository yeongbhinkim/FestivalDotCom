package com.googoo.festivaldotcom;

import io.github.cdimascio.dotenv.Dotenv;
import io.sentry.Sentry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SpringBootApplication
public class FestivalDotComApplication {

    static {
        // .env 파일에서 환경 변수 로드
        Dotenv dotenv = Dotenv.load();
        String logFilePath = dotenv.get("LOG_FILE_PATH");

        // 현재 날짜와 시간을 포함한 로그 파일 경로 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String logFileName = sdf.format(new Date(System.currentTimeMillis())) + ".log";

        // 로그 파일 경로 설정
        System.setProperty("LOG_FILE", logFilePath + logFileName);
    }

    public static void main(String[] args) {
        SpringApplication.run(FestivalDotComApplication.class, args);

    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Locale.setDefault(Locale.KOREA);
    }
}
