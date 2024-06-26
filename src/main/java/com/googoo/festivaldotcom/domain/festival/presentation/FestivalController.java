package com.googoo.festivaldotcom.domain.festival.presentation;

import com.googoo.festivaldotcom.domain.festival.application.applicationService.FestivalApplicationService;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.global.base.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j // 로깅 기능을 추가해주는 Lombok 애노테이션
@RestController // 해당 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냄
@RequestMapping("/api/v1/festival") // "/api/v1/festival" 경로로 들어오는 요청을 이 컨트롤러가 처리함
@RequiredArgsConstructor // 생성자를 자동으로 생성해주는 Lombok 애노테이션
public class FestivalController {

    // FestivalApplicationService를 주입받음
    private final FestivalApplicationService festivalApplicationService;

    /**
     * 축제 정보 조회 메서드
     * @param getFestival 조회 조건을 담은 객체
     * @return 조회된 축제 목록을 포함한 응답
     * @throws Exception 조회 중 발생할 수 있는 예외
     */
    @PostMapping // HTTP POST 요청을 처리함
    public ResponseEntity<ApiResponse<List<Festival>>> getFestival(@RequestBody GetFestival getFestival) {
        // 요청을 받았음을 로그로 기록
        log.info("Receiving request to get festivals: {}", getFestival);

        try {
            // 서비스 클래스를 이용해 축제 정보를 조회
            List<Festival> festivals = festivalApplicationService.getFestival(getFestival);
            // 조회된 축제 정보의 개수를 로그로 기록
            log.info("Retrieved {} festivals", festivals.size());

            // 조회된 축제 정보를 성공 응답으로 반환
            return ResponseEntity.ok(new ApiResponse<>(true, "Festivals retrieved successfully", festivals));
        } catch (Exception e) {
            // 조회 중 오류가 발생한 경우 오류 로그를 기록
            log.error("Error occurred while retrieving festivals", e);
            // 오류 메시지를 포함한 실패 응답을 반환
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Error occurred while retrieving festivals: " + e.getMessage(), null));
        }
    }

}
