package com.googoo.festivaldotcom.domain.chat.presentation;


import com.googoo.festivaldotcom.domain.chat.application.dto.request.RegisDTO;
import com.googoo.festivaldotcom.domain.chat.domain.service.FestivalRegisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/regis")
@RequiredArgsConstructor

public class ChatRegisController {


    private final FestivalRegisService festivalRegisService;

    @PostMapping("/insert")
    public ResponseEntity<String> regisInsertController (@RequestBody RegisDTO regisDTO){
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