package com.googoo.festivaldotcom.domain.chat.application.dto.request;

import com.googoo.festivaldotcom.domain.member.application.dto.response.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함하는 생성자 추가
public class SchedulerRoomDTO {
    private Long festivalId;
    private String festivalName;
    private Long roomId;
    private String roomName;
    private Long id;
    private Gender gender;
}