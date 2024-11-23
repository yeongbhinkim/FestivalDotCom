package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RegisDTO;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FestivalRegisService {

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

    public long getRegistrationCount(RegisDTO regisDTO) {
        return chatRoomMapper.selectRegistrationCount(regisDTO);
    }

}