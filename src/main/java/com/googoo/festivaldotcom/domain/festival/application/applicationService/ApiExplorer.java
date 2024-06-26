package com.googoo.festivaldotcom.domain.festival.application.applicationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@Slf4j // 로깅을 위한 Lombok 어노테이션
@Service // 이 클래스가 서비스 레이어의 빈(Bean)임을 나타냅니다.
public class ApiExplorer {

    @Value("${secretKey.festival}")
    private String FESTIVAL_SECRET_KEY; // application.properties에서 'secretKey.festival' 값을 주입받습니다.

    // API로부터 데이터를 가져오는 메서드입니다.
    public String getDataFromApi() throws IOException, URISyntaxException {
        StringBuilder urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api"); // 기본 URL 설정

        // URL에 쿼리 파라미터를 추가합니다.
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=").append(FESTIVAL_SECRET_KEY); // 서비스 키 추가
        urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); // 페이지 번호 추가
        urlBuilder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("100", StandardCharsets.UTF_8)); // 한 페이지 결과 수 추가
        urlBuilder.append("&").append(URLEncoder.encode("type", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("json", StandardCharsets.UTF_8)); // 응답 형식(JSON) 추가

        
        // 테스트 데이터는 하루에 1개씩하자 스케줄러 수정해서
        // 오늘 날짜를 'yyyy-MM-dd' 형식으로 포맷합니다.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String currentDate = sdf.format(new Date());
        String currentDate = "2023-04-28";
        urlBuilder.append("&").append(URLEncoder.encode("referenceDate", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(currentDate, StandardCharsets.UTF_8)); // 데이터 기준일자 추가

        URI uri = new URI(urlBuilder.toString());
        URL url = uri.toURL();

        // HTTP 연결을 설정하고 GET 요청을 설정합니다.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        // 로깅: 요청 URL 기록
        log.info("Request URL: {}", url);

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

        // 로깅: 응답 데이터 기록
        log.info("Response data: {}", sb);

        // 응답 결과를 문자열로 반환합니다.
        return sb.toString();
    }
}
