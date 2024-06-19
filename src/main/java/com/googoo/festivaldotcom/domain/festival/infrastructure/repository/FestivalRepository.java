package com.googoo.festivaldotcom.domain.festival.infrastructure.repository;

import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.domain.festival.domain.service.FestivalService;
import com.googoo.festivaldotcom.domain.festival.infrastructure.mapper.FestivalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FestivalRepository implements FestivalService {

    private final FestivalMapper festivalMapper;

    @Override
    public void setFestival(Festival festival) {
        festivalMapper.insertFestival(festival);
    }
}
