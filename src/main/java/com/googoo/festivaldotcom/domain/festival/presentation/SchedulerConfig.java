package com.googoo.festivaldotcom.domain.festival.presentation;

import com.googoo.festivaldotcom.domain.festival.application.applicationService.ApiExplorer;
import com.googoo.festivaldotcom.domain.festival.domain.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final ApiExplorer apiExplorer;
    private final FestivalService festivalService;


    @Scheduled(cron = "0 0 1 * * ?")
    public void fetchAndSaveFestivalData() {
        try {
            String data = apiExplorer.getDataFromApi();
            // Parse data and save to database
            // User user = parseDataToUser(data);
//            festivalService.setFestival();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}