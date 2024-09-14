package com.googoo.festivaldotcom.domain.member.domain.model;

import com.googoo.festivaldotcom.domain.member.application.dto.request.UpdateUserRequest;
import com.googoo.festivaldotcom.domain.member.application.dto.response.ChatPermission;
import com.googoo.festivaldotcom.domain.member.application.dto.response.Gender;
import com.googoo.festivaldotcom.global.base.domain.BaseEntity;
import com.googoo.festivaldotcom.global.utils.ValidateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;

import static lombok.AccessLevel.PROTECTED;
import static org.springframework.util.Assert.notNull;

@Getter
@NoArgsConstructor(access = PROTECTED)
@ToString
@Tag(name = "User", description = "사용자 정보 관련 도메인 모델") // Swagger의 Tag로 User 도메인 설명 추가
public class User extends BaseEntity {

	@Schema(description = "기본 자기소개 문구")
	public static final String DEFAULT_INTRODUCE = "자기소개를 작성해주세요";

	@Schema(description = "0을 나타내는 상수 값")
	public static final Integer ZERO = 0;

	@Schema(description = "매너 점수 기준치")
	public static final BigDecimal CRITERIA = new BigDecimal("0.1");

	private static final Random RANDOM = new Random();

	@Schema(description = "사용자 ID")
	private Long id;

	@Schema(description = "사용자 닉네임", example = "johnDoe123")
	private String nickName;

	@Schema(description = "프로필 이미지 URL")
	private String profileImgUrl;

	@Schema(description = "자기소개")
	private String introduction;

	@Schema(description = "OAuth 공급자 (Google, Facebook 등)", example = "google")
	private String provider;

	@Schema(description = "OAuth 사용자 ID")
	private String oauthId;

	@Schema(description = "사용자의 매너 점수", example = "36.5")
	private BigDecimal mannerScore;

	@Schema(description = "축제 참여 횟수", example = "3")
	private Integer festivalCount;

	@Schema(description = "사용자 활성화 여부")
	private boolean enabled;

	@Schema(description = "사용자의 채팅 권한")
	private ChatPermission chatPermission;

	@Schema(description = "성별", example = "MALE")
	private Gender gender;

	@Schema(description = "회사 이메일")
	private String companyEmail;

	@Builder
	protected User(
			@Schema(description = "사용자 닉네임", example = "johnDoe123") String nickName,
			@Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg") String profileImgUrl,
			@Schema(description = "OAuth 공급자", example = "google") String provider,
			@Schema(description = "OAuth 사용자 ID", example = "1234567890") String oauthId,
			@Schema(description = "회사 이메일", example = "abc@gmail.com") String companyEmail
	) {
		this.nickName = validateNickName(nickName);
		this.profileImgUrl = validateProfileImgUrl(profileImgUrl);
		this.provider = provider;
		this.oauthId = oauthId;
		this.introduction = DEFAULT_INTRODUCE;
		this.mannerScore = new BigDecimal("36.5");
		this.festivalCount = ZERO;
		this.chatPermission = chatPermission != null ? chatPermission : ChatPermission.GUEST;
		this.companyEmail = companyEmail;
	}

	private String validateNickName(String nickName) {
		ValidateUtil.checkText(nickName, "유효하지 않은 닉네임");
		ValidateUtil.checkOverLength(nickName, 100, "최대 글자수를 초과했습니다.");
		return nickName;
	}

	private String validateProfileImgUrl(String profileImgUrl) {
		ValidateUtil.checkText(profileImgUrl, "유효하지 않은 URL");
		ValidateUtil.checkOverLength(profileImgUrl, 255, "최대 글자수를 초과했습니다.");
		return profileImgUrl;
	}

	private String validateIntroduction(String introduction) {
		ValidateUtil.checkText(introduction, "유효하지 않는 자기소개");
		ValidateUtil.checkOverLength(introduction, 255, "최대 글자수를 초과했습니다.");
		return introduction;
	}

	private String validateCompanyEmail(String companyEmail) {
		ValidateUtil.checkEmail(companyEmail, "유효하지 않은 이메일 형식입니다.");
		return companyEmail;
	}

	@Operation(summary = "프로필 정보 변경", description = "사용자의 프로필 정보를 업데이트합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "프로필이 성공적으로 업데이트됨"),
			@ApiResponse(responseCode = "400", description = "잘못된 요청")
	})
	public User changeProfile(UpdateUserRequest updateUserRequest) {
		this.nickName = validateNickName(updateUserRequest.nickName());
		this.profileImgUrl = validateProfileImgUrl(updateUserRequest.profileImgUrl());
		this.introduction = validateIntroduction(updateUserRequest.introduction());
		if (updateUserRequest.companyEmail() != null) {
			this.companyEmail = validateCompanyEmail(updateUserRequest.companyEmail());
		}
		return this;
	}

	public boolean isSameUser(Long userId) {
		return Objects.equals(this.id, userId);
	}

	@Operation(summary = "매너 점수 추가", description = "사용자의 매너 점수를 추가합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "매너 점수가 성공적으로 추가됨"),
			@ApiResponse(responseCode = "400", description = "유효하지 않는 매너 온도")
	})
	public void addMannerScore(Integer mannerScore) {
		notNull(mannerScore, "유효하지 않는 매너 온도입니다.");
		BigDecimal score = CRITERIA.multiply(new BigDecimal(mannerScore));
		BigDecimal result = this.mannerScore.add(score);
		this.mannerScore = checkMinusMannerScore(result);
	}

	public BigDecimal checkMinusMannerScore(BigDecimal mannerScore) {
		BigDecimal zero = new BigDecimal("0.0");

		if (mannerScore.compareTo(zero) > 0) {
			return mannerScore;
		}
		return zero;
	}

	public BigDecimal getMannerScore() {
		return mannerScore.setScale(1, RoundingMode.HALF_UP);
	}
}
