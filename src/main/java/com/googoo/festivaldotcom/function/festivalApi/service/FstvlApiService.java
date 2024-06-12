package com.googoo.festivaldotcom.function.festivalApi.service;

import com.googoo.festivaldotcom.function.festivalApi.dto.FstvlResponseDto;
import com.googoo.festivaldotcom.function.festivalApi.repository.FstvlApiRepository;
import com.googoo.festivaldotcom.function.festivalApi.vo.FstvlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FstvlApiService {
    @Autowired
    FstvlApiRepository fstvlApiRepository;

    public void saveFestivalData(FstvlResponseDto responseDto) {
        List<FstvlResponseDto.Item> items = responseDto.getResponse().getBody().getItems();
        for (FstvlResponseDto.Item item : items) {
            validateAndSetDefaults(item);
            FstvlData festival = new FstvlData();
            festival.setFstvlNm(item.getFstvlNm());
            festival.setOpar(item.getOpar());
            festival.setFstvlStartDate(String.valueOf(LocalDate.parse(item.getFstvlStartDate())));
            festival.setFstvlEndDate(String.valueOf(LocalDate.parse(item.getFstvlEndDate())));
            festival.setFstvlCo(item.getFstvlCo());
            festival.setMnnstNm(item.getMnnstNm());
            festival.setAuspcInsttNm(item.getAuspcInsttNm());
            festival.setPhoneNumber(item.getPhoneNumber());
            festival.setHomepageUrl(item.getHomepageUrl());
            festival.setRdnmadr(item.getRdnmadr());
            festival.setLatitude(item.getLatitude());
            festival.setLongitude(item.getLongitude());
            festival.setReferenceDate(String.valueOf(LocalDate.parse(item.getReferenceDate())));
            festival.setInsttCode(item.getInsttCode());
            fstvlApiRepository.insertFstvlData(festival);
        }
    }

    private void validateAndSetDefaults(FstvlResponseDto.Item data) {
        if (data.getLatitude() == null || data.getLatitude().trim().isEmpty()) {
            data.setLatitude("0.0"); // 유효하지 않은 경우 기본값 설정
        }
        if (data.getLongitude() == null || data.getLongitude().trim().isEmpty()) {
            data.setLongitude("0.0"); // 유효하지 않은 경우 기본값 설정
        }
    }

    public void getFestivalData() {
        fstvlApiRepository.getFstvlData();
    }


    public String getFestivalDescription(int id) {
        return fstvlApiRepository.getFestivalDescription(id);
    }

}
