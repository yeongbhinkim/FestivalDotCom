package com.googoo.festivaldotcom.domain.festival.presentation;

import com.googoo.festivaldotcom.domain.festival.application.applicationService.FestivalApplicationService;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j // 로깅 기능을 추가해주는 Lombok 애노테이션
@Controller
@RequestMapping("/api/v1/festival") // "/api/v1/festival" 경로로 들어오는 요청을 이 컨트롤러가 처리함
@RequiredArgsConstructor // 생성자를 자동으로 생성해주는 Lombok 애노테이션
public class FestivalController {

    // FestivalApplicationService를 주입받음
    private final FestivalApplicationService festivalApplicationService;

    /**
     * 축제 정보 조회 메서드
     *
     * @param getFestival 조회 조건을 담은 객체
     * @return 조회된 축제 목록을 포함한 응답
     * @throws Exception 조회 중 발생할 수 있는 예외
     */
    @PostMapping(value = "/search")
    public String getFestival(
            @ModelAttribute GetFestival getFestival,
            Model model
    ) {
        List<Festival> festivals = festivalApplicationService.getFestival(getFestival);

        model.addAttribute("festivals", festivals);

        return "festival/festivalPage";
    }



    // 축제 상세 정보를 조회하는 메서드
    @GetMapping("/detail/{festivalId}")
    public String getFestivalDetail(@PathVariable Long festivalId, Model model) {
        // 축제 ID를 사용하여 상세 정보를 조회
        Festival festival = festivalApplicationService.getFestivalDetail(festivalId);

        // 모델에 축제 상세 정보를 추가
        model.addAttribute("festival", festival);

        // 상세 정보를 보여줄 페이지로 이동
        return "festival/festivalDetailPage";
    }
}
