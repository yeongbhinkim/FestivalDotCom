package com.googoo.festivaldotcom.domain.festival.presentation;

import com.googoo.festivaldotcom.domain.festival.application.applicationService.FestivalApplicationService;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Festival API", description = "축제 정보를 관리하는 API")
public class FestivalController {

    private final FestivalApplicationService festivalApplicationService;

    /**
     * 축제 정보 조회 메서드
     *
     * @param getFestival 조회 조건을 담은 객체
     * @return 조회된 축제 목록을 포함한 응답
     * @throws Exception 조회 중 발생할 수 있는 예외
     */
    @Operation(summary = "축제 목록 조회", description = "입력한 조건에 맞는 축제 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 축제 목록을 반환함"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping(value = "/search")
    public String getFestival(
            @Parameter(description = "조회 조건을 담은 객체") @ModelAttribute GetFestival getFestival,
            Model model
    ) {

        log.info("search = {}", "search() 호출됨");

        List<Festival> festivals = festivalApplicationService.getFestival(getFestival);
        model.addAttribute("festivals", festivals);
        return "festival/festivalPage";
    }

    @Operation(summary = "축제 목록 조회", description = "입력한 조건에 맞는 축제 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 축제 목록을 반환함"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping(value = "/searchScroll", produces = "application/json")
    @ResponseBody // JSON 형식으로 반환
    public List<Festival> getFestivalSearchScroll(
            @Parameter(description = "조회 조건을 담은 객체") @ModelAttribute GetFestival getFestival
    ) {
        log.info("searchScroll = {}", "searchScroll() 호출됨");

        return festivalApplicationService.getFestival(getFestival);
    }

    /**
     * 축제 상세 정보 조회 메서드
     *
     * @param festivalId 축제 ID
     * @param model      모델 객체
     * @return 축제 상세 정보를 포함한 페이지
     */
    @Operation(summary = "축제 상세 조회", description = "해당 ID에 해당하는 축제의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 축제 상세 정보를 반환함"),
            @ApiResponse(responseCode = "404", description = "해당 ID의 축제가 존재하지 않음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/detail/{festivalId}")
    public String getFestivalDetail(
            @Parameter(description = "조회할 축제의 ID", example = "1") @PathVariable Long festivalId,
            Model model
    ) {
        log.info("detail = {}", "detail()호출됨");
        Festival festival = festivalApplicationService.getFestivalDetail(festivalId);
        model.addAttribute("festival", festival);
        return "festival/festivalDetailPage";
    }
}
