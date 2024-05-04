package com.googoo.festivaldotcom.global.infra.alarm;

import com.googoo.festivaldotcom.global.infra.alarm.dto.RequestInfo;
import jakarta.servlet.http.HttpServletRequest;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

@Aspect  // AOP를 위한 Aspect 선언
@Component  // 스프링에서 관리할 컴포넌트로 선언
@Profile(value = {"dev", "prod"})  // 개발(dev) 및 제품(prod) 프로필에서만 활성화
public class SlackNotificationAspect {
    private final SlackApi slackApi;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final Environment env;

    public SlackNotificationAspect(@Value("${spring.slack.webhook}") String webhook,
                                   ThreadPoolTaskExecutor threadPoolTaskExecutor, Environment env) {
        this.slackApi = new SlackApi(webhook);  // Slack API 초기화
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;  // 비동기 작업을 위한 ThreadPoolTaskExecutor 주입
        this.env = env;  // 환경 설정 정보 주입
    }

    @Around("@annotation(com.googoo.festivaldotcom.global.infra.alarm.annotation.Alarm) && args(request, e)")  // Alarm 어노테이션이 붙은 메소드를 포인트컷으로 삼고 HttpServletRequest와 Exception 인자를 포함
    public void slackNotificate(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request,
                                Exception e) throws Throwable {

        proceedingJoinPoint.proceed();  // 실제 메소드 실행

        RequestInfo requestInfo = new RequestInfo(request);  // 요청 정보 생성

        threadPoolTaskExecutor.execute(() -> sendSlackMessage(requestInfo, e));  // 비동기적으로 슬랙 메시지 전송
    }

    public void sendSlackMessage(RequestInfo request, Exception e) {
        SlackAttachment slackAttachment = new SlackAttachment();  // 슬랙 메시지의 Attachment 생성
        slackAttachment.setFallback("Error");  // 블록킷이 작동하지 않을 때 대체 텍스트
        slackAttachment.setColor("danger");  // Attachment의 색상 설정

        slackAttachment.setFields(
                List.of(
                        new SlackField().setTitle("Exception class").setValue(e.getClass().getCanonicalName()),  // 예외 클래스 이름
                        new SlackField().setTitle("예외 메시지").setValue(e.getMessage()),  // 예외 메시지
                        new SlackField().setTitle("Request URI").setValue(request.requestURL()),  // 요청 URI
                        new SlackField().setTitle("Request Method").setValue(request.method()),  // 요청 메소드
                        new SlackField().setTitle("요청 시간")
                                .setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))),  // 요청 시간
                        new SlackField().setTitle("Request IP").setValue(request.remoteAddress()),  // 요청자 IP
                        new SlackField().setTitle("Profile 정보").setValue(Arrays.toString(env.getActiveProfiles()))  // 활성 프로필
                )
        );

        SlackMessage slackMessage = new SlackMessage();  // 슬랙 메시지 생성
        slackMessage.setAttachments(singletonList(slackAttachment));  // 첨부파일 설정
        slackMessage.setIcon(":ghost:");  // 메시지 아이콘
        slackMessage.setText("Error Detect");  // 메시지 본문
        slackMessage.setUsername("kkiniRobot");  // 메시지를 보내는 사용자 이름

        slackApi.call(slackMessage);  // 슬랙 API를 통해 메시지 전송
    }
}
