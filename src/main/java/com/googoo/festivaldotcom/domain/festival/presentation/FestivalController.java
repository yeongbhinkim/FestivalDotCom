package com.googoo.festivaldotcom.domain.festival.presentation;

import com.googoo.festivaldotcom.domain.festival.application.applicationService.FestivalApplicationService;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/api/v1/festival")
@RequiredArgsConstructor
public class FestivalController {


    private final FestivalApplicationService festivalApplicationService;

    /**
     * 축제 정보 조회
     * @param getFestival
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getFestival.do", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> getFestival(@ModelAttribute("GetFestival") GetFestival getFestival)
            throws Exception {

        System.out.println("getFestival --> ");

        // 리스트 조회
        List<Festival> getFestivalList = festivalApplicationService.getFestival(getFestival);

        System.out.println("getFestivalList --> " + getFestivalList);

        return new ResponseEntity<>(getFestivalList, HttpStatus.OK); // JsonModel 객체 반환
    }

}



