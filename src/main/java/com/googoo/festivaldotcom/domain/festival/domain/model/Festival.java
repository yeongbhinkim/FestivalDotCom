package com.googoo.festivaldotcom.domain.festival.domain.model;

import com.googoo.festivaldotcom.global.base.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp; // java.sql.Timestamp 임포트

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString
public class Festival extends BaseEntity {
	private Long festivalId;
	private String festivalName;
	private String location;
	private Timestamp startAt;
	private Timestamp endAt;
	private String festivalContent;
	private String mainCorp;
	private String openCorp;
	private String supportCorp;
	private String telNumber;
	private String website;
	private String relatedInfo;
	private String roadAddress;
	private String lotAddress;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private Timestamp updateAt;
	private String providerCode;
	private String providerName;
	private String festivalImgUrl;
}
