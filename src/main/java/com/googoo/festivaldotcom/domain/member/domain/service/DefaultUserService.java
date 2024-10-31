package com.googoo.festivaldotcom.domain.member.domain.service;


import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.UserProfileResponse;
import com.googoo.festivaldotcom.domain.member.domain.exception.UserNotFoundException;
import com.googoo.festivaldotcom.domain.member.domain.model.User;
import com.googoo.festivaldotcom.domain.member.infrastructure.mapper.UserMapper;
import com.googoo.festivaldotcom.domain.member.infrastructure.repository.UserRepository;
import com.googoo.festivaldotcom.global.auth.oauth.dto.AuthUserInfo;
import com.googoo.festivaldotcom.global.auth.oauth.dto.OAuthUserInfo;
import com.googoo.festivaldotcom.global.auth.token.service.TokenService;
import com.googoo.festivaldotcom.global.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {

    public static final String DEFAULT_ROLE = "ROLE_USER";

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Value("${attach.root_dir}")
    private String ROOT_DIR;

    @Value("${attach.handler}")
    private String HANDLER;

    /* [회원 인증 정보 조회 및 저장] 등록된 유저 정보 찾아서 제공하고 없으면 등록합니다. */
    @Override
    @Transactional
    @Cacheable(value = "User", key = "#oauthUserInfo.oauthId")
    public AuthUserInfo getOauthId(OAuthUserInfo oauthUserInfo) {
        Optional<User> optionalUser = userRepository.selectOauthId(oauthUserInfo.provider(), oauthUserInfo.oauthId());

        User user = optionalUser.orElseGet(() -> {
            // 새로운 User 객체를 생성하고 데이터베이스에 저장
            User newUser = userMapper.toUser(oauthUserInfo);
            userRepository.insertUser(newUser);
            return userRepository.selectOauthId(oauthUserInfo.provider(), oauthUserInfo.oauthId())
                    .orElseThrow(() -> new IllegalStateException("User could not be created"));
        });

        return new AuthUserInfo(user.getId(), DEFAULT_ROLE, user.getNickName());
    }


    /* [회원 조회] 사용자 ID를 통해 등록된 유저 정보 찾아서 제공하고 없으면 예외가 발생합니다. */
    @Override
//    @Cacheable(value = "User", key = "#userId")
    @CacheEvict(value = "User", key = "#userId")
    public UserProfileResponse getUser(Long userId) {
        return userRepository.selectId(userId)
                .map(userMapper::toSingleUserResponse)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public String getOauthId(Long userId) {
        return userRepository.getOauthId(userId);
    }

    /* [회원 프로필 수정] UpdateUserRequest DTO를 사용해서 사용자의 프로필(닉네임, 프로필 이미지, 자기소개)를 한번에 수정합니다. */

    @Override
    @Transactional
    @CachePut(value = "User", key = "#userId")
    public UserProfileResponse setUser(UpdateUserRequest updateUserRequest, Long userId) throws IOException {

        MultipartFile file = updateUserRequest.file();
        if (file != null && !file.isEmpty()) {
            String uploadDir = ROOT_DIR + userId;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            if (!fileExtension.matches("\\.(jpg|jpeg|png)")) {
                throw new IllegalStateException("Invalid file type.");
            }

            String fileName = UUID.randomUUID().toString() + fileExtension;
            String filePath = Paths.get(uploadDir, fileName).toString();
            File dest = new File(filePath);
            file.transferTo(dest);

            String fileUrl = HANDLER + userId + "/" + fileName;
            updateUserRequest = new UpdateUserRequest(
                    updateUserRequest.nickName(),
                    fileUrl,
                    updateUserRequest.introduction(),
                    null,
                    updateUserRequest.mannerScore(),
                    updateUserRequest.gender(),
                    updateUserRequest.companyEmail()
            );
        } else {
			// 파일이 없거나 비어있는 경우 기본 이미지 경로를 설정
			String defaultImgUrl = "/img/default.png";
			updateUserRequest = new UpdateUserRequest(
					updateUserRequest.nickName(),
					defaultImgUrl,
					updateUserRequest.introduction(),
					null,
                    updateUserRequest.mannerScore(),
                    updateUserRequest.gender(),
                    updateUserRequest.companyEmail()
			);
        }

        userRepository.updateUser(updateUserRequest.nickName(), updateUserRequest.profileImgUrl(), updateUserRequest.introduction(), updateUserRequest.gender(), updateUserRequest.companyEmail(), userId);

        return userRepository.selectId(userId)
                .map(user -> userMapper.toSingleUserResponse(user))
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /* [회원 탈퇴] 계정을 삭제합니다. soft delete가 적용됩니다.*/
    @Override
    @Transactional
//    @CacheEvict(value = "User", key = "#userId")
//    public void removeUser(HttpServletRequest request, HttpServletResponse response, Long userId, String refreshToken) {
    @Caching(evict = {
            @CacheEvict(value = "User", key = "#userId"),
            @CacheEvict(value = "User", key = "#oauthId")
    })
    public void removeUser(HttpServletRequest request, HttpServletResponse response, Long userId, String oauthId, String refreshToken) {

        userRepository.selectId(userId)
                .ifPresentOrElse(user -> {
                    tokenService.deleteRefreshToken(refreshToken);
                    userRepository.deleteUser(userId);
                    // 쿠키 제거 메서드 호출
                    CookieUtil.clearCookie(response, "accessToken");
                    CookieUtil.clearCookie(response, "refreshToken");
                    CookieUtil.clearCookie(response, "JSESSIONID");

                }, () -> {
                    throw new UserNotFoundException(userId);
                });
    }


    /**
     * 닉네임 중복체크
     * @param nickName
     * @return
     */
    public boolean getNickName(String nickName) {
        return userRepository.selectNickName(nickName);
    }
}
