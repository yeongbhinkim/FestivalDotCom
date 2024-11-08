package com.googoo.festivaldotcom.domain.festival.infrastructure.repository;

import com.googoo.festivaldotcom.domain.festival.application.dto.response.FestivalApi;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.domain.festival.domain.service.FestivalService;
import com.googoo.festivaldotcom.domain.festival.infrastructure.mapper.FestivalMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FestivalRepository implements FestivalService {

    private final FestivalMapper festivalMapper;

    @Override
    public void setFestival(FestivalApi festivalApi) {
        festivalMapper.insertFestival(festivalApi);
    }

    @Override
    public List<Festival> getFestival(GetFestival getFestival) {
        return festivalMapper.selectFestival(getFestival);
    }

    @Override
    public Festival getFestivalDetail(@Param("festivalId") Long festivalId) {
        return festivalMapper.selectFestivalDetail(festivalId);
    }

    @Override
    public String getFestivalUpdateAt() {
        return festivalMapper.selectFestivalUpdateAt();
    }

}
