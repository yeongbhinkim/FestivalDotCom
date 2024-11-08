package com.googoo.festivaldotcom.global.log;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Slf4j  // 로깅을 위한 Lombok 어노테이션 (@Slf4j: 로깅을 간단하게 사용하기 위해 자동으로 Logger 인스턴스를 생성)
@Configuration  // 해당 클래스가 Spring의 Bean으로 등록됨을 나타냄
public class CustomRequestLoggingFilter extends AbstractRequestLoggingFilter {

	private static final int MAX_PAYLOAD_LENGTH = 200;  // 로깅 시 최대 페이로드 길이 제한
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";  // 멀티파트 폼 데이터 형식을 위한 상수
	private static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";  // URL 인코딩된 폼 데이터 형식을 위한 상수
	private final ReentrantLock lock = new ReentrantLock(); // 파일 접근을 위한 락 추가

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		// 요청을 로깅할지 여부를 결정. 로그 레벨이 DEBUG인 경우에만 로깅
		return log.isDebugEnabled();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {

		// POST 요청일 경우 페이로드(요청 본문) 포함 설정
		if (request.getMethod().equals(POST.name())) {
			setIncludePayload(true);
		}

		// GET 요청일 경우 쿼리 문자열 포함 설정
		if (request.getMethod().equals(GET.name())) {
			setIncludeQueryString(true);
		}

		setIncludeClientInfo(true);  // 클라이언트 정보 포함 설정 (IP 주소, 세션 ID 등)
		setIncludeHeaders(true);  // 요청 헤더 포함 설정

		// 파일 접근을 직렬화하기 위해 synchronized 블록으로 보호
		lock.lock();
		try {
			super.doFilterInternal(request, response, filterChain);  // 부모 클래스의 필터 로직 실행
		} finally {
			lock.unlock();
		}
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		// 요청 처리 전에 로깅을 하지 않도록 빈 메서드로 처리
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		// 요청 처리 후 로그를 기록 (DEBUG 레벨)
		log.debug("Request ---> {}", message);

		// 이미지 리소스가 종종 로드되지 않는 문제를 해결하기 위해 추가적인 예외 로그와 파일 접근 상태를 기록
		if (request.getRequestURI().contains("profileImgUrl")) {
			log.debug("[IMAGE LOAD] Checking image resource availability for URI: {}", request.getRequestURI());
			// 파일 접근 문제를 파악하기 위한 추가적인 로깅
			HttpSession session = request.getSession(false);
			if (session != null) {
				log.debug("[SESSION INFO] Session ID: {}", session.getId());
			} else {
				log.debug("[SESSION INFO] No active session found.");
			}
			String user = request.getRemoteUser();
			if (user != null) {
				log.debug("[USER INFO] Remote User: {}", user);
			} else {
				log.debug("[USER INFO] No remote user available.");
			}
		}
	}

	@Override
	protected String getMessagePayload(HttpServletRequest request) {
		// 요청의 페이로드(본문)을 로깅하기 위해 메시지를 생성
		if (request.getContentType() != null && request.getContentType().equals(X_WWW_FORM_URLENCODED)) {
			// 폼 데이터 요청일 경우, 파라미터 맵을 문자열로 변환하여 반환
			return request.getParameterMap()
					.entrySet()
					.stream()
					.map(value -> value.getKey() + "=" + String.join("", value.getValue()))
					.collect(Collectors.joining("&"));
		}

		if (request.getContentType() != null && request.getContentType().equals(MULTIPART_FORM_DATA)) {
			// 멀티파트 데이터는 로깅하지 않음 (파일 업로드 시 보안 및 데이터 크기 문제 때문)
			return null;
		}

		ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);

		if (wrapper != null) {
			// ContentCachingRequestWrapper를 사용하여 요청 본문을 로깅할 수 있도록 함
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				lock.lock(); // 락을 사용하여 파일 접근을 직렬화함
				try {
					// 본문 데이터가 존재하면 이를 문자열로 변환하여 반환
					return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf), UTF_8)).lines()
							.collect(Collectors.joining());
				} finally {
					lock.unlock(); // 락 해제
				}
			}
		}
		return null;
	}

	@Override
	public int getMaxPayloadLength() {
		// 로깅할 페이로드의 최대 길이 반환
		return MAX_PAYLOAD_LENGTH;
	}

	@Override
	protected String createMessage(HttpServletRequest request, String prefix, String suffix) {
		// 로그 메시지를 생성하는 메서드. 요청 정보와 각종 추가 정보를 문자열로 조합
		StringBuilder msg = new StringBuilder();
		msg.append(prefix);
		msg.append(request.getMethod()).append(' ');
		msg.append(request.getRequestURI());

		if (isIncludeQueryString()) {
			appendQueryStringMessage(request, msg);  // 쿼리 문자열 추가
		}

		if (isIncludeClientInfo()) {
			appendClientInfoMessage(request, msg);  // 클라이언트 정보 추가
		}

		if (isIncludeHeaders()) {
			appendHeadersMessage(request, msg);  // 요청 헤더 정보 추가
		}

		if (isIncludePayload()) {
			appendPayloadMessage(request, msg);  // 요청 본문(페이로드) 추가
		}

		msg.append(suffix);
		return msg.toString();  // 최종 조합된 메시지 반환
	}

	private void appendQueryStringMessage(HttpServletRequest request, StringBuilder msg) {
		// 요청의 쿼리 문자열을 로그 메시지에 추가
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, String[]> parameterMap = request.getParameterMap();
		if (parameterNames.hasMoreElements()) {
			msg.append('?');
		}
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			String[] parameterValues = parameterMap.get(parameterName);
			msg.append(parameterName).append("=");

			if (parameterValues.length == 1) {
				msg.append(parameterValues[0]);
			}
			if (parameterValues.length > 1) {
				msg.append(Arrays.toString(parameterValues));
			}
			msg.append("&");
		}
		int querySeparatorIndex = msg.lastIndexOf("&");
		if (querySeparatorIndex >= 0) {
			msg.deleteCharAt(querySeparatorIndex);  // 마지막 '&' 문자 제거
		}
	}

	private void appendHeadersMessage(HttpServletRequest request, StringBuilder msg) {
		// 요청의 헤더 정보를 로그 메시지에 추가
		HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
		if (getHeaderPredicate() != null) {
			Enumeration<String> names = request.getHeaderNames();
			while (names.hasMoreElements()) {
				String header = names.nextElement();
				if (!getHeaderPredicate().test(header)) {
					headers.set(header, "masked");  // 민감한 헤더는 마스킹 처리
				}
			}
		}
		msg.append(", headers=").append(headers);  // 헤더 정보를 문자열로 추가
	}

	private void appendClientInfoMessage(HttpServletRequest request, StringBuilder msg) {
		// 클라이언트 관련 정보를 로그 메시지에 추가 (IP, 세션, 사용자 정보 등)
		String client = request.getRemoteAddr();
		if (StringUtils.hasLength(client)) {
			msg.append(", client=").append(client);
		}
		HttpSession session = request.getSession(false);
		if (session != null) {
			msg.append(", session=").append(session.getId());
		}
		String user = request.getRemoteUser();
		if (user != null) {
			msg.append(", user=").append(user);
		}
	}

	private void appendPayloadMessage(HttpServletRequest request, StringBuilder msg) {
		// 요청의 페이로드(본문)를 로그 메시지에 추가
		String payload = getMessagePayload(request);
		if (payload != null) {
			msg.append(", payload=").append(payload);
		}
	}
}
