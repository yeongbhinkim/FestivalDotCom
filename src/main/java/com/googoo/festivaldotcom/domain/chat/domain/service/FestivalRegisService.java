package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RegisDTO;
import com.googoo.festivaldotcom.domain.chat.application.dto.request.SchedulerRoomDTO;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatRoomMapper;
import com.googoo.festivaldotcom.domain.member.application.dto.response.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalRegisService {

//    @Value("${url.stable_diffusion}")
//    private String STABLE_DIFFUSION_URL;

    private final ChatRoomMapper chatRoomMapper;

    @Transactional
    public void festivalRegisInsert(RegisDTO regisDTO) {
        try {
            long rowsAffected = chatRoomMapper.insertRegistration(regisDTO);
            if (rowsAffected == 0) {
                throw new RuntimeException("등록에 실패했습니다.");
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("데이터베이스 접근 중 오류가 발생했습니다.", e);
        } catch (RuntimeException e) {
            throw new IllegalStateException("등록 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 축제 신청 중복 체크
     * @param regisDTO
     * @return
     */
    public long getRegistrationCount(RegisDTO regisDTO) {
        return chatRoomMapper.selectRegistrationCount(regisDTO);
    }

    /**
     * 축제 채팅방 생성
     */
    @Transactional
    public void createChatRoomsForTodayFestivals(SchedulerRoomDTO schedulerRoomDTO) {
        // 축제 참가자 조회
        List<SchedulerRoomDTO> applications = chatRoomMapper.findRegistrationByFestivalId(schedulerRoomDTO);

        // 참가자 성별 분류
        List<Long> maleMembers = new ArrayList<>();
        List<Long> femaleMembers = new ArrayList<>();
        for (SchedulerRoomDTO app : applications) {
            if (app.getGender() == Gender.MALE) { // Gender Enum 사용
                maleMembers.add(app.getId());
            } else if (app.getGender() == Gender.FEMALE) {
                femaleMembers.add(app.getId());
            }
        }

        // 참가자의 남녀 비율 계산
        int totalMembers = maleMembers.size() + femaleMembers.size();
        if (totalMembers == 0) return; // 참가자가 없는 경우 처리 종료

        double maleRatio = (double) maleMembers.size() / totalMembers;
        double femaleRatio = (double) femaleMembers.size() / totalMembers;

        // 한 채팅방의 인원 수 설정 (예: 4명)
        int groupSize = 4;

        // 그룹 내 남녀 비율에 따른 인원수 계산
        int maleCount = (int) Math.round(groupSize * maleRatio);
        int femaleCount = groupSize - maleCount; // 나머지는 여성

        // 남성과 여성의 인원이 부족할 경우 최소 한 명은 포함하도록 보정
        if (maleCount == 0 && !maleMembers.isEmpty()) maleCount = 1;
        if (femaleCount == 0 && !femaleMembers.isEmpty()) femaleCount = 1;

        // 비율에 맞게 채팅방 구성
        while (maleMembers.size() >= maleCount && femaleMembers.size() >= femaleCount) {
            List<Long> group = new ArrayList<>();
            group.addAll(maleMembers.subList(0, maleCount));
            group.addAll(femaleMembers.subList(0, femaleCount));

            // 채팅방 생성
            SchedulerRoomDTO chatRoom = SchedulerRoomDTO.builder()
                    .festivalId(schedulerRoomDTO.getFestivalId())
                    .roomName(schedulerRoomDTO.getFestivalName()) // 방 이름 생성
                    .build();

            chatRoomMapper.insertChatRoom(chatRoom);

            // 생성된 채팅방 ID 가져오기
            Long chatRoomId = chatRoom.getRoomId();

            // 채팅 멤버 추가
            for (Long memberId : group) {
                SchedulerRoomDTO chatMember = SchedulerRoomDTO.builder()
                        .roomId(chatRoomId)
                        .id(memberId)
                        .build();
                chatRoomMapper.insertChatMember(chatMember);
            }

            // 생성된 멤버 제거
            maleMembers = maleMembers.subList(maleCount, maleMembers.size());
            femaleMembers = femaleMembers.subList(femaleCount, femaleMembers.size());
        }
    }

    /**
     * 축제 채팅 나가기
     * @param schedulerRoomDTO
     */
    public void removeChatMember(SchedulerRoomDTO schedulerRoomDTO) {
        chatRoomMapper.deleteChatMember(schedulerRoomDTO);
    }

}