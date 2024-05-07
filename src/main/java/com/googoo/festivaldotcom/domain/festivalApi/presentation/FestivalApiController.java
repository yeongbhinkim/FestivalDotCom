package com.googoo.festivaldotcom.domain.festivalApi.presentation;

import com.googoo.festivaldotcom.global.log.annotation.Trace;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
//@RequestMapping("/api")
public class FestivalApiController {
    @Trace
    @RequestMapping("/fstv")
    public String fstv(HttpServletRequest request, Model model) throws IOException {
        ApiExplorer apiExplorer = new ApiExplorer();
        String s = apiExplorer.fetchFestivalData();
        System.out.println(s);

        model.addAttribute("festivalData", s);
        return "apiData/homeBanner";
    }

    @Trace
    @RequestMapping("/pm10")
    public String pm10(HttpServletRequest request, Model model) throws IOException {
        ApiExplorer apiExplorer = new ApiExplorer();
        String s = apiExplorer.pm10();
        System.out.println(s);

        model.addAttribute("pm10Data", s);
        return "apiData/pm10";
    }
}
