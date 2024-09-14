package com.googoo.festivaldotcom.domain.festival.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data // Lombok 라이브러리를 사용하여 getter, setter, toString 등을 자동으로 생성
@Builder // Lombok 라이브러리를 사용하여 Builder 패턴을 자동으로 생성
public class GetFestival {

    @Schema(example = "축제", description = "축제명")
    private String festivalName; // 축제명
}
