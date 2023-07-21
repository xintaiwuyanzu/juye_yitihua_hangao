package com.dr.archive.statistics.mapper;

import com.dr.framework.core.orm.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author: caor
 * @Date: 2022-04-25 15:28
 * @Description: TODO 这里需要优化
 */
@Mapper
public interface StaticMapper {

    /**
     * 统计表但数据直接插入到临时统计表
     *
     * @param tableName
     * @return
     */
    @Select(" INSERT INTO ARCHIVE_RESOURECE_STATISTICS_TEMP ( fileArchiveNum, VINTAGES, fondCode, categoryCode )  SELECT COUNT( id ) AS fileArchiveNum, VINTAGES, FOND_CODE, CATE_GORY_CODE FROM ${tableName} GROUP BY VINTAGES, FOND_CODE, CATE_GORY_CODE ")
    void statisticsForm(@Param("tableName") String tableName);

    //清空表
    @Select(" DELETE from  ARCHIVE_RESOURECE_STATISTICS_TEMP ")
    void deleteForm(@Param("tableName") String tableName);

    //批量修改分类编码
    @Select(" update   ARCHIVE_RESOURECE_STATISTICS_TEMP set categoryCode = '${newCategoryCode}' where categoryCode= '${oldCategoryCode}'")
    void updateForm(@Param("newCategoryCode") String newCategoryCode, @Param("oldCategoryCode") String oldCategoryCode);

    //合并分类编码
    @Select(" SELECT sum(fileArchiveNum)as fileArchiveNum ,VINTAGES,fondCode,categoryCode from ARCHIVE_RESOURECE_STATISTICS_TEMP GROUP BY VINTAGES,fondCode,categoryCode ")
    void statisticsAll(@Param("tableName") String tableName);
}
