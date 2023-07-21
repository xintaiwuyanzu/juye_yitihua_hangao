package com.dr.archive.common.filereading.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


@Mapper
public interface SelectMapper {


    @Select( "<script>" +"select id from ${tableName}"+ "</script>")
    List<Map> getIdByTableName(@Param("tableName") String tableName);
}
