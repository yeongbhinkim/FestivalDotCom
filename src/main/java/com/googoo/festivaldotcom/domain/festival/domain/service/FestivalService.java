package com.googoo.festivaldotcom.domain.festival.domain.service;

import com.googoo.festivaldotcom.domain.festival.application.dto.response.FestivalApi;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
