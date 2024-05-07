package com.googoo.festivaldotcom.domain.festivalApi.presentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ApiExplorer {
    public String fetchFestivalData() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api");// URL
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=AIb6%2BJbInXNgcvsxF4g%2FbHygOHq1Fx6kbP2YKDz32B4K1vUIgc5tuF4dZ3Eq1FWuQkqQuOq5tLaoRB0LU0qAYw%3D%3D"); /*Service Key*/
        urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*페이지 번호*/
        urlBuilder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("100", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
        urlBuilder.append("&").append(URLEncoder.encode("type", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("xml", StandardCharsets.UTF_8)); /*XML/JSON 여부*/
        urlBuilder.append("&").append(URLEncoder.encode("fstvlNm", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*축제명*/
        urlBuilder.append("&").append(URLEncoder.encode("opar", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*개최장소*/
        urlBuilder.append("&").append(URLEncoder.encode("fstvlStartDate", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*축제시작일자*/
        urlBuilder.append("&").append(URLEncoder.encode("fstvlEndDate", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*축제종료일자*/
        urlBuilder.append("&").append(URLEncoder.encode("fstvlCo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*축제내용*/
        urlBuilder.append("&").append(URLEncoder.encode("mnnstNm", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*주관기관명*/
        urlBuilder.append("&").append(URLEncoder.encode("auspcInsttNm", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*주최기관명*/
        urlBuilder.append("&").append(URLEncoder.encode("suprtInsttNm", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*후원기관명*/
        urlBuilder.append("&").append(URLEncoder.encode("phoneNumber", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*전화번호*/
        urlBuilder.append("&").append(URLEncoder.encode("homepageUrl", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*홈페이지주소*/
        urlBuilder.append("&").append(URLEncoder.encode("relateInfo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*관련정보*/
        urlBuilder.append("&").append(URLEncoder.encode("rdnmadr", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*소재지도로명주소*/
        urlBuilder.append("&").append(URLEncoder.encode("lnmadr", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*소재지지번주소*/
        urlBuilder.append("&").append(URLEncoder.encode("latitude", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*위도*/
        urlBuilder.append("&").append(URLEncoder.encode("longitude", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*경도*/
        urlBuilder.append("&").append(URLEncoder.encode("referenceDate", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*데이터기준일자*/
        urlBuilder.append("&").append(URLEncoder.encode("instt_code", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*제공기관코드*/
        urlBuilder.append("&").append(URLEncoder.encode("instt_nm", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*제공기관기관명*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader reader;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        conn.disconnect();
        return sb.toString();
    }

    public String pm10() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth"); /*URL*/
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=AIb6%2BJbInXNgcvsxF4g%2FbHygOHq1Fx6kbP2YKDz32B4K1vUIgc5tuF4dZ3Eq1FWuQkqQuOq5tLaoRB0LU0qAYw%3D%3D"); /*Service Key*/
        urlBuilder.append("&").append(URLEncoder.encode("returnType", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("xml", StandardCharsets.UTF_8)); /*xml 또는 json*/
        urlBuilder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("100", StandardCharsets.UTF_8)); /*한 페이지 결과 수(조회 날짜로 검색 시 사용 안함)*/
        urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*페이지번호(조회 날짜로 검색 시 사용 안함)*/
        urlBuilder.append("&").append(URLEncoder.encode("searchDate", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("2024-05-07", StandardCharsets.UTF_8)); /*통보시간 검색(조회 날짜 입력이 없을 경우 한달동안 예보통보 발령 날짜의 리스트 정보를 확인)*/
        urlBuilder.append("&").append(URLEncoder.encode("InformCode", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("PM10", StandardCharsets.UTF_8)); /*통보코드검색(PM10, PM25, O3)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb.toString();
    }
}
