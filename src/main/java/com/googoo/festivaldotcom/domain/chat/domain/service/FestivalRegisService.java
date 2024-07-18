package com.googoo.festivaldotcom.domain.chat.domain.service;

import com.googoo.festivaldotcom.domain.chat.application.dto.request.RegisDTO;
import com.googoo.festivaldotcom.domain.chat.infrastructure.mapper.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalRegisService {

    private final ChatRoomMapper chatRoomMapper;

    public void festivalRegisInsert(RegisDTO regisDTO) {
        chatRoomMapper.insertRegistration(regisDTO);
    }
}