package com.dr.archive.common.accessControl.service;

import com.dr.archive.common.accessControl.entity.DataAuthority;
import com.dr.framework.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author Mr.Zhu
 * @date 2022/5/17 - 11:32
 */
public interface DataAuthorityService extends BaseService<DataAuthority> {
   /**
    * 返回权限数据
    * @return
    */
   Map<String,List<DataAuthority>> getAllDataAuthority();

   /**
    * 判断是否重复
    * @param accessLevel
    * @param levelCode
    * @return
    */
   boolean findDataLevelCode(String accessLevel,String levelCode);
}
