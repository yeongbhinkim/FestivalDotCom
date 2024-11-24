package com.googoo.festivaldotcom.domain.festival.infrastructure.mapper;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.SchedulerRoomDTO;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.FestivalApi;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper // MyBatis 매퍼 인터페이스임을 나타냄
public interface FestivalMapper {

    void insertFestival(FestivalApi festivalApi);

    List<Festival> selectFestival(GetFestival getFestival);

    Festival selectFestivalDetail(@Param("festivalId") Long festivalId);

    String selectFestivalUpdateAt();

    List<SchedulerRoomDTO> selectFestivalByDate(@Param("date") LocalDate date);
}
