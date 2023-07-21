package com.dr.archive.formMap.service;

import com.dr.archive.formMap.bo.FormKeyMap;
import com.dr.archive.formMap.bo.FormKeyMapGroup;

import java.util.List;

/**
 * 抽象字段映射相关接口，
 *
 * @author dr
 */
public interface FormKeyMapService {
    /**
     * 获取字段映射方案分组
     *
     * @param fondCode     全宗编码
     * @param cateGoryCode 门类编码
     * @param year         年度
     * @param businessCode 业务类型
     * @return
     */
    List<FormKeyMapGroup> getFormKeyMapGroup(String fondCode, String cateGoryCode, String year, String businessCode);

    /**
     * 根据映射方案Id查询字段映射分组
     *
     * @param formGroupId
     * @return
     */
    List<FormKeyMap> getFormKeyMap(String formGroupId);

}
