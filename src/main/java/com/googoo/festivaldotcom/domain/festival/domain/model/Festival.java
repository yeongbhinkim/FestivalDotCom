package com.googoo.festivaldotcom.domain.festival.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter // Lombok 라이브러리를 사용하여 getter을 자동으로 생성
@Builder // Lombok 라이브러리를 사용하여 Builder 패턴을 자동으로 생성
public class Festival {
    private long  festivalId ; // 축제 ID
    private String festivalName; // 축제명
    private String location; // 운영자
    private Date startAt; // 축제 시작 날짜
    private Date endAt; // 축제 종료 날짜
    private String festivalContent; // 축제 내용
    private String mainCorp; // 주관 기관명
    private String openCorp; // 주최 기관명
    private String supportCorp; // 후원 기관명
    private String telNumber; // 연락처 전화번호
    private String website; // 홈페이지 URL
    private String relatedInfo; // 관련 정보
    private String roadAddress; // 도로명 주소
    private String lotAddress; // 지번 주소
    private BigDecimal latitude; // 위도
    private BigDecimal longitude; // 경도
    private Date updateAt; // 데이터 기준 날짜
    private String providerCode; // 제공 기관 코드
    private String providerName; // 제공 기관명
    private String festivalImgUrl; // 축제 이미지 URL
    private String canceled; // 취소여부
}
