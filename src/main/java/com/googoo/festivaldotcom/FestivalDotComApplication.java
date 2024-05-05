package com.googoo.festivaldotcom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class FestivalDotComApplication {

    static {
        // 현재 날짜와 시간을 포함한 로그 파일 경로 설정
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String logFileName = sdf.format(new Date(System.currentTimeMillis())) + ".log";

        // 리눅스 경로 변경 해야됨
        System.setProperty("LOG_FILE", "C:/Users/yb/Desktop/logs/" + logFileName);
    }


    private static final Logger logger = LoggerFactory.getLogger(FestivalDotComApplication.class);

    public static void main(String[] args) {

        logger.info("Starting FestivalDotComApplication...");
        SpringApplication.run(FestivalDotComApplication.class, args);
    }

}
