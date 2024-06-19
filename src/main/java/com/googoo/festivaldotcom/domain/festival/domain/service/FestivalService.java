package com.googoo.festivaldotcom.domain.festival.domain.service;

import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface FestivalService {

	/* 축제 등록 */
	void setFestival(Festival festival);


}
