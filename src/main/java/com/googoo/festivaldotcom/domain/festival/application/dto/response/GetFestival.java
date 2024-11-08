package com.googoo.festivaldotcom.domain.festival.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetFestival {

    @Schema(example = "축제", description = "축제명")
    private String festivalName;

    @Schema(example = "1", description = "페이지 번호")
    private Integer page; // Integer로 변경하여 null 허용

    @Schema(example = "10", description = "페이지 크기")
    private Integer size; // Integer로 변경하여 null 허용

    @Schema(example = "0", description = "페이지 시작 위치 (계산된 오프셋 값)")
    public int getOffset() {
        return (page != null ? page : 0) * (size != null ? size : 10); // null일 경우 기본값 사용
    }

    // page 기본값을 설정하는 메서드
    public int getPage() {
        return page != null ? page : 0;
    }

    // size 기본값을 설정하는 메서드
    public int getSize() {
        return size != null ? size : 10;
    }
}

