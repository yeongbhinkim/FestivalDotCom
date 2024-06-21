package com.googoo.festivaldotcom.domain.festival.presentation;

import com.googoo.festivaldotcom.domain.festival.application.applicationService.ApiExplorer;
import com.googoo.festivaldotcom.domain.festival.application.applicationService.FestivalApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration // 스프링의 설정 클래스를 나타내는 어노테이션입니다.
@EnableScheduling // 스케줄링을 활성화하는 어노테이션입니다.
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성해줍니다.
public class SchedulerConfig {

    // ApiExplorer를 주입받습니다. ApiExplorer는 API에서 데이터를 가져오는 역할을 합니다.
    private final ApiExplorer apiExplorer;

    // FestivalApplicationService를 주입받습니다. 이 서비스는 데이터를 파싱하고 저장하는 역할을 합니다.
    private final FestivalApplicationService festivalApplicationService;

    // FilterRegistrationBean을 주입받습니다. (여기서는 Sentry와 관련된 필터인 것으로 보입니다.)
    private final FilterRegistrationBean sentryUserFilter;

    /**
     * 스케줄러 메서드입니다. cron 표현식을 사용하여 매일 새벽 1시에 실행됩니다.
     * API에서 데이터를 가져와서 파싱하고 저장합니다.
     */
    @Scheduled(cron = "0 0 1 * * ?") // cron 표현식을 사용하여 매일 새벽 1시에 작업을 실행하도록 설정합니다.
    // 이 메서드는 5초마다 실행됩니다.
//    @Scheduled(cron = "*/10 * * * * *")
    public void fetchAndSaveFestivalData() {
        try {
            // API에서 데이터를 가져옵니다.
            String data = apiExplorer.getDataFromApi();

            // 가져온 데이터를 콘솔에 출력합니다. (디버깅 목적)
//            System.out.println("Fetched Data: " + data);

            // 데이터를 파싱하고 저장하는 메서드를 호출합니다.
            festivalApplicationService.parseAndPrintJson(data);
        } catch (IOException e) {
            // 예외가 발생하면 스택 트레이스를 출력합니다.
            e.printStackTrace();
        }
    }
}
