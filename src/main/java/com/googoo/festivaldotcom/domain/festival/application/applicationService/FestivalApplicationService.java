package com.googoo.festivaldotcom.domain.festival.application.applicationService;

import com.googoo.festivaldotcom.domain.festival.application.dto.response.FestivalApi;
import com.googoo.festivaldotcom.domain.festival.application.dto.response.GetFestival;
import com.googoo.festivaldotcom.domain.festival.domain.model.Festival;
import com.googoo.festivaldotcom.domain.festival.domain.service.FestivalService;
import com.googoo.festivaldotcom.global.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// 로깅을 위한 어노테이션
@Slf4j
// 스프링 서비스로 등록
@Service
public class FestivalApplicationService {

    // application.properties에서 설정된 이미지 저장 경로를 자동으로 주입받음
    @Value("${attach.img_dir}")
    private String IMG_DIR;

    @Value("${url.stable_diffusion}")
    private String STABLE_DIFFUSION_URL;

    // 축제 관련 비즈니스 로직을 처리하는 서비스를 의존성 주입 받음
    private final FestivalService festivalService;

    // WebClient 인스턴스
    private final WebClient webClient;

    // 생성자에서 WebClient를 초기화합니다.
    public FestivalApplicationService(FestivalService festivalService, WebClient.Builder webClientBuilder) {
        this.festivalService = festivalService;
        this.webClient = webClientBuilder.baseUrl(STABLE_DIFFUSION_URL).build();
    }

    /**
     * 사용자로부터 입력받은 DTO를 기반으로 축제 정보를 조회하는 메서드
     * @param getFestival
     * @return
     */
    public List<Festival> getFestival(GetFestival getFestival) {
        return festivalService.getFestival(getFestival);
    }

    /**
     * 축제 상세 조회
     * @param festivalId
     * @return
     */
    public Festival getFestivalDetail(Long festivalId) {
        return festivalService.getFestivalDetail(festivalId);
    }

    /**
     * 주어진 JSON 문자열에서 축제 정보를 파싱하고 처리하는 메서드
     * @param jsonString
     * @return
     */
    public Mono<Void> parseAndProcessJson(String jsonString) {
        try {
            // JSON 문자열을 JSON 객체로 변환
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject header = response.getJSONObject("header");

            // resultCode를 확인하여 데이터가 없는 경우를 체크
            String resultCode = header.getString("resultCode");
            if ("03".equals(resultCode)) {
                // 데이터가 없는 경우 처리 로직
                System.out.println("No data available from the API.");
                return Mono.empty();
            }

            // body가 있는지 확인
            if (!response.has("body")) {
                throw new JSONException("JSONObject[\"body\"] not found.");
            }

            JSONObject body = response.getJSONObject("body");

            // items 배열이 있는지 확인
            if (!body.has("items")) {
                throw new JSONException("JSONArray[\"items\"] not found.");
            }

            JSONArray items = body.getJSONArray("items");

            // items 배열 내 각 객체를 순회하며 처리
//            return Flux.range(0, items.length())
//                    .flatMap(i -> processItem(items.getJSONObject(i)))
//                    .then(); // 모든 항목 처리가 완료되면 종료

            // 순차적으로 항목 처리
            return Flux.range(0, items.length())
                    .concatMap(i -> processItem(items.getJSONObject(i)))
                    .then();

        } catch (JSONException e) {
            // JSON 처리 중 예외 발생 시 로깅 및 Mono 에러 반환
            System.err.println("JSON processing error: " + e.getMessage());
            return Mono.error(e);
        }
    }

