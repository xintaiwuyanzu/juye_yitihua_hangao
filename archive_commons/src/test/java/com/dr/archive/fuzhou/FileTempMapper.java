package com.dr.archive.fuzhou;

import com.dr.framework.core.orm.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: caor
 * @Date: 2022-06-22 22:26
 * @Description:
 */
@Mapper
public interface FileTempMapper {
    @Select("SELECT concat(refid,':::',fileid) as a  FROM `common_file_relation`   GROUP BY a  HAVING count(a)>1")
    List<String> selectRefIds(String aa);
}
