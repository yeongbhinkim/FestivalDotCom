package com.googoo.festivaldotcom.function.festivalApi.repository;

import com.googoo.festivaldotcom.function.festivalApi.vo.FstvlData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FstvlApiRepository {
    void insertFstvlData(FstvlData fstvlData);

    List<FstvlData> getFstvlData();

    String getFestivalDescription(@Param("id") int id);
}
