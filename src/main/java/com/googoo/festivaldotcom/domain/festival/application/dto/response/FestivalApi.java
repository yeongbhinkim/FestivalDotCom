package com.googoo.festivaldotcom.domain.festival.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data // Lombok 라이브러리를 사용하여 getter, setter, toString 등을 자동으로 생성
@Builder // Lombok 라이브러리를 사용하여 Builder 패턴을 자동으로 생성
public class FestivalApi {
    private String fstvlNm; // 축제명
    private String opar; // 운영자
    private Date fstvlStartDate; // 축제 시작 날짜
    private Date fstvlEndDate; // 축제 종료 날짜
    private String fstvlCo; // 축제 내용
    private String mnnstNm; // 주관 기관명
    private String auspcInsttNm; // 주최 기관명
    private String suprtInsttNm; // 후원 기관명
    private String phoneNumber; // 연락처 전화번호
    private String homepageUrl; // 홈페이지 URL
    private String relateInfo; // 관련 정보
    private String rdnmadr; // 도로명 주소
    private String lnmadr; // 지번 주소
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
    private Date referenceDate; // 데이터 기준 날짜
    private String instt_code; // 제공 기관 코드
    private String instt_nm; // 제공 기관명
    private String festivalImgUrl; // 축제 이미지 URL
}
