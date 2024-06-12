package com.googoo.festivaldotcom.function.festivalApi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FstvlResponseDto {
    private Response response;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private Header header;
        private Body body;

        public Header getHeader() {
            return header;
        }

        public void setHeader(Header header) {
            this.header = header;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        private String resultCode;
        private String resultMsg;
        private String type;

    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Body {
        private List<Item> items;

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
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

        // Getter and Setter methods for each field
    }
}
