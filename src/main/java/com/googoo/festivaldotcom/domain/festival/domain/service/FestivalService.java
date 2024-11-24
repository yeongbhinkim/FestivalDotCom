package com.googoo.festivaldotcom.domain.festival.domain.service;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.SchedulerRoomDTO;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.FestivalApi;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public interface FestivalService {

    /* 축제 등록 */
    void setFestival(FestivalApi festivalApi);

    /* 축제 조회 */
	List<Festival> getFestival(GetFestival getFestival);

    /* 축제 상세 조회 */
    Festival getFestivalDetail(@Param("festivalId") Long festivalId);

    /* 축제 최신 등록 날짜 조회 */
    String getFestivalUpdateAt();

    /* 특정 날짜의 축제 조회 */
    List<SchedulerRoomDTO> getFestivalByDate(@Param("date") LocalDate date);
}
