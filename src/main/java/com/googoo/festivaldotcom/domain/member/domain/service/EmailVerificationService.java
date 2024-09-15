package com.googoo.festivaldotcom.domain.member.domain.service;

import com.googoo.festivaldotcom.domain.member.infrastructure.repository.VerificationTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailVerificationService {

    @Value("${url.baseurl}")
    private String BASEURL;

    @Autowired
    private JavaMailSender mailSender; // 이메일 전송을 담당하는 JavaMailSender 객체
    @Autowired
    private VerificationTokenMapper tokenMapper; // MyBatis 매퍼로, 인증 토큰 관련 CRUD를 수행

    // 이메일 발송 메서드
    public void sendVerificationEmail(String recipientEmail) {
        // 인증 토큰을 생성하는 메서드 호출
        String verificationToken = generateToken();

        // 생성된 토큰을 포함한 인증 링크 생성
        String verificationLink = BASEURL + "/api/v1/user/verify?token=" + verificationToken;

        // 이메일 메시지 객체 생성
        SimpleMailMessage message = new SimpleMailMessage();

        // 수신자의 이메일 주소 설정
        message.setTo(recipientEmail);

        // 이메일 제목 설정
        message.setSubject("Email Verification");

        // 이메일 본문 설정: 인증 링크를 포함
        message.setText("Click the link to verify your email: " + verificationLink);

        // 이메일 전송
        mailSender.send(message);

        // 생성된 토큰과 이메일을 데이터베이스에 저장 (추후 검증을 위해 저장)
        tokenMapper.insertVerificationToken(recipientEmail, verificationToken);
    }

    // 인증 토큰 생성 메서드
    private String generateToken() {
        // UUID를 사용하여 고유한 토큰을 생성
        return UUID.randomUUID().toString();
    }

    // 이메일 인증 처리
    public boolean verifyEmailToken(String token) {
        // 데이터베이스에서 해당 토큰을 조회하고, 토큰이 존재하는지 확인
        return tokenMapper.findByToken(token) != null;
    }

}
