package com.googoo.festivaldotcom.domain.festival.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data // Lombok 라이브러리를 사용하여 getter, setter, toString 등을 자동으로 생성
@Builder // Lombok 라이브러리를 사용하여 Builder 패턴을 자동으로 생성
public class GetFestival {
    private String festivalName; // 축제명
}
