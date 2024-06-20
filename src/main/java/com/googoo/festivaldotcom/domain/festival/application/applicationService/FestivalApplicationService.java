package com.googoo.festivaldotcom.domain.festival.application.applicationService;

import com.googoo.festivaldotcom.domain.festival.application.dto.response.FestivalApi;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.domain.festival.domain.service.FestivalService;
import com.googoo.festivaldotcom.global.utils.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalApplicationService {

    // application.properties 또는 application.yml 파일에서 설정된 IMG_DIR 값을 주입받습니다.
    @Value("${attach.img_dir}")
    private String IMG_DIR;

    // FestivalService를 주입받습니다.
    private final FestivalService festivalService;

    public List<Festival> getFestival(GetFestival getFestival){

        return festivalService.getFestival(getFestival);
    }

    /**
     * JSON 문자열을 파싱하여 FestivalApi 객체로 변환하고, 이를 서비스에 전달합니다.
     *
     * @param jsonString 파싱할 JSON 문자열
     */
    public void parseAndPrintJson(String jsonString) {
        // JSON 문자열을 JSONObject로 변환합니다.
        JSONObject jsonObject = new JSONObject(jsonString);
        // response 객체를 가져옵니다.
        JSONObject response = jsonObject.getJSONObject("response");
        // body 객체를 가져옵니다.
        JSONObject body = response.getJSONObject("body");
        // items 배열을 가져옵니다.
        JSONArray items = body.getJSONArray("items");

        // 각 item 객체를 파싱합니다.
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            // 축제 이미지 URL을 가져와서 이미지 경로를 생성합니다.
            // 현재 시간 가져오기
            LocalDateTime now = LocalDateTime.now();
            // 폴더용 날짜 포맷 (년월일)
            String formattedDate = DateTimeUtil.formatDateForFolder(now);
            // 파일명용 시간 포맷 (년월일시분초)
            String formattedDateTime = DateTimeUtil.formatDateTimeForFileName(now);
            // 최종 이미지 경로 생성
            // 예시 /festivalImg/20240620/20240620123045.jpg
            String imagePath = String.format("%s/%s/%s.jpg", IMG_DIR, formattedDate, formattedDateTime);





            // FestivalApi 객체를 빌더 패턴을 사용하여 생성합니다.
            FestivalApi festivalApi = FestivalApi.builder()
                    .fstvlNm(item.optString("fstvlNm", null)) // 축제 이름
                    .opar(item.optString("opar", null)) // 운영자
                    .fstvlStartDate(DateTimeUtil.convertToDate(item.optString("fstvlStartDate", null))) // 축제 시작일
                    .fstvlEndDate(DateTimeUtil.convertToDate(item.optString("fstvlEndDate", null))) // 축제 종료일
                    .fstvlCo(item.optString("fstvlCo", null)) // 축제 내용
                    .mnnstNm(item.optString("mnnstNm", null)) // 주관기관명
                    .auspcInsttNm(item.optString("auspcInsttNm", null)) // 후원기관명
                    .suprtInsttNm(item.optString("suprtInsttNm", null)) // 지원기관명
                    .phoneNumber(item.optString("phoneNumber", null)) // 전화번호
                    .homepageUrl(item.optString("homepageUrl", null)) // 홈페이지 URL
                    .relateInfo(item.optString("relateInfo", null)) // 관련 정보
                    .rdnmadr(item.optString("rdnmadr", null)) // 도로명 주소
                    .lnmadr(item.optString("lnmadr", null)) // 지번 주소
                    .latitude(item.optString("latitude").isEmpty() ? null : new BigDecimal(item.optString("latitude", "0"))) // 위도
                    .longitude(item.optString("longitude").isEmpty() ? null : new BigDecimal(item.optString("longitude", "0"))) // 경도
                    .referenceDate(DateTimeUtil.convertToDate(item.optString("referenceDate", null))) // 참조 날짜
                    .instt_code(item.optString("instt_code", null)) // 기관 코드
                    .instt_nm(item.optString("instt_nm", null)) // 기관 이름
                    .festivalImgUrl(imagePath) // 축제 이미지 경로
                    .build();

            // FestivalService에 FestivalApi 객체를 전달합니다.
            festivalService.setFestival(festivalApi);
        }
    }


}
