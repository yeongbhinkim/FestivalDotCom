package com.googoo.festivaldotcom.domain.festival.presentation;

import com.googoo.festivaldotcom.domain.festival.application.applicationService.ApiExplorer;
import com.googoo.festivaldotcom.domain.festival.application.applicationService.FestivalApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j // 로깅을 위한 Lombok 어노테이션
@Configuration // 스프링의 설정 클래스를 나타내는 어노테이션
@EnableScheduling // 스케줄링을 활성화하는 어노테이션
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성
public class SchedulerConfig {

    private final ApiExplorer apiExplorer; // API에서 데이터를 가져오는 역할
    private final FestivalApplicationService festivalApplicationService; // 데이터를 파싱하고 저장하는 역할

    /**
     * 스케줄러 메서드. 매일 새벽 1시에 실행됩니다.
     */
    //모델 변경후 적용
//    @Scheduled(cron = "0 0 1 * * ?") // cron 표현식을 사용하여 매일 새벽 1시에 작업을 실행하도록 설정
//    @Scheduled(cron = "0 31 21 * * *")
    public void fetchAndSaveFestivalData() {
        log.info("Fetching festival data from API...");
        try {
            String referenceDate = festivalApplicationService.getFestivalUpdateAt();

            // API에서 데이터를 가져옴
            String data = apiExplorer.getDataFromApi(referenceDate);
            log.info("Fetched data from API.");

            // 가져온 데이터를 파싱하고 저장하는 메서드를 호출
            festivalApplicationService.parseAndProcessJson(data)
                    .doOnSuccess(aVoid -> log.info("Successfully processed festival data."))
                    .doOnError(e -> log.error("Error processing festival data: {}", e.getMessage()))
                    .subscribe();
        } catch (IOException e) {
            // 예외가 발생하면 로그에 예외 메시지를 기록
            log.error("Error fetching data from API: {}", e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

