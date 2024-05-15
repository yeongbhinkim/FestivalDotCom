package com.googoo.festivaldotcom.domain.festivalApi.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FstvlData {
    private int fstvlId;
    private String fstvlNm;
    private String opar;
    private String fstvlStartDate;
    private String fstvlEndDate;
    private String fstvlCo;
    private String mnnstNm;
    private String auspcInsttNm;
    private String suprtInsttNm;
    private String phoneNumber;
    private String homepageUrl;
    private String relateInfo;
    private String rdnmadr;
    private String lnmadr;
    private String latitude;
    private String longitude;
    private String referenceDate;
    private String insttCode;
}
