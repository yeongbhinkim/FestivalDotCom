package com.googoo.festivaldotcom.domain.festivalApi.repository;

import com.googoo.festivaldotcom.domain.festivalApi.vo.FstvlData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FstvlApiRepository {
    void insertFstvlData(FstvlData fstvlData);

    List<FstvlData> getFstvlData();

    String getFestivalDescription(@Param("id") int id);
}
