package com.googoo.festivaldotcom.domain.festivalApi.presentation;

import com.google.gson.Gson;
import com.googoo.festivaldotcom.domain.festivalApi.dto.FstvlResponseDto;
import com.googoo.festivaldotcom.domain.festivalApi.repository.FstvlApiRepository;
import com.googoo.festivaldotcom.domain.festivalApi.service.FstvlApiService;
import com.googoo.festivaldotcom.global.log.annotation.Trace;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
//@RequestMapping("/api")
public class FstvlApiController {

    private final FstvlApiService fstvlApiService;
    private final FstvlApiRepository fstvlApiRepository;

    @RequestMapping("/banner")
    public String festivalBanner(HttpServletRequest request, Model model) throws IOException {
//        ApiExplorer apiExplorer = new ApiExplorer();
//        String s = apiExplorer.pm10();
//        System.out.println(s);

//        model.addAttribute("pm10Data", s);
        fstvlApiService.getFestivalData();
        return "fstv/homeBanner";
    }

    @RequestMapping("/fstv")
    public String festivalDataSave(HttpServletRequest request) throws IOException {
        ApiExplorer apiExplorer = new ApiExplorer();
        String s = apiExplorer.fetchFestivalData();
        Gson gson = new Gson();
        FstvlResponseDto fstvlResponseDtoList = gson.fromJson(s, FstvlResponseDto.class);
        fstvlApiService.saveFestivalData(fstvlResponseDtoList);

        return "200";
    }

    @RequestMapping("/pm10")
    public String pm10(HttpServletRequest request, Model model) throws IOException {
        ApiExplorer apiExplorer = new ApiExplorer();
        String s = apiExplorer.pm10();
        System.out.println(s);

        model.addAttribute("pm10Data", s);
        return "fstv/pm10";
    }
}
