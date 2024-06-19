package com.googoo.festivaldotcom.domain.festival.infrastructure.mapper;

import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import org.mapstruct.Mapper;

@Mapper
public interface FestivalMapper {

    void insertFestival(Festival festival);

}
