package com.googoo.festivaldotcom.domain.member.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ModifyForm {
  @NotBlank
  @Size(min = 2, max = 15)
  private String nickName;      //  MEMBER_NAME   VARCHAR2(15 BYTE)
  private Long id;

//  private MemberGender memberGender;    // 남 여


  private MultipartFile file;
  private String introduction;
  private String profileImgUrl;

}
