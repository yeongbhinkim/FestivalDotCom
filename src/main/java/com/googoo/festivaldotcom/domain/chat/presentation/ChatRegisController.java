package com.googoo.festivaldotcom.domain.chat.presentation;


import com.googoo.festivaldotcom.domain.chat.application.dto.request.RegisDTO;
import com.googoo.festivaldotcom.domain.chat.domain.service.FestivalRegisService;
import com.googoo.festivaldotcom.domain.member.application.dto.response.Gender;
import com.googoo.festivaldotcom.domain.member.domain.service.DefaultUserService;
import com.googoo.festivaldotcom.global.auth.token.dto.jwt.JwtAuthentication;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/chat/regis")
@RequiredArgsConstructor

public class ChatRegisController {


    private final FestivalRegisService festivalRegisService;
    private final DefaultUserService defaultUserService;

    @PostMapping("/setRegis")
    public ResponseEntity<String> setRegis(
            @RequestBody RegisDTO regisDTO,
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal JwtAuthentication user) {

        regisDTO = RegisDTO.builder()
                .id(user.id())
                .festivalId(regisDTO.getFestivalId()) // 기존의 festivalId 값 유지
                .build();

        // 성별 선택 검증
        String gender = defaultUserService.getGender(user.id());

        if (Gender.valueOf(gender) == Gender.NONE) {
            return new ResponseEntity<>("성별을 작성해주세요.", HttpStatus.CONFLICT);
        }

        // 축제 중복 체크
        long registrationCount = festivalRegisService.getRegistrationCount(regisDTO);

        if (registrationCount > 0) {
            return new ResponseEntity<>("이미 이 축제에 신청된 상태입니다.", HttpStatus.CONFLICT);
        }

        try {
            festivalRegisService.festivalRegisInsert(regisDTO);
            return new ResponseEntity<>("신청이 성공적으로 완료되었습니다.", HttpStatus.OK);
        } catch (IllegalStateException e) {
            log.warn("신청 처리 중 문제가 발생했습니다: {}", e.getMessage());
            return new ResponseEntity<>("신청 처리 중 문제가 발생했습니다: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            log.error("시스템 오류가 발생했습니다. 다시 시도해 주세요.", e);
            return new ResponseEntity<>("시스템 오류가 발생했습니다. 다시 시도해 주세요.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}