package com.googoo.festivaldotcom.domain.festival.application.applicationService;

// 필요한 라이브러리들을 import 합니다.

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service // 이 클래스가 서비스 레이어의 빈(Bean)임을 나타냅니다.
public class ApiExplorer {

    // application.properties 파일에서 'secretKey.festival'이라는 키로 값을 가져와 이 변수에 저장합니다.
    @Value("${secretKey.festival}")
    private String FESTIVAL_SECRET_KEY;

    // API로부터 데이터를 가져오는 메서드입니다.
    public String getDataFromApi() throws IOException {
        // URL을 구성하기 위해 StringBuilder를 사용합니다.
        StringBuilder urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api"); /*URL*/

        // 서비스 키를 URL에 추가합니다.
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + FESTIVAL_SECRET_KEY); /*Service Key*/

        // 페이지 번호를 URL에 추가합니다. 여기서는 1 페이지를 요청합니다.
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/

        // 한 페이지에 표시할 결과 수를 URL에 추가합니다. 여기서는 100개의 결과를 요청합니다.
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("5", "UTF-8")); /*한 페이지 결과 수*/

        // XML/JSON 여부
        urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*XML/JSON 여부*/

        // 오늘 날짜를 'yyyy-MM-dd' 형식으로 포맷합니다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());

        // 오늘 날짜를 'referenceDate' 파라미터로 URL에 추가합니다.
        urlBuilder.append("&" + URLEncoder.encode("referenceDate", "UTF-8") + "=" + URLEncoder.encode(currentDate, "UTF-8")); /*데이터기준일자*/
//        urlBuilder.append("&" + URLEncoder.encode("referenceDate", "UTF-8") + "=" + URLEncoder.encode("2024-05-30", "UTF-8")); /*데이터기준일자*/

        // 완성된 URL 문자열을 URL 객체로 변환합니다.
        URL url = new URL(urlBuilder.toString());
        // HTTP 연결을 열고 GET 요청을 설정합니다.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); // GET 요청 설정
        conn.setRequestProperty("Content-type", "application/json"); // 요청 헤더 설정

        // 응답을 읽기 위한 BufferedReader를 선언합니다.
        BufferedReader rd;
        // 응답 코드가 200에서 300 사이일 경우(정상 응답) 입력 스트림을 설정합니다.
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else { // 그렇지 않은 경우(에러 응답) 에러 스트림을 설정합니다.
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        // 응답 결과를 저장하기 위한 StringBuilder를 선언합니다.
        StringBuilder sb = new StringBuilder();
        String line;
        // 응답 결과를 한 줄씩 읽어 StringBuilder에 추가합니다.
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        // BufferedReader를 닫습니다.
        rd.close();
        // HTTP 연결을 닫습니다.
        conn.disconnect();

        // 응답 결과를 문자열로 반환합니다.
        return sb.toString();
    }
}