    /**
     * JSON 객체를 개별적으로 처리하는 메서드
     * @param item
     * @return
     */
    private Mono<Void> processItem(JSONObject item) {
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        // 폴더와 파일 이름에 사용할 날짜 형식으로 변환
        String formattedDate = DateTimeUtil.formatDateForFolder(now);
        String formattedDateTime = DateTimeUtil.formatDateTimeForFileName(now);
        // 이미지 저장 경로 생성
        String imagePath = String.format("%s/%s/%s.png", IMG_DIR, formattedDate, formattedDateTime);
        // JSON 객체에서 필요한 정보 추출
        String fstvlNm = item.optString("fstvlNm");
        String fstvlCo = item.optString("fstvlCo");
        String fstvlStartDate = item.optString("fstvlStartDate", "0000-00-00");

        // 외부 API 호출 및 데이터 저장
        return executePythonApi(formattedDate, formattedDateTime, imagePath, fstvlNm, fstvlCo, fstvlStartDate)
                .flatMap(responseData -> saveFestival(item, imagePath))
                .onErrorResume(e -> {
                    // 오류 발생 시 로그를 남기고 빈 결과 반환
                    log.error("Error processing festival item: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * 페스티벌 정보를 저장하는 메서드
     * @param item
     * @param imagePath
     * @return
     */
    private Mono<Void> saveFestival(JSONObject item, String imagePath) {
        return Mono.defer(() -> {
            // FestivalApi 객체 생성
            FestivalApi festivalApi = buildFestivalApi(item, imagePath);
            return Mono.fromRunnable(() -> {
                // 서비스에 페스티벌 정보 저장 요청
                festivalService.setFestival(festivalApi);
                log.info("Festival saved: {}", festivalApi.getFstvlNm());
            }).subscribeOn(Schedulers.boundedElastic());
        }).then();
    }

    /**
     * FestivalApi 객체를 생성하는 메서드
     * @param item
     * @param imagePath
     * @return
     */
    private FestivalApi buildFestivalApi(JSONObject item, String imagePath) {
        return FestivalApi.builder()
                .festivalImgUrl(imagePath)
                .fstvlNm(item.optString("fstvlNm"))
                .opar(item.optString("opar"))
                .fstvlStartDate(DateTimeUtil.convertToDate(item.optString("fstvlStartDate", "0000-00-00")))
                .fstvlEndDate(DateTimeUtil.convertToDate(item.optString("fstvlEndDate", "0000-00-00")))
                .fstvlCo(item.optString("fstvlCo"))
                .mnnstNm(item.optString("mnnstNm"))
                .auspcInsttNm(item.optString("auspcInsttNm"))
                .suprtInsttNm(item.optString("suprtInsttNm"))
                .phoneNumber(item.optString("phoneNumber"))
                .homepageUrl(item.optString("homepageUrl"))
                .relateInfo(item.optString("relateInfo"))
                .rdnmadr(item.optString("rdnmadr"))
                .lnmadr(item.optString("lnmadr"))
                .latitude(parseDecimal(item.optString("latitude")))
                .longitude(parseDecimal(item.optString("longitude")))
                .referenceDate(DateTimeUtil.convertToDate(item.optString("referenceDate", "0000-00-00")))
                .instt_code(item.optString("instt_code"))
                .instt_nm(item.optString("instt_nm"))
                .build();
    }

    /**
     * 문자열을 BigDecimal로 변환하는 메서드
     * @param value
     * @return
     */
    private BigDecimal parseDecimal(String value) {
        return Optional.ofNullable(value)
                .filter(v -> !v.isEmpty())
                .map(BigDecimal::new)
                .orElse(null);
    }

    /**
     * 외부 Python API를 호출하는 메서드
     * @param formattedDate
     * @param formattedDateTime
     * @param imagePath
     * @param fstvlNm
     * @param fstvlCo
     * @param fstvlStartDate
     * @return
     */
    public Mono<JSONObject> executePythonApi(String formattedDate, String formattedDateTime, String imagePath, String fstvlNm, String fstvlCo, String fstvlStartDate) {
        return webClient.post()
                .uri(STABLE_DIFFUSION_URL) // API 엔드포인트 설정
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new JSONObject()
                        .put("formattedDate", formattedDate)
                        .put("formattedDateTime", formattedDateTime)
                        .put("imagePath", imagePath)
                        .put("festivalName", fstvlNm)
                        .put("festivalContent", fstvlCo)
                        .put("fstvlStartDate", fstvlStartDate)
                        .toString())
                .retrieve()
                .bodyToMono(String.class) // 응답을 문자열로 받기
                .map(JSONObject::new) // 문자열을 JSON 객체로 변환
                .doOnError(e -> log.error("Error calling Python API: {}", e.getMessage())) // 오류 로그 기록
                .onErrorResume(e -> Mono.empty()); // 오류 발생 시 빈 결과 반환
    }
}
