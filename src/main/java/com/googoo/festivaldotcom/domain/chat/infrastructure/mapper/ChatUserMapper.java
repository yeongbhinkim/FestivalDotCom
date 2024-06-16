package com.googoo.festivaldotcom.domain.chat.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatUserMapper {
    List<Long> selectUserGender(@Param("festivalId") Long festivalId, @Param("gender") String gender);
}